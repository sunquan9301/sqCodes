package sunquan.com.common.callback

import android.app.Activity
import android.app.Application
import android.os.Bundle

/**
 * @author sunquan
 * sunquan@bitstarlight.com
 * @date 2019/12/19
 **/
/**
 * Helper to avoid implementing all lifecycle callback methods.
 */
abstract class SimpleLifecycleCallbacks : Application.ActivityLifecycleCallbacks {
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

    }
}