package com.rubyfood.features.dymanicSection.presentation

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rubyfood.R
import com.rubyfood.app.NetworkConstant
import com.rubyfood.app.types.FragType
import com.rubyfood.app.utils.AppUtils
import com.rubyfood.base.presentation.BaseActivity
import com.rubyfood.base.presentation.BaseFragment
import com.rubyfood.features.dashboard.presentation.DashboardActivity
import com.rubyfood.features.dymanicSection.api.DynamicRepoProvider
import com.rubyfood.features.dymanicSection.model.AllDynamicDataModel
import com.rubyfood.features.dymanicSection.model.AllDynamicListResponseModel
import com.rubyfood.widgets.AppCustomTextView
import com.elvishew.xlog.XLog
import com.pnikosis.materialishprogress.ProgressWheel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class AllDynamicListFragment : BaseFragment() {

    private lateinit var mContext: Context

    private lateinit var rv_all_dynamic_list: RecyclerView
    private lateinit var tv_no_data: AppCustomTextView
    private lateinit var progress_wheel: ProgressWheel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.fragment_all_dynamic_list, container, false)

        initView(view)
        getDynamicList()

        return view
    }

    private fun initView(view: View) {
        view.apply {
            rv_all_dynamic_list = findViewById(R.id.rv_all_dynamic_list)
            tv_no_data = findViewById(R.id.tv_no_data)
            progress_wheel = findViewById(R.id.progress_wheel)
        }
        progress_wheel.stopSpinning()
        rv_all_dynamic_list.layoutManager = LinearLayoutManager(mContext)
    }

    private fun getDynamicList() {
        if (!AppUtils.isOnline(mContext)) {
            (mContext as DashboardActivity).showSnackMessage(getString(R.string.no_internet))
            tv_no_data.visibility = View.VISIBLE
            return
        }

        val repository = DynamicRepoProvider.dynamicRepoProvider()
        progress_wheel.spin()
        BaseActivity.compositeDisposable.add(
                repository.getAllDynamicList()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({ result ->

                            progress_wheel.stopSpinning()

                            val response = result as AllDynamicListResponseModel

                            XLog.d("DYNAMIC ALL LIST RESPONSE=======> " + response.status)

                            if (response.status == NetworkConstant.SUCCESS) {
                                if (response.form_list != null && response.form_list!!.size > 0) {
                                    tv_no_data.visibility = View.GONE
                                    initAdapter(response.form_list)

                                } else {
                                    tv_no_data.visibility = View.VISIBLE
                                    (mContext as DashboardActivity).showSnackMessage(response.message!!)
                                }
                            } else {
                                tv_no_data.visibility = View.VISIBLE
                                (mContext as DashboardActivity).showSnackMessage(response.message!!)
                            }

                        }, { error ->
                            BaseActivity.isApiInitiated = false
                            error.printStackTrace()
                            progress_wheel.stopSpinning()
                            tv_no_data.visibility = View.VISIBLE
                            (mContext as DashboardActivity).showSnackMessage(getString(R.string.something_went_wrong))
                            XLog.d("DYNAMIC ALL LIST ERROR=======> " + error.localizedMessage)
                        })
        )
    }

    private fun initAdapter(formList: ArrayList<AllDynamicDataModel>?) {
        rv_all_dynamic_list.adapter = AllDynamicListAdapter(mContext, formList) {
            (mContext as DashboardActivity).dynamicScreen = it.name
            (mContext as DashboardActivity).loadFragment(FragType.DynamicListFragment, true, it.id)
        }
    }
}