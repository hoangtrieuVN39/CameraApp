package com.android.camera

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.io.File

@Composable
fun EditSite(
    modifier: Modifier = Modifier,
    pictureState: MutableState<File>,
    onFlipVerticalBtn: () -> Unit = {},
    onFlipHorizontalBtn: () -> Unit = {},
    onRotateBtn: () -> Unit = {},
    onDeleteBtn: () -> Unit = {},
) {
    Box(modifier = Modifier.fillMaxSize()){
        Utils.FileToImage(pictureState.value)
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
                onClick = onFlipVerticalBtn,
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(Color.White),
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.baseline_flip_24),
                    contentDescription = "Flip Vertically",
                    tint = Color.Black,
                    modifier = Modifier.fillMaxSize(0.8f).rotate(90f))
            }

            Spacer(modifier = Modifier.weight(1F))

            IconButton(
                onClick = onFlipHorizontalBtn,
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(Color.White)
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.baseline_flip_24),
                    contentDescription = "Flip Horizontally",
                    tint = Color.Black,
                    modifier = Modifier.fillMaxSize(0.8f))
            }

            Spacer(modifier = Modifier.weight(1F))

            IconButton(
                onClick = onRotateBtn,
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(Color.White)
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.baseline_rotate_left_24),
                    contentDescription = "Rotate",
                    tint = Color.Black,
                    modifier = Modifier.fillMaxSize(0.8f))
            }

            Spacer(modifier = Modifier.weight(1F))

            IconButton(
                onClick = onDeleteBtn,
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(Color.White)
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.baseline_delete_24),
                    contentDescription = "Delete",
                    tint = Color.Red,
                    modifier = Modifier.fillMaxSize(0.8f))
            }
        }
    }

}