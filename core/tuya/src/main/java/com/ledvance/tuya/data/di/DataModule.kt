package com.ledvance.tuya.data.di

import com.ledvance.tuya.data.repo.ITuyaRepo
import com.ledvance.tuya.data.repo.TuyaRepo
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * @author : jason yin
 * Email : j.yin@ledvance.com
 * Created date 2025/9/5 08:54
 * Describe : DataModule
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {
    @Binds
    internal abstract fun bindsTuyaRepo(
        tuyaRepo: TuyaRepo
    ): ITuyaRepo
}