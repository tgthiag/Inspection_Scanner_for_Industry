package com.tgapps.sgascanner

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.tgapps.sgascanner.databinding.ActivityBarcodeScannerBinding
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

private lateinit var binding: ActivityBarcodeScannerBinding
class BarcodeScannerActivity : AppCompatActivity() {
    private lateinit var cameraExecutor: ExecutorService
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBarcodeScannerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        checkCameraPermission()
        cameraExecutor = Executors.newSingleThreadExecutor()
        var database = BarcodeDatabase(this)
        var db = database.readableDatabase

        // Complete activity setup...
    }

    override fun onDestroy() {
        super.onDestroy()

        cameraExecutor.shutdown()
    }


    //SOLICITA PERMISSÃO PARA CÂMERA
    private fun checkCameraPermission() {
        try {
            val requiredPermissions = arrayOf(Manifest.permission.CAMERA)
            ActivityCompat.requestPermissions(this, requiredPermissions, 0)
        } catch (e: IllegalArgumentException) {
            checkIfCameraPermissionIsGranted()
        }
    }


    //CHECANDO PERMISSÃO DA CÂMERA PARA INICIAR
    private fun checkIfCameraPermissionIsGranted() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            // PERMITIDO: INICIAR CAMERA PREVIEW
            startCamera()
        } else {
            // PERMISSÃO NEGADA
            MaterialAlertDialogBuilder(this)
                .setTitle("Necessita de permissão!")
                .setMessage("Este app precisa de autorização da câmera para escanear.")
                .setPositiveButton("Ok") { _, _ ->
                    // CONTINUA PEDINDO PERMISSÃO ATÉ A ACEITAÇÃO
                    checkCameraPermission()
                }
                .setCancelable(false)
                .create()
                .apply {
                    setCanceledOnTouchOutside(false)
                    show()
                }
        }
    }

   //EXECUTA QUANDO O USUÁRIO RESPONDE A PERMISSÃO DA CAMERA
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        checkIfCameraPermissionIsGranted()
    }

    //CAMERA PREVIEW E ANALISADOR DE IMAGEM
    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            // CAMERA PREVIEW
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.previewView.surfaceProvider)
                }

            // ANALISADOR DE IMAGEM
            val imageAnalyzer = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()
                .also {
                    it.setAnalyzer(cameraExecutor, BarcodeAnalyzer(this))
                }

            // CAMERA TRASEIRA PADRÃO
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                // DESMONTANDO CÂMERA
                cameraProvider.unbindAll()

                // REMONTANDO CAMERA
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageAnalyzer
                )

            } catch (exc: Exception) {
                exc.printStackTrace()
            }
        }, ContextCompat.getMainExecutor(this))
    }
}
