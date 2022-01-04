package com.rubyfood.features.myorder.presentation

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.rubyfood.R
import com.rubyfood.app.types.FragType
import com.rubyfood.base.presentation.BaseFragment
import com.rubyfood.features.dashboard.presentation.DashboardActivity

/**
 * Created by Pratishruti on 27-10-2017.
 */
class MyOrderListFragment : BaseFragment() {
    private lateinit var mMyOrderListAdapter:MyOrderListAdapter
    private lateinit var myOrderRecyclerView:RecyclerView
    private lateinit var mContext:Context
    private lateinit var layoutManager: RecyclerView.LayoutManager

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext=context
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater!!.inflate(R.layout.fragment_my_order_list, container, false)
        initView(view)
        return view
    }

    private fun initView(view:View) {
        myOrderRecyclerView=view.findViewById(R.id.my_order_list_RCV)
        initAdapter()
    }

    private fun initAdapter() {
        mMyOrderListAdapter = MyOrderListAdapter(this!!.context!!, object : MyOrderListClickListener {
            override fun OnOrderListClick(position: Int) {
                (mContext as DashboardActivity).loadFragment(FragType.OrderDetailFragment,true,"")
            }
        })
        layoutManager = LinearLayoutManager(mContext, LinearLayout.VERTICAL, false)
        myOrderRecyclerView.layoutManager=layoutManager
        myOrderRecyclerView.adapter=mMyOrderListAdapter

    }


}