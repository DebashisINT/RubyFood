package com.rubyfood.app.domain

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.rubyfood.app.AppConstant

/**
 * Created by Saikat on 10-Jul-20.
 */
@Dao
interface TimesheetProductListDao {

    @Query("SELECT * FROM " + AppConstant.PRODUCT_LIST)
    fun getAll(): List<TimesheetProductListEntity>

    @Insert
    fun insertAll(vararg client: TimesheetProductListEntity)

    @Query("DELETE FROM " + AppConstant.PRODUCT_LIST)
    fun deleteAll()

}