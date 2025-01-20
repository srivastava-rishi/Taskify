package com.rsstudio.taskify.domain.local

import android.content.Context
import android.speech.tts.TextToSpeech
import android.util.Log
import java.util.Locale

class TextToSpeechUseCase(
    private val context: Context
) : TextToSpeech.OnInitListener {

    private var textToSpeech: TextToSpeech? = null
    private var isInitialized = false

    init {
        initializeTTS()
    }

    private fun initializeTTS() {
        textToSpeech = TextToSpeech(context, this)
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = textToSpeech?.setLanguage(Locale.US)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("speechUseCase", "Language not supported or missing data")
            } else {
                isInitialized = true
            }
        } else {
            Log.e("speechUseCase", "Initialization failed")
        }
    }

    fun speak(text: String) {
        if (isInitialized) {
            textToSpeech?.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
        } else {
            Log.e("speechUseCase", "TTS not initialized")
        }
    }

    fun stop() {
        textToSpeech?.stop()
    }

    fun shutdown() {
        textToSpeech?.shutdown()
    }
}
