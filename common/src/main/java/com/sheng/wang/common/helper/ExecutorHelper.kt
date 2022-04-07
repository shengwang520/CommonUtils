package com.sheng.wang.common.helper

import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executor
import java.util.concurrent.Executors

/**
 * 线程助手
 */
class ExecutorHelper @JvmOverloads constructor(private val mDiskIO: Executor = DisIOExecutor(),
    private val mNetworkIO: Executor = NetExecutor(),
    private val mUiExecutor: Executor = MainThreadExecutor(),
    private val mCompressExecutor: Executor = CompressExecutor()
) {

    /**
     * io线程
     */
    fun diskIO(): Executor {
        return mDiskIO
    }

    /**
     * 网络线程
     */
    fun networkIO(): Executor {
        return mNetworkIO
    }

    /**
     * 压缩线程
     */
    fun compressExecutor(): Executor {
        return mCompressExecutor
    }

    /**
     * 主线程
     */
    fun mainThread(): MainThreadExecutor {
        return mUiExecutor as MainThreadExecutor
    }

    class MainThreadExecutor : Executor {
        var mainThreadHandler = Handler(Looper.getMainLooper())
        override fun execute(command: Runnable) {
            mainThreadHandler.post(command)
        }

        /**
         * 延迟加载
         */
        fun postDelayed(command: Runnable, delayMillis: Long) {
            mainThreadHandler.postDelayed(command, delayMillis)
        }
    }

    private class DisIOExecutor : Executor {
        private val mDiskIO: Executor = Executors.newSingleThreadExecutor()
        override fun execute(command: Runnable) {
            mDiskIO.execute(command)
        }
    }

    private class NetExecutor : Executor {
        private val mNetExecutor: Executor = Executors.newSingleThreadExecutor()
        override fun execute(command: Runnable) {
            mNetExecutor.execute(command)
        }
    }

    /**
     * 无核心线程，使用后 60 秒自动释放,视频压缩，正则替换
     */
    private class CompressExecutor : Executor {
        private val mCompressExecutor: Executor = Executors.newCachedThreadPool()
        override fun execute(command: Runnable) {
            mCompressExecutor.execute(command)
        }
    }

    private object SingletonHolder {
        var instance = ExecutorHelper()
    }

    companion object {
        fun getInstance(): ExecutorHelper {
            return SingletonHolder.instance
        }
    }

}