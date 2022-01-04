package com.rubyfood.features.shopdetail.presentation


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rubyfood.R
import com.rubyfood.base.presentation.BaseFragment



/**
 * A simple [Fragment] subclass.
 *
 */
class ShopDetailsOrderFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.fragment_shop_order, container, false)

        initView(view)
        return view
        }

    private fun initView(view: View) {

    }


}
