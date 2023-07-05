package com.paparazziteam.yakulap.presentation.dashboard.views

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.get
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.google.android.material.textview.MaterialTextView
import com.paparazziteam.yakulap.R
import com.paparazziteam.yakulap.databinding.ActivityDashboardBinding
import com.paparazziteam.yakulap.helper.application.MyPreferences
import com.paparazziteam.yakulap.helper.application.toast
import com.paparazziteam.yakulap.helper.base.BaseActivity
import com.paparazziteam.yakulap.helper.replaceFirstCharInSequenceToUppercase
import com.paparazziteam.yakulap.helper.setColorToStatusBar
import com.paparazziteam.yakulap.presentation.bienvenida.views.WelcomeActivity
import com.paparazziteam.yakulap.presentation.dashboard.fragments.BottomDialogFragment
import com.paparazziteam.yakulap.presentation.dashboard.viewmodels.ViewModelDashboard
import com.paparazziteam.yakulap.presentation.navigation.NavigationRoot
import com.paparazziteam.yakulap.presentation.navigation.NavigationRootImpl
import com.paparazziteam.yakulap.presentation.puntaje.views.PuntajeActivity
import dagger.hilt.android.AndroidEntryPoint
import io.ak1.pix.helpers.PixBus
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class DashboardActivity : BaseActivity<ActivityDashboardBinding>(ActivityDashboardBinding::inflate) {

    private lateinit var appBarConfiguration: AppBarConfiguration

    private lateinit var mainToolbar: Toolbar
    lateinit var drawer:DrawerLayout
    lateinit var navView:NavigationView
    lateinit var headerLayout: View
    lateinit var greetingsNameHeader: MaterialTextView

    @Inject
    lateinit var preferences:MyPreferences

    @Inject
    lateinit var navigationRoot: NavigationRootImpl

    private val _viewModel:ViewModelDashboard by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
                R.id.nav_home,
                R.id.nav_challenge_complete,
            ), drawer
        )
        //Here we bind the navController to the navigationRoot
        navigationRoot.bind(navController)

        setupActionBarWithNavController(navController, appBarConfiguration)

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
            preferences.clearData()
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
                preferences.email = user.email?:""

                //Asignar Nombres y Primera letra del apellido del usuario
                val names = replaceFirstCharInSequenceToUppercase(user.nombres?:"")
                val apellidos = user.apellidos?.substring(0,1)
                greetingsNameHeader.text =  "$names ${apellidos?.uppercase(Locale.getDefault())}."
            }
        }

        _viewModel.snackbar.observe(this){
            if(!it.isNullOrEmpty()){
                Log.d("TAG","New Toast Post Reported")
                this.toast(it)
            }
        }
    }

    private fun saveLogin() {
        preferences.isLogin = true
    }


    private fun setToolbarConfig() {
        mainToolbar.apply {
            title = "Desafíos"
            setTitleTextColor(ContextCompat.getColor(context, R.color.colorWhite))
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

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_dashboard)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onDestroy() {
        _viewModel.getUserData().removeObservers(this)
        _viewModel.snackbar.removeObservers(this)
        super.onDestroy()
    }
}