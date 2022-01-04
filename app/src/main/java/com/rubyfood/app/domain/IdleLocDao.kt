package com.rubyfood.app.domain

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.rubyfood.app.AppConstant

/**
 * Created by Saikat on 04-02-2019.
 */
@Dao
interface IdleLocDao {
    @Query("SELECT * FROM " + AppConstant.IDEAL_LOCATION_TABLE)
    fun getAll(): List<IdleLocEntity>

    @Query("SELECT * FROM " + AppConstant.IDEAL_LOCATION_TABLE + " where isUploaded=:isUploaded")
    fun getDataSyncStateWise(isUploaded: Boolean): List<IdleLocEntity>

    @Query("update " + AppConstant.IDEAL_LOCATION_TABLE + " set isUploaded=:isUploaded where id=:id")
    fun updateIsUploadedAccordingToId(isUploaded: Boolean, id: Int)

    @Insert
    fun insert(vararg ideal: IdleLocEntity)

    @Query("DELETE FROM " + AppConstant.IDEAL_LOCATION_TABLE)
    fun delete()
}