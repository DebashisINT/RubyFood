package com.rubyfood.app.domain

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.rubyfood.app.AppConstant

@Dao
interface RetailerDao {
    @Query("SELECT * FROM " + AppConstant.RETAILER_TABLE)
    fun getAll(): List<RetailerEntity>

    @Insert
    fun insert(vararg retailer: RetailerEntity)

    @Query("DELETE FROM " + AppConstant.RETAILER_TABLE)
    fun delete()

    @Query("SELECT * FROM " + AppConstant.RETAILER_TABLE + " where retailer_id=:retailer_id")
    fun getSingleItem(retailer_id: String): RetailerEntity

    @Query("SELECT * FROM " + AppConstant.RETAILER_TABLE + " where type_id=:type_id")
    fun getItemTypeWise(type_id: String): List<RetailerEntity>
}