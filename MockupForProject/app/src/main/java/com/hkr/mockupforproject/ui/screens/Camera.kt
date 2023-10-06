package com.hkr.mockupforproject.ui.screens

import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import com.hkr.mockupforproject.ui.AppViewModel


/**
 * CameraWithBoundedBox creates a bounded box on top of the CameraPreview, and this bounded box
 * has x height and y top padding, which are sent to the analyzer.
 * Then the scanned IMEI's are presented in a LazyColumn.
 * @param modifier to change the layout parameters of the bounded box.
 * @param context to bind the CameraX lifecycle to activity's lifecycle.
 * @param appViewModel to add the scanned IMEI's to a MutableLiveData list in the view model.
 * @param mainNavController to navigate to another screen when clicking the FAB.
 */
@Composable
fun CameraWithBoundedBox(
    modifier: Modifier = Modifier,
    context: Context,
    appViewModel: AppViewModel,
    mainNavController: NavController
) {
    /*
        70 = top padding, 100 = height.
     */
    val boundedBoxParams: Pair<Int, Int> = 70 to 100
    val imeis by appViewModel.scannedImeis.observeAsState(emptyList())

    Box(
        modifier = modifier
            .fillMaxSize()
    ) {

        CameraPreview(
            modifier = modifier,
            context = context,
            boundedBoxParams = boundedBoxParams
        ) { imeis ->
            appViewModel.addScannedImeis(imeis)

        }

        // This is the bounded box
        Box(
            modifier = modifier
                .fillMaxWidth()
                .padding(top = boundedBoxParams.first.dp, start = 10.dp, end = 10.dp)
                .height(boundedBoxParams.second.dp)
                .dashedBorder(2.dp, 7.dp, Color.White)
        )


        Column(
            modifier = modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(15.dp)
        ) {

            LazyColumn(
                modifier = modifier
                    .height(350.dp)
                    .align(Alignment.CenterHorizontally),
                reverseLayout = true
            ) {
                items(imeis) { imei ->
                    Imei(imei = imei)
                }
            }

            /*
                Replace this with Per's boss desired button :)
             */
            ExtendedFloatingActionButton(
                modifier = modifier
                    .align(Alignment.CenterHorizontally),
                onClick = {
                    /*
                        Continue to some place when done scanning imei(s) using mainNavController
                     */

                    // mainNavController.navigate("SavedDevices")
                },
                expanded = true,
                icon = { Icon(Icons.Filled.Add, "Localized Description") },
                text = { Text(text = "Add (${imeis.size})") },
            )
        }


    }
}

/**
 * This will display each IMEI with some fancy animations.
 * @param modifier
 * @param imei
 */
@Composable
fun Imei(modifier: Modifier = Modifier, imei: Long) {
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = true) {
        visible = true
    }

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(animationSpec = tween(durationMillis = 1000)) + slideInVertically(
            initialOffsetY = { it },
            animationSpec = tween(durationMillis = 500)
        ),
        exit = fadeOut(animationSpec = tween(durationMillis = 1000)) + slideOutVertically(
            targetOffsetY = { -it },
            animationSpec = tween(durationMillis = 500)
        )
    ) {
        Text(
            modifier = modifier
                .background(Color.White, RoundedCornerShape(7.dp))
                .padding(10.dp),
            text = imei.toString(),
            color = Color.Black,
            fontSize = 15.sp
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


/**
 * This is a dotted/dashed border.
 *
 * @param width of the border
 * @param radius for rounded corners
 * @param color of the border
 */
private fun Modifier.dashedBorder(width: Dp, radius: Dp, color: Color) =
    drawBehind {
        drawIntoCanvas {
            val paint = Paint()
                .apply {
                    strokeWidth = width.toPx()
                    this.color = color
                    style = PaintingStyle.Stroke
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(20f, 20f), 0f)
                }

            it.drawRoundRect(
                width.toPx(),
                width.toPx(),
                size.width - width.toPx(),
                size.height - width.toPx(),
                radius.toPx(),
                radius.toPx(),
                paint
            )
        }
    }
