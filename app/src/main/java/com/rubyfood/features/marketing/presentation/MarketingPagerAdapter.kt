package com.rubyfood.features.marketing.presentation

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.PagerAdapter
import com.rubyfood.features.marketing.model.MarketingDetailData

/**
 * Created by Pratishruti on 23-02-2018.
 */
class MarketingPagerAdapter(fm: FragmentManager?,retail_branding_list:ArrayList<MarketingDetailData>,pop_material_list:ArrayList<MarketingDetailData>) : FragmentStatePagerAdapter(fm) {

    var retail_branding_list: ArrayList<MarketingDetailData>
    var pop_material_list: ArrayList<MarketingDetailData>

    init {
        this.retail_branding_list = retail_branding_list
        this.pop_material_list = pop_material_list
    }
    override fun getItem(position: Int): Fragment {
     if (position==0){
         return MarketingDetailFragment.newInstance(retail_branding_list)
     }else if(position==1){
         return MarketingDetailFragment.newInstance(pop_material_list)
     }
     else
         return Fragment()
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