package com.rubyfood.app.domain

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.rubyfood.app.AppConstant

/**
 * Created by Saikat on 11-09-2019.
 */
@Dao
interface StockProductListDao {

    @Query("SELECT * FROM " + AppConstant.STOCK_PRODUCT_LIST)
    fun getAll(): List<StockProductListEntity>

    @Query("SELECT * FROM " + AppConstant.STOCK_PRODUCT_LIST + " where stock_id=:stock_id and shop_id=:shop_id")
    fun getDataAccordingToShopAndStockId(stock_id: String, shop_id: String): List<StockProductListEntity>

    @Query("SELECT * FROM " + AppConstant.STOCK_PRODUCT_LIST + " where stock_id=:stock_id")
    fun getDataAccordingToStockId(stock_id: String): List<StockProductListEntity>

    @Insert
    fun insert(vararg stockProductList: StockProductListEntity)

    @Query("DELETE FROM " + AppConstant.STOCK_PRODUCT_LIST)
    fun delete()
}