package com.android.camera.model

import android.annotation.SuppressLint
import android.content.Context
import android.os.Environment
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.core.Preview.Builder
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class CameraManager(context: Context, outputDirectory: File): ViewModel() {
    private val _cameraState = MutableStateFlow(CameraState())
    var cameraState: StateFlow<CameraState> = _cameraState.asStateFlow()

    private var cameraxSelector = CameraSelector.Builder().requireLensFacing(_cameraState.value.lensFacing).build()

    private lateinit var preview: Preview
    private var imageCapture: ImageCapture = ImageCapture.Builder().build()

    private var cameraExecutor: ExecutorService = Executors.newSingleThreadExecutor()
    private val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

    private lateinit var lifecycleOwner: LifecycleOwner
    private lateinit var context: Context
    private lateinit var previewView: PreviewView

    private lateinit var cameraProvider: ProcessCameraProvider
    private val outputDirectory = outputDirectory

    init {
        val latestPicture: File?
        if (sharedPreferences.contains("latestPicture")){
            latestPicture = File(sharedPreferences.getString("latestPicture", "")!!)
        } else {
            sharedPreferences.edit().putString("latestPicture", null).apply()
            latestPicture = null
        }
        _cameraState.value = _cameraState.value.copy(
            latestPicture = latestPicture)
    }

    fun updateLatestPicture(file: File) {
        val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("latestPicture", file.absolutePath)
        editor.apply()
    }

    fun getLensFacing(): Int {
        return _cameraState.value.lensFacing
    }

    fun getCameraState(): CameraState {
        return _cameraState.value
    }

    fun getPhotoDirectory(): File {
        return outputDirectory
    }

    fun changeLens() {
        val lensFacing = getLensFacing()
        _cameraState.value = _cameraState.value.copy(
            lensFacing = if (lensFacing == CameraSelector.LENS_FACING_BACK)
                CameraSelector.LENS_FACING_FRONT
            else CameraSelector.LENS_FACING_BACK)
        cameraxSelector = CameraSelector.Builder().requireLensFacing(lensFacing).build()
    }

    @Composable
    fun CameraPreview(
        modifier: Modifier = Modifier,
        lensFacing: Int
        ) {
        lifecycleOwner = LocalLifecycleOwner.current
        context = LocalContext.current
        previewView = remember { PreviewView(context) }

        LaunchedEffect(cameraxSelector) {
            try {
                bindPreview(previewView, lifecycleOwner, context, lensFacing)
            } catch (e: Exception) {
                println(e.printStackTrace())
            }
        }

        AndroidView({ previewView }, modifier = modifier.fillMaxSize())
    }

    private suspend fun bindPreview(
        previewView: PreviewView,
        lifecycleOwner: LifecycleOwner,
        context: Context,
        lensFacing: Int
        )
    {
        preview = Builder().build()
        cameraxSelector = CameraSelector.Builder().requireLensFacing(lensFacing).build()

        cameraProvider  = context.getCameraProvider()
        cameraProvider.unbindAll()
        try{
            cameraProvider.bindToLifecycle(
                lifecycleOwner,
                cameraxSelector,
                preview,
                imageCapture
            )
            preview.setSurfaceProvider(previewView.surfaceProvider)
        }
        catch (e: Exception){
            e.printStackTrace()
        }
    }

    private suspend fun Context.getCameraProvider(): ProcessCameraProvider =
        suspendCoroutine { continuation ->
            ProcessCameraProvider.getInstance(this).also { cameraProvider ->
                cameraProvider.addListener({
                    continuation.resume(cameraProvider.get())
                }, ContextCompat.getMainExecutor(this))
            }
        }

    fun takePhoto() {
        val photoFile = File(
            outputDirectory,
            SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss",
                Locale.US
            ).format(System.currentTimeMillis()) + ".jpg"
        )

        if (photoFile.exists()) {
            return
        }

        val outputFileOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture.takePicture(
            outputFileOptions, cameraExecutor,
            object : ImageCapture.OnImageSavedCallback {
                @SuppressLint("RestrictedApi")
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    updateLatestPicture(photoFile)
                }
                override fun onError(exception: ImageCaptureException) {
                    exception.printStackTrace()
                }
            }
        )
    }

}