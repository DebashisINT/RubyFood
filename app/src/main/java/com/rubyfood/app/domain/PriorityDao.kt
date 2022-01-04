package com.rubyfood.app.domain

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.rubyfood.app.AppConstant

@Dao
interface PriorityDao {

    @Query("SELECT * FROM " + AppConstant.PRIORITY_TABLE)
    fun getAll(): List<PriorityListEntity>

    @Insert
    fun insertAll(vararg priority: PriorityListEntity)

    @Query("DELETE FROM " + AppConstant.PRIORITY_TABLE)
    fun deleteAll()

    @Query("SELECT * FROM " + AppConstant.PRIORITY_TABLE + " where priority_id=:priority_id")
    fun getSingleType(priority_id: String): PriorityListEntity
}