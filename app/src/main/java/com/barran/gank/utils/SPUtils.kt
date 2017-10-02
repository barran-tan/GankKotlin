package com.barran.gank.utils

import android.content.Context
import com.barran.gank.app.App

/**
 * 包装
 *
 * Created by tanwei on 2017/9/30.
 */
object SPUtils {
    const val SP_CONFIG = "config"
    fun put(key: String, value: Any) {
        val editor = App.appContext.getSharedPreferences(SP_CONFIG, Context.MODE_PRIVATE).edit()
        when (value) {
            is Int -> editor.putInt(key, value)
            is String -> editor.putString(key, value)
            is Boolean -> editor.putBoolean(key, value)
            is Long -> editor.putLong(key, value)
            is Float -> editor.putFloat(key, value)
            else
            -> editor.putString(key, value.toString())
        }

        editor.apply()
    }

    fun getInt(key: String, default: Int): Int {
        val sp = App.appContext.getSharedPreferences(SP_CONFIG, Context.MODE_PRIVATE)
        return if (sp.contains(key)) sp.getInt(key, default) else default
    }

    fun getString(key: String, default: String): String {
        val sp = App.appContext.getSharedPreferences(SP_CONFIG, Context.MODE_PRIVATE)
        return if (sp.contains(key)) sp.getString(key, default) else default
    }

    fun getLong(key: String, default: Long): Long {
        val sp = App.appContext.getSharedPreferences(SP_CONFIG, Context.MODE_PRIVATE)
        return if (sp.contains(key)) sp.getLong(key, default) else default
    }

    fun getFloat(key: String, default: Float): Float {
        val sp = App.appContext.getSharedPreferences(SP_CONFIG, Context.MODE_PRIVATE)
        return if (sp.contains(key)) sp.getFloat(key, default) else default
    }
}