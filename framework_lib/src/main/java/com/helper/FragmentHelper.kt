package com.helper

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.util.SimpleArrayMap
import android.text.TextUtils
import android.util.Log
import com.navigation.ErrorFragment


/**
 * Created by yangsk on 2017/6/4.
 */

object FragmentHelper {

    private val sClassMap = SimpleArrayMap<String, Class<*>>()

    @JvmOverloads
    fun instantiate(context: Context, fname: String, args: Bundle? = null): Fragment {
        return instantiate(context.classLoader, fname, args)
    }

    fun instantiate(classLoader: ClassLoader?, fname: String, args: Bundle?): Fragment {
        try {
            if (null == classLoader || TextUtils.isEmpty(fname)) {
                return ErrorFragment.instance
            }
            var clazz = sClassMap.get(fname)
            if (clazz == null) {
                clazz = classLoader.loadClass(fname)
                sClassMap.put(fname, clazz)
            }
            val f = clazz?.newInstance() as Fragment
            if (args != null) {
                args.classLoader = f.javaClass.classLoader
                f.arguments = args
            }
            return f
        } catch (e: Throwable) {
            Log.e(
                "FragmentHelper", "Unable to instantiate fragment " + fname
                        + ": make sure class name exists, is public, and has an"
                        + " empty constructor that is public", e
            )
        }

        return ErrorFragment.instance
    }
}
