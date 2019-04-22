package com.utils

import android.content.Context
import android.graphics.*
import android.os.Build
import android.renderscript.*
import android.support.annotation.FloatRange


/**
 * 项目名称：xcbb_client_android
 * 类名称：BlurBitmapUtil
 * 类描述：
 * 创建人: Yangsk
 * 创建时间: 2017/9/8 15:18
 * 修改人：
 * 修改时间： 2017/9/8 15:18
 * 修改备注：
 */


object BitmapUtil {
    /**
     * 对图片进行高斯模糊
     *
     * @param context    上下文对象
     * @param image      需要模糊的图片
     * @param blurRadius 模糊半径，由于性能限制，这个值的取值区间为(0至25f)
     * @return 模糊处理后的图片
     */
    fun blurBitmap(
        context: Context, image: Bitmap, @FloatRange(from = 1.0, to = 25.0)
        blurRadius: Float
    ): Bitmap {
        // 计算图片缩小后的长宽
        val width = Math.round((image.width / 10).toFloat())
        val height = Math.round((image.height / 10).toFloat())

        // 创建一张缩小后的图片做为渲染的图片
        val bitmap = Bitmap.createScaledBitmap(image, width, height, false)

        // 创建RenderScript内核对象
        val rs = RenderScript.create(context)
        // 创建一个模糊效果的RenderScript的工具对象，第二个参数Element相当于一种像素处理的算法，高斯模糊的话用这个就好
        val blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs))

        // 由于RenderScript并没有使用VM来分配内存,所以需要使用Allocation类来创建和分配内存空间
        // 创建Allocation对象的时候其实内存是空的,需要使用copyTo()将数据填充进去
        val input = Allocation.createFromBitmap(rs, bitmap)
        // 创建相同类型的Allocation对象用来输出
        val type = input.type
        val output = Allocation.createTyped(rs, type)

        // 设置渲染的模糊程度, 25f是最大模糊度
        blurScript.setRadius(blurRadius)
        // 设置blurScript对象的输入内存
        blurScript.setInput(input)
        // 将输出数据保存到输出内存中
        blurScript.forEach(output)
        // 将数据填充到bitmap中
        output.copyTo(bitmap)

        // 销毁它们释放内存
        if (type != null) {
            try {
                type.destroy()
            } catch (e: RSInvalidStateException) {
                e.printStackTrace()
            }

        }
        try {
            input.destroy()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        try {
            output.destroy()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        try {
            blurScript.destroy()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        if (rs != null) {
            try {
                rs.destroy()
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

        return bitmap
    }

    fun grayBitmap(bitmap: Bitmap): Bitmap? {

        val width = bitmap.width
        val height = bitmap.height

        val config = Bitmap.Config.ARGB_8888
        val grayBitmap = Bitmap.createBitmap(width, height, config)

        val canvas = Canvas(grayBitmap)
        val saturation = ColorMatrix()
        saturation.setSaturation(0f)
        val paint = Paint()
        paint.colorFilter = ColorMatrixColorFilter(saturation)
        canvas.drawBitmap(bitmap, 0f, 0f, paint)

        return grayBitmap
    }

    /**
     * 判断该Bitmap是否可以设置到BitmapFactory.Options.inBitmap上
     */
    fun canUseForInBitmap(bitmap: Bitmap, options: BitmapFactory.Options): Boolean {
        // 在Android4.4以后，如果要使用inBitmap的话，只需要解码的Bitmap比inBitmap设置的小就行了，对inSampleSize没有限制
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            var width = options.outWidth
            var height = options.outHeight
            if (options.inSampleSize > 0) {
                width /= options.inSampleSize
                height /= options.inSampleSize
            }
            val byteCount = width * height * getBytesPerPixel(bitmap.config)
            return byteCount <= bitmap.allocationByteCount
        }
        // 在Android4.4之前，如果想使用inBitmap的话，解码的Bitmap必须和inBitmap设置的宽高相等，且inSampleSize为1
        return (bitmap.width == options.outWidth
                && bitmap.height == options.outHeight
                && options.inSampleSize == 1)
    }

    //获取每个像素所占用的Byte数
    private fun getBytesPerPixel(config: Bitmap.Config): Int {
        if (config == Bitmap.Config.ARGB_8888) {
            return 4
        } else if (config == Bitmap.Config.RGB_565) {
            return 2
        } else if (config == Bitmap.Config.ARGB_4444) {
            return 2
        } else if (config == Bitmap.Config.ALPHA_8) {
            return 1
        }
        return 1
    }
}
