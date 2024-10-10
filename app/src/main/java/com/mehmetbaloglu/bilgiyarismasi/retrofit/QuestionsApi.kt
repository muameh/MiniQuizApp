package com.mehmetbaloglu.bilgiyarismasi.retrofit

import com.mehmetbaloglu.bilgiyarismasi.data.model.Questions
import retrofit2.http.GET
import javax.inject.Singleton


@Singleton
interface QuestionsApi {

    @GET("osmanliDevleti")
    suspend fun getAllQuestions(): Questions

}