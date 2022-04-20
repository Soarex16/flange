package com.soarex.flange.ast

import com.soarex.flange.SyntaxVisitor

data class ConditionalStatement(
    val condition: Expression,
    val thenBranch: CompoundStatement,
    val elseBranch: CompoundStatement?
) : Statement {
    override val children: List<SyntaxNode>
        get() = listOfNotNull(condition, thenBranch, elseBranch)

    override fun <T> accept(visitor: SyntaxVisitor<T>): T {
        super.accept(visitor)
        return visitor.visitConditionalStatement(this)
    }
}
