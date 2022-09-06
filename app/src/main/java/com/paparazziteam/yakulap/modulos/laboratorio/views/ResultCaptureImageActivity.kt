package com.paparazziteam.yakulap.modulos.laboratorio.views

import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.paparazziteam.yakulap.R
import com.paparazziteam.yakulap.databinding.ActivityResultCaptureImageBinding
import com.paparazziteam.yakulap.helper.setColorToStatusBar
import com.paparazziteam.yakulap.helper.tintDrawable
import com.paparazziteam.yakulap.modulos.bienvenida.views.WelcomeActivity
import com.paparazziteam.yakulap.modulos.dashboard.views.DashboardActivity

class ResultCaptureImageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultCaptureImageBinding

    private lateinit var myToolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultCaptureImageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setColorToStatusBar(this)

        binding.apply {
            myToolbar   = included.toolbar
        }

        setupActionBar()
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