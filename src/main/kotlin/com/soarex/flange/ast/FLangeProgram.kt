package com.soarex.flange.ast

import com.soarex.flange.SyntaxVisitor
import com.soarex.flange.indent

data class FLangeProgram(val statements: List<Statement>) : SyntaxNode {
    override val children: List<SyntaxNode>
        get() = statements

    override fun <T> accept(visitor: SyntaxVisitor<T>): T = visitor.visitProgram(this)

    override fun toString(): String {
        val statementsString = statements.joinToString(",\n") { it.toString() }
        return """
            |${javaClass.simpleName}(
            |    statements = (
            |${indent(statementsString, 2)}
            |    )
            |)""".trimMargin()
    }
}