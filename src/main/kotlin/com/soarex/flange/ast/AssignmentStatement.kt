package com.soarex.flange.ast

import com.soarex.flange.SyntaxVisitor
import com.soarex.flange.indent

data class AssignmentStatement(val variable: Identifier, val value: Expression) : Statement {
    override val children: List<SyntaxNode>
        get() = listOf(variable, variable)

    override fun <T> accept(visitor: SyntaxVisitor<T>): T {
        super.accept(visitor)
        return visitor.visitAssignmentStatement(this)
    }

    override fun toString() = """
    |${javaClass.simpleName}(
    |${indent(variable.toString())}
    |${indent(value.toString())}
    |)""".trimMargin()
}
