package com.utils

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.support.annotation.DrawableRes
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.transformation.GlideRoundTransform

import java.io.File

/**
 * Created by yangsk on 2017/6/12.
 */

object ImageLoader {

    private val TAG = ImageLoader::class.java.simpleName

    fun getDefaultOptions(drawable: Drawable): RequestOptions {
        return RequestOptions.noAnimation()
            .centerCrop()
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            .error(drawable)
            .placeholder(drawable)
            .dontTransform()
    }


    fun getBitmapRoundTransformationOptions(drawable: Drawable, transformation: BitmapTransformation): RequestOptions {
        return RequestOptions.noAnimation()
            .transform(transformation)
            .skipMemoryCache(false)
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            .error(drawable)
    }

    fun getBitmapRoundTransformationOptions(drawable: Drawable, corners: Int): RequestOptions {
        val roundedCorners = RoundedCorners(corners)
        return RequestOptions.noAnimation()
            .transform(roundedCorners)
            .skipMemoryCache(false)
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            .error(drawable)
    }

    fun loadImage(target: ImageView, url: String, @DrawableRes holder: Int) {
        load(target.context, target, url, getDrawable(target, holder))
    }

    fun loadGrayImage(target: ImageView, url: String, @DrawableRes holder: Int) {
        getGlide(target.context)
            .setDefaultRequestOptions(this.getDrawable(target, holder)?.let { getDefaultOptions(it) })
            .asBitmap().load(url).into(object : SimpleTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap?, transition: Transition<in Bitmap>) {
                    if (null != resource) {
                        target.setImageBitmap(BitmapUtil.grayBitmap(resource))
                    } else {
                        target.setImageResource(holder)
                    }
                }
            })
    }

    fun loadImage(target: ImageView, url: String, holder: Drawable) {
        load(target.context, target, url, holder)
    }

    fun loadImage(target: ImageView, url: File, @DrawableRes holder: Int) {
        load(target.context, target, url, getDrawable(target, holder))
    }


    fun loadImage(target: ImageView, resourceId: Int, @DrawableRes holder: Int) {
        load(target.context, target, resourceId, getDrawable(target, holder))
    }

    fun loadImageBackground(target: View, url: String, @DrawableRes holder: Int) {
        loadBackground(target.context, target, url, getDrawable(target, holder))
    }

    fun loadRoundImage(target: ImageView, url: String, @DrawableRes holder: Int, radius: Float) {
        loadRound(
            target.context,
            target,
            url,
            getDrawable(target, holder),
            GlideRoundTransform(target.context, radius)
        )
    }

    fun loadCircleCropImage(target: ImageView, url: String, @DrawableRes holder: Int) {
        if (null != target) {
            loadCircleCrop(target.context, target, url, getDrawable(target, holder))
        } else {
            Logger.e(TAG, "target is null !!!")
        }
    }

    fun loadCircleCropImage(target: ImageView, url: String, holder: Drawable) {
        if (null != target) {
            loadCircleCrop(target.context, target, url, holder)
        } else {
            Logger.e(TAG, "target is null !!!")
        }
    }

    fun loadBlurImage(target: ImageView, url: String, @DrawableRes holder: Int) {//高斯模糊
        if (target != null) {
            getGlide(target.context).asBitmap().load(url).into(object : SimpleTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap?, transition: Transition<in Bitmap>) {
                    if (resource != null) {
                        target.setImageBitmap(BitmapUtil.blurBitmap(target.context, resource, 1f))
                    } else {
                        target.setImageResource(holder)
                    }
                }
            })
        } else {
            Logger.e(TAG, "target is null !!!")
        }
    }

    private fun load(context: Context, target: ImageView, url: Any, holder: Drawable?) {
        getGlide(context).setDefaultRequestOptions(holder?.let { getDefaultOptions(it) }).load(url).into(target)
    }

    private fun load(context: Context, target: ImageView, resourceId: Int, holder: Drawable?) {
        getGlide(context).setDefaultRequestOptions(holder?.let { getDefaultOptions(it) }).load(resourceId).into(target)
    }

    fun getGlide(context: Context): RequestManager {
        return Glide.with(context)
    }

    private fun loadBackground(context: Context, target: View, url: String, holder: Drawable?) {
        getGlide(context).setDefaultRequestOptions(holder?.let { getDefaultOptions(it) }).load(url)
            .into(object : SimpleTarget<Drawable>() {
                override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>) {
                    target.background = resource
                }
            })
    }

    private fun loadRound(
        context: Context,
        target: ImageView,
        url: String,
        holder: Drawable?,
        transformation: BitmapTransformation
    ) {
        getGlide(context).setDefaultRequestOptions(
            holder?.let {
                getBitmapRoundTransformationOptions(
                    it,
                    transformation
                ).placeholder(holder)
            }
        ).load(url).into(target)
    }

    private fun loadCircleCrop(context: Context, target: ImageView, url: String, holder: Drawable?) {
        getGlide(context).setDefaultRequestOptions(holder?.let { getDefaultOptions(it).circleCrop() }).load(url).into(target)
    }

    @SuppressLint("ResourceType")
    private fun getDrawable(target: View, @DrawableRes holder: Int): Drawable? {
        return if (holder > 0) target.resources.getDrawable(holder) else null
    }
}
