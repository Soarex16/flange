package com.soarex.flange.ast

import com.soarex.flange.SyntaxVisitor

data class UnaryExpression(val operator: UnaryOperator, val innerExpression: Expression) : Expression {
    override val children: List<SyntaxNode>
        get() = listOf(innerExpression)

    override fun <T> accept(visitor: SyntaxVisitor<T>): T {
        super.accept(visitor)
        return visitor.visitUnaryExpression(this)
    }
}

enum class UnaryOperator(override val op: String): Operator {
    MINUS("-"),
    NOT("!")
}
