package com.ledvance.ai.light.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ledvance.tuya.data.repo.ITuyaRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

/**
 * @author : jason yin
 * Email : j.yin@ledvance.com
 * Created date 2025/9/3 13:42
 * Describe : UserViewModel
 */
@HiltViewModel
class UserViewModel @Inject constructor(
    private val tuyaRepo: ITuyaRepo,
) : ViewModel() {
    val isLoginFlow = tuyaRepo.getAccountApi().getUserFlow().map {
        it != null
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = tuyaRepo.getAccountApi().isLogin()
    )
}