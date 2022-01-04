package com.rubyfood.app.domain

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.rubyfood.app.AppConstant

/**
 * Created by Saikat on 10-Jul-20.
 */
@Dao
interface ProjectListDao {

    @Query("SELECT * FROM " + AppConstant.PROJECT_LIST)
    fun getAll(): List<ProjectListEntity>

    @Insert
    fun insertAll(vararg project: ProjectListEntity)

    @Query("DELETE FROM " + AppConstant.PROJECT_LIST)
    fun deleteAll()

}