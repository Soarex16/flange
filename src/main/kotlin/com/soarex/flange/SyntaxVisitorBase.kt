package com.soarex.flange

import com.soarex.flange.ast.*

open class SyntaxVisitorBase<T> : SyntaxVisitor<T> {
    override fun visit(node: SyntaxNode): T = node.accept(this)

    override fun visitProgram(node: FLangeProgram) = visitChildren(node)

    override fun visitStatement(node: Statement) = visitChildren(node)

    override fun visitCompoundStatement(node: CompoundStatement) = visitChildren(node)

    override fun visitFunctionCallStatement(node: FunctionCallStatement) = visitChildren(node)

    override fun visitAssignmentStatement(node: AssignmentStatement) = visitChildren(node)

    override fun visitConditionalStatement(node: ConditionalStatement) = visitChildren(node)

    override fun visitWhileStatement(node: WhileStatement) = visitChildren(node)

    override fun visitExpression(node: Expression) = visitChildren(node)

    override fun visitBinaryExpression(node: BinaryExpression) = visitChildren(node)

    override fun visitUnaryExpression(node: UnaryExpression) = visitChildren(node)

    override fun visitFunctionCallExpression(node: FunctionCallExpression) = visitChildren(node)

    override fun visitNumberLiteral(node: NumberLiteral) = visitChildren(node)

    override fun visitIdentifier(node: Identifier) = visitChildren(node)

    private fun visitChildren(node: SyntaxNode): T {
        node.children.forEach { visit(it) }
        return null as T
    }
}
