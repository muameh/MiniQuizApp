package com.mehmetbaloglu.bilgiyarismasi.components

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mehmetbaloglu.bilgiyarismasi.data.model.QuestionItem
import com.mehmetbaloglu.bilgiyarismasi.ui.viewmodels.QuestionsViewModel


@Composable
fun Questions(viewModel: QuestionsViewModel) {
    val questionsList = viewModel.dataInViewModel.value.data?.toMutableList()
    val currentQuestionsIndex = remember { mutableStateOf(0) }


    if (viewModel.dataInViewModel.value.loading == true) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator()
        }
    } else {
        if (questionsList != null) {
            if (currentQuestionsIndex.value >= questionsList.size) {
                //sorular bittiğinde burası çalışır, sonuç ekranına yönlendirilebilir
            } else {
                val question = questionsList[currentQuestionsIndex.value]
                QuestionDisplay(
                    question = question,
                    currentQuestionIndex = currentQuestionsIndex,
                    onNextClicked = { currentQuestionsIndex.value += 1 },
                    onFinalClicked = {},
                    viewModel
                )
            }
        }
    }
}

@Composable
fun QuestionTracker(counter: Int = 1, outOf: Int = 100) {
    Spacer(modifier = Modifier.height(30.dp))
    Text(text = "Soru $counter / $outOf", modifier = Modifier.padding(8.dp))
}

@Composable
fun QuestionDisplay(
    question: QuestionItem,
    currentQuestionIndex: MutableState<Int>,
    onNextClicked: (Int) -> Unit = {},
    onFinalClicked: () -> Unit = {},
    viewModel: QuestionsViewModel
) {
    var correctAnswerState = remember(question) {
        mutableStateOf<Boolean?>(null)
    }

    var selectedChoiceIndex = remember(question) {
        mutableIntStateOf(-1)
    }

    var chociesList = remember(question) {
        question.choices?.toMutableList()
    }

    // Diyalog gösterim durumunu kontrol eden state
    var showDialog = remember { mutableStateOf<Boolean?>(null) }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        QuestionTracker(
            (currentQuestionIndex.value + 1), viewModel.dataInViewModel.value.data?.size ?: 0
        )
        Spacer(modifier = Modifier.height(20.dp))


        Column {
            Text(
                text = question.question.toString(), modifier = Modifier.padding(8.dp)
            )
            Spacer(modifier = Modifier.height(30.dp))
            chociesList?.forEachIndexed { index, choice ->
                Row(
                    modifier = Modifier.height(30.dp),
                    verticalAlignment = Alignment.CenterVertically

                ) {
                    RadioButton(
                        selected = 
                        selectedChoiceIndex.intValue == index,
                        onClick = {
                        selectedChoiceIndex.intValue = index
                        correctAnswerState.value = (selectedChoiceIndex.intValue == question.answersIndex)
                        showDialog.value = true
                            Log.d("CorrectAnswer", "correctAnswerState: ${correctAnswerState.value}")
                            Log.d("CorrectAnswer", "answersIndex: ${question.answersIndex}")
                            Log.d("CorrectAnswer", "selectedChoiceIndex: ${selectedChoiceIndex.intValue}")

                        }
                    )
                    Text(text = choice.toString())
                }
            }
            Button(modifier = Modifier.padding(8.dp), onClick = {
                onNextClicked(currentQuestionIndex.value)
            }) {
                Text(text = "Sonraki")

            }
        }
        // Eğer diyalog durumu aktifse CorrectAnswerDialog'u göster
        if (showDialog.value == true) {
            CorrectAnswerDialog(
                question = question,
                answersState = correctAnswerState
            ) {
                showDialog.value = false // Diyalog kapandığında durumu false yap
                selectedChoiceIndex.intValue = -1 // Seçimi sıfırla
            }
        }
    }

}


@Composable
fun CorrectAnswerDialog(
    question: QuestionItem,
    answersState: MutableState<Boolean?>,
    onDismissRequest: () -> Unit
) {
    val correctAnswer = question.answersIndex?.let { question.choices?.get(it) } // Doğru cevabı al

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = {
            Text(text = if (answersState.value == true) "Tebrikler!" else "Yanlış!")
        },
        text = {
            Text(
                text = if (answersState.value == true) {
                    "Doğru cevabı seçtiniz."
                } else {
                    "Doğru cevap:${question.answer}"
                }
            )
        },
        confirmButton = {
            Button(onClick = onDismissRequest) {
                Text("Tamam")
            }
        }
    )
}

