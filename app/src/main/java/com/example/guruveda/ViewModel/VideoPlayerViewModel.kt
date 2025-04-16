package com.example.guruveda.ViewModel

import android.annotation.SuppressLint
import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer

class VideoPlayerViewModel(application: Application) : AndroidViewModel(application) {

    private val _isLoading = MutableLiveData(true)
    val isLoading: LiveData<Boolean> = _isLoading

    private var _player: ExoPlayer? = null
    val player: ExoPlayer?
        get() = _player

    @SuppressLint("UseKtx")
    fun initializePlayer(videoUrl: String) {
        if (_player != null) return
        _player = ExoPlayer.Builder(getApplication()).build()
        val mediaItem = MediaItem.fromUri(Uri.parse(videoUrl))
        _player?.setMediaItem(mediaItem)
        _player?.prepare()

        _player?.addListener(object : androidx.media3.common.Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                if (playbackState == androidx.media3.common.Player.STATE_READY) {
                    _isLoading.postValue(false)
                    _player?.play()
                }
            }
        })
    }

    override fun onCleared() {
        super.onCleared()
        _player?.release()
        _player = null
    }

}