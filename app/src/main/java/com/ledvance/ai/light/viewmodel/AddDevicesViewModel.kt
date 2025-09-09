package com.ledvance.ai.light.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ledvance.tuya.data.repo.ITuyaRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.sample
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

/**
 * @author : jason yin
 * Email : j.yin@ledvance.com
 * Created date 2025/9/9 17:10
 * Describe : AddDevicesViewModel
 */
@HiltViewModel
class AddDevicesViewModel @Inject constructor(
    private val tuyaRepo: ITuyaRepo
) : ViewModel() {
    private val TAG = "AddDevicesViewModel"

    @OptIn(FlowPreview::class)
    val scanDevicesFlow = tuyaRepo.getScanDeviceApi().getScanDevicesFlow()
        .sample(1000)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = listOf()
        )

    fun startScanDevice() {
        tuyaRepo.getScanDeviceApi().startScanDevice()
    }

    override fun onCleared() {
        super.onCleared()
        tuyaRepo.getScanDeviceApi().stopScanDevice()
    }

}