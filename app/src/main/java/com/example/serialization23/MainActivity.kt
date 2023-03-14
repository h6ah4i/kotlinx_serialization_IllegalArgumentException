package com.example.serialization23

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import java.io.ByteArrayInputStream
@Serializable
data class SomeDto(
    @SerialName("id")
    val id: String
)

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        handleJson()
    }

    @OptIn(ExperimentalSerializationApi::class)
    private fun handleJson() {
        val someJson = "{\"id\":\"1\"},"
        val resultString = "[" + someJson.repeat(1490).removeSuffix(",") + "]"
        // 16391 bytes, more than 16384 (2^15),
        val byteArray = resultString.toByteArray(java.nio.charset.StandardCharsets.UTF_8)
        val stream = ByteArrayInputStream(byteArray)
        // throws java.lang.IllegalArgumentException: Bad position on Api level 23  if stream more than 16384 bytes
        // but works on 24+ with any stream sizes
        val parsedResponse = Json.decodeFromStream<List<SomeDto>>(stream)
        val textView: TextView = findViewById(R.id.text_view)
        textView.text = parsedResponse.firstOrNull()?.id
    }
}