package com.android.camera.model

import androidx.camera.core.CameraSelector
import java.io.File

data class CameraState (
    val latestPicture: File? = null,
    var lensFacing: Int = CameraSelector.LENS_FACING_BACK
)