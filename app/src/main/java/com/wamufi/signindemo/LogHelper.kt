package com.wamufi.signindemo

import android.util.Log
import com.wamufi.signindemo.BuildConfig
import java.util.Locale

object LogHelper {
    private const val TAG = "DailyBible"

    fun v(message: Any) {
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "💬 Verbose ${convertMessage(convertToString(message))}")
        }
    }

    fun d(message: Any) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "🔧 Debug ${convertMessage(convertToString(message))}")
        }
    }

    fun i(message: Any) {
        if (BuildConfig.DEBUG) {
            Log.i(TAG, "📌 Info ${convertMessage(convertToString(message))}")
        }
    }

    fun w(message: Any) {
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "⚠️ Warning ${convertMessage(convertToString(message))}")
        }
    }

    fun e(message: Any) {
        if (BuildConfig.DEBUG) {
            Log.e(TAG, "❌ Error ${convertMessage(convertToString(message))}")
        }
    }

    private fun convertMessage(message: String): String {
        val element = Thread.currentThread().stackTrace[4]
        val fileName = element.fileName
        return String.format(
            Locale.getDefault(),
            "[%s.%s()#%d] %s",
            fileName.substring(0, fileName.indexOf(".")),
            element.methodName,
            element.lineNumber,
            message
        )
    }

    private fun convertToString(obj: Any): String {
        return if (obj is String) obj.toString() else {
            try {
                obj.toString()
            } catch (e: Exception) {
                ""
            }
        }
    }
}