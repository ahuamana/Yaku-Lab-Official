package com.paparazziteam.yakulap.presentation.dashboard.views

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationBarView
import com.google.android.material.textview.MaterialTextView
import com.paparazziteam.yakulab.binding.helper.application.MyPreferences
import com.paparazziteam.yakulab.binding.helper.base.BaseActivity
import com.paparazziteam.yakulab.binding.helper.replaceFirstCharInSequenceToUppercase
import com.paparazziteam.yakulab.binding.helper.setColorToStatusBar
import com.paparazziteam.yakulab.binding.helper.toast
import com.paparazziteam.yakulap.R
import com.paparazziteam.yakulap.databinding.ActivityDashboardBinding
import com.paparazziteam.yakulap.presentation.dashboard.fragments.BottomDialogFragment
import com.paparazziteam.yakulap.presentation.dashboard.viewmodels.ViewModelDashboard
import com.paparazziteam.yakulap.navigation.NavigationRootImpl
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class DashboardActivity : BaseActivity<ActivityDashboardBinding>(ActivityDashboardBinding::inflate) {

    private lateinit var appBarConfiguration: AppBarConfiguration

    lateinit var greetingsNameHeader: MaterialTextView

    @Inject
    lateinit var preferences: MyPreferences

    @Inject
    lateinit var navigationRoot: NavigationRootImpl

    private val viewModel: ViewModelDashboard by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setColorToStatusBar(this)
        saveLogin()

        //Setup BottomNavigation
        setupBottomNavigation()

        //Observers
        observers()
    }

    private fun setupBottomNavigation() {
        val navHostController = supportFragmentManager.findFragmentById(R.id.nav_host_dashboard_fragment) as NavHostFragment

        binding.bottomNavView.setupWithNavController(navHostController.navController)
    }


    //TODO: IMPLEMENTAR PROFILE
    /*private fun setupDrawerMenuLeft() {
        println("Header setup")
        headerLayout = navView.getHeaderView(0)
        val menuSignOut: ConstraintLayout = headerLayout.findViewById(R.id.item_menu_signout)
        val resourcesHelp: ConstraintLayout =
            headerLayout.findViewById(R.id.item_menu_recursos_de_ayuda)
        val challengesHome = headerLayout.findViewById<ConstraintLayout>(R.id.item_menu_home)

        greetingsNameHeader = headerLayout.findViewById(R.id.greetings_name)

        challengesHome.setOnClickListener {
            drawer.closeDrawers()
            navigationRoot.navigateToHome()
        }

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

    }*/


    private fun observers() {
        lifecycleScope.launch {
            viewModel.snackbar.observe(this@DashboardActivity) {
                if (!it.isNullOrEmpty()) {
                    Timber.d("New Toast Post Reported")
                    toast(it)
                }
            }
        }

        lifecycleScope.launch {
            viewModel.getUserData().observe(this@DashboardActivity) { user ->
                if (user != null) {
                    Timber.d("Puntos Guardados: ${user.points}")
                    preferences.points = user.points ?: 0
                    preferences.firstName = user.nombres ?: ""
                    preferences.lastName = user.apellidos ?: ""
                    preferences.email = user.email ?: ""

                    //Asignar Nombres y Primera letra del apellido del usuario
                    val names = replaceFirstCharInSequenceToUppercase(user.nombres ?: "")
                    val apellidos = user.apellidos?.substring(0, 1)
                    greetingsNameHeader.text = "$names ${apellidos?.uppercase(Locale.getDefault())}."
                }
            }
        }

    }

    private fun saveLogin() {
        preferences.isLogin = true
    }

    private fun bottomDialogFragment() {
        val bottomSheetDialogFragment = BottomDialogFragment.newInstance()
        bottomSheetDialogFragment.show(supportFragmentManager, "bottomSheetDialogFragment")
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return super.onCreateOptionsMenu(menu)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_dashboard)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

}