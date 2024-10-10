package com.mehmetbaloglu.bilgiyarismasi.ui.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mehmetbaloglu.bilgiyarismasi.data.model.DataOrException
import com.mehmetbaloglu.bilgiyarismasi.data.model.Questions
import com.mehmetbaloglu.bilgiyarismasi.data.repository.QuestionsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class QuestionsViewModel @Inject constructor(val repository: QuestionsRepository)
    : ViewModel() {

    val dataInViewModel: MutableState<DataOrException<Questions, Boolean, Exception>> =
        mutableStateOf(DataOrException(null, true, Exception("")))

    init {
        getAllQuestions()
    }

    private fun getAllQuestions() {
        viewModelScope.launch {
            dataInViewModel.value.loading = true
            dataInViewModel.value = repository.getAllQuestions()
            if (dataInViewModel.value.data.toString().isNotEmpty()) {
                dataInViewModel.value.loading = false
            }
        }
    }
}