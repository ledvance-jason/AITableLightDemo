package com.ledvance.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.ledvance.database.model.DeviceEntity

/**
 * @author : jason yin
 * Email : j.yin@ledvance.com
 * Created date 2023/11/23 17:57
 * Describe : DeviceDao
 */
@Dao
interface DeviceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(device: DeviceEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(devices: List<DeviceEntity>)

    @Delete
    suspend fun delete(device: DeviceEntity)

}