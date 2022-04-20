package com.soarex.flange.ast

import com.soarex.flange.SyntaxVisitor

data class FLangeProgram(val statements: List<Statement>) : SyntaxNode {
    override val children: List<SyntaxNode>
        get() = statements

    override fun <T> accept(visitor: SyntaxVisitor<T>): T = visitor.visitProgram(this)
}