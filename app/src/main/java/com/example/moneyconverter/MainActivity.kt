package com.example.moneyconverter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.TextView
import org.json.JSONObject
import java.net.URL
import java.text.DecimalFormat
import javax.net.ssl.HttpsURLConnection

class MainActivity : AppCompatActivity() {
    private lateinit var result:TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        result = findViewById<TextView>(R.id.resultado_field)
        val buttonConverter = findViewById<Button>(R.id.button_converter)
        buttonConverter.setOnClickListener {
            converter()
        }
    }
        private fun converter() {
            val selectedCorrency = findViewById<RadioGroup>(R.id.radio_group)

            val currency = when(selectedCorrency.checkedRadioButtonId){
                R.id.radio_usd -> "USD"
                R.id.radio_euro -> "EUR"
               else -> "GBP"
            }

            val editField = findViewById<EditText>(R.id.edit_field)

            val value = editField.text.toString()
            if(value.isEmpty()) return

            result.text = value
            result.visibility = View.VISIBLE

            Thread{// tem que fazer um processo paralelo, para não travar o programa para o user

                val url = URL("https://free.currconv.com/api/v7/convert?q=${currency}_BRL&compact=ultra&apiKey=44d14aae4ce403d55671")
                val conn = url.openConnection() as HttpsURLConnection

                try{
                    val dec = DecimalFormat("#,###.00")
                    val data = conn.inputStream.bufferedReader().readText()

                    val obj = JSONObject(data)

                    runOnUiThread {
                        val res = obj.getDouble("${currency}_BRL")

                        result.text = "${dec.format(value.toDouble() * res)}"
                        result.visibility = View.VISIBLE
                    }
                }finally {
                    conn.disconnect()
                }
            }.start()
        }

}