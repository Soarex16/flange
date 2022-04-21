package com.soarex.flange.ast

import com.soarex.flange.SyntaxVisitor
import com.soarex.flange.indent

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

    override fun toString() = """
    |${javaClass.simpleName}(
    |    if = (
    |${indent(condition.toString(), 2)}
    |    )
    |    then = (
    |${indent(thenBranch.toString(), 2)}
    |    )
    |    else = (
    |${indent(elseBranch.toString(), 2)}
    |    )
    |)""".trimMargin()
}
