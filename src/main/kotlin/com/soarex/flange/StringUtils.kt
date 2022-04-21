package com.soarex.flange

const val INDENT_SIZE = 4

fun indent(code: String, step: Int = 1): String {
    val indent = " ".repeat(step * INDENT_SIZE)
    return code.prependIndent(indent)
}