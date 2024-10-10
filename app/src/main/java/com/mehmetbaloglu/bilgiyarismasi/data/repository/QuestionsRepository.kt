package com.mehmetbaloglu.bilgiyarismasi.data.repository

import android.util.Log
import com.mehmetbaloglu.bilgiyarismasi.data.model.DataOrException
import com.mehmetbaloglu.bilgiyarismasi.data.model.Questions
import com.mehmetbaloglu.bilgiyarismasi.retrofit.QuestionsApi
import javax.inject.Inject


class QuestionsRepository @Inject constructor(private val api: QuestionsApi) {
    private val dataOrException = DataOrException<Questions, Boolean, Exception>()

    suspend fun getAllQuestions(): DataOrException<Questions, Boolean, Exception> {
        try {
            dataOrException.loading = true
            dataOrException.data = api.getAllQuestions()
            if (dataOrException.data.toString().isNotEmpty()) {
                dataOrException.loading = false
            }
        } catch (exception: Exception) {
            dataOrException.e = exception
            Log.d("Exc", "getAllQuestions: ${dataOrException.e!!.localizedMessage}")
        }
        return dataOrException
    }

}
