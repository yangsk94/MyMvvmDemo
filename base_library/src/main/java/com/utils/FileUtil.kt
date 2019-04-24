package com.utils

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import android.text.TextUtils
import android.util.Base64
import android.util.Log
import java.io.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.zip.ZipEntry
import java.util.zip.ZipFile


/**
 * 文件操作类
 *
 * @author ZengJ
 */
class FileUtil {
    var path1: String? = null

    interface ZipFileListener {
        /**
         * 成功
         */
        fun onSuccess()

        /**
         * 失败
         */
        fun onError()
    }

    companion object {
        val AES_KEY = "xcVNCPrSoftZyKey"

        fun getPath1(): String {
            return sdPath + "UncaughtExceptions/" + data
        }

        /**
         * 创建文件的模式，已经存在的文件要覆盖
         */
        val MODE_COVER = 1

        /**
         * 创建文件的模式，文件已经存在则不做其它事
         */
        val MODE_UNCOVER = 0

        /**
         * SD卡是否可用
         *
         * @return
         */
        val isSDCardAvailable: Boolean
            get() = Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED


        /**
         * 获取文件的输入流
         *
         * @param path
         * @return
         */
        fun getFileInputStream(path: String): FileInputStream? {
            var fis: FileInputStream? = null
            try {
                val file = File(path)
                if (file.exists()) {
                    fis = FileInputStream(file)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return fis
        }

        /**
         * 获取文件的输出流
         *
         * @param path
         * @return
         */
        fun getFileOutputStream(path: String): OutputStream? {
            var fos: FileOutputStream? = null
            try {
                val file = File(path)
                if (file.exists()) {
                    fos = FileOutputStream(file)
                }
            } catch (e: Exception) {
                return null
            }

            return fos
        }

        /**
         * 删除文件或文件夹(包括目录下的文件)
         *
         * @param filePath
         */
        fun deleteFile(filePath: String) {
            if (TextUtils.isEmpty(filePath)) {
                return
            }
            try {
                val f = File(filePath)
                if (f.exists() && f.isDirectory) {
                    val delFiles = f.listFiles()
                    if (delFiles != null) {
                        for (i in delFiles.indices) {
                            deleteFile(delFiles[i].absolutePath)
                        }
                    }
                }
                f.delete()
            } catch (e: Exception) {

            }

        }

        /**
         * 删除文件或文件夹(包括目录下的文件)
         *
         * @param
         */
        fun deleteFile(f: File) {

            try {

                if (f.exists() && f.isDirectory) {
                    val delFiles = f.listFiles()
                    if (delFiles != null) {
                        for (i in delFiles.indices) {
                            deleteFile(delFiles[i].absolutePath)
                        }
                    }
                }
                f.delete()
            } catch (e: Exception) {

            }

        }

        /**
         * 删除文件
         *
         * @param filePath
         * @param deleteParent 是否删除父目录
         */
        fun deleteFile(filePath: String?, deleteParent: Boolean) {
            if (filePath == null) {
                return
            }
            try {
                val f = File(filePath)
                if (f.exists() && f.isDirectory) {
                    val delFiles = f.listFiles()
                    if (delFiles != null) {
                        for (i in delFiles.indices) {
                            deleteFile(delFiles[i].absolutePath, deleteParent)
                        }
                    }
                }
                if (deleteParent) {
                    f.delete()
                } else if (f.isFile) {
                    f.delete()
                }
            } catch (e: Exception) {

            }

        }

        /**
         * 创建一个空的文件(创建文件的模式，已经存在的是否要覆盖)
         *
         * @param path
         * @param mode
         */
        fun createFile(path: String, mode: Int): Boolean {
            if (TextUtils.isEmpty(path)) {
                return false
            }
            try {
                val file = File(path)
                if (file.exists()) {
                    if (mode == FileUtil.MODE_COVER) {
                        file.delete()
                        file.createNewFile()
                    }
                } else {
                    // 如果路径不存在，先创建路径
                    val mFile = file.parentFile
                    if (!mFile.exists()) {
                        mFile.mkdirs()
                    }
                    file.createNewFile()
                }
            } catch (e: Exception) {
                return false
            }

            return true
        }

        /**
         * 创建一个空的文件夹(创建文件夹的模式，已经存在的是否要覆盖)
         *
         * @param path
         * @param mode
         */
        fun createFolder(path: String, mode: Int) {
            try {
                // LogUtil.debug(path);
                val file = File(path)
                if (file.exists()) {
                    if (mode == FileUtil.MODE_COVER) {
                        file.delete()
                        file.mkdirs()
                    }
                } else {
                    file.mkdirs()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

        /**
         * 获取文件大小
         *
         * @param path
         * @return
         */
        fun getSize(path: String): Long {
            if (TextUtils.isEmpty(path)) {
                return 0
            }
            var size: Long = 0
            try {
                val file = File(path)
                if (file.exists()) {
                    size = file.length()
                }
            } catch (e: Exception) {
                return 0
            }

            return size
        }

        /**
         * 判断文件或文件夹是否存在
         *
         * @param path
         * @return true 文件存在
         */
        fun isExist(path: String): Boolean {
            if (TextUtils.isEmpty(path)) {
                return false
            }
            var exist = false
            try {
                val file = File(path)
                exist = file.exists()
            } catch (e: Exception) {
                return false
            }

            return exist
        }

        /**
         * 重命名文件/文件夹
         *
         * @param path
         * @param newName
         */
        fun rename(path: String, newName: String): Boolean {
            var result = false
            if (TextUtils.isEmpty(path) || TextUtils.isEmpty(newName)) {
                return result
            }
            try {
                val file = File(path)
                if (file.exists()) {
                    result = file.renameTo(File(newName))
                }
            } catch (e: Exception) {
            }

            return result
        }

        /**
         * 列出目录文件
         *
         * @return
         */
        fun listFiles(filePath: String): Array<File>? {
            val file = File(filePath)
            return if (file.exists() && file.isDirectory) {
                file.listFiles()
            } else null
        }

        /**
         * 移动文件
         *
         * @param oldFilePath 旧路径
         * @param newFilePath 新路径
         * @return
         */
        fun moveFile(oldFilePath: String, newFilePath: String): Boolean {
            if (TextUtils.isEmpty(oldFilePath) || TextUtils.isEmpty(newFilePath)) {
                return false
            }
            val oldFile = File(oldFilePath)
            if (oldFile.isDirectory || !oldFile.exists()) {
                return false
            }
            try {
                val newFile = File(newFilePath)
                if (!newFile.exists()) {
                    newFile.createNewFile()
                }
                val bis = BufferedInputStream(
                    FileInputStream(oldFile)
                )
                val fos = FileOutputStream(newFile)
                val buf = ByteArray(1024)
                var read: Int = 0
                while (read != -1) {
                    fos.write(buf, 0, read)
                    read = bis.read(buf)
                }
                fos.flush()
                fos.close()
                bis.close()
                return true
            } catch (e: Exception) {
                e.printStackTrace()
                return false
            }

        }

        /**
         * @param dir Directory or file
         * @return space size of all files in this dir, default value in Bytes;
         * return 0 if incorrect dir.
         */
        fun getFileSize(dir: File?) // 取得文件夹大小
                : Long {
            var size: Long = 0
            if (dir == null) {
                return size
            }
            if (!dir.isDirectory) {
                return dir.length()
            }
            val flist = dir.listFiles() ?: return 0
            for (i in flist.indices) {
                if (flist[i].isDirectory) {
                    size = size + getFileSize(flist[i])
                } else {
                    size = size + flist[i].length()
                }
            }
            return size
        }


        /**
         * delete file or dirs(including all files under that dir)
         *
         * @param file
         */
        fun deleteFileOrDir(file: File?) {
            if (file != null && file.exists()) {
                // 判断是否为文件
                if (file.isFile) { // 为文件时调用删除文件方法
                    file.delete()
                } else { // 为目录时调用删除目录方法
                    deleteDir(file)
                }
            }
        }

        /**
         * delete dir and all its children
         *
         * @param dir
         */
        private fun deleteDir(dir: File?) {
            if (dir != null && dir.exists() && dir.isDirectory) {
                // 删除文件夹下的所有文件(包括子目录)
                val files = dir.listFiles()
                for (filedir in files) {
                    // 删除子文件
                    if (filedir.isFile) {
                        filedir.delete()
                    } // 删除子目录
                    else {
                        deleteDir(filedir)
                    }
                }
                dir.delete()
            }
        }

        // String time = formatter.format(new Date());
        // String fileName = "crash-" + time + "-" + timestamp + ".txt";
        val file: File
            get() {
                val logFt = SimpleDateFormat("yyyyMMdd")
                val timestamp = System.currentTimeMillis()
                val d = Date(timestamp)
                val time = logFt.format(d)
                val fileName = "crash-$time.txt"
                val dir = File(getPath1())
                if (!dir.exists()) {
                    dir.mkdirs()
                }
                val f = File(getPath1() + fileName)
                if (!f.exists()) {
                    try {
                        f.createNewFile()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }

                }
                return f
            }


        /**
         * 动态发布的文件夹
         *
         * @param context
         * @return
         */
        fun getPrsoftPublishDir(context: Context): File {
            // Get the directory for the app's private pictures directory.
            val file = File(
                context.getExternalFilesDir(
                    Environment.MEDIA_MOUNTED
                ), "prSoftPublish"
            )
            if (!file.exists()) {
                if (!file.mkdirs()) {
                    Log.e("FileUtil", "prSoftPublish Directory not created")
                }
            }

            return file
        }

        /**
         * app的文件夹
         *
         * @param context
         * @return
         */
        fun getPublicDir(context: Context): File {
            // Get the directory for the app's private pictures directory.
            val file = File(context.getExternalFilesDir(Environment.MEDIA_MOUNTED), "base")
            if (!file.exists()) {
                if (!file.mkdirs()) {
                    Log.e("FileUtil", "prSoftPublish Directory not created")
                }
            }

            return file
        }

        /**
         * 得到输出的Video保存路径
         *
         * @return
         */
        fun newVideoFilePath(context: Context): String {
            val str = System.currentTimeMillis()
            return getPrsoftPublishDir(context).path + "/" + str + ".mp4"
        }

        /**
         * 得到输出的图片保存路径
         *
         * @return
         */
        fun newPhotoFilePath(context: Context): String {
            return getPublicDir(context).path + "/" + System.currentTimeMillis() + ".jpg"
        }

        fun newPhotoFilePath(context: Context, name: String): String {
            return getPublicDir(context).path + "/" + name + ".jpg"
        }

        fun newSignatureFilePath(context: Context, name: String): String {
            return getPublicDir(context).path + "/" + name + ".jpg"
        }

        /**
         * 解压缩功能.简单加密将zipFile文件解压到folderPath目录下.
         *
         * @throws Exception
         */
        fun upZipFileEncrypt(zipFile: File, folderPath: String, listener: ZipFileListener) {
            try {
                Logger.i("upZipFile", "解压缩 -----------" + zipFile.absolutePath)
                val zfile = ZipFile(zipFile)
                val zList = zfile.entries()
                var ze: ZipEntry? = null
                val buf = ByteArray(1024)
                while (zList.hasMoreElements()) {
                    ze = zList.nextElement() as ZipEntry
                    if (ze.isDirectory) {
                        // Logger.i("upZipFile", "ze.getName() = " + ze.getName());
                        var dirstr = folderPath + "/" + ze.name
                        //dirstr.trim();
                        dirstr = String(dirstr.toByteArray(charset("8859_1")), charset("GB2312"))
                        Logger.i("upZipFile", "str = $dirstr")
                        val f = File(dirstr)
                        f.mkdir()
                        continue
                    }
                    Logger.i("upZipFile", "ze.getName() = " + ze.name)
                    //			OutputStream os=new BufferedOutputStream(new FileOutputStream(getRealFileName(folderPath, ze.getName())));
                    val bis = BufferedInputStream(zfile.getInputStream(ze))
                    val os = BufferedOutputStream(FileOutputStream(File(folderPath, ze.name)))

                    val a = AES_KEY.toByteArray()
                    os.write(a)

                    var readLen = 0
                    while (readLen != -1) {
                        os.write(buf, 0, readLen)
                        readLen = bis.read(buf, 0, 1024)
                    }
                    bis.close()
                    os.close()
                }
                zfile.close()
                zipFile.delete()
                //            Logger.i("upZipFile", "finishs 完毕： " );
                listener.onSuccess()
            } catch (e: Exception) {
                e.printStackTrace()
                listener.onError()
            }

        }


        /**
         * 获取今天的日期
         *
         * @return
         */
        val data: String
            get() {
                val c = Calendar.getInstance()
                val f = SimpleDateFormat("yyyy-MM-dd")
                return f.format(c.time)
            }

        /**
         * 获取sd卡路径
         *
         * @return
         */
        // 判断sd卡是否存在
        // 获取跟目录
        val sdPath: String
            @SuppressLint("SdCardPath")
            get() {
                var sdDir = ""
                val sdCardExist = Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
                if (sdCardExist) {
                    sdDir = Environment.getExternalStorageDirectory().toString() + "/prsoft/cyvideo/"
                } else {
                    sdDir = "/data/data/com.prsoft.cyvideo/prsoft/"
                }

                val file = File(sdDir)
                if (!file.exists()) {
                    file.mkdirs()
                }
                return sdDir
            }

        fun saveImage(fromFile: File, tagFile: File): File {
            val b = getSmallBitmap(fromFile.absolutePath)

            savePicToView(b, tagFile)
            return tagFile
        }

        /**
         * 方法说明：保存裁剪之后的图片数据
         */
        fun savePicToView(photo: Bitmap?, file: File) {
            if (photo == null) {
                return
            }
            try {
                // 创建文件
                file.createNewFile()
            } catch (e: IOException) {
                e.printStackTrace()
            }

            // 定义文件输出流
            var fOut: FileOutputStream? = null
            try {
                fOut = FileOutputStream(file)
                // 将bitmap存储为jpg格式的图片
                photo.compress(Bitmap.CompressFormat.JPEG, 100, fOut)
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            } catch (e: NullPointerException) {
                e.printStackTrace()
            } finally {
                try {
                    fOut?.flush()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

                try {
                    fOut?.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }

        }

        /**
         * 根据路径获得图片并压缩，返回bitmap用于显示
         */
        fun getSmallBitmap(filePath: String): Bitmap {
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            BitmapFactory.decodeFile(filePath, options)
            //        options.inSampleSize = calculateInSampleSize(options, 360, 600);
            options.inSampleSize = calculateInSampleSize(options, 720, 1200)
            options.inJustDecodeBounds = false
            //添加
            val b = BitmapFactory.decodeFile(filePath, options)
            // int degree = readPictureDegree(filePath);
            // return rotaingImageView(degree, b);

            return BitmapFactory.decodeFile(filePath, options)
        }

        /**
         * 计算图片的缩放值
         */
        fun calculateInSampleSize(
            options: BitmapFactory.Options,
            reqWidth: Int, reqHeight: Int
        ): Int {
            val height = options.outHeight
            val width = options.outWidth
            var inSampleSize = 1

            if (height > reqHeight || width > reqWidth) {
                val heightRatio = Math.round(height.toFloat() / reqHeight.toFloat())
                val widthRatio = Math.round(width.toFloat() / reqWidth.toFloat())
                inSampleSize = if (heightRatio < widthRatio) heightRatio else widthRatio
            }
            return inSampleSize
        }


        /**
         * 释放资源
         */
        fun recycleBitmap(vararg bitmaps: Bitmap) {
            for (bm in bitmaps) {
                if (!bm.isRecycled) {
                    bm.recycle()
                }
            }
        }

        /**
         * 图片转成string
         */
        fun convertIconToString(bitmap: Bitmap): String {
            val baos = ByteArrayOutputStream()// outputstream
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
            /*    int options = 100;
        while (baos.toByteArray().length / 1024 > 500) {  //循环判断如果压缩后图片是否大于500kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            options -= 10;//每次都减少10
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
        }*/
            val appicon = baos.toByteArray()// 转为byte数组
            try {
                baos.flush()
            } catch (e: IOException) {
                e.printStackTrace()
            }

            recycleBitmap(bitmap)
            return Base64.encodeToString(appicon, 0, appicon.size, Base64.DEFAULT)
        }

        fun closeQuietly(closeable: Closeable?) {
            val clo = closeable
            try {
                if (null != clo) {
                    clo.close()
                }
            } catch (ignore: Throwable) {
                Logger.e(ignore)
            }

        }

        private val DEFAULT_SKIP_COUNT = FileUtil.AES_KEY.toByteArray().size

        fun decodeFileDescriptor(path: String, options: BitmapFactory.Options, decryption: Boolean): Bitmap? {
            var bitmap: Bitmap? = null
            var file: RandomAccessFile? = null
            try {
                file = RandomAccessFile(File(path), "r")
                if (decryption) {
                    file.skipBytes(DEFAULT_SKIP_COUNT)
                }
                bitmap = BitmapFactory.decodeFileDescriptor(file.fd, null, options)
            } catch (e: Throwable) {
                Logger.e(e)
            } finally {
                FileUtil.closeQuietly(file)
            }
            return bitmap
        }

        /**
         *  
         *  * 图片按比例大小压缩方法 
         *  * 
         *  * @param image （根据Bitmap图片压缩） 
         *  * @return 
         *  
         */
        fun compressScale(image: Bitmap): Bitmap? {
            val baos = ByteArrayOutputStream()
            image.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            if (baos.toByteArray().size / 1024 > 1024) {//判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
                baos.reset()//重置baos即清空baos
                image.compress(Bitmap.CompressFormat.JPEG, 50, baos)//这里压缩50%，把压缩后的数据存放到baos中
            }
            var isBm = ByteArrayInputStream(baos.toByteArray())
            val newOpts = BitmapFactory.Options()
            //开始读入图片，此时把options.inJustDecodeBounds 设回true了
            newOpts.inJustDecodeBounds = true
            var bitmap = BitmapFactory.decodeStream(isBm, null, newOpts)
            newOpts.inJustDecodeBounds = false
            val w = newOpts.outWidth
            val h = newOpts.outHeight
            //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
            val hh = 800f//这里设置高度为800f
            val ww = 480f//这里设置宽度为480f
            //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
            var be = 1//be=1表示不缩放
            if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
                be = (newOpts.outWidth / ww).toInt()
            } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
                be = (newOpts.outHeight / hh).toInt()
            }
            if (be <= 0)
                be = 1
            newOpts.inSampleSize = be//设置缩放比例
            //newOpts.inPreferredConfig = Bitmap.Config.RGB_565;//降低图片从ARGB888到RGB565
            //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
            isBm = ByteArrayInputStream(baos.toByteArray())
            bitmap = BitmapFactory.decodeStream(isBm, null, newOpts)
            // return compressImage(bitmap);//压缩好比例大小后再进行质量压缩
            return bitmap
        }
    }
}



