package com.mehmetbaloglu.bilgiyarismasi.data.model


import com.google.gson.annotations.SerializedName

data class QuestionItem(
    val answer: String?,
    val answersIndex: Int?,
    val category: String?,
    val choices: List<String?>?,
    val question: String?
)