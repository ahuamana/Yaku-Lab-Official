package com.paparazziteam.yakulap.modulos.laboratorio.views

import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textview.MaterialTextView
import com.paparazziteam.yakulap.R
import com.paparazziteam.yakulap.databinding.ActivityResultCaptureImageBinding
import com.paparazziteam.yakulap.helper.setColorToStatusBar
import com.paparazziteam.yakulap.helper.tintDrawable
import com.paparazziteam.yakulap.modulos.bienvenida.views.WelcomeActivity
import com.paparazziteam.yakulap.modulos.dashboard.views.DashboardActivity
import com.paparazziteam.yakulap.modulos.laboratorio.viewmodels.ViewModelResult
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ResultCaptureImageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultCaptureImageBinding

    private val viewmodel:ViewModelResult by viewModels()

    private lateinit var myToolbar: Toolbar
    private lateinit var txtPoints: MaterialTextView
    private lateinit var txtTittle: MaterialTextView
    private lateinit var txtDescription: MaterialTextView
    private lateinit var imageRounded: ShapeableImageView
    val options: RequestOptions =
        RequestOptions().override(300,200).transform(CenterCrop(), RoundedCorners(10))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultCaptureImageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setColorToStatusBar(this)
        binding.apply {
            //myToolbar       = included.toolbar
            txtPoints       = textPoints
            imageRounded    = roundedImageViewSustantivo
            txtTittle       = title
            txtDescription  = textViewDescriptionSustantivo
        }
        getExtras()
        setupActionBar()
        OtherComponents()
        observers()
    }

    private fun observers() {

    }

    private fun OtherComponents() {
        viewmodel.congratulations.observe(this){
            Log.d("TAG","Congra: $it")
        }
    }

    private fun getExtras() {
        if(intent.extras!= null){
           var title = intent.getStringExtra("title")
           var description = intent.getStringExtra("description")
           var image = intent.getStringExtra("image")
           var pointsToGive = intent.getIntExtra("pointsToGive",0)

            txtPoints.text = pointsToGive.toString()
            txtTittle.text = title
            txtDescription.text = description

            Glide.with(this)
                .load(image)
                .apply(options)
                .into(imageRounded)
        }
    }

    private fun setupActionBar() {
        myToolbar?.apply {
            setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimaryYaku))
            setNavigationOnClickListener { onBackPressed() }
            title = "Premio"
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        myToolbar.inflateMenu(R.menu.menu_item_save)

        setupComponentesMenu()
    }

    private fun setupComponentesMenu() {
        val btnSave   = myToolbar.menu?.findItem(R.id.action_menu_save)?.actionView?.findViewById<AppCompatButton>(R.id.home_boton)
        val colorState = ContextCompat.getColor(this, R.color.colorWhite)

        btnSave?.apply {
            isEnabled = true
            val resouse = ContextCompat.getDrawable(applicationContext, R.drawable.btn_outline_disable) as Drawable
            val customResource = tintDrawable(resouse, colorState)
            background = customResource

            val resouseDrawable = ContextCompat.getDrawable(applicationContext, R.drawable.ic_home) as Drawable
            val customResourceDrawable = tintDrawable(resouseDrawable, colorState)

            setCompoundDrawablesWithIntrinsicBounds(customResourceDrawable, null, null, null)
            setTextColor(colorState)
            setOnClickListener {
                startActivity(Intent(applicationContext, DashboardActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK) //Remove activities that have been created before
                })
            }
        }
    }
}