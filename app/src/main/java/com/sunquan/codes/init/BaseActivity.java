package com.sunquan.codes.init;

import android.Manifest;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import com.sunquan.codes.R;
import sunquan.com.common.utils.Check;
import sunquan.com.common.utils.PermissionUtil;
import sunquan.com.common.utils.statusbar.StatusBarUtil;

/**
 * Created by jiangchunbo on 18/3/15.
 */

public abstract class BaseActivity extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_CODE = 1;
    private long curTime;
    private long curTimeForStopPages;
    protected String TAG = "";
    protected int statusBarHeight;
    private boolean isTranslucent = true;
    private Callback permissionCallBack = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //透明状态栏
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        statusBarHeight = StatusBarUtil.getStatusBarHeight(this);
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        try {
            TAG = getClass().getSimpleName();
        } catch (Exception e) {
            e.printStackTrace();
        }
        StatusBarUtil.setStatusBarColor(getWindow(), getResources().getColor(R.color.status_bar));
        StatusBarUtil.setStatusTextWhite(this);
        startInit();
        compatFontSize();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    public void setTranslucent(boolean isTranslucent) {
        this.isTranslucent = isTranslucent;
    }

    /**
     * 初始化布局
     *
     * @return
     */
    public abstract int getLayoutId();

    /**
     * 参数设置
     */
    public abstract void startInit();

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        compatFontSize();
    }

    public void compatFontSize() {
        Resources res = super.getResources();
        final float fontScale0 = res.getConfiguration().fontScale;
        float maxScale = res.getConfiguration().densityDpi == 320 ? 1.0f : configFontMaxScale();
        if (res.getConfiguration().fontScale > maxScale) {
            res.getConfiguration().fontScale = maxScale;
            res.updateConfiguration(null, null);
        }
        final float fontScale = res.getConfiguration().fontScale;

    }

    public float configFontMaxScale() {
        return 1.2f;
    }

    public void reqPermission(String[] permissions, Callback callback) {
        if (Check.INSTANCE.isEmpty(permissions)) {
            callback.onResult(true);
        }
        boolean needPermission = false;
        for (String permission : permissions) {
            if (ActivityCompat.checkSelfPermission(App.Companion.inst(), permission) != PackageManager.PERMISSION_GRANTED) {
                needPermission = true;
                break;
            }
        }
        permissionCallBack = callback;
        if (needPermission) {
            ActivityCompat.requestPermissions(this, permissions, PERMISSION_REQUEST_CODE);
        } else if (callback != null) {
            callback.onResult(true);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults == null) {
            if (permissionCallBack != null) {
                permissionCallBack.onResult(false);
            }
            return;
        }
        if (requestCode != PERMISSION_REQUEST_CODE || grantResults.length == 0) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            if (permissionCallBack != null) {
                permissionCallBack.onResult(false);
            }
            return;
        }
        for (int result : grantResults) {
            if (result == PackageManager.PERMISSION_DENIED) {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                if (permissionCallBack != null) {
                    permissionCallBack.onResult(false);
                }
                return;
            }
        }
        for (String permission : permissions) {
            if (!Manifest.permission.RECORD_AUDIO.equals(permission)) continue;
            if (!PermissionUtil.hasAudioPermission(this)) {
                if (permissionCallBack != null) {
                    permissionCallBack.onResult(false);
                    return;
                }
            } else {
                break;
            }
        }
        if (permissionCallBack != null) {
            permissionCallBack.onResult(true);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        curTime = System.currentTimeMillis();
        curTimeForStopPages = System.currentTimeMillis();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
        long duration = System.currentTimeMillis() - curTime;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        long duration = System.currentTimeMillis() - curTimeForStopPages;
    }

    public interface Callback {
        void onResult(boolean success);
    }

    protected void setTvStatusBarHeight(View tvStatusBar) {
        ViewGroup.LayoutParams layoutParams = tvStatusBar.getLayoutParams();
        layoutParams.height = statusBarHeight;
        tvStatusBar.setLayoutParams(layoutParams);
    }
}
