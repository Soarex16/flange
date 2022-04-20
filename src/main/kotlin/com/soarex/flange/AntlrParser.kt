package com.soarex.flange

import com.soarex.flange.ast.*
import com.soarex.flange.ex.UnknownOperatorException
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import com.soarex.flange.parser.FLangeLexer as AntlrFLangeLexer
import com.soarex.flange.parser.FLangeParser as AntlrFLangeParser
import com.soarex.flange.parser.FLangeParserBaseVisitor as AntlrFLangeParserBaseVisitor

private val BINARY_OPERATORS_TABLE: Map<String, BinaryOperator> = mapOf(
    "^" to BinaryOperator.POW,
    "*" to BinaryOperator.MUL,
    "/" to BinaryOperator.DIV,
    "+" to BinaryOperator.PLUS,
    "-" to BinaryOperator.MINUS,
    "<" to BinaryOperator.LESS,
    ">" to BinaryOperator.GREATER,
    "<=" to BinaryOperator.LESS_OR_EQUAL,
    ">=" to BinaryOperator.GREATER_OR_EQUAL,
    "==" to BinaryOperator.EQUAL,
    "/=" to BinaryOperator.NOT_EQUAL,
    "&&" to BinaryOperator.AND,
    "||" to BinaryOperator.OR,
)

private val UNARY_OPERATORS_TABLE: Map<String, UnaryOperator> = mapOf(
    "-" to UnaryOperator.MINUS,
    "!" to UnaryOperator.NOT
)

class FLangeAntlrParser: FLangeParser {
    override fun parse(input: String): FLangeProgram {
        val charsStream = CharStreams.fromString(input)
        val lexer = AntlrFLangeLexer(charsStream)
        val tokens = CommonTokenStream(lexer)
        val parser = AntlrFLangeParser(tokens)
        val rootNode = parser.program()
        return AntlrToFLangeVisitor().visitProgram(rootNode)
    }
}

private class AntlrToFLangeVisitor: AntlrFLangeParserBaseVisitor<SyntaxNode>() {
    override fun visitProgram(ctx: AntlrFLangeParser.ProgramContext): FLangeProgram {
        val statements = ctx.statement().map { visit(it) as Statement }
        return FLangeProgram(statements)
    }

    override fun visitCompound_statement(ctx: AntlrFLangeParser.Compound_statementContext): CompoundStatement {
        val statements = ctx.statement().map { visit(it) as Statement }
        return CompoundStatement(statements)
    }

    override fun visitFunction_call_statement(ctx: AntlrFLangeParser.Function_call_statementContext): FunctionCallStatement {
        val functionExpr = visit(ctx.function_call_expression()) as FunctionCallExpression
        return FunctionCallStatement(functionExpr)
    }

    override fun visitConditional_statement(ctx: AntlrFLangeParser.Conditional_statementContext): ConditionalStatement {
        val condition = visit(ctx.condition) as Expression
        val thenBranch = visit(ctx.then_statement) as CompoundStatement
        val elseBranch = visit(ctx.else_statement) as? CompoundStatement

        return ConditionalStatement(condition, thenBranch, elseBranch)
    }

    override fun visitWhile_loop_statement(ctx: AntlrFLangeParser.While_loop_statementContext): WhileStatement {
        val condition = visit(ctx.condition) as Expression
        val body = visit(ctx.body) as CompoundStatement
        return WhileStatement(condition, body)
    }

    override fun visitAssignment_statement(ctx: AntlrFLangeParser.Assignment_statementContext): AssignmentStatement {
        val variableName = visit(ctx.identifier()) as Identifier
        val value = visit(ctx.expression()) as Expression
        return AssignmentStatement(variableName, value)
    }

    override fun visitFunction_call_expression(ctx: AntlrFLangeParser.Function_call_expressionContext): FunctionCallExpression {
        val functionName = visit(ctx.identifier()) as Identifier
        val arguments = ctx.arguments.expression().map { visit(it) as Expression }

        return FunctionCallExpression(functionName, arguments)
    }

    override fun visitBinary_expression(ctx: AntlrFLangeParser.Binary_expressionContext): BinaryExpression {
        val lhs = visit(ctx.lhs) as Expression
        val rhs = visit(ctx.rhs) as Expression
        val operator = parseBinaryOperator(ctx.operator.text)
        return BinaryExpression(lhs, operator, rhs)
    }

    override fun visitUnary_expression(ctx: AntlrFLangeParser.Unary_expressionContext): UnaryExpression {
        val innerExpression = visit(ctx.expression()) as Expression
        val operator = parseUnaryOperator(ctx.operator.text)
        return UnaryExpression(operator, innerExpression)
    }

    private fun parseBinaryOperator(text: String) = BINARY_OPERATORS_TABLE[text]
        ?: throw UnknownOperatorException("Unknown binary operator: $text")

    private fun parseUnaryOperator(text: String) = UNARY_OPERATORS_TABLE[text]
        ?: throw UnknownOperatorException("Unknown unary operator: $text")

    override fun visitIdentifier(ctx: AntlrFLangeParser.IdentifierContext): Identifier {
        val name = ctx.IDENTIFIER().text
        return Identifier(name)
    }

    override fun visitNumber_literal(ctx: AntlrFLangeParser.Number_literalContext): NumberLiteral {
        val num = ctx.NUMBER_LITERAL().text.toInt()
        return NumberLiteral(num)
    }
}