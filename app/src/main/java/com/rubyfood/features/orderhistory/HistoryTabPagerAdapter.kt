package com.rubyfood.features.orderhistory

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.PagerAdapter
import com.rubyfood.features.orderhistory.model.ActionFeed

/**
 * Created by Pratishruti on 01-11-2017.
 */
class HistoryTabPagerAdapter(fm: FragmentManager?) : FragmentStatePagerAdapter(fm), ActionFeed {

    override fun refresh() {
        notifyDataSetChanged()
    }


    override fun getItem(position: Int): Fragment {
        if (position == 0) {
            return DayWiseFragment()
        } else if (position == 1) {
            return ConsolidatedFragment()
        } else {
            return Fragment()
        }
    }

    override fun getCount(): Int {
        return 2
    }

    override fun getItemPosition(`object`: Any): Int {
        // Causes adapter to reload all Fragments when
        // notifyDataSetChanged is called
        return PagerAdapter.POSITION_NONE
    }
}