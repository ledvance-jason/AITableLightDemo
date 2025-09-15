package com.ledvance.tuya.command

import kotlinx.coroutines.flow.Flow

/**
 * @author : jason yin
 * Email : j.yin@ledvance.com
 * Created date 2025/9/11 10:01
 * Describe : Dp
 */
internal data class Dp<T>(
    val dpId: String,
    val dpFlow: Flow<T>,
    val setDpValue: suspend (T) -> Result<Boolean>
)