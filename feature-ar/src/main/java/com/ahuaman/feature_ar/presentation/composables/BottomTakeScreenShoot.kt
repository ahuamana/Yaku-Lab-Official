package com.ahuaman.feature_ar.presentation.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ahuaman.feature_ar.R
import com.ahuaman.feature_ar.utils.findActivity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomTakeScreenShoot(
    onClickTakeScreenShoot: () -> Unit = {}
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.Bottom
    ) {
        Card(
            modifier = Modifier.padding(16.dp),
            shape = RoundedCornerShape(50.dp),
            onClick = {
                //Close feature AR
                onClickTakeScreenShoot()
            }) {
            Box(
                modifier = Modifier.padding(4.dp),
                contentAlignment = Alignment.Center,
            ) {
                Image(
                    painterResource(id = R.drawable.ic_camera),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(10.dp)
                        .size(34.dp)
                        .clip(RoundedCornerShape(50.dp)))
            }

        }
    }
}

@Preview
@Composable
fun BottomTakeScreenshotPreview() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
        ) {
        BottomTakeScreenShoot()
    }

}