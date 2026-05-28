package com.lmartinez.miniaitana.core.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.lmartinez.miniaitana.core.data.db.dao.ContactDao
import com.lmartinez.miniaitana.core.data.db.dao.ConversationLogDao
import com.lmartinez.miniaitana.core.data.db.dao.PromptTemplateDao
import com.lmartinez.miniaitana.core.data.db.entity.ContactEntity
import com.lmartinez.miniaitana.core.data.db.entity.ConversationLogEntity
import com.lmartinez.miniaitana.core.data.db.entity.PromptTemplateEntity

@Database(
    entities = [
        ContactEntity::class,
        ConversationLogEntity::class,
        PromptTemplateEntity::class,
    ],
    version = 2,
    exportSchema = false,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun contactDao(): ContactDao
    abstract fun conversationLogDao(): ConversationLogDao
    abstract fun promptTemplateDao(): PromptTemplateDao

    companion object {
        const val DATABASE_NAME = "mini_aitana_db"
    }
}
