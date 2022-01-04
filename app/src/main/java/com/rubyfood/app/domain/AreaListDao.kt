package com.rubyfood.app.domain

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.rubyfood.app.AppConstant

/**
 * Created by Saikat on 14-May-20.
 */
@Dao
interface AreaListDao {

    @Query("SELECT * FROM " + AppConstant.AREA_LIST_TABLE)
    fun getAll(): List<AreaListEntity>

    @Query("SELECT * FROM " + AppConstant.AREA_LIST_TABLE + " where area_id=:area_id")
    fun getSingleArea(area_id: String): AreaListEntity

    @Insert
    fun insert(vararg area: AreaListEntity)

    @Query("DELETE FROM " + AppConstant.AREA_LIST_TABLE)
    fun deleteAll()
}