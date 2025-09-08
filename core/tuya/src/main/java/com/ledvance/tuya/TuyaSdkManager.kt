package com.ledvance.tuya

import android.app.Application
import android.content.Context
import android.util.Log
import com.ledvance.tuya.impl.BizBundleFamilyServiceImpl
import com.ledvance.tuya.utils.AppConfig
import com.ledvance.utils.CoroutineScopeUtils
import com.ledvance.utils.extensions.isDebuggable
import com.ledvance.utils.extensions.tryCatch
import com.thingclips.sdk.core.PluginManager
import com.thingclips.security.prompt.PreferencesManager
import com.thingclips.smart.activator.scan.qrcode.ScanManager
import com.thingclips.smart.android.common.utils.L
import com.thingclips.smart.commonbiz.bizbundle.family.api.AbsBizBundleFamilyService
import com.thingclips.smart.family.FamilyInstance
import com.thingclips.smart.home.sdk.ThingHomeSdk
import com.thingclips.smart.interior.api.IThingHomePlugin
import com.thingclips.smart.optimus.sdk.ThingOptimusSdk
import com.thingclips.smart.thingpackconfig.PackConfig
import com.thingclips.smart.wrapper.api.ThingWrapper
import kotlinx.coroutines.Dispatchers
import timber.log.Timber


/**
 * @author : jason yin
 * Email : j.yin@ledvance.com
 * Created date 2025/9/3 11:47
 * Describe : TuyaSdkManager
 */
object TuyaSdkManager {
    private const val TAG = "TuyaSdkManager"
    private const val THING_TAG = "Thing"
    internal fun init(application: Application) {
        disableSecurityPrompt(application)
        PackConfig.addValueDelegate(AppConfig::class.java)
        L.setLogInterception(if (application.isDebuggable()) Log.DEBUG else Log.INFO) { level, tag, msg ->
            Timber.tag(THING_TAG).log(priority = level, "$tag $msg")
        }
        ThingHomeSdk.init(application)
        ThingOptimusSdk.init(application)
        ThingWrapper.init(application, { errorCode, urlBuilder ->
            val link = urlBuilder.target + urlBuilder.params.toString()
            Timber.tag(TAG).e("router not implement($errorCode) $link")
        }, { serviceName ->
            Timber.tag(TAG).e("service not implement ->$serviceName")
        })

        // register family service，mall bizbundle don't have to implement it.
        // 注册家庭服务，商城业务包可以不注册此服务
        ThingWrapper.registerService(
            AbsBizBundleFamilyService::class.java,
            BizBundleFamilyServiceImpl()
        )

        if (ThingHomeSdk.getUserInstance().isLogin) {
            onLogin()
        }
    }

    fun release() {
        ThingHomeSdk.onDestroy()
    }

    fun openScan(context: Context) {
        ScanManager.openScan(context)
    }

    internal fun onLogin() {
        ThingWrapper.onLogin()
        PluginManager.service(IThingHomePlugin::class.java)?.homeManagerInstance
            ?.unRegisterThingHomeChangeListener(FamilyInstance.getInstance().inviteListener)
    }

    internal fun onLogout(context: Context) {
        ThingWrapper.onLogout(context)
    }

    internal fun disableSecurityPrompt(context: Context) {
        CoroutineScopeUtils.launch(Dispatchers.IO) {
            tryCatch {
                // 关闭涂鸦application is for testing only提示弹窗，只会在非正式版SDK会弹出
                PreferencesManager(context).putLong(
                    "thing_security_prompt_key",
                    System.currentTimeMillis()
                )
            }
        }
    }

}