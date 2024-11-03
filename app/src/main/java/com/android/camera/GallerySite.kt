package com.android.camera

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.android.camera.model.GalleryViewModel
import java.io.File


@Composable
fun GallerySite(
    modifier: Modifier = Modifier,
    viewModel: GalleryViewModel,
    onImageClick: (File) -> () -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(5),
        modifier = modifier,
    ) {
        val listPicture = viewModel.getListPicture()
        items(listPicture.size) { i ->
            Image(
                contentDescription = null,
                painter = BitmapPainter(Utils.fileToBitmap(listPicture[i]).asImageBitmap()),
                alignment = Alignment.Center,
                modifier = Modifier.clickable(
                    onClick = onImageClick(listPicture[i]),
                    enabled = true)
                    .clip(shape = CutCornerShape(0.dp))
                    .aspectRatio(1F),
                contentScale = ContentScale.FillBounds
            )
        }
    }

}