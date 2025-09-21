package com.vcmsa.projects.memestream

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

class MemeSyncWorker(
    context: Context,
    params: WorkerParameters,
    private val repository: MemeRepository
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val unsynced = repository.getUnsyncedMemes()
        for (meme in unsynced) {
            try {
                // TODO: Call your REST API (POST /memes)
                repository.markAsSynced(meme)
            } catch (e: Exception) {
                return Result.retry()
            }
        }
        return Result.success()
    }
}
