package com.rubyfood.app.domain

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.rubyfood.app.AppConstant

/**
 * Created by Saikat on 30-11-2018.
 */
@Dao
interface SelectedWorkTypeDao {

    @Query("SELECT * FROM " + AppConstant.SELECTED_WORK_TYPE_TABLE)
    fun getAll(): List<SelectedWorkTypeEntity>

    @Query("SELECT * FROM " + AppConstant.SELECTED_WORK_TYPE_TABLE + " where date=:date")
    fun getTodaysData(date: String): List<SelectedWorkTypeEntity>

    @Query("DELETE FROM " + AppConstant.SELECTED_WORK_TYPE_TABLE)
    fun delete()

    @Insert
    fun insertAll(vararg workType: SelectedWorkTypeEntity)
}