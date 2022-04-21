package com.soarex.flange.ast

import com.soarex.flange.SyntaxVisitor
import com.soarex.flange.indent

data class CompoundStatement(override val children: List<Statement>) : Statement {
    override fun <T> accept(visitor: SyntaxVisitor<T>): T {
        super.accept(visitor)
        return visitor.visitCompoundStatement(this)
    }

    override fun toString(): String {
        val statements = children.joinToString(",\n") { it.toString() }
        return """
        |${javaClass.simpleName}(
        |${indent(statements)}
        |)""".trimMargin()
    }
}