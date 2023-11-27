package com.rubyfood.features.location.api

import com.rubyfood.features.location.shopdurationapi.ShopDurationApi
import com.rubyfood.features.location.shopdurationapi.ShopDurationRepository


object LocationRepoProvider {
    fun provideLocationRepository(): LocationRepo {
        return LocationRepo(LocationApi.create())
    }
}