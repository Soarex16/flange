package com.soarex.flange.ast

import com.soarex.flange.SyntaxVisitor

data class WhileStatement(val condition: Expression, val body: CompoundStatement) : Statement {
    override val children: List<SyntaxNode>
        get() = listOfNotNull(condition, body)

    override fun <T> accept(visitor: SyntaxVisitor<T>): T {
        super.accept(visitor)
        return visitor.visitWhileStatement(this)
    }
}
