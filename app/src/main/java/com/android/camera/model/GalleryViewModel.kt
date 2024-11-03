package com.android.camera.model

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Matrix
import androidx.core.graphics.rotationMatrix
import androidx.lifecycle.ViewModel
import com.android.camera.Utils
import java.io.File

class GalleryViewModel(
    private val outputDirectory: File
): ViewModel() {
    var selectedPicture = mutableStateOf(File(""))

    fun onImageClick(file: File) {
        selectedPicture.value = file
    }

    fun getListPicture(): List<File>{
        return outputDirectory.listFiles()?.toList() ?: emptyList()
    }

    fun onDeleteBtn(){
        selectedPicture.value.delete()
    }

    fun onRotateBtn(){
        selectedPicture.value.rotate()
    }

    fun updateSelectedPicture(){
        selectedPicture.value = File(selectedPicture.value.absolutePath)
    }

    fun onFlipHorizontalBtn(){

    }

    fun onFlipVerticalBtn(){

    }

    private fun File.rotate(){
        Utils.rotate90(this)
    }

}