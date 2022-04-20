package com.soarex.flange.ast

import com.soarex.flange.SyntaxVisitor

data class AssignmentStatement(val variable: Identifier, val value: Expression) : Statement {
    override val children: List<SyntaxNode>
        get() = listOf(variable, variable)

    override fun <T> accept(visitor: SyntaxVisitor<T>): T {
        super.accept(visitor)
        return visitor.visitAssignmentStatement(this)
    }
}
