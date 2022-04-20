package com.soarex.flange.ast

import com.soarex.flange.SyntaxVisitor

data class CompoundStatement(override val children: List<Statement>): Statement {
    override fun <T> accept(visitor: SyntaxVisitor<T>): T {
        super.accept(visitor)
        return visitor.visitCompoundStatement(this)
    }
}