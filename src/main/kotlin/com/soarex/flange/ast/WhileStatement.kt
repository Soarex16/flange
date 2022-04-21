package com.soarex.flange.ast

import com.soarex.flange.SyntaxVisitor
import com.soarex.flange.indent

data class WhileStatement(val condition: Expression, val body: CompoundStatement) : Statement {
    override val children: List<SyntaxNode>
        get() = listOfNotNull(condition, body)

    override fun <T> accept(visitor: SyntaxVisitor<T>): T {
        super.accept(visitor)
        return visitor.visitWhileStatement(this)
    }

    override fun toString() = """
    |${javaClass.simpleName}(
    |    while = (
    |${indent(condition.toString(), 2)}
    |    )
    |    do = (
    |${indent(body.toString(), 2)}
    |    )
    |)""".trimMargin()
}
