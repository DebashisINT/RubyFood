package com.rubyfood.features.dailyPlan.api

import com.rubyfood.app.Pref
import com.rubyfood.base.BaseResponse
import com.rubyfood.features.dailyPlan.model.AllPlanListResponseModel
import com.rubyfood.features.dailyPlan.model.GetPlanDetailsResponseModel
import com.rubyfood.features.dailyPlan.model.GetPlanListResponseModel
import com.rubyfood.features.dailyPlan.model.UpdatePlanListInputParamsModel
import io.reactivex.Observable

/**
 * Created by Saikat on 24-12-2019.
 */
class PlanRepo(val apiService: PlanApi) {
    fun getPlanList(): Observable<GetPlanListResponseModel> {
        return apiService.getPlanList(Pref.session_token!!, Pref.user_id!!)
    }

    fun updatePlanList(updatePlan: UpdatePlanListInputParamsModel): Observable<BaseResponse> {
        return apiService.updatePlanList(updatePlan)
    }

    fun getPlanDetails(plan_id: String): Observable<GetPlanDetailsResponseModel> {
        return apiService.getPlanListDetails(Pref.session_token!!, Pref.user_id!!, plan_id)
    }

    fun getAllPlanList(): Observable<AllPlanListResponseModel> {
        return apiService.getAllPlanList(Pref.session_token!!, Pref.user_id!!)
    }
}