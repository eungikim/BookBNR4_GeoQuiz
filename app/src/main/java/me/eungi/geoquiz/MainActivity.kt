package me.eungi.geoquiz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var previousButton: ImageButton
    private lateinit var nextButton: ImageButton
    private lateinit var questionTextView: TextView

    private val questionBank = listOf(
        Question(R.string.question_australia, true),
        Question(R.string.question_oceans, true),
        Question(R.string.question_mideast, false),
        Question(R.string.question_africa, false),
        Question(R.string.question_americas, true),
        Question(R.string.question_asia, true),
    )

    private var currentIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        trueButton = findViewById(R.id.true_button)
        falseButton = findViewById(R.id.false_button)
        previousButton = findViewById(R.id.previous_button)
        nextButton = findViewById(R.id.next_button)
        questionTextView = findViewById(R.id.question_text_view)

        questionTextView.setOnClickListener {
            currentIndex = (currentIndex + 1) % questionBank.size
            updateQuestion()
        }

        trueButton.setOnClickListener { view: View ->
            checkAnswer(true)
        }

        falseButton.setOnClickListener { view: View ->
            checkAnswer(false)
        }

        previousButton.setOnClickListener {
            if (currentIndex -1 < 0) currentIndex = questionBank.size - 1
            else currentIndex = (currentIndex - 1) % questionBank.size
            updateQuestion()
        }

        nextButton.setOnClickListener {
            currentIndex = (currentIndex + 1) % questionBank.size
            updateQuestion()
        }

        updateQuestion()
    }

    private fun updateQuestion() {
        val questionTextResId = questionBank[currentIndex].textResId
        questionTextView.setText(questionTextResId)
        if (questionBank[currentIndex].solve) {
            trueButton.isEnabled = false
            falseButton.isEnabled = false
        } else {
            trueButton.isEnabled = true
            falseButton.isEnabled = true
        }
    }

    private fun checkAnswer(userAnswer: Boolean) {
        val correctAnswer = questionBank[currentIndex].answer
        questionBank[currentIndex].solve = true
        questionBank[currentIndex].correct = correctAnswer == userAnswer
        trueButton.isEnabled = false
        falseButton.isEnabled = false

        val messageResId = if (userAnswer == correctAnswer) {
            R.string.correct_toast
        } else {
            R.string.incorrect_toast
        }
        showTopToast(getString(messageResId))
        if (checkSolveAllQuestion()) {
            val correctPercentage = (questionBank.count { it.correct }.toDouble() / questionBank.size * 100).toInt()
            val answerPercentage = getString(R.string.correct_percentage_toast, correctPercentage)
            showTopToast(answerPercentage)
        }
    }

    private fun checkSolveAllQuestion(): Boolean {
        return questionBank.all { it.solve }
    }

    /* Challenge */
    fun showTopToast(strRes: String) {
        val container = layoutInflater.inflate(R.layout.toast_text, findViewById(R.id.toast_container))
        val text: TextView = container.findViewById(R.id.toast_text)
        text.setText(strRes)
        val toast = Toast(this)
        toast.setGravity(Gravity.TOP, 0, 300)
        toast.view = container
        toast.show()
    }
}