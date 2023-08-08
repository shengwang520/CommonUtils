package com.sheng.wang.common.utils

import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.text.TextUtils
import java.io.*

/**
 * 本地文件管理
 */
object FileUtils {

    /**
     * 创建文件
     *
     * @param type   文件缓存类型[Environment]
     * @param suffix 文件后缀
     */
    @Throws(IOException::class)
    fun createFile(context: Context, type: String?, suffix: String?): File {
        val storageDir = context.getExternalFilesDir(type)
        return File.createTempFile(System.currentTimeMillis().toString(), suffix, storageDir)
    }

    /**
     * 删除指定文件
     * 1.安卓10一下所有文件可删除
     * 2.安卓10以上，只能删除app内部文件
     * @param path 被删除的文件路径
     */
    fun deleteFile(path: String?) {
        if (path.isNullOrEmpty()) return
        val file = File(path)
        if (file.exists()) {
            file.delete()
        }
    }

    /**
     * android 10+ 适配 uri转化为app内部文件
     */
    fun getUri2CachePath(context: Context?, path: String?): String? {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && !TextUtils.isEmpty(path)) {
            val imgFile = context?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            if (imgFile?.exists() == false) {
                imgFile.mkdir()
            }
            try {
                val file = File(imgFile?.absolutePath + File.separator + getFileNewName(path))
                // 使用openInputStream(uri)方法获取字节输入流
                val fileInputStream = context?.contentResolver?.openInputStream(Uri.parse(path))
                val fileOutputStream = FileOutputStream(file)
                val buffer = ByteArray(1024)
                var byteRead: Int
                while (-1 != fileInputStream?.read(buffer).also { byteRead = it ?: 0 }) {
                    fileOutputStream.write(buffer, 0, byteRead)
                }
                fileInputStream?.close()
                fileOutputStream.flush()
                fileOutputStream.close()
                // 文件可用新路径 file.getAbsolutePath()
                return file.absolutePath
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return path
        } else {
            return path
        }
    }

    /**
     * 获取文件新名称
     *
     * @param path 原文件路径
     */
    private fun getFileNewName(path: String?): String {
        try {
            return System.currentTimeMillis().toString() + path?.substring(path.lastIndexOf("."))
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return System.currentTimeMillis().toString()
    }
}