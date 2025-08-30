package com.example.cameraapp.ui.theme

import android.content.Context
import android.util.Size
import android.view.ViewGroup
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

@Composable
fun rememberImageCapture(): ImageCapture = remember {
    ImageCapture.Builder().setTargetResolution(Size(1080,1920)).setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY).build()
}

@Composable
fun CameraPreview(modifier: Modifier = Modifier, imageCapture: ImageCapture): PreviewView {
    val context = LocalContext.current
    val previewView = remember { PreviewView(context).apply { layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT) } }

    LaunchedEffect(Unit) {
        val cameraProvider = context.cameraProvider()
        val preview = androidx.camera.core.Preview.Builder().build().also { it.setSurfaceProvider(previewView.surfaceProvider) }
        try {
            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(
                context as androidx.lifecycle.LifecycleOwner,
                CameraSelector.DEFAULT_BACK_CAMERA,
                preview,
                imageCapture
            )
        } catch (_: Exception) {}
    }

    AndroidView(modifier = modifier, factory = { previewView })
    return previewView
}

private suspend fun Context.cameraProvider(): ProcessCameraProvider =
    suspendCancellableCoroutine { cont ->
        val provider = ProcessCameraProvider.getInstance(this)
        provider.addListener({ cont.resume(provider.get()) }, ContextCompat.getMainExecutor(this))
    }
