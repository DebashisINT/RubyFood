package com.rubyfood.app.domain

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.rubyfood.app.AppConstant

/**
 * Created by Saikat on 30-11-2018.
 */
@Dao
interface SelectedRouteDao {

    @Query("SELECT * FROM " + AppConstant.SELECTED_ROUTE_LIST_TABLE)
    fun getAll(): List<SelectedRouteEntity>

    @Query("SELECT * FROM " + AppConstant.SELECTED_ROUTE_LIST_TABLE + " where date=:date")
    fun getTodaysData(date: String): List<SelectedRouteEntity>

    @Query("DELETE FROM " + AppConstant.SELECTED_ROUTE_LIST_TABLE)
    fun deleteRoute()

    @Insert
    fun insert(vararg route: SelectedRouteEntity)
}