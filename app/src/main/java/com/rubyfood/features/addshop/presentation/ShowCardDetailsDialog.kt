package com.rubyfood.features.addshop.presentation

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import com.rubyfood.R
import com.rubyfood.app.Pref
import com.rubyfood.app.domain.AssignToPPEntity
import com.rubyfood.app.utils.Toaster
import com.rubyfood.widgets.AppCustomEditText
import com.rubyfood.widgets.AppCustomTextView

class ShowCardDetailsDialog : DialogFragment(), View.OnClickListener {

    private lateinit var mContext: Context

    private lateinit var rv_details_list: RecyclerView
    private lateinit var iv_close_icon: AppCompatImageView
    private lateinit var iv_copy: AppCompatImageView

    private var cardDetails: ArrayList<String>? = null
    private var cardDetailsText = ""

    companion object {
        fun newInstance(cardDetails: ArrayList<String>): ShowCardDetailsDialog {
            val dialogFragment = ShowCardDetailsDialog()

            val bundle = Bundle()
            bundle.putStringArrayList("cardDetails", cardDetails)
            dialogFragment.arguments = bundle

            return dialogFragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        cardDetails = arguments?.getStringArrayList("cardDetails")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)
        dialog.window!!.requestFeature(Window.FEATURE_NO_TITLE)
        dialog.setCanceledOnTouchOutside(true)

        val v = inflater.inflate(R.layout.dialog_card_details, container, false)

        initView(v)
        initClickListener()

        return v
    }

    private fun initView(v: View) {
        iv_copy = v.findViewById(R.id.iv_copy)
        iv_close_icon = v.findViewById(R.id.iv_close_icon)
        rv_details_list = v.findViewById(R.id.rv_details_list)
        rv_details_list.layoutManager = LinearLayoutManager(mContext)
        rv_details_list.adapter = ShowCardAdapter(mContext, cardDetails!!)

        cardDetails?.forEach {
            cardDetailsText += it
        }
    }

    private fun initClickListener() {
        iv_close_icon.setOnClickListener(this)
        iv_copy.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.iv_close_icon -> {
                dismiss()
            }

            R.id.iv_copy -> {
                val clipboard: ClipboardManager = mContext.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip: ClipData = ClipData.newPlainText("Copy All Text", cardDetailsText)
                clipboard.primaryClip = clip

                Toaster.msgShort(mContext, "All Text Copied")
            }
        }
    }
}