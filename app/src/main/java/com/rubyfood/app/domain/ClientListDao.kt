package com.rubyfood.app.domain

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.rubyfood.app.AppConstant

/**
 * Created by Saikat on 10-Jul-20.
 */
@Dao
interface ClientListDao {

    @Query("SELECT * FROM " + AppConstant.CLIENT_LIST)
    fun getAll(): List<ClientListEntity>

    @Insert
    fun insertAll(vararg client: ClientListEntity)

    @Query("DELETE FROM " + AppConstant.CLIENT_LIST)
    fun deleteAll()
}