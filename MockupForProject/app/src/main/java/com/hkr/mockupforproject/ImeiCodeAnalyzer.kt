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
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions

class ImeiCodeAnalyzer(
    private val onImeiCodesDetected: (imeis: List<Long>) -> Unit,
    private val rect: State<Rect>
) : ImageAnalysis.Analyzer {
    private val imeiLength = 15
    private val scanner = BarcodeScanning.getClient(
        BarcodeScannerOptions.Builder()
            .enableAllPotentialBarcodes()
            .build()
    )
    private val textscanner = TextRecognition.getClient(
        TextRecognizerOptions.DEFAULT_OPTIONS
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
            .addOnCompleteListener {
                imageProxy.close()
            }
        textscanner.process(image)
            .addOnSuccessListener {text ->
                for (block in text.textBlocks) {
                    for (line in block.lines) {
                        if(line.boundingBox?.let{adjustedRect.contains(it)} == true){
                            val lineText = line.text
                            if(lineText.length == imeiLength) {
                                var imeis: List<Long> = listOf()
                                try {
                                    imeis = listOf(lineText.toLong())
                                } catch (e : NumberFormatException) { null }
                                if(imeis.isNotEmpty()) {
                                    onImeiCodesDetected(imeis)
                                } else null
                            }
                        }

                        // I leave all these examples in here in case we want to work with it later
                        /*
                        val blockText = block.text
                        val blockCornerPoints = block.cornerPoints
                        val blockFrame = block.boundingBox
                        val lineCornerPoints = line.cornerPoints
                        val lineFrame = line.boundingBox
                        for (element in line.elements) {
                            val elementText = element.text
                            val elementCornerPoints = element.cornerPoints
                            val elementFrame = element.boundingBox
                        }
                        */
                    }
                }
            }
            .addOnCompleteListener {
                imageProxy.close()
            }
    }
}
