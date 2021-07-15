package me.eungi.geoquiz

import androidx.annotation.StringRes

data class Question(@StringRes val textResId: Int, val answer: Boolean) {
    var solve = false
    var correct = false
}