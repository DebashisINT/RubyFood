package com.rubyfood.app.domain

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.rubyfood.app.AppConstant

@Dao
interface ShopTypeStockViewStatusDao {

    @Insert
    fun insertAll(vararg shopTypeStockViewStatus: ShopTypeStockViewStatus)

    @Query("Select CurrentStockEnable from "+ AppConstant.SHOP_TYPE_STOCK_VIEW_STATUS+ " where shoptype_id =:shoptype_id " )
    fun getShopCurrentStockViewStatus(shoptype_id:String) : Int

    @Query("Select CompetitorStockEnable from "+ AppConstant.SHOP_TYPE_STOCK_VIEW_STATUS+ " where shoptype_id =:shoptype_id " )
    fun getShopCompetitorStockViewStatus(shoptype_id:String) : Int

    @Query("DELETE FROM " + AppConstant.SHOP_TYPE_STOCK_VIEW_STATUS)
    fun deleteAll()
}