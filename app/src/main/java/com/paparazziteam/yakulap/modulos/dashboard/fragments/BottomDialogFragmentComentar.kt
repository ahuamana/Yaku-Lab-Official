package com.paparazziteam.yakulap.modulos.dashboard.fragments

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.paparazziteam.yakulap.R
import com.paparazziteam.yakulap.databinding.BottomSheetComentarBinding
import com.paparazziteam.yakulap.helper.application.MyPreferences
import com.paparazziteam.yakulap.helper.beGone
import com.paparazziteam.yakulap.helper.beVisible
import com.paparazziteam.yakulap.helper.onlyOneSpace
import com.paparazziteam.yakulap.modulos.dashboard.adapters.AdapterComment
import com.paparazziteam.yakulap.modulos.dashboard.model.CommentRepository
import com.paparazziteam.yakulap.modulos.dashboard.pojo.Comment
import com.paparazziteam.yakulap.modulos.dashboard.pojo.TypeComment
import com.paparazziteam.yakulap.modulos.dashboard.viewmodels.ViewModelDashboard
import com.paparazziteam.yakulap.modulos.repositorio.CommentProvider
import com.paparazziteam.yakulap.modulos.dashboard.model.CommentRepositoryImpl
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class BottomDialogFragmentComentar : BottomSheetDialogFragment() {

    private val ARG_ID_PHOTO = "idPhoto"
    private var idPhotoReceived:String?= null
    private var _binding: BottomSheetComentarBinding? = null
    private val binding get() = _binding!!

    @Inject lateinit var mCommentRepository:CommentRepository

    private val _viewModel:ViewModelDashboard by activityViewModels()

    var mLinearLayoutManager: LinearLayoutManager? = null
    var recyclerComments: RecyclerView? = null
    var lytWithoutComments: LinearLayout? = null
    var mAdapterComment = AdapterComment()

    //send message
    var editTextMessage: TextInputEditText? = null
    var fabSendMessage: FloatingActionButton? = null
    var contenedorMessages: LinearLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            idPhotoReceived = it.getString(ARG_ID_PHOTO)
        }
    }

    /*override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        var dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        return dialog
    }*/

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding =BottomSheetComentarBinding.inflate(layoutInflater,container, false)
        val view = binding.root

        binding.apply {
            recyclerComments    = recyclerViewComentarios
            lytWithoutComments  = linearLayoutSinComentarios
            editTextMessage     = txtMessage
            fabSendMessage      = fabSend
            contenedorMessages  = contentMensajes
        }

        setupComponentes()
        setupObservers()
        _viewModel.getCommentsFromThread(idPhotoReceived?:"")


        return view
    }

    private fun setupObservers() {
        _viewModel.commentsCompleted.observe(viewLifecycleOwner){
            mAdapterComment.setData(it)
            contenedorMessages?.beVisible()
            recyclerComments?.beVisible()
            lytWithoutComments?.beGone()
        }

        _viewModel.emptyComments.observe(viewLifecycleOwner){
            if(it){
                println("Comentarios vacios")
                recyclerComments?.beGone()
                lytWithoutComments?.beVisible()
            }
        }
    }

    private fun setupComponentes() {
        //SetUp Recycler Comments
        mLinearLayoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
        recyclerComments?.apply {
            layoutManager = mLinearLayoutManager
            adapter = mAdapterComment
        }

        fabSendMessage?.setOnClickListener {
            createNewComment()
        }

        editTextMessage?.apply {
            addTextChangedListener {
                if(editTextMessage?.text?.trim()?.isNotEmpty() == true){
                    fabSendMessage?.apply {
                        isEnabled = true
                        //setBackgroundColor(context.getColor(R.color.colorPrimaryYaku))
                        var color =ContextCompat.getColor(context, R.color.colorPrimaryYaku)
                        backgroundTintList = ColorStateList.valueOf(color)
                    }

                }else{
                    fabSendMessage?.apply {
                        isEnabled = false
                        var color =ContextCompat.getColor(context, R.color.colorGrayLight)
                        backgroundTintList = ColorStateList.valueOf(color)
                        //setBackgroundColor(context.getColor(R.color.colorGrayLight))
                    }
                }
            }
        }
    }

    fun createNewComment(){
        var comment = Comment(
            id_photo = idPhotoReceived,
            status = true,
            email = MyPreferences().email_login,
            type = TypeComment.Comentario.value,
            timestamp = Date().time,
            message = editTextMessage?.text?.trim().toString().onlyOneSpace()
        )

        mCommentRepository.create(comment)?.addOnCompleteListener {
            editTextMessage?.text?.clear()
        }?.addOnFailureListener {

        }

    }

    override fun onDestroyView() {
        _viewModel.emptyComments.removeObservers(this)
        _viewModel.commentsCompleted.removeObservers(this)
        super.onDestroyView()
    }

    companion object {
        @JvmStatic
        fun newInstance(idPhotoSend: String) =
            BottomDialogFragmentComentar().apply {
                arguments = Bundle().apply {
                    putString(ARG_ID_PHOTO,idPhotoSend)
                }
            }
    }
}