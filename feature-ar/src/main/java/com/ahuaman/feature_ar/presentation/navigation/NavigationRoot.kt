package com.ahuaman.feature_ar.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ahuaman.feature_ar.presentation.HomeARScreen
import com.ahuaman.feature_ar.presentation.AR.ARScreenViewModel
import com.ahuaman.feature_ar.presentation.AR.StateARScreen
import timber.log.Timber

@Composable
fun NavigationRoot(
    modifier : Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    arModel: String = "",
    scaleInUnitItem: Float = 0.5f
) {
    NavHost(
        navController = navController,
        route = Graph.ROOT,
        startDestination = Graph.AR,
        modifier = modifier
    ) {
        composable(Graph.AR) {

            val viewModel = hiltViewModel<ARScreenViewModel>()
            val model by viewModel.model.collectAsStateWithLifecycle()

            when(model){
                is StateARScreen.Loading -> {

                }

                StateARScreen.Error -> {
                    Timber.d("Error")
                }
                is StateARScreen.Success -> {
                    HomeARScreen(
                        arModel = arModel,
                        scaleInUnitItem = scaleInUnitItem
                    )
                }
            }

        }

    }

}


object Graph {
    const val ROOT = "root_graph"
    const val HOME = "home_graph"
    const val SPLASH = "splash_graph"
    const val AR = "ar_graph"
}