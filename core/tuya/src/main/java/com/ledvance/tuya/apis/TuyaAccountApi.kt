package com.ledvance.tuya.apis

import android.app.Application
import com.alibaba.fastjson.JSON
import com.ledvance.tuya.TuyaSdkManager
import com.ledvance.tuya.apis.domain.ITuyaAccountApi
import com.thingclips.smart.android.user.api.ILoginCallback
import com.thingclips.smart.android.user.api.ILogoutCallback
import com.thingclips.smart.android.user.api.IUidLoginCallback
import com.thingclips.smart.android.user.bean.User
import com.thingclips.smart.home.sdk.ThingHomeSdk
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.suspendCancellableCoroutine
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume

/**
 * @author : jason yin
 * Email : j.yin@ledvance.com
 * Created date 2025/9/3 09:34
 * Describe : TuyaAccountApi
 */
@Singleton
internal class TuyaAccountApi @Inject constructor(
    private val application: Application
) : ITuyaAccountApi {
    private val TAG = "TuyaAccountApi"

    private val userFlow = MutableStateFlow(ThingHomeSdk.getUserInstance().user)

    override fun isLogin(): Boolean = ThingHomeSdk.getUserInstance().isLogin
    override fun getUserFlow(): Flow<User?> {
        return userFlow
    }

    override suspend fun loginWithUid(countryCode: String, uid: String, password: String) =
        suspendCancellableCoroutine {
            ThingHomeSdk.getUserInstance()
                .loginOrRegisterWithUid(countryCode, uid, password, true, object :
                    IUidLoginCallback {
                    override fun onSuccess(user: User?, homeId: Long) {
                        Timber.tag(TAG)
                            .i("loginWithUid onSuccess ${JSON.toJSON(user)},homeId:$homeId")
                        it.takeIf { it.isActive }?.resume(user)
                        userFlow.update { user }
                        TuyaSdkManager.onLogin()
                    }

                    override fun onError(code: String?, error: String?) {
                        Timber.tag(TAG).e("loginWithUid onError code:$code,error:$error")
                        it.takeIf { it.isActive }?.resume(null)
                    }
                })
        }

    override suspend fun loginWithEmail(countryCode: String, email: String, password: String) =
        suspendCancellableCoroutine {
            ThingHomeSdk.getUserInstance().loginWithEmail(countryCode, email, password, object :
                ILoginCallback {
                override fun onSuccess(user: User?) {
                    Timber.tag(TAG).i("loginWithEmail onSuccess ${JSON.toJSON(user)}")
                    it.takeIf { it.isActive }?.resume(user)
                    userFlow.update { user }
                    TuyaSdkManager.onLogin()
                }

                override fun onError(code: String?, error: String?) {
                    Timber.tag(TAG).e("loginWithEmail onError code:$code,error:$error")
                    it.takeIf { it.isActive }?.resume(null)
                }
            })
        }

    override suspend fun logout() = suspendCancellableCoroutine {
        ThingHomeSdk.getUserInstance().logout(object : ILogoutCallback {
            override fun onSuccess() {
                Timber.tag(TAG).i("logout onSuccess")
                it.takeIf { it.isActive }?.resume(true)
                userFlow.update { null }
                TuyaSdkManager.onLogout(application)
            }

            override fun onError(code: String?, error: String?) {
                Timber.tag(TAG).e("logout onError code:$code,error:$error")
                it.takeIf { it.isActive }?.resume(false)
            }
        })
    }
}