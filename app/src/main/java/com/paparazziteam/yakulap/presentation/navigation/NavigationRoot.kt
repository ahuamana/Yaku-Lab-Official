package com.paparazziteam.yakulap.presentation.navigation

import android.os.Bundle
import androidx.navigation.NavController

interface NavigationRoot {
    fun bind(navController: NavController)
    fun retrieveNavController(): NavController?
    fun navigateToChallenge(bunble: Bundle)
    fun navigateToChallengeList(bunble: Bundle)
    fun navigateToChallengeDetail()
    fun navigateToChallengeComplete()

    fun navigateToCameraWhatsapp()
}