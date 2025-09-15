package com.ledvance.tuya.command.controller

import androidx.annotation.CallSuper
import com.ledvance.tuya.command.DeviceDpHook
import com.ledvance.tuya.command.Dp
import com.ledvance.tuya.data.repo.ITuyaRepo
import com.ledvance.utils.AppContext
import com.ledvance.utils.extensions.tryCatch
import com.thingclips.smart.sdk.bean.DeviceBean
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel

/**
 * @author : jason yin
 * Email : j.yin@ledvance.com
 * Created date 2025/9/15 11:40
 * Describe : BaseController
 */
internal open class BaseController(protected val device: DeviceBean) {
    protected val TAG = this::class.simpleName ?: "BaseController"

    protected val scope: CoroutineScope = CoroutineScope(Dispatchers.IO)

    protected val deviceDpHook by lazy {
        DeviceDpHook(device = device, tuyaRepo = getTuyaRepo(), scope = scope)
    }

    protected suspend fun <T> Dp<T>.sendDp(value: T): Result<Boolean> {
        return if (deviceDpHook.getDp(this.dpId) != value) {
            setDpValue(value)
        } else Result.success(true)
    }

    @CallSuper
    open fun release() {
        tryCatch {
            deviceDpHook.release()
            scope.cancel()
        }
    }

    private fun getTuyaRepo(): ITuyaRepo {
        return getHiltEntryPoint().getTuyaRepo()
    }

    private fun getHiltEntryPoint(): LightControllerEntryPoint {
        return EntryPointAccessors.fromApplication(
            AppContext.get(),
            LightControllerEntryPoint::class.java
        )
    }
}

@EntryPoint
@InstallIn(SingletonComponent::class)
internal interface LightControllerEntryPoint {
    fun getTuyaRepo(): ITuyaRepo
}