package com.soarex.flange.ast

import com.soarex.flange.SyntaxVisitor

data class FunctionCallExpression(val functionName: Identifier, val arguments: List<Expression>) : Expression {
    override val children: List<SyntaxNode>
        get() = listOf(functionName) + arguments

    override fun <T> accept(visitor: SyntaxVisitor<T>): T {
        super.accept(visitor)
        return visitor.visitFunctionCallExpression(this)
    }
}
