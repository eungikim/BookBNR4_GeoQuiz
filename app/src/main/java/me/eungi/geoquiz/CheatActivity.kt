package me.eungi.geoquiz

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

const val EXTRA_ANSWER_SHOWN = "me.eungi.geoquiz.EXTRA_ANSWER_SHOWN"
private const val EXTRA_ANSWER_IS_TRUE = "me.eungi.geoquiz.EXTRA_ANSWER_IS_TRUE"
private const val KEY_SHOWN_ANSWER = "KEY_SHOWN_ANSWER"

class CheatActivity : AppCompatActivity() {

    private lateinit var answerTextView: TextView
    private lateinit var showAnswerButton: Button

    private var answerIsTrue = false
    private var shownAnswer = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cheat)

        answerIsTrue = intent.getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false)
        answerTextView = findViewById(R.id.answer_text_view)
        showAnswerButton = findViewById(R.id.show_answer_button)
        showAnswerButton.setOnClickListener {
            shownAnswer = true
            showAnswer()
        }

        shownAnswer = savedInstanceState?.getBoolean(KEY_SHOWN_ANSWER, false) ?: false
        if (shownAnswer) showAnswer()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(KEY_SHOWN_ANSWER, shownAnswer)
    }

    private fun showAnswer() {
        val answerText = when {
            answerIsTrue -> R.string.true_button
            else -> R.string.false_button
        }
        answerTextView.setText(answerText)
        setAnswerShownResult(true)
    }


    private fun setAnswerShownResult(isAnswerShown: Boolean) {
        val data = Intent().apply {
            putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown)
        }
        setResult(Activity.RESULT_OK, data)
    }

    companion object {
        fun newIntent(packageContext: Context, answerIsTrue: Boolean): Intent {
            return Intent(packageContext, CheatActivity::class.java).apply {
                putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue)
            }
        }
    }
}