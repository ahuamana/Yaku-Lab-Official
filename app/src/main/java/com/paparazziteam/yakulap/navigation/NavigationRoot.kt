package com.paparazziteam.yakulap.navigation

import android.os.Bundle
import androidx.navigation.NavController

interface NavigationRoot {
    fun bind(navController: NavController)

    fun navigateToHome()
    fun retrieveNavController(): NavController?
    fun navigateToChallenge(bunble: Bundle)
    fun navigateToChallengeList(bunble: Bundle)
    fun navigateToChallengeDetail()
    fun navigateToChallengeComplete(bunble: Bundle)
    fun navigateToCameraWhatsapp()
    fun navigateChallengeCompleteToNavHome()

    fun navHomeToNavChallengeAR(bunble: Bundle)

    fun onBack()
}