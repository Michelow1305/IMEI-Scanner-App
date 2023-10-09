package com.hkr.mockupforproject.ui.screens

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Rect
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
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
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
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import com.google.common.util.concurrent.ListenableFuture
import com.hkr.mockupforproject.ui.AppViewModel
import com.hkr.mockupforproject.ImeiCodeAnalyzer


/**
 * CameraWithBoundedBox creates a bounded box on top of the CameraPreview, and this bounded box
 * has x and y coordinates to relative to the root/parent. These coordinates are converted to Dp sizes,
 * which can be used to crate a Rect.
 * Then the scanned IMEI's are presented in a LazyColumn. TODO()
 * @param modifier to change the layout parameters of the bounded box.
 * @param context to bind the CameraX lifecycle to activity's lifecycle.
 * @param appViewModel to add the scanned IMEI's to a MutableLiveData list in the view model.
 * @param mainNavController to navigate to another screen/composable
 * @throws SecurityException if camera permission is not granted.
 * @suppress UnusedMaterial3ScaffoldPaddingParameter setting padding to the scaffold screws with the
 *                                                   .onGloballyPositioned.
 */
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CameraWithBoundedBox(
    modifier: Modifier = Modifier,
    context: Context,
    appViewModel: AppViewModel,
    mainNavController: NavController
) {
    if (!appViewModel.hasCameraPermission(context)) {
        throw SecurityException("Camera permission not granted")
    }

    val imeis by appViewModel.scannedImeis.observeAsState(emptyList())
    /*
        I added cameraProviderFuture outside the CameraPreview(), so I can unbind it, whenever the user
        is not looking at this composable.
     */
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
    val rect = remember { mutableStateOf(Rect(0, 0, 0, 0)) }

    val density = LocalDensity.current
    val boxHeight = 70
    val xPadding = 25

    Scaffold(
        topBar = {
            IconButton(
                onClick = {
                    cameraProviderFuture.get().unbindAll()
                    mainNavController.popBackStack()
                },
                modifier = modifier
                    .padding(start = xPadding.dp, top = 25.dp)
                    .background(Color.White, shape = RoundedCornerShape(7.dp))
                    .size(45.dp)

            ) {
                Icon(
                    Icons.Filled.KeyboardArrowLeft,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = modifier
                        .size(35.dp)
                )
            }
        },

        floatingActionButton = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(30.dp)
            ) {

                LazyColumn(
                    modifier = modifier
                        .height(350.dp)
                        .align(Alignment.CenterHorizontally),
                    reverseLayout = true,
                ) {
                    items(imeis){imei ->
                        Imei(imei = imei)
                    }
                }

                ExtendedFloatingActionButton(
                    onClick = {
                        if (imeis.isNotEmpty()) {
                            cameraProviderFuture.get().unbindAll()
                            mainNavController.navigate("SavedDevices")
                        }
                    },
                    expanded = true,
                    icon = { Icon(Icons.Filled.Add, null) },
                    text = { Text(text = "Add (${imeis.size})") },
                    containerColor = if (imeis.isNotEmpty()) MaterialTheme.colorScheme.primary else Color.Gray,
                    contentColor = if (imeis.isNotEmpty()) Color.White else Color.DarkGray
                )
                Text(
                    text = "Scan manually",
                    color = Color.White,
                    fontSize = 15.sp,
                    modifier = modifier
                        .clickable(
                            enabled = true,
                            onClick = {
                                mainNavController.navigate("StartScreen")
                            }
                        )
                )
            }
        },
        floatingActionButtonPosition = FabPosition.Center,

        ) {
        Box(
            modifier = modifier
                .fillMaxSize()
        ) {

            CameraPreview(
                modifier = modifier,
                context = context,
                cameraProviderFuture,
                rect = rect
            ) { imeis ->
                appViewModel.addScannedImeis(imeis)
            }

            Column {
                // Bounded box
                Box(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(
                            top = 250.dp,
                            start = xPadding.dp,
                            end = xPadding.dp
                        )
                        .height(boxHeight.dp)
                        .dashedBorder(2.dp, 7.dp, Color.White)

                        /*
                            Here we convert the coordinates of this box to sizes in Dp.
                            Since this box is overlapping the CameraPreview(), we can use:
                            coordinates.positionInParent()
                         */
                        .onGloballyPositioned { coordinates ->
                            val position = coordinates.positionInParent()
                            val size = with(density) {
                                coordinates.size
                                    .toSize()
                                    .toDpSize()
                            }
                            val positionX = with(density) { position.x.toDp().value.toInt() }
                            val positionY = with(density) { position.y.toDp().value.toInt() }
                            rect.value = Rect(
                                positionX,
                                positionY,
                                (positionX + size.width.value.toInt()),
                                (positionY + size.height.value.toInt())
                            )
                        }
                )

                Text(
                    modifier = modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 17.dp),
                    text = "Scan IMEI code",
                    color = Color.White,
                    fontSize = 15.sp
                )

            }

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
 * @param cameraProviderFuture It is needed to initialize the camera in a separate thread (it is done automatically).
 * @param rect a rectangle that contains information on where the bounding box is relative to the root.
 * @param onImeiCodesDetected is a function that can be called when IMEI codes are detected.
 * @sample CameraPreview
 * (
 *      modifier = modifier,
 *      context = context,
 *      rect = Rect()
 * ) { imeis ->
 *      imeis.forEach { i ->
 *          Toast.makeText(context, i.toString(), Toast.LENGTH_LONG).show()
 *      }
 * }
 *
 */
@Composable
private fun CameraPreview(
    modifier: Modifier = Modifier,
    context: Context,
    cameraProviderFuture: ListenableFuture<ProcessCameraProvider> = remember {
        ProcessCameraProvider.getInstance(
            context
        )
    },
    rect: State<Rect>,
    onImeiCodesDetected: (imeis: List<Long>) -> Unit
) {
    val tag = "cameraPreview"

    val analyzer by remember(rect) {
        mutableStateOf(
            ImeiCodeAnalyzer(onImeiCodesDetected, rect)
        )
    }

    val imageAnalysis = remember {
        ImageAnalysis.Builder()
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()
            .also {
                it.setAnalyzer(
                    ContextCompat.getMainExecutor(context),
                    analyzer
                )
            }
    }

    val preview = remember {
        Preview.Builder()
            .build()
    }

    val cameraProvider = cameraProviderFuture.get()
    val cameraSelector = CameraSelector.Builder()
        .requireLensFacing(CameraSelector.LENS_FACING_BACK)
        .build()

    if (!cameraProvider.hasCamera(cameraSelector)) {
        throw SecurityException("Phone has no back camera.")
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
            try {
                cameraProvider.unbindAll()

                cameraProvider.bindToLifecycle(
                    context as LifecycleOwner,
                    cameraSelector,
                    imageAnalysis,
                    preview
                )

                preview.setSurfaceProvider(it.surfaceProvider)

            } catch (ex: Exception) {
                ex.printStackTrace()
                Log.e(tag, "Error binding cameraX to lifecycle")

            }
        },

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