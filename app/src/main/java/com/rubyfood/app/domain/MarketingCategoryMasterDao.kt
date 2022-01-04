package com.rubyfood.app.domain

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query

/**
 * Created by Pratishruti on 05-03-2018.
 */
@Dao
interface MarketingCategoryMasterDao {
    @Query("SELECT * FROM marketing_category_master_table")
    fun getAll(): List<MarketingCategoryMasterEntity>

    @Insert
    fun insertAll(vararg marketingdetail: MarketingCategoryMasterEntity)

    @Query("Select material_id from marketing_category_master_table where material_name=:cat_name")
    fun getMarketingCategoryIdFromName(cat_name:String):String

    @Query("Select material_name from marketing_category_master_table where material_id=:material_id")
    fun getMarketingCategoryNameFromId(material_id:String):String

    @Query("Delete from marketing_category_master_table")
    fun deleteAll()
}