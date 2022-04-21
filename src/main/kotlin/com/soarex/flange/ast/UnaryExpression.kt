package com.soarex.flange.ast

import com.soarex.flange.SyntaxVisitor
import com.soarex.flange.indent

data class UnaryExpression(val operator: UnaryOperator, val innerExpression: Expression) : Expression {
    override val children: List<SyntaxNode>
        get() = listOf(innerExpression)

    override fun <T> accept(visitor: SyntaxVisitor<T>): T {
        super.accept(visitor)
        return visitor.visitUnaryExpression(this)
    }

    override fun toString() = """
    |${javaClass.simpleName}(
    |    op = $operator
    |    expr = (
    |${indent(innerExpression.toString(), 2)}
    |    )
    |)""".trimMargin()
}

enum class UnaryOperator(override val op: String): Operator {
    MINUS("-"),
    NOT("!")
}
