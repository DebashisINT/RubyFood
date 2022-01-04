package com.rubyfood.app.domain

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.rubyfood.app.AppConstant

/**
 * Created by Saikat on 26-10-2018.
 */
@Dao
interface CollectionDetailsDao {

    @Query("SELECT * FROM " + AppConstant.COLLECTION_LIST_TABLE + " order by id desc")
    fun getAll(): List<CollectionDetailsEntity>

    @Query("SELECT * FROM " + AppConstant.COLLECTION_LIST_TABLE + " where shop_id=:shop_id order by id desc")
    fun getListAccordingToShopId(shop_id: String): List<CollectionDetailsEntity>

    @Query("SELECT * FROM " + AppConstant.COLLECTION_LIST_TABLE + " where isUploaded=:isUploaded and collection_id=:collection_id")
    fun getUnsyncListAccordingToOrderId(collection_id: String, isUploaded: Boolean): List<CollectionDetailsEntity>

    @Insert
    fun insert(vararg collectionDetails: CollectionDetailsEntity)

    @Query("update " + AppConstant.COLLECTION_LIST_TABLE + " set isUploaded=:isUploaded where collection_id=:collection_id")
    fun updateIsUploaded(isUploaded: Boolean, collection_id: String)

    @Query("SELECT * FROM " + AppConstant.COLLECTION_LIST_TABLE + " where isUploaded=:isUploaded")
    fun getUnsyncCollection(isUploaded: Boolean): List<CollectionDetailsEntity>

    @Query("SELECT * FROM " + AppConstant.COLLECTION_LIST_TABLE + " where date=:date order by id desc")
    fun getDateWiseCollection(date: String): List<CollectionDetailsEntity>

    @Query("DELETE FROM " + AppConstant.COLLECTION_LIST_TABLE)
    fun delete()

    @Query("update " + AppConstant.COLLECTION_LIST_TABLE + " set file_path=:file_path where collection_id=:collection_id")
    fun updateAttachment(file_path: String, collection_id: String)

    @Query("SELECT * FROM " + AppConstant.COLLECTION_LIST_TABLE + " where order_id=:order_id")
    fun getListOrderWise(order_id: String): List<CollectionDetailsEntity>
}