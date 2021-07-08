package me.eungi.geoquiz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    private lateinit var trueButton: Button
    private lateinit var falseButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        trueButton = findViewById(R.id.true_button)
        falseButton = findViewById(R.id.false_button)

        trueButton.setOnClickListener { view: View ->
            showTopToast(R.string.correct_toast)
        }

        falseButton.setOnClickListener { view: View ->
            showTopToast(R.string.incorrect_toast)
        }
    }

    /* Challenge */
    fun showTopToast(strRes: Int) {
        val container = layoutInflater.inflate(R.layout.toast_text, findViewById(R.id.toast_container))
        val text: TextView = container.findViewById(R.id.toast_text)
        text.setText(strRes)
        val toast = Toast(this)
        toast.setGravity(Gravity.TOP, 0, 300)
        toast.view = container
        toast.show()

    }
}