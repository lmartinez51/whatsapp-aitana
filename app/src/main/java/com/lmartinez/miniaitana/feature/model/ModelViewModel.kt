package com.lmartinez.miniaitana.feature.model

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lmartinez.miniaitana.core.domain.usecase.ConfigUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

@HiltViewModel
class ModelViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val configUseCase: ConfigUseCase
) : ViewModel() {

    val config = configUseCase.getConfig()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    private val _isCopyingModel = MutableStateFlow(false)
    val isCopyingModel = _isCopyingModel.asStateFlow()

    fun updateModelPath(uri: Uri) {
        viewModelScope.launch {
            _isCopyingModel.value = true
            try {
                val path = copyModelToExternalStorage(uri)
                if (path != null) {
                    configUseCase.updateModelPath(path)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isCopyingModel.value = false
            }
        }
    }

    private suspend fun copyModelToExternalStorage(uri: Uri): String? = withContext(Dispatchers.IO) {
        val resolver = context.contentResolver
        val targetFile = File(context.getExternalFilesDir(null), "active_model.litertlm")
        
        try {
            resolver.openInputStream(uri)?.use { inputStream ->
                FileOutputStream(targetFile).use { outputStream ->
                    val buffer = ByteArray(1024 * 1024) // 1MB buffer for large files
                    var bytesRead: Int
                    while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                        outputStream.write(buffer, 0, bytesRead)
                    }
                }
            }
            return@withContext targetFile.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            return@withContext null
        }
    }

    fun setServiceEnabled(enabled: Boolean) {
        viewModelScope.launch {
            configUseCase.setServiceEnabled(enabled)
        }
    }
}
