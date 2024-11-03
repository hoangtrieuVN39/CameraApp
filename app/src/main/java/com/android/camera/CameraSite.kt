package com.android.camera

import android.graphics.BitmapFactory
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import java.io.File

@Composable
fun CameraSite(
    modifier: Modifier = Modifier,
    onTakePictureBtn: () -> Unit = {},
    onChangeCameraBtn: () -> Unit = {},
    onGalleryBtn: () -> Unit = {},
    cameraPreview: @Composable (modifier: Modifier) -> Unit = {},
    lastPicture: File? = null,
) {
    var show by remember { mutableStateOf(false) }
    var isCapture by remember { mutableStateOf(false) }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.BottomCenter,
    ) {
        cameraPreview(modifier.fillMaxSize())

        LaunchedEffect(key1 = isCapture){
            show = true
            delay(500)
            show = false
        }

        AnimatedVisibility(
            visible = show,
            enter = fadeIn(
                initialAlpha = 0.4f,
                animationSpec = spring(stiffness = Spring.StiffnessMedium)
            ),
            exit = fadeOut(
                targetAlpha = 0.4f,
                animationSpec = spring(stiffness = Spring.StiffnessMedium)
            )
        ) {
            Box(
                modifier = modifier.fillMaxSize().background(Color.White),
            )
        }

        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(
                    top = 16.dp,
                    bottom = 16.dp,
                    start = 32.dp,
                    end = 32.dp
                ),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(
                onClick = onGalleryBtn,
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(Color.White)
            ) {
                if (lastPicture != null) {
                    Utils.FileToImage(lastPicture)
                }
            }

            Spacer(modifier = Modifier.weight(1F))

            Button(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 2.dp,
                    pressedElevation = 8.dp,
                    disabledElevation = 0.dp,
                    hoveredElevation = 4.dp,
                    focusedElevation = 4.dp
                ),
                colors = ButtonDefaults.buttonColors(Color.White),
                onClick = {
                    isCapture = !isCapture
                    onTakePictureBtn()
                }
            ){

            }

            Spacer(modifier = Modifier.weight(1F))

            IconButton(
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(Color.White),
                onClick = onChangeCameraBtn
            ){
                Icon(
                    imageVector = Icons.Filled.Refresh,
                    contentDescription = "Refresh",
                    tint = Color.Black,
                    modifier = Modifier.fillMaxSize(0.8f))
            }
        }

    }

}