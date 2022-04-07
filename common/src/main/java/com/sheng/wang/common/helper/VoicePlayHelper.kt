package com.sheng.wang.common.helper

import android.content.Context
import android.media.MediaPlayer
import java.io.IOException

/**
 * 声音播放器
 */
class VoicePlayHelper private constructor() {

    private var mPlayer: MediaPlayer? = null

    /**
     * 播放声音
     */
    @JvmOverloads
    fun playFromRawFile(mContext: Context, id: Int, isLoop: Boolean = false) {
        stopPlay()
        try {
            mPlayer = MediaPlayer()
            val file = mContext.resources.openRawResourceFd(id)
            mPlayer?.setDataSource(file.fileDescriptor, file.startOffset, file.length)
            if (!isPlaying) {
                mPlayer?.prepare()
                mPlayer?.start()
                mPlayer?.isLooping = isLoop //循环播放
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 播放音频，本地或远端
     */
    @JvmOverloads
    fun playUrl(url: String?, onCompletionListener: MediaPlayer.OnCompletionListener? = null, onInfoListener: MediaPlayer.OnInfoListener? = null) {
        try {
            mPlayer = MediaPlayer()
            mPlayer?.setOnCompletionListener(onCompletionListener)
            mPlayer?.setOnInfoListener(onInfoListener)
            mPlayer?.setDataSource(url)
            mPlayer?.prepare()
            mPlayer?.start()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    /**
     * 结束播放来
     */
    fun stopPlay() {
        try {
            if (mPlayer != null && mPlayer!!.isPlaying) {
                mPlayer?.stop()
                mPlayer?.release()
            }
            mPlayer = null
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 是否正在播放
     */
    private val isPlaying: Boolean
        get() = if (mPlayer == null) false else mPlayer!!.isPlaying

    companion object {
        var instance: VoicePlayHelper? = null
            get() {
                if (field == null) {
                    synchronized(VoicePlayHelper::class.java) {
                        if (null == field) {
                            field = VoicePlayHelper()
                        }
                    }
                }
                return field
            }
            private set
    }
}