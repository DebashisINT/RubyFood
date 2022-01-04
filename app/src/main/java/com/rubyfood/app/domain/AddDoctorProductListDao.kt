package com.rubyfood.app.domain

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.rubyfood.app.AppConstant

/**
 * Created by Saikat on 09-01-2020.
 */
@Dao
interface AddDoctorProductListDao {

    @Query("SELECT * FROM " + AppConstant.DOCTOR_VISIT_PRODUCT_TABLE)
    fun getAll(): List<AddDoctorProductListEntity>

    @Insert
    fun insertAll(vararg addChemProd: AddDoctorProductListEntity)

    @Query("DELETE FROM " + AppConstant.DOCTOR_VISIT_PRODUCT_TABLE)
    fun deleteAll()

    @Query("SELECT * FROM " + AppConstant.DOCTOR_VISIT_PRODUCT_TABLE + " where doc_visit_id=:doc_visit_id")
    fun getDataIdWise(doc_visit_id: String): List<AddDoctorProductListEntity>

    @Query("SELECT * FROM " + AppConstant.DOCTOR_VISIT_PRODUCT_TABLE + " where doc_visit_id=:doc_visit_id and product_status=:product_status")
    fun getDataIdPodWise(doc_visit_id: String, product_status: Int): List<AddDoctorProductListEntity>

    @Query("DELETE FROM " + AppConstant.DOCTOR_VISIT_PRODUCT_TABLE + " where doc_visit_id=:doc_visit_id and product_status=:product_status")
    fun deleteStatusDocIdWise(doc_visit_id: String, product_status: Int)
}