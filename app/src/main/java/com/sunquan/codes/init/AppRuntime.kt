package com.sunquan.codes.init

import android.app.Activity
import android.content.ComponentCallbacks
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.util.DisplayMetrics
import sunquan.com.common.IMMLeaks
import sunquan.com.common.callback.SimpleLifecycleCallbacks
import sunquan.com.common.utils.statusbar.StatusBarUtil

/**
 * @author sunquan
 * sunquan@bitstarlight.com
 * @date 2019/7/18
 **/
class AppRuntime(var context: Context) {

    private val IMAGE_PIPELINE_CACHE_DIR = "fresco_cache"

    private var appDisplayMetrics: DisplayMetrics? = null
    private var barHeight: Int = 0
    private var appDensity: Float = 0.toFloat()
    private var appScaledDensity: Float = 0.toFloat()


    fun onCreate(componentCallback: Runnable?) {
        IMMLeaks.fixFocusedViewLeak(App.inst())

        initDensity()
        registerActivityLifecycleCallbacks()
        context.registerComponentCallbacks(object : ComponentCallbacks {
            override fun onConfigurationChanged(newConfig: Configuration?) {
                if (newConfig != null && newConfig.fontScale > 0) {
                    appScaledDensity = App.inst().resources.displayMetrics.scaledDensity
                }
            }

            override fun onLowMemory() {}
        })
    }

    private fun registerActivityLifecycleCallbacks() {
        App.inst().registerActivityLifecycleCallbacks(object : SimpleLifecycleCallbacks() {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle) {
                if (activity !is SplashActivity) {
                    setActivityOrientation(activity, SCREEN_DENSITY, 667f)
                } else {
                    val targetDensityDpi = (160 * appDensity).toInt()
                    val activityDisplayMetrics = activity.resources.displayMetrics
                    activityDisplayMetrics.density = appDensity
                    activityDisplayMetrics.scaledDensity = appScaledDensity
                    activityDisplayMetrics.densityDpi = targetDensityDpi
                }
            }
        })
        context.registerComponentCallbacks(object : ComponentCallbacks {
            override fun onConfigurationChanged(newConfig: Configuration?) {
                if (newConfig != null && newConfig.fontScale > 0) {
                    appScaledDensity = App.inst().resources.displayMetrics.scaledDensity
                }
            }

            override fun onLowMemory() {}
        })
    }

    private fun setActivityOrientation(activity: Activity, width: Float, height: Float) {
        val targetDensity: Float
        if (appDisplayMetrics == null) {
            return
        }
        if (width > 0) {
            targetDensity = appDisplayMetrics!!.widthPixels / width
        } else {
            targetDensity = (appDisplayMetrics!!.heightPixels - barHeight) / height
        }

        val targetScaledDensity = targetDensity * (appScaledDensity / appDensity)
        val targetDensityDpi = (160 * targetDensity).toInt()
        /**
         *
         * 最后在这里将修改过后的值赋给系统参数
         *
         * 只修改Activity的density值
         */
        val activityDisplayMetrics = activity.resources.displayMetrics
        activityDisplayMetrics.density = targetDensity
        activityDisplayMetrics.scaledDensity = targetScaledDensity
        activityDisplayMetrics.densityDpi = targetDensityDpi
        val appDisplayMetrics = App.inst().resources.displayMetrics
        appDisplayMetrics.density = targetDensity
        appDisplayMetrics.scaledDensity = targetScaledDensity
        appDisplayMetrics.densityDpi = targetDensityDpi
    }



    private fun initDensity() {
        //获取application的DisplayMetrics
        appDisplayMetrics = App.inst().resources.displayMetrics
        //获取状态栏高度
        barHeight = StatusBarUtil.getStatusBarHeight(App.inst())

        if (appDensity == 0f) {
            //初始化的时候赋值
            appDensity = appDisplayMetrics?.density ?: 0f
            appScaledDensity = appDisplayMetrics?.scaledDensity ?: 0f
        }
    }

    public fun onDestroy() {
    }

    companion object {
        val SCREEN_DENSITY = 399.6f

    }
}