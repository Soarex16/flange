package com.soarex.flange.ast

import com.soarex.flange.SyntaxVisitor

interface Statement: SyntaxNode {
    override fun <T> accept(visitor: SyntaxVisitor<T>): T = visitor.visitStatement(this)
}