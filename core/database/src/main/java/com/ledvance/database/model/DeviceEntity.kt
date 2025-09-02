package com.ledvance.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @author : jason yin
 * Email : j.yin@ledvance.com
 * Created date 2023/11/23 17:57
 * Describe : DeviceEntity
 */
@Entity(tableName = "devices")
data class DeviceEntity(
    @PrimaryKey
    val id: String,
    val groupId: Long,
    val projectId: Long,
    val name: String,
    @ColumnInfo(name = "node_id")
    val nodeId: String,
    @ColumnInfo(name = "device_type")
    val deviceType: Int,
    @ColumnInfo(name = "is_master_sensor")
    val isMasterSensor: Boolean = false,
    @ColumnInfo(defaultValue = "0")
    val ambient: Int = 0,
    @ColumnInfo(name = "create_time")
    val createTime: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "update_time")
    val updateTime: Long = System.currentTimeMillis(),
)