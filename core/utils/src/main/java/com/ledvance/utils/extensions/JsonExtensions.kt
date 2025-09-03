package com.ledvance.utils.extensions

import com.alibaba.fastjson2.JSON

/**
 * @author : jason yin
 * Email : j.yin@ledvance.com
 * Created date 2025/5/30 08:30
 * Describe : JsonExtensions
 */

/**
 * 将对象序列化为 JSON 字符串
 */
fun Any?.toJson(): String? = try {
    JSON.toJSONString(this)
} catch (e: Exception) {
    e.printStackTrace()
    null
}

/**
 * 将 JSON 字符串反序列化为对象，类型自动推断
 */
inline fun <reified T> String.jsonAsOrNull(): T? = try {
    JSON.parseObject(this, T::class.java)
} catch (e: Exception) {
    e.printStackTrace()
    null
}