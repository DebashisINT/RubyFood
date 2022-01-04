package com.rubyfood.app.domain

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.rubyfood.app.AppConstant

/**
 * Created by Saikat on 26-10-2018.
 */
@Entity(tableName = AppConstant.GPS_STATUS_TABLE)
class GpsStatusEntity {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0

    @ColumnInfo(name = "gps_id")
    var gps_id: String? = null

    @ColumnInfo(name = "date")
    var date: String? = null

    @ColumnInfo(name = "gps_off_time")
    var gps_off_time: String? = null

    @ColumnInfo(name = "gps_on_time")
    var gps_on_time: String? = null

    @ColumnInfo(name = "duration")
    var duration: String? = null

    @ColumnInfo(name = "isUploaded")
    var isUploaded: Boolean = false
}