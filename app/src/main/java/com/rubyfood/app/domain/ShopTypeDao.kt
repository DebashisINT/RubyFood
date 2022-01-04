package com.rubyfood.app.domain

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.rubyfood.app.AppConstant

/**
 * Created by Saikat on 01-Jun-20.
 */
@Dao
interface ShopTypeDao {

    @Query("SELECT * FROM " + AppConstant.SHOP_TYPE)
    fun getAll(): List<ShopTypeEntity>

    @Query("SELECT * FROM " + AppConstant.SHOP_TYPE + " where shoptype_id=:shoptype_id")
    fun getSingleType(shoptype_id: String): ShopTypeEntity


    @Insert
    fun insertAll(vararg shopType: ShopTypeEntity)

    @Query("DELETE FROM " + AppConstant.SHOP_TYPE)
    fun deleteAll()
}