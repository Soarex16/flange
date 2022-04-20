package com.soarex.flange.ast

import com.soarex.flange.SyntaxVisitor

data class FunctionCallStatement(val function: FunctionCallExpression): Statement {
    override val children: List<SyntaxNode>
        get() = listOf(function)

    override fun <T> accept(visitor: SyntaxVisitor<T>): T {
        super.accept(visitor)
        return visitor.visitFunctionCallStatement(this)
    }
}
