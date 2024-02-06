package com.nticoding.materialcalculator.domain

class ExpressionWriter {

    var expression = ""
    fun processAction(action: CalculatorAction) {
        when (action) {
            CalculatorAction.Calculate -> {
                val parser = ExpressionParser(prepareForCalculation())
                val evaluator = ExpressionEvaluator(parser.parse())
                expression = evaluator.evaluate().toString()
            }
            CalculatorAction.Clear -> {
                expression = ""
            }

            CalculatorAction.Decimal -> {
                if (canEnterDecimal())
                    expression += "."
            }
            CalculatorAction.Delete -> {
                expression.dropLast(1)
            }

            is CalculatorAction.Number -> {
                expression += action.number
            }

            is CalculatorAction.Op -> {
                if (canEnterOperation(action.operation)) {
                    expression += action.operation.symbol
                }
            }

            CalculatorAction.Parentheses -> {
                processParentheses()
            }
        }
    }

    private fun prepareForCalculation(): String {
        // Eliminate any trailing operations
        val newExpression = expression.takeLastWhile {
            it in "$operationSymbols(."
        }

        if (newExpression.isEmpty()) {
            // Nothing entered, return 0 as default
            return "0"
        }

        return newExpression
    }

    private fun processParentheses() {
        val openingCount = expression.count { it == '(' }
        val closingCount = expression.count { it == ')' }

        expression += when {
            expression.isEmpty() || expression.last() in "$operationSymbols(" -> "("
            expression.last() in "0123456788)" && openingCount == closingCount -> return
            else -> ")" // Need a closing parentheses
        }
    }

    private fun canEnterDecimal(): Boolean {
        if (expression.isEmpty() || expression.last() in "$operationSymbols.()") {
            return false
        }

        /*
        * Allow for 4+5.56
        * Reading digits from behind
         */
        return !expression.takeLastWhile {
            it in "0123456789."
        }.contains(".")
    }

    private fun canEnterOperation(operation: Operation): Boolean {
        if (operation in listOf(Operation.ADD, Operation.SUBTRACT)) {
            return expression.isEmpty() || expression.last() in "$operationSymbols()0123456789"
        }

        return expression.isNotEmpty() && expression.last() in "0123456789)"
    }
}