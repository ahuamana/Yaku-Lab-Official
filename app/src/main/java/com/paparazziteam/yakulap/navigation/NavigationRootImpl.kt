package com.paparazziteam.yakulap.navigation

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.paparazziteam.yakulap.R
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NavigationRootImpl @Inject constructor() : NavigationRoot {

    private var navController: NavController? = null

    override fun bind(navController: NavController) {
        this.navController = navController
    }

    override fun navigateToHome() {
        navController?.navigate(R.id.nav_home)
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

    override fun navigateToChallengeComplete(bunble: Bundle) {
        navController?.navigate(R.id.action_nav_challenge_to_nav_challenge_complete, bunble)
    }

    override fun navigateToCameraWhatsapp() {
        //TODO: implementar navegacion a la camara de whatsapp
    }

    override fun navigateChallengeCompleteToNavHome() {
        navController?.navigate(R.id.action_nav_challenge_complete_to_nav_home)
    }

    override fun onBack() {
        navController?.popBackStack()
    }


}