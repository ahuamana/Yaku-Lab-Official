package com.paparazziteam.yakulap.presentation.navigation

import androidx.navigation.NavController

interface NavigationRoot {
    fun bind(navController: NavController)
    fun retrieveNavController(): NavController?
    fun navigateToChallenge()
    fun navigateToChallengeDetail()
    fun navigateToChallengeComplete()

    fun navigateToCameraWhatsapp()
}