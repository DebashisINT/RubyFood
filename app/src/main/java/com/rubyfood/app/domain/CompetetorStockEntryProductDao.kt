package com.rubyfood.app.domain

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.rubyfood.app.AppConstant

@Dao
interface CompetetorStockEntryProductDao {
    @Insert
    fun insert(vararg competetorStockProductEntry: CompetetorStockEntryProductModelEntity)

    @Query("Select * from "+ AppConstant.SHOP_COMTETETOR_STOCK_PRODUCTS_TABLE +" where competitor_stock_id=:competitor_stock_id and isUploaded = 0 ")
    fun getComProductStockByStockIDUnsynced(competitor_stock_id:String): List<CompetetorStockEntryProductModelEntity>

    @Query("Select * from "+ AppConstant.SHOP_COMTETETOR_STOCK_PRODUCTS_TABLE+" where shop_id=:shop_id" )
    fun getComProductStockAllByShopID(shop_id:String): List<CompetetorStockEntryProductModelEntity>

    @Query("Select * from "+ AppConstant.SHOP_COMTETETOR_STOCK_PRODUCTS_TABLE+" where competitor_stock_id=:competitor_stock_id" )
    fun getComProductStockAllByStockID(competitor_stock_id:String): List<CompetetorStockEntryProductModelEntity>

    @Query("update "+ AppConstant.SHOP_COMTETETOR_STOCK_PRODUCTS_TABLE+ " set isUploaded = 1 where competitor_stock_id=:competitor_stock_id" )
    fun syncShopCompProductable(competitor_stock_id:String)

}