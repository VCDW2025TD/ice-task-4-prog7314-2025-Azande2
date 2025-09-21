package com.vcmsa.projects.memestream

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import com.vcmsa.projects.memestream.MemeDatabase
import com.vcmsa.projects.memestream.MemeEntity
import com.vcmsa.projects.memestream.MemeRepository
import com.vcmsa.projects.memestream.MemeSyncWorker
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private lateinit var memeRepository: MemeRepository

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // ✅ Show logged-in user
        val user = FirebaseAuth.getInstance().currentUser
        val welcomeText = findViewById<TextView>(R.id.txtWelcome)
        welcomeText.text = "Welcome, ${user?.displayName ?: "User"}"

        // ✅ Initialize RoomDB + Repository
        val db = Room.databaseBuilder(
            applicationContext,
            MemeDatabase::class.java,
            "meme_db"
        ).build()
        memeRepository = MemeRepository(db.memeDao())

        // ✅ Example: Insert a meme (for testing)
        lifecycleScope.launch {
            val testMeme = MemeEntity(
                userId = user?.uid ?: "guest",
                imageUrl = "https://example.com/meme.jpg",
                caption = "First Meme!",
                isSynced = false
            )
            memeRepository.saveMeme(testMeme)
        }

        // ✅ Schedule WorkManager to sync every 15 minutes
        val syncRequest = PeriodicWorkRequestBuilder<MemeSyncWorker>(
            15, TimeUnit.MINUTES
        ).build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "MemeSync",
            ExistingPeriodicWorkPolicy.KEEP,
            syncRequest
        )
    }
}
