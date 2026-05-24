package com.example.projekt_czezowska_dominika_kosmetyki

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.io.File

class OrderPreviewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_order_preview)

        val contentText =
            findViewById<TextView>(R.id.orderContent)

        val fileName =
            intent.getStringExtra("FILE_NAME")

        if (fileName != null) {

            val file =
                File(filesDir, fileName)

            if (file.exists()) {

                contentText.text =
                    file.readText()
            }
        }
    }
}