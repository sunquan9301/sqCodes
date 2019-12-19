package sunquan.com.common

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.KITKAT
import android.os.Build.VERSION_CODES.M
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import sunquan.com.common.utils.DeviceUtil
import java.util.*

/**
 * @author sunquan
 * sunquan@bitstarlight.com
 * @date 2019/12/19
 **/

object IMMLeaks {
    private val TAG = "IMMLeaks"

    private val DEFAULT_FIELD = arrayOf("mCurRootView", "mServedView", "mNextServedView")

    /**
     * Fix for https://code.google.com/p/android/issues/detail?id=171190 .
     *
     *
     * When a view that has focus gets detached, we wait for the main thread to be idle and succ
     * check if the InputMethodManager is leaking a view. If yes, we tell it that the decor view got
     * focus, which is what happens if you press home and come back from recent apps. This replaces
     * the reference to the detached view with a reference to the decor view.
     *
     *
     * Should be called from [Activity.onCreate] )}.
     */
    fun fixFocusedViewLeak(application: Application) {
        if (SDK_INT < KITKAT || SDK_INT > 25) { //fixed this issue in Android O
            return
        }
        val fixedList = getWontFixedFiled(application)
        application.registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle) {

            }

            override fun onActivityStarted(activity: Activity) {

            }

            override fun onActivityResumed(activity: Activity) {

            }

            override fun onActivityPaused(activity: Activity) {

            }

            override fun onActivityStopped(activity: Activity) {

            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {

            }

            override fun onActivityDestroyed(activity: Activity) {
                fixInputMethod(activity, fixedList)
            }
        })
    }

    /**
     * fixInputMethod
     *
     * @param context Context
     */
    private fun fixInputMethod(context: Context?, fixedFiled: List<String>?) {
        if (context == null || fixedFiled == null) return
        var inputMethodManager: InputMethodManager? = null
        try {
            inputMethodManager =
                context.applicationContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        } catch (th: Throwable) {
            th.printStackTrace()
        }

        if (inputMethodManager == null) return
        //InputMethodManager上持有的view  mLastSrvView是华为特有的view
        //        String[] strArr =new String[]{"mCurRootView", "mServedView", "mNextServedView", "mLastSrvView"};
        // 以上四个变量前三个getContext通常是一个，容易导致泄漏则为 mLastSrvView，这个变量是imm最后持有的，通常会导致内存泄漏
        for (aStrArr in fixedFiled) {
            try {
                val declaredField = inputMethodManager.javaClass.getDeclaredField(aStrArr) ?: continue
                if (!declaredField.isAccessible) {
                    declaredField.isAccessible = true
                }
                val obj = declaredField.get(inputMethodManager)
                if (obj == null || obj !is View) continue
                if (obj.context === context) {
                    declaredField.set(inputMethodManager, null)
                }
            } catch (th: Throwable) {
                //method not found
                th.printStackTrace()
            }

        }
    }

    private fun getWontFixedFiled(context: Context): List<String> {
        val fixedList = ArrayList<String>()
        Collections.addAll(fixedList, *DEFAULT_FIELD)
        //华为rom,>6.0
        if (DeviceUtil.isHuaweiRom() && SDK_INT >= M) {
            Log.d(TAG, "getWontFixedFiled: ")
            fixedList.add("mLastSrvView")
        }
        Log.d(TAG, "getWontFixedFiled: fixedList=$fixedList")
        return fixedList
    }
}