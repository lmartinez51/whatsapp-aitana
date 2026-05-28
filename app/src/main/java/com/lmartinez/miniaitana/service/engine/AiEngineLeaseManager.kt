package com.lmartinez.miniaitana.service.engine

import android.content.Context
import android.util.Log
import com.lmartinez.miniaitana.core.domain.repository.ConfigRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.runBlocking
import java.io.Closeable
import java.io.File
import java.io.RandomAccessFile
import java.nio.channels.FileChannel
import java.nio.channels.FileLock
import java.nio.channels.OverlappingFileLockException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AiEngineLeaseManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val configRepository: ConfigRepository
) {
    private val TAG = "AiEngineLease"
    private val LOCK_FILE_NAME = "aitana_ai_engine.lock"
    private val DEFAULT_POLL_MS = 100L

    inner class Lease internal constructor(
        val owner: String,
        private val lockFile: File,
        private val randomAccessFile: RandomAccessFile,
        private val channel: FileChannel,
        private val lock: FileLock
    ) : Closeable {
        @Volatile
        private var closed = false

        val isValid: Boolean
            get() = !closed && lock.isValid

        fun heartbeat() {
            if (isValid) {
                runBlocking {
                    configRepository.setEngineOwner(owner)
                }
            }
        }

        override fun close() {
            if (closed) return
            closed = true

            try {
                if (lock.isValid) lock.release()
            } catch (e: Exception) {
                Log.w(TAG, "Failed to release file lock for $owner: ${e.message}")
            }

            try {
                channel.close()
            } catch (_: Exception) {
            }

            try {
                randomAccessFile.close()
            } catch (_: Exception) {
            }

            runBlocking {
                configRepository.clearEngineOwner()
            }
            Log.i(TAG, "Released AI engine lease for $owner: ${lockFile.name}")
        }
    }

    fun acquire(owner: String): Lease? {
        val lockFile = File(context.filesDir, LOCK_FILE_NAME)
        lockFile.parentFile?.mkdirs()

        val randomAccessFile = RandomAccessFile(lockFile, "rw")
        val channel = randomAccessFile.channel
        val lock = try {
            channel.tryLock()
        } catch (_: OverlappingFileLockException) {
            null
        } catch (e: Exception) {
            Log.w(TAG, "Failed to acquire AI engine lock for $owner: ${e.message}")
            null
        }

        if (lock == null) {
            try {
                channel.close()
            } catch (_: Exception) {
            }
            try {
                randomAccessFile.close()
            } catch (_: Exception) {
            }
            return null
        }

        randomAccessFile.setLength(0)
        randomAccessFile.write(
            "owner=$owner\nsince=${System.currentTimeMillis()}\n".toByteArray()
        )
        randomAccessFile.fd.sync()

        runBlocking {
            configRepository.setEngineOwner(owner)
        }
        Log.i(TAG, "Acquired AI engine lease for $owner: ${lockFile.name}")
        return Lease(owner, lockFile, randomAccessFile, channel, lock)
    }

    fun acquireBlocking(owner: String, timeoutMs: Long): Lease? {
        val deadline = if (timeoutMs <= 0L) {
            System.currentTimeMillis()
        } else {
            System.currentTimeMillis() + timeoutMs
        }

        do {
            acquire(owner)?.let { return it }
            if (timeoutMs <= 0L) return null
            Thread.sleep(DEFAULT_POLL_MS)
        } while (System.currentTimeMillis() < deadline)

        return null
    }
}
