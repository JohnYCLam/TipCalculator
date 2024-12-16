package com.example.tipcalculator.util

fun calculateTotalTip(totalBill: String, tipPercentage: Int): Double {
    return if (totalBill.isEmpty())
        0.0
    else if (totalBill.toDouble() <= 1)
        0.0
    else
        totalBill.toDouble() * tipPercentage / 100
}

fun calculateTotalPerPerson(totalBill: String, splitBy: Int, tipPercentage: Int): Double {
    return if (totalBill.isEmpty()) {
        0.0
    }
    else {
        val bill = calculateTotalTip(totalBill, tipPercentage) + totalBill.toDouble()
        bill / splitBy
    }
}