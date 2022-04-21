package com.soarex.flange.js

import com.soarex.flange.SyntaxVisitorBase
import com.soarex.flange.ast.*
import com.soarex.flange.indent

class JsTranspilerImpl: JsTranspiler {
    override fun transform(program: FLangeProgram, mapper: SymbolMapper): String {
        return FLangeToJsTransformer(mapper).visit(program)
    }
}

private class FLangeToJsTransformer(val mapper: SymbolMapper): SyntaxVisitorBase<String>() {
    private class VariableDeclarationsCollector: SyntaxVisitorBase<Unit>() {
        private val collectedDeclarations: MutableSet<String> = mutableSetOf()

        val declarations: Set<String>
            get() = collectedDeclarations

        override fun visitAssignmentStatement(node: AssignmentStatement) {
            collectedDeclarations.add(node.variable.name)
        }
    }

    override fun visitProgram(node: FLangeProgram): String {
        val declarationsCollector = VariableDeclarationsCollector().apply { visitProgram(node) }

        val declarationsBlock = declarationsCollector.declarations.joinToString("\n") { "var $it;" }
        val statements = node.statements.joinToString("\n") { visit(it) }
        return  """
            |(function() {
            |${indent(declarationsBlock)}
            |${indent(statements)}
            |})()
        """.trimMargin()
    }

    override fun visitCompoundStatement(node: CompoundStatement): String {
        val statements = node.children.joinToString("\n") { visit(it) }
        return if (statements.isBlank()) {
            "{ }"
        } else {
            """ 
                |{
                |${indent(statements)}
                |}
                """.trimMargin()
        }
    }

    override fun visitConditionalStatement(node: ConditionalStatement): String {
        val condition = visit(node.condition)

        val thenBranch = visit(node.thenBranch)

        val elseBranch = if (node.elseBranch != null)
            """ else ${visit(node.elseBranch)}"""
        else
            ""

        return "if ($condition) $thenBranch$elseBranch"
    }

    override fun visitWhileStatement(node: WhileStatement): String {
        val condition = visit(node.condition)

        val loopBody = visit(node.body)

        return "while ($condition) $loopBody"
    }

    override fun visitAssignmentStatement(node: AssignmentStatement): String {
        val varName = visit(node.variable)
        val varValue = visit(node.value)
        return "$varName = $varValue;"
    }

    override fun visitFunctionCallStatement(node: FunctionCallStatement): String {
        return visitFunctionCallExpression(node.function) + ";"
    }

    override fun visitFunctionCallExpression(node: FunctionCallExpression): String {
        val functionName = mapper(visit(node.functionName))
        val args = node.arguments.joinToString(", ") { visit(it) }
        return "${functionName}($args)"
    }

    override fun visitNumberLiteral(node: NumberLiteral): String {
        return node.value.toString()
    }

    override fun visitIdentifier(node: Identifier): String {
        return node.name
    }

    override fun visitBinaryExpression(node: BinaryExpression): String {
        val lhs = visit(node.left)
        val rhs = visit(node.right)
        val expr = transformBinaryExpression(node.operator, lhs, rhs)
        return "($expr)"
    }

    private fun transformBinaryExpression(op: BinaryOperator, l: String, r: String) = when(op) {
        BinaryOperator.POW -> "Math.pow($l, $r)"
        else -> "$l ${op.op} $r"
    }

    override fun visitUnaryExpression(node: UnaryExpression): String {
        val inner = visit(node.innerExpression)
        return "(${node.operator.op}${inner})"
    }
}