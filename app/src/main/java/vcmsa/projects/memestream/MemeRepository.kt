package com.vcmsa.projects.memestream

class MemeRepository(private val dao: MemeDao) {
    suspend fun saveMeme(meme: MemeEntity) = dao.insertMeme(meme)
    suspend fun getMemes() = dao.getAllMemes()
    suspend fun getUnsyncedMemes() = dao.getUnsyncedMemes()
    suspend fun markAsSynced(meme: MemeEntity) = dao.updateMeme(meme.copy(isSynced = true))
}
