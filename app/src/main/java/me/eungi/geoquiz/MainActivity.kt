package me.eungi.geoquiz

import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider

private const val TAG = "MainActivity"
private const val KEY_INDEX = "KEY_INDEX"
private const val KEY_CHEAT_COUNT = "KEY_CHEAT_COUNT"
private const val REQUEST_CODE_CHEAT = 0

class MainActivity : AppCompatActivity() {

    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var previousButton: ImageButton
    private lateinit var nextButton: ImageButton
    private lateinit var cheatButton: Button
    private lateinit var questionTextView: TextView
    private lateinit var cheatCountTextView: TextView

    private val quizViewModel: QuizViewModel by lazy {
        ViewModelProvider(this).get(QuizViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val currentIndex = savedInstanceState?.getInt(KEY_INDEX, 0) ?: 0
        quizViewModel.currentIndex = currentIndex
        val cheatCount = savedInstanceState?.getInt(KEY_CHEAT_COUNT, 3) ?: 3
        quizViewModel.cheatCount = cheatCount

        trueButton = findViewById(R.id.true_button)
        falseButton = findViewById(R.id.false_button)
        previousButton = findViewById(R.id.previous_button)
        nextButton = findViewById(R.id.next_button)
        cheatButton = findViewById(R.id.cheat_button)
        questionTextView = findViewById(R.id.question_text_view)
        cheatCountTextView = findViewById(R.id.remain_cheat_count_text_view)

        questionTextView.setOnClickListener {
            quizViewModel.moveToNext()
            updateQuestion()
        }

        trueButton.setOnClickListener { view: View ->
            checkAnswer(true)
        }

        falseButton.setOnClickListener { view: View ->
            checkAnswer(false)
        }

        previousButton.setOnClickListener {
            quizViewModel.moveToPrevious()
            updateQuestion()
        }

        nextButton.setOnClickListener {
            quizViewModel.moveToNext()
            updateQuestion()
        }

        cheatButton.setOnClickListener {
            val answerIsTrue = quizViewModel.currentQuestionAnswer
            val intent = CheatActivity.newIntent(this@MainActivity, answerIsTrue)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val optionAnim =
                    ActivityOptions.makeClipRevealAnimation(it, 0, 0, it.width, it.height)
                startActivityForResult(intent, REQUEST_CODE_CHEAT, optionAnim.toBundle())
            } else {
                startActivityForResult(intent, REQUEST_CODE_CHEAT)
            }
        }

        updateQuestion()
        setCheatButton()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) return

        if (requestCode == REQUEST_CODE_CHEAT) {
            quizViewModel.cheat(data?.getBooleanExtra(EXTRA_ANSWER_SHOWN, false) ?: false)
            quizViewModel.cheatCount--
            setCheatButton()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.d(TAG, "onSaveInstanceState")
        outState.putInt(KEY_INDEX, quizViewModel.currentIndex)
        outState.putInt(KEY_CHEAT_COUNT, quizViewModel.cheatCount)
    }

    private fun setCheatButton() {
        if (quizViewModel.cheatCount in 1..3) {
            val cheatCountText = "남은 컨닝 횟수 ${quizViewModel.cheatCount}회"
            cheatCountTextView.setText(cheatCountText)
        }
        else {
            val cheatCountText = "남은 컨닝 횟수 0회"
            cheatCountTextView.setText(cheatCountText)
            cheatButton.isEnabled = false
        }
    }

    private fun updateQuestion() {
        val questionTextResId = quizViewModel.currentQuestionText
        questionTextView.setText(questionTextResId)
        if (quizViewModel.currentQuestionSolve) {
            trueButton.isEnabled = false
            falseButton.isEnabled = false
            cheatButton.isEnabled = false
        } else {
            trueButton.isEnabled = true
            falseButton.isEnabled = true
            cheatButton.isEnabled = true
        }
    }

    private fun checkAnswer(userAnswer: Boolean) {
        val correctAnswer = quizViewModel.currentQuestionAnswer
        quizViewModel.solve(true)
        if (!quizViewModel.currentQuestionCheat)
            quizViewModel.correct(correctAnswer == userAnswer)
        trueButton.isEnabled = false
        falseButton.isEnabled = false
        cheatButton.isEnabled = false

//        val messageResId = if (userAnswer == correctAnswer) {
//            R.string.correct_toast
//        } else {
//            R.string.incorrect_toast
//        }
        val messageResId = when {
            quizViewModel.currentQuestionCheat -> R.string.judgment_toast
            userAnswer == correctAnswer -> R.string.correct_toast
            else -> R.string.incorrect_toast
        }
        showTopToast(getString(messageResId))
        if (quizViewModel.checkSolveAllQuestion()) {
            val correctPercentage = quizViewModel.correctPercentage()
            val answerPercentage = getString(R.string.correct_percentage_toast, correctPercentage)
            showTopToast(answerPercentage)
        }
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