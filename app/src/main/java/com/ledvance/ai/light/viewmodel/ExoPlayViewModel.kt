package com.ledvance.ai.light.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.apply
import kotlin.collections.map
import kotlin.collections.toSet

/**
 * @author : jason yin
 * Email : j.yin@ledvance.com
 * Created date 2025/8/18 11:15
 * Describe : ExoPlayViewModel
 */
@HiltViewModel
class ExoPlayViewModel @Inject constructor(
    application: Application,
) : ViewModel() {
    private val exoPlayer by lazy {
        ExoPlayer.Builder(application)
            .build().apply {
                this.playWhenReady = true
                this.repeatMode = Player.REPEAT_MODE_ALL
            }
    }

    private val currentPlayList by lazy {
        mutableListOf<String>()
    }

    fun getPlayer(): ExoPlayer {
        return exoPlayer
    }

    fun loadMediaItem(assetNames: List<String>) {
        if (currentPlayList.toSet() == assetNames.toSet()) {
            return
        }
        val mediaItems = assetNames.map {
            MediaItem.fromUri("asset:///$it")
        }
        exoPlayer.setMediaItems(mediaItems)
        exoPlayer.prepare()
    }

    override fun onCleared() {
        super.onCleared()
        exoPlayer.release()
    }
}