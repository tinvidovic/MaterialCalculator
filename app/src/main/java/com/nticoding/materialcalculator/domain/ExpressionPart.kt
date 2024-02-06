package com.nticoding.materialcalculator.domain

sealed interface ExpressionPart {
    data class Number(val number: Double): ExpressionPart
    data class Op(val operation: Operation): ExpressionPart
    data class Parentheses(val type: ParenthesesType): ExpressionPart
}

sealed interface ParenthesesType {
    data object Opening: ParenthesesType
    data object Closing: ParenthesesType
}