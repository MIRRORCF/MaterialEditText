package com.mirrorcf.materialedittext

import android.text.TextUtils
import android.util.Log

object LogUtils {

    private const val DEFAULT_TAG = "material"
    private const val OPEN = false
    private const val LEVEL_V = Log.VERBOSE
    private const val LEVEL_D = Log.DEBUG
    private const val LEVEL_I = Log.INFO
    private const val LEVEL_W = Log.WARN
    private const val LEVEL_E = Log.ERROR

    fun v(msg: String?) {
        log(LEVEL_V, msg)
    }

    fun v(tag: String?, msg: String?) {
        log(LEVEL_V, tag, msg)
    }

    fun d(msg: String?) {
        log(LEVEL_D, msg)
    }

    fun d(tag: String?, msg: String?) {
        log(LEVEL_D, tag, msg)
    }

    fun i(msg: String?) {
        log(LEVEL_I, msg)
    }

    fun i(tag: String?, msg: String?) {
        log(LEVEL_I, tag, msg)
    }

    fun w(msg: String?) {
        log(LEVEL_W, msg)
    }

    fun w(tag: String?, msg: String?) {
        log(LEVEL_W, tag, msg)
    }

    fun e(msg: String?) {
        log(LEVEL_E, msg)
    }

    fun e(tag: String?, msg: String?) {
        log(LEVEL_E, tag, msg)
    }

    fun log(level: Int, msg: String?) {
        log(level, DEFAULT_TAG, msg)
    }

    fun log(level: Int, tag: String?, msg: String?) {
        if (TextUtils.isEmpty(tag) || !OPEN) return
        msg?.let {
            when (level) {
                LEVEL_V -> Log.v(tag, msg)
                LEVEL_D -> Log.d(tag, msg)
                LEVEL_I -> Log.i(tag, msg)
                LEVEL_W -> Log.w(tag, msg)
                LEVEL_E -> Log.e(tag, msg)
                else -> Log.i(tag, msg)
            }
        }

    }


}