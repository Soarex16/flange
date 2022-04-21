package com.soarex.flange.ast

import com.soarex.flange.SyntaxVisitor
import com.soarex.flange.indent

data class FunctionCallExpression(val functionName: Identifier, val arguments: List<Expression>) : Expression {
    override val children: List<SyntaxNode>
        get() = listOf(functionName) + arguments

    override fun <T> accept(visitor: SyntaxVisitor<T>): T {
        super.accept(visitor)
        return visitor.visitFunctionCallExpression(this)
    }

    override fun toString(): String {
        val args = arguments.joinToString(",\n") { it.toString() }

        val argsPart = if (args.isBlank())
            """
            |arguments = (
            |)""".trimMargin()
        else
            """
            |arguments = (
            |${indent(args)}
            |)""".trimMargin()

        return """
            |${javaClass.simpleName}(
            |    $functionName
            |${indent(argsPart)}
            |)""".trimMargin()
    }
}
