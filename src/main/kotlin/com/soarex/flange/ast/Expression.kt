package com.soarex.flange.ast

import com.soarex.flange.SyntaxVisitor

interface Expression: SyntaxNode {
    override fun <T> accept(visitor: SyntaxVisitor<T>): T = visitor.visitExpression(this)
}
