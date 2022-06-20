package com.tgapps.sgascanner

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.tgapps.sgascanner.databinding.ActivityResultsBinding

private lateinit var binding: ActivityResultsBinding
class ActivityResults : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityResultsBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.txtNome.append(intent.getStringExtra("nome"))
        binding.txtMaterial.append(intent.getStringExtra("material"))
        binding.txtConjunto.append(intent.getStringExtra("conjunto"))
        binding.txtUnidade.append(intent.getStringExtra("unidade"))
        binding.txtPacote.append(intent.getStringExtra("pacote"))

    }
}