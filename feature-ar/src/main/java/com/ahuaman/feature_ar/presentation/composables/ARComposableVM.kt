package com.ahuaman.feature_ar.presentation.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.google.android.filament.Renderer
import com.google.ar.core.Config
import com.google.ar.core.Plane
import com.paparazziteam.yakulap.common.R
import io.github.sceneview.ar.ARScene
import io.github.sceneview.ar.arcore.getUpdatedPlanes
import io.github.sceneview.ar.node.AnchorNode
import io.github.sceneview.loaders.ModelLoader
import io.github.sceneview.math.Position
import io.github.sceneview.model.ModelInstance
import io.github.sceneview.node.ModelNode
import io.github.sceneview.rememberNodes
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

@Composable
fun ARComposableVM(
    scaleInUnitItem: Float,
    arModel:String,
    engine:com.google.android.filament.Engine,
    renderer:Renderer,
    modelLoader: ModelLoader,
    modelInstance: ModelInstance
) {

    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        var isLoading by remember { mutableStateOf(false) }
        var planeRenderer by remember { mutableStateOf(true) }

        val childNodes = rememberNodes()
        val coroutineExceptionHandler  = CoroutineExceptionHandler { _, throwable ->
            println("CoroutineExceptionHandler got $throwable")
        }
        val coroutineScope = rememberCoroutineScope()

        ARScene(
            modifier = Modifier.fillMaxSize(),
            childNodes = childNodes,
            engine = engine,
            renderer = renderer,
            modelLoader = modelLoader,
            planeRenderer = planeRenderer,
            onSessionConfiguration = { session, config ->
                config.depthMode =
                    when (session.isDepthModeSupported(Config.DepthMode.AUTOMATIC)) {
                        true -> Config.DepthMode.AUTOMATIC
                        else -> Config.DepthMode.DISABLED
                    }
                config.instantPlacementMode = Config.InstantPlacementMode.DISABLED
                config.lightEstimationMode =
                    Config.LightEstimationMode.ENVIRONMENTAL_HDR
            },
            onSessionUpdated = { _, frame ->
                if (childNodes.isNotEmpty()) return@ARScene

                frame.getUpdatedPlanes()
                    .firstOrNull { it.type == Plane.Type.HORIZONTAL_UPWARD_FACING }
                    ?.let { plane ->
                        isLoading = true
                        childNodes += AnchorNode(
                            engine = engine,
                            anchor = plane.createAnchor(plane.centerPose)
                        ).apply {
                            isEditable = true
                            coroutineScope.launch(coroutineExceptionHandler) {
                                addChildNode(
                                    ModelNode(
                                        modelInstance = modelInstance,
                                        // Scale to fit in a 0.5 meters cube
                                        scaleToUnits = scaleInUnitItem,
                                        // Bottom origin instead of center so the
                                        // model base is on floor
                                        centerOrigin = Position(y = -0.5f)
                                    ).apply {
                                        isEditable = false
                                    }
                                )


                                planeRenderer = false
                                isLoading = false
                            }
                        }
                    }
            }
        )
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(64.dp)
                    .align(Alignment.Center),
                color = colorResource(id = R.color.colorPrimaryYaku)
            )
        }
    }

}