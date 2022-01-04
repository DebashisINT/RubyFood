package com.rubyfood.features.dashboard.presentation

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.support.design.widget.TextInputLayout
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentManager
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import com.rubyfood.R
import com.rubyfood.app.AppDatabase
import com.rubyfood.app.Pref
import com.rubyfood.app.utils.AppUtils
import com.rubyfood.app.utils.Toaster
import com.rubyfood.widgets.AppCustomEditText
import com.rubyfood.widgets.AppCustomTextView

/**
 * Created by Saikat on 26-11-2018.
 */
class CustomProgressDialog : DialogFragment() {

    private lateinit var mContext: Context

    companion object {

        fun getInstance(): CustomProgressDialog {
            return CustomProgressDialog()
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        dialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
        //dialog.window?.setBackgroundDrawableResource(R.drawable.rounded_corner_white_bg)
        val v = inflater.inflate(R.layout.custom_progress_dialog, container, false)

        isCancelable = false
        return v
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun show(manager: FragmentManager?, tag: String?) {
        //super.show(manager, tag)
        val ft = manager?.beginTransaction()
        ft?.add(this, tag)
        try {
            ft?.commit()
        } catch (e: IllegalStateException) {
            e.printStackTrace()
            ft?.commitAllowingStateLoss()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}