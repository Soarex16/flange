package com.soarex.flange

import com.soarex.flange.ast.*

interface SyntaxVisitor<T> {
    fun visit(node: SyntaxNode): T

    fun visitProgram(node: FLangeProgram): T

    fun visitStatement(node: Statement): T

    fun visitCompoundStatement(node: CompoundStatement): T

    fun visitFunctionCallStatement(node: FunctionCallStatement): T

    fun visitAssignmentStatement(node: AssignmentStatement): T

    fun visitConditionalStatement(node: ConditionalStatement): T

    fun visitWhileStatement(node: WhileStatement): T

    fun visitExpression(node: Expression): T

    fun visitBinaryExpression(node: BinaryExpression): T

    fun visitUnaryExpression(node: UnaryExpression): T

    fun visitFunctionCallExpression(node: FunctionCallExpression): T

    fun visitNumberLiteral(node: NumberLiteral): T

    fun visitIdentifier(node: Identifier): T
}