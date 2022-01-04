package com.rubyfood.app.domain

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.rubyfood.app.AppConstant
import com.rubyfood.features.myprofile.model.citylist.CityListApiResponse
import io.reactivex.Observable
import retrofit2.http.GET

@Dao
interface OrderStatusRemarksDao {

    @Insert
    fun insert(vararg orderStatusRemarks: OrderStatusRemarksModelEntity)

    @Query("SELECT * FROM shop_order_status_remarks  where shop_revisit_uniqKey=:shop_revisit_uniqKey")
    fun getSingleItem(shop_revisit_uniqKey: String): OrderStatusRemarksModelEntity

    @Query("SELECT * FROM shop_order_status_remarks  where isUploaded = 0 ORDER BY id ASC")
    fun getUnsyncedList(): List<OrderStatusRemarksModelEntity>

    @Query("update shop_order_status_remarks  set isUploaded = 1 where  shop_revisit_uniqKey=:shop_revisit_uniqKey")
    fun updateOrderStatus(shop_revisit_uniqKey: String)
}