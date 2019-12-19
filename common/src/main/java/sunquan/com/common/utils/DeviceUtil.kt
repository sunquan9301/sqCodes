package sunquan.com.common.utils

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.Context
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Process
import android.provider.Settings
import android.telephony.TelephonyManager
import android.text.TextUtils
import android.util.Log
import sunquan.com.common.Constant
import java.net.NetworkInterface
import java.net.SocketException
import java.util.*

/**
 * @author sunquan
 * sunquan@bitstarlight.com
 * @date 2019/12/19
 **/
object DeviceUtil {
    val TAG = Constant.TAG.DEVICE
    private var sCurProcessName: String? = null

    /**
     * 获取当下进程名字
     */
    fun getCurrentProcessName(context: Context): String? {
        var processName: String? = sCurProcessName
        if (!TextUtils.isEmpty(processName)) {
            return processName
        }
        try {
            val pid = Process.myPid()
            val mActivityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            for (appProcess in mActivityManager.runningAppProcesses) {
                if (appProcess.pid == pid) {
                    processName = appProcess.processName
                    sCurProcessName = processName
                    return processName
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    fun getMemoryInfo(context: Context): String {
        val activityManager =
            context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val memoInfo = ActivityManager.MemoryInfo()
        activityManager.getMemoryInfo(memoInfo)
        return ("totalMem = " + memoInfo.totalMem + "/ (1024 * 1024)}MB,"
                + "availMem = " + memoInfo.availMem + "/ (1024 * 1024)}MB,"
                + "threshold = " + memoInfo.threshold + "/ (1024 * 1024)}MB")
    }

    fun getDeviceId(context: Context): String {
        val packageName = context.getPackageName()
        val androidId = getAndroidId(context)
        val deviceId = Build.ID
        val macAddress = getMacAddress(context)
        val imeiStr = getIMEI(context)[0]
        var additionalStr: String? = null
        if (Check.isEmpty(macAddress ?: "") && Check.isEmpty(imeiStr)) {
            additionalStr = UUID.randomUUID().toString()
        }
        //String time = DateUtil.getCurrentTime().toString();

        val installationId: String
        if (Check.isEmpty(androidId) && Check.isEmpty(deviceId)) {
            installationId = EncryptUtil.md5(packageName + androidId + deviceId)
        } else {
            installationId = EncryptUtil.md5(packageName + androidId + deviceId + macAddress + imeiStr + additionalStr)
        }

        return installationId
    }

    fun getMacAddress(context: Context): String? {
        val macAddress = getMacAddressWithWifiManager(context)
        return if (Check.isEmpty(macAddress?:"")) getMacAddressWithNetworkInterface() else macAddress
    }

    private fun getMacAddressWithNetworkInterface(): String? {
        var interfaceList: ArrayList<*>? = null

        try {
            interfaceList = Collections.list(NetworkInterface.getNetworkInterfaces())
            val wlan = getHardwareAddress(interfaceList, "wlan0")
            return if (Check.isEmpty(wlan?:"")) getHardwareAddress(interfaceList, "eth1") else wlan
        } catch (var2: SocketException) {
            return null
        }
    }

    private fun getHardwareAddress(interfaceList: List<NetworkInterface>?, name: String): String? {
        if (null != interfaceList) {
            try {
                val `i$` = interfaceList.iterator()

                while (`i$`.hasNext()) {
                    val networkInterface = `i$`.next()
                    val interfaceName = networkInterface.name
                    if (!Check.isEmpty(interfaceName) && interfaceName.equals(name, ignoreCase = true)) {
                        val macBytes = networkInterface.hardwareAddress
                        if (null != macBytes) {
                            val mac = StringBuilder()
                            val len = macBytes.size

                            for (i in 0 until len) {
                                val b = macBytes[i]
                                mac.append(String.format("%02X:", b))
                            }

                            if (mac.length > 0) {
                                mac.deleteCharAt(mac.length - 1)
                            }

                            return mac.toString()
                        }
                    }
                }
            } catch (var11: Exception) {

            }

        }

        return null
    }

    @SuppressLint("MissingPermission")
    fun getIMEI(context: Context): Array<String> {
        val imeistring = arrayOf("", "")

        try {
            val telephonyManager =
                context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            imeistring[0] = telephonyManager.deviceId
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                imeistring[1] = telephonyManager.getDeviceId(1)
            }
        } catch (var2: Exception) {
            Log.d(TAG, "failed to get imei $var2")
        }

        return imeistring
    }

    private fun getMacAddressWithWifiManager(context: Context): String? {
        if (Build.VERSION.SDK_INT >= 23) {
            return null
        } else {
            var macAddress: String? = null

            try {
                val wifiManager =
                    context.getSystemService(Context.WIFI_SERVICE) as WifiManager
                val wInfo = wifiManager.connectionInfo
                macAddress = wInfo.macAddress
                if ("02:00:00:00:00:00" == macAddress) {
                    return null
                }
            } catch (var3: Exception) {
                Log.d(TAG, "failed to get wifi mac address$var3")
            }

            return macAddress
        }
    }

    /**
     * 判断是否为华为系统
     */
    fun isHuaweiRom(): Boolean {
        val manufacturer = Build.MANUFACTURER
        return !TextUtils.isEmpty(manufacturer) && manufacturer.contains("HUAWEI")
    }

    fun getAndroidId(context: Context): String {
        var androidId = ""
        try {
            androidId = Settings.Secure.getString(
                context.getContentResolver(),
                Settings.Secure.ANDROID_ID
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return androidId
    }

}