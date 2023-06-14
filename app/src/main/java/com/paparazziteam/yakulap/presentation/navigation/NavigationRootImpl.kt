package com.paparazziteam.yakulap.presentation.navigation

import androidx.navigation.NavController
import com.paparazziteam.yakulap.R
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NavigationRootImpl @Inject constructor() : NavigationRoot{

    private var navController: NavController? = null

    override fun bind(navController: NavController) {
        this.navController = navController
    }

    override fun retrieveNavController(): NavController? {
        return navController
    }

    override fun navigateToChallenge() {
        navController?.navigate(R.id.nav_challenge)
    }

    override fun navigateToChallengeDetail() {
        //navController?.navigate(R.id.nav_challenge_detail)
    }

    override fun navigateToChallengeComplete() {
        navController?.navigate(R.id.nav_challenge_complete)
    }

    override fun navigateToCameraWhatsapp() {
        navController?.navigate(R.id.nav_camera_whatsapp)
    }

}