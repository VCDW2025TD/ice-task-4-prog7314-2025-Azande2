package com.vcmsa.projects.memestream

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "memes")
data class MemeEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: String,
    val imageUrl: String,
    val caption: String,
    val isSynced: Boolean = false
)

