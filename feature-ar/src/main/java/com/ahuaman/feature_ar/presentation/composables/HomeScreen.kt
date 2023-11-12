package com.ahuaman.feature_ar.presentation.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ahuaman.feature_ar.utils.findActivity
import com.google.ar.core.Config
import com.google.ar.core.Plane
import com.paparazziteam.yakulap.common.R
import io.github.sceneview.ar.ARScene
import io.github.sceneview.ar.arcore.getUpdatedPlanes
import io.github.sceneview.ar.node.AnchorNode
import io.github.sceneview.math.Position
import io.github.sceneview.node.ModelNode
import io.github.sceneview.rememberEngine
import io.github.sceneview.rememberModelLoader
import io.github.sceneview.rememberNodes
import io.github.sceneview.rememberRenderer
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

@Composable
fun HomeARScreen(
    arModel: String,
    scaleInUnitItem: Float
) {
    Box(modifier = Modifier.fillMaxSize()) {
        ARComposable(
            arModel = arModel,
            scaleInUnitItem = scaleInUnitItem
        )
        TopBackButton()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBackButton(
    onClickBack: () -> Unit = {}
) {
    val context = LocalContext.current
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Card(
            modifier = Modifier.padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            onClick = {
                //Close feature AR
                findActivity(context)?.finish()
            }) {
            Box(
                modifier = Modifier.padding(4.dp),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    Icons.Default.ArrowBack, contentDescription = null
                )
            }

        }

        Card(
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            onClick = {
                //Close feature AR
                findActivity(context)?.finish()
            }) {
            Box(
                modifier = Modifier.padding(4.dp),
                contentAlignment = Alignment.Center,
            ) {
                Image(
                    modifier = Modifier.size(24.dp),
                    painter = painterResource(id = R.drawable.ic_logo_ya), contentDescription = null
                )
            }

        }


    }

}


@Preview
@Composable
fun ARComposablePreview() {
    TopBackButton()
}


@Composable
fun HomeARPreview() {
    HomeARScreen(
        arModel = kModelFile,
        scaleInUnitItem = kScaleInUnit
    )
}


private const val kModelFile = "https://ahuamana.github.io/models-ar/insect/ant.glb"
private const val kScaleInUnit = 0.5f

@Composable
fun ARComposable(
    arModel: String,
    scaleInUnitItem: Float
) {
    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        var isLoading by remember { mutableStateOf(false) }
        var planeRenderer by remember { mutableStateOf(true) }
        val engine = rememberEngine()
        val renderer = rememberRenderer(engine)
        val modelLoader = rememberModelLoader(engine)
        val childNodes = rememberNodes()
        val coroutineExceptionHandler  = CoroutineExceptionHandler { _, throwable ->
            println("CoroutineExceptionHandler got $throwable")
        }
        val coroutineScope = rememberCoroutineScope()

        ARScene(
            lifecycle = LocalLifecycleOwner.current.lifecycle,
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
                                modelLoader.loadModelInstanceAsync(arModel){ modelInstance ->
                                    modelInstance?.let {
                                        addChildNode(
                                            ModelNode(
                                                modelInstance = it,
                                                // Scale to fit in a 0.5 meters cube
                                                scaleToUnits = scaleInUnitItem,
                                                // Bottom origin instead of center so the
                                                // model base is on floor
                                                centerOrigin = Position(y = -0.5f)
                                            ).apply {
                                                isEditable = false
                                            }
                                        )
                                    }
                                }
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