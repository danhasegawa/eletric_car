package com.example.electriccarapp.ui

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.electriccarapp.R

class CalcularAutonomiaActivity : AppCompatActivity() {
    private lateinit var preco: EditText
    private lateinit var kmPercorrido: EditText
    private lateinit var resultado: TextView
    private lateinit var btnCalcular: Button
    private lateinit var btnClose: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calcular_autonomia)
        setupView()
        setupListeners()
        setupCachedResult()
    }

    private fun setupView() {
        preco = findViewById(R.id.et_preco_kwh)
        kmPercorrido = findViewById(R.id.et_km_percorrido)
        btnCalcular = findViewById(R.id.btn_calcular)
        resultado = findViewById(R.id.tv_resultado)
        btnClose = findViewById(R.id.iv_close)
    }

    private fun setupListeners() {
        btnCalcular.setOnClickListener {
            calcular()
        }
        btnClose.setOnClickListener {
            finish()
        }
    }

    private fun calcular() {
        val preco = preco.text.toString().toFloat()
        val km = kmPercorrido.text.toString().toFloat()
        val result = preco / km
        resultado.text = result.toString()
        saveSharedPref(result)
    }

    private fun saveSharedPref(resultado: Float) {
        val sharedPref = getPreferences(Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            putFloat(getString(R.string.saved_cal), resultado)
            apply()
        }
    }

    fun getSharedPref(): Float {
        val sharedPref = getPreferences(Context.MODE_PRIVATE)
        return sharedPref.getFloat(getString(R.string.saved_cal), 0.0f)
    }

    private fun setupCachedResult() {
        val valorCalculado = getSharedPref()
        resultado.text = valorCalculado.toString()
    }


}