package com.rubyfood.app.domain

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.rubyfood.app.AppConstant

@Dao
interface PaymentModeDao {
    @Query("SELECT * FROM " + AppConstant.PAYMENT_MODE_TABLE)
    fun getAll(): List<PaymentModeEntity>

    @Insert
    fun insert(vararg performance: PaymentModeEntity)

    @Query("DELETE FROM " + AppConstant.PAYMENT_MODE_TABLE)
    fun delete()

    @Query("SELECT * FROM " + AppConstant.PAYMENT_MODE_TABLE + " where payment_id=:payment_id")
    fun getSingleData(payment_id: String): PaymentModeEntity
}