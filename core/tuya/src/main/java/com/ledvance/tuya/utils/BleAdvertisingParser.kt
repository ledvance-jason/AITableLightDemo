package com.ledvance.tuya.utils

/**
 * @author : jason yin
 * Email : j.yin@ledvance.com
 * Created date 2025/9/10 14:07
 * Describe : BleAdvertisingParser
 */
import java.nio.charset.StandardCharsets

/**
 * BLE广播数据解析结果实体类
 */
internal data class BleAdvertisingInfo(
    val flags: Map<String, Boolean>? = null,
    val completeLocalName: String? = null,
    val shortLocalName: String? = null,
    val completeUuids16: List<String>? = null,
    val incompleteUuids16: List<String>? = null,
    val completeUuids32: List<String>? = null,
    val incompleteUuids32: List<String>? = null,
    val completeUuids128: List<String>? = null,
    val incompleteUuids128: List<String>? = null,
    val serviceData16: Map<String, ByteArray>? = null,
    val serviceData32: Map<String, ByteArray>? = null,
    val serviceData128: Map<String, ByteArray>? = null,
    val manufacturerData: Map<Int, ByteArray>? = null,
    val txPowerLevel: Int? = null,
    val unknownFields: Map<Int, ByteArray> = emptyMap()
)

/**
 * BLE广播原始数据全解析工具
 */
internal object BleAdvertisingParser {

    private fun revertRaw(str: String): ByteArray {
        // 创建与字符串长度相同的字节数组
        val byteArray = ByteArray(str.length)
        // 遍历字符串每个字符，将其转换回对应的字节值
        for (i in str.indices) {
            // 关键：char 转 byte 时取低8位（与原方法的 byte 转 char 对应）
            byteArray[i] = str[i].code.toByte()
        }
        return byteArray
    }

    fun parse(rawData: String): BleAdvertisingInfo {
        val rawByteArray = revertRaw(rawData)
        return parse(rawByteArray)
    }

    /**
     * 解析BLE广播原始字节数组
     * @param rawData 广播数据字节数组（通常来自ScanRecord.getBytes()）
     * @return 解析结果实体类
     */
    fun parse(rawData: ByteArray): BleAdvertisingInfo {
        val flags = mutableMapOf<String, Boolean>()
        var completeLocalName: String? = null
        var shortLocalName: String? = null
        val completeUuids16 = mutableListOf<String>()
        val incompleteUuids16 = mutableListOf<String>()
        val completeUuids32 = mutableListOf<String>()
        val incompleteUuids32 = mutableListOf<String>()
        val completeUuids128 = mutableListOf<String>()
        val incompleteUuids128 = mutableListOf<String>()
        val serviceData16 = mutableMapOf<String, ByteArray>()
        val serviceData32 = mutableMapOf<String, ByteArray>()
        val serviceData128 = mutableMapOf<String, ByteArray>()
        val manufacturerData = mutableMapOf<Int, ByteArray>()
        var txPowerLevel: Int? = null
        val unknownFields = mutableMapOf<Int, ByteArray>()

        var index = 0
        val dataLength = rawData.size

        while (index < dataLength) {
            // 1. 读取AD单元长度（1字节）：长度 = 类型(1字节) + 数据(N字节)
            val adLength = rawData[index].toInt() and 0xFF
            if (adLength <= 0) {
                index++
                continue
            }

            // 2. 检查数据边界
            if (index + adLength + 1 > dataLength) break

            // 3. 读取AD类型（1字节）
            val adType = rawData[index + 1].toInt() and 0xFF

            // 4. 读取AD数据（值）
            val adData = rawData.copyOfRange(index + 2, index + 1 + adLength)

            // 5. 按类型解析数据
            when (adType) {
                // 标志位（Flags）
                0x01 -> parseFlags(adData, flags)

                // 完整的16位UUID列表
                0x03 -> completeUuids16.addAll(parseUuids(adData, 16))

                // 不完整的16位UUID列表
                0x02 -> incompleteUuids16.addAll(parseUuids(adData, 16))

                // 完整的32位UUID列表
                0x05 -> completeUuids32.addAll(parseUuids(adData, 32))

                // 不完整的32位UUID列表
                0x04 -> incompleteUuids32.addAll(parseUuids(adData, 32))

                // 完整的128位UUID列表
                0x07 -> completeUuids128.addAll(parseUuids(adData, 128))

                // 不完整的128位UUID列表
                0x06 -> incompleteUuids128.addAll(parseUuids(adData, 128))

                // 短本地名称
                0x08 -> shortLocalName =
                    String(adData, StandardCharsets.UTF_8).trimEnd { it == '\u0000' || it == ' ' }

                // 完整本地名称
                0x09 -> completeLocalName =
                    String(adData, StandardCharsets.UTF_8).trimEnd { it == '\u0000' || it == ' ' }

                // TX功率级别
                0x0A -> txPowerLevel = adData.getOrNull(0)?.toInt()

                // 16位UUID服务数据
                0x16 -> parseServiceData(adData, 16, serviceData16)

                // 32位UUID服务数据
                0x20 -> parseServiceData(adData, 32, serviceData32)

                // 128位UUID服务数据
                0x21 -> parseServiceData(adData, 128, serviceData128)

                // 厂商特定数据
                0xFF -> parseManufacturerData(adData, manufacturerData)

                // 未知类型
                else -> unknownFields[adType] = adData
            }

            // 6. 移动到下一个AD单元
            index += adLength + 1
        }

        return BleAdvertisingInfo(
            flags = flags.ifEmpty { null },
            completeLocalName = completeLocalName,
            shortLocalName = shortLocalName,
            completeUuids16 = completeUuids16.ifEmpty { null },
            incompleteUuids16 = incompleteUuids16.ifEmpty { null },
            completeUuids32 = completeUuids32.ifEmpty { null },
            incompleteUuids32 = incompleteUuids32.ifEmpty { null },
            completeUuids128 = completeUuids128.ifEmpty { null },
            incompleteUuids128 = incompleteUuids128.ifEmpty { null },
            serviceData16 = serviceData16.ifEmpty { null },
            serviceData32 = serviceData32.ifEmpty { null },
            serviceData128 = serviceData128.ifEmpty { null },
            manufacturerData = manufacturerData.ifEmpty { null },
            txPowerLevel = txPowerLevel,
            unknownFields = unknownFields
        )
    }

    /**
     * 解析标志位（Flags）
     * 参考蓝牙SIG规范：0x01类型定义
     */
    private fun parseFlags(data: ByteArray, flags: MutableMap<String, Boolean>) {
        if (data.isEmpty()) return
        val flagValue = data[0].toInt() and 0xFF
        flags["LE_LIMITED_DISCOVERABLE_MODE"] = (flagValue and 0x01) != 0
        flags["LE_GENERAL_DISCOVERABLE_MODE"] = (flagValue and 0x02) != 0
        flags["BR_EDR_NOT_SUPPORTED"] = (flagValue and 0x04) != 0
        flags["LE_BR_EDR_CONTROLLER"] = (flagValue and 0x08) != 0
        flags["LE_BR_EDR_HOST"] = (flagValue and 0x10) != 0
    }

    /**
     * 解析UUID列表
     * @param data 原始数据
     * @param bitLength UUID长度（16/32/128）
     */
    private fun parseUuids(data: ByteArray, bitLength: Int): List<String> {
        val uuids = mutableListOf<String>()
        val byteCount = bitLength / 8 // 16位=2字节，32位=4字节，128位=16字节
        if (data.size % byteCount != 0) return uuids

        for (i in data.indices step byteCount) {
            val uuidBytes = data.copyOfRange(i, i + byteCount)
            uuids.add(formatUuid(uuidBytes, bitLength))
        }
        return uuids
    }

    /**
     * 格式化UUID为标准字符串
     */
    private fun formatUuid(bytes: ByteArray, bitLength: Int): String {
        return when (bitLength) {
            16 -> String.format("%02X%02X", bytes[1], bytes[0]) // 小端转大端
            32 -> String.format("%02X%02X%02X%02X", bytes[3], bytes[2], bytes[1], bytes[0])
            128 -> buildString { // 128位UUID按8-4-4-4-12格式
                append(bytes.sliceArray(15..15).toHexString())
                append(bytes.sliceArray(14..14).toHexString())
                append(bytes.sliceArray(13..13).toHexString())
                append(bytes.sliceArray(12..12).toHexString())
                append("-")
                append(bytes.sliceArray(11..11).toHexString())
                append(bytes.sliceArray(10..10).toHexString())
                append("-")
                append(bytes.sliceArray(9..9).toHexString())
                append(bytes.sliceArray(8..8).toHexString())
                append("-")
                append(bytes.sliceArray(7..6).toHexString())
                append("-")
                append(bytes.sliceArray(5..0).toHexString())
            }

            else -> bytes.toHexString()
        }
    }

    /**
     * 解析服务数据（Service Data）
     */
    private fun parseServiceData(
        data: ByteArray,
        uuidBitLength: Int,
        resultMap: MutableMap<String, ByteArray>
    ) {
        val uuidByteCount = uuidBitLength / 8
        if (data.size < uuidByteCount) return

        // 前N字节为UUID，剩余为服务数据
        val uuidBytes = data.copyOfRange(0, uuidByteCount)
        val serviceData = data.copyOfRange(uuidByteCount, data.size)
        val uuid = formatUuid(uuidBytes, uuidBitLength)
        resultMap[uuid] = serviceData
    }

    /**
     * 解析厂商特定数据（Manufacturer Specific Data）
     * 前2字节为厂商ID（小端），剩余为厂商自定义数据
     */
    private fun parseManufacturerData(data: ByteArray, resultMap: MutableMap<Int, ByteArray>) {
        if (data.size < 2) return
        val manufacturerId = (data[1].toInt() and 0xFF shl 8) or (data[0].toInt() and 0xFF)
        val manufacturerSpecificData =
            if (data.size > 2) data.copyOfRange(2, data.size) else ByteArray(0)
        resultMap[manufacturerId] = manufacturerSpecificData
    }

    /**
     * 字节数组转十六进制字符串
     */
    private fun ByteArray.toHexString(): String = joinToString("") { "%02X".format(it) }
}