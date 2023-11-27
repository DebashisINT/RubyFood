package com.rubyfood.features.lead.api

import com.rubyfood.features.NewQuotation.api.GetQuotListRegRepository
import com.rubyfood.features.NewQuotation.api.GetQutoListApi


object GetLeadRegProvider {
    fun provideList(): GetLeadListRegRepository {
        return GetLeadListRegRepository(GetLeadListApi.create())
    }
}