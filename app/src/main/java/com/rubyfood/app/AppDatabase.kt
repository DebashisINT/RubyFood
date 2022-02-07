package com.rubyfood.app

import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import android.content.Context

import com.rubyfood.app.AppConstant.DBNAME
import com.rubyfood.app.domain.*
import com.rubyfood.features.location.UserLocationDataDao
import com.rubyfood.features.location.UserLocationDataEntity
import com.rubyfood.features.login.UserAttendanceDataDao
import com.rubyfood.features.login.UserLoginDataEntity



/*
 * Copyright (C) 2017 Naresh Gowd Idiga
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

@Database(entities = arrayOf(AddShopDBModelEntity::class, UserLocationDataEntity::class, UserLoginDataEntity::class, ShopActivityEntity::class,
        StateListEntity::class, CityListEntity::class, MarketingDetailEntity::class, MarketingDetailImageEntity::class, MarketingCategoryMasterEntity::class,
        TaListDBModelEntity::class, AssignToPPEntity::class, AssignToDDEntity::class, WorkTypeEntity::class, OrderListEntity::class,
        OrderDetailsListEntity::class, ShopVisitImageModelEntity::class, UpdateStockEntity::class, PerformanceEntity::class,
        GpsStatusEntity::class, CollectionDetailsEntity::class, InaccurateLocationDataEntity::class, LeaveTypeEntity::class, RouteEntity::class,
        ProductListEntity::class, OrderProductListEntity::class, StockListEntity::class, RouteShopListEntity::class, SelectedWorkTypeEntity::class,
        SelectedRouteEntity::class, SelectedRouteShopListEntity::class, OutstandingListEntity::class/*, LocationEntity::class*/,
        IdleLocEntity::class, BillingEntity::class, StockDetailsListEntity::class, StockProductListEntity::class, BillingProductListEntity::class,
        MeetingEntity::class, MeetingTypeEntity::class, ProductRateEntity::class, AreaListEntity::class, PjpListEntity::class,
        ShopTypeEntity::class, ModelEntity::class, PrimaryAppEntity::class, SecondaryAppEntity::class, LeadTypeEntity::class,
        StageEntity::class, FunnelStageEntity::class, BSListEntity::class, QuotationEntity::class, TypeListEntity::class,
        MemberEntity::class, MemberShopEntity::class, TeamAreaEntity::class, TimesheetListEntity::class, ClientListEntity::class,
        ProjectListEntity::class, ActivityListEntity::class, TimesheetProductListEntity::class, ShopVisitAudioEntity::class,
        TaskEntity::class, BatteryNetStatusEntity::class, ActivityDropDownEntity::class, TypeEntity::class,
        PriorityListEntity::class, ActivityEntity::class, AddDoctorProductListEntity::class, AddDoctorEntity::class,
        AddChemistProductListEntity::class, AddChemistEntity::class, DocumentypeEntity::class, DocumentListEntity::class, PaymentModeEntity::class,
        EntityTypeEntity::class, PartyStatusEntity::class, RetailerEntity::class, DealerEntity::class, BeatEntity::class, AssignToShopEntity::class,
        VisitRemarksEntity::class,ShopVisitCompetetorModelEntity::class,
        OrderStatusRemarksModelEntity::class,CurrentStockEntryModelEntity::class,CurrentStockEntryProductModelEntity::class,
           CcompetetorStockEntryModelEntity::class,CompetetorStockEntryProductModelEntity::class,
        ShopTypeStockViewStatus::class,
        NewOrderGenderEntity::class,NewOrderProductEntity::class,NewOrderColorEntity::class,NewOrderSizeEntity::class,NewOrderScrOrderEntity::class,ProspectEntity::class,
        QuestionEntity::class,QuestionSubmitEntity::class,AddShopSecondaryImgEntity::class,ReturnDetailsEntity::class
        ,ReturnProductListEntity::class,UserWiseLeaveListEntity::class),
        version = 3, exportSchema = false)
@TypeConverters(DateConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun addShopEntryDao(): AddShopDao
    abstract fun userLocationDataDao(): UserLocationDataDao
    abstract fun userAttendanceDataDao(): UserAttendanceDataDao
    abstract fun shopActivityDao(): ShopActivityDao
    abstract fun stateDao(): StateListDao
    abstract fun cityDao(): CityListDao
    abstract fun marketingDetailDao(): MarketingDetailDao
    abstract fun marketingDetailImageDao(): MarketingDetailImageDao
    abstract fun marketingCategoryMasterDao(): MarketingCategoryMasterDao

    //New implementation
    abstract fun taListDao(): TaListDao

    abstract fun ppListDao(): AssignToPPDao
    abstract fun ddListDao(): AssignToDDDao
    abstract fun workTypeDao(): WorkTypeDao
    abstract fun orderListDao(): OrderListDao
    abstract fun orderDetailsListDao(): OrderDetailsListDao
    abstract fun shopVisitImageDao(): ShopVisitImageDao
    abstract fun shopVisitCompetetorImageDao(): ShopVisitCompetetorDao
    abstract fun shopVisitOrderStatusRemarksDao(): OrderStatusRemarksDao
    abstract fun shopCurrentStockEntryDao(): CurrentStockEntryDao
    abstract fun shopCurrentStockProductsEntryDao(): CurrentStockEntryProductDao
    abstract fun competetorStockEntryDao(): CompetetorStockEntryDao
    abstract fun competetorStockEntryProductDao(): CompetetorStockEntryProductDao
    abstract fun shopTypeStockViewStatusDao(): ShopTypeStockViewStatusDao
    abstract fun updateStockDao(): UpdateStockDao
    abstract fun performanceDao(): PerformanceDao
    abstract fun gpsStatusDao(): GpsStatusDao
    abstract fun collectionDetailsDao(): CollectionDetailsDao
    abstract fun inaccurateLocDao(): InAccurateLocDataDao
    abstract fun leaveTypeDao(): LeaveTypeDao
    abstract fun routeDao(): RouteDao
    abstract fun productListDao(): ProductListDao
    abstract fun orderProductListDao(): OrderProductListDao
    abstract fun stockListDao(): StockListDao
    abstract fun routeShopListDao(): RouteShopListDao
    abstract fun selectedWorkTypeDao(): SelectedWorkTypeDao
    abstract fun selectedRouteListDao(): SelectedRouteDao
    abstract fun selectedRouteShopListDao(): SelectedRouteShopListDao
    abstract fun updateOutstandingDao(): OutstandingListDao
    //abstract fun locationDao(): LocationDao
    abstract fun idleLocDao(): IdleLocDao

    abstract fun billingDao(): BillingDao
    abstract fun stockDetailsListDao(): StockDetailsListDao
    abstract fun stockProductDao(): StockProductListDao
    abstract fun billProductDao(): BillingProductListDao
    abstract fun addMeetingDao(): MeetingDao
    abstract fun addMeetingTypeDao(): MeetingTypeDao
    abstract fun productRateDao(): ProductRateDao
    abstract fun areaListDao(): AreaListDao
    abstract fun shopTypeDao(): ShopTypeDao
    abstract fun pjpListDao(): PjpListDao
    abstract fun modelListDao(): ModelDao
    abstract fun primaryAppListDao(): PrimaryAppDao
    abstract fun secondaryAppListDao(): SecondaryAppDao
    abstract fun leadTypeDao(): LeadTypeDao
    abstract fun stageDao(): StageDao
    abstract fun funnelStageDao(): FunnelStageDao
    abstract fun bsListDao(): BSListDao
    abstract fun quotDao(): QuotationDao
    abstract fun typeListDao(): TypeListDao
    abstract fun memberDao(): MemberDao
    abstract fun memberShopDao(): MemberShopDao
    abstract fun memberAreaDao(): TeamAreaDao
    abstract fun timesheetDao(): TimesheetListDao
    abstract fun clientDao(): ClientListDao
    abstract fun projectDao(): ProjectListDao
    abstract fun activityDao(): ActivityListDao
    abstract fun productDao(): TimesheetProductListDao
    abstract fun shopVisitAudioDao(): ShopVisitAudioDao
    abstract fun taskDao(): TaskDao
    abstract fun batteryNetDao(): BatteryNetStatusDao

    abstract fun activityDropdownDao(): ActivityDropDownDao
    abstract fun typeDao(): TypeDao
    abstract fun priorityDao(): PriorityDao
    abstract fun activDao(): ActivityDao

    abstract fun addDocProductDao(): AddDoctorProductListDao
    abstract fun addDocDao(): AddDoctorDao
    abstract fun addChemistProductDao(): AddChemistProductListDao
    abstract fun addChemistDao(): AddChemistDao

    abstract fun documentTypeDao(): DocumentypeDao
    abstract fun documentListDao(): DocumentListDao

    abstract fun paymenttDao(): PaymentModeDao

    abstract fun entityDao(): EntityTypeDao
    abstract fun partyStatusDao(): PartyStatusDao
    abstract fun retailerDao(): RetailerDao
    abstract fun dealerDao(): DealerDao
    abstract fun beatDao(): BeatDao
    abstract fun assignToShopDao(): AssignToShopDao

    abstract fun visitRemarksDao(): VisitRemarksDao


    //03-09-2021
    abstract fun newOrderGenderDao(): NewOrderGenderDao
    abstract fun newOrderProductDao(): NewOrderProductDao
    abstract fun newOrderColorDao(): NewOrderColorDao
    abstract fun newOrderSizeDao(): NewOrderSizeDao
    abstract fun newOrderScrOrderDao(): NewOrderScrOrderDao

    abstract fun prosDao(): ProspectDao
    abstract fun questionMasterDao(): QuestionDao
    abstract fun questionSubmitDao():  QuestionSubmitDao
    abstract fun addShopSecondaryImgDao():  AddShopSecondaryImgDao

    abstract fun returnDetailsDao():ReturnDetailsDao
    abstract fun returnProductListDao():ReturnProductListDao

    abstract fun userWiseLeaveListDao(): UserWiseLeaveListDao


    companion object {
        var INSTANCE: AppDatabase? = null

        fun initAppDatabase(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, DBNAME)
                        // allow queries on the main thread.
                        // Don't do this on a real app! See PersistenceBasicSample for an example.
                        .allowMainThreadQueries()
                        .addMigrations(MIGRATION_1_2,MIGRATION_2_3
                        )
//                        .fallbackToDestructiveMigration()
                        .build()
            }
        }

        fun getDBInstance(): AppDatabase? {

            return INSTANCE
        }

        fun destroyInstance() {
            INSTANCE = null
        }

        val MIGRATION_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE shop_current_stock_list (id INTEGER NOT NULL PRIMARY KEY , user_id TEXT, stock_id TEXT , shop_id TEXT, visited_datetime TEXT , visited_date TEXT ," +
                        " total_product_stock_qty TEXT ,isUploaded INTEGER NOT NULL DEFAULT 0 )")

                database.execSQL("CREATE TABLE shop_current_stock_products_list (id INTEGER NOT NULL PRIMARY KEY , user_id TEXT, stock_id TEXT , shop_id TEXT, product_id TEXT , product_stock_qty TEXT ," +
                        " isUploaded INTEGER NOT NULL DEFAULT 0  )")

                database.execSQL("CREATE TABLE shop_competetor_stock_list (id INTEGER NOT NULL PRIMARY KEY , user_id TEXT, competitor_stock_id TEXT , shop_id TEXT, visited_datetime TEXT , visited_date TEXT ," +
                        " total_product_stock_qty TEXT , isUploaded INTEGER NOT NULL DEFAULT 0  )")

                database.execSQL("CREATE TABLE shop_competetor_stock_products_list (id INTEGER NOT NULL PRIMARY KEY , user_id TEXT, competitor_stock_id TEXT , shop_id TEXT , brand_name TEXT, product_name TEXT , " +
                        " qty TEXT , mrp TEXT , isUploaded INTEGER NOT NULL DEFAULT 0  )")


                /* database.execSQL("CREATE TABLE shop_visit_competetor_image (id INTEGER NOT NULL PRIMARY KEY , session_token TEXT, shop_id TEXT , user_id TEXT, shop_image TEXT , " +
                         " visited_date TEXT , mrp TEXT , isUploaded INTEGER NOT NULL DEFAULT 0  )")*/

                database.execSQL("CREATE TABLE shop_order_status_remarks (id INTEGER NOT NULL PRIMARY KEY , shop_id TEXT, user_id TEXT , order_status TEXT, order_remarks TEXT , " +
                        "  isUploaded INTEGER NOT NULL DEFAULT 0 , visited_date_time TEXT, visited_date TEXT , shop_revisit_uniqKey  TEXT ) ")

                database.execSQL("CREATE TABLE shop_visit_competetor_image (id INTEGER NOT NULL PRIMARY KEY , session_token TEXT, shop_id TEXT , user_id TEXT, shop_image TEXT , " +
                        "  isUploaded INTEGER NOT NULL DEFAULT 0 , visited_date TEXT ) ")


                database.execSQL("alter table shop_activity ADD COLUMN shop_revisit_uniqKey TEXT")

                database.execSQL("alter table shop_detail ADD COLUMN shop_image_local_path_competitor TEXT")

                database.execSQL("CREATE TABLE shop_type_stock_view_status (id INTEGER NOT NULL PRIMARY KEY , shoptype_id TEXT, shoptype_name TEXT , CurrentStockEnable INTEGER, CompetitorStockEnable INTEGER ) ")


            }

        }
        val MIGRATION_2_3: Migration = object : Migration(2, 3){
            override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("CREATE TABLE tbl_user_wise_leave_list (id INTEGER NOT NULL PRIMARY KEY , applied_date  TEXT , applied_date_time TEXT," +
            "  from_date TEXT, from_date_modified TEXT, to_date TEXT , leave_type TEXT , approve_status INTEGER, reject_status INTEGER , leave_reason TEXT, " +
            " approval_date_time TEXT , approver_remarks TEXT) ")

                /*update shop details table*/
                database.execSQL("ALTER TABLE shop_detail ADD COLUMN agency_name TEXT")
                database.execSQL("ALTER TABLE shop_detail ADD COLUMN lead_contact_number TEXT")
                database.execSQL("ALTER TABLE shop_detail ADD COLUMN rubylead_image1 TEXT")
                database.execSQL("ALTER TABLE shop_detail ADD COLUMN rubylead_image2 TEXT")
                database.execSQL("ALTER TABLE shop_detail ADD COLUMN project_name TEXT")
                database.execSQL("ALTER TABLE shop_detail ADD COLUMN landline_number TEXT")

                /*update shop activity table*/
                database.execSQL("alter table shop_activity ADD COLUMN updated_by TEXT")
                database.execSQL("alter table shop_activity ADD COLUMN updated_on TEXT")
                database.execSQL("alter table shop_activity ADD COLUMN approximate_1st_billing_value TEXT")
                database.execSQL("alter table shop_activity ADD COLUMN agency_name TEXT")
                database.execSQL("alter table shop_activity ADD COLUMN pros_id TEXT")

                database.execSQL("alter table assignedto_dd ADD COLUMN dd_latitude TEXT  ")
                database.execSQL("alter table assignedto_dd ADD COLUMN dd_longitude TEXT ")

                /*update order_details_list table*/
                database.execSQL("alter table order_details_list ADD COLUMN scheme_amount TEXT")
                database.execSQL("alter table order_details_list ADD COLUMN Hospital TEXT")
                database.execSQL("alter table order_details_list ADD COLUMN Email_Address TEXT")

                /*update collection_list table*/
                database.execSQL("alter table collection_list ADD COLUMN Hospital TEXT")
                database.execSQL("alter table collection_list ADD COLUMN Email_Address TEXT")

                /*update order produect list table*/
                database.execSQL("alter table order_product_list ADD COLUMN scheme_qty TEXT")
                database.execSQL("alter table order_product_list ADD COLUMN scheme_rate TEXT")
                database.execSQL("alter table order_product_list ADD COLUMN total_scheme_price TEXT")
                database.execSQL("alter table order_product_list ADD COLUMN MRP TEXT")

                database.execSQL("alter table document_type ADD COLUMN IsForOrganization INTEGER NOT NULL DEFAULT 0 ")
                database.execSQL("alter table document_type ADD COLUMN IsForOwn INTEGER NOT NULL DEFAULT 0 ")

                database.execSQL("alter table document_list ADD COLUMN document_name TEXT ")

                database.execSQL("CREATE TABLE IF NOT EXISTS `new_order_gender` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `gender_id` INTEGER NOT NULL, `gender` TEXT)");

                database.execSQL("CREATE TABLE new_order_product (id INTEGER NOT NULL PRIMARY KEY , product_id INTEGER , product_name TEXT , product_for_gender TEXT ) ")
                database.execSQL("CREATE TABLE new_order_color (id INTEGER NOT NULL PRIMARY KEY , color_id INTEGER , color_name TEXT , product_id INTEGER ) ")
                database.execSQL("CREATE TABLE new_order_size (id INTEGER NOT NULL PRIMARY KEY , product_id INTEGER , size TEXT  ) ")

                database.execSQL("CREATE TABLE new_order_entry (id INTEGER NOT NULL PRIMARY KEY , order_id TEXT , product_id TEXT ," +
                        " product_name TEXT , gender TEXT , size TEXT , qty TEXT , order_date TEXT , shop_id TEXT , color_id TEXT , color_name TEXT , " +
                        " isUploaded INTEGER NOT NULL DEFAULT 0  ) ")

                /*New create table*/
                database.execSQL("CREATE TABLE prospect_list_master (id INTEGER NOT NULL PRIMARY KEY , pros_id  TEXT , pros_name TEXT ) ")
                database.execSQL("CREATE TABLE question_list_master (id INTEGER NOT NULL PRIMARY KEY , question_id TEXT , question TEXT) ")

                database.execSQL("CREATE TABLE question_list_submit (id INTEGER NOT NULL PRIMARY KEY , shop_id TEXT, question_id TEXT , answer TEXT ,"+
                        " isUploaded INTEGER NOT NULL DEFAULT 0 , isUpdateToUploaded INTEGER NOT NULL DEFAULT 0)")
                database.execSQL("CREATE TABLE tbl_addShop_Secondary_Img (id INTEGER NOT NULL PRIMARY KEY , lead_shop_id TEXT, rubylead_image1 TEXT , rubylead_image2 TEXT ,"+
                        " isUploaded_image1 INTEGER NOT NULL DEFAULT 0 , isUploaded_image2 INTEGER NOT NULL DEFAULT 0)")
                database.execSQL("CREATE TABLE tbl_return_details (id INTEGER NOT NULL PRIMARY KEY , date TEXT, only_date TEXT , amount TEXT ,description TEXT ,"+
                        " isUploaded INTEGER NOT NULL DEFAULT 0 , return_id TEXT , shop_id TEXT ,return_lat TEXT ,return_long TEXT)")
                database.execSQL("CREATE TABLE return_product_list (id INTEGER NOT NULL PRIMARY KEY , product_id TEXT, product_name TEXT , brand_id TEXT ,brand TEXT ,"+
                        "category_id TEXT , category TEXT ,watt_id TEXT ,watt TEXT ,qty TEXT ,rate TEXT ,total_price TEXT ,return_id TEXT ,shop_id TEXT )")
            }
        }

    }


//}


}