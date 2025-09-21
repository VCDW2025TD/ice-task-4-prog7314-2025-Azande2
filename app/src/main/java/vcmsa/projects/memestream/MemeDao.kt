package com.vcmsa.projects.memestream

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface MemeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMeme(meme: MemeEntity)

    @Query("SELECT * FROM memes")
    suspend fun getAllMemes(): List<MemeEntity>

    @Query("SELECT * FROM memes WHERE isSynced = 0")
    suspend fun getUnsyncedMemes(): List<MemeEntity>

    @Update
    suspend fun updateMeme(meme: MemeEntity)
}
