package com.soarex.flange.ast

import com.soarex.flange.SyntaxVisitor

data class Identifier(val name: String) : Expression {
    override val children: List<SyntaxNode>
        get() = emptyList()

    override fun <T> accept(visitor: SyntaxVisitor<T>): T {
        super.accept(visitor)
        return visitor.visitIdentifier(this)
    }
}
