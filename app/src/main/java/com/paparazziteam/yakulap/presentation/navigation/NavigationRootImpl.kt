package com.paparazziteam.yakulap.presentation.navigation

import android.os.Bundle
import androidx.navigation.NavController
import com.paparazziteam.yakulap.R
import timber.log.Timber
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

    override fun navigateToChallenge(bunble:Bundle) {
        navController?.navigate(R.id.nav_challenge, bunble)
    }

    override fun navigateToChallengeList(bunble: Bundle) {
        navController?.navigate(R.id.nav_challenge_list, bunble)
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