package com.rubyfood.app.domain

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.rubyfood.app.AppConstant

@Entity(tableName = AppConstant.DOCUMENT_LIST_TABLE)
class DocumentListEntity {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0

    @ColumnInfo(name = "list_id")
    var list_id: String? = null

    @ColumnInfo(name = "type_id")
    var type_id: String? = null

    @ColumnInfo(name = "date_time")
    var date_time: String? = null

    @ColumnInfo(name = "attachment")
    var attachment: String? = null

    @ColumnInfo(name = "isUploaded")
    var isUploaded: Boolean = false
}