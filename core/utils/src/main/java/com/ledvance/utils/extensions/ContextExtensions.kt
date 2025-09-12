package com.ledvance.utils.extensions

import android.annotation.SuppressLint
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.location.LocationManager
import android.net.wifi.WifiManager
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy.Builder
import android.provider.Settings
import timber.log.Timber

/**
 * @author : jason yin
 * Email : j.yin@ledvance.com
 * Created date 2025/6/13 11:07
 * Describe : ContextExtensions
 */
/**
 * Return true if the application is debuggable.
 */
fun Context.isDebuggable(): Boolean {
    return 0 != applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE
}

/**
 * Set a thread policy that detects all potential problems on the main thread, such as network
 * and disk access.
 *
 * If a problem is found, the offending call will be logged and the application will be killed.
 */
fun Context.enableStrictModePolicy() {
    if (isDebuggable()) {
        StrictMode.setThreadPolicy(
            Builder().detectAll().penaltyLog().build(),
        )
    }
}


fun Context.enableTimerDebugTree() {
    if (isDebuggable()) {
        Timber.plant(Timber.DebugTree())
    }
}


fun Context.openWifiSettingsPage() {
    val intent = Intent(WifiManager.ACTION_PICK_WIFI_NETWORK).let { intent ->
        resolveActivity(intent).let { if (it) intent else null }
    } ?: Intent(Settings.ACTION_WIFI_SETTINGS).let { intent ->
        resolveActivity(intent).let { if (it) intent else null }
    }
    intent?.also { startActivity(it) }
}

fun Context.openLocationSettingsPage() {
    safeStartActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
}

fun Context.openBluetoothSettingsPage() {
    safeStartActivity(Intent(Settings.ACTION_BLUETOOTH_SETTINGS))
}

@SuppressLint("QueryPermissionsNeeded")
fun Context.resolveActivity(intent: Intent?): Boolean = intent?.run {
    try {
        resolveActivity(packageManager) != null
    } catch (e: Exception) {
        false
    }
} ?: false

fun Context.safeStartActivity(intent: Intent): Boolean = tryCatchReturn {
    if (!resolveActivity(intent)) {
        return@tryCatchReturn false
    }
    startActivity(intent)
    return@tryCatchReturn true
} ?: false


val Context.wifiManager get() = getSystemService(Context.WIFI_SERVICE) as WifiManager

val Context.bluetoothManager get() = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager

val Context.locationManager get() = getSystemService(Context.LOCATION_SERVICE) as LocationManager

val Context.wifiSsid: String
    get() = tryCatchReturn {
        wifiManager.connectionInfo?.ssid?.takeIf {
            // 未赋予定位权限导致获取不到名称
            !it.contains("<unknown ssid>")
        }?.let {
            tryCatchReturn { it.substring(1 until it.length - 1) } ?: it
        } ?: ""
    } ?: ""

val Context.isBluetoothEnable: Boolean
    get() = bluetoothManager.adapter?.isEnabled ?: false

val Context.isLocationEnable: Boolean
    get() = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)