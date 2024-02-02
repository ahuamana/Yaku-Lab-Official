package com.ahuaman.feature_ar.utils.bitmap

import android.content.Context
import android.graphics.Bitmap
import android.view.View
import androidx.activity.ComponentActivity
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent


object ComposeScreenshotUtil {

    @Composable
    fun captureScreenshot(
        activity: ComponentActivity,
        content: @Composable (Modifier) -> Unit,
        callback: (Bitmap) -> Unit
    ) {
        val context = activity.applicationContext
        val rootView = activity.window.decorView.rootView
        val scale = context.resources.displayMetrics.density

        val bitmap = drawToBitmap(context, rootView, content, scale)

        callback(bitmap)
    }

    @Composable
    private fun drawToBitmap(
        context: Context,
        rootView: View,
        content: @Composable (Modifier) -> Unit,
        scale: Float
    ): Bitmap {
        var bitmap: Bitmap? = null

        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .drawWithContent {
                    bitmap = Bitmap.createBitmap(
                        size.width.toInt(),
                        size.height.toInt(),
                        Bitmap.Config.ARGB_8888
                    )
                    bitmap?.let {
                        val canvas = android.graphics.Canvas(it)
                        canvas.scale(scale, scale)
                        rootView.draw(canvas)
                    }
                }
        ) {}

        return bitmap ?: Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
    }
}