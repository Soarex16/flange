package com.soarex.flange.ast

import com.soarex.flange.SyntaxVisitor

interface SyntaxNode {
    val children: List<SyntaxNode>

    fun <T> accept(visitor: SyntaxVisitor<T>): T
}