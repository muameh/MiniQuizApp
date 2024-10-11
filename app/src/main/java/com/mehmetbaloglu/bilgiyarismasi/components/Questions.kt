package com.mehmetbaloglu.bilgiyarismasi.components

import android.app.Activity
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonColors
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mehmetbaloglu.bilgiyarismasi.data.model.QuestionItem
import com.mehmetbaloglu.bilgiyarismasi.ui.viewmodels.QuestionsViewModel
import com.mehmetbaloglu.bilgiyarismasi.utils.AppColors


@Composable
fun Questions(viewModel: QuestionsViewModel) {
    val questionsList = viewModel.dataInViewModel.value.data?.toMutableList()
    val currentQuestionsIndex = remember { mutableStateOf(0) }

    // Doğru ve yanlış cevap sayacı
    val correctAnswerCounter = remember { mutableStateOf(0) }
    val incorrectAnswerCounter = remember { mutableStateOf(0) }

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
                FinalDialog(
                    correctCount = correctAnswerCounter.value,
                    incorrectCount = incorrectAnswerCounter.value
                )
            } else {
                val question = questionsList[currentQuestionsIndex.value]
                QuestionDisplay(
                    question = question,
                    currentQuestionIndex = currentQuestionsIndex,
                    onNextClicked = { currentQuestionsIndex.value += 1 },
                    onFinalClicked = {},
                    correctAnswerCounter = correctAnswerCounter, // Doğru sayısını ilet
                    incorrectAnswerCounter = incorrectAnswerCounter, // Yanlış sayısını ilet
                    viewModel
                )
            }
        }
    }
}

@Composable
fun QuestionTracker(counter: Int = 1, outOf: Int = 100) {
    Spacer(modifier = Modifier.height(30.dp))
    Text(
        text = "Soru $counter / $outOf",
        modifier = Modifier.padding(10.dp),
        color = AppColors.white,
        fontSize = 32.sp
    )
}

@Composable
fun QuestionDisplay(
    question: QuestionItem,
    currentQuestionIndex: MutableState<Int>,
    onNextClicked: (Int) -> Unit = {},
    onFinalClicked: () -> Unit = {},
    correctAnswerCounter: MutableState<Int>, // Doğru cevap sayacını parametre olarak al
    incorrectAnswerCounter: MutableState<Int>, // Yanlış cevap sayacını parametre olarak al
    viewModel: QuestionsViewModel
) {
    val correctAnswerState = remember(question) {
        mutableStateOf<Boolean?>(null)
    }

    val selectedChoiceIndex = remember(question) {
        mutableIntStateOf(-1)
    }

    val choicesList = remember(question) {
        question.choices?.toMutableList()
    }

    val showResult = remember { mutableStateOf<Boolean?>(null) }
    val resultMessage = remember { mutableStateOf("") }

    // Sorunun daha önce yanıtlanıp yanıtlanmadığını kontrol eden durum
    val isAnswered = remember { mutableStateOf(false) }

    val context = LocalContext.current // Mevcut Context'i al

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = AppColors.back_ground_color
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,

            ) {
            QuestionTracker(
                currentQuestionIndex.value + 1, viewModel.dataInViewModel.value.data?.size ?: 0
            )
            Spacer(modifier = Modifier.height(20.dp))
            HorizontalDivider()
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start,
            ) {
                Text(
                    text = question.question.toString(),
                    color = AppColors.white,
                    modifier = Modifier.padding(12.dp),
                    fontSize = 24.sp

                )
                Spacer(modifier = Modifier.height(20.dp))
                choicesList?.forEachIndexed { index, choice ->
                    Row(
                        modifier = Modifier.padding(2.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = selectedChoiceIndex.intValue == index,
                            colors = RadioButtonColors(
                                selectedColor = if (correctAnswerState.value == true) Color.Green else Color.Red,
                                unselectedColor = AppColors.white,
                                disabledSelectedColor = AppColors.white,
                                disabledUnselectedColor = AppColors.white
                            ),
                            onClick = {
                                handleAnswerSelection(
                                    index,
                                    question,
                                    correctAnswerCounter,
                                    incorrectAnswerCounter,
                                    correctAnswerState,
                                    resultMessage,
                                    showResult,
                                    isAnswered,
                                    selectedChoiceIndex,
                                    context
                                )
                            }
                        )
                        Text(
                            text = choice.toString(),
                            color = when {
                                selectedChoiceIndex.intValue == index && correctAnswerState.value == true -> Color.Green
                                selectedChoiceIndex.intValue == index && correctAnswerState.value == false -> Color.Red
                                else -> AppColors.white
                            },
                            fontSize = 20.sp,
                            modifier = Modifier.clickable {
                                handleAnswerSelection(
                                    index,
                                    question,
                                    correctAnswerCounter,
                                    incorrectAnswerCounter,
                                    correctAnswerState,
                                    resultMessage,
                                    showResult,
                                    isAnswered,
                                    selectedChoiceIndex,
                                    context
                                )
                            }

                        )
                    }
                }

                Button(
                    modifier = Modifier
                        .padding(8.dp, 8.dp, 8.dp, 18.dp)
                        .align(Alignment.CenterHorizontally),
                    onClick = {
                        onNextClicked(currentQuestionIndex.value)
                        showResult.value = false // Sonuç gösterimini sıfırla
                        isAnswered.value = false // Sonraki soru için yanıt durumunu sıfırla
                    }) {
                    Text(text = "Sonraki")
                }

                // Sonucu göster
                if (showResult.value == true) {
                    Column(
                        modifier = Modifier.fillMaxWidth(), // Genişliği doldur
                        horizontalAlignment = Alignment.CenterHorizontally, // Yatayda ortala
                        verticalArrangement = Arrangement.Center // Dikeyde ortala (isteğe bağlı)
                    ) {
                        Text(
                            text = resultMessage.value,
                            color = if (resultMessage.value.startsWith("T")) Color.Green else Color.Red,
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                            fontSize = 18.sp
                        )
                        Text(
                            text = "Doğru cevap sayınız: ${correctAnswerCounter.value}/${currentQuestionIndex.value + 1}",
                            color = AppColors.golden,
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                            fontSize = 18.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FinalDialog(correctCount: Int, incorrectCount: Int) {
    val context = LocalContext.current // Get the current context

    Surface {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Quiz Tamamlandı!", fontSize = 24.sp)
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Doğru Sayınız: $correctCount", fontSize = 18.sp)
            Text(text = "Yanlış Sayınız: $incorrectCount", fontSize = 18.sp)

            Spacer(modifier = Modifier.height(24.dp)) // Add some space

            // Button to close the application
            Button(onClick = {
                // Finish the activity
                (context as? Activity)?.finish()
            }) {
                Text(text = "Çıkış Yap") // Button text
            }
        }
    }
}


private fun handleAnswerSelection(
    index: Int,
    question: QuestionItem,
    correctAnswerCounter: MutableState<Int>,
    incorrectAnswerCounter: MutableState<Int>,
    correctAnswerState: MutableState<Boolean?>,
    resultMessage: MutableState<String>,
    showResult: MutableState<Boolean?>,
    isAnswered: MutableState<Boolean>,
    selectedChoiceIndex: MutableIntState, // Yeni parametre olarak ekledik
    context: Context
) {
    if (!isAnswered.value) {
        selectedChoiceIndex.intValue = index
        correctAnswerState.value = (selectedChoiceIndex.intValue == question.answers_index)

        // Doğru veya yanlış cevabını say
        if (correctAnswerState.value == true) {
            correctAnswerCounter.value += 1 // Doğru sayısını artır
        } else {
            incorrectAnswerCounter.value += 1 // Yanlış sayısını artır
        }

        // Sonuç mesajını ayarla
        resultMessage.value = if (correctAnswerState.value == true) {
            "Tebrikler! Doğru cevabı bildiniz."
        } else {
            "Yanlış cevap! Doğru cevap: ${question.answer}"
        }

        showResult.value = true
        isAnswered.value = true // Soruyu daha önce cevaplanmış olarak işaretle
    } else {
        Toast.makeText(
            context,
            "Bu soru daha önce cevaplanmıştır.",
            Toast.LENGTH_SHORT
        ).show()
    }
}




