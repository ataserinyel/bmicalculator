package com.example.bmicalculator

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import com.example.bmicalculator.databinding.ActivityMainBinding
import org.json.JSONArray
import org.json.JSONObject
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var b: ActivityMainBinding
    private val prefs by lazy { getSharedPreferences("bmi_prefs", Context.MODE_PRIVATE) }
    private val df = DecimalFormat("#.##")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityMainBinding.inflate(layoutInflater)
        setContentView(b.root)

        val dark = prefs.getBoolean("dark", false)
        AppCompatDelegate.setDefaultNightMode(
            if (dark) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        )
        b.switchTheme.isChecked = dark
        b.switchTheme.setOnCheckedChangeListener { _, isChecked ->
            prefs.edit().putBoolean("dark", isChecked).apply()
            AppCompatDelegate.setDefaultNightMode(
                if (isChecked) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
            )
        }

        renderHistory()

        b.btnCalc.setOnClickListener { calculateAndShow() }
        b.btnClear.setOnClickListener {
            b.etHeight.setText("")
            b.etWeight.setText("")
            b.cardResult.visibility = View.GONE
        }
        b.btnBack.setOnClickListener {
            val intent = Intent(this, StartActivity::class.java)
            startActivity(intent)
            finish() // geri tuşuna basıldığında hesaplama ekranına dönmesin
        }

    }

    private fun calculateAndShow() {
        val heightStr = b.etHeight.text?.toString()?.trim().orEmpty()
        val weightStr = b.etWeight.text?.toString()?.trim().orEmpty()

        if (heightStr.isEmpty() || weightStr.isEmpty()) {
            Toast.makeText(this, "Boy ve kilo giriniz.", Toast.LENGTH_SHORT).show()
            return
        }

        val heightInput = heightStr.replace(',', '.').toDoubleOrNull()
        val weight = weightStr.replace(',', '.').toDoubleOrNull()
        if (heightInput == null || weight == null || heightInput <= 0.0 || weight <= 0.0) {
            Toast.makeText(this, "Geçerli sayılar giriniz.", Toast.LENGTH_SHORT).show()
            return
        }

        val heightMeters = if (b.rbCm.isChecked) heightInput / 100.0 else heightInput
        val bmi = weight / (heightMeters * heightMeters)
        val (category, colorRes, iconRes) = categoryFor(bmi)

        b.cardResult.visibility = View.VISIBLE
        b.cardResult.setCardBackgroundColor(ContextCompat.getColor(this, colorRes))
        b.tvBmiValue.text = "BMI: ${df.format(bmi)}"
        b.tvCategory.text = category
        b.imgCategory.setImageResource(iconRes)

        val gender = if (b.rbMale.isChecked) "Erkek" else "Kadın"
        saveMeasurement(gender, heightMeters, weight, bmi)
        renderHistory()
    }

    private fun categoryFor(bmi: Double) = when {
        bmi < 18.5 -> Triple("Zayıf", android.R.color.holo_blue_light, R.drawable.ic_underweight)
        bmi < 25.0 -> Triple("Normal", android.R.color.holo_green_light, R.drawable.ic_normal)
        bmi < 30.0 -> Triple("Fazla Kilolu", android.R.color.holo_orange_light, R.drawable.ic_overweight)
        else -> Triple("Obez", android.R.color.holo_red_light, R.drawable.ic_obese)
    }

    private fun saveMeasurement(gender: String, heightMeters: Double, weightKg: Double, bmi: Double) {
        val arr = JSONArray(prefs.getString("history", "[]"))
        val fmt = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
        val obj = JSONObject().apply {
            put("ts", fmt.format(Date()))
            put("gender", gender)
            put("h", df.format(heightMeters))
            put("w", df.format(weightKg))
            put("bmi", df.format(bmi))
        }
        arr.put(obj)

        val trimmed = JSONArray()
        val start = (arr.length() - 5).coerceAtLeast(0)
        for (i in start until arr.length()) trimmed.put(arr.getJSONObject(i))
        prefs.edit().putString("history", trimmed.toString()).apply()
    }

    private fun renderHistory() {
        val arr = JSONArray(prefs.getString("history", "[]"))
        if (arr.length() == 0) {
            b.tvHistory.text = "-"
            return
        }
        val sb = StringBuilder()
        for (i in 0 until arr.length()) {
            val o = arr.getJSONObject(i)
            sb.append("• ${o.getString("ts")} | ${o.getString("gender")} | ")
            sb.append("Boy: ${o.getString("h")} m, Kilo: ${o.getString("w")} kg, ")
            sb.append("BMI: ${o.getString("bmi")}\n")
        }
        b.tvHistory.text = sb.toString().trim()
    }
}
