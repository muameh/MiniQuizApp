package com.mehmetbaloglu.bilgiyarismasi.ui.screens

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.mehmetbaloglu.bilgiyarismasi.components.Questions
import com.mehmetbaloglu.bilgiyarismasi.ui.viewmodels.QuestionsViewModel


@Composable
fun HomeScreen(viewModel: QuestionsViewModel = hiltViewModel()) {
    Questions(viewModel = viewModel)


}