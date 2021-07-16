package me.eungi.geoquiz

import androidx.lifecycle.ViewModel

private const val TAG = "QuizViewModel"
class QuizViewModel : ViewModel() {

    private val questionBank = listOf(
        Question(R.string.question_australia, true),
        Question(R.string.question_oceans, true),
        Question(R.string.question_mideast, false),
        Question(R.string.question_africa, false),
        Question(R.string.question_americas, true),
        Question(R.string.question_asia, true),
    )

    var currentIndex = 0

    val currentQuestionAnswer: Boolean
        get() = questionBank[currentIndex].answer

    val currentQuestionText: Int
        get() = questionBank[currentIndex].textResId


    val currentQuestionSolve: Boolean
        get() = questionBank[currentIndex].solve

    val currentQuestionCorrect: Boolean
        get() = questionBank[currentIndex].correct

    fun solve(solve: Boolean) {
        questionBank[currentIndex].solve = solve
    }

    fun correct(correct: Boolean) {
        questionBank[currentIndex].correct = correct
    }

    fun checkSolveAllQuestion(): Boolean {
        return questionBank.all { it.solve }
    }

    fun correctPercentage(): Int {
        return (questionBank.count { it.correct }.toDouble() / questionBank.size * 100).toInt()
    }

    fun moveToNext() {
        currentIndex = (currentIndex + 1) % questionBank.size
    }

    fun moveToPrevious() {
        if (currentIndex -1 < 0) currentIndex = questionBank.size - 1
        else currentIndex = (currentIndex - 1) % questionBank.size
    }


}