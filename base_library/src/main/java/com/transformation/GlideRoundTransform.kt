package com.transformation

import android.content.Context
import android.graphics.Bitmap
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import com.bumptech.glide.load.resource.bitmap.TransformationUtils

import java.security.MessageDigest

/**
 * Created by Rex on 2017/6/20.
 */

class GlideRoundTransform(context: Context, dp: Float) : BitmapTransformation() {

    private val mRadius: Int

    private val mContext: Context

    init {
        mContext = context.applicationContext
        mRadius = Math.round(mContext.resources.displayMetrics.density * dp)
    }

    override fun transform(pool: BitmapPool, toTransform: Bitmap, outWidth: Int, outHeight: Int): Bitmap {
        return TransformationUtils.roundedCorners(pool, toTransform, outWidth, outHeight, mRadius)
    }

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {}
}
