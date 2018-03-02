package com.template.mvvm.base.utils

import android.util.Log

/**
 * My logger class
 *
 * @author Chris Xinyue Zhao <hasszhao></hasszhao>@gmail.com>
 */
class LL {

    fun info(_msg: String) {
        if (mDebugable) {
            Log.i(
                mkTag(),
                mkMessage(_msg)
            )
        }
    }

    fun info(_msg: String, _t: Throwable) {
        if (mDebugable) {
            Log.i(
                mkTag(),
                mkMessage(_msg), _t)
        }
    }

    fun warn(_msg: String) {
        if (mDebugable) {
            Log.w(
                mkTag(),
                mkMessage(_msg)
            )
        }
    }

    fun warn(_msg: String, _t: Throwable) {
        if (mDebugable) {
            Log.w(
                mkTag(),
                mkMessage(_msg), _t)
        }
    }

    fun debug(_msg: String) {
        if (mDebugable) {
            Log.d(
                mkTag(),
                mkMessage(_msg)
            )
        }
    }

    fun debug(_msg: String, _t: Throwable) {
        if (mDebugable) {
            Log.d(
                mkTag(),
                mkMessage(_msg), _t)
        }
    }

    fun error(_msg: String) {
        if (mDebugable) {
            Log.e(
                mkTag(),
                mkMessage(_msg)
            )
        }
    }

    fun error(_msg: String, _t: Throwable) {
        if (mDebugable) {
            Log.e(
                mkTag(),
                mkMessage(_msg), _t)
        }
    }

    fun fatal(_msg: String) {
        if (mDebugable) {
            Log.e(
                mkTag(),
                mkMessage(_msg)
            )
        }
    }

    fun fatal(_msg: String, _t: Throwable) {
        if (mDebugable) {
            Log.e(
                mkTag(),
                mkMessage(_msg), _t)
        }
    }

    companion object {

        private var mDebugable = true
        private val DEBUG_TAG = "#!#!"
        val logger = LL()

        private fun mkMessage(msg: String): String {
            return "[${Thread.currentThread().name}: ${DEBUG_TAG} $msg ${DEBUG_TAG}]"
        }

        private fun mkTag(): String {
            val stackDepth = 5
            val arrClassName = Thread.currentThread().stackTrace[stackDepth].className.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val className = arrClassName[arrClassName.size - 1]
            val methodName = Thread.currentThread().stackTrace[stackDepth].methodName
            val lineNumber = Thread.currentThread().stackTrace[stackDepth].lineNumber
            return "$className.$methodName @line: $lineNumber"
        }

        fun i(_tag: String, _info: String) {
            logger.info(_info)
        }

        fun i(_tag: String, _info: String, _throwable: Throwable) {
            logger.info(_info, _throwable)
        }

        fun d(_tag: String, _debug: String) {
            logger.debug(_debug)
        }

        fun d(_tag: String, _debug: String, _throwable: Throwable) {
            logger.debug(_debug, _throwable)
        }

        fun e(_tag: String, _error: String) {
            logger.error(_error)
        }

        fun e(_tag: String, _error: String, _throwable: Throwable) {
            logger.error(_error, _throwable)
        }

        fun v(_tag: String, _verbose: String) {
            logger.info(_verbose)
        }

        fun v(_tag: String, _verbose: String, _throwable: Throwable) {
            logger.info(_verbose, _throwable)
        }

        fun w(_tag: String, _warning: String) {
            logger.warn(_warning)
        }

        fun w(_tag: String, _warning: String, _throwable: Throwable) {
            logger.warn(_warning, _throwable)
        }

        fun i(_info: String) {
            logger.info(_info)
        }

        fun i(_info: String, _throwable: Throwable) {
            logger.info(_info, _throwable)
        }

        fun d(_debug: String) {
            logger.debug(_debug)
        }

        fun d(_debug: String, _throwable: Throwable) {
            logger.debug(_debug, _throwable)
        }

        fun e(_error: String) {
            logger.error(_error)
        }

        fun e(_error: String, _throwable: Throwable) {
            logger.error(_error, _throwable)
        }

        fun v(_verbose: String) {
            logger.info(_verbose)
        }

        fun v(_verbose: String, _throwable: Throwable) {
            logger.info(_verbose, _throwable)
        }

        fun w(_warning: String) {
            logger.warn(_warning)
        }

        fun w(_warning: String, _throwable: Throwable) {
            logger.warn(_warning, _throwable)
        }

        fun getStackTraceString(_e: Throwable): String {
            return Log.getStackTraceString(_e)
        }

        fun setDebugable(_debugable: Boolean) {
            mDebugable = _debugable
        }
    }
}