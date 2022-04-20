package com.soarex.flange.js

class DefaultEvaluationContext {
    fun input(): Int {
        while (true) {
            val input = readln().toIntOrNull()
            if (input != null)
                return input
        }
    }

    fun print(obj: Any) {
        println(obj)
    }
}