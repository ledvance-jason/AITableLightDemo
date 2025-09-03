package com.ledvance.ai.light.viewmodel

import androidx.lifecycle.ViewModel
import com.ledvance.tuya.data.repo.ITuyaRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * @author : jason yin
 * Email : j.yin@ledvance.com
 * Created date 2025/9/3 13:32
 * Describe : LoginViewModel
 */
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val tuyaRepo: ITuyaRepo,
) : ViewModel() {


    suspend fun login(): Boolean {
        val user = tuyaRepo.getAccountApi().loginWithUid("86", "ledvanceaitablelight", "11111111")
        return user != null
    }
}