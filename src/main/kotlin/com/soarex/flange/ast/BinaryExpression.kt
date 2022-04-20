package com.soarex.flange.ast

import com.soarex.flange.SyntaxVisitor

data class BinaryExpression(val left: Expression, val operator: BinaryOperator, val right: Expression) : Expression {
    override val children: List<SyntaxNode>
        get() = listOf(left, right)

    override fun <T> accept(visitor: SyntaxVisitor<T>): T {
        super.accept(visitor)
        return visitor.visitBinaryExpression(this)
    }
}

enum class BinaryOperator(override val op: String): Operator {
    POW("^"),
    MUL("*"),
    DIV("/"),
    PLUS("+"),
    MINUS("-"),
    LESS("<"),
    GREATER(">"),
    LESS_OR_EQUAL("<="),
    GREATER_OR_EQUAL(">="),
    EQUAL("==="),
    NOT_EQUAL("!="),
    AND("&&"),
    OR("||")
}
