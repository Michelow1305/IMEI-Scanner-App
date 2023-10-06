package com.hkr.mockupforproject.ui.screens

import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner


/**
 * CameraWithBoundedBox creates a bounded box on top of the CameraPreview, and this bounded box
 * has x height and y top padding, which are sent to the analyzer.
 * @param modifier to change the layout parameters of the bounded box.
 * @param context to bind the CameraX lifecycle to activity's lifecycle.
 */
@Composable
fun CameraWithBoundedBox(modifier: Modifier = Modifier, context: Context) {
    val boundedBoxParams: Pair<Int, Int> = 70 to 100

    Box(
        modifier = modifier
            .fillMaxSize()
    ) {

        CameraPreview(
            modifier = modifier,
            context = context,
            boundedBoxParams = boundedBoxParams
        ) { imeis ->
            imeis.forEach { i ->
                Toast.makeText(context, i.toString(), Toast.LENGTH_LONG).show()
            }
        }

        // This is the bounded box
        Box(
            modifier = modifier
                .fillMaxWidth()
                .padding(top = boundedBoxParams.first.dp, start = 10.dp, end = 10.dp)
                .height(boundedBoxParams.second.dp)
                .border(2.dp, Color.White, shape = RoundedCornerShape(7.dp))
        )

    }
}


/**
 * @param modifier to change the layout parameters of the AndroidView.
 * @param context to bind the CameraX lifecycle to activity's lifecycle.
 * @param boundedBoxParams it's pair the specifies the height of the bounded box and how far away it is from the top (top padding).
 *                         The first element is the top padding, and the second element is the height.
 * @param onImeiCodesDetected is a function that can be called when IMEI codes are detected.
 * @throws SecurityException if camera permission is not granted.
 * @sample CameraPreview
 * (
 *      modifier = modifier,
 *      context = context,
 *      boundedBoxParams = boundedBoxParams
 * ) { imeis ->
 *      imeis.forEach { i ->
 *          Toast.makeText(context, i.toString(), Toast.LENGTH_LONG).show()
 *      }
 * }
 *
 */
@Composable
fun CameraPreview(
    modifier: Modifier = Modifier,
    context: Context,
    boundedBoxParams: Pair<Int, Int>,
    onImeiCodesDetected: (imeis: List<Long>) -> Unit
) {
    val tag = "cameraPreview"

    if (ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.CAMERA
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        throw SecurityException("Camera permission not granted")
    }

    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
    val imageAnalysis = remember {
        ImageAnalysis.Builder()
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()
//                .also {
//                    it.setAnalyzer(
//                        ContextCompat.getMainExecutor(context),
//                        ImeiCodeAnalyzer(onImeiCodesDetected, boundedBoxParams)
//                    )
//                }
    }

    val preview = remember {
        Preview.Builder()
            .build()
    }

    AndroidView(
        modifier = modifier
            .fillMaxSize(),
        factory = { ctx ->
            PreviewView(ctx).apply {
                implementationMode = PreviewView.ImplementationMode.COMPATIBLE
            }
        },
        update = {
            val cameraProvider = cameraProviderFuture.get()
            val cameraSelector = CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build()

            try {
                cameraProvider.unbindAll()

                cameraProvider.bindToLifecycle(
                    context as LifecycleOwner,
                    cameraSelector,
                    imageAnalysis,
                    preview
                )

                preview.setSurfaceProvider(it.surfaceProvider)

            } catch (e: Exception) {
                e.printStackTrace()
                Log.e(tag, "Use case binding failed")

            }
        }
    )
}