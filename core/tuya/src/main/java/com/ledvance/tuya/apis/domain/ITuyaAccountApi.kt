package com.ledvance.tuya.apis.domain

import com.thingclips.smart.android.user.bean.User
import kotlinx.coroutines.flow.Flow

/**
 * @author : jason yin
 * Email : j.yin@ledvance.com
 * Created date 2025/9/5 08:40
 * Describe : ITuyaAccountApi
 */
interface ITuyaAccountApi {
    suspend fun loginWithUid(countryCode: String, uid: String, password: String): Result<User?>
    suspend fun loginWithEmail(countryCode: String, email: String, password: String): Result<User?>
    suspend fun logout(): Result<Boolean>
    fun isLogin(): Boolean
    fun getUserFlow(): Flow<User?>
}