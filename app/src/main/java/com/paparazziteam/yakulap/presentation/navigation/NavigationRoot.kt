package com.paparazziteam.yakulap.presentation.navigation

import androidx.navigation.NavController

interface NavigationRoot {
    fun bind(navController: NavController)
    fun navigateToChallenge()
    fun navigateToChallengeDetail()
    fun navigateToChallengeComplete()
}