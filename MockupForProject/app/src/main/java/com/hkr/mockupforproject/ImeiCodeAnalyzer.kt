package com.hkr.mockupforproject

import android.graphics.Rect
import android.util.Log
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.compose.runtime.State
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage

class ImeiCodeAnalyzer(
    private val onImeiCodesDetected: (imeis: List<Long>) -> Unit,
    private val rect: State<Rect>
) : ImageAnalysis.Analyzer {

    private val imeiLength = 15
    private val tag = "analyzer"
    private val scanner = BarcodeScanning.getClient(
        BarcodeScannerOptions.Builder()
            .enableAllPotentialBarcodes()
            .build()
    )

    @androidx.annotation.OptIn(ExperimentalGetImage::class)
    override fun analyze(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image ?: return
        val rotationDegrees = imageProxy.imageInfo.rotationDegrees

        val image = InputImage.fromMediaImage(mediaImage, rotationDegrees)
        val rect = rect.value
        val adjustedRect = Rect()
        adjustedRect.left = rect.left
        adjustedRect.top = mediaImage.height - rect.bottom
        adjustedRect.right = rect.right
        adjustedRect.bottom = rect.top

        scanner.process(image)
            .addOnSuccessListener { barcodes ->
                val imeis = barcodes.mapNotNull { barcode ->

                    if (barcode.boundingBox?.let { adjustedRect.contains(it) } == true) {
                        try {

                            barcode.rawValue?.takeIf {
                                it.isNotBlank() && it.length == imeiLength && it.all { char -> char.isDigit() }
                            }?.toLong()

                        } catch (ex: NumberFormatException) {
                            null

                        }


                    } else {
                        null

                    }

                }

                onImeiCodesDetected(imeis)

            }

            .addOnFailureListener {
                Log.d(tag, "Analysis Failed")
            }

            .addOnCompleteListener {
                imageProxy.close()
            }

    }

}
