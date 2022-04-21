package com.soarex.flange.ast

import com.soarex.flange.SyntaxVisitor

data class NumberLiteral(val value: Int) : Expression {
    override val children: List<SyntaxNode>
        get() = emptyList()

    override fun <T> accept(visitor: SyntaxVisitor<T>): T {
        super.accept(visitor)
        return visitor.visitNumberLiteral(this)
    }

    override fun toString() = "${javaClass.simpleName}($value)"
}