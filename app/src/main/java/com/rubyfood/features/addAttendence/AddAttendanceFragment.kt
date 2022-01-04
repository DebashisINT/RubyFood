package com.rubyfood.features.addAttendence

import android.Manifest
import android.app.Activity
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.speech.tts.TextToSpeech
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v7.widget.CardView
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import com.borax12.materialdaterangepicker.date.DatePickerDialog
import com.elvishew.xlog.XLog
import com.rubyfood.R
import com.rubyfood.app.AppDatabase
import com.rubyfood.app.NetworkConstant
import com.rubyfood.app.Pref
import com.rubyfood.app.Pref.willShowUpdateDayPlan
import com.rubyfood.app.domain.*
import com.rubyfood.app.types.FragType
import com.rubyfood.app.utils.AppUtils
import com.rubyfood.app.utils.FTStorageUtils
import com.rubyfood.app.utils.NotificationUtils
import com.rubyfood.app.utils.PermissionUtils
import com.rubyfood.base.BaseResponse
import com.rubyfood.base.presentation.BaseActivity
import com.rubyfood.features.addAttendence.api.WorkTypeListRepoProvider
import com.rubyfood.features.addAttendence.api.addattendenceapi.AddAttendenceRepoProvider
import com.rubyfood.features.addAttendence.api.leavetytpeapi.LeaveTypeRepoProvider
import com.rubyfood.features.addAttendence.api.routeapi.RouteRepoProvider
import com.rubyfood.features.addAttendence.model.*
import com.rubyfood.features.dashboard.presentation.DashboardActivity
import com.rubyfood.features.location.LocationWizard
import com.rubyfood.features.login.UserLoginDataEntity
import com.rubyfood.features.login.model.LoginStateListDataModel
import com.rubyfood.widgets.AppCustomEditText
import com.rubyfood.widgets.AppCustomTextView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.themechangeapp.pickimage.PermissionHelper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by Saikat on 29-08-2018.
 */
class AddAttendanceFragment : Fragment(), View.OnClickListener, DatePickerDialog.OnDateSetListener, OnMapReadyCallback {

    private lateinit var mContext: Context
    private lateinit var tv_attendance_submit: AppCustomTextView
    private lateinit var ll_work_type_list: LinearLayout
    private lateinit var rv_work_type_list: RecyclerView
    private lateinit var iv_work_type_dropdown: ImageView
    private lateinit var ll_add_attendance_main: LinearLayout
    private lateinit var iv_attendance_check: ImageView
    private lateinit var iv_leave_check: ImageView
    private lateinit var tv_work_type: AppCustomTextView
    private lateinit var rl_work_type_header: RelativeLayout
    private lateinit var ll_on_leave: LinearLayout
    private lateinit var ll_at_work: LinearLayout
    private lateinit var progress_wheel: com.pnikosis.materialishprogress.ProgressWheel
    private lateinit var tv_current_address: AppCustomTextView
    private lateinit var tv_current_date_time: AppCustomTextView
    private lateinit var ll_add_attendance_leave_type: LinearLayout
    private lateinit var tv_show_date_range: AppCustomTextView
    private lateinit var rl_leave_type_header: RelativeLayout
    private lateinit var tv_leave_type: AppCustomTextView
    private lateinit var iv_leave_type_dropdown: ImageView
    private lateinit var ll_leave_type_list: LinearLayout
    private lateinit var rv_leave_type_list: RecyclerView
    private lateinit var rl_route_header: RelativeLayout
    private lateinit var tv_route_type: AppCustomTextView
    private lateinit var iv_route_dropdown: ImageView
    private lateinit var ll_route_list: LinearLayout
    private lateinit var rv_route_list: RecyclerView
    private lateinit var cv_route: CardView
    private lateinit var fab_add_work_type: FloatingActionButton
    private lateinit var et_leave_reason_text: AppCustomEditText
    private lateinit var et_order_value: AppCustomEditText
    private lateinit var et_collection_value: AppCustomEditText
    private lateinit var et_shop_visit: AppCustomEditText
    private lateinit var et_shop_revisit: AppCustomEditText
    private lateinit var cv_dd_field: CardView
    private lateinit var et_dd_name: AppCustomEditText
    private lateinit var et_market_worked: AppCustomEditText
    private lateinit var cv_todays_target: CardView
    private lateinit var mapFragment: SupportMapFragment
    private var mGoogleMap: GoogleMap? = null
    private lateinit var tv_address: AppCustomTextView
    private lateinit var et_work_type_text: AppCustomEditText
    private lateinit var tv_approved_in_time: AppCustomTextView
    private lateinit var cv_visit_plan: CardView
    private lateinit var et_from_loc: AppCustomEditText
    private lateinit var et_to_loc: AppCustomEditText
    private lateinit var cv_distance: CardView
    private lateinit var et_distance: AppCustomEditText

    private var isOnLeave = false
    private var workTypeId = ""

    private var workTypeModel: WorkTypeListData? = null

    private var startDate = ""
    private var endDate = ""
    private var leaveId = ""

    private lateinit var ll_target_value: LinearLayout
    private lateinit var rv_primary_value_list: RecyclerView

    private var stateList: ArrayList<LoginStateListDataModel>? = null
    private var fingerprintDialog: FingerprintDialog? = null
    private var selfieDialog: SelfieDialog? = null
    private var loc_list: ArrayList<LocationDataModel>? = null
    private var location: LocationDataModel?= null
    private var fromID = ""
    private var toID = ""
    private var fromLat = ""
    private var fromLong = ""
    private var toLat = ""
    private var toLong = ""

    private val addAttendenceModel: AddAttendenceInpuModel by lazy {
        AddAttendenceInpuModel()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.fragment_add_attendence, container, false)
        initView(view)
        initClickListener()

        /*try {
            if (AppDatabase.getDBInstance()?.workTypeDao()?.getAll()!!.isEmpty())
                getWorkTypeListApi()
            else {
                Log.e("add attendance", "database work type")
                val list = (AppDatabase.getDBInstance()?.workTypeDao()?.getAll() as ArrayList<WorkTypeEntity>?)!!

                for (i in list.indices) {
                    AppDatabase.getDBInstance()?.workTypeDao()?.updateIsSelected(false, list[i].ID)
                }

                setAdapter(list)
                checkForLeaveTypeData()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }*/

        locationList()
        //getWorkTypeListApi()

        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    private fun initView(view: View) {
        tv_attendance_submit = view.findViewById(R.id.tv_attendance_submit)
        ll_work_type_list = view.findViewById(R.id.ll_work_type_list)
        rv_work_type_list = view.findViewById(R.id.rv_work_type_list)
        iv_work_type_dropdown = view.findViewById(R.id.iv_work_type_dropdown)
        ll_add_attendance_main = view.findViewById(R.id.ll_add_attendance_main)
        ll_add_attendance_main.isEnabled = false
        iv_attendance_check = view.findViewById(R.id.iv_attendance_check)
        iv_leave_check = view.findViewById(R.id.iv_leave_check)
        tv_work_type = view.findViewById(R.id.tv_work_type)
        rl_work_type_header = view.findViewById(R.id.rl_work_type_header)
        ll_on_leave = view.findViewById(R.id.ll_on_leave)
        ll_at_work = view.findViewById(R.id.ll_at_work)
        tv_current_date_time = view.findViewById(R.id.tv_current_date_time)
        tv_current_address = view.findViewById(R.id.tv_current_address)
        progress_wheel = view.findViewById(R.id.progress_wheel)
        progress_wheel.stopSpinning()
        ll_add_attendance_leave_type = view.findViewById(R.id.ll_add_attendance_leave_type)
        tv_show_date_range = view.findViewById(R.id.tv_show_date_range)
        rl_leave_type_header = view.findViewById(R.id.rl_leave_type_header)
        tv_leave_type = view.findViewById(R.id.tv_leave_type)
        iv_leave_type_dropdown = view.findViewById(R.id.iv_leave_type_dropdown)
        ll_leave_type_list = view.findViewById(R.id.ll_leave_type_list)
        rv_leave_type_list = view.findViewById(R.id.rv_leave_type_list)
        rl_route_header = view.findViewById(R.id.rl_route_header)
        tv_route_type = view.findViewById(R.id.tv_route_type)
        iv_route_dropdown = view.findViewById(R.id.iv_route_dropdown)
        ll_route_list = view.findViewById(R.id.ll_route_list)
        rv_route_list = view.findViewById(R.id.rv_route_list)
        cv_route = view.findViewById(R.id.cv_route)
        fab_add_work_type = view.findViewById(R.id.fab_add_work_type)
        et_leave_reason_text = view.findViewById(R.id.et_leave_reason_text)
        et_order_value = view.findViewById(R.id.et_order_value)
        et_collection_value = view.findViewById(R.id.et_collection_value)
        et_shop_visit = view.findViewById(R.id.et_shop_visit)
        et_shop_revisit = view.findViewById(R.id.et_shop_revisit)
        ll_target_value = view.findViewById(R.id.ll_target_value)
        cv_dd_field = view.findViewById(R.id.cv_dd_field)
        et_dd_name = view.findViewById(R.id.et_dd_name)
        et_market_worked = view.findViewById(R.id.et_market_worked)
        rv_primary_value_list = view.findViewById(R.id.rv_primary_value_list)
        rv_primary_value_list.layoutManager = LinearLayoutManager(mContext)
        cv_todays_target = view.findViewById(R.id.cv_todays_target)
        mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        tv_address = view.findViewById(R.id.tv_address)
        et_work_type_text = view.findViewById(R.id.et_work_type_text)
        tv_approved_in_time = view.findViewById(R.id.tv_approved_in_time)
        cv_visit_plan = view.findViewById(R.id.cv_visit_plan)
        et_from_loc = view.findViewById(R.id.et_from_loc)
        et_to_loc = view.findViewById(R.id.et_to_loc)
        cv_distance = view.findViewById(R.id.cv_distance)
        et_distance = view.findViewById(R.id.et_distance)

        if (Pref.isVisitPlanShow)
            cv_visit_plan.visibility = View.VISIBLE
        else
            cv_visit_plan.visibility = View.GONE

        if (Pref.willSetYourTodaysTargetVisible)
            cv_todays_target.visibility = View.VISIBLE
        else
            cv_todays_target.visibility = View.GONE

        stateList = AppUtils.loadSharedPreferencesStateList(mContext)

        if (stateList != null && stateList?.size!! > 0) {
            ll_target_value.visibility = View.GONE
            rv_primary_value_list.visibility = View.VISIBLE

            rv_primary_value_list.adapter = PrimaryValueAdapter(mContext, stateList!!)
        } else {
            ll_target_value.visibility = View.VISIBLE
            rv_primary_value_list.visibility = View.GONE
        }


        if (!TextUtils.isEmpty(Pref.current_latitude) && !TextUtils.isEmpty(Pref.current_longitude))
            tv_current_address.text = LocationWizard.getLocationName(mContext, Pref.current_latitude.toDouble(), Pref.current_longitude.toDouble())
        tv_current_date_time.text = AppUtils.getCurrentDateTime12Format()
        tv_approved_in_time.text = Pref.attendance_text


        et_work_type_text.setOnTouchListener(View.OnTouchListener { v, event ->
            v.parent.requestDisallowInterceptTouchEvent(true)

            when (event.action and MotionEvent.ACTION_MASK) {
                MotionEvent.ACTION_UP -> v.parent.requestDisallowInterceptTouchEvent(false)
            }

            false
        })

        et_leave_reason_text.setOnTouchListener(View.OnTouchListener { v, event ->
            v.parent.requestDisallowInterceptTouchEvent(true)

            when (event.action and MotionEvent.ACTION_MASK) {
                MotionEvent.ACTION_UP -> v.parent.requestDisallowInterceptTouchEvent(false)
            }

            false
        })

        //setLeaveTypeAdapter(AppDatabase.getDBInstance()?.leaveTypeDao()?.getAll())
        //setRouteAdapter()
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        mGoogleMap = googleMap
        mGoogleMap?.uiSettings?.isZoomControlsEnabled = true

        if (!TextUtils.isEmpty(Pref.current_latitude) && !TextUtils.isEmpty(Pref.current_longitude)) {
            mGoogleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(Pref.current_latitude.toDouble(),
                    Pref.current_longitude.toDouble()), 15f))

            val latLng = LatLng(Pref.current_latitude.toDouble(), Pref.current_longitude.toDouble())
            val markerOptions = MarkerOptions()

            markerOptions.also {
                it.position(latLng)
                /*it.title(locationName)
                it.snippet(locationName)*/
                it.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                mGoogleMap?.addMarker(it)!!
            }

            tv_address.text = LocationWizard.getLocationName(mContext, Pref.current_latitude.toDouble(), Pref.current_longitude.toDouble())
        }
    }


    private var selectedRoute = ArrayList<RouteEntity>()
    private var routeID = ""

    private fun setRouteAdapter(arrayList: ArrayList<RouteEntity>, selectionStatus: Int, route_id: String?) {
        /*val routeList = ArrayList<String>()
        for (i in 1..10) {
            routeList.add("Route" + i)
        }*/

        rv_route_list.layoutManager = LinearLayoutManager(mContext, LinearLayout.VERTICAL, false)
        rv_route_list.adapter = RouteAdapter(mContext, arrayList, selectionStatus, route_id!!, object : RouteAdapter.OnRouteClickListener {
            override fun unCheckRoute(route: RouteEntity?, adapterPosition: Int) {

                checkIfRouteSelected(route!!, adapterPosition, arrayList, true)

                if (selectedRoute.size > 0) {

                    for (i in selectedRoute.indices) {
                        if (i == 0) {
                            //workTypeId = selectedRoute[i].ID
                            tv_route_type.text = selectedRoute[i].route_name
                            routeID = selectedRoute[i].route_id!!

                        } else {
                            //workTypeId = workTypeId + ", " + selectedRoute[i].ID
                            routeID = routeID + "," + selectedRoute[i].route_id!!
                            tv_route_type.text = tv_route_type.text.toString().trim() + ", " + selectedRoute[i].route_name
                        }
                    }
                } else {
                    //workTypeId = ""
                    routeID = ""
                    tv_route_type.text = ""

                }
            }

            override fun onRouteCheckClick(route: RouteEntity?, adapterPosition: Int, isCheckBoxClicked: Boolean) {

                checkIfRouteSelected(route!!, adapterPosition, arrayList, isCheckBoxClicked)

                if (selectedRoute.size > 0) {

                    for (i in selectedRoute.indices) {
                        if (i == 0) {
                            //workTypeId = selectedRoute[i].ID
                            tv_route_type.text = selectedRoute[i].route_name
                            routeID = selectedRoute[i].route_id!!

                        } else {
                            //workTypeId = workTypeId + ", " + selectedRoute[i].ID
                            routeID = routeID + "," + selectedRoute[i].route_id!!
                            tv_route_type.text = tv_route_type.text.toString().trim() + ", " + selectedRoute[i].route_name
                        }
                    }
                } else {
                    //workTypeId = ""
                    routeID = ""
                    tv_route_type.text = ""

                }

                //tv_route_type.text = route
            }

            override fun onRouteTextClick(route: RouteEntity?, adapterPosition: Int, selected: Boolean) {
                val list = AppDatabase.getDBInstance()?.routeShopListDao()?.getDataRouteIdWise(route?.route_id!!) as ArrayList<RouteShopListEntity>

                if (list == null || list.size == 0)
                    return

                showRouteShopList(route?.route_id!!, selected)
            }
        })
    }

    private fun showRouteShopList(route_id: String, selected: Boolean) {
        try {
            RouteShopListDialog.getInstance(route_id, selected, object : RouteShopListDialog.RouteShopClickLisneter {
                override fun onCheckClick(leaveTypeList: RouteShopListEntity?) {

                    val list = AppDatabase.getDBInstance()?.routeShopListDao()?.getDataRouteIdWise(leaveTypeList?.route_id!!)

                    val selectedList = AppDatabase.getDBInstance()?.routeShopListDao()?.getSelectedDataRouteIdWise(false, leaveTypeList?.route_id!!)

                    if (list?.size == selectedList?.size) {
                        AppDatabase.getDBInstance()?.routeDao()?.updateIsSelectedAccordingToRouteId(false, leaveTypeList?.route_id!!)
                        val list_ = AppDatabase.getDBInstance()?.routeDao()?.getAll() as ArrayList<RouteEntity>
                        setRouteAdapter(list_, 0, leaveTypeList?.route_id)
                        return
                    }
                    for (i in list?.indices!!) {
                        if (list[i].isSelected) {
                            AppDatabase.getDBInstance()?.routeDao()?.updateIsSelectedAccordingToRouteId(true, leaveTypeList?.route_id!!)
                            val list_ = AppDatabase.getDBInstance()?.routeDao()?.getAll() as ArrayList<RouteEntity>
                            setRouteAdapter(list_, 1, leaveTypeList?.route_id)
                            return
                        }
                    }

                }
            }).show((mContext as DashboardActivity).supportFragmentManager, "")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun checkIfRouteSelected(route: RouteEntity, adapterPosition: Int, list: ArrayList<RouteEntity>, checkBoxClicked: Boolean) {
        try {
            if (selectedRoute.size > 0) {

                for (i in selectedRoute.indices) {
                    if (selectedRoute[i] == route) {

                        AppDatabase.getDBInstance()?.routeDao()?.updateIsSelectedAccordingToRouteId(false, list[adapterPosition].route_id!!)
                        selectedRoute.remove(list[adapterPosition])
                        return

                    } else if (selectedRoute[i].route_id == route.route_id) {
                        if (!checkBoxClicked)
                            return
                        else {
                            //selectedRoute.removeAt(adapterPosition)
                            AppDatabase.getDBInstance()?.routeDao()?.updateIsSelectedAccordingToRouteId(false, route.route_id!!)
                            selectedRoute.removeAt(i)
                            return
                        }
                    }
                }

                selectedRoute.add(list[adapterPosition])
                AppDatabase.getDBInstance()?.routeDao()?.updateIsSelectedAccordingToRouteId(true, list[adapterPosition].route_id!!)
                val list_ = AppDatabase.getDBInstance()?.routeShopListDao()?.getDataRouteIdWise(route.route_id!!) as ArrayList<RouteShopListEntity>

                if (list_ == null || list_.size == 0)
                    return

                if (checkBoxClicked)
                    showRouteShopList(list[adapterPosition].route_id!!, true)
            } else {
                selectedRoute.add(list[adapterPosition])
                AppDatabase.getDBInstance()?.routeDao()?.updateIsSelectedAccordingToRouteId(true, list[adapterPosition].route_id!!)
                val list_ = AppDatabase.getDBInstance()?.routeShopListDao()?.getDataRouteIdWise(route.route_id!!) as ArrayList<RouteShopListEntity>

                if (list_ == null || list_.size == 0)
                    return

                if (checkBoxClicked)
                    showRouteShopList(list[adapterPosition].route_id!!, true)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setLeaveTypeAdapter(leaveTypeList: ArrayList<LeaveTypeEntity>?) {
        rv_leave_type_list.layoutManager = LinearLayoutManager(mContext, LinearLayout.VERTICAL, false)
        rv_leave_type_list.adapter = LeaveTypeListAdapter(mContext, leaveTypeList!!, object : LeaveTypeListAdapter.OnLeaveTypeClickListener {
            override fun onLeaveTypeClick(leaveType: LeaveTypeEntity?, adapterPosition: Int) {
                tv_leave_type.text = leaveType?.leave_type
                (mContext as DashboardActivity).leaveType = leaveType?.leave_type!!
                leaveId = leaveType.id.toString()
                ll_leave_type_list.visibility = View.GONE
                iv_leave_type_dropdown.isSelected = false
            }
        })
    }

    private fun initClickListener() {
        tv_attendance_submit.setOnClickListener(this)
        ll_on_leave.setOnClickListener(this)
        ll_at_work.setOnClickListener(this)
        rl_work_type_header.setOnClickListener(this)
        rl_leave_type_header.setOnClickListener(this)
        rl_route_header.setOnClickListener(this)
        tv_address.setOnClickListener(null)
        tv_show_date_range.setOnClickListener(this)
        et_from_loc.setOnClickListener(this)
        et_to_loc.setOnClickListener(this)
    }

    private fun locationList() {
        if (!AppUtils.isOnline(mContext)) {
            (mContext as DashboardActivity).showSnackMessage(getString(R.string.no_internet))
            return
        }

        val repository = RouteRepoProvider.routeListRepoProvider()
        progress_wheel.spin()
        BaseActivity.compositeDisposable.add(
                repository.getLocList()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({ result ->
                            val response = result as LocationListResponseModel
                            if (response.status == NetworkConstant.SUCCESS)
                                loc_list = response.loc_list

                            progress_wheel.stopSpinning()
                            getWorkTypeListApi()

                        }, { error ->
                            error.printStackTrace()
                            progress_wheel.stopSpinning()
                            //(mContext as DashboardActivity).showSnackMessage(getString(R.string.something_went_wrong))

                            getWorkTypeListApi()
                        })
        )
    }

    private fun getWorkTypeListApi() {

        /*val workTypeList = ArrayList<WorkTypeListData>()

        workTypeModel = WorkTypeListData()
        workTypeModel?.ID = "1"
        workTypeModel?.Descrpton = "Field Work (Self, ASM, ZSM, SH)"
        workTypeList.add(workTypeModel!!)

        workTypeModel = WorkTypeListData()
        workTypeModel?.ID = "2"
        workTypeModel?.Descrpton = "Meeting"
        workTypeList.add(workTypeModel!!)

        workTypeModel = WorkTypeListData()
        workTypeModel?.ID = "3"
        workTypeModel?.Descrpton = "DD / PP Visit"
        workTypeList.add(workTypeModel!!)


        doAsync {

            for (i in 0 until (workTypeList.size ?: 0)) {
                val workType = WorkTypeEntity()
                workType.ID = workTypeList[i].ID.toInt()
                workType.Descrpton = workTypeList[i].Descrpton
                AppDatabase.getDBInstance()?.workTypeDao()?.insertAll(workType)
            }

            uiThread {
                setAdapter((AppDatabase.getDBInstance()?.workTypeDao()?.getAll() as ArrayList<WorkTypeListData>?)!!)
                checkForLeaveTypeData()
            }
        }*/

        if (!AppUtils.isOnline(mContext)) {
            (mContext as DashboardActivity).showSnackMessage(getString(R.string.no_internet))
            return
        }

        val repository = WorkTypeListRepoProvider.workTypeListRepo()
        progress_wheel.spin()
        BaseActivity.compositeDisposable.add(
                repository.getWorkTypeList()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({ result ->
                            progress_wheel.stopSpinning()
                            val response = result as WorkTypeResponseModel
                            if (response.status == NetworkConstant.SUCCESS) {
                                val list = response.worktype_list

                                val workList = AppDatabase.getDBInstance()?.workTypeDao()?.getAll()
                                if (workList != null && workList.isNotEmpty())
                                    AppDatabase.getDBInstance()?.workTypeDao()?.delete()

                                for (i in 0 until (list?.size ?: 0)) {
                                    val workType = WorkTypeEntity()
                                    workType.ID = list!![i].ID.toInt()
                                    workType.Descrpton = list[i].Descrpton
                                    AppDatabase.getDBInstance()?.workTypeDao()?.insertAll(workType)
                                }
                                Log.e("add attendance", "api work type")
                                setAdapter(AppDatabase.getDBInstance()?.workTypeDao()?.getAll() as ArrayList<WorkTypeEntity>)
                                checkForLeaveTypeData()
                            }
                        }, { error ->
                            error.printStackTrace()
                            progress_wheel.stopSpinning()
                            (mContext as DashboardActivity).showSnackMessage("ERROR")
                        })
        )
    }

    private fun checkForLeaveTypeData() {
        AppDatabase.getDBInstance()?.leaveTypeDao()?.delete()
        if (AppDatabase.getDBInstance()?.leaveTypeDao()?.getAll()!!.isEmpty())
            getLeaveTypeList()
        else {
            setLeaveTypeAdapter(AppDatabase.getDBInstance()?.leaveTypeDao()?.getAll() as ArrayList<LeaveTypeEntity>)

            //if (AppDatabase.getDBInstance()?.routeDao()?.getAll()!!.isEmpty())
            getRouteList()
            /*else {
                val list = AppDatabase.getDBInstance()?.routeShopListDao()?.getAll()
                for (i in list?.indices!!)
                    AppDatabase.getDBInstance()?.routeShopListDao()?.updateIsUploadedAccordingToRouteId(false, list[i].route_id!!)

                val list_ = AppDatabase.getDBInstance()?.routeDao()?.getAll()
                for (i in list_?.indices!!)
                    AppDatabase.getDBInstance()?.routeDao()?.updateIsSelectedAccordingToRouteId(false, list_[i].route_id!!)

                setRouteAdapter(AppDatabase.getDBInstance()?.routeDao()?.getAll() as ArrayList<RouteEntity>, -1, "")
            }*/
        }
    }

    private fun getRouteList() {
        /*val routeList = ArrayList<RouteEntity>()

        for (i in 1..10) {
            val routeEntity = RouteEntity()
            routeEntity.id = i
            routeEntity.route_name = "Route " + i
            routeList.add(routeEntity)
        }*/

        if (!AppUtils.isOnline(mContext)) {
            (mContext as DashboardActivity).showSnackMessage(getString(R.string.no_internet))
            return
        }

        val list_ = AppDatabase.getDBInstance()?.routeDao()?.getAll()
        val routeShopList = AppDatabase.getDBInstance()?.routeShopListDao()?.getAll()

        val repository = RouteRepoProvider.routeListRepoProvider()
        progress_wheel.spin()
        BaseActivity.compositeDisposable.add(
                repository.getRouteList()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({ result ->
                            val response = result as RouteResponseModel
                            if (response.status == NetworkConstant.SUCCESS) {
                                val list = response.route_list
                                /*for (i in 0 until (list?.size ?: 0)) {
                                    val workType = WorkTypeEntity()
                                    workType.ID = list!![i].ID.toInt()
                                    workType.Descrpton = list[i].Descrpton
                                    AppDatabase.getDBInstance()?.workTypeDao()?.insertAll(workType)
                                }*/
                                if (list != null && list.size > 0) {
                                    doAsync {

                                        if (list_ != null) {
                                            AppDatabase.getDBInstance()?.routeDao()?.deleteRoute()

                                            if (routeShopList != null)
                                                AppDatabase.getDBInstance()?.routeShopListDao()?.deleteData()
                                        }

                                        for (i in list.indices) {

                                            val route = RouteEntity()
                                            route.route_id = list[i].id
                                            route.route_name = list[i].route_name

                                            if (list[i].shop_details_list != null) {
                                                for (j in list[i].shop_details_list?.indices!!) {
                                                    val routeShopList = RouteShopListEntity()
                                                    routeShopList.route_id = list[i].id
                                                    routeShopList.shop_id = list[i].shop_details_list?.get(j)?.shop_id
                                                    routeShopList.shop_address = list[i].shop_details_list?.get(j)?.shop_address
                                                    routeShopList.shop_name = list[i].shop_details_list?.get(j)?.shop_name
                                                    routeShopList.shop_contact_no = list[i].shop_details_list?.get(j)?.shop_contact_no
                                                    AppDatabase.getDBInstance()?.routeShopListDao()?.insert(routeShopList)
                                                }
                                            }

                                            AppDatabase.getDBInstance()?.routeDao()?.insert(route)
                                        }

                                        uiThread {
                                            setRouteAdapter(AppDatabase.getDBInstance()?.routeDao()?.getAll() as ArrayList<RouteEntity>, -1, "")
                                            progress_wheel.stopSpinning()
                                        }
                                    }
                                } else
                                    progress_wheel.stopSpinning()
                            } else {
                                progress_wheel.stopSpinning()

                                if (list_ != null && list_.isNotEmpty()) {
                                    setRouteAdapter(AppDatabase.getDBInstance()?.routeDao()?.getAll() as ArrayList<RouteEntity>, -1, "")
                                }
                            }
                        }, { error ->
                            progress_wheel.stopSpinning()
                            (mContext as DashboardActivity).showSnackMessage("ERROR")

                            if (list_ != null && list_.isNotEmpty()) {
                                setRouteAdapter(AppDatabase.getDBInstance()?.routeDao()?.getAll() as ArrayList<RouteEntity>, -1, "")
                            }
                        })
        )


        /*doAsync {

            for (i in 0 until (routeList.size ?: 0)) {
                AppDatabase.getDBInstance()?.routeDao()?.insert(routeList[i])
            }

            uiThread {
                setRouteAdapter(AppDatabase.getDBInstance()?.routeDao()?.getAll() as ArrayList<RouteEntity>)
            }
        }*/
    }

    var leaveEntity = LeaveTypeEntity()
    private fun getLeaveTypeList() {
        /*val leaveTypeList = ArrayList<LeaveTypeEntity>()

        leaveEntity = LeaveTypeEntity()
        leaveEntity.id = 1
        leaveEntity.leave_type = "Casual Leave"
        leaveTypeList.add(leaveEntity)

        leaveEntity = LeaveTypeEntity()
        leaveEntity.id = 2
        leaveEntity.leave_type = "Planned Leave"
        leaveTypeList.add(leaveEntity)

        leaveEntity = LeaveTypeEntity()
        leaveEntity.id = 3
        leaveEntity.leave_type = "Sick Leave"
        leaveTypeList.add(leaveEntity)

        leaveEntity = LeaveTypeEntity()
        leaveEntity.id = 4
        leaveEntity.leave_type = "Maternity Leave"
        leaveTypeList.add(leaveEntity)

        leaveEntity = LeaveTypeEntity()
        leaveEntity.id = 5
        leaveEntity.leave_type = "Paternity Leave"
        leaveTypeList.add(leaveEntity)

        leaveEntity = LeaveTypeEntity()
        leaveEntity.id = 6
        leaveEntity.leave_type = "Half Day Leave"
        leaveTypeList.add(leaveEntity)

        leaveEntity = LeaveTypeEntity()
        leaveEntity.id = 7
        leaveEntity.leave_type = "Marriage Leave"
        leaveTypeList.add(leaveEntity)*/

        if (!AppUtils.isOnline(mContext)) {
            (mContext as DashboardActivity).showSnackMessage(getString(R.string.no_internet))
            return
        }

        val repository = LeaveTypeRepoProvider.leaveTypeListRepoProvider()
        progress_wheel.spin()
        BaseActivity.compositeDisposable.add(
                repository.getLeaveTypeList()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({ result ->
                            val response = result as LeaveTypeResponseModel
                            if (response.status == NetworkConstant.SUCCESS) {
                                val list = response.leave_type_list

                                if (list != null && list.size > 0) {
                                    doAsync {

                                        for (i in list.indices) {

                                            val leave = LeaveTypeEntity()
                                            leave.id = list[i].id?.toInt()!!
                                            leave.leave_type = list[i].type_name
                                            AppDatabase.getDBInstance()?.leaveTypeDao()?.insert(leave)
                                        }

                                        uiThread {
                                            setLeaveTypeAdapter(AppDatabase.getDBInstance()?.leaveTypeDao()?.getAll() as ArrayList<LeaveTypeEntity>)

                                            if (AppDatabase.getDBInstance()?.routeDao()?.getAll()!!.isEmpty())
                                                getRouteList()
                                            else {
                                                setRouteAdapter(AppDatabase.getDBInstance()?.routeDao()?.getAll() as ArrayList<RouteEntity>, -1, "")
                                            }
                                            progress_wheel.stopSpinning()
                                        }
                                    }
                                } else
                                    progress_wheel.stopSpinning()
                            } else
                                progress_wheel.stopSpinning()
                        }, { error ->
                            progress_wheel.stopSpinning()
                            (mContext as DashboardActivity).showSnackMessage("ERROR")
                        })
        )


        /*doAsync {

            for (i in 0 until (leaveTypeList.size ?: 0)) {
                AppDatabase.getDBInstance()?.leaveTypeDao()?.insert(leaveTypeList[i])
            }

            uiThread {
                setLeaveTypeAdapter(AppDatabase.getDBInstance()?.leaveTypeDao()?.getAll() as ArrayList<LeaveTypeEntity>)

                if (AppDatabase.getDBInstance()?.routeDao()?.getAll()!!.isEmpty())
                    getRouteList()
                else {
                    setRouteAdapter(AppDatabase.getDBInstance()?.routeDao()?.getAll() as ArrayList<RouteEntity>)
                }
            }
        }*/

        /*leaveTypeList.add("Casual Leave")
        leaveTypeList.add("Planned Leave")
        leaveTypeList.add("Sick Leave")
        leaveTypeList.add("Maternity Leave")
        leaveTypeList.add("Paternity Leave")
        leaveTypeList.add("Half Day Leave")
        leaveTypeList.add("Marriage Leave")*/
    }

    private var position = -1
    private var workTypeList = ArrayList<WorkTypeEntity>()
    private fun setAdapter(list: ArrayList<WorkTypeEntity>?) {
        if (list != null) {
            rv_work_type_list.layoutManager = LinearLayoutManager(mContext, LinearLayout.VERTICAL, false)
            rv_work_type_list.adapter = WorkTypeAdapter(mContext, list, object : WorkTypeAdapter.OnWorkTypeClickListener {
                override fun onWorkTypeClick(workType: WorkTypeEntity?, adapterPosition: Int) {

                    checkIfWorkTypeSelected(workType, adapterPosition, list)

                    if (workTypeList.size > 0) {

                        for (i in workTypeList.indices) {
                            if (Pref.isMultipleAttendanceSelection) {
                                if (i == 0) {
                                    workTypeId = workTypeList[i].ID.toString()
                                    tv_work_type.text = workTypeList[i].Descrpton
                                } else {
                                    workTypeId = workTypeId + "," + workTypeList[i].ID
                                    tv_work_type.text = tv_work_type.text.toString().trim() + ", " + workTypeList[i].Descrpton
                                }
                            }
                            else {
                                workTypeId = workTypeList[i].ID.toString()
                                tv_work_type.text = workTypeList[i].Descrpton
                            }
                        }
                    } else {
                        workTypeId = ""
                        tv_work_type.text = ""

                    }

                    /*workTypeId = workType?.ID!!
                    tv_work_type.text = workType.Descrpton*/


                    //Show Route option
                    if (tv_work_type.text.contains("Field")) {

                        val list_ = AppDatabase.getDBInstance()?.routeDao()?.getAll()
                        if (list_ != null && list_.isNotEmpty())
                            cv_route.visibility = View.VISIBLE
                    } else
                        cv_route.visibility = View.GONE


                    /*ll_work_type_list.visibility = View.GONE
                    iv_work_type_dropdown.isSelected = false*/

                    /*if (TextUtils.isEmpty(tv_work_type.text.toString().trim())) {
                        tv_work_type.text = workType?.Descrpton
                        position = adapterPosition
                    } else {

                        if (position == adapterPosition)
                            tv_work_type.text = tv_work_type.text.toString().trim() + ", " + workType?.Descrpton

                    }*/
                    /*iv_work_type_dropdown.isSelected = false
                    ll_work_type_list.visibility = View.GONE*/

                    /*if (!TextUtils.isEmpty(workTypeId))
                        workTypeId = workTypeId + ", " + workType?.ID!!
                    else
                        workTypeId = workType?.ID!!*/
                }
            })
        }
    }

    private fun checkIfWorkTypeSelected(workType: WorkTypeEntity?, adapterPosition: Int, list: ArrayList<WorkTypeEntity>?) {
        //if (Pref.isMultipleAttendanceSelection) {
            if (workTypeList.size > 0) {

                for (i in workTypeList.indices) {
                    if (workTypeList[i].ID == workType?.ID) {
                        AppDatabase.getDBInstance()?.workTypeDao()?.updateIsSelected(false, workType.ID)
                        workTypeList.remove(list?.get(adapterPosition))
                        return
                    }
                }

                if (!Pref.isMultipleAttendanceSelection) {
                    workTypeList.forEach {
                        AppDatabase.getDBInstance()?.workTypeDao()?.updateIsSelected(false, it.ID)
                        workTypeList.remove(it)
                    }
                }

                AppDatabase.getDBInstance()?.workTypeDao()?.updateIsSelected(true, workType?.ID!!)
                workTypeList.add(list?.get(adapterPosition)!!)

            } else {
                AppDatabase.getDBInstance()?.workTypeDao()?.updateIsSelected(true, workType?.ID!!)
                workTypeList.add(list!![adapterPosition])
            }
        /*}
        else {
            if (list?.get(adapterPosition)?.isSelected!!)
                workTypeList.add(list[adapterPosition])
        }*/
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.tv_attendance_submit -> {
                AppUtils.hideSoftKeyboard(mContext as DashboardActivity)
                visibilityCheck()
            }

            R.id.ll_on_leave -> {
                if (!iv_leave_check.isSelected) {
                    iv_leave_check.isSelected = true
                    iv_attendance_check.isSelected = false
                    ll_add_attendance_main.visibility = View.GONE
                    isOnLeave = true
                    tv_attendance_submit.visibility = View.VISIBLE
                    ll_add_attendance_leave_type.visibility = View.VISIBLE
                    cv_dd_field.visibility = View.GONE
                    //fab_add_work_type.visibility = View.GONE
                }

                val now = Calendar.getInstance(Locale.ENGLISH)
                val dpd = com.borax12.materialdaterangepicker.date.DatePickerDialog.newInstance(
                        this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                )
                dpd.isAutoHighlight = true
                dpd.minDate = Calendar.getInstance(Locale.ENGLISH)
                dpd.show((context as Activity).fragmentManager, "Datepickerdialog")

                if (Pref.willLeaveApprovalEnable)
                    tv_attendance_submit.text = getString(R.string.send_for_approval)
                else
                    tv_attendance_submit.text = getString(R.string.submit_button_text)
            }

            R.id.ll_at_work -> {

                if (!checkHomeLocation()) {
                    (mContext as DashboardActivity).showSnackMessage("'Not allowed to Mark Attendance. You are not at permitted location.")
                    return
                }

                if (!iv_attendance_check.isSelected) {
                    iv_leave_check.isSelected = false
                    iv_attendance_check.isSelected = true
                    ll_add_attendance_main.visibility = View.VISIBLE
                    ll_add_attendance_main.isEnabled = true
                    isOnLeave = false
                    tv_attendance_submit.visibility = View.VISIBLE
                    ll_add_attendance_leave_type.visibility = View.GONE

                    if (Pref.isDDFieldEnabled)
                        cv_dd_field.visibility = View.VISIBLE
                    else
                        cv_dd_field.visibility = View.GONE

                    if (willShowUpdateDayPlan)
                        tv_attendance_submit.text = Pref.updateDayPlanText
                    else
                        tv_attendance_submit.text = getString(R.string.submit_button_text)

                    //fab_add_work_type.visibility = View.VISIBLE
                }
            }

            R.id.rl_work_type_header -> {
                if (iv_work_type_dropdown.isSelected) {
                    iv_work_type_dropdown.isSelected = false
                    ll_work_type_list.visibility = View.GONE
                } else {
                    iv_work_type_dropdown.isSelected = true
                    ll_work_type_list.visibility = View.VISIBLE
                }
            }

            R.id.rl_leave_type_header -> {
                if (iv_leave_type_dropdown.isSelected) {
                    iv_leave_type_dropdown.isSelected = false
                    ll_leave_type_list.visibility = View.GONE
                } else {
                    iv_leave_type_dropdown.isSelected = true
                    ll_leave_type_list.visibility = View.VISIBLE
                }
            }

            R.id.rl_route_header -> {
                if (iv_route_dropdown.isSelected) {
                    iv_route_dropdown.isSelected = false
                    ll_route_list.visibility = View.GONE
                } else {
                    iv_route_dropdown.isSelected = true
                    ll_route_list.visibility = View.VISIBLE
                }
            }

            R.id.tv_show_date_range -> {
                val now = Calendar.getInstance(Locale.ENGLISH)
                val dpd = com.borax12.materialdaterangepicker.date.DatePickerDialog.newInstance(
                        this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                )
                dpd.isAutoHighlight = true
                dpd.minDate = Calendar.getInstance(Locale.ENGLISH)
                dpd.show((context as Activity).fragmentManager, "Datepickerdialog")
            }

            R.id.et_from_loc -> {
                if (loc_list != null && loc_list!!.isNotEmpty())
                    showAreaDialog(true)
                else
                    (mContext as DashboardActivity).showSnackMessage(getString(R.string.no_date_found))
            }

            R.id.et_to_loc -> {
                if (loc_list != null && loc_list!!.isNotEmpty())
                    showAreaDialog(false)
                else
                    (mContext as DashboardActivity).showSnackMessage(getString(R.string.no_date_found))
            }
        }
    }

    private fun showAreaDialog(isFromLoc: Boolean) {
        LocationListDialog.newInstance(loc_list) {
            if (isFromLoc) {
                et_from_loc.setText(it.location)
                fromID = it.id
                fromLat = it.lattitude
                fromLong = it.longitude

                if (Pref.isAttendanceDistanceShow && toID.isNotEmpty())
                    callDistanceApi()
            }
            else {
                et_to_loc.setText(it.location)
                toID = it.id
                toLat = it.lattitude
                toLong = it.longitude

                if (Pref.isAttendanceDistanceShow && fromID.isNotEmpty())
                    callDistanceApi()
            }

        }.show(fragmentManager, "")
    }

    private fun callDistanceApi() {
        if (!AppUtils.isOnline(mContext)) {
            (mContext as DashboardActivity).showSnackMessage(getString(R.string.no_internet))
            return
        }

        val repository = RouteRepoProvider.routeListRepoProvider()
        progress_wheel.spin()
        BaseActivity.compositeDisposable.add(
                repository.getDistance(fromID, toID)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({ result ->
                            progress_wheel.stopSpinning()
                            val response = result as DistanceResponseModel
                            if (response.status == NetworkConstant.SUCCESS) {
                                cv_distance.visibility = View.VISIBLE
                                et_distance.setText(response.distance + " KM")
                                (mContext as DashboardActivity).visitDistance = response.distance
                            }
                            else {
                                cv_distance.visibility = View.GONE
                                (mContext as DashboardActivity).showSnackMessage(response.message!!)
                            }

                        }, { error ->
                            error.printStackTrace()
                            progress_wheel.stopSpinning()
                            (mContext as DashboardActivity).showSnackMessage(getString(R.string.something_went_wrong))
                            cv_distance.visibility = View.GONE
                        })
        )
    }

    private fun checkHomeLocation(): Boolean {
        if (!TextUtils.isEmpty(Pref.current_latitude) && !TextUtils.isEmpty(Pref.current_longitude)) {
            if (Pref.isHomeLocAvailable) {

                if (!TextUtils.isEmpty(Pref.home_latitude) && !TextUtils.isEmpty(Pref.home_longitude)) {
                    val distance = LocationWizard.getDistance(Pref.home_latitude.toDouble(), Pref.home_longitude.toDouble(), Pref.current_latitude.toDouble(),
                            Pref.current_longitude.toDouble())

                    XLog.e("Distance from home====> $distance")

                    if (Pref.isHomeRestrictAttendance == "0") {
                        if (distance * 1000 > Pref.homeLocDistance.toDouble()) {
                            return true
                        } else {
                            return false
                        }
                    } else if (Pref.isHomeRestrictAttendance == "1")
                        return true
                    else if (Pref.isHomeRestrictAttendance == "2") {
                        if (distance * 1000 > Pref.homeLocDistance.toDouble()) {
                            return false
                        } else {
                            return true
                        }
                    } else
                        return true
                } else {
                    XLog.e("========Home location is not available========")
                    return true
                }

            } else {
                XLog.e("========isHomeLocAvailable is false========")
                return true
            }
        } else {
            XLog.e("========Current location is not available========")
            return true
        }
    }

    override fun onDateSet(datePickerDialog: DatePickerDialog?, year: Int, monthOfYear: Int, dayOfMonth: Int, yearEnd: Int, monthOfYearEnd: Int,
                           dayOfMonthEnd: Int) {

        datePickerDialog?.minDate = Calendar.getInstance(Locale.ENGLISH)
        var monthOfYear = monthOfYear
        var monthOfYearEnd = monthOfYearEnd
        var day = "" + dayOfMonth
        var dayEnd = "" + dayOfMonthEnd
        if (dayOfMonth < 10)
            day = "0$dayOfMonth"
        if (dayOfMonthEnd < 10)
            dayEnd = "0$dayOfMonthEnd"
        var fronString: String = day + "-" + FTStorageUtils.formatMonth((monthOfYear + 1).toString() + "") + "-" + year
        var endString: String = dayEnd + "-" + FTStorageUtils.formatMonth((monthOfYearEnd + 1).toString() + "") + "-" + yearEnd
        if (AppUtils.getStrinTODate(endString).before(AppUtils.getStrinTODate(fronString))) {
            (mContext as DashboardActivity).showSnackMessage("Your end date is before start date.")
            return
        }
        val date = "Leave: From " + day + AppUtils.getDayNumberSuffix(day.toInt()) + FTStorageUtils.formatMonth((++monthOfYear).toString() + "") + " " + year + " To " + dayEnd + AppUtils.getDayNumberSuffix(dayEnd.toInt()) + FTStorageUtils.formatMonth((++monthOfYearEnd).toString() + "") + " " + yearEnd
        tv_show_date_range.visibility = View.VISIBLE
        tv_show_date_range.text = date

        startDate = AppUtils.convertFromRightToReverseFormat(fronString)
        endDate = AppUtils.convertFromRightToReverseFormat(endString)

        /*if (AppUtils.isOnline(mContext)) {
            var attendanceReq = AttendanceRequest()
            attendanceReq.user_id = Pref.user_id
            attendanceReq.session_token = Pref.session_token
            attendanceReq.start_date = AppUtils.changeLocalDateFormatToAtt(fronString)
            attendanceReq.end_date = AppUtils.changeLocalDateFormatToAtt(endString)
            callAttendanceListApi(attendanceReq)
        } else {
            (mContext as DashboardActivity).showSnackMessage(getString(R.string.no_internet))
        }*/
    }

    private fun visibilityCheck() {
        if (!isOnLeave) {
            if (TextUtils.isEmpty(workTypeId))
                (mContext as DashboardActivity).showSnackMessage("Please select work type")
            else {
                if (tv_work_type.text.contains("Field")) {
                    val list_ = AppDatabase.getDBInstance()?.routeDao()?.getAll()
                    if (list_ != null && list_.isNotEmpty()) {
                        if (TextUtils.isEmpty(routeID))
                            (mContext as DashboardActivity).showSnackMessage("Please select route")
                        else
                            checkStateValidation()
                    } else
                        checkStateValidation()
                } else {
                    checkStateValidation()
                }
            }
        } else {
            if (TextUtils.isEmpty(leaveId))
                (mContext as DashboardActivity).showSnackMessage("Please select leave type")
            else if (TextUtils.isEmpty(startDate) || TextUtils.isEmpty(endDate))
                (mContext as DashboardActivity).showSnackMessage("Please select date range")
            else if (Pref.willLeaveApprovalEnable && TextUtils.isEmpty(et_leave_reason_text.text.toString().trim()))
                (mContext as DashboardActivity).showSnackMessage("Please enter remarks")
            else
                checkNetworkConnectivity()
        }
    }

    private fun checkStateValidation() {
        if (stateList != null && stateList!!.size > 0) {
            if (stateList?.size!! != PrimaryValueAdapter.primaryValueList.size && Pref.isPrimaryTargetMandatory) {
                (mContext as DashboardActivity).showSnackMessage("Please enter all primary value plans")
            } else {

                var isInvalid = false

                for (i in PrimaryValueAdapter.primaryValueList.indices) {
                    if (PrimaryValueAdapter.primaryValueList[i].toInt() == 0) {
                        isInvalid = true
                        (mContext as DashboardActivity).showSnackMessage("Please enter valid primary value plan")
                        break
                    }
                }

                if (!isInvalid) {
                    if (Pref.isDDFieldEnabled)
                        checkDDFields()
                    else if (Pref.isVisitPlanMandatory)
                        checkVisitFields()
                    else
                        checkNetworkConnectivity()
                }
            }
        } else
            if (Pref.isDDFieldEnabled)
                checkDDFields()
            else if (Pref.isVisitPlanMandatory)
                checkVisitFields()
            else
                checkNetworkConnectivity()
    }

    private fun checkDDFields() {
        when {
            TextUtils.isEmpty(et_dd_name.text.toString().trim()) -> (mContext as DashboardActivity).showSnackMessage("Please enter distributor name")
            TextUtils.isEmpty(et_market_worked.text.toString().trim()) -> (mContext as DashboardActivity).showSnackMessage("Please enter market worked")
            else -> {
                if (Pref.isVisitPlanMandatory)
                    checkVisitFields()
                else
                    checkNetworkConnectivity()
            }
        }
    }

    private fun checkVisitFields() {
        when {
            TextUtils.isEmpty(fromID) -> (mContext as DashboardActivity).showSnackMessage("Please select start location")
            TextUtils.isEmpty(toID) -> (mContext as DashboardActivity).showSnackMessage("Please select end location")
            else -> checkNetworkConnectivity()
        }
    }

    private fun checkNetworkConnectivity() {

        /*if (AppDatabase.getDBInstance()!!.userAttendanceDataDao().getLoginDate(Pref.user_id!!, AppUtils.getCurrentDateChanged()).isEmpty()) {
            val userLoginDataEntity = UserLoginDataEntity()
            userLoginDataEntity.logindate = AppUtils.getCurrentDateChanged()
//            userLoginDataEntity.login_date= AppUtils.getCurrentDateInDate()
            if (!isOnLeave)
                userLoginDataEntity.logintime = AppUtils.getCurrentTimeWithMeredian()
            userLoginDataEntity.userId = Pref.user_id!!
            AppDatabase.getDBInstance()!!.userAttendanceDataDao().insertAll(userLoginDataEntity)
        }
        Pref.isAddAttendence = true
        (mContext as DashboardActivity).showSnackMessage("Attendance added successfully")
        (mContext as DashboardActivity).onBackPressed()*/

        if (AppUtils.isOnline(mContext)) {
            if (BaseActivity.isApiInitiated)
                return

            BaseActivity.isApiInitiated = true

            /*if (!Pref.willLeaveApprovalEnable) {
                if (!isOnLeave) {
                    if (Pref.isFingerPrintMandatoryForAttendance) {
                        if ((mContext as DashboardActivity).isFingerPrintSupported)
                            showFingerPrintDialog()
                        else
                            prepareAddAttendanceInputParams()
                    } else if (Pref.isSelfieMandatoryForAttendance) {
                        showSelfieDialog()
                    } else
                        prepareAddAttendanceInputParams()
                } else
                    prepareAddAttendanceInputParams()
            } else {
                if (!isOnLeave) {
                    if (Pref.isFingerPrintMandatoryForAttendance) {
                        if ((mContext as DashboardActivity).isFingerPrintSupported)
                            showFingerPrintDialog()
                        else
                            prepareAddAttendanceInputParams()
                    } else if (Pref.isSelfieMandatoryForAttendance) {
                        showSelfieDialog()
                    } else
                        prepareAddAttendanceInputParams()
                } else
                    callLeaveApprovalApi()
            }*/

            if (!Pref.willLeaveApprovalEnable)
                prepareAddAttendanceInputParams()
            else {
                if (!isOnLeave)
                    prepareAddAttendanceInputParams()
                else
                    callLeaveApprovalApi()
        }

        } else
            (mContext as DashboardActivity).showSnackMessage(getString(R.string.no_internet))
    }

    private fun showSelfieDialog() {
        selfieDialog = SelfieDialog.getInstance({
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                initPermissionCheck()
            else {
                launchCamera()
            }
        }, false)
        selfieDialog?.show((mContext as DashboardActivity).supportFragmentManager, "")
    }

    private var permissionUtils: PermissionUtils? = null
    private fun initPermissionCheck() {
        permissionUtils = PermissionUtils(mContext as Activity, object : PermissionUtils.OnPermissionListener {
            override fun onPermissionGranted() {
                //showPictureDialog()
                launchCamera()
            }

            override fun onPermissionNotGranted() {
                (mContext as DashboardActivity).showSnackMessage(getString(R.string.accept_permission))
            }

        }, arrayOf<String>(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE))
    }

    fun onRequestPermission(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        permissionUtils?.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    fun launchCamera() {
        if (PermissionHelper.checkCameraPermission(mContext as DashboardActivity) && PermissionHelper.checkStoragePermission(mContext as DashboardActivity)) {
            /*val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, (mContext as DashboardActivity).getPhotoFileUri(System.currentTimeMillis().toString() + ".png"))
            (mContext as DashboardActivity).startActivityForResult(intent, PermissionHelper.REQUEST_CODE_CAMERA)*/

            (mContext as DashboardActivity).captureFrontImage()
        }
    }

    fun setCameraImage(file: File) {
        selfieDialog?.dismiss()

        if (file == null || TextUtils.isEmpty(file.absolutePath)) {
            (mContext as DashboardActivity).showSnackMessage("Invalid Image")
            return
        }

        if (!AppUtils.isOnline(mContext)) {
            (mContext as DashboardActivity).showSnackMessage(getString(R.string.no_internet))
            return
        }

        val repository = AddAttendenceRepoProvider.sendAttendanceImgRepo()
        progress_wheel.spin()
        BaseActivity.compositeDisposable.add(
                repository.attendanceWithImage(file.absolutePath, mContext)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({ result ->
                            progress_wheel.stopSpinning()
                            val response = result as BaseResponse

                            if (response.status == NetworkConstant.SUCCESS) {
                                callAddAttendanceApi(addAttendenceModel)
                            } else {
                                BaseActivity.isApiInitiated = false
                                (mContext as DashboardActivity).showSnackMessage(response.message!!)
                            }


                        }, { error ->
                            error.printStackTrace()
                            BaseActivity.isApiInitiated = false
                            progress_wheel.stopSpinning()
                            (mContext as DashboardActivity).showSnackMessage(getString(R.string.something_went_wrong))
                        })
        )

    }

    private fun showFingerPrintDialog() {
        (mContext as DashboardActivity).checkForFingerPrint()

        fingerprintDialog = FingerprintDialog()
        fingerprintDialog?.show((mContext as DashboardActivity).supportFragmentManager, "")
    }

    private fun callLeaveApprovalApi() {
        val leaveApproval = SendLeaveApprovalInputParams()
        leaveApproval.session_token = Pref.session_token!!
        leaveApproval.user_id = Pref.user_id!!
        leaveApproval.leave_from_date = startDate
        leaveApproval.leave_to_date = endDate
        leaveApproval.leave_type = leaveId

        if (TextUtils.isEmpty(Pref.current_latitude))
            leaveApproval.leave_lat = "0.0"
        else
            leaveApproval.leave_lat = Pref.current_latitude

        if (TextUtils.isEmpty(Pref.current_longitude))
            leaveApproval.leave_long = "0.0"
        else
            leaveApproval.leave_long = Pref.current_longitude

        if (TextUtils.isEmpty(Pref.current_latitude))
            leaveApproval.leave_add = ""
        else
            leaveApproval.leave_add = LocationWizard.getLocationName(mContext, Pref.current_latitude.toDouble(), Pref.current_longitude.toDouble())


        if (!TextUtils.isEmpty(et_leave_reason_text.text.toString().trim()))
            leaveApproval.leave_reason = et_leave_reason_text.text.toString().trim()

        if (!AppUtils.isOnline(mContext)) {
            (mContext as DashboardActivity).showSnackMessage(getString(R.string.no_internet))
            return
        }

        XLog.d("=========Leave Approval Input Params==========")
        XLog.d("session_token======> " + leaveApproval.session_token)
        XLog.d("user_id========> " + leaveApproval.user_id)
        XLog.d("leave_from_date=======> " + leaveApproval.leave_from_date)
        XLog.d("leave_to_date=======> " + leaveApproval.leave_to_date)
        XLog.d("leave_type========> " + leaveApproval.leave_type)
        XLog.d("leave_lat========> " + leaveApproval.leave_lat)
        XLog.d("leave_long========> " + leaveApproval.leave_long)
        XLog.d("leave_add========> " + leaveApproval.leave_add)
        XLog.d("===============================================")

        val repository = AddAttendenceRepoProvider.leaveApprovalRepo()
        progress_wheel.spin()
        BaseActivity.compositeDisposable.add(
                repository.sendLeaveApproval(leaveApproval)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({ result ->
                            progress_wheel.stopSpinning()
                            val response = result as BaseResponse
                            XLog.d("Leave Approval Response Code========> " + response.status)
                            XLog.d("Leave Approval Response Msg=========> " + response.message)
                            BaseActivity.isApiInitiated = false

                            if (response.status == NetworkConstant.SUCCESS) {

                                Pref.prevOrderCollectionCheckTimeStamp = 0L

                                (mContext as DashboardActivity).showSnackMessage(response.message!!)
                                voiceAttendanceMsg("Hi, your leave applied successfully.")

                                Handler().postDelayed(Runnable {
                                    (mContext as DashboardActivity).onBackPressed()
                                }, 500)

                            } else {
                                (mContext as DashboardActivity).showSnackMessage(response.message!!)
                            }

                        }, { error ->
                            XLog.d("Leave Approval Response ERROR=========> " + error.message)
                            BaseActivity.isApiInitiated = false
                            progress_wheel.stopSpinning()
                            (mContext as DashboardActivity).showSnackMessage(getString(R.string.something_went_wrong))
                        })
        )
    }

    private fun voiceAttendanceMsg(msg: String) {
        if (Pref.isVoiceEnabledForAttendanceSubmit) {
            val speechStatus = (mContext as DashboardActivity).textToSpeech.speak(msg, TextToSpeech.QUEUE_FLUSH, null)
            if (speechStatus == TextToSpeech.ERROR)
                Log.e("Add Attendance", "TTS error in converting Text to Speech!");
        }
    }

    fun continueAddAttendance() {

        if (fingerprintDialog != null && fingerprintDialog?.isVisible!!) {
            fingerprintDialog?.dismiss()

            if (Pref.isSelfieMandatoryForAttendance)
                showSelfieDialog()
            else
                callAddAttendanceApi(addAttendenceModel)
        }
    }

    private fun prepareAddAttendanceInputParams() {
        try {
            addAttendenceModel.session_token = Pref.session_token!!
            addAttendenceModel.user_id = Pref.user_id!!
            addAttendenceModel.is_on_leave = isOnLeave.toString()

            if (!isOnLeave) {
                if (TextUtils.isEmpty(Pref.current_latitude))
                    addAttendenceModel.work_lat = "0.0"
                else
                    addAttendenceModel.work_lat = Pref.current_latitude

                if (TextUtils.isEmpty(Pref.current_longitude))
                    addAttendenceModel.work_long = "0.0"
                else
                    addAttendenceModel.work_long = Pref.current_longitude

                if (TextUtils.isEmpty(Pref.current_latitude))
                    addAttendenceModel.work_address = ""
                else
                    addAttendenceModel.work_address = LocationWizard.getLocationName(mContext, Pref.current_latitude.toDouble(), Pref.current_longitude.toDouble())

                addAttendenceModel.work_desc = et_work_type_text.text.toString().trim()
                //addAttendenceModel.work_date_time = AppUtils.getCurrentDateTime12FormatToAttr(tv_current_date_time.text.toString().trim())
                addAttendenceModel.work_lat = Pref.current_latitude
                addAttendenceModel.work_type = workTypeId

                if (!TextUtils.isEmpty(et_order_value.text.toString().trim()))
                    addAttendenceModel.order_taken = et_order_value.text.toString().trim()

                if (!TextUtils.isEmpty(et_collection_value.text.toString().trim()))
                    addAttendenceModel.collection_taken = et_collection_value.text.toString().trim()

                if (!TextUtils.isEmpty(et_shop_visit.text.toString().trim()))
                    addAttendenceModel.new_shop_visit = et_shop_visit.text.toString().trim()

                if (!TextUtils.isEmpty(et_shop_revisit.text.toString().trim()))
                    addAttendenceModel.revisit_shop = et_shop_revisit.text.toString().trim()

                if (!TextUtils.isEmpty(Pref.profile_state))
                    addAttendenceModel.state_id = Pref.profile_state

                if (tv_work_type.text.contains("Field")) {
                    addAttendenceModel.route = routeID

                    val list = AppDatabase.getDBInstance()?.routeShopListDao()?.getSelectedData(true)
                    if (list != null && list.isNotEmpty()) {
                        val routeShopList = ArrayList<AddAttendenceInputDataModel>()
                        for (i in list.indices) {
                            val addAttendanceInputModel = AddAttendenceInputDataModel()
                            addAttendanceInputModel.route = list[i].route_id!!
                            addAttendanceInputModel.shop_id = list[i].shop_id!!
                            routeShopList.add(addAttendanceInputModel)
                        }
                        addAttendenceModel.shop_list = routeShopList
                    }
                }

                if (!TextUtils.isEmpty(Pref.current_latitude) && !TextUtils.isEmpty(Pref.current_longitude)) {
                    Pref.source_latitude = Pref.current_latitude
                    Pref.source_longitude = Pref.current_longitude
                }

                if (stateList != null && stateList!!.size > 0) {

                    val primaryList = ArrayList<PrimaryValueDataModel>()

                    for (i in PrimaryValueAdapter.primaryValueList.indices) {
                        val primaryValue = PrimaryValueDataModel()
                        primaryValue.id = stateList?.get(i)?.id!!
                        primaryValue.primary_value = PrimaryValueAdapter.primaryValueList[i]
                        primaryList.add(primaryValue)
                    }

                    addAttendenceModel.primary_value_list = primaryList
                }

                if (Pref.isDDFieldEnabled) {
                    addAttendenceModel.distributor_name = et_dd_name.text.toString().trim()
                    addAttendenceModel.market_worked = et_market_worked.text.toString().trim()
                }

            } else {
                addAttendenceModel.leave_from_date = startDate
                addAttendenceModel.leave_to_date = endDate
                addAttendenceModel.leave_type = leaveId

                if (!TextUtils.isEmpty(et_leave_reason_text.text.toString().trim()))
                    addAttendenceModel.leave_reason = et_leave_reason_text.text.toString().trim()
            }

            addAttendenceModel.work_date_time = /*"2018-12-21T18:05:41"*/ AppUtils.getCurrentISODateTime()//AppUtils.getCurrentDateTime12FormatToAttr(AppUtils.getCurrentDateTime12Format())
            val addAttendenceTime =  /*"06:05 PM"*/ AppUtils.getCurrentTimeWithMeredian()
            addAttendenceModel.add_attendence_time = addAttendenceTime
            addAttendenceModel.from_id = fromID
            addAttendenceModel.to_id = toID

            if (!TextUtils.isEmpty(fromLat) && !TextUtils.isEmpty(toLat))
                addAttendenceModel.distance = LocationWizard.getDistance(fromLat.toDouble(), fromLong.toDouble(), toLat.toDouble(), toLong.toDouble()).toString()



            doAttendanceViaApiOrPlanScreen()

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun doAttendanceViaApiOrPlanScreen() {
        if (!willShowUpdateDayPlan) {

            if (!isOnLeave) {
                if (Pref.isFingerPrintMandatoryForAttendance) {
                    if ((mContext as DashboardActivity).isFingerPrintSupported)
                        showFingerPrintDialog()
                    else {
                        if (Pref.isSelfieMandatoryForAttendance)
                            showSelfieDialog()
                        else
                            callAddAttendanceApi(addAttendenceModel)
                    }
                } else if (Pref.isSelfieMandatoryForAttendance)
                    showSelfieDialog()
                else
                    callAddAttendanceApi(addAttendenceModel)
            } else
                callAddAttendanceApi(addAttendenceModel)

        } else {
            if (!isOnLeave) {
                AppUtils.isFromAttendance = true
                (mContext as DashboardActivity).isDailyPlanFromAlarm = false
                BaseActivity.isApiInitiated = false
                (mContext as DashboardActivity).loadFragment(FragType.DailyPlanListFragment, true, addAttendenceModel)
            } else
                callAddAttendanceApi(addAttendenceModel)
        }
    }

    private fun callAddAttendanceApi(addAttendenceModel: AddAttendenceInpuModel) {
        XLog.e("==========AddAttendance=============")
        XLog.d("=====AddAttendance Input Params========")
        XLog.d("session_token-----> " + addAttendenceModel.session_token)
        XLog.d("user_id----------> " + addAttendenceModel.user_id)
        XLog.d("is_on_leave----------> " + addAttendenceModel.is_on_leave)
        XLog.d("work_lat----------> " + addAttendenceModel.work_lat)
        XLog.d("work_long----------> " + addAttendenceModel.work_long)
        XLog.d("work_address----------> " + addAttendenceModel.work_address)
        XLog.d("work_type----------> " + addAttendenceModel.work_type)
        XLog.d("route----------> " + addAttendenceModel.route)
        XLog.d("leave_from_date----------> " + addAttendenceModel.leave_from_date)
        XLog.d("leave_to_date----------> " + addAttendenceModel.leave_to_date)
        XLog.d("leave_type----------> " + addAttendenceModel.leave_type)
        XLog.d("leave_reason----------> " + addAttendenceModel.leave_reason)
        XLog.d("work_date_time----------> " + addAttendenceModel.work_date_time)
        XLog.d("add_attendence_time----------> " + addAttendenceModel.add_attendence_time)
        XLog.d("order taken----------> " + addAttendenceModel.order_taken)
        XLog.d("collection taken----------> " + addAttendenceModel.collection_taken)
        XLog.d("visit new shop----------> " + addAttendenceModel.new_shop_visit)
        XLog.d("revisit shop----------> " + addAttendenceModel.revisit_shop)
        XLog.d("state id----------> " + addAttendenceModel.state_id)
        XLog.d("shop_list size----------> " + addAttendenceModel.shop_list.size)
        XLog.d("primary_value_list size----------> " + addAttendenceModel.primary_value_list.size)
        XLog.d("update_plan_list size----------> " + addAttendenceModel.update_plan_list.size)
        XLog.d("from_id----------> " + addAttendenceModel.from_id)
        XLog.d("to_id----------> " + addAttendenceModel.to_id)
        XLog.d("distance----------> " + addAttendenceModel.distance)
        XLog.d("======End AddAttendance Input Params======")

        val repository = AddAttendenceRepoProvider.addAttendenceRepo()
        progress_wheel.spin()
        BaseActivity.compositeDisposable.add(
                repository.addAttendence(addAttendenceModel)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({ result ->
                            progress_wheel.stopSpinning()
                            val response = result as BaseResponse
                            XLog.d("AddAttendance Response Code========> " + response.status)
                            XLog.d("AddAttendance Response Msg=========> " + response.message)
                            if (response.status == NetworkConstant.SUCCESS) {

                                /*if (AppDatabase.getDBInstance()?.selectedWorkTypeDao()?.getAll() != null)
                            AppDatabase.getDBInstance()?.selectedWorkTypeDao()?.delete()

                        if (AppDatabase.getDBInstance()?.selectedRouteListDao()?.getAll() != null)
                            AppDatabase.getDBInstance()?.selectedRouteListDao()?.deleteRoute()

                        if (AppDatabase.getDBInstance()?.selectedRouteShopListDao()?.getAll() != null)
                            AppDatabase.getDBInstance()?.selectedRouteShopListDao()?.deleteData()*/

                                Pref.visitDistance = (mContext as DashboardActivity).visitDistance

                                Pref.prevOrderCollectionCheckTimeStamp = 0L
                                PrimaryValueAdapter.primaryValueList.clear()

                                val list = AppDatabase.getDBInstance()!!.userAttendanceDataDao().getLoginDate(Pref.user_id!!, AppUtils.getCurrentDateChanged())
                                if (list != null && list.isNotEmpty()) {
                                    AppDatabase.getDBInstance()!!.userAttendanceDataDao().deleteTodaysData(AppUtils.getCurrentDateChanged())
                                }

                                if (AppDatabase.getDBInstance()!!.userAttendanceDataDao().getLoginDate(Pref.user_id!!, AppUtils.getCurrentDateChanged()).isEmpty()) {
                                    val userLoginDataEntity = UserLoginDataEntity()
                                    userLoginDataEntity.logindate = AppUtils.getCurrentDateChanged()
                                    userLoginDataEntity.logindate_number = AppUtils.getTimeStampFromDateOnly(AppUtils.getCurrentDateForShopActi())
                                    //val addAttendenceTime = AppUtils.getCurrentTimeWithMeredian()
                                    //Pref.add_attendence_time = addAttendenceModel.add_attendence_time
                                    if (!isOnLeave) {
                                        userLoginDataEntity.logintime = addAttendenceModel.add_attendence_time
                                        userLoginDataEntity.Isonleave = "false"
                                        Pref.isOnLeave = "false"

                                        if (TextUtils.isEmpty(Pref.isFieldWorkVisible) || Pref.isFieldWorkVisible.equals("true", ignoreCase = true)) {

                                            val list = AppDatabase.getDBInstance()?.workTypeDao()?.getSelectedWork(true)
                                            if (list != null && list.isNotEmpty()) {

                                                for (i in list.indices) {
                                                    val selectedwortkType = SelectedWorkTypeEntity()
                                                    selectedwortkType.ID = list[i].ID
                                                    selectedwortkType.Descrpton = list[i].Descrpton
                                                    selectedwortkType.date = AppUtils.getCurrentDate()
                                                    AppDatabase.getDBInstance()?.selectedWorkTypeDao()?.insertAll(selectedwortkType)
                                                }
                                            }

                                            val routeList = AppDatabase.getDBInstance()?.routeDao()?.getSelectedRoute(true)
                                            if (routeList != null && routeList.isNotEmpty()) {

                                                for (i in routeList.indices) {
                                                    val selectedRoute = SelectedRouteEntity()
                                                    selectedRoute.route_id = routeList[i].route_id
                                                    selectedRoute.route_name = routeList[i].route_name
                                                    selectedRoute.date = AppUtils.getCurrentDate()
                                                    AppDatabase.getDBInstance()?.selectedRouteListDao()?.insert(selectedRoute)
                                                }
                                            }

                                            val routeShopList = AppDatabase.getDBInstance()?.routeShopListDao()?.getSelectedData(true)
                                            if (routeShopList != null && routeShopList.isNotEmpty()) {

                                                for (i in routeShopList.indices) {
                                                    val selectedRouteShop = SelectedRouteShopListEntity()
                                                    selectedRouteShop.route_id = routeShopList[i].route_id
                                                    selectedRouteShop.shop_address = routeShopList[i].shop_address
                                                    selectedRouteShop.shop_contact_no = routeShopList[i].shop_contact_no
                                                    selectedRouteShop.shop_name = routeShopList[i].shop_name
                                                    selectedRouteShop.shop_id = routeShopList[i].shop_id
                                                    selectedRouteShop.date = AppUtils.getCurrentDate()
                                                    AppDatabase.getDBInstance()?.selectedRouteShopListDao()?.insert(selectedRouteShop)
                                                }
                                            }
                                        }

                                        Pref.isAddAttendence = true
                                        Pref.add_attendence_time = addAttendenceModel.add_attendence_time
                                        (mContext as DashboardActivity).update_worktype_tv.apply {
                                            visibility = if (Pref.isUpdateWorkTypeEnable)
                                                View.VISIBLE
                                            else
                                                View.GONE
                                        }

                                        Pref.distributorName = addAttendenceModel.distributor_name
                                        Pref.marketWorked = addAttendenceModel.market_worked

                                        voiceAttendanceMsg("Hi, your attendance mark successfully.")

                                    } else {
                                        userLoginDataEntity.Isonleave = "true"

                                        if (addAttendenceModel.leave_from_date == AppUtils.getCurrentDateForShopActi()) {
                                            Pref.isOnLeave = "true"
                                            Pref.isAddAttendence = true
                                            Pref.add_attendence_time = addAttendenceModel.add_attendence_time

                                            val notificationManager = mContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                                            notificationManager.cancelAll()

                                        } else {
                                            Pref.isOnLeave = "false"
                                            Pref.isAddAttendence = false
                                        }

                                        val notification = NotificationUtils(getString(R.string.app_name), "", "", "")
                                        val body = "You have applied leave from date: " + addAttendenceModel.leave_from_date +
                                                ", to date: " + addAttendenceModel.leave_to_date + ", type: " + tv_leave_type.text.toString().trim()
                                        notification.sendLocNotification(mContext, body)

                                        voiceAttendanceMsg("Hi, your leave applied successfully.")
                                    }
                                    userLoginDataEntity.userId = Pref.user_id!!
                                    AppDatabase.getDBInstance()!!.userAttendanceDataDao().insertAll(userLoginDataEntity)
                                }

                                //Pref.isAddAttendence = true
                                BaseActivity.isApiInitiated = false
                                (mContext as DashboardActivity).showSnackMessage(response.message!!)
                                (mContext as DashboardActivity).onBackPressed()


                                /*Pref.isAddAttendence = true
                        BaseActivity.isApiInitiated = false
                        (mContext as DashboardActivity).showSnackMessage(response.message!!)
                        (mContext as DashboardActivity).onBackPressed()*/
                            } else {
                                BaseActivity.isApiInitiated = false
                                (mContext as DashboardActivity).showSnackMessage(response.message!!)
                            }

                            Log.e("add attendance", "api work type")

                        }, { error ->
                            XLog.d("AddAttendance Response Msg=========> " + error.message)
                            BaseActivity.isApiInitiated = false
                            progress_wheel.stopSpinning()
                            (mContext as DashboardActivity).showSnackMessage(getString(R.string.something_went_wrong))
                        })
        )

    }
}