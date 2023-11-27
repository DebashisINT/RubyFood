package com.rubyfood.features.alarm.api.visit_report_api

import com.rubyfood.app.Pref
import com.rubyfood.features.alarm.model.VisitReportResponseModel
import com.rubyfood.features.beatCustom.BeatTeamResponseModel
import io.reactivex.Observable

/**
 * Created by Saikat on 21-02-2019.
 */
class VisitReportRepo(val apiService: VisitReportApi) {
    fun getVisitReportList(from_date: String, to_date: String): Observable<VisitReportResponseModel> {
        return apiService.visitReportResponse(Pref.session_token!!, Pref.user_id!!, from_date, to_date)
    }

    fun getVisitReportListFromTeam(from_date: String, to_date: String,usrID:String): Observable<BeatTeamResponseModel> {
        return apiService.visitReportResponseTeam(Pref.session_token!!, usrID, from_date, to_date)
    }
}