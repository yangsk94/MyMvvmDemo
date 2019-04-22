package com.navigation

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import com.wram.framework_lib.R

/**
 * Created by yangsk on 2017/6/4.
 */

class ErrorFragment : PageFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val context = container?.context
        val root = FrameLayout(context)
        getContext()?.resources?.getColor(R.color.c_aabbcc)?.let { root.setBackgroundColor(it) }
        val button = Button(context)
        button.text = getContext()?.resources?.getString(R.string.error)
        button.textSize = 22f
        button.setOnClickListener { finish() }
        val params = FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        params.gravity = Gravity.CENTER
        button.layoutParams = params
        root.addView(button, params)
        return root
    }

    companion object {

        val instance: ErrorFragment
            get() = ErrorFragment()
    }


}
