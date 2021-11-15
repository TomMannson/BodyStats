package com.tommannson.bodystats.feature.camerascan


import android.annotation.SuppressLint
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.text.FirebaseVisionCloudTextRecognizerOptions
import java.util.*
import java.util.concurrent.Executor
import com.google.android.gms.tasks.OnFailureListener

import com.google.firebase.ml.vision.text.FirebaseVisionText

import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata
import java.lang.IllegalArgumentException


lateinit var imageCapture: ImageCapture
lateinit var executor: Executor
var detector = FirebaseVision.getInstance()
    .onDeviceTextRecognizer

var options = FirebaseVisionCloudTextRecognizerOptions.Builder()
    .setLanguageHints(Arrays.asList("en", "hi"))
    .build()

@Composable
fun SimpleCameraPreview() {
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        AndroidView(
            factory = { ctx ->
                val previewView = PreviewView(ctx)
                executor = ContextCompat.getMainExecutor(ctx)
                imageCapture = ImageCapture.Builder()
                    .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                    .build()
                cameraProviderFuture.addListener({
                    val cameraProvider = cameraProviderFuture.get()
                    val preview = Preview.Builder().build().also {
                        it.setSurfaceProvider(previewView.surfaceProvider)
                    }


                    val cameraSelector = CameraSelector.Builder()
                        .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                        .build()

                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(
                        lifecycleOwner,
                        cameraSelector,
                        imageCapture,
                        preview
                    )
                }, executor)
                previewView
            },
            modifier = Modifier.fillMaxSize(),
        )
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .size(48.dp)
                .padding(15.dp)
                .clip(
                    CircleShape
                )
                .background(MaterialTheme.colors.primary)
                .clickable {

                    imageCapture.takePicture(
                        executor,
                        object : ImageCapture.OnImageCapturedCallback() {
                            override fun onCaptureSuccess(imageProxy: ImageProxy) {
                                val mediaImage = imageProxy.image
                                mediaImage?.let {
                                    val rotation = degreesToFirebaseRotation(imageProxy.imageInfo.rotationDegrees)
                                    val image =
                                        FirebaseVisionImage.fromMediaImage(mediaImage, rotation)

                                    detector
                                        .processImage(image)
                                        .addOnSuccessListener {
                                            it.toString()
                                            imageProxy.close()
                                        }
                                        .addOnFailureListener {
                                            it.toString()
                                            imageProxy.close()
                                        }

                                }

                            }

                            override fun onError(exception: ImageCaptureException) {
                                super.onError(exception)
                            }
                        })
                }
        ) {

        }
    }
}

private fun degreesToFirebaseRotation(degrees: Int): Int {
    return when (degrees) {
        0 -> FirebaseVisionImageMetadata.ROTATION_0
        90 -> FirebaseVisionImageMetadata.ROTATION_90
        180 -> FirebaseVisionImageMetadata.ROTATION_180
        270 -> FirebaseVisionImageMetadata.ROTATION_270
        else -> throw IllegalArgumentException(
            "Rotation must be 0, 90, 180, or 270."
        )
    }
}