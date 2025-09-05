package com.ledvance.utils.extensions

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

/**
 * @author : jason yin
 * Email : j.yin@ledvance.com
 * Created date 2025/9/4 16:44
 * Describe : MutableStateFlowExtensions
 */
inline fun <T> MutableStateFlow<List<T>>.updateList(operation: MutableList<T>.() -> Unit) =
    update {
        it.toMutableList().apply { operation() }
    }

inline fun <K, T> MutableStateFlow<Map<K, T>>.updateMap(operation: MutableMap<K, T>.() -> Unit) =
    update {
        it.toMutableMap().apply { operation() }
    }