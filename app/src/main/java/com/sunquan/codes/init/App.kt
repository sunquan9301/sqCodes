package com.sunquan.codes.init

import android.content.Context
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.sunquan.codes.BuildConfig
import sunquan.com.common.Constant
import sunquan.com.common.utils.DeviceUtil

/**
 * @author sunquan
 * sunquan@bitstarlight.com
 * @date 2019/12/19
 **/
open class App : MultiDexApplication(){
    val appRuntime: AppRuntime by lazy { AppRuntime(this) }

    override fun onCreate() {
        super.onCreate()
        sApp = this
        var processName = DeviceUtil.getCurrentProcessName(this)
        if (processName == null) {
            processName = ""
        }
        if (MAIN_PROCESS_NAME != processName) {
            return
        }


    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    companion object {
        val TAG = Constant.TAG.APP
        const val MAIN_PROCESS_NAME = BuildConfig.APPLICATION_ID


        var sApp: App = App()

        fun inst(): App {
            return sApp
        }

        fun exit() {
            inst().appRuntime.onDestroy()
            System.exit(0)
        }
    }
}