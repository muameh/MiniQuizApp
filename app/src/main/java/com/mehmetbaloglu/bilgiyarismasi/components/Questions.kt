package com.mehmetbaloglu.bilgiyarismasi.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
    val correctAnswerState = remember(question) {
        mutableStateOf<Boolean?>(null)
    }

    val selectedChoiceIndex = remember(question) {
        mutableIntStateOf(-1)
    }

    val chociesList = remember(question) {
        question.choices?.toMutableList()
    }

    val showResult = remember { mutableStateOf<Boolean?>(null) }
    val resultMessage = remember { mutableStateOf("") }

    // Doğru cevap sayısını tutacak state
    val correctAnswerCounter = remember { mutableStateOf(0) }

    // Sorunun daha önce yanıtlanıp yanıtlanmadığını kontrol eden durum
    val isAnswered = remember { mutableStateOf(false) }

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
                    RadioButton(selected = selectedChoiceIndex.intValue == index, onClick = {
                        if (!isAnswered.value) {
                            selectedChoiceIndex.intValue = index
                            correctAnswerState.value =
                                (selectedChoiceIndex.intValue == question.answers_index)

                            // Doğru cevap sayısını güncelle
                            if (correctAnswerState.value == true) {
                                correctAnswerCounter.value += 1
                            }

                            // Sonuç mesajını ayarla
                            resultMessage.value = if (correctAnswerState.value == true) {
                                "Tebrikler! Doğru cevabı bildiniz."
                            } else {
                                "Yanlış cevap! Doğru cevap: ${question.answer}"
                            }

                            showResult.value = true
                            isAnswered.value = true // Soruyu daha önce cevaplanmış olarak işaretle
                        }


                    })
                    Text(text = choice.toString())
                }
            }

            Button(modifier = Modifier.padding(8.dp), onClick = {
                onNextClicked(currentQuestionIndex.value)
                showResult.value = false // Sonuç gösterimini sıfırla
                isAnswered.value = false // Sonraki soru için yanıt durumunu sıfırla
            }) {
                Text(text = "Sonraki")
            }

            // Sonucu göster
            if (showResult.value == true) {
                Text(text = resultMessage.value, modifier = Modifier.padding(8.dp))
                Text(
                    text = "Doğru cevap sayınız: ${correctAnswerCounter.value}/${currentQuestionIndex.value + 1}",
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }

}



