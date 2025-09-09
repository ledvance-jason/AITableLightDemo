package com.ledvance.tuya.beans

/**
 * @author : jason yin
 * Email : j.yin@ledvance.com
 * Created date 2025/9/9 16:25
 * Describe : TuyaScanDeviceSetting
 */
class TuyaScanDeviceSetting private constructor(
    val timeout: Long,
    val scanTypeList: List<TuyaScanDeviceType>,
    val repeatFilter: Boolean,
    val uuid: String?,
    val needMatchUUID: Boolean
) {
    class Builder {
        private var timeout: Long = 30000L
        private val scanTypeList: MutableList<TuyaScanDeviceType> = mutableListOf()
        private var repeatFilter: Boolean = true
        private var uuid: String? = null
        private var needMatchUUID: Boolean = false

        fun setUUID(uuid: String): Builder {
            this.uuid = uuid
            return this
        }

        fun setNeedMatchUUID(needMatchUUID: Boolean): Builder {
            this.needMatchUUID = needMatchUUID
            return this
        }

        fun setTimeout(timeout: Long): Builder {
            this.timeout = timeout
            return this
        }

        fun addScanType(type: TuyaScanDeviceType): Builder {
            scanTypeList.add(type)
            return this
        }

        fun setRepeatFilter(filter: Boolean): Builder {
            this.repeatFilter = filter
            return this
        }

        fun build(): TuyaScanDeviceSetting {
            require(scanTypeList.isNotEmpty()) { "At least one TuyaScanDeviceType needs to be added!" }
            return TuyaScanDeviceSetting(
                timeout = timeout,
                scanTypeList = scanTypeList.toList(),
                repeatFilter = repeatFilter,
                uuid = uuid,
                needMatchUUID = needMatchUUID
            )
        }
    }
}
