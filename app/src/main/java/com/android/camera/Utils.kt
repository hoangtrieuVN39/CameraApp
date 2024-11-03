package com.android.camera

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.Image
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import java.io.File

object Utils {

    fun fileToBitmap(file: File): Bitmap {
        val bitmap = BitmapFactory.decodeFile(file.absolutePath)
        return bitmap
    }

    @Composable
    fun FileToImage(file: File) {
        println(file.absolutePath)
        val bitmap = fileToBitmap(file)
        val painter = remember { BitmapPainter(bitmap.asImageBitmap()) }
        Image(
            modifier = Modifier.fillMaxSize(1F),
            contentDescription = null,
            painter = painter,
            alignment = Alignment.Center,
            contentScale = ContentScale.FillBounds
        )
    }

    fun rotate90(file: File){
        var bitmap = fileToBitmap(file)
        val matrix = Matrix()
        matrix.postRotate(90F)
        bitmap = Bitmap.createBitmap(
            bitmap,
            0,
            0,
            bitmap.width,
            bitmap.height,
            matrix,
            true)
        bitmapToFile(bitmap, file)
    }

    fun bitmapToFile(bitmap: Bitmap, file: File){
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, file.outputStream())
    }
}