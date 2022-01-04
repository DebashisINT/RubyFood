package com.rubyfood.app.domain

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.rubyfood.app.AppConstant

/**
 * Created by Saikat on 19-02-2019.
 */
@Dao
interface BillingDao {
    @Query("SELECT * FROM " + AppConstant.BILLING_TABLE)
    fun getAll(): List<BillingEntity>

    @Query("SELECT * FROM " + AppConstant.BILLING_TABLE + " where order_id=:order_id ORDER BY invoice_date DESC")
    fun getDataOrderIdWise(order_id: String): List<BillingEntity>

    @Query("SELECT * FROM " + AppConstant.BILLING_TABLE + " where isUploaded=:isUploaded")
    fun getDataSyncWise(isUploaded: Boolean): List<BillingEntity>

    @Insert
    fun insertAll(vararg bill: BillingEntity)

    @Query("update " + AppConstant.BILLING_TABLE + " set isUploaded=:isUploaded where id=:id")
    fun updateIsUploaded(isUploaded: Boolean, id: Int)

    @Query("update " + AppConstant.BILLING_TABLE + " set isUploaded=:isUploaded where bill_id=:bill_id")
    fun updateIsUploadedBillingIdWise(isUploaded: Boolean, bill_id: String)

    @Query("update " + AppConstant.BILLING_TABLE + " set isEditUploaded=:isEditUploaded where id=:id")
    fun updateIsEdited(isEditUploaded: Int, id: Int)

    @Query("update " + AppConstant.BILLING_TABLE + " set attachment=:attachment where id=:id")
    fun updateAttachment(attachment: String, id: Int)

    @Query("DELETE FROM " + AppConstant.BILLING_TABLE)
    fun deleteAll()
}