package com.paparazziteam.yakulap.presentation.dashboard.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textview.MaterialTextView
import com.paparazziteam.yakulab.binding.utils.getActivity
import com.paparazziteam.yakulab.binding.utils.replaceFirstCharInSequenceToUppercase
import com.paparazziteam.yakulab.binding.utils.toJson
import com.paparazziteam.yakulap.R
import com.paparazziteam.yakulap.databinding.ItemComentarioBinding
import com.paparazziteam.yakulap.presentation.dashboard.fragments.BottomDialogFragmentMoreOptionsComment
import com.yakulab.domain.dashboard.Comment
import com.paparazziteam.yakulap.presentation.dashboard.viewmodels.ViewModelDashboard
import com.paparazziteam.yakulap.presentation.login.providers.UserProvider
import javax.inject.Singleton

@Singleton
class AdapterComment(private val viewModel: ViewModelDashboard) : RecyclerView.Adapter<AdapterComment.ViewHolder>(){


    val coments = mutableListOf<Comment>()
    fun setData(newComments:MutableList<Comment>){
        println("NewCommets: ${newComments.count()}")
        coments.clear()
        coments.addAll(newComments)
        notifyDataSetChanged()
    }

    fun addNewComment(comment: Comment){
        coments.add(comment)
        notifyItemInserted(coments.size)
    }

    class ViewHolder(itemview:View):RecyclerView.ViewHolder(itemview){

        val binding = ItemComentarioBinding.bind(itemview)
        var messageText:MaterialTextView?= null
        var txtTitle:MaterialTextView?= null
        var containMensaje:LinearLayout?= null

        fun bind(item: Comment, _viewModel: ViewModelDashboard){
            binding.apply {
                messageText         =  txtMessage
                txtTitle            = title
                containMensaje      = containerMensaje
            }
            //Get Info comment User
            getUserInfo(item)

            //MoreOptionsComment
            moreOptionsComment(item)
        }



        private fun moreOptionsComment(item: Comment) {
            containMensaje?.setOnLongClickListener {
                openDialogMoreOptionsComment(item)
                return@setOnLongClickListener true
            }
        }

        fun openDialogMoreOptionsComment(item: Comment){
            val fragment = BottomDialogFragmentMoreOptionsComment.newInstance(toJson(item) ?: "")
            fragment.show((itemView.context.getActivity() as FragmentActivity).supportFragmentManager,"bottomSheetMoreOptionsComment")
        }

        private fun getUserInfo(item: Comment) {
            messageText?.text = item.message

            UserProvider().searchUserByEmail(item.email).addOnCompleteListener {
                if(it.isSuccessful){
                    val first = it.result.get("nombres").toString()
                    val last = it.result.get("apellidos").toString()
                    val fin = replaceFirstCharInSequenceToUppercase("$first $last")
                    txtTitle?.text = fin
                }else Log.e("ERROR", "Error al traer los datos de Firebase")
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemview = LayoutInflater.from(parent.context).inflate(R.layout.item_comentario, parent, false)
        return  ViewHolder(itemview)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(coments[position], viewModel)
    }

    override fun getItemCount(): Int  = coments.size
}