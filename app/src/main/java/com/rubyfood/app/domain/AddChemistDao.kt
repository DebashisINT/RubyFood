package com.rubyfood.app.domain

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import android.arch.persistence.room.Update
import com.rubyfood.app.AppConstant

/**
 * Created by Saikat on 08-01-2020.
 */
@Dao
interface AddChemistDao {

    @Query("SELECT * FROM " + AppConstant.CHEMIST_VISIT_LIST_TABLE)
    fun getAll(): List<AddChemistEntity>

    @Insert
    fun insertAll(vararg addChem: AddChemistEntity)

    @Query("DELETE FROM " + AppConstant.CHEMIST_VISIT_LIST_TABLE)
    fun deleteAll()

    @Query("update " + AppConstant.CHEMIST_VISIT_LIST_TABLE + " set isUploaded=:isUploaded where chemist_visit_id=:chemist_visit_id")
    fun updateIsUploaded(isUploaded: Boolean, chemist_visit_id: String)

    @Query("SELECT * FROM " + AppConstant.CHEMIST_VISIT_LIST_TABLE + " where isUploaded=:isUploaded")
    fun getDataSyncWise(isUploaded: Boolean): List<AddChemistEntity>

    @Query("SELECT * FROM " + AppConstant.CHEMIST_VISIT_LIST_TABLE + " where shop_id=:shop_id order by visit_date desc")
    fun getDataShopIdWise(shop_id: String): List<AddChemistEntity>

    @Update
    fun updateAll(vararg addChem: AddChemistEntity)
}