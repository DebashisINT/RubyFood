package com.rubyfood.app.domain

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.rubyfood.app.AppConstant

/**
 * Created by Saikat on 05-Jun-20.
 */
@Dao
interface StageDao {
    @Query("SELECT * FROM " + AppConstant.STAGE_TABLE)
    fun getAll(): List<StageEntity>

    @Insert
    fun insertAll(vararg stage: StageEntity)

    @Query("DELETE FROM " + AppConstant.STAGE_TABLE)
    fun deleteAll()

    @Query("SELECT * FROM " + AppConstant.STAGE_TABLE + " where stage_id=:stage_id")
    fun getSingleType(stage_id: String): StageEntity
}