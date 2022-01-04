package com.rubyfood.app.domain

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.rubyfood.app.AppConstant

/**
 * Created by Saikat on 17-Aug-20.
 */
@Dao
interface BatteryNetStatusDao {

    @Query("SELECT * FROM " + AppConstant.BATTERY_NET_TABLE)
    fun getAll(): List<BatteryNetStatusEntity>

    @Query("SELECT * FROM " + AppConstant.BATTERY_NET_TABLE + " where date=:date order by date_time")
    fun getDataDateWise(date: String): List<BatteryNetStatusEntity>

    @Query("SELECT * FROM " + AppConstant.BATTERY_NET_TABLE + " where isUploaded=:isUploaded")
    fun getDataSyncStateWise(isUploaded: Boolean): List<BatteryNetStatusEntity>

    @Query("update " + AppConstant.BATTERY_NET_TABLE + " set isUploaded=:isUploaded where id=:id")
    fun updateIsUploadedAccordingToId(isUploaded: Boolean, id: Int)

    @Insert
    fun insert(vararg batNet: BatteryNetStatusEntity)

    @Query("DELETE FROM " + AppConstant.BATTERY_NET_TABLE)
    fun delete()
}