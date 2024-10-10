package com.mehmetbaloglu.bilgiyarismasi.di

import com.mehmetbaloglu.bilgiyarismasi.retrofit.QuestionsApi
import com.mehmetbaloglu.bilgiyarismasi.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideQuestionsApi() :QuestionsApi{
        val QuestionsApi =
            Retrofit.Builder().baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(QuestionsApi::class.java)

        return QuestionsApi
    }


}