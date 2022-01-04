package com.rubyfood.app.domain

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.rubyfood.app.AppConstant

/**
 * Created by Saikat on 11-May-20.
 */
@Dao
interface ProductRateDao {

    @Insert
    fun insert(vararg productRate: ProductRateEntity)

    @Query("DELETE FROM " + AppConstant.PRODUCT_RATE_TABLE)
    fun deleteAll()

    @Query("SELECT * FROM " + AppConstant.PRODUCT_RATE_TABLE)
    fun getAll(): List<ProductRateEntity>
}