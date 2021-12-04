package com.example.finalprojectassignment_20012022003

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder

class BackgroundMusicService : Service() {
    private lateinit var mediaPlayer: MediaPlayer

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if(intent!=null){
            val position = intent.getIntExtra("position",0)
            val data = intent.getStringExtra("service")
            if(!this::mediaPlayer.isInitialized){
                mediaPlayer = MediaPlayer.create(this,WorkoutActivity.arrayList[position].music)
                mediaPlayer.isLooping = true
            }

            if(data=="play/pause"){
                if(!mediaPlayer.isPlaying){
                    mediaPlayer.start()
                }
                else{
                    mediaPlayer.pause()
                }
            }
        }
        else{
            mediaPlayer.start()
        }
        return START_STICKY
    }

    override fun onDestroy() {
        mediaPlayer.stop()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }
}