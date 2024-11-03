package com.android.camera

import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.android.camera.model.CameraManager
import com.android.camera.model.GalleryViewModel
import java.io.File

class MainActivity : AppCompatActivity() {

    private val cameraPermissionRequest = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            setCamera(
                this,
                outputDirectory = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
            )
        } else {
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        when (PackageManager.PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) -> {
                setCamera(
                    this,
                    outputDirectory = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
                )
            }
            else -> {
                cameraPermissionRequest.launch(android.Manifest.permission.CAMERA)
            }
        }
    }

    private fun setCamera(
        context: Context,
        outputDirectory: File
    ){
        val cameraManager = CameraManager(
            context = context,
            outputDirectory = outputDirectory)

        val galleryViewModel = GalleryViewModel(
            outputDirectory = cameraManager.getPhotoDirectory())

        setContent {
            CameraApp(
                modifier = Modifier,
                galleryViewModel = galleryViewModel,
                cameraManager = cameraManager
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar(
    canNavigateBack: Boolean,
    currentScreen: AppScreen,
    navigateUp: () -> Unit, modifier: Modifier = Modifier
) {
    TopAppBar(
        title = {
            Text(
                text = currentScreen.name,
                modifier = Modifier,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            )
        },
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(
                    onClick = navigateUp
                ) {
                    Icon(
                        painter = rememberVectorPainter(
                            image = Icons.AutoMirrored.Outlined.ArrowBack
                        ),
                        contentDescription ="" )
                }
            }
        },
    )
}

enum class AppScreen {
    Camera, Gallery, Edit
}

@Composable
fun CameraApp(
    modifier: Modifier,
    navController: NavHostController = rememberNavController(),
    galleryViewModel: GalleryViewModel,
    cameraManager: CameraManager
) {
    val cameraState = cameraManager.cameraState.collectAsStateWithLifecycle()

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = AppScreen.valueOf(
        backStackEntry?.destination?.route ?: AppScreen.Camera.name)

    Scaffold(
        topBar = {
            TopAppBar(
                currentScreen = currentScreen,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() })}
    ) {
        innerPadding ->

        NavHost(
            navController = navController,
            startDestination = AppScreen.Camera.name,
            modifier = modifier
                .padding(innerPadding)
                .fillMaxHeight(),
            contentAlignment = Alignment.CenterStart,
        ){
            composable(route = AppScreen.Camera.name) {
                NavigationBar {

                }
                CameraSite(
                    modifier = modifier,
                    lastPicture = cameraState.value.latestPicture,
                    onTakePictureBtn = {
                        cameraManager.takePhoto()
                    },
                    onChangeCameraBtn = {
                        cameraManager.changeLens()
                    },
                    onGalleryBtn = {
                        navController.navigate(AppScreen.Gallery.name)
                    },
                    cameraPreview = {
                        cameraManager.CameraPreview(
                            modifier = modifier,
                            lensFacing = cameraState.value.lensFacing)
                    })
            }
            composable(route = AppScreen.Gallery.name) {
                GallerySite(
                    modifier = modifier,
                    viewModel = galleryViewModel,
                    onImageClick = { i -> {
                        galleryViewModel.onImageClick(i)
                        navController.navigate(AppScreen.Edit.name)
                    }
                    }
                )
            }
            composable(route = AppScreen.Edit.name) {
                EditSite(
                    modifier = modifier,
                    pictureState = galleryViewModel.selectedPicture,
                    onFlipVerticalBtn = {
                        galleryViewModel.onFlipVerticalBtn()
                    },
                    onFlipHorizontalBtn = {
                        galleryViewModel.onFlipHorizontalBtn()
                    },
                    onRotateBtn = {
                        galleryViewModel.onRotateBtn()},
                    onDeleteBtn = {
                        galleryViewModel.onDeleteBtn()
                        navController.navigate(AppScreen.Gallery.name)
                    }
                )
            }
        }
    }

}
