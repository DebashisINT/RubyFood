package com.rubyfood.app.domain

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.rubyfood.app.AppConstant

/**
 * Created by Saikat on 08-11-2018.
 */
@Dao
interface LeaveTypeDao {

    @Query("SELECT * FROM " + AppConstant.LEAVE_TYPE_TABLE)
    fun getAll(): List<LeaveTypeEntity>

    @Insert
    fun insert(vararg leaveType: LeaveTypeEntity)

    @Query("DELETE FROM " + AppConstant.LEAVE_TYPE_TABLE)
    fun delete()
}