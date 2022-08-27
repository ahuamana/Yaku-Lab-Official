package com.paparazziteam.yakulap.modulos.dashboard.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textview.MaterialTextView
import com.paparazziteam.yakulap.R
import com.paparazziteam.yakulap.databinding.ItemComentarioBinding
import com.paparazziteam.yakulap.helper.replaceFirstCharInSequenceToUppercase
import com.paparazziteam.yakulap.modulos.dashboard.pojo.Comment
import com.paparazziteam.yakulap.modulos.login.providers.UserProvider

class AdapterComment : RecyclerView.Adapter<AdapterComment.ViewHolder>(){

    val coments = mutableListOf<Comment>()

    fun setData(newComments:MutableList<Comment>){
        println("NewCommets: ${newComments.count()}")
        coments.clear()
        coments.addAll(newComments)
        notifyDataSetChanged()
    }

    fun addNewComment(comment:Comment){
        coments.add(comment)
        notifyItemInserted(coments.size)
    }

    class ViewHolder(itemview:View):RecyclerView.ViewHolder(itemview){

        val binding = ItemComentarioBinding.bind(itemview)
        var messageText:MaterialTextView?= null
        var txtTitle:MaterialTextView?= null

        fun bind(item:Comment){
            binding.apply {
                messageText     =  txtMessage
                txtTitle        = title
            }

            //Get Info comment User
            getUserInfo(item)
        }

        private fun getUserInfo(item: Comment) {

            messageText?.text = item.message

            //println("Email imprimido ${item.email}")
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
        holder.bind(coments[position])
    }

    override fun getItemCount(): Int  = coments.size
}