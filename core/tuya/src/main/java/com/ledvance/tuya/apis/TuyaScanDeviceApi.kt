package com.ledvance.tuya.apis

import com.ledvance.tuya.apis.domain.ITuyaScanDeviceApi
import com.ledvance.tuya.beans.DeviceNetworkType
import com.ledvance.tuya.beans.DeviceVendor
import com.ledvance.tuya.beans.ScanDevice
import com.ledvance.tuya.beans.TuyaScanDeviceSetting
import com.ledvance.tuya.beans.TuyaScanDeviceType
import com.ledvance.tuya.utils.BleAdvertisingParser
import com.ledvance.tuya.utils.BleScanResultAggregator
import com.ledvance.utils.extensions.toJson
import com.ledvance.utils.extensions.tryCatch
import com.ledvance.utils.extensions.tryCatchReturn
import com.thingclips.sdk.matter.activator.IDynamicDiscoveryListener
import com.thingclips.smart.android.ble.api.LeScanSetting
import com.thingclips.smart.android.ble.api.ScanType
import com.thingclips.smart.home.sdk.ThingHomeSdk
import com.thingclips.smart.sdk.bean.ThingMatterDiscovery
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

/**
 * @author : jason yin
 * Email : j.yin@ledvance.com
 * Created date 2025/9/9 10:34
 * Describe : TuyaScanDeviceApi
 */
@Singleton
internal class TuyaScanDeviceApi @Inject constructor(
    private val tuyaDeviceApi: TuyaDeviceApi
) : ITuyaScanDeviceApi {
    private val TAG = "TuyaScanDeviceApi"
    private val ldvUUID16List by lazy { listOf("1827") }
    private val scanDevicesFlow = MutableStateFlow<ScanDevice?>(null)
    private val bleScanResultAggregator by lazy {
        BleScanResultAggregator()
    }

    @Volatile
    private var tuyaScanDeviceSetting: TuyaScanDeviceSetting? = null
    private val tuyaLeScanTypeMapping = mapOf(
        TuyaScanDeviceType.SINGLE to ScanType.SINGLE,
        TuyaScanDeviceType.SINGLE_QR to ScanType.SINGLE_QR,
        TuyaScanDeviceType.MESH to ScanType.MESH,
        TuyaScanDeviceType.SIG_MESH to ScanType.SIG_MESH,
        TuyaScanDeviceType.NORMAL to ScanType.NORMAL,
        TuyaScanDeviceType.THING_BEACON to ScanType.THING_BEACON
    )

    private val gwSearcher by lazy {
        ThingHomeSdk.getActivatorInstance().newThingGwActivator().newSearcher()
    }

    override fun getScanDevicesFlow(): Flow<List<ScanDevice>> {
        return scanDevicesFlow.map {
            bleScanResultAggregator.aggregateDevices(it) {
                tuyaDeviceApi.getActivatorDeviceInfo(it.pid, it.uuid, it.mac)
            }
        }
    }

    private fun isContainsLeScan(): Boolean {
        val keys = tuyaLeScanTypeMapping.keys
        return tuyaScanDeviceSetting?.scanTypeList?.any { keys.contains(it) } ?: true
    }

    private fun isContainsCameraScan(): Boolean {
        return tuyaScanDeviceSetting?.scanTypeList?.contains(TuyaScanDeviceType.CAMERA) ?: true
    }

    private fun isContainsMatterScan(): Boolean {
        return tuyaScanDeviceSetting?.scanTypeList?.contains(TuyaScanDeviceType.MATTER) ?: true
    }

    override fun startScanDevice(setting: TuyaScanDeviceSetting) {
        Timber.tag(TAG).i("startScanDevice: call")
        bleScanResultAggregator.reset()
        if (tuyaScanDeviceSetting != null) {
            stopScanDevice()
        }
        val block: (ScanDevice) -> Unit = { device ->
            scanDevicesFlow.update { device }
        }
        tuyaScanDeviceSetting = setting
        if (isContainsLeScan()) {
            startLeScan(setting, block)
        }
        if (isContainsCameraScan()) {
            startCameraScan(block)
        }
        if (isContainsMatterScan()) {
            startMatterScan(block)
        }
    }

    override fun stopScanDevice() {
        Timber.tag(TAG).i("stopScanDevice: call")
        if (isContainsLeScan()) {
            tryCatch { stopLeScan() }
        }
        if (isContainsCameraScan()) {
            tryCatch { stopCameraScan() }
        }
        if (isContainsMatterScan()) {
            tryCatch { stopMatterScan() }
        }
        tuyaScanDeviceSetting = null
    }

    private fun startLeScan(setting: TuyaScanDeviceSetting, block: (ScanDevice) -> Unit) {
        val scanTypeList = setting.scanTypeList
        Timber.tag(TAG).i("startLeScan: call $scanTypeList")
        val scanSetting = LeScanSetting.Builder()
            .setTimeout(setting.timeout)
            .apply {
                scanTypeList.forEach { tuyaType ->
                    tuyaLeScanTypeMapping[tuyaType]?.let { addScanType(it) }
                }
            }
            .setNeedMatchUUID(setting.needMatchUUID)
            .setUUID(setting.uuid)
            .setRepeatFilter(setting.repeatFilter)
            .build()
        val timeout = scanSetting.timeout
        val needMatchUUID = setting.needMatchUUID
        Timber.tag(TAG).i("startLeScan: timeout->$timeout needMatchUUID->$needMatchUUID")
        if (needMatchUUID) {
            Timber.tag(TAG).i("startLeScan: uuid->${setting.uuid}")
        }
        Timber.tag(TAG).i("startLeScan: repeatFilter->${setting.repeatFilter}")
        ThingHomeSdk.getBleOperator().startLeScan(scanSetting) { deviceBean ->
            val isTuyaDevice = !deviceBean.productId.isNullOrEmpty()
            val device = when {
                isTuyaDevice -> {
                    Timber.tag(TAG).i("startLeScan: name-> ${deviceBean.toJson()}")
                    ScanDevice(
                        uuid = deviceBean.uuid,
                        pid = deviceBean.productId,
                        vendor = DeviceVendor.Tuya,
                        networkType = DeviceNetworkType.Wifi,
                        deviceType = deviceBean.deviceType,
                        address = deviceBean.address,
                        mac = deviceBean.mac,
                        name = deviceBean.name,
                        rssi = deviceBean.rssi
                    )
                }

                else -> {
                    val result = tryCatchReturn {
                        BleAdvertisingParser.parse(deviceBean.data)
                    }
                    val name = result?.completeLocalName
                    val uuid16 = result?.completeUuids16?.firstOrNull() ?: ""
                    if (!name.isNullOrEmpty() && ldvUUID16List.contains(uuid16)) {
                        Timber.tag(TAG).d("startLeScan: result->${result.toJson()}")
                        ScanDevice(
                            uuid = deviceBean.mac,
                            pid = deviceBean.mac,
                            vendor = DeviceVendor.Ledvance,
                            networkType = DeviceNetworkType.Ble,
                            deviceType = deviceBean.deviceType,
                            mac = deviceBean.mac,
                            address = deviceBean.mac,
                            name = name,
                            rssi = deviceBean.rssi
                        )
                    } else null
                }
            }
            device?.run { block.invoke(this) }
        }
    }

    private fun stopLeScan() {
        Timber.tag(TAG).i("stopLeScan: call")
        ThingHomeSdk.getBleOperator().stopLeScan()
    }

    private fun startCameraScan(block: (ScanDevice) -> Unit) {
        Timber.tag(TAG).i("startCameraScan: call")
        gwSearcher.registerGwSearchListener { hgwBean ->
            Timber.tag(TAG).i("startCameraScan: hgwBean->${hgwBean.toJson()}")
            hgwBean ?: return@registerGwSearchListener
            val device = ScanDevice(
                uuid = hgwBean.getGwId() ?: "",
                pid = hgwBean.productKey ?: "",
                vendor = DeviceVendor.Tuya,
                networkType = DeviceNetworkType.Camera,
                name = "Camera",
                extraObjectJson = hgwBean.toJson(),
            )
            block(device)
        }
    }

    private fun stopCameraScan() {
        Timber.tag(TAG).i("stopCameraScan: call")
        gwSearcher.unRegisterGwSearchListener()
    }

    private fun startMatterScan(block: (ScanDevice) -> Unit) {
        Timber.tag(TAG).i("startMatterScan: call")
        ThingHomeSdk.getDiscoveryActivatorInstance()?.startDiscovery(object :
            IDynamicDiscoveryListener {
            override fun onFound(discovery: ThingMatterDiscovery?) {
                Timber.tag(TAG).i("startMatterScan onFound: ${discovery?.productName}")
                discovery?.also {
                    val setupPayload = it.payload ?: return
                    val vendorId = setupPayload.vendorId
                    val productId = setupPayload.productId
                    val discriminatorValue = setupPayload.discriminator?.value ?: 0
                    //  String id = vendrorId + productId + discriminator.value 涂鸦说这三个字段拼接在一起 算一个唯一码
                    val uuid = "${vendorId}${productId}${discriminatorValue}"
                    val device = ScanDevice(
                        uuid = uuid,
                        pid = setupPayload.productId.toString(),
                        vendor = DeviceVendor.Tuya,
                        networkType = DeviceNetworkType.Matter,
                        name = it.productName ?: "",
                        icon = it.iconUrlStr ?: "",
                        extraObjectJson = setupPayload.toJson(),
                    )
                    block(device)
                }
            }

            override fun onError(errorCode: String?, errorMsg: String?) {
                Timber.tag(TAG).e("startMatterScan onError: $errorCode $errorMsg")
            }
        })
    }

    private fun stopMatterScan() {
        Timber.tag(TAG).i("stopMatterScan: call")
        ThingHomeSdk.getDiscoveryActivatorInstance()?.stopDiscovery()
    }
}