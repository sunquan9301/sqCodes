package sunquan.com.common.utils

import android.app.Activity
import android.content.Context
import android.content.Intent

/**
 * @author sunquan
 * sunquan@bitstarlight.com
 * @date 2019/12/19
 **/
object NavUtil {
    /**
     * 跳转到某个Activity
     */
    fun navActivity(context: Context, toActivityClass: Class<*>) {
        val intent = Intent(context, toActivityClass)
        safeNavActivity(context, intent, -1)
    }

    fun safeNavActivity(context: Context, intent: Intent, requestCode: Int) {
        if (context is Activity) {
            if (requestCode <= 0) {
                context.startActivity(intent)
            } else {
                context.startActivityForResult(intent, requestCode)
            }
        } else {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
    }
}