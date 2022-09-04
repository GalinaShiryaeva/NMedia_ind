package ru.netology.nmedia_ind.util

import kotlin.math.floor

fun validateText(number: Int): String {
    return when (number) {
        in 0..999 -> "$number"
        in 1_000..10_000 -> adjustNumberUnder10K(number) + "K"
        in 10_001..999_999 -> adjustNumberUnder1M(number) + "K"
        else -> adjustNumberOver1M(number) + "M"
    }
}

fun adjustNumberUnder10K(number: Int): String {
    val f = floor((number / 1_000.0) * 10) / 10
    return if ((f * 10).toInt() % 10 == 0) {
        floor(f).toInt().toString()
    } else f.toString()
}

fun adjustNumberUnder1M(number: Int): String {
    return floor((number / 1_000.0)).toInt().toString()
}

fun adjustNumberOver1M(number: Int): String {
    val f = floor((number / 1_000_000.0) * 10) / 10
    return if ((f * 10).toInt() % 10 == 0) {
        floor(f).toInt().toString()
    } else f.toString()
}