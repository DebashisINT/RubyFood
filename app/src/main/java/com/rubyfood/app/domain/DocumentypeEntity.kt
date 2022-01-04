package com.rubyfood.app.domain

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.rubyfood.app.AppConstant

@Entity(tableName = AppConstant.DOCUMENT_TYPE_TABLE)
class DocumentypeEntity {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0

    @ColumnInfo(name = "type_id")
    var type_id: String? = null

    @ColumnInfo(name = "type_name")
    var type_name: String? = null

    @ColumnInfo(name = "image")
    var image: String? = null
}