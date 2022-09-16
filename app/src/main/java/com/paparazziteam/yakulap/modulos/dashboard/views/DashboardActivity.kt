package com.paparazziteam.yakulap.modulos.dashboard.views

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.google.android.material.textview.MaterialTextView
import com.paparazziteam.yakulap.R
import com.paparazziteam.yakulap.databinding.ActivityDashboardBinding
import com.paparazziteam.yakulap.helper.application.MyPreferences
import com.paparazziteam.yakulap.helper.replaceFirstCharInSequenceToUppercase
import com.paparazziteam.yakulap.helper.setColorToStatusBar
import com.paparazziteam.yakulap.modulos.bienvenida.views.WelcomeActivity
import com.paparazziteam.yakulap.modulos.dashboard.fragments.BottomDialogFragment
import com.paparazziteam.yakulap.modulos.dashboard.viewmodels.ViewModelDashboard
import com.paparazziteam.yakulap.modulos.puntaje.views.PuntajeActivity
import java.util.*

class DashboardActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityDashboardBinding
    private var preferences = MyPreferences()
    private lateinit var mainToolbar: Toolbar
    lateinit var drawer:DrawerLayout
    lateinit var navView:NavigationView
    lateinit var headerLayout: View
    lateinit var greetingsNameHeader: MaterialTextView

    private var _viewModel = ViewModelDashboard.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            mainToolbar = appBarDashboard.toolbar
        }

        setColorToStatusBar(this)

        setSupportActionBar(mainToolbar)
        saveLogin()

        binding.appBarDashboard.fab.setOnClickListener { view ->
            //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show()
            startActivity(Intent(applicationContext, PuntajeActivity::class.java))
        }
        drawer = binding.drawerLayout
        navView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_dashboard)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow
            ), drawer
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        //Change color toolbar
        setToolbarConfig()

        //Observers
        observers()

        //features
        _viewModel.showUserData()

        //setupDrawerItems
        setupDrawerMenuLeft()
    }

    private fun setupDrawerMenuLeft() {
        println("Header setup")
        headerLayout = navView.getHeaderView(0)
        var menuSignOut: ConstraintLayout = headerLayout.findViewById(R.id.item_menu_signout)
        var resourcesHelp: ConstraintLayout = headerLayout.findViewById(R.id.item_menu_recursos_de_ayuda)
        greetingsNameHeader = headerLayout.findViewById(R.id.greetings_name)

        resourcesHelp.setOnClickListener {
            bottomDialogFragment()
        }

        menuSignOut.setOnClickListener {
            preferences.removeLoginData()
            startActivity(Intent(applicationContext, WelcomeActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK) //Remove activities that have been created before
            })
        }

    }

    @Throws (Throwable::class)
    private fun observers() {
        _viewModel.getUserData().observe(this) { user ->
            if (user != null) {
                println("Puntos Guardados: ${user.points}")
                preferences.points = user.points?:0
                preferences.firstName = user.nombres?:""
                preferences.lastName = user.apellidos?:""

                //Asignar Nombres y Primera letra del apellido del usuario
                val names = replaceFirstCharInSequenceToUppercase(user.nombres?:"")
                val apellidos = user.apellidos?.substring(0,1)
                greetingsNameHeader.text =  "$names ${apellidos?.uppercase(Locale.getDefault())}."
            }
        }

    }

    private fun saveLogin() {
        preferences.isLogin = true
    }

    private fun setToolbarConfig() {
        mainToolbar.apply {
            title = "Desafíos"
            setTitleTextColor(getColor(R.color.colorWhite))
            navigationIcon = getDrawable(R.drawable.ic_menu)
        }
    }

    private fun bottomDialogFragment() {
        val bottomSheetDialogFragment = BottomDialogFragment.newInstance()
        bottomSheetDialogFragment.show(supportFragmentManager,"bottomSheetDialogFragment")
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return super.onCreateOptionsMenu(menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        /*menuInflater.inflate(R.menu.dashboard, menu)
        val itemLogout = menu?.findItem(R.id.action_logout)
        val icon = ContextCompat.getDrawable(this, R.drawable.ic_logout)
        //icon?.alpha = 255
        itemLogout?.icon = icon
        itemLogout?.isVisible = true

        val itemChallenge = menu?.findItem(R.id.action_challenges)
        val iconChallenge = ContextCompat.getDrawable(this, R.drawable.ic_target)
        //icon?.alpha = 255
        itemChallenge?.icon = iconChallenge
        itemChallenge?.isVisible = true*/

        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_dashboard)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}