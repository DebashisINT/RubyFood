package com.rubyfood.app.domain

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.rubyfood.app.AppConstant

/**
 * Created by Saikat on 05-Jun-20.
 */
@Dao
interface LeadTypeDao {
    @Query("SELECT * FROM " + AppConstant.LEAD_TABLE)
    fun getAll(): List<LeadTypeEntity>

    @Insert
    fun insertAll(vararg lead: LeadTypeEntity)

    @Query("DELETE FROM " + AppConstant.LEAD_TABLE)
    fun deleteAll()

    @Query("SELECT * FROM " + AppConstant.LEAD_TABLE + " where lead_id=:lead_id")
    fun getSingleType(lead_id: String): LeadTypeEntity
}