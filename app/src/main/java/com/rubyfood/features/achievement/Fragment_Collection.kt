package com.rubyfood.features.achievement

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.rubyfood.R

class Fragment_Collection : Fragment() {

    private lateinit var rv_order_taken: RecyclerView
    private lateinit var mContext: Context

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_collection, container, false)

        rv_order_taken = view.findViewById<View>(R.id.rv_collection_list) as RecyclerView
        rv_order_taken.layoutManager = LinearLayoutManager(mContext)
        rv_order_taken.addItemDecoration(DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL))

        val adapter = CustomRecyclerViewAdapter(mContext,"collection")
        rv_order_taken.adapter = adapter

        return view
    }
}
