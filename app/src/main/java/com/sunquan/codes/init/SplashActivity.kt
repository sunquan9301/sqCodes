package com.sunquan.codes.init

import android.Manifest
import android.content.Intent
import com.sunquan.codes.MainActivity
import com.sunquan.codes.R
import sunquan.com.common.utils.NavUtil

/**
 * @author sunquan
 * sunquan@bitstarlight.com
 * @date 2019/7/25
 **/
class SplashActivity : BaseActivity() {

    override fun getLayoutId(): Int {
        return R.layout.activity_splash
    }

    override fun startInit() {

        checkoutPermission()
        checkoutIsTaskRoot()
    }


    private fun checkoutPermission() {
        reqPermission(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            if (it) {
                NavUtil.navActivity(this, MainActivity::class.java)
                finish()
            } else {
                App.exit()
            }
        }
    }

    private fun checkoutIsTaskRoot() {
        if (!this.isTaskRoot) {
            val intent = intent
            if (intent != null) {
                val action = intent.action
                if (intent.hasCategory(Intent.CATEGORY_LAUNCHER) && Intent.ACTION_MAIN == action) {
                    finish()
                    return
                }
            }
        }
    }


    override fun onResume() {
        super.onResume()
    }

    companion object {
        private var firstLaunch = true
    }
}