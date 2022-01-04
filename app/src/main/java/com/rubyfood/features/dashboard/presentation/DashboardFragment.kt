package com.rubyfood.features.dashboard.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.annotation.UiThread
import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.elvishew.xlog.XLog
import com.pnikosis.materialishprogress.ProgressWheel
import com.rubyfood.R
import com.rubyfood.app.*
import com.rubyfood.app.domain.*
import com.rubyfood.app.types.DashboardType
import com.rubyfood.app.types.FragType
import com.rubyfood.app.uiaction.IntentActionable
import com.rubyfood.app.utils.AppUtils
import com.rubyfood.app.utils.FTStorageUtils
import com.rubyfood.app.widgets.MovableFloatingActionButton
import com.rubyfood.base.BaseResponse
import com.rubyfood.base.presentation.BaseActivity
import com.rubyfood.base.presentation.BaseFragment
import com.rubyfood.features.SearchLocation.locationInfoModel
import com.rubyfood.features.addshop.api.assignToPPList.AssignToPPListRepoProvider
import com.rubyfood.features.addshop.api.assignedToDDList.AssignToDDListRepoProvider
import com.rubyfood.features.addshop.api.typeList.TypeListRepoProvider
import com.rubyfood.features.addshop.model.AssignedToShopListResponseModel
import com.rubyfood.features.addshop.model.EntityResponseModel
import com.rubyfood.features.addshop.model.PartyStatusResponseModel
import com.rubyfood.features.addshop.model.assigntoddlist.AssignToDDListResponseModel
import com.rubyfood.features.addshop.model.assigntopplist.AssignToPPListResponseModel
import com.rubyfood.features.alarm.api.attendance_report_list_api.AttendanceReportRepoProvider
import com.rubyfood.features.alarm.model.AlarmData
import com.rubyfood.features.alarm.model.AttendanceReportDataModel
import com.rubyfood.features.alarm.presetation.AttendanceReportAdapter
import com.rubyfood.features.averageshop.api.ShopActivityRepositoryProvider
import com.rubyfood.features.averageshop.business.InfoWizard
import com.rubyfood.features.averageshop.model.ShopActivityRequest
import com.rubyfood.features.averageshop.model.ShopActivityResponse
import com.rubyfood.features.averageshop.model.ShopActivityResponseDataList
import com.rubyfood.features.commondialog.presentation.CommonDialog
import com.rubyfood.features.commondialog.presentation.CommonDialogClickListener
import com.rubyfood.features.dashboard.presentation.api.gteroutelistapi.GetRouteListRepoProvider
import com.rubyfood.features.dashboard.presentation.api.submithomeloc.SubmitHomeLocationRepoProvider
import com.rubyfood.features.dashboard.presentation.model.SelectedRouteListResponseModel
import com.rubyfood.features.dashboard.presentation.model.SubmitHomeLocationInputModel
import com.rubyfood.features.location.LocationWizard
import com.rubyfood.features.location.UserLocationDataEntity
import com.rubyfood.features.location.model.ShopDurationRequestData
import com.rubyfood.features.location.model.VisitRemarksResponseModel
import com.rubyfood.features.location.shopdurationapi.ShopDurationRepositoryProvider
import com.rubyfood.features.login.api.LoginRepositoryProvider
import com.rubyfood.features.login.api.alarmconfigapi.AlarmConfigRepoProvider
import com.rubyfood.features.login.api.global_config.ConfigFetchRepoProvider
import com.rubyfood.features.login.api.productlistapi.ProductListRepoProvider
import com.rubyfood.features.login.api.user_config.UserConfigRepoProvider
import com.rubyfood.features.login.model.alarmconfigmodel.AlarmConfigResponseModel
import com.rubyfood.features.login.model.globalconfig.ConfigFetchResponseModel
import com.rubyfood.features.login.model.mettingListModel.MeetingListResponseModel
import com.rubyfood.features.login.model.productlistmodel.ProductListResponseModel
import com.rubyfood.features.login.model.userconfig.UserConfigResponseModel
import com.rubyfood.features.login.presentation.LoginActivity
import com.rubyfood.features.member.api.TeamRepoProvider
import com.rubyfood.features.member.model.TeamAreaListResponseModel
import com.rubyfood.features.member.model.TeamListResponseModel
import com.rubyfood.features.member.model.TeamShopListResponseModel
import com.rubyfood.features.member.model.UserPjpResponseModel
import com.rubyfood.features.nearbyshops.api.ShopListRepositoryProvider
import com.rubyfood.features.nearbyshops.model.*
import com.rubyfood.features.report.presentation.ReportAdapter
import com.rubyfood.features.timesheet.api.TimeSheetRepoProvider
import com.rubyfood.features.timesheet.model.TimeSheetConfigResponseModel
import com.rubyfood.features.timesheet.model.TimeSheetDropDownResponseModel
import com.rubyfood.widgets.AppCustomTextView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.Writer
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by rp : 31-10-2017:16:49
 */
class DashboardFragment : BaseFragment(), View.OnClickListener {

    private lateinit var fab: FloatingActionButton
    private lateinit var mContext: Context
    private lateinit var mRouteActivityDashboardAdapter: RouteActivityDashboardAdapter
    private lateinit var layoutManager: RecyclerView.LayoutManager
    private lateinit var history_ll: LinearLayout
    private lateinit var shop_ll: LinearLayout
    private lateinit var attandance_ll: LinearLayout
    private lateinit var order_ll: LinearLayout
    private var mFragment: DashboardType = DashboardType.Home
    private lateinit var reportList: RecyclerView
    private lateinit var adapter: ReportAdapter
    private lateinit var list: ArrayList<AddShopDBModelEntity>
    private lateinit var avgTime: AppCustomTextView
    private lateinit var avgShop: AppCustomTextView
    private lateinit var avgOrder: AppCustomTextView
    private lateinit var shops_RL: RelativeLayout
    private lateinit var time_RL: RelativeLayout
    private lateinit var price_RL: RelativeLayout
    private lateinit var best_performing_shop_TV: AppCustomTextView
    private lateinit var no_shop_tv: AppCustomTextView
    private lateinit var progress_wheel: ProgressWheel
    private lateinit var tv_view_all: AppCustomTextView
    private lateinit var rv_pjp_list: RecyclerView
    private lateinit var rl_dashboard_fragment_main: RelativeLayout
    private lateinit var tv_shop: TextView
    private lateinit var tv_order: TextView
    private lateinit var no_of_order_TV: AppCustomTextView
    private lateinit var iv_order_icon: ImageView
    private lateinit var iv_quot_icon: ImageView
    private lateinit var fab_bot: MovableFloatingActionButton

    private lateinit var ll_attendance_report_main: LinearLayout
    private lateinit var rv_attendance_report_list: RecyclerView
    private lateinit var tv_no_data_available: AppCustomTextView
    private lateinit var tv_pick_date_range: AppCustomTextView
    private lateinit var progress_wheel_attendance: ProgressWheel
    private lateinit var shop_tome_order_tab_LL: LinearLayout

    private val customProgressDialog: CustomProgressDialog by lazy {
        CustomProgressDialog.getInstance()
    }

    private val myCalendar: Calendar by lazy {
        Calendar.getInstance(Locale.ENGLISH)
    }

    fun getInstance(objects: Any): DashboardFragment {
        val cardFragment = DashboardFragment()
        mFragment = objects as DashboardType
        return cardFragment
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.fragment_dashboard_new, container, false)

        initView(view)

        if (Pref.isAttendanceFeatureOnly) {

            /*Handler().postDelayed(Runnable {
                getAttendanceReport(AppUtils.getCurrentDateForShopActi())
            }, 500)*/
        } else
            getShopActivityList()

        return view
    }

    private fun getShopActivityList() {
        if (AppDatabase.getDBInstance()!!.shopActivityDao().getAll().isEmpty()) {
            Handler().postDelayed(Runnable {
                callShopActivityApi()
            }, 1500)
        }
        else
            checkToCallMemberList()
    }

    private fun callShopActivityApi() {
        var shopActivity = ShopActivityRequest()
        shopActivity.user_id = Pref.user_id
        shopActivity.session_token = Pref.session_token
        shopActivity.date_span = "30"
        shopActivity.from_date = ""
        shopActivity.to_date = ""
        val repository = ShopActivityRepositoryProvider.provideShopActivityRepository()

        BaseActivity.compositeDisposable.add(
                repository.fetchShopActivity(shopActivity)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({ result ->
                            var shopActityResponse = result as ShopActivityResponse
                            if (shopActityResponse.status == "200") {
                                updateShopTableInDB(shopActityResponse.date_list)
                                var list = AppDatabase.getDBInstance()!!.shopActivityDao().getTotalShopVisitedForADay(AppUtils.getCurrentDateForShopActi())
                                var totalMinute = InfoWizard.getTotalShopVisitTimeForActi()
                                Pref.totalShopVisited = (Pref.totalShopVisited.toInt() - list.size).toString()
                                Pref.totalTimeSpenAtShop = (Pref.totalTimeSpenAtShop.toInt() - totalMinute).toString()

                                val todaysShopVisitCount = InfoWizard.getAvergareShopVisitCount()
                                XLog.e("=======RESPONSE FROM SHOP ACTIVITY API (DASHBOARD FRAGMENT)=======")
                                XLog.e("Today's Shop Visit Count====> $todaysShopVisitCount")

                                avgShop.text = todaysShopVisitCount
                                avgTime.text = InfoWizard.getAverageShopVisitTimeDuration() + " Hrs"

                                when {
                                    Pref.willActivityShow -> avgOrder.text = InfoWizard.getActivityForToday()
                                    Pref.isQuotationShow -> avgOrder.text = getString(R.string.rupee_symbol) + InfoWizard.getTotalQuotAmountForToday()
                                    else -> avgOrder.text = getString(R.string.rupee_symbol) + InfoWizard.getTotalOrderAmountForToday()
                                }

//                                (mContext as DashboardActivity).showSnackMessage(result.message!!)
                            } else {
//                                (mContext as DashboardActivity).showSnackMessage(result.message!!)
                                //TODO SNACK MESSAGE

                            }

                            initBottomAdapter()
                            checkToCallMemberList()
                            (mContext as DashboardActivity).takeActionOnGeofence()
                        }, { error ->
                            error.printStackTrace()
                            //(mContext as DashboardActivity).showSnackMessage("ERROR")
                            //TODO SNACK MESSAGE
                            initBottomAdapter()
                            checkToCallMemberList()
                            (mContext as DashboardActivity).takeActionOnGeofence()
                        })
        )
    }

    private fun updateShopTableInDB(date_list: List<ShopActivityResponseDataList>?) {
        for (i in date_list!!.indices) {
            for (j in 0 until date_list[i].shop_list!!.size) {
                var shopActivityEntity = ShopActivityEntity()
                shopActivityEntity.shopid = date_list[i].shop_list!![j].shopid
                shopActivityEntity.shop_name = date_list[i].shop_list!![j].shop_name
                shopActivityEntity.shop_address = date_list[i].shop_list!![j].shop_address
                shopActivityEntity.date = date_list[i].shop_list!![j].date
                if (date_list[i].shop_list!![j].duration_spent!!.contains("."))
                    shopActivityEntity.duration_spent = date_list[i].shop_list!![j].duration_spent!!.split(".")[0]
                else
                    shopActivityEntity.duration_spent = date_list[i].shop_list!![j].duration_spent!!
                shopActivityEntity.totalMinute = AppUtils.convertMinuteFromHHMMSS(shopActivityEntity.duration_spent)

                if (!TextUtils.isEmpty(date_list[i].shop_list!![j].start_timestamp))
                    shopActivityEntity.startTimeStamp = date_list[i].shop_list!![j].start_timestamp!!
                else
                    shopActivityEntity.startTimeStamp = "0"

                shopActivityEntity.endTimeStamp = "0"
                shopActivityEntity.visited_date = date_list[i].shop_list!![j].visited_date
                shopActivityEntity.isUploaded = true
                shopActivityEntity.isVisited = true
                shopActivityEntity.isDurationCalculated = true
                shopActivityEntity.isFirstShopVisited = false
                shopActivityEntity.distance_from_home_loc = ""

                shopActivityEntity.device_model = date_list[i].shop_list!![j].device_model
                shopActivityEntity.android_version = date_list[i].shop_list!![j].android_version
                shopActivityEntity.battery = date_list[i].shop_list!![j].battery
                shopActivityEntity.net_status = date_list[i].shop_list!![j].net_status
                shopActivityEntity.net_type = date_list[i].shop_list!![j].net_type

                shopActivityEntity.in_time = date_list[i].shop_list!![j].in_time
                shopActivityEntity.out_time = date_list[i].shop_list!![j].out_time

                shopActivityEntity.in_loc = date_list[i].shop_list!![j].in_location
                shopActivityEntity.out_loc = date_list[i].shop_list!![j].out_location
                shopActivityEntity.shop_revisit_uniqKey=date_list[i].shop_list!![j].Key!!
                AppDatabase.getDBInstance()!!.shopActivityDao().insertAll(shopActivityEntity)
            }
        }

    }

    override fun onResume() {
        super.onResume()

        (mContext as DashboardActivity).teamHierarchy.clear()

        val f = mContext.getDatabasePath(AppConstant.DBNAME)
        val dbSize = f.length()
        val dbSizeInKB = dbSize / 1024
        XLog.e("Original DataBase Size====> $dbSize")
        XLog.e("DataBase Size====> $dbSizeInKB KB")

        writeDataToFile()
    }


    private fun writeDataToFile() {

        val list = AppDatabase.getDBInstance()!!.userLocationDataDao().getLocationUpdateForADay(AppUtils.getCurrentDateForShopActi())

        val company = JSONArray()

        for (i in 0 until list.size) {
            if (list[i].latitude == null || list[i].longitude == null)
                continue
            val jsonObject = JSONObject()
            jsonObject.put("locationId", list[i].locationId)
            jsonObject.put("latitude", list[i].latitude)
            jsonObject.put("longitude", list[i].longitude)
            jsonObject.put("distance_covered", list[i].distance)
            jsonObject.put("location_name", list[i].locationName)
            jsonObject.put("timestamp", list[i].timestamp)
            jsonObject.put("time", list[i].time)
            jsonObject.put("hours", list[i].hour)
            jsonObject.put("minutes", list[i].minutes)
            jsonObject.put("shops", list[i].shops)
            jsonObject.put("meridiem", list[i].meridiem)
            jsonObject.put("isUploaded", list[i].isUploaded)
            jsonObject.put("updateDate", list[i].updateDate)
            jsonObject.put("updateDateTime", list[i].updateDateTime)
            company.put(jsonObject)
        }

        val accurateObject = JSONObject()
        accurateObject.put("accurate_locations", company)

        /*val parentObject = JSONObject()
        parentObject.put("accurate_loc", accurateObject)*/

        try {
            var output: Writer? = null
            val folderPath = FTStorageUtils.getFolderPath(mContext)
            val file = File("$folderPath/FTS_Todays_Accurate_Location.txt")
            if (file.exists()) {
                Log.e("Location List", "File deleted")
                file.delete()
            }
            output = BufferedWriter(FileWriter(file))
            output.write(accurateObject.toString())
            output.close()
            Log.e("Location list", "Value saved")

            val length = file.length()
            val lengthInKB = length / 1024

            XLog.e("Original today accurate table Size====> $length")
            XLog.e("Today accurate table Size====> $lengthInKB KB")

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    override fun updateUI(any: Any) {
        super.updateUI(any)

        val todaysShopVisitCount = InfoWizard.getAvergareShopVisitCount()
        XLog.e("=======UPDATE UI (DASHBOARD FRAGMENT)=======")
        XLog.e("Today's Shop Visit Count====> $todaysShopVisitCount")

        avgShop.text = todaysShopVisitCount
        avgTime.text = InfoWizard.getAverageShopVisitTimeDuration() + " Hrs"

        when {
            Pref.willActivityShow -> avgOrder.text = InfoWizard.getActivityForToday()
            Pref.isQuotationShow -> avgOrder.text = getString(R.string.rupee_symbol) + InfoWizard.getTotalQuotAmountForToday()
            else -> avgOrder.text = getString(R.string.rupee_symbol) + InfoWizard.getTotalOrderAmountForToday()
        }

        UpdateLocationData()
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    private fun initView(view: View?) {
        fab = view!!.findViewById(R.id.fab)
        fab.setOnClickListener(this)
        history_ll = view.findViewById(R.id.history_ll)
        shop_ll = view.findViewById(R.id.shop_ll)
        attandance_ll = view.findViewById(R.id.attandance_ll)
        order_ll = view.findViewById(R.id.order_ll)
        reportList = view.findViewById(R.id.report_RCV)
        avgTime = view.findViewById(R.id.n_time_TV)
        avgShop = view.findViewById(R.id.n_shops_TV)
        avgOrder = view.findViewById(R.id.n_order_TV)
        avgOrder.text = getString(R.string.rupee_symbol) + "0.00"
        shops_RL = view.findViewById(R.id.shops_RL)
        time_RL = view.findViewById(R.id.time_RL)
        price_RL = view.findViewById(R.id.price_RL)
        best_performing_shop_TV = view.findViewById(R.id.best_performing_shop_TV)
        no_shop_tv = view.findViewById(R.id.no_shop_tv)
        progress_wheel = view.findViewById(R.id.progress_wheel)
        progress_wheel.stopSpinning()

        tv_view_all = view.findViewById(R.id.tv_view_all)
        rl_dashboard_fragment_main = view.findViewById(R.id.rl_dashboard_fragment_main)
        ll_attendance_report_main = view.findViewById(R.id.ll_attendance_report_main)

        rv_pjp_list = view.findViewById(R.id.rv_pjp_list)
        rv_pjp_list.layoutManager = LinearLayoutManager(mContext)

        rv_attendance_report_list = view.findViewById(R.id.rv_attendance_report_list)
        rv_attendance_report_list.layoutManager = LinearLayoutManager(mContext)
        tv_no_data_available = view.findViewById(R.id.tv_no_data_available)
        tv_pick_date_range = view.findViewById(R.id.tv_pick_date_range)
        progress_wheel_attendance = view.findViewById(R.id.progress_wheel_attendance)
        progress_wheel_attendance.stopSpinning()
        tv_shop = view.findViewById(R.id.tv_shop)
        tv_order = view.findViewById(R.id.tv_order)
        no_of_order_TV = view.findViewById(R.id.no_of_order_TV)
        iv_order_icon = view.findViewById(R.id.iv_order_icon)
        iv_quot_icon = view.findViewById(R.id.iv_quot_icon)
        fab_bot = view.findViewById(R.id.fab_bot)
        shop_tome_order_tab_LL = view.findViewById(R.id.shop_tome_order_tab_LL)

        /*if (Pref.isReplaceShopText)
            tv_shop.text = getString(R.string.customers)
        else
            tv_shop.text = getString(R.string.shops)*/

        tv_shop.text = Pref.shopText + "(s)"

        if (Pref.willShowUpdateDayPlan)
            tv_view_all.visibility = View.VISIBLE
        else
            tv_view_all.visibility = View.GONE

        val todaysShopVisitCount = InfoWizard.getAvergareShopVisitCount()
        XLog.e("=======INIT VIEW (DASHBOARD FRAGMENT)=======")
        XLog.e("Today's Shop Visit Count====> $todaysShopVisitCount")

        avgShop.text = todaysShopVisitCount
        avgTime.text = InfoWizard.getAverageShopVisitTimeDuration() + " Hrs"

        history_ll.setOnClickListener(this)
        shop_ll.setOnClickListener(this)
        attandance_ll.setOnClickListener(this)
        order_ll.setOnClickListener(this)
        shops_RL.setOnClickListener(this)
        time_RL.setOnClickListener(this)
        price_RL.setOnClickListener(this)
        tv_view_all.setOnClickListener(this)
        tv_pick_date_range.setOnClickListener(this)

        fab_bot.setCustomClickListener {
            (mContext as DashboardActivity).showLanguageAlert(false)
        }

        best_performing_shop_TV.text = getString(R.string.todays_task)

        tv_pick_date_range.text = AppUtils.getFormattedDate(myCalendar.time)

        //addShopDBModel()

        /*val list=AppDatabase.getDBInstance()!!.orderDetailsListDao().getListAccordingDate(AppUtils.getCurrentDate())

        if (list==null || list.isEmpty()) {
            avgOrder.text = "0"
        }
        else {
            var totalAmount = 0.0

            for (i in list.indices) {
                totalAmount += list[i].total_price?.toDouble()!!
            }
            //val totalPrice = DecimalFormat("##.##").format(totalAmount)
            val totalPrice = String.format("%.2f", totalAmount.toFloat())
            itemView.tv_total_amount.text = context.getString(R.string.rupee_symbol)+totalPrice
        }*/

        if (Pref.isAttendanceFeatureOnly) {
            ll_attendance_report_main.visibility = View.VISIBLE
            rl_dashboard_fragment_main.visibility = View.GONE

            //getAttendanceReport(AppUtils.getCurrentDateForShopActi())

        } else {
            ll_attendance_report_main.visibility = View.GONE
            rl_dashboard_fragment_main.visibility = View.VISIBLE
        }

        /*if (Pref.isOrderShow) {
            order_ll.visibility = View.VISIBLE
            price_RL.visibility = View.VISIBLE
        } else {
            order_ll.visibility = View.GONE
            price_RL.visibility = View.GONE
        }*/

        if (!Pref.isMeetingAvailable && !Pref.isShopAddEditAvailable)
            fab.visibility = View.GONE
        else
            fab.visibility = View.VISIBLE

        if (Pref.isServiceFeatureEnable){
            tv_order.text = getString(R.string.myjobs)
            iv_order_icon.setImageResource(R.drawable.ic_activity_white)
            iv_order_icon.visibility = View.VISIBLE
            iv_quot_icon.visibility = View.GONE
            shop_tome_order_tab_LL.visibility = View.GONE
        }
        else if (Pref.willActivityShow) {
            tv_order.text = getString(R.string.activities)
            no_of_order_TV.text = getString(R.string.today_activity)
            avgOrder.text = InfoWizard.getActivityForToday()
            iv_order_icon.setImageResource(R.drawable.ic_activity_white)
            iv_order_icon.visibility = View.VISIBLE
            iv_quot_icon.visibility = View.GONE
            shop_tome_order_tab_LL.visibility = View.VISIBLE
        }
        else {
            shop_tome_order_tab_LL.visibility = View.VISIBLE
            if (Pref.isQuotationShow) {
                tv_order.text = getString(R.string.quotation)
                no_of_order_TV.text = getString(R.string.total_quot)
                avgOrder.text = getString(R.string.rupee_symbol) + InfoWizard.getTotalQuotAmountForToday()
                iv_order_icon.visibility = View.GONE
                iv_quot_icon.visibility = View.VISIBLE
            } else {
                no_of_order_TV.text = getString(R.string.total_order_value_new)
                avgOrder.text = getString(R.string.rupee_symbol) + InfoWizard.getTotalOrderAmountForToday()
                iv_order_icon.visibility = View.VISIBLE
                iv_quot_icon.visibility = View.GONE

                if (Pref.isOrderReplacedWithTeam) {
                    tv_order.text = getString(R.string.team_details)
                    iv_order_icon.setImageResource(R.drawable.ic_team_icon)
                    price_RL.visibility = View.GONE
                } else {
                    tv_order.text = getString(R.string.orders)
                    iv_order_icon.setImageResource(R.drawable.ic_dashboard_order_icon)

                    if (Pref.isOrderShow) {
                        order_ll.visibility = View.VISIBLE
                        price_RL.visibility = View.VISIBLE
                    } else {
                        order_ll.visibility = View.GONE
                        price_RL.visibility = View.GONE
                    }
                }
            }
        }

        if (Pref.isChatBotShow)
            fab_bot.visibility = View.VISIBLE
        else
            fab_bot.visibility = View.GONE

        if (Pref.isShowTimeline)
            history_ll.visibility = View.VISIBLE
        else
            history_ll.visibility = View.GONE

        initAdapter()
        initBottomAdapter()
    }

    private fun getAttendanceReport(date: String) {

        if (!AppUtils.isOnline(mContext)) {
            (mContext as DashboardActivity).showSnackMessage(getString(R.string.no_internet))
            return
        }

        var isApiCalling = true

        val repository = AttendanceReportRepoProvider.provideAttendanceReportRepository()
        progress_wheel_attendance.spin()
        BaseActivity.compositeDisposable.add(
                repository.getAttendanceReportList(date)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        /*.doOnTerminate {
                            Log.e("Dashboard", "===========On Terminate Attendance Report==========")
                            if (isApiCalling)
                                getAttendanceReport(date)
                        }*/
                        .subscribe({ result ->
                            val attendanceList = result as AttendanceReportDataModel

                            isApiCalling = false

                            when {
                                attendanceList.status == NetworkConstant.SUCCESS -> {
                                    progress_wheel_attendance.stopSpinning()

                                    if (attendanceList.attendance_report_list != null && attendanceList.attendance_report_list!!.size > 0) {

                                        tv_no_data_available.visibility = View.GONE
                                        rv_attendance_report_list.adapter = AttendanceReportAdapter(mContext, attendanceList.attendance_report_list, object : AttendanceReportAdapter.OnClickListener {
                                            override fun onCallClick(adapterPosition: Int) {
                                                if (TextUtils.isEmpty(attendanceList.attendance_report_list!![adapterPosition].contact_no) || attendanceList.attendance_report_list!![adapterPosition].contact_no.equals("null", ignoreCase = true)
                                                        || !AppUtils.isValidateMobile(attendanceList.attendance_report_list!![adapterPosition].contact_no!!)) {
                                                    (mContext as DashboardActivity).showSnackMessage(getString(R.string.error_phn_no_unavailable))
                                                } else {
                                                    IntentActionable.initiatePhoneCall(mContext, attendanceList.attendance_report_list!![adapterPosition].contact_no)
                                                }
                                            }
                                        })
                                    } else
                                        tv_no_data_available.visibility = View.VISIBLE

                                }
                                attendanceList.status == NetworkConstant.SESSION_MISMATCH -> {
                                    progress_wheel_attendance.stopSpinning()
                                    (mContext as DashboardActivity).showSnackMessage(attendanceList.message!!)
                                    startActivity(Intent(mContext as DashboardActivity, LoginActivity::class.java))
                                    (mContext as DashboardActivity).overridePendingTransition(0, 0)
                                    (mContext as DashboardActivity).finish()
                                }
                                attendanceList.status == NetworkConstant.NO_DATA -> {
                                    progress_wheel_attendance.stopSpinning()
                                    tv_no_data_available.visibility = View.VISIBLE
                                    (mContext as DashboardActivity).showSnackMessage(attendanceList.message!!)

                                }
                                else -> {
                                    progress_wheel_attendance.stopSpinning()
                                    tv_no_data_available.visibility = View.VISIBLE
                                    (mContext as DashboardActivity).showSnackMessage(attendanceList.message!!)
                                }
                            }

                        }, { error ->
                            isApiCalling = false
                            progress_wheel_attendance.stopSpinning()
                            error.printStackTrace()
                            tv_no_data_available.visibility = View.VISIBLE
                            (mContext as DashboardActivity).showSnackMessage(getString(R.string.something_went_wrong))
                        })
        )
    }


    var shopDurationData: ShopDurationRequestData? = null
    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.fab -> {
                if (!Pref.isAddAttendence)
                    (mContext as DashboardActivity).checkToShowAddAttendanceAlert()
                else {
                    /*if (!Pref.isMeetingAvailable)
                        (mContext as DashboardActivity).loadFragment(FragType.AddShopFragment, true, "")
                    else {
                        CommonDialog.getInstance("Action", "What you like to do?", "Add Shop", Pref.meetingText, false, false, true, object : CommonDialogClickListener {
                            override fun onLeftClick() {
                                (mContext as DashboardActivity).loadFragment(FragType.AddShopFragment, true, "")
                            }

                            override fun onRightClick(editableData: String) {
                                showAddMeetingAlert()
                            }

                        }).show((mContext as DashboardActivity).supportFragmentManager, "")
                    }*/

                    if (Pref.isMeetingAvailable && Pref.isShopAddEditAvailable) {
                        CommonDialog.getInstance("Select Activity", "What will you be doing now?", "Add a ${Pref.shopText}", Pref.meetingText, false, false, true, object : CommonDialogClickListener {
                            override fun onLeftClick() {
                                (mContext as DashboardActivity).loadFragment(FragType.AddShopFragment, true, "")
                            }

                            override fun onRightClick(editableData: String) {
                                showAddMeetingAlert()
                            }

                        }).show((mContext as DashboardActivity).supportFragmentManager, "")
                    } else if (Pref.isMeetingAvailable)
                        showAddMeetingAlert()
                    else if (Pref.isShopAddEditAvailable)
                        (mContext as DashboardActivity).loadFragment(FragType.AddShopFragment, true, "")
                }
            }
            R.id.history_ll -> {
                (mContext as DashboardActivity).isMemberMap = false
                if (!Pref.willTimelineWithFixedLocationShow)
                    (mContext as DashboardActivity).loadFragment(FragType.OrderhistoryFragment, false, "")
                else
                    (mContext as DashboardActivity).loadFragment(FragType.TimeLineFragment, false, "")

            }
            R.id.shop_ll -> {

                /*val list = ArrayList<ShopDurationRequestData>()

                shopDurationData = ShopDurationRequestData()
                shopDurationData?.shop_id = "1_72"
                shopDurationData?.spent_duration = "00"
                shopDurationData?.visited_date = "2019-01-30T00:00:02"
                shopDurationData?.visited_time = "2019-01-30T00:00:02"
                shopDurationData?.total_visit_count = "1"
                list.add(shopDurationData!!)

                shopDurationData = ShopDurationRequestData()
                shopDurationData?.shop_id = "1_89"
                shopDurationData?.spent_duration = "00"
                shopDurationData?.visited_date = "2019-01-30T00:00:02"
                shopDurationData?.visited_time = "2019-01-30T00:00:02"
                shopDurationData?.total_visit_count = "1"
                list.add(shopDurationData!!)

                shopDurationData = ShopDurationRequestData()
                shopDurationData?.shop_id = "1_82"
                shopDurationData?.spent_duration = "00"
                shopDurationData?.visited_date = "2019-01-30T00:00:02"
                shopDurationData?.visited_time = "2019-01-30T00:00:02"
                shopDurationData?.total_visit_count = "1"
                list.add(shopDurationData!!)

                shopDurationData = ShopDurationRequestData()
                shopDurationData?.shop_id = "1_72"
                shopDurationData?.spent_duration = "00"
                shopDurationData?.visited_date = "2019-01-30T00:00:02"
                shopDurationData?.visited_time = "2019-01-30T00:00:02"
                shopDurationData?.total_visit_count = "1"
                list.add(shopDurationData!!)

                shopDurationData = ShopDurationRequestData()
                shopDurationData?.shop_id = "1_897"
                shopDurationData?.spent_duration = "00"
                shopDurationData?.visited_date = "2019-01-30T00:00:02"
                shopDurationData?.visited_time = "2019-01-30T00:00:02"
                shopDurationData?.total_visit_count = "1"
                list.add(shopDurationData!!)

                shopDurationData = ShopDurationRequestData()
                shopDurationData?.shop_id = "1_72"
                shopDurationData?.spent_duration = "00"
                shopDurationData?.visited_date = "2019-01-29T00:00:02"
                shopDurationData?.visited_time = "2019-01-29T00:00:02"
                shopDurationData?.total_visit_count = "1"
                list.add(shopDurationData!!)

                shopDurationData = ShopDurationRequestData()
                shopDurationData?.shop_id = "1_72"
                shopDurationData?.spent_duration = "00"
                shopDurationData?.visited_date = "2019-01-28T00:00:02"
                shopDurationData?.visited_time = "2019-01-28T00:00:02"
                shopDurationData?.total_visit_count = "1"
                list.add(shopDurationData!!)

                Log.e("Dashboard", "Duplicate array list size===> " + list.size)*/

                //val newShopList = FTStorageUtils.removeDuplicateData(list)

                //val hashSet = HashSet<ShopDurationRequestData>()
                /*hashSet.addAll(list)
                list.clear()
                list.addAll(hashSet)*/

                /*val newShopList = ArrayList<ShopDurationRequestData>()
                for (i in list.indices){
                    if (hashSet.add(list[i]))
                        newShopList.add(list[i])
                }*/

                //Log.e("Dashboard", "Unique new list size===> " + newShopList.size)

                if (!Pref.isShowShopBeatWise) {
                    (mContext as DashboardActivity).isShopFromChatBot = false
                    if (!Pref.isServiceFeatureEnable)
                        (mContext as DashboardActivity).loadFragment(FragType.NearByShopsListFragment, false, "")
                    else
                        (mContext as DashboardActivity).loadFragment(FragType.CustomerListFragment, false, "")
                }
                else
                    (mContext as DashboardActivity).loadFragment(FragType.BeatListFragment, false, "")
                //(mContext as DashboardActivity).loadFragment(FragType.NewNearByShopsListFragment, false, "")

            }
            R.id.attandance_ll -> {
                (mContext as DashboardActivity).isChatBotAttendance = false
                (mContext as DashboardActivity).loadFragment(FragType.AttendanceFragment, false, "")

            }
            R.id.order_ll -> {
                //(mContext as DashboardActivity).showSnackMessage(getString(R.string.functionality_disabled))
                when {
                    Pref.isServiceFeatureEnable -> (mContext as DashboardActivity).loadFragment(FragType.MyJobsFragment, false, "")
                    Pref.willActivityShow -> (mContext as DashboardActivity).loadFragment(FragType.ActivityShopListFragment, false, "")
                    Pref.isQuotationShow -> {
                        (mContext as DashboardActivity).isBack = false
                        (mContext as DashboardActivity).loadFragment(FragType.QuotationListFragment, false, "")
                    }
                    Pref.isOrderReplacedWithTeam -> (mContext as DashboardActivity).loadFragment(FragType.MemberListFragment, true, Pref.user_id!!)
                    else -> {
                        (mContext as DashboardActivity).isOrderFromChatBot = false
                        (mContext as DashboardActivity).loadFragment(FragType.NewOrderListFragment, false, "")
                    }

                }

                //(mContext as DashboardActivity).loadFragment(FragType.OrderListFragment, false, "")
            }
            R.id.shops_RL -> {
                (mContext as DashboardActivity).loadFragment(FragType.AverageShopFragment, true, "")
            }
            R.id.time_RL -> {
                (mContext as DashboardActivity).loadFragment(FragType.AvgTimespentShopListFragment, true, "")
            }

            R.id.price_RL -> {
                //(mContext as DashboardActivity).showSnackMessage(getString(R.string.functionality_disabled))
                when {
                    //Pref.isServiceFeatureEnable -> (mContext as DashboardActivity).loadFragment(FragType.MyJobsFragment, true, "")
                    Pref.willActivityShow -> (mContext as DashboardActivity).loadFragment(FragType.DateWiseActivityListFragment, true, "")
                    Pref.isQuotationShow -> (mContext as DashboardActivity).loadFragment(FragType.DateWiseQuotationList, true, "")
                    else -> (mContext as DashboardActivity).loadFragment(FragType.NewDateWiseOrderListFragment, true, "")
                }
            }

            R.id.tv_view_all -> {
                (mContext as DashboardActivity).loadFragment(FragType.AllShopListFragment, true, "")
            }
            R.id.tv_pick_date_range -> {
                val datePicker = android.app.DatePickerDialog(mContext, R.style.DatePickerTheme, date, myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH))
                datePicker.show()
            }

            R.id.fab_bot -> {
                (mContext as DashboardActivity).showLanguageAlert(false)
            }
        }
    }

    val date = android.app.DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
        // TODO Auto-generated method stub
        myCalendar.set(Calendar.YEAR, year)
        myCalendar.set(Calendar.MONTH, monthOfYear)
        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

        tv_pick_date_range.text = AppUtils.getFormattedDate(myCalendar.time)

        getAttendanceReport(AppUtils.getFormattedDateForApi(myCalendar.time))
    }


    private fun showAddMeetingAlert() {
        AddMeetingDialog.getInstance(Pref.meetingText, "", "Cancel", "Confirm", false, true,
                false, object : AddMeetingDialog.OnButtonClickListener {
            override fun onLeftClick() {
            }

            override fun onRightClick(editableData: String, selectedTypeId: String) {

                val list = AppDatabase.getDBInstance()!!.addMeetingDao().durationAvailable(false)
                if (list != null) {
                    for (i in 0 until list.size) {
                        val endTimeStamp = System.currentTimeMillis().toString()
                        val duration = AppUtils.getTimeFromTimeSpan(list[i].startTimeStamp!!, endTimeStamp)
                        AppDatabase.getDBInstance()!!.addMeetingDao().updateEndTimeOfMeeting(endTimeStamp, list[i].id, AppUtils.getCurrentDateForShopActi())
                        AppDatabase.getDBInstance()!!.addMeetingDao().updateTimeDurationForDayOfMeeting(list[i].id, duration, AppUtils.getCurrentDateForShopActi())
                        AppDatabase.getDBInstance()!!.addMeetingDao().updateDurationAvailable(true, list[i].id, AppUtils.getCurrentDateForShopActi())
                    }
                }

                val meetingEntity = MeetingEntity()
                meetingEntity.date = AppUtils.getCurrentDateForShopActi()
                meetingEntity.duration_spent = "00:00:00"
                meetingEntity.remakrs = editableData
                meetingEntity.startTimeStamp = System.currentTimeMillis().toString()
                meetingEntity.date_time = AppUtils.getCurrentISODateTime()
                meetingEntity.lattitude = Pref.current_latitude
                meetingEntity.longitude = Pref.current_longitude
                meetingEntity.meetingTypeId = selectedTypeId

                var address = LocationWizard.getAdressFromLatlng(mContext, meetingEntity.lattitude?.toDouble(), meetingEntity.longitude?.toDouble())

                if (address.contains("http"))
                    address = "Unknown"

                meetingEntity.address = address
                meetingEntity.pincode = LocationWizard.getPostalCode(mContext, meetingEntity.lattitude?.toDouble()!!, meetingEntity.longitude?.toDouble()!!)

                var distance = 0.0

                if (Pref.isOnLeave.equals("false", ignoreCase = true)) {

                    XLog.e("=====User is at work (At meeting revisit time)=======")

                    val userlocation = UserLocationDataEntity()
                    userlocation.latitude = meetingEntity.lattitude!!
                    userlocation.longitude = meetingEntity.longitude!!

                    var loc_distance = 0.0

                    val locationList = AppDatabase.getDBInstance()!!.userLocationDataDao().getLocationUpdateForADay(AppUtils.getCurrentDateForShopActi())

                    if (locationList != null && locationList.isNotEmpty()) {
                        loc_distance = LocationWizard.getDistance(locationList[locationList.size - 1].latitude.toDouble(), locationList[locationList.size - 1].longitude.toDouble(),
                                userlocation.latitude.toDouble(), userlocation.longitude.toDouble())
                    }
                    val finalDistance = (Pref.tempDistance.toDouble() + loc_distance).toString()

                    XLog.e("===Distance (At meeting revisit time)===")
                    XLog.e("Temp Distance====> " + Pref.tempDistance)
                    XLog.e("Normal Distance====> $loc_distance")
                    XLog.e("Total Distance====> $finalDistance")
                    XLog.e("=====================================")

                    userlocation.distance = finalDistance
                    userlocation.locationName = LocationWizard.getNewLocationName(mContext, userlocation.latitude.toDouble(), userlocation.longitude.toDouble())
                    userlocation.timestamp = LocationWizard.getTimeStamp()
                    userlocation.time = LocationWizard.getFormattedTime24Hours(true)
                    userlocation.meridiem = LocationWizard.getMeridiem()
                    userlocation.hour = LocationWizard.getHour()
                    userlocation.minutes = LocationWizard.getMinute()
                    userlocation.isUploaded = false
                    userlocation.shops = AppDatabase.getDBInstance()!!.shopActivityDao().getTotalShopVisitedForADay(AppUtils.getCurrentDateForShopActi()).size.toString()
                    userlocation.updateDate = AppUtils.getCurrentDateForShopActi()
                    userlocation.updateDateTime = AppUtils.getCurrentDateTime()
                    userlocation.meeting = AppDatabase.getDBInstance()!!.addMeetingDao().getMeetingDateWise(AppUtils.getCurrentDateForShopActi()).size.toString()
                    userlocation.network_status = if (AppUtils.isOnline(mContext)) "Online" else "Offline"
                    userlocation.battery_percentage = AppUtils.getBatteryPercentage(mContext).toString()
                    AppDatabase.getDBInstance()!!.userLocationDataDao().insertAll(userlocation)

                    Pref.totalS2SDistance = (Pref.totalS2SDistance.toDouble() + userlocation.distance.toDouble()).toString()

                    distance = Pref.totalS2SDistance.toDouble()
                    Pref.totalS2SDistance = "0.0"
                    Pref.tempDistance = "0.0"
                } else {
                    XLog.e("=====User is on leave (At meeting revisit time)=======")
                    distance = 0.0
                }

                meetingEntity.distance_travelled = distance.toString()

                AppDatabase.getDBInstance()!!.addMeetingDao().insertAll(meetingEntity)
            }

        }).show((mContext as DashboardActivity).supportFragmentManager, "")
    }


    private fun initAdapter() {
        mRouteActivityDashboardAdapter = RouteActivityDashboardAdapter(this.context!!, AppDatabase.getDBInstance()!!.userLocationDataDao().all)
        layoutManager = LinearLayoutManager(mContext, LinearLayout.VERTICAL, false)
    }


    private fun getLocationList(): List<UserLocationDataEntity> {
        return AppDatabase.getDBInstance()!!.userLocationDataDao().all

    }

    fun UpdateLocationData() {
        mRouteActivityDashboardAdapter.update(getLocationList())
        mRouteActivityDashboardAdapter.notifyDataSetChanged()
    }

    public fun initBottomAdapter() {

        /*val performList = ArrayList<AddShopDBModelEntity>()
        val updatedPerformList = ArrayList<AddShopDBModelEntity>()

        for (i in list.indices) {
            if (i == 0)
                performList.add(list[i])
            else {
                if (list[i].shop_id != list[i - 1].shop_id) {
                    performList.add(list[i])
                }
            }
        }


        for (i in performList.indices) {
            if (i > 4)
                break

            updatedPerformList.add(performList[i])
        }

        if (updatedPerformList.size == 1)
            best_performing_shop_TV.text = "Best performing " + updatedPerformList.size + " shop"
        else
            best_performing_shop_TV.text = "Best performing " + updatedPerformList.size + " shops"*/


        val work_type_list = AppDatabase.getDBInstance()?.selectedWorkTypeDao()?.getTodaysData(AppUtils.getCurrentDate()) as ArrayList<SelectedWorkTypeEntity>

        if (work_type_list != null && work_type_list.size > 0) {
            no_shop_tv.visibility = View.GONE
            reportList.visibility = View.VISIBLE
            //adapter = ReportAdapter(mContext, work_type_list)
            layoutManager = LinearLayoutManager(mContext, LinearLayout.VERTICAL, false)
            reportList.layoutManager = layoutManager
            reportList.adapter = TodaysWorkAdapter(mContext, work_type_list)
            reportList.isNestedScrollingEnabled = false
        } else {
            reportList.visibility = View.GONE
        }

        if (Pref.isAttendanceFeatureOnly)
            getAttendanceReport(AppUtils.getCurrentDateForShopActi())
        else {
            //getUserPjpList(work_type_list)
            val pjpList = AppDatabase.getDBInstance()?.pjpListDao()?.getAll() as ArrayList<PjpListEntity>

            if (pjpList != null && pjpList.isNotEmpty()) {
                no_shop_tv.visibility = View.GONE
                rv_pjp_list.visibility = View.VISIBLE

                rv_pjp_list.adapter = PjpAdapter(mContext, pjpList,object :PJPClickListner{
                    override fun visitShop(shop: Any) {
                        if (!Pref.isAddAttendence)
                            (mContext as DashboardActivity).checkToShowAddAttendanceAlert()
                        else {
                            val nearbyShop: AddShopDBModelEntity = shop as AddShopDBModelEntity
                            (mContext as DashboardActivity).callShopVisitConfirmationDialog(nearbyShop.shopName, nearbyShop.shop_id)
                        }
                    }
                })

            } else {
                rv_pjp_list.visibility = View.GONE

                if (work_type_list == null || work_type_list.size == 0)
                    no_shop_tv.visibility = View.VISIBLE
            }
        }
    }

    private fun getUserPjpList(workTypeList: ArrayList<SelectedWorkTypeEntity>) {
        if (!AppUtils.isOnline(mContext)) {

            if (workTypeList == null || workTypeList.size == 0)
                no_shop_tv.visibility = View.VISIBLE

            rv_pjp_list.visibility = View.GONE
            //(mContext as DashboardActivity).showSnackMessage(getString(R.string.no_internet))
            return
        }

        progress_wheel.spin()
        val repository = TeamRepoProvider.teamRepoProvider()
        BaseActivity.compositeDisposable.add(
                repository.getUserPJPList(AppUtils.getCurrentDateForShopActi())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({ result ->
                            val response = result as UserPjpResponseModel
                            XLog.d("GET USER PJP DATA : " + "RESPONSE : " + response.status + "\n" + "Time : " + AppUtils.getCurrentDateTime() + ", USER :" + Pref.user_name + ",MESSAGE : " + response.message)
                            progress_wheel.stopSpinning()
                            if (response.status == NetworkConstant.SUCCESS) {

                                if (response.pjp_list != null && response.pjp_list.size > 0) {
                                    no_shop_tv.visibility = View.GONE
                                    rv_pjp_list.visibility = View.VISIBLE
                                    //rv_pjp_list.adapter = PjpAdapter(mContext, response.pjp_list)

                                } else {
                                    rv_pjp_list.visibility = View.GONE
                                    //(mContext as DashboardActivity).showSnackMessage(response.message!!)

                                    if (workTypeList == null || workTypeList.size == 0)
                                        no_shop_tv.visibility = View.VISIBLE
                                }
                            } else {
                                rv_pjp_list.visibility = View.GONE
                                //(mContext as DashboardActivity).showSnackMessage(response.message!!)

                                if (workTypeList == null || workTypeList.size == 0)
                                    no_shop_tv.visibility = View.VISIBLE
                            }

                        }, { error ->
                            progress_wheel.stopSpinning()
                            XLog.d("GET USER PJP DATA : " + "ERROR : " + "\n" + "Time : " + AppUtils.getCurrentDateTime() + ", USER :" + Pref.user_name + ",MESSAGE : " + error.localizedMessage)
                            error.printStackTrace()
                            //(mContext as DashboardActivity).showSnackMessage(getString(R.string.something_went_wrong))
                            rv_pjp_list.visibility = View.GONE

                            if (workTypeList == null || workTypeList.size == 0)
                                no_shop_tv.visibility = View.VISIBLE
                        })
        )
    }

    fun addShopDBModel() {

        try {
            val endDate = AppUtils.convertDateStringToLong(AppUtils.getCurrentDateForShopActi())
            val startDate = AppUtils.convertDateStringToLong(AppUtils.getThreeMonthsPreviousDate(AppUtils.getCurrentDateForShopActi()))

            val orderList = AppDatabase.getDBInstance()!!.orderListDao().getListAccordingToDateRange(startDate, endDate) as ArrayList<OrderListEntity>

            /*val hashSet = HashSet<OrderListEntity>()
            hashSet.addAll(orderList)
            orderList.clear()
            orderList.addAll(hashSet)*/

            list = ArrayList<AddShopDBModelEntity>()

            /*if (orderList != null && orderList.isNotEmpty()) {

                no_shop_tv.visibility = View.GONE

                for (i in orderList.indices) {
                    val maxAmount = AppDatabase.getDBInstance()!!.orderDetailsListDao().getAmountAccordingToShopId(orderList[i].shop_id!!)

                    if (!TextUtils.isEmpty(maxAmount)) {
                        val mAddShopDBModelEntity = AddShopDBModelEntity()
                        mAddShopDBModelEntity.shop_id = orderList[i].shop_id
                        mAddShopDBModelEntity.address = orderList[i].address
                        mAddShopDBModelEntity.shopName = orderList[i].shop_name
                        mAddShopDBModelEntity.orderValue = maxAmount.toFloat().toInt() //orderList[i].order_amount?.toFloat()!!.toInt()
                        list.add(mAddShopDBModelEntity)
                    }
                }

                Collections.sort(list, object : Comparator<AddShopDBModelEntity> {
                    override fun compare(p0: AddShopDBModelEntity?, p1: AddShopDBModelEntity?): Int {
                        return p0?.orderValue?.let { p1?.orderValue?.compareTo(it) }!!
                    }
                })

            } else {*/
            //best_performing_shop_TV.text = /*"Best Performing 0 shop"*/ getString(R.string.best_performing_shop)
            //no_shop_tv.visibility = View.VISIBLE
            val mAddShopDBModelEntity1: AddShopDBModelEntity = AddShopDBModelEntity()
            mAddShopDBModelEntity1.address = "SDF Module GP block Kol 700091"
            mAddShopDBModelEntity1.shopName = "The Tommy Hilfinger"
            mAddShopDBModelEntity1.orderValue = 200

            val mAddShopDBModelEntity2: AddShopDBModelEntity = AddShopDBModelEntity()

            mAddShopDBModelEntity2.address = "SDF Module GP block Kol 700091"
            mAddShopDBModelEntity2.shopName = "Addidus Store"
            mAddShopDBModelEntity2.orderValue = 300


            val mAddShopDBModelEntity3: AddShopDBModelEntity = AddShopDBModelEntity()

            mAddShopDBModelEntity3.address = "SDF Module GP block Kol 700091"
            mAddShopDBModelEntity3.shopName = "Turtle Outlet"
            mAddShopDBModelEntity3.orderValue = 400

            val mAddShopDBModelEntity4: AddShopDBModelEntity = AddShopDBModelEntity()

            mAddShopDBModelEntity4.address = "SDF Module GP block Kol 700091"
            mAddShopDBModelEntity4.shopName = "Levice International"
            mAddShopDBModelEntity4.orderValue = 500

            val mAddShopDBModelEntity5: AddShopDBModelEntity = AddShopDBModelEntity()

            mAddShopDBModelEntity5.address = "SDF Module GP block Kol 700091"
            mAddShopDBModelEntity5.shopName = "Image Kolkata Store"
            mAddShopDBModelEntity5.orderValue = 600

            val mAddShopDBModelEntity6: AddShopDBModelEntity = AddShopDBModelEntity()

            mAddShopDBModelEntity5.address = "SDF Module GP block Kol 700091"
            mAddShopDBModelEntity5.shopName = "Image Kolkata Store"
            mAddShopDBModelEntity5.orderValue = 600


            list.add(mAddShopDBModelEntity1)
            list.add(mAddShopDBModelEntity2)
            list.add(mAddShopDBModelEntity3)
            list.add(mAddShopDBModelEntity4)
            list.add(mAddShopDBModelEntity5)
            list.add(mAddShopDBModelEntity6)


            Collections.sort(list, object : Comparator<AddShopDBModelEntity> {
                override fun compare(p0: AddShopDBModelEntity?, p1: AddShopDBModelEntity?): Int {
                    return p0?.orderValue?.let { p1?.orderValue?.compareTo(it) }!!
                }
            })

            //}
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun updateItem() {
        //addShopDBModel()
        //initBottomAdapter()

        if (!TextUtils.isEmpty(Pref.isFieldWorkVisible) && Pref.isFieldWorkVisible.equals("false", ignoreCase = true)) {
            val list = AppDatabase.getDBInstance()?.selectedWorkTypeDao()?.getAll()
            if (Pref.isAddAttendence && (list == null || list.isEmpty()))
                getSelectedRouteList()
            else
                initBottomAdapter()
        } else
            initBottomAdapter()
    }

    fun updateBottomList() {
        if (!TextUtils.isEmpty(Pref.isFieldWorkVisible) && Pref.isFieldWorkVisible.equals("false", ignoreCase = true))
            getSelectedRouteList()
        else {
            initBottomAdapter()
            checkToCallMemberList()
        }
    }

    private fun getSelectedRouteList() {
        val repository = GetRouteListRepoProvider.routeListRepoProvider()
        var progress_wheel: ProgressWheel?= null
        if (Pref.isAttendanceFeatureOnly)
            progress_wheel = progress_wheel_attendance
        else
            progress_wheel = this.progress_wheel

        progress_wheel?.spin()

        BaseActivity.compositeDisposable.add(
                repository.routeList()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({ result ->
                            val response = result as SelectedRouteListResponseModel
                            if (response.status == NetworkConstant.SUCCESS) {
                                val workTypeList = response.worktype

                                if (workTypeList != null && workTypeList.isNotEmpty()) {

                                    AppDatabase.getDBInstance()?.selectedWorkTypeDao()?.delete()
                                    AppDatabase.getDBInstance()?.selectedRouteShopListDao()?.deleteData()
                                    AppDatabase.getDBInstance()?.selectedRouteListDao()?.deleteRoute()

                                    doAsync {

                                        for (i in workTypeList.indices) {
                                            val selectedwortkType = SelectedWorkTypeEntity()
                                            selectedwortkType.ID = workTypeList[i].id?.toInt()!!
                                            selectedwortkType.Descrpton = workTypeList[i].name
                                            selectedwortkType.date = AppUtils.getCurrentDate()
                                            AppDatabase.getDBInstance()?.selectedWorkTypeDao()?.insertAll(selectedwortkType)
                                        }

                                        val routeList = response.route_list
                                        if (routeList != null && routeList.isNotEmpty()) {
                                            for (i in routeList.indices) {
                                                val selectedRoute = SelectedRouteEntity()
                                                selectedRoute.route_id = routeList[i].id
                                                selectedRoute.route_name = routeList[i].route_name
                                                selectedRoute.date = AppUtils.getCurrentDate()

                                                val routeShopList = routeList[i].shop_details_list
                                                if (routeShopList != null && routeShopList.size > 0) {
                                                    for (j in routeShopList.indices) {
                                                        val selectedRouteShop = SelectedRouteShopListEntity()
                                                        selectedRouteShop.route_id = routeList[i].id
                                                        selectedRouteShop.shop_address = routeShopList[j].shop_address
                                                        selectedRouteShop.shop_contact_no = routeShopList[j].shop_contact_no
                                                        selectedRouteShop.shop_name = routeShopList[j].shop_name
                                                        selectedRouteShop.shop_id = routeShopList[j].shop_id
                                                        selectedRouteShop.date = AppUtils.getCurrentDate()
                                                        AppDatabase.getDBInstance()?.selectedRouteShopListDao()?.insert(selectedRouteShop)
                                                    }
                                                }

                                                AppDatabase.getDBInstance()?.selectedRouteListDao()?.insert(selectedRoute)
                                            }
                                        }

                                        uiThread {
                                            progress_wheel.stopSpinning()
                                            initBottomAdapter()
                                            checkToCallMemberList()
                                        }
                                    }
                                } else {
                                    progress_wheel.stopSpinning()
                                    checkToCallMemberList()
                                }
                            } else {
                                progress_wheel.stopSpinning()
                                checkToCallMemberList()
                            }

                        }, { error ->
                            error.printStackTrace()
                            progress_wheel.stopSpinning()
                            checkToCallMemberList()
                        })
        )
    }

    fun sendHomeLoc(locationInfoModel: locationInfoModel?) {

        if (AppUtils.isOnline(mContext))
            callSubmitLocApi(locationInfoModel)
        else {
            (mContext as DashboardActivity).showSnackMessage(getString(R.string.no_internet))

            Handler().postDelayed(Runnable {
                (mContext as DashboardActivity).checkToShowHomeLocationAlert()
            }, 200)
        }
    }

    private fun callSubmitLocApi(locationInfoModel: locationInfoModel?) {

        if (TextUtils.isEmpty(Pref.session_token) || TextUtils.isEmpty(Pref.user_id)) {
            startActivity(Intent(mContext as DashboardActivity, LoginActivity::class.java))
            (mContext as DashboardActivity).overridePendingTransition(0, 0)
            (mContext as DashboardActivity).finish()
        }

        val submitLoc = SubmitHomeLocationInputModel()
        submitLoc.session_token = Pref.session_token!!
        submitLoc.user_id = Pref.user_id!!

        if (!TextUtils.isEmpty(locationInfoModel?.latitude))
            submitLoc.latitude = locationInfoModel?.latitude!!

        if (!TextUtils.isEmpty(locationInfoModel?.longitude))
            submitLoc.longitude = locationInfoModel?.longitude!!

        if (!TextUtils.isEmpty(locationInfoModel?.address))
            submitLoc.address = locationInfoModel?.address!!

        if (!TextUtils.isEmpty(locationInfoModel?.city))
            submitLoc.city = locationInfoModel?.city!!

        if (!TextUtils.isEmpty(locationInfoModel?.state))
            submitLoc.state = locationInfoModel?.state!!

        if (!TextUtils.isEmpty(locationInfoModel?.pinCode))
            submitLoc.pincode = locationInfoModel?.pinCode!!

        val repository = SubmitHomeLocationRepoProvider.submitHomeLocRepo()
        var progress_wheel: ProgressWheel?= null
        if (Pref.isAttendanceFeatureOnly)
            progress_wheel = progress_wheel_attendance
        else
            progress_wheel = this.progress_wheel

        progress_wheel?.spin()
        BaseActivity.compositeDisposable.add(
                repository.submitAttendance(submitLoc)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({ result ->
                            val response = result as BaseResponse
                            progress_wheel.stopSpinning()
                            if (response.status == NetworkConstant.SUCCESS) {
                                (mContext as DashboardActivity).showSnackMessage(response.message!!)
                                Pref.isHomeLocAvailable = true
                                Pref.home_latitude = submitLoc.latitude
                                Pref.home_longitude = submitLoc.longitude
                                (mContext as DashboardActivity).checkToShowAddAttendanceAlert()

                            } else if (response.status == NetworkConstant.SESSION_MISMATCH) {
//                                (mContext as DashboardActivity).clearData()
                                startActivity(Intent(mContext as DashboardActivity, LoginActivity::class.java))
                                (mContext as DashboardActivity).overridePendingTransition(0, 0)
                                (mContext as DashboardActivity).finish()
                            } else {
                                (mContext as DashboardActivity).showSnackMessage(response.message!!)
                                Handler().postDelayed(Runnable {
                                    (mContext as DashboardActivity).checkToShowHomeLocationAlert()
                                }, 200)
                            }
                        }, { error ->
                            progress_wheel.stopSpinning()
                            error.printStackTrace()
                            (mContext as DashboardActivity).showSnackMessage(getString(R.string.something_went_wrong))
                            Handler().postDelayed(Runnable {
                                (mContext as DashboardActivity).checkToShowHomeLocationAlert()
                            }, 200)
                        })
        )
    }


    private fun checkToCallMemberList() {
        XLog.e("==============checkToCallMemberList(Dashboard Fragment)==============")
        Handler().postDelayed(Runnable {
            if (Pref.isOfflineTeam && Pref.isAddAttendence) {
                var progress_wheel: ProgressWheel?= null
                if (Pref.isAttendanceFeatureOnly)
                    progress_wheel = progress_wheel_attendance
                else
                    progress_wheel = this.progress_wheel

                progress_wheel?.spin()

                doAsync {

                    val list = AppDatabase.getDBInstance()?.memberDao()?.getAll()

                    uiThread {
                        progress_wheel.stopSpinning()
                        if (list == null || list.isEmpty())
                            callMemberListApi()
                    }
                }
            }
        }, 200)
    }

    private fun callMemberListApi() {
        return
        if (!AppUtils.isOnline(mContext)) {
            (mContext as DashboardActivity).showSnackMessage(getString(R.string.no_internet))
            return
        }

        if (customProgressDialog.isAdded)
            return

        XLog.e("==============call offline member api(Dashboard Fragment)==============")

        val repository = TeamRepoProvider.teamRepoProvider()
        customProgressDialog.show((mContext as DashboardActivity).supportFragmentManager, "")
        BaseActivity.compositeDisposable.add(
                repository.offlineTeamList("")
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({ result ->
                            val response = result as TeamListResponseModel
                            if (response.status == NetworkConstant.SUCCESS) {

                                if (response.member_list != null && response.member_list!!.isNotEmpty()) {

                                    doAsync {

                                        response.member_list?.forEach {
                                            val member = MemberEntity()
                                            AppDatabase.getDBInstance()?.memberDao()?.insertAll(member.apply {
                                                user_id = it.user_id
                                                user_name = it.user_name
                                                contact_no = it.contact_no
                                                super_id = it.super_id
                                                super_name = it.super_name
                                                date_time = AppUtils.getCurrentISODateTime()
                                            })
                                        }

                                        uiThread {
                                            //callMemberShopListApi()
                                            customProgressDialog.dismiss()
                                        }
                                    }
                                } else {
                                    //callMemberShopListApi()
                                    customProgressDialog.dismiss()
                                }

                            } else if (response.status == NetworkConstant.NO_DATA) {
                                //callMemberShopListApi()
                                customProgressDialog.dismiss()
                            }
                            else if (response.status == NetworkConstant.SESSION_MISMATCH) {
//                                (mContext as DashboardActivity).clearData()
                                customProgressDialog.dismiss()
                                startActivity(Intent(mContext as DashboardActivity, LoginActivity::class.java))
                                (mContext as DashboardActivity).overridePendingTransition(0, 0)
                                (mContext as DashboardActivity).finish()
                            } else {
                                customProgressDialog.dismiss()
                                (mContext as DashboardActivity).showSnackMessage(response.message!!)
                            }

                        }, { error ->
                            customProgressDialog.dismiss()
                            error.printStackTrace()
                            (mContext as DashboardActivity).showSnackMessage(getString(R.string.something_went_wrong))
                        })
        )
    }

    private fun callMemberShopListApi() {

        val repository = TeamRepoProvider.teamRepoProvider()
        BaseActivity.compositeDisposable.add(
                repository.offlineTeamShopList("")
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({ result ->
                            val response = result as TeamShopListResponseModel
                            if (response.status == NetworkConstant.SUCCESS) {

                                if (response.shop_list != null && response.shop_list!!.isNotEmpty()) {

                                    doAsync {

                                        response.shop_list?.forEach {
                                            val memberShop = MemberShopEntity()
                                            AppDatabase.getDBInstance()?.memberShopDao()?.insertAll(memberShop.apply {
                                                user_id = it.user_id
                                                shop_id = it.shop_id
                                                shop_name = it.shop_name
                                                shop_lat = it.shop_lat
                                                shop_long = it.shop_long
                                                shop_address = it.shop_address
                                                shop_pincode = it.shop_pincode
                                                shop_contact = it.shop_contact
                                                total_visited = it.total_visited
                                                last_visit_date = it.last_visit_date
                                                shop_type = it.shop_type
                                                dd_name = it.dd_name
                                                entity_code = it.entity_code
                                                model_id = it.model_id
                                                primary_app_id = it.primary_app_id
                                                secondary_app_id = it.secondary_app_id
                                                lead_id = it.lead_id
                                                funnel_stage_id = it.funnel_stage_id
                                                stage_id = it.stage_id
                                                booking_amount = it.booking_amount
                                                type_id = it.type_id
                                                area_id = it.area_id
                                                assign_to_pp_id = it.assign_to_pp_id
                                                assign_to_dd_id = it.assign_to_dd_id
                                                isUploaded = true
                                                date_time = AppUtils.getCurrentISODateTime()
                                            })
                                        }

                                        uiThread {
                                            customProgressDialog.dismiss()
                                        }
                                    }
                                }

                            } else if (response.status == NetworkConstant.SESSION_MISMATCH) {
//                                (mContext as DashboardActivity).clearData()
                                customProgressDialog.dismiss()
                                startActivity(Intent(mContext as DashboardActivity, LoginActivity::class.java))
                                (mContext as DashboardActivity).overridePendingTransition(0, 0)
                                (mContext as DashboardActivity).finish()
                            } else {
                                customProgressDialog.dismiss()
                                (mContext as DashboardActivity).showSnackMessage(response.message!!)
                            }

                        }, { error ->
                            customProgressDialog.dismiss()
                            error.printStackTrace()
                            (mContext as DashboardActivity).showSnackMessage(getString(R.string.something_went_wrong))
                        })
        )
    }

    fun updateUi(){

        val todaysShopVisitCount = InfoWizard.getAvergareShopVisitCount()
        XLog.e("=======UPDATE UI FOR AUTO REVISIT (DASHBOARD FRAGMENT)=======")
        XLog.e("Today's Shop Visit Count====> $todaysShopVisitCount")

        avgShop.text = todaysShopVisitCount
        avgTime.text = InfoWizard.getAverageShopVisitTimeDuration() + " Hrs"

        when {
            Pref.willActivityShow -> avgOrder.text = InfoWizard.getActivityForToday()
            Pref.isQuotationShow -> avgOrder.text = getString(R.string.rupee_symbol) + InfoWizard.getTotalQuotAmountForToday()
            else -> avgOrder.text = getString(R.string.rupee_symbol) + InfoWizard.getTotalOrderAmountForToday()
        }

        UpdateLocationData()

        if (!TextUtils.isEmpty(Pref.isFieldWorkVisible) && Pref.isFieldWorkVisible.equals("false", ignoreCase = true)) {
            val list = AppDatabase.getDBInstance()?.selectedWorkTypeDao()?.getAll()
            if (Pref.isAddAttendence && (list == null || list.isEmpty()))
                getSelectedRouteList()
            else
                initBottomAdapter()
        } else
            initBottomAdapter()
    }

    fun refresh() {
        checkToCallAssignedDDListApi()
    }

    private fun checkToCallAssignedDDListApi() {
        if (!TextUtils.isEmpty(Pref.profile_state))
            getAssignedDDListApi()
        else {
            if (AppDatabase.getDBInstance()?.productListDao()?.getAll()!!.isEmpty())
                getProductList("")
            else
                getProductList(AppDatabase.getDBInstance()?.productListDao()?.getAll()?.get(0)?.date)
        }
    }

    private fun getAssignedDDListApi() {
        val repository = AssignToDDListRepoProvider.provideAssignDDListRepository()
        var progress_wheel: ProgressWheel?= null
        if (Pref.isAttendanceFeatureOnly)
            progress_wheel = progress_wheel_attendance
        else
            progress_wheel = this.progress_wheel

        progress_wheel?.spin()
        BaseActivity.compositeDisposable.add(
                repository.assignToDDList(Pref.profile_state)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({ result ->
                            val response = result as AssignToDDListResponseModel
                            if (response.status == NetworkConstant.SUCCESS) {
                                val list = response.assigned_to_dd_list

                                if (list != null && list.isNotEmpty()) {

                                    doAsync {

                                        val assignDDList = AppDatabase.getDBInstance()?.ddListDao()?.getAll()
                                        if (assignDDList != null)
                                            AppDatabase.getDBInstance()?.ddListDao()?.delete()

                                        for (i in list.indices) {
                                            val assignToDD = AssignToDDEntity()
                                            assignToDD.dd_id = list[i].assigned_to_dd_id
                                            assignToDD.dd_name = list[i].assigned_to_dd_authorizer_name
                                            assignToDD.dd_phn_no = list[i].phn_no
                                            assignToDD.pp_id = list[i].assigned_to_pp_id
                                            assignToDD.type_id = list[i].type_id
                                            AppDatabase.getDBInstance()?.ddListDao()?.insert(assignToDD)
                                        }

                                        uiThread {
                                            progress_wheel.stopSpinning()
                                            if (!TextUtils.isEmpty(Pref.profile_state))
                                                getAssignedPPListApi()
                                            else {
                                                if (AppDatabase.getDBInstance()?.productListDao()?.getAll()!!.isEmpty())
                                                    getProductList("")
                                                else
                                                    getProductList(AppDatabase.getDBInstance()?.productListDao()?.getAll()?.get(0)?.date)
                                            }
                                        }
                                    }
                                } else {
                                    progress_wheel.stopSpinning()
                                    if (!TextUtils.isEmpty(Pref.profile_state))
                                        getAssignedPPListApi()
                                    else {
                                        if (AppDatabase.getDBInstance()?.productListDao()?.getAll()!!.isEmpty())
                                            getProductList("")
                                        else
                                            getProductList(AppDatabase.getDBInstance()?.productListDao()?.getAll()?.get(0)?.date)
                                    }
                                }
                            } else {
                                progress_wheel.stopSpinning()
                                if (!TextUtils.isEmpty(Pref.profile_state))
                                    getAssignedPPListApi()
                                else {
                                    if (AppDatabase.getDBInstance()?.productListDao()?.getAll()!!.isEmpty())
                                        getProductList("")
                                    else
                                        getProductList(AppDatabase.getDBInstance()?.productListDao()?.getAll()?.get(0)?.date)
                                }
                            }

                        }, { error ->
                            error.printStackTrace()
                            progress_wheel.stopSpinning()
                            if (!TextUtils.isEmpty(Pref.profile_state))
                                getAssignedPPListApi()
                            else {
                                if (AppDatabase.getDBInstance()?.productListDao()?.getAll()!!.isEmpty())
                                    getProductList("")
                                else
                                    getProductList(AppDatabase.getDBInstance()?.productListDao()?.getAll()?.get(0)?.date)
                            }
                        })
        )
    }

    private fun getAssignedPPListApi() {
        val repository = AssignToPPListRepoProvider.provideAssignPPListRepository()
        var progress_wheel: ProgressWheel?= null
        if (Pref.isAttendanceFeatureOnly)
            progress_wheel = progress_wheel_attendance
        else
            progress_wheel = this.progress_wheel

        progress_wheel?.spin()
        BaseActivity.compositeDisposable.add(
                repository.assignToPPList(Pref.profile_state)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({ result ->
                            val response = result as AssignToPPListResponseModel
                            if (response.status == NetworkConstant.SUCCESS) {
                                val list = response.assigned_to_pp_list

                                if (list != null && list.isNotEmpty()) {

                                    doAsync {

                                        val assignPPList = AppDatabase.getDBInstance()?.ppListDao()?.getAll()
                                        if (assignPPList != null)
                                            AppDatabase.getDBInstance()?.ppListDao()?.delete()

                                        for (i in list.indices) {
                                            val assignToPP = AssignToPPEntity()
                                            assignToPP.pp_id = list[i].assigned_to_pp_id
                                            assignToPP.pp_name = list[i].assigned_to_pp_authorizer_name
                                            assignToPP.pp_phn_no = list[i].phn_no
                                            AppDatabase.getDBInstance()?.ppListDao()?.insert(assignToPP)
                                        }

                                        uiThread {
                                            progress_wheel.stopSpinning()
                                            getAssignedToShopApi()
                                        }
                                    }
                                } else {
                                    progress_wheel.stopSpinning()
                                    getAssignedToShopApi()
                                }
                            } else {
                                progress_wheel.stopSpinning()
                                getAssignedToShopApi()
                            }

                        }, { error ->
                            error.printStackTrace()
                            progress_wheel.stopSpinning()
                            getAssignedToShopApi()
                        })
        )
    }

    private fun getAssignedToShopApi() {
        val repository = TypeListRepoProvider.provideTypeListRepository()
        progress_wheel.spin()
        BaseActivity.compositeDisposable.add(
                repository.assignToShopList(Pref.profile_state)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({ result ->
                            val response = result as AssignedToShopListResponseModel
                            if (response.status == NetworkConstant.SUCCESS) {
                                val list = response.shop_list

                                AppDatabase.getDBInstance()?.assignToShopDao()?.delete()

                                doAsync {
                                    list?.forEach {
                                        val shop = AssignToShopEntity()
                                        AppDatabase.getDBInstance()?.assignToShopDao()?.insert(shop.apply {
                                            assigned_to_shop_id = it.assigned_to_shop_id
                                            name = it.name
                                            phn_no = it.phn_no
                                            type_id = it.type_id
                                        })
                                    }

                                    uiThread {
                                        progress_wheel.stopSpinning()
                                        if (AppDatabase.getDBInstance()?.productListDao()?.getAll()!!.isEmpty())
                                            getProductList("")
                                        else
                                            getProductList(AppDatabase.getDBInstance()?.productListDao()?.getAll()?.get(0)?.date)
                                    }
                                }
                            }
                            else {
                                progress_wheel.stopSpinning()
                                if (AppDatabase.getDBInstance()?.productListDao()?.getAll()!!.isEmpty())
                                    getProductList("")
                                else
                                    getProductList(AppDatabase.getDBInstance()?.productListDao()?.getAll()?.get(0)?.date)
                            }

                        }, { error ->
                            progress_wheel.stopSpinning()
                            error.printStackTrace()
                            if (AppDatabase.getDBInstance()?.productListDao()?.getAll()!!.isEmpty())
                                getProductList("")
                            else
                                getProductList(AppDatabase.getDBInstance()?.productListDao()?.getAll()?.get(0)?.date)
                        })
        )
    }


    private fun getProductList(date: String?) {
        val repository = ProductListRepoProvider.productListProvider()
        var progress_wheel: ProgressWheel?= null
        if (Pref.isAttendanceFeatureOnly)
            progress_wheel = progress_wheel_attendance
        else
            progress_wheel = this.progress_wheel

        progress_wheel?.spin()
        BaseActivity.compositeDisposable.add(
                repository.getProductList(Pref.session_token!!, Pref.user_id!!, date!!)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({ result ->
                            val response = result as ProductListResponseModel
                            if (response.status == NetworkConstant.SUCCESS) {
                                val list = response.product_list

                                if (list != null && list.isNotEmpty()) {

                                    doAsync {

                                        if (!TextUtils.isEmpty(date))
                                            AppDatabase.getDBInstance()?.productListDao()?.deleteAllProduct()

                                        for (i in list.indices) {
                                            val productEntity = ProductListEntity()
                                            productEntity.id = list[i].id?.toInt()!!
                                            productEntity.product_name = list[i].product_name
                                            productEntity.watt = list[i].watt
                                            productEntity.category = list[i].category
                                            productEntity.brand = list[i].brand
                                            productEntity.brand_id = list[i].brand_id
                                            productEntity.watt_id = list[i].watt_id
                                            productEntity.category_id = list[i].category_id
                                            productEntity.date = AppUtils.getCurrentDateForShopActi()
                                            AppDatabase.getDBInstance()?.productListDao()?.insert(productEntity)
                                        }

                                        uiThread {
                                            progress_wheel.stopSpinning()
                                            getSelectedRouteListRefresh()
                                        }
                                    }
                                } else {
                                    progress_wheel.stopSpinning()
                                    getSelectedRouteListRefresh()
                                }
                            } else {
                                progress_wheel.stopSpinning()
                                getSelectedRouteListRefresh()
                            }

                        }, { error ->
                            error.printStackTrace()
                            progress_wheel.stopSpinning()
                            getSelectedRouteListRefresh()
                        })
        )
    }

    private fun getSelectedRouteListRefresh() {
        val list = AppDatabase.getDBInstance()?.selectedWorkTypeDao()?.getAll()
        if (list != null && list.isNotEmpty()) {
            AppDatabase.getDBInstance()?.selectedWorkTypeDao()?.delete()
            AppDatabase.getDBInstance()?.selectedRouteShopListDao()?.deleteData()
            AppDatabase.getDBInstance()?.selectedRouteListDao()?.deleteRoute()
        }

        val repository = GetRouteListRepoProvider.routeListRepoProvider()
        var progress_wheel: ProgressWheel?= null
        if (Pref.isAttendanceFeatureOnly)
            progress_wheel = progress_wheel_attendance
        else
            progress_wheel = this.progress_wheel

        progress_wheel?.spin()
        BaseActivity.compositeDisposable.add(
                repository.routeList()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({ result ->
                            val response = result as SelectedRouteListResponseModel
                            if (response.status == NetworkConstant.SUCCESS) {
                                val workTypeList = response.worktype

                                if (workTypeList != null && workTypeList.isNotEmpty()) {

                                    doAsync {

                                        for (i in workTypeList.indices) {
                                            val selectedwortkType = SelectedWorkTypeEntity()
                                            selectedwortkType.ID = workTypeList[i].id?.toInt()!!
                                            selectedwortkType.Descrpton = workTypeList[i].name
                                            selectedwortkType.date = AppUtils.getCurrentDate()
                                            AppDatabase.getDBInstance()?.selectedWorkTypeDao()?.insertAll(selectedwortkType)
                                        }

                                        val routeList = response.route_list
                                        if (routeList != null && routeList.isNotEmpty()) {
                                            for (i in routeList.indices) {
                                                val selectedRoute = SelectedRouteEntity()
                                                selectedRoute.route_id = routeList[i].id
                                                selectedRoute.route_name = routeList[i].route_name
                                                selectedRoute.date = AppUtils.getCurrentDate()

                                                val routeShopList = routeList[i].shop_details_list
                                                if (routeShopList != null && routeShopList.size > 0) {
                                                    for (j in routeShopList.indices) {
                                                        val selectedRouteShop = SelectedRouteShopListEntity()
                                                        selectedRouteShop.route_id = routeList[i].id
                                                        selectedRouteShop.shop_address = routeShopList[j].shop_address
                                                        selectedRouteShop.shop_contact_no = routeShopList[j].shop_contact_no
                                                        selectedRouteShop.shop_name = routeShopList[j].shop_name
                                                        selectedRouteShop.shop_id = routeShopList[j].shop_id
                                                        selectedRouteShop.date = AppUtils.getCurrentDate()
                                                        AppDatabase.getDBInstance()?.selectedRouteShopListDao()?.insert(selectedRouteShop)
                                                    }
                                                }

                                                AppDatabase.getDBInstance()?.selectedRouteListDao()?.insert(selectedRoute)
                                            }
                                        }

                                        uiThread {
                                            progress_wheel.stopSpinning()
                                            callUserConfigApi()
                                        }
                                    }
                                } else {
                                    progress_wheel.stopSpinning()
                                    callUserConfigApi()
                                }
                            } else {
                                progress_wheel.stopSpinning()
                                callUserConfigApi()
                            }

                        }, { error ->
                            error.printStackTrace()
                            progress_wheel.stopSpinning()
                            callUserConfigApi()
                        })
        )
    }

    private fun callUserConfigApi() {
        val repository = UserConfigRepoProvider.provideUserConfigRepository()
        var progress_wheel: ProgressWheel?= null
        if (Pref.isAttendanceFeatureOnly)
            progress_wheel = progress_wheel_attendance
        else
            progress_wheel = this.progress_wheel

        progress_wheel?.spin()
        BaseActivity.compositeDisposable.add(
                repository.userConfig(Pref.user_id!!)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({ result ->
                            val response = result as UserConfigResponseModel
                            if (response.status == NetworkConstant.SUCCESS) {

                                try {

                                    Log.e("Dashboard", "willLeaveApprovalEnable================> " + Pref.willLeaveApprovalEnable)


                                    if (response.getconfigure != null && response.getconfigure!!.size > 0) {
                                        for (i in response.getconfigure!!.indices) {
                                            if (response.getconfigure!![i].Key.equals("isVisitSync", ignoreCase = true)) {
                                                if (!TextUtils.isEmpty(response.getconfigure!![i].Value))
                                                    AppUtils.isVisitSync = response.getconfigure!![i].Value!!
                                            } else if (response.getconfigure!![i].Key.equals("isAddressUpdate", ignoreCase = true)) {
                                                if (!TextUtils.isEmpty(response.getconfigure!![i].Value))
                                                    AppUtils.isAddressUpdated = response.getconfigure!![i].Value!!
                                            } else if (response.getconfigure!![i].Key.equals("willShowUpdateDayPlan", ignoreCase = true)) {
                                                if (!TextUtils.isEmpty(response.getconfigure!![i].Value))
                                                    Pref.willShowUpdateDayPlan = response.getconfigure!![i].Value == "1"
                                            } else if (response.getconfigure!![i].Key.equals("updateDayPlanText", ignoreCase = true)) {
                                                if (!TextUtils.isEmpty(response.getconfigure!![i].Value))
                                                    Pref.updateDayPlanText = response.getconfigure!![i].Value!!
                                            } else if (response.getconfigure!![i].Key.equals("dailyPlanListHeaderText", ignoreCase = true)) {
                                                if (!TextUtils.isEmpty(response.getconfigure!![i].Value))
                                                    Pref.dailyPlanListHeaderText = response.getconfigure!![i].Value!!
                                            } else if (response.getconfigure!![i].Key.equals("isRateNotEditable", ignoreCase = true)) {
                                                if (!TextUtils.isEmpty(response.getconfigure!![i].Value))
                                                    Pref.isRateNotEditable = response.getconfigure!![i].Value == "1"
                                            } else if (response.getconfigure!![i].Key.equals("isMeetingAvailable", ignoreCase = true)) {
                                                if (!TextUtils.isEmpty(response.getconfigure!![i].Value))
                                                    Pref.isMeetingAvailable = response.getconfigure!![i].Value == "1"
                                            } else if (response.getconfigure?.get(i)?.Key.equals("willShowTeamDetails", ignoreCase = true)) {
                                                if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                                    Pref.willShowTeamDetails = response.getconfigure?.get(i)?.Value == "1"
                                                }
                                            } else if (response.getconfigure?.get(i)?.Key.equals("isAllowPJPUpdateForTeam", ignoreCase = true)) {
                                                if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                                    Pref.isAllowPJPUpdateForTeam = response.getconfigure?.get(i)?.Value == "1"
                                                }
                                            } else if (response.getconfigure!![i].Key.equals("willLeaveApprovalEnable", ignoreCase = true)) {
                                                if (!TextUtils.isEmpty(response.getconfigure!![i].Value))
                                                    Pref.willLeaveApprovalEnable = response.getconfigure!![i].Value == "1"
                                            } else if (response.getconfigure?.get(i)?.Key.equals("willReportShow", ignoreCase = true)) {
                                                if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                                    Pref.willReportShow = response.getconfigure?.get(i)?.Value == "1"
                                                }
                                            } else if (response.getconfigure?.get(i)?.Key.equals("willAttendanceReportShow", ignoreCase = true)) {
                                                if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                                    Pref.willAttendanceReportShow = response.getconfigure?.get(i)?.Value == "1"
                                                }
                                            } else if (response.getconfigure?.get(i)?.Key.equals("willPerformanceReportShow", ignoreCase = true)) {
                                                if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                                    Pref.willPerformanceReportShow = response.getconfigure?.get(i)?.Value == "1"
                                                }
                                            } else if (response.getconfigure?.get(i)?.Key.equals("willVisitReportShow", ignoreCase = true)) {
                                                if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                                    Pref.willVisitReportShow = response.getconfigure?.get(i)?.Value == "1"
                                                }
                                            } else if (response.getconfigure?.get(i)?.Key.equals("attendance_text", ignoreCase = true)) {
                                                if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                                    Pref.attendance_text = response.getconfigure?.get(i)?.Value!!
                                                }
                                            } else if (response.getconfigure?.get(i)?.Key.equals("willTimesheetShow", ignoreCase = true)) {
                                                if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                                    Pref.willTimesheetShow = response.getconfigure?.get(i)?.Value == "1"
                                                }
                                            } else if (response.getconfigure?.get(i)?.Key.equals("isAttendanceFeatureOnly", ignoreCase = true)) {
                                                if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                                    Pref.isAttendanceFeatureOnly = response.getconfigure?.get(i)?.Value == "1"
                                                }
                                            } else if (response.getconfigure?.get(i)?.Key.equals("iscollectioninMenuShow", ignoreCase = true)) {
                                                if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                                    Pref.isCollectioninMenuShow = response.getconfigure?.get(i)?.Value == "1"
                                                }
                                            } else if (response.getconfigure?.get(i)?.Key.equals("isVisitShow", ignoreCase = true)) {
                                                if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                                    Pref.isVisitShow = response.getconfigure?.get(i)?.Value == "1"
                                                }
                                            } else if (response.getconfigure?.get(i)?.Key.equals("isOrderShow", ignoreCase = true)) {
                                                if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                                    Pref.isOrderShow = response.getconfigure?.get(i)?.Value == "1"
                                                }
                                            } else if (response.getconfigure?.get(i)?.Key.equals("isShopAddEditAvailable", ignoreCase = true)) {
                                                if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                                    Pref.isShopAddEditAvailable = response.getconfigure?.get(i)?.Value == "1"
                                                }
                                            } else if (response.getconfigure?.get(i)?.Key.equals("isEntityCodeVisible", ignoreCase = true)) {
                                                if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                                    Pref.isEntityCodeVisible = response.getconfigure?.get(i)?.Value == "1"
                                                }
                                            } else if (response.getconfigure?.get(i)?.Key.equals("isAreaMandatoryInPartyCreation", ignoreCase = true)) {
                                                if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                                    Pref.isAreaMandatoryInPartyCreation = response.getconfigure?.get(i)?.Value == "1"
                                                }
                                            } else if (response.getconfigure?.get(i)?.Key.equals("isShowPartyInAreaWiseTeam", ignoreCase = true)) {
                                                if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                                    Pref.isShowPartyInAreaWiseTeam = response.getconfigure?.get(i)?.Value == "1"
                                                }
                                            } else if (response.getconfigure?.get(i)?.Key.equals("isChangePasswordAllowed", ignoreCase = true)) {
                                                if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                                    Pref.isChangePasswordAllowed = response.getconfigure?.get(i)?.Value == "1"
                                                }
                                            } else if (response.getconfigure?.get(i)?.Key.equals("isQuotationShow", ignoreCase = true)) {
                                                if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                                    Pref.isQuotationShow = response.getconfigure?.get(i)?.Value == "1"
                                                }
                                            } else if (response.getconfigure?.get(i)?.Key.equals("isQuotationPopupShow", ignoreCase = true)) {
                                                if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                                    Pref.isQuotationPopupShow = response.getconfigure?.get(i)?.Value == "1"
                                                }
                                            } else if (response.getconfigure?.get(i)?.Key.equals("isHomeRestrictAttendance", ignoreCase = true)) {
                                                if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                                    Pref.isHomeRestrictAttendance = response.getconfigure?.get(i)?.Value!!
                                                }
                                            } else if (response.getconfigure?.get(i)?.Key.equals("homeLocDistance", ignoreCase = true)) {
                                                if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                                    Pref.homeLocDistance = response.getconfigure?.get(i)?.Value!!
                                                }
                                            } else if (response.getconfigure?.get(i)?.Key.equals("shopLocAccuracy", ignoreCase = true)) {
                                                if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                                    Pref.shopLocAccuracy = response.getconfigure?.get(i)?.Value!!
                                                }
                                            } else if (response.getconfigure?.get(i)?.Key.equals("isMultipleAttendanceSelection", ignoreCase = true)) {
                                                if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                                    Pref.isMultipleAttendanceSelection = response.getconfigure?.get(i)?.Value == "1"
                                                }
                                            }
                                            else if (response.getconfigure?.get(i)?.Key.equals("isOrderReplacedWithTeam", ignoreCase = true)) {
                                                if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                                    Pref.isOrderReplacedWithTeam = response.getconfigure?.get(i)?.Value == "1"
                                                }
                                            }
                                            else if (response.getconfigure?.get(i)?.Key.equals("isDDShowForMeeting", ignoreCase = true)) {
                                                if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                                    Pref.isDDShowForMeeting = response.getconfigure?.get(i)?.Value == "1"
                                                }
                                            }
                                            else if (response.getconfigure?.get(i)?.Key.equals("isDDMandatoryForMeeting", ignoreCase = true)) {
                                                if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                                    Pref.isDDMandatoryForMeeting = response.getconfigure?.get(i)?.Value == "1"
                                                }
                                            }
                                            else if (response.getconfigure?.get(i)?.Key.equals("isOfflineTeam", ignoreCase = true)) {
                                                if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                                    Pref.isOfflineTeam = response.getconfigure?.get(i)?.Value == "1"
                                                }
                                            }
                                            else if (response.getconfigure?.get(i)?.Key.equals("isAllTeamAvailable", ignoreCase = true)) {
                                                if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                                    Pref.isAllTeamAvailable = response.getconfigure?.get(i)?.Value == "1"
                                                }
                                            }
                                            else if (response.getconfigure?.get(i)?.Key.equals("isNextVisitDateMandatory", ignoreCase = true)) {
                                                if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                                    Pref.isNextVisitDateMandatory = response.getconfigure?.get(i)?.Value == "1"
                                                }
                                            }
                                            else if (response.getconfigure?.get(i)?.Key.equals("isRecordAudioEnable", ignoreCase = true)) {
                                                if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                                    Pref.isRecordAudioEnable = response.getconfigure?.get(i)?.Value == "1"
                                                }
                                            }
                                            else if (response.getconfigure?.get(i)?.Key.equals("isShowCurrentLocNotifiaction", ignoreCase = true)) {
                                                if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                                    Pref.isShowCurrentLocNotifiaction = response.getconfigure?.get(i)?.Value == "1"
                                                }
                                            }
                                            else if (response.getconfigure?.get(i)?.Key.equals("isUpdateWorkTypeEnable", ignoreCase = true)) {
                                                if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                                    Pref.isUpdateWorkTypeEnable = response.getconfigure?.get(i)?.Value == "1"
                                                }
                                            }
                                            else if (response.getconfigure?.get(i)?.Key.equals("isAchievementEnable", ignoreCase = true)) {
                                                if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                                    Pref.isAchievementEnable = response.getconfigure?.get(i)?.Value == "1"
                                                }
                                            }
                                            else if (response.getconfigure?.get(i)?.Key.equals("isTarVsAchvEnable", ignoreCase = true)) {
                                                if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                                    Pref.isTarVsAchvEnable = response.getconfigure?.get(i)?.Value == "1"
                                                }
                                            }
                                            else if (response.getconfigure?.get(i)?.Key.equals("isLeaveEnable", ignoreCase = true)) {
                                                if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                                    Pref.isLeaveEnable = response.getconfigure?.get(i)?.Value == "1"
                                                }
                                            }
                                            else if (response.getconfigure?.get(i)?.Key.equals("isOrderMailVisible", ignoreCase = true)) {
                                                if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                                    Pref.isOrderMailVisible = response.getconfigure?.get(i)?.Value == "1"
                                                }
                                            }
                                            else if (response.getconfigure?.get(i)?.Key.equals("isShopEditEnable", ignoreCase = true)) {
                                                if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                                    Pref.isShopEditEnable = response.getconfigure?.get(i)?.Value == "1"
                                                }
                                            }
                                            else if (response.getconfigure?.get(i)?.Key.equals("isTaskEnable", ignoreCase = true)) {
                                                if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                                    Pref.isTaskEnable = response.getconfigure?.get(i)?.Value == "1"
                                                }
                                            }
                                            else if (response.getconfigure?.get(i)?.Key.equals("isAppInfoEnable", ignoreCase = true)) {
                                                if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                                    Pref.isAppInfoEnable = response.getconfigure?.get(i)?.Value == "1"
                                                }
                                            }
                                            else if (response.getconfigure?.get(i)?.Key.equals("appInfoMins", ignoreCase = true)) {
                                                if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                                    Pref.appInfoMins = response.getconfigure?.get(i)?.Value!!
                                                }
                                            }
                                            else if (response.getconfigure!![i].Key.equals("autoRevisitDistance", ignoreCase = true)) {
                                                if (!TextUtils.isEmpty(response.getconfigure!![i].Value))
                                                    Pref.autoRevisitDistance = response.getconfigure!![i].Value!!
                                            } else if (response.getconfigure!![i].Key.equals("autoRevisitTime", ignoreCase = true)) {
                                                if (!TextUtils.isEmpty(response.getconfigure!![i].Value))
                                                    Pref.autoRevisitTime = response.getconfigure!![i].Value!!
                                            } else if (response.getconfigure!![i].Key.equals("willAutoRevisitEnable", ignoreCase = true)) {
                                                if (!TextUtils.isEmpty(response.getconfigure!![i].Value))
                                                    Pref.willAutoRevisitEnable = response.getconfigure!![i].Value == "1"
                                            }
                                            else if (response.getconfigure!![i].Key.equals("dynamicFormName", ignoreCase = true)) {
                                                if (!TextUtils.isEmpty(response.getconfigure!![i].Value))
                                                    Pref.dynamicFormName = response.getconfigure!![i].Value!!
                                            }
                                            else if (response.getconfigure!![i].Key.equals("willDynamicShow", ignoreCase = true)) {
                                                if (!TextUtils.isEmpty(response.getconfigure!![i].Value))
                                                    Pref.willDynamicShow = response.getconfigure!![i].Value == "1"
                                            }
                                            else if (response.getconfigure!![i].Key.equals("willActivityShow", ignoreCase = true)) {
                                                if (!TextUtils.isEmpty(response.getconfigure!![i].Value))
                                                    Pref.willActivityShow = response.getconfigure!![i].Value == "1"
                                            }
                                            else if (response.getconfigure!![i].Key.equals("willMoreVisitUpdateCompulsory", ignoreCase = true)) {
                                                if (!TextUtils.isEmpty(response.getconfigure!![i].Value))
                                                    Pref.willMoreVisitUpdateCompulsory = response.getconfigure!![i].Value == "1"
                                            }
                                            else if (response.getconfigure!![i].Key.equals("willMoreVisitUpdateOptional", ignoreCase = true)) {
                                                if (!TextUtils.isEmpty(response.getconfigure!![i].Value))
                                                    Pref.willMoreVisitUpdateOptional = response.getconfigure!![i].Value == "1"
                                            }
                                            else if (response.getconfigure!![i].Key.equals("isDocumentRepoShow", ignoreCase = true)) {
                                                if (!TextUtils.isEmpty(response.getconfigure!![i].Value))
                                                    Pref.isDocumentRepoShow = response.getconfigure!![i].Value == "1"
                                            }
                                            else if (response.getconfigure!![i].Key.equals("isChatBotShow", ignoreCase = true)) {
                                                if (!TextUtils.isEmpty(response.getconfigure!![i].Value))
                                                    Pref.isChatBotShow = response.getconfigure!![i].Value == "1"
                                            }
                                            else if (response.getconfigure!![i].Key.equals("isAttendanceBotShow", ignoreCase = true)) {
                                                if (!TextUtils.isEmpty(response.getconfigure!![i].Value))
                                                    Pref.isAttendanceBotShow = response.getconfigure!![i].Value == "1"
                                            }
                                            else if (response.getconfigure!![i].Key.equals("isVisitBotShow", ignoreCase = true)) {
                                                if (!TextUtils.isEmpty(response.getconfigure!![i].Value))
                                                    Pref.isVisitBotShow = response.getconfigure!![i].Value == "1"
                                            }
                                            else if (response.getconfigure!![i].Key.equals("isShowOrderRemarks", ignoreCase = true)) {
                                                if (!TextUtils.isEmpty(response.getconfigure!![i].Value))
                                                    Pref.isShowOrderRemarks = response.getconfigure!![i].Value == "1"
                                            }
                                            else if (response.getconfigure!![i].Key.equals("isShowOrderSignature", ignoreCase = true)) {
                                                if (!TextUtils.isEmpty(response.getconfigure!![i].Value))
                                                    Pref.isShowOrderSignature = response.getconfigure!![i].Value == "1"
                                            }
                                            else if (response.getconfigure!![i].Key.equals("isShowSmsForParty", ignoreCase = true)) {
                                                if (!TextUtils.isEmpty(response.getconfigure!![i].Value))
                                                    Pref.isShowSmsForParty = response.getconfigure!![i].Value == "1"
                                            }
                                            else if (response.getconfigure!![i].Key.equals("isVisitPlanShow", ignoreCase = true)) {
                                                if (!TextUtils.isEmpty(response.getconfigure!![i].Value))
                                                    Pref.isVisitPlanShow = response.getconfigure!![i].Value == "1"
                                            }
                                            else if (response.getconfigure!![i].Key.equals("isVisitPlanMandatory", ignoreCase = true)) {
                                                if (!TextUtils.isEmpty(response.getconfigure!![i].Value))
                                                    Pref.isVisitPlanMandatory = response.getconfigure!![i].Value == "1"
                                            }
                                            else if (response.getconfigure!![i].Key.equals("isAttendanceDistanceShow", ignoreCase = true)) {
                                                if (!TextUtils.isEmpty(response.getconfigure!![i].Value))
                                                    Pref.isAttendanceDistanceShow = response.getconfigure!![i].Value == "1"
                                            }
                                            else if (response.getconfigure!![i].Key.equals("willTimelineWithFixedLocationShow", ignoreCase = true)) {
                                                if (!TextUtils.isEmpty(response.getconfigure!![i].Value))
                                                    Pref.willTimelineWithFixedLocationShow = response.getconfigure!![i].Value == "1"
                                            }
                                            else if (response.getconfigure!![i].Key.equals("isShowTimeline", ignoreCase = true)) {
                                                if (!TextUtils.isEmpty(response.getconfigure!![i].Value))
                                                    Pref.isShowTimeline = response.getconfigure!![i].Value == "1"
                                            }
                                            else if (response.getconfigure!![i].Key.equals("willScanVisitingCard", ignoreCase = true)) {
                                                if (!TextUtils.isEmpty(response.getconfigure!![i].Value))
                                                    Pref.willScanVisitingCard = response.getconfigure!![i].Value == "1"
                                            }
                                            else if (response.getconfigure!![i].Key.equals("isCreateQrCode", ignoreCase = true)) {
                                                if (!TextUtils.isEmpty(response.getconfigure!![i].Value))
                                                    Pref.isCreateQrCode = response.getconfigure!![i].Value == "1"
                                            }
                                            else if (response.getconfigure!![i].Key.equals("isScanQrForRevisit", ignoreCase = true)) {
                                                if (!TextUtils.isEmpty(response.getconfigure!![i].Value))
                                                    Pref.isScanQrForRevisit = response.getconfigure!![i].Value == "1"
                                            }
                                            else if (response.getconfigure!![i].Key.equals("isShowLogoutReason", ignoreCase = true)) {
                                                if (!TextUtils.isEmpty(response.getconfigure!![i].Value))
                                                    Pref.isShowLogoutReason = response.getconfigure!![i].Value == "1"
                                            }
                                            else if (response.getconfigure!![i].Key.equals("willShowHomeLocReason", ignoreCase = true)) {
                                                if (!TextUtils.isEmpty(response.getconfigure!![i].Value))
                                                    Pref.willShowHomeLocReason = response.getconfigure!![i].Value == "1"
                                            }
                                            else if (response.getconfigure!![i].Key.equals("willShowShopVisitReason", ignoreCase = true)) {
                                                if (!TextUtils.isEmpty(response.getconfigure!![i].Value))
                                                    Pref.willShowShopVisitReason = response.getconfigure!![i].Value == "1"
                                            }
                                            else if (response.getconfigure!![i].Key.equals("minVisitDurationSpentTime", ignoreCase = true)) {
                                                if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                                    Pref.minVisitDurationSpentTime = response.getconfigure?.get(i)?.Value!!
                                                }
                                            }
                                            else if (response.getconfigure!![i].Key.equals("willShowPartyStatus", ignoreCase = true)) {
                                                if (!TextUtils.isEmpty(response.getconfigure!![i].Value))
                                                    Pref.willShowPartyStatus = response.getconfigure!![i].Value == "1"
                                            }
                                            else if (response.getconfigure!![i].Key.equals("willShowEntityTypeforShop", ignoreCase = true)) {
                                                if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                                    Pref.willShowEntityTypeforShop = response.getconfigure!![i].Value == "1"
                                                }
                                            }
                                            else if (response.getconfigure!![i].Key.equals("isShowRetailerEntity", ignoreCase = true)) {
                                                if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                                    Pref.isShowRetailerEntity = response.getconfigure!![i].Value == "1"
                                                }
                                            }
                                            else if (response.getconfigure!![i].Key.equals("isShowDealerForDD", ignoreCase = true)) {
                                                if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                                    Pref.isShowDealerForDD = response.getconfigure!![i].Value == "1"
                                                }
                                            }
                                            else if (response.getconfigure!![i].Key.equals("isShowBeatGroup", ignoreCase = true)) {
                                                if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                                    Pref.isShowBeatGroup = response.getconfigure!![i].Value == "1"
                                                }
                                            }
                                            else if (response.getconfigure!![i].Key.equals("isShowShopBeatWise", ignoreCase = true)) {
                                                if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                                    Pref.isShowShopBeatWise = response.getconfigure!![i].Value == "1"
                                                }
                                            }
                                            else if (response.getconfigure!![i].Key.equals("isShowBankDetailsForShop", ignoreCase = true)) {
                                                if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                                    Pref.isShowBankDetailsForShop = response.getconfigure!![i].Value == "1"
                                                }
                                            }
                                            else if (response.getconfigure!![i].Key.equals("isShowOTPVerificationPopup", ignoreCase = true)) {
                                                if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                                    Pref.isShowOTPVerificationPopup = response.getconfigure!![i].Value == "1"
                                                }
                                            }
                                            else if (response.getconfigure!![i].Key.equals("locationTrackInterval", ignoreCase = true)) {
                                                if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                                    Pref.locationTrackInterval = response.getconfigure!![i].Value!!
                                                }
                                            }
                                            else if (response.getconfigure!![i].Key.equals("isShowMicroLearning", ignoreCase = true)) {
                                                if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                                    Pref.isShowMicroLearning = response.getconfigure!![i].Value == "1"
                                                }
                                            }
                                            else if (response.getconfigure!![i].Key.equals("homeLocReasonCheckMins", ignoreCase = true)) {
                                                if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                                    Pref.homeLocReasonCheckMins = response.getconfigure!![i].Value!!
                                                }
                                            }
                                            else if (response.getconfigure!![i].Key.equals("currentLocationNotificationMins", ignoreCase = true)) {
                                                if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                                    Pref.currentLocationNotificationMins = response.getconfigure!![i].Value!!
                                                }
                                            }
                                            else if (response.getconfigure!![i].Key.equals("isMultipleVisitEnable", ignoreCase = true)) {
                                                if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                                    Pref.isMultipleVisitEnable = response.getconfigure!![i].Value!! == "1"
                                                }
                                            }
                                            else if (response.getconfigure!![i].Key.equals("isShowVisitRemarks", ignoreCase = true)) {
                                                if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                                    Pref.isShowVisitRemarks = response.getconfigure!![i].Value!! == "1"
                                                }
                                            }
                                            else if (response.getconfigure!![i].Key.equals("isShowNearbyCustomer", ignoreCase = true)) {
                                                if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                                    Pref.isShowNearbyCustomer = response.getconfigure!![i].Value == "1"
                                                }
                                            }
                                            else if(response.getconfigure!![i].Key.equals("isServiceFeatureEnable",ignoreCase = true)){
                                                if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                                    Pref.isServiceFeatureEnable = response.getconfigure!![i].Value == "1"
                                                }
                                            }
                                            else if(response.getconfigure!![i].Key.equals("isPatientDetailsShowInOrder",ignoreCase = true)){
                                                if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                                    Pref.isPatientDetailsShowInOrder = response.getconfigure!![i].Value == "1"
                                                }
                                            }
                                            else if(response.getconfigure!![i].Key.equals("isPatientDetailsShowInCollection",ignoreCase = true)){
                                                if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                                    Pref.isPatientDetailsShowInCollection = response.getconfigure!![i].Value == "1"
                                                }
                                            }
                                            else if (response.getconfigure!![i].Key.equals("isShopImageMandatory", ignoreCase = true)) {
                                                if (!TextUtils.isEmpty(response.getconfigure?.get(i)?.Value)) {
                                                    Pref.isShopImageMandatory = response.getconfigure!![i].Value == "1"
                                                }
                                            }
                                        }
                                    }
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                            Log.e("Dashboard", "willLeaveApprovalEnable================> " + Pref.willLeaveApprovalEnable)

                            progress_wheel.stopSpinning()
                            getConfigFetchApi()

                        }, { error ->
                            error.printStackTrace()
                            progress_wheel.stopSpinning()
                            getConfigFetchApi()
                        })
        )
    }

    private fun getConfigFetchApi() {
        val repository = ConfigFetchRepoProvider.provideConfigFetchRepository()
        var progress_wheel: ProgressWheel?= null
        if (Pref.isAttendanceFeatureOnly)
            progress_wheel = progress_wheel_attendance
        else
            progress_wheel = this.progress_wheel

        progress_wheel?.spin()
        BaseActivity.compositeDisposable.add(
                repository.configFetch()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({ result ->

                            val configResponse = result as ConfigFetchResponseModel
                            XLog.d("ConfigFetchApiResponse : " + "\n" + "Status=====> " + configResponse.status + ", Message====> " + configResponse.message)

                            progress_wheel.stopSpinning()
                            if (configResponse.status == NetworkConstant.SUCCESS) {

                                if (!TextUtils.isEmpty(configResponse.min_distance))
                                    AppUtils.minDistance = configResponse.min_distance!!

                                if (!TextUtils.isEmpty(configResponse.max_distance))
                                    AppUtils.maxDistance = configResponse.max_distance!!

                                if (!TextUtils.isEmpty(configResponse.max_accuracy))
                                    AppUtils.maxAccuracy = configResponse.max_accuracy!!

                                if (!TextUtils.isEmpty(configResponse.min_accuracy))
                                    AppUtils.minAccuracy = configResponse.min_accuracy!!

                                /*if (!TextUtils.isEmpty(configResponse.idle_time))
                                    AppUtils.idle_time = configResponse.idle_time!!*/

                                if (configResponse.willStockShow != null)
                                    Pref.willStockShow = configResponse.willStockShow!!

                                // From Hahnemann
                                if (configResponse.isPrimaryTargetMandatory != null)
                                    Pref.isPrimaryTargetMandatory = configResponse.isPrimaryTargetMandatory!!

                                if (configResponse.isRevisitCaptureImage != null)
                                    Pref.isRevisitCaptureImage = configResponse.isRevisitCaptureImage!!

                                if (configResponse.isShowAllProduct != null)
                                    Pref.isShowAllProduct = configResponse.isShowAllProduct!!

                                if (configResponse.isStockAvailableForAll != null)
                                    Pref.isStockAvailableForAll = configResponse.isStockAvailableForAll!!

                                if (configResponse.isStockAvailableForPopup != null)
                                    Pref.isStockAvailableForPopup = configResponse.isStockAvailableForPopup!!

                                if (configResponse.isOrderAvailableForPopup != null)
                                    Pref.isOrderAvailableForPopup = configResponse.isOrderAvailableForPopup!!

                                if (configResponse.isCollectionAvailableForPopup != null)
                                    Pref.isCollectionAvailableForPopup = configResponse.isCollectionAvailableForPopup!!

                                if (configResponse.isDDFieldEnabled != null)
                                    Pref.isDDFieldEnabled = configResponse.isDDFieldEnabled!!

                                if (!TextUtils.isEmpty(configResponse.maxFileSize))
                                    Pref.maxFileSize = configResponse.maxFileSize!!

                                if (configResponse.willKnowYourStateShow != null)
                                    Pref.willKnowYourStateShow = configResponse.willKnowYourStateShow!!

                                if (configResponse.willAttachmentCompulsory != null)
                                    Pref.willAttachmentCompulsory = configResponse.willAttachmentCompulsory!!

                                if (configResponse.canAddBillingFromBillingList != null)
                                    Pref.canAddBillingFromBillingList = configResponse.canAddBillingFromBillingList!!

                                if (!TextUtils.isEmpty(configResponse.allPlanListHeaderText))
                                    Pref.allPlanListHeaderText = configResponse.allPlanListHeaderText!!

                                if (configResponse.willSetYourTodaysTargetVisible != null)
                                    Pref.willSetYourTodaysTargetVisible = configResponse.willSetYourTodaysTargetVisible!!

                                if (!TextUtils.isEmpty(configResponse.attendenceAlertHeading))
                                    Pref.attendenceAlertHeading = configResponse.attendenceAlertHeading!!

                                if (!TextUtils.isEmpty(configResponse.attendenceAlertText))
                                    Pref.attendenceAlertText = configResponse.attendenceAlertText!!

                                if (!TextUtils.isEmpty(configResponse.meetingText))
                                    Pref.meetingText = configResponse.meetingText!!

                                if (!TextUtils.isEmpty(configResponse.meetingDistance))
                                    Pref.meetingDistance = configResponse.meetingDistance!!

                                if (configResponse.isActivatePJPFeature != null)
                                    Pref.isActivatePJPFeature = configResponse.isActivatePJPFeature!!

                                if (configResponse.willReimbursementShow != null)
                                    Pref.willReimbursementShow = configResponse.willReimbursementShow!!

                                if (!TextUtils.isEmpty(configResponse.updateBillingText))
                                    Pref.updateBillingText = configResponse.updateBillingText!!

                                if (configResponse.isRateOnline != null)
                                    Pref.isRateOnline = configResponse.isRateOnline!!

                                if (!TextUtils.isEmpty(configResponse.ppText))
                                    Pref.ppText = configResponse.ppText

                                if (!TextUtils.isEmpty(configResponse.ddText))
                                    Pref.ddText = configResponse.ddText

                                if (!TextUtils.isEmpty(configResponse.shopText))
                                    Pref.shopText = configResponse.shopText

                                if (configResponse.isCustomerFeatureEnable != null)
                                    Pref.isCustomerFeatureEnable = configResponse.isCustomerFeatureEnable!!

                                if (configResponse.isAreaVisible != null)
                                    Pref.isAreaVisible = configResponse.isAreaVisible!!

                                if (!TextUtils.isEmpty(configResponse.cgstPercentage))
                                    Pref.cgstPercentage = configResponse.cgstPercentage

                                if (!TextUtils.isEmpty(configResponse.sgstPercentage))
                                    Pref.sgstPercentage = configResponse.sgstPercentage

                                if (!TextUtils.isEmpty(configResponse.tcsPercentage))
                                    Pref.tcsPercentage = configResponse.tcsPercentage

                                if (!TextUtils.isEmpty(configResponse.docAttachmentNo))
                                    Pref.docAttachmentNo = configResponse.docAttachmentNo

                                if (!TextUtils.isEmpty(configResponse.chatBotMsg))
                                    Pref.chatBotMsg = configResponse.chatBotMsg

                                if (!TextUtils.isEmpty(configResponse.contactMail))
                                    Pref.contactMail = configResponse.contactMail

                                if (configResponse.isVoiceEnabledForAttendanceSubmit != null)
                                    Pref.isVoiceEnabledForAttendanceSubmit = configResponse.isVoiceEnabledForAttendanceSubmit!!

                                if (configResponse.isVoiceEnabledForOrderSaved != null)
                                    Pref.isVoiceEnabledForOrderSaved = configResponse.isVoiceEnabledForOrderSaved!!

                                if (configResponse.isVoiceEnabledForInvoiceSaved != null)
                                    Pref.isVoiceEnabledForInvoiceSaved = configResponse.isVoiceEnabledForInvoiceSaved!!

                                if (configResponse.isVoiceEnabledForCollectionSaved != null)
                                    Pref.isVoiceEnabledForCollectionSaved = configResponse.isVoiceEnabledForCollectionSaved!!

                                if (configResponse.isVoiceEnabledForHelpAndTipsInBot != null)
                                    Pref.isVoiceEnabledForHelpAndTipsInBot = configResponse.isVoiceEnabledForHelpAndTipsInBot!!


                            }
                            BaseActivity.isApiInitiated = false
                            checkToCallAlarmConfigApi()

                        }, { error ->
                            BaseActivity.isApiInitiated = false
                            error.printStackTrace()
                            checkToCallAlarmConfigApi()
                            progress_wheel.stopSpinning()
                            XLog.d("ConfigFetchApiResponse ERROR: " + error.localizedMessage)
                        })
        )
    }

    private fun checkToCallAlarmConfigApi() {
        if (Pref.willAlarmTrigger)
            callAlarmConfig()
        else
            getPjpListApi()

    }

    private fun callAlarmConfig() {
        val repository = AlarmConfigRepoProvider.provideAlarmConfigRepository()
        var progress_wheel: ProgressWheel?= null
        if (Pref.isAttendanceFeatureOnly)
            progress_wheel = progress_wheel_attendance
        else
            progress_wheel = this.progress_wheel

        progress_wheel?.spin()
        BaseActivity.compositeDisposable.add(
                repository.alarmConfig()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({ result ->

                            val configResponse = result as AlarmConfigResponseModel
                            XLog.d("AlarmConfigApiResponse : " + "\n" + "Status=====> " + configResponse.status + ", Message====> " + configResponse.message)

                            progress_wheel.stopSpinning()
                            if (configResponse.status == NetworkConstant.SUCCESS) {

                                val alarmArr = java.util.ArrayList<AlarmData>()
                                for (item in configResponse.alarm_settings_list!!) {

                                    if (AppUtils.getCurrentTimeInMintes() < ((item.alarm_time_hours!!.toInt() * 60) + item.alarm_time_mins!!.toInt())) {
                                        val al = AlarmData()
                                        al.requestCode = 2010 + item.id!!.toInt()
                                        al.id = item.id
                                        al.report_id = item.report_id!!
                                        al.report_title = item.report_title!!
                                        al.alarm_time_hours = item.alarm_time_hours!!
                                        al.alarm_time_mins = item.alarm_time_mins!!

                                        alarmArr.add(al)

                                        AlarmReceiver.setAlarm(mContext, item.alarm_time_hours!!.toInt(), item.alarm_time_mins!!.toInt(), al.requestCode)
                                    }

                                }

                                AlarmReceiver.saveSharedPreferencesLogList(mContext, alarmArr)
                            }

                            BaseActivity.isApiInitiated = false
                            getPjpListApi()
                        }, { error ->
                            BaseActivity.isApiInitiated = false
                            error.printStackTrace()
                            progress_wheel.stopSpinning()
                            XLog.d("AlarmConfigApiResponse ERROR: " + error.localizedMessage)
                            getPjpListApi()
                        })
        )
    }

    private fun getPjpListApi() {
        var progress_wheel: ProgressWheel?= null
        if (Pref.isAttendanceFeatureOnly)
            progress_wheel = progress_wheel_attendance
        else
            progress_wheel = this.progress_wheel

        progress_wheel?.spin()
        val repository = TeamRepoProvider.teamRepoProvider()
        BaseActivity.compositeDisposable.add(
                repository.getUserPJPList(AppUtils.getCurrentDateForShopActi())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({ result ->
                            val response = result as UserPjpResponseModel
                            XLog.d("GET USER PJP DATA : " + "RESPONSE : " + response.status + "\n" + "Time : " + AppUtils.getCurrentDateTime() + ", USER :" + Pref.user_name + ",MESSAGE : " + response.message)
                            if (response.status == NetworkConstant.SUCCESS) {

                                if (response.pjp_list != null && response.pjp_list.isNotEmpty()) {

                                    doAsync {

                                        AppDatabase.getDBInstance()?.pjpListDao()?.deleteAll()

                                        response.pjp_list.forEach {
                                            val pjpEntity = PjpListEntity()
                                            AppDatabase.getDBInstance()?.pjpListDao()?.insert(pjpEntity.apply {
                                                pjp_id = it.id
                                                from_time = it.from_time
                                                to_time = it.to_time
                                                customer_name = it.customer_name
                                                customer_id = it.customer_id
                                                location = it.location
                                                date = it.date
                                                remarks = it.remarks
                                            })
                                        }

                                        uiThread {
                                            progress_wheel.stopSpinning()
                                            getTeamAreaListApi()
                                        }
                                    }
                                } else {
                                    progress_wheel.stopSpinning()
                                    getTeamAreaListApi()
                                }


                            } else {
                                progress_wheel.stopSpinning()
                                getTeamAreaListApi()
                            }

                        }, { error ->
                            progress_wheel.stopSpinning()
                            XLog.d("GET USER PJP DATA : " + "ERROR : " + "\n" + "Time : " + AppUtils.getCurrentDateTime() + ", USER :" + Pref.user_name + ",MESSAGE : " + error.localizedMessage)
                            error.printStackTrace()
                            getTeamAreaListApi()
                        })
        )
    }

    private fun getTeamAreaListApi() {
        val repository = TeamRepoProvider.teamRepoProvider()
        var progress_wheel: ProgressWheel?= null
        if (Pref.isAttendanceFeatureOnly)
            progress_wheel = progress_wheel_attendance
        else
            progress_wheel = this.progress_wheel

        progress_wheel?.spin()
        BaseActivity.compositeDisposable.add(
                repository.teamAreaList()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({ result ->
                            val response = result as TeamAreaListResponseModel
                            if (response.status == NetworkConstant.SUCCESS) {
                                val list = response.area_list

                                if (list != null && list.isNotEmpty()) {

                                    AppDatabase.getDBInstance()?.memberAreaDao()?.deleteAll()

                                    doAsync {

                                        list.forEach {
                                            val area = TeamAreaEntity()
                                            AppDatabase.getDBInstance()?.memberAreaDao()?.insertAll(area.apply {
                                                area_id = it.area_id
                                                area_name = it.area_name
                                                user_id = it.user_id
                                            })
                                        }

                                        uiThread {
                                            progress_wheel.stopSpinning()
                                            getTimesheetDropdownApi()
                                        }
                                    }
                                } else {
                                    progress_wheel.stopSpinning()
                                    getTimesheetDropdownApi()
                                }
                            } else {
                                progress_wheel.stopSpinning()
                                getTimesheetDropdownApi()
                            }

                        }, { error ->
                            progress_wheel.stopSpinning()
                            error.printStackTrace()
                            getTimesheetDropdownApi()
                        })
        )
    }

    private fun getTimesheetDropdownApi() {
        var progress_wheel: ProgressWheel?= null
        if (Pref.isAttendanceFeatureOnly)
            progress_wheel = progress_wheel_attendance
        else
            progress_wheel = this.progress_wheel

        progress_wheel?.spin()
        val repository = TimeSheetRepoProvider.timeSheetRepoProvider()
        BaseActivity.compositeDisposable.add(
                repository.getTimeSheetDropdown()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({ result ->
                            val response = result as TimeSheetDropDownResponseModel
                            XLog.d("TIMESHEET DROPDOWN: " + "RESPONSE : " + response.status + "\n" + "Time : " + AppUtils.getCurrentDateTime() + ", USER :" + Pref.user_name + ",MESSAGE : " + response.message)

                            if (response.status == NetworkConstant.SUCCESS) {

                                AppDatabase.getDBInstance()?.clientDao()?.deleteAll()
                                AppDatabase.getDBInstance()?.projectDao()?.deleteAll()
                                AppDatabase.getDBInstance()?.activityDao()?.deleteAll()
                                AppDatabase.getDBInstance()?.productDao()?.deleteAll()


                                doAsync {

                                    response.client_list?.forEach {
                                        val client = ClientListEntity()
                                        AppDatabase.getDBInstance()?.clientDao()?.insertAll(client.apply {
                                            client_id = it.client_id
                                            client_name = it.client_name
                                        })
                                    }

                                    response.project_list?.forEach {
                                        val project = ProjectListEntity()
                                        AppDatabase.getDBInstance()?.projectDao()?.insertAll(project.apply {
                                            project_id = it.project_id
                                            project_name = it.project_name
                                        })
                                    }

                                    response.product_list?.forEach {
                                        val product = TimesheetProductListEntity()
                                        AppDatabase.getDBInstance()?.productDao()?.insertAll(product.apply {
                                            product_id = it.product_id
                                            product_name = it.product_name
                                        })
                                    }

                                    response.activity_list?.forEach {
                                        val activity = ActivityListEntity()
                                        AppDatabase.getDBInstance()?.activityDao()?.insertAll(activity.apply {
                                            activity_id = it.activity_id
                                            activity_name = it.activity_name
                                        })
                                    }

                                    uiThread {
                                        progress_wheel.stopSpinning()
                                        getTimesheetConfig()
                                    }
                                }
                            }
                            else {
                                progress_wheel.stopSpinning()
                                getTimesheetConfig()
                            }

                        }, { error ->
                            progress_wheel.stopSpinning()
                            XLog.d("TIMESHEET DROPDOWN: " + "ERROR : " + "\n" + "Time : " + AppUtils.getCurrentDateTime() + ", USER :" + Pref.user_name + ",MESSAGE : " + error.localizedMessage)
                            error.printStackTrace()
                            getTimesheetConfig()
                        })
        )
    }

    private fun getTimesheetConfig() {
        var progress_wheel: ProgressWheel?= null
        if (Pref.isAttendanceFeatureOnly)
            progress_wheel = progress_wheel_attendance
        else
            progress_wheel = this.progress_wheel

        progress_wheel?.spin()
        val repository = TimeSheetRepoProvider.timeSheetRepoProvider()
        BaseActivity.compositeDisposable.add(
                repository.timeSheetConfig(true)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({ result ->
                            val response = result as TimeSheetConfigResponseModel
                            XLog.d("TIMESHEET CONFIG: " + "RESPONSE : " + response.status + "\n" + "Time : " + AppUtils.getCurrentDateTime() + ", USER :" + Pref.user_name + ",MESSAGE : " + response.message)

                            progress_wheel.stopSpinning()

                            if (response.status == NetworkConstant.SUCCESS) {
                                response.apply {
                                    Pref.timesheet_past_days = timesheet_past_days
                                    Pref.supervisor_name = supervisor_name
                                    Pref.client_text = client_text
                                    Pref.product_text = product_text
                                    Pref.activity_text = activity_text
                                    Pref.project_text = project_text
                                    Pref.time_text = time_text
                                    Pref.comment_text = comment_text
                                    Pref.submit_text = submit_text
                                }
                            }

                            gePrimaryAppListApi()

                        }, { error ->
                            progress_wheel.stopSpinning()
                            XLog.d("TIMESHEET CONFIG: " + "ERROR : " + "\n" + "Time : " + AppUtils.getCurrentDateTime() + ", USER :" + Pref.user_name + ",MESSAGE : " + error.localizedMessage)
                            error.printStackTrace()
                            gePrimaryAppListApi()
                        })
        )
    }

    private fun gePrimaryAppListApi() {
        progress_wheel.spin()
        val repository = ShopListRepositoryProvider.provideShopListRepository()
        BaseActivity.compositeDisposable.add(
                repository.getPrimaryAppList()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({ result ->
                            val response = result as PrimaryAppListResponseModel
                            XLog.d("GET PRIMARY APP DATA : " + "RESPONSE : " + response.status + "\n" + "Time : " + AppUtils.getCurrentDateTime() + ", USER :" + Pref.user_name + ",MESSAGE : " + response.message)
                            if (response.status == NetworkConstant.SUCCESS) {

                                if (response.primary_application_list != null && response.primary_application_list!!.isNotEmpty()) {

                                    AppDatabase.getDBInstance()?.primaryAppListDao()?.deleteAll()

                                    doAsync {

                                        response.primary_application_list?.forEach {
                                            val primaryEntity = PrimaryAppEntity()
                                            AppDatabase.getDBInstance()?.primaryAppListDao()?.insertAll(primaryEntity.apply {
                                                primary_app_id = it.id
                                                primary_app_name = it.name
                                            })
                                        }

                                        uiThread {
                                            progress_wheel.stopSpinning()
                                            geSecondaryAppListApi()
                                        }
                                    }
                                } else {
                                    progress_wheel.stopSpinning()
                                    geSecondaryAppListApi()
                                }


                            } else {
                                progress_wheel.stopSpinning()
                                geSecondaryAppListApi()
                            }

                        }, { error ->
                            progress_wheel.stopSpinning()
                            XLog.d("GET PRIMARY APP DATA : " + "ERROR : " + "\n" + "Time : " + AppUtils.getCurrentDateTime() + ", USER :" + Pref.user_name + ",MESSAGE : " + error.localizedMessage)
                            error.printStackTrace()
                            geSecondaryAppListApi()
                        })
        )
    }

    private fun geSecondaryAppListApi() {
        progress_wheel.spin()
        val repository = ShopListRepositoryProvider.provideShopListRepository()
        BaseActivity.compositeDisposable.add(
                repository.getSecondaryAppList()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({ result ->
                            val response = result as SecondaryAppListResponseModel
                            XLog.d("GET SECONDARY APP DATA : " + "RESPONSE : " + response.status + "\n" + "Time : " + AppUtils.getCurrentDateTime() + ", USER :" + Pref.user_name + ",MESSAGE : " + response.message)
                            if (response.status == NetworkConstant.SUCCESS) {

                                if (response.secondary_application_list != null && response.secondary_application_list!!.isNotEmpty()) {

                                    AppDatabase.getDBInstance()?.secondaryAppListDao()?.deleteAll()

                                    doAsync {

                                        response.secondary_application_list?.forEach {
                                            val secondaryEntity = SecondaryAppEntity()
                                            AppDatabase.getDBInstance()?.secondaryAppListDao()?.insertAll(secondaryEntity.apply {
                                                secondary_app_id = it.id
                                                secondary_app_name = it.name
                                            })
                                        }

                                        uiThread {
                                            progress_wheel.stopSpinning()
                                            geLeadApi()
                                        }
                                    }
                                } else {
                                    progress_wheel.stopSpinning()
                                    geLeadApi()
                                }


                            } else {
                                progress_wheel.stopSpinning()
                                geLeadApi()
                            }

                        }, { error ->
                            progress_wheel.stopSpinning()
                            XLog.d("GET SECONDARY APP DATA : " + "ERROR : " + "\n" + "Time : " + AppUtils.getCurrentDateTime() + ", USER :" + Pref.user_name + ",MESSAGE : " + error.localizedMessage)
                            error.printStackTrace()
                            geLeadApi()
                        })
        )
    }

    private fun geLeadApi() {
        progress_wheel.spin()
        val repository = ShopListRepositoryProvider.provideShopListRepository()
        BaseActivity.compositeDisposable.add(
                repository.getLeadTypeList()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({ result ->
                            val response = result as LeadListResponseModel
                            XLog.d("GET LEAD TYPE DATA : " + "RESPONSE : " + response.status + "\n" + "Time : " + AppUtils.getCurrentDateTime() + ", USER :" + Pref.user_name + ",MESSAGE : " + response.message)
                            if (response.status == NetworkConstant.SUCCESS) {

                                if (response.lead_type_list != null && response.lead_type_list!!.isNotEmpty()) {

                                    AppDatabase.getDBInstance()?.leadTypeDao()?.deleteAll()

                                    doAsync {

                                        response.lead_type_list?.forEach {
                                            val leadEntity = LeadTypeEntity()
                                            AppDatabase.getDBInstance()?.leadTypeDao()?.insertAll(leadEntity.apply {
                                                lead_id = it.id
                                                lead_name = it.name
                                            })
                                        }

                                        uiThread {
                                            progress_wheel.stopSpinning()
                                            geStageApi()
                                        }
                                    }
                                } else {
                                    progress_wheel.stopSpinning()
                                    geStageApi()
                                }


                            } else {
                                progress_wheel.stopSpinning()
                                geStageApi()
                            }

                        }, { error ->
                            progress_wheel.stopSpinning()
                            XLog.d("GET LEAD TYPE DATA : " + "ERROR : " + "\n" + "Time : " + AppUtils.getCurrentDateTime() + ", USER :" + Pref.user_name + ",MESSAGE : " + error.localizedMessage)
                            error.printStackTrace()
                            geStageApi()
                        })
        )
    }

    private fun geStageApi() {
        progress_wheel.spin()
        val repository = ShopListRepositoryProvider.provideShopListRepository()
        BaseActivity.compositeDisposable.add(
                repository.getStagList()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({ result ->
                            val response = result as StageListResponseModel
                            XLog.d("GET STAGE DATA : " + "RESPONSE : " + response.status + "\n" + "Time : " + AppUtils.getCurrentDateTime() + ", USER :" + Pref.user_name + ",MESSAGE : " + response.message)
                            if (response.status == NetworkConstant.SUCCESS) {

                                if (response.stage_list != null && response.stage_list!!.isNotEmpty()) {

                                    AppDatabase.getDBInstance()?.stageDao()?.deleteAll()

                                    doAsync {

                                        response.stage_list?.forEach {
                                            val stageEntity = StageEntity()
                                            AppDatabase.getDBInstance()?.stageDao()?.insertAll(stageEntity.apply {
                                                stage_id = it.id
                                                stage_name = it.name
                                            })
                                        }

                                        uiThread {
                                            progress_wheel.stopSpinning()
                                            geFunnelStageApi()
                                        }
                                    }
                                } else {
                                    progress_wheel.stopSpinning()
                                    geFunnelStageApi()
                                }


                            } else {
                                progress_wheel.stopSpinning()
                                geFunnelStageApi()
                            }

                        }, { error ->
                            progress_wheel.stopSpinning()
                            XLog.d("GET STAGE DATA : " + "ERROR : " + "\n" + "Time : " + AppUtils.getCurrentDateTime() + ", USER :" + Pref.user_name + ",MESSAGE : " + error.localizedMessage)
                            error.printStackTrace()
                            geFunnelStageApi()
                        })
        )
    }

    private fun geFunnelStageApi() {
        progress_wheel.spin()
        val repository = ShopListRepositoryProvider.provideShopListRepository()
        BaseActivity.compositeDisposable.add(
                repository.getFunnelStageList()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({ result ->
                            val response = result as FunnelStageListResponseModel
                            XLog.d("GET FUNNEL STAGE DATA : " + "RESPONSE : " + response.status + "\n" + "Time : " + AppUtils.getCurrentDateTime() + ", USER :" + Pref.user_name + ",MESSAGE : " + response.message)
                            if (response.status == NetworkConstant.SUCCESS) {

                                if (response.funnel_stage_list != null && response.funnel_stage_list!!.isNotEmpty()) {

                                    AppDatabase.getDBInstance()?.funnelStageDao()?.deleteAll()

                                    doAsync {

                                        response.funnel_stage_list?.forEach {
                                            val funnelStageEntity = FunnelStageEntity()
                                            AppDatabase.getDBInstance()?.funnelStageDao()?.insertAll(funnelStageEntity.apply {
                                                funnel_stage_id = it.id
                                                funnel_stage_name = it.name
                                            })
                                        }

                                        uiThread {
                                            progress_wheel.stopSpinning()
                                            getEntityTypeListApi()
                                        }
                                    }
                                } else {
                                    progress_wheel.stopSpinning()
                                    getEntityTypeListApi()
                                }


                            } else {
                                progress_wheel.stopSpinning()
                                getEntityTypeListApi()
                            }

                        }, { error ->
                            progress_wheel.stopSpinning()
                            XLog.d("GET FUNNEL STAGE DATA : " + "ERROR : " + "\n" + "Time : " + AppUtils.getCurrentDateTime() + ", USER :" + Pref.user_name + ",MESSAGE : " + error.localizedMessage)
                            error.printStackTrace()
                            getEntityTypeListApi()
                        })
        )
    }

    private fun getEntityTypeListApi() {
        val repository = TypeListRepoProvider.provideTypeListRepository()
        progress_wheel.spin()
        BaseActivity.compositeDisposable.add(
                repository.entityList()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({ result ->
                            val response = result as EntityResponseModel
                            if (response.status == NetworkConstant.SUCCESS) {
                                val list = response.entity_type

                                if (list != null && list.isNotEmpty()) {
                                    AppDatabase.getDBInstance()?.entityDao()?.delete()
                                    doAsync {
                                        list.forEach {
                                            val entity = EntityTypeEntity()
                                            AppDatabase.getDBInstance()?.entityDao()?.insert(entity.apply {
                                                entity_id = it.id
                                                name = it.name
                                            })
                                        }

                                        uiThread {
                                            progress_wheel.stopSpinning()
                                            getPartyStatusListApi()
                                        }
                                    }
                                } else {
                                    progress_wheel.stopSpinning()
                                    getPartyStatusListApi()
                                }
                            }
                            else {
                                progress_wheel.stopSpinning()
                                getPartyStatusListApi()
                            }

                        }, { error ->
                            progress_wheel.stopSpinning()
                            error.printStackTrace()
                            getPartyStatusListApi()
                        })
        )
    }

    private fun getPartyStatusListApi() {
        val repository = TypeListRepoProvider.provideTypeListRepository()
        progress_wheel.spin()
        BaseActivity.compositeDisposable.add(
                repository.partyStatusList()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({ result ->
                            val response = result as PartyStatusResponseModel
                            if (response.status == NetworkConstant.SUCCESS) {
                                val list = response.party_status
                                if (list != null && list.isNotEmpty()) {
                                    AppDatabase.getDBInstance()?.partyStatusDao()?.delete()
                                    doAsync {
                                        list.forEach {
                                            val party = PartyStatusEntity()
                                            AppDatabase.getDBInstance()?.partyStatusDao()?.insert(party.apply {
                                                party_status_id = it.id
                                                name = it.name
                                            })
                                        }

                                        uiThread {
                                            progress_wheel.stopSpinning()
                                            getMeetingTypeListApi()
                                        }
                                    }
                                } else {
                                    progress_wheel.stopSpinning()
                                    getMeetingTypeListApi()
                                }
                            }
                            else {
                                progress_wheel.stopSpinning()
                                getMeetingTypeListApi()
                            }

                        }, { error ->
                            progress_wheel.stopSpinning()
                            error.printStackTrace()
                            getMeetingTypeListApi()
                        })
        )
    }

    private fun getMeetingTypeListApi() {
        val repository = LoginRepositoryProvider.provideLoginRepository()
        progress_wheel.spin()
        BaseActivity.compositeDisposable.add(
                repository.getMeetingList()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({ result ->
                            val response = result as MeetingListResponseModel
                            BaseActivity.isApiInitiated = false
                            if (response.status == NetworkConstant.SUCCESS) {

                                if (response.meeting_type_list != null && response.meeting_type_list!!.size > 0) {

                                    AppDatabase.getDBInstance()!!.addMeetingTypeDao().deleteAll()

                                    doAsync {
                                        val list = response.meeting_type_list

                                        for (i in list!!.indices) {
                                            val meetingType = MeetingTypeEntity()
                                            meetingType.typeId = list[i].type_id.toInt()
                                            meetingType.typeText = list[i].type_text

                                            AppDatabase.getDBInstance()!!.addMeetingTypeDao().insertAll(meetingType)
                                        }

                                        uiThread {
                                            progress_wheel.stopSpinning()
                                            getRemarksList()
                                        }
                                    }
                                } else {
                                    progress_wheel.stopSpinning()
                                    getRemarksList()
                                }
                            } else {
                                progress_wheel.stopSpinning()
                                getRemarksList()
                            }

                        }, { error ->
                            error.printStackTrace()
                            BaseActivity.isApiInitiated = false
                            progress_wheel.stopSpinning()
                            getRemarksList()
                        })
        )
    }

    private fun getRemarksList() {
        progress_wheel.spin()
        val repository = ShopDurationRepositoryProvider.provideShopDurationRepository()
        BaseActivity.compositeDisposable.add(
                repository.getRemarksList()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({ result ->
                            val response = result as VisitRemarksResponseModel
                            XLog.d("Visit Remarks List : RESPONSE " + response.status)
                            if (response.status == NetworkConstant.SUCCESS) {
                                AppDatabase.getDBInstance()?.visitRemarksDao()?.delete()

                                doAsync {
                                    response.remarks_list?.forEach {
                                        val visitRemarks = VisitRemarksEntity()
                                        AppDatabase.getDBInstance()?.visitRemarksDao()?.insertAll(visitRemarks.apply {
                                            remarks_id = it.id
                                            name = it.name
                                        })
                                    }

                                    uiThread {
                                        progress_wheel.stopSpinning()
                                        changeUI()
                                    }
                                }
                            }
                            else {
                                progress_wheel.stopSpinning()
                                changeUI()
                            }
                        }, { error ->
                            progress_wheel.stopSpinning()
                            error.printStackTrace()
                            XLog.d("Visit Remarks List : ERROR " + error.localizedMessage)
                            changeUI()
                        })
        )
    }

    private fun changeUI() {
        tv_shop.text = Pref.shopText + "(s)"

        if (Pref.willShowUpdateDayPlan)
            tv_view_all.visibility = View.VISIBLE
        else
            tv_view_all.visibility = View.GONE

        val todaysShopVisitCount = InfoWizard.getAvergareShopVisitCount()

        avgShop.text = todaysShopVisitCount
        avgTime.text = InfoWizard.getAverageShopVisitTimeDuration() + " Hrs"

        best_performing_shop_TV.text = getString(R.string.todays_task)

        tv_pick_date_range.text = AppUtils.getFormattedDate(myCalendar.time)

        if (Pref.isAttendanceFeatureOnly) {
            ll_attendance_report_main.visibility = View.VISIBLE
            rl_dashboard_fragment_main.visibility = View.GONE
        } else {
            ll_attendance_report_main.visibility = View.GONE
            rl_dashboard_fragment_main.visibility = View.VISIBLE
        }

        if (!Pref.isMeetingAvailable && !Pref.isShopAddEditAvailable)
            fab.visibility = View.GONE
        else
            fab.visibility = View.VISIBLE

        if (Pref.isServiceFeatureEnable){
            tv_order.text = getString(R.string.myjobs)
            iv_order_icon.setImageResource(R.drawable.ic_activity_white)
            iv_order_icon.visibility = View.VISIBLE
            iv_quot_icon.visibility = View.GONE
            shop_tome_order_tab_LL.visibility = View.GONE
        }
        else if (Pref.willActivityShow) {
            tv_order.text = getString(R.string.activities)
            no_of_order_TV.text = getString(R.string.today_activity)
            avgOrder.text = InfoWizard.getActivityForToday()
            iv_order_icon.setImageResource(R.drawable.ic_activity_white)
            iv_order_icon.visibility = View.VISIBLE
            iv_quot_icon.visibility = View.GONE
            shop_tome_order_tab_LL.visibility = View.VISIBLE
        }
        else {
            shop_tome_order_tab_LL.visibility = View.VISIBLE
            if (Pref.isQuotationShow) {
                tv_order.text = getString(R.string.quotation)
                no_of_order_TV.text = getString(R.string.total_quot)
                avgOrder.text = getString(R.string.rupee_symbol) + InfoWizard.getTotalQuotAmountForToday()
                iv_order_icon.visibility = View.GONE
                iv_quot_icon.visibility = View.VISIBLE
            } else {
                no_of_order_TV.text = getString(R.string.total_order_value_new)
                avgOrder.text = getString(R.string.rupee_symbol) + InfoWizard.getTotalOrderAmountForToday()
                iv_order_icon.visibility = View.VISIBLE
                iv_quot_icon.visibility = View.GONE

                if (Pref.isOrderReplacedWithTeam) {
                    tv_order.text = getString(R.string.team_details)
                    iv_order_icon.setImageResource(R.drawable.ic_team_icon)
                    price_RL.visibility = View.GONE
                } else {
                    tv_order.text = getString(R.string.orders)
                    iv_order_icon.setImageResource(R.drawable.ic_dashboard_order_icon)

                    if (Pref.isOrderShow) {
                        order_ll.visibility = View.VISIBLE
                        price_RL.visibility = View.VISIBLE
                    } else {
                        order_ll.visibility = View.GONE
                        price_RL.visibility = View.GONE
                    }
                }
            }
        }

        if (Pref.isChatBotShow)
            fab_bot.visibility = View.VISIBLE
        else
            fab_bot.visibility = View.GONE

        if (Pref.isShowTimeline)
            history_ll.visibility = View.VISIBLE
        else
            history_ll.visibility = View.GONE

        initAdapter()
        initBottomAdapter()

        (mContext as DashboardActivity).updateUI()
    }
}