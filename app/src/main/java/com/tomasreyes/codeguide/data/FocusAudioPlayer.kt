package com.tomasreyes.codeguide.data

import android.content.Context
import android.net.Uri
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer

class FocusAudioPlayer(context: Context) {

    private val appContext = context.applicationContext
    private val player = ExoPlayer.Builder(appContext).build()

    var isPlaying: Boolean = false
        private set

    var currentRawResId: Int? = null
        private set

    fun play(audioResource: Int) {
        if (currentRawResId == audioResource && isPlaying) return

        currentRawResId = audioResource
        val uri = Uri.parse("android.resource://${appContext.packageName}/$audioResource")

        player.setMediaItem(MediaItem.fromUri(uri))
        player.repeatMode = Player.REPEAT_MODE_ONE
        player.prepare()
        player.play()
        isPlaying = true
    }

    fun pause() {
        player.pause()
        isPlaying = false
    }

    fun resume() {
        if (currentRawResId != null) {
            player.play()
            isPlaying = true
        }
    }

    fun stop() {
        player.stop()
        player.clearMediaItems()
        currentRawResId = null
        isPlaying = false
    }

    fun setVolume(volume: Float) {
        player.volume = volume.coerceIn(0f, 1f)
    }

    fun release() {
        player.release()
        isPlaying = false
        currentRawResId = null
    }
}