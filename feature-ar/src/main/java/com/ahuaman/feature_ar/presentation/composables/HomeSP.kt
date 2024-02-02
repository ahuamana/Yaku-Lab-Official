package com.ahuaman.feature_ar.presentation.composables

import android.net.wifi.hotspot2.pps.HomeSp
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ahuaman.feature_ar.presentation.AR.ARScreenViewModel
import com.ahuaman.feature_ar.presentation.AR.StateARScreen
import io.github.sceneview.rememberEngine
import io.github.sceneview.rememberModelLoader
import io.github.sceneview.rememberRenderer
import timber.log.Timber

@Composable
fun HomeSp(
    viewModel:ARScreenViewModel,
    arModel:String,
    scaleInUnitItem:Float,
) {
    val model by viewModel.model.collectAsStateWithLifecycle()

    val engine = rememberEngine()
    val renderer = rememberRenderer(engine)
    val modelLoader = rememberModelLoader(engine)

    LaunchedEffect(key1 = true){
        viewModel.getModel(modelLoader = modelLoader, arModel)
    }

    when{
        model.isLoading -> {
            Timber.d("STATE AR - isLoading - ${model.isLoading}")
            Column(modifier = Modifier.fillMaxSize()) {
                Text(text = "Loading...")
            }
        }
        model.isError -> {
            Timber.d("STATE AR - isError - ${model.isError}")
        }

        model.isSuccess.isNotEmpty() -> {
            Timber.d("STATE AR - isSuccess")

            model.isSuccess.forEachIndexed{ index, item ->
                when(item){
                    is StateARScreen.SuccessArray -> {

                    }
                    is StateARScreen.SuccessModel ->{
                        item.modelInstance?.let {
                            /*ARComposableVM(
                                arModel = arModel,
                                scaleInUnitItem = scaleInUnitItem,
                                engine = engine,
                                modelLoader = modelLoader,
                                renderer = renderer,
                                modelInstance = it
                            )*/

                            HomeARScreen(arModel = arModel, scaleInUnitItem = scaleInUnitItem)
                            TopBackButton()
                        }

                    }
                }
            }
        }

    }
}