package com.rubyfood.app.domain

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.rubyfood.app.AppConstant

/**
 * Created by Saikat on 17-01-2020.
 */
@Dao
interface MeetingTypeDao {

    @Query("SELECT * FROM " + AppConstant.MEETING_TYPE)
    fun getAll(): List<MeetingTypeEntity>

    @Insert
    fun insertAll(vararg meetingType: MeetingTypeEntity)

    @Query("DELETE FROM " + AppConstant.MEETING_TYPE)
    fun deleteAll()

    @Query("SELECT * FROM " + AppConstant.MEETING_TYPE + " where typeId=:typeId")
    fun getSingleType(typeId: Int): MeetingTypeEntity
}