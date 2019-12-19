package sunquan.com.common.utils.statusbar;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import androidx.annotation.ColorInt;

public class StatusBarUtil {
    public static void makeWindowFullIncludeNavigationBar(Window window) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            View view = window.getDecorView();
            int flags = view.getSystemUiVisibility();
            flags |= View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
            window.getDecorView().setSystemUiVisibility(flags);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.setNavigationBarColor(Color.TRANSPARENT);
        }
    }

    public static int getStatusBarHeight(Context mContext) {
        return QMUIStatusBarHelper.getStatusbarHeight(mContext);
    }

    public static void translucent(Activity activity, @ColorInt int colorOn5x) {
        QMUIStatusBarHelper.translucent(activity, colorOn5x);
    }

    public static void setStatusTextBlack(Activity mContext) {
        QMUIStatusBarHelper.setStatusBarLightMode(mContext);
    }

    public static void setStatusTextWhite(Activity mContext) {
        QMUIStatusBarHelper.setStatusBarDarkMode(mContext);
    }

    public static void hideStatusBar(Window window) {
        View decorView = window.getDecorView();
        if (decorView == null) {
            return;
        }
        int uiOptions = decorView.getSystemUiVisibility() | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }

    public static void showStatusBar(Window window) {
        if (!checkNull(window)) {
            View decorView = window.getDecorView();
            if (decorView != null) {
                int uiOptions = decorView.getSystemUiVisibility() & -5;
                decorView.setSystemUiVisibility(uiOptions);
            }
        }
    }


    private static boolean checkNull(Window window) {
        if (window == null) {
            return true;
        }
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT;
    }

}
