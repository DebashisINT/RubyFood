package com.rubyfood.app.domain

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.rubyfood.app.AppConstant

/**
 * Created by Saikat on 13-11-2018.
 */
@Dao
interface StockListDao {

    @Query("SELECT * FROM " + AppConstant.STOCK_LIST_TABLE/* + " ORDER BY current_date DESC"*/)
    fun getAll(): List<StockListEntity>

    @Query("SELECT * FROM " + AppConstant.STOCK_LIST_TABLE + " where shop_id=:shop_id ORDER BY id DESC")
    fun getStockAccordingToShopId(shop_id: String): List<StockListEntity>

    @Insert
    fun insert(vararg stockList: StockListEntity)

    @Query("DELETE FROM " + AppConstant.STOCK_LIST_TABLE)
    fun delete()
}