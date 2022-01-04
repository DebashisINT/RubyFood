package com.rubyfood.features.document.presentation

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rubyfood.R
import com.rubyfood.app.domain.DocumentListEntity
import com.rubyfood.app.utils.AppUtils
import com.rubyfood.features.billing.presentation.BillingDetailsFragment
import kotlinx.android.synthetic.main.inflater_document_item.view.*
import java.io.File

class DocumentAdapter(private val mContext: Context, private val docList:  ArrayList<DocumentListEntity>,
                      private val onEditClick: (DocumentListEntity) -> Unit, private val onDeleteClick: (DocumentListEntity) -> Unit,
                      private val onShareClick: (DocumentListEntity, String) -> Unit, private val onSyncClick: (DocumentListEntity) -> Unit/*,
                      private val onAttchmentClick: (DocumentListEntity, String) -> Unit*/) : RecyclerView.Adapter<DocumentAdapter.MyViewHolder>() {

    private val layoutInflater: LayoutInflater by lazy {
        LayoutInflater.from(mContext)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindItems()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = layoutInflater.inflate(R.layout.inflater_document_item, parent, false)
        return MyViewHolder(v)
    }

    override fun getItemCount(): Int {
        return docList.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems() {

            itemView.apply {

                if (adapterPosition % 2 == 0)
                    setBackgroundColor(mContext.resources.getColor(R.color.report_screen_bg))
                else
                    setBackgroundColor(mContext.resources.getColor(R.color.white))

                if (!TextUtils.isEmpty(docList[adapterPosition].date_time))
                    tv_doc_date_time.text = AppUtils.convertToNotificationDateTime(docList[adapterPosition].date_time!!)
                else
                    tv_doc_date_time.text = "N.A."

                if(!docList[adapterPosition].attachment?.startsWith("http")!!) {
                    val strFileName = File(docList[adapterPosition].attachment!!).name
                    tv_attachment.text = strFileName
                }
                else {
                    val strFileName = docList[adapterPosition].attachment?.substring(docList[adapterPosition].attachment?.lastIndexOf("/")!! + 1)
                    tv_attachment.text = strFileName
                }

                if(docList[adapterPosition].isUploaded)
                    sync_status_iv.setImageResource(R.drawable.ic_registered_shop_sync)
                else {
                    sync_status_iv.setImageResource(R.drawable.ic_registered_shop_not_sync)
                    sync_status_iv.setOnClickListener {
                        onSyncClick(docList[adapterPosition])
                    }
                }

                iv_edit.setOnClickListener {
                    onEditClick(docList[adapterPosition])
                }

                iv_delete.setOnClickListener {
                    onDeleteClick(docList[adapterPosition])
                }

                iv_share.setOnClickListener {
                    onShareClick(docList[adapterPosition], tv_attachment.text.toString().trim())
                }

                /*tv_attachment.setOnClickListener {
                    onAttchmentClick(docList[adapterPosition], tv_attachment.text.toString().trim())
                }*/
            }
        }
    }
}