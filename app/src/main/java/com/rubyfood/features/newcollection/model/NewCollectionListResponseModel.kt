package com.rubyfood.features.newcollection.model

import com.rubyfood.app.domain.CollectionDetailsEntity
import com.rubyfood.base.BaseResponse
import com.rubyfood.features.shopdetail.presentation.model.collectionlist.CollectionListDataModel

/**
 * Created by Saikat on 15-02-2019.
 */
class NewCollectionListResponseModel : BaseResponse() {
    //var collection_list: ArrayList<CollectionListDataModel>? = null
    var collection_list: ArrayList<CollectionDetailsEntity>? = null
}