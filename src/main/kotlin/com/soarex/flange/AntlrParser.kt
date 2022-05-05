package com.soarex.flange

import com.soarex.flange.ast.*
import com.soarex.flange.ex.UnknownOperatorException
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import com.soarex.flange.parser.FLangeLexer as AntlrFLangeLexer
import com.soarex.flange.parser.FLangeParser as AntlrFLangeParser
import com.soarex.flange.parser.FLangeParserBaseVisitor as AntlrFLangeParserBaseVisitor

class FLangeAntlrParser : FLangeParser {
    override fun parse(input: String): FLangeProgram {
        val charsStream = CharStreams.fromString(input)
        val lexer = AntlrFLangeLexer(charsStream)
        val tokens = CommonTokenStream(lexer)
        val parser = AntlrFLangeParser(tokens)
        val rootNode = parser.program()
        return AntlrToFlangeTranslator().visitProgram(rootNode)
    }
}

private class AntlrToFlangeTranslator(
    private val statementTranslator: AntlrToFlangeStatementTranslator = AntlrToFlangeStatementTranslator()
) : AntlrFLangeParserBaseVisitor<FLangeProgram>() {
    override fun visitProgram(ctx: AntlrFLangeParser.ProgramContext): FLangeProgram {
        val statements = ctx.statement().map { statementTranslator.visit(it) }
        return FLangeProgram(statements)
    }
}

private class AntlrToFlangeStatementTranslator(
    private val expressionTranslator: AntlrToFlangeExpressionTranslator = AntlrToFlangeExpressionTranslator()
) : AntlrFLangeParserBaseVisitor<Statement>() {
    override fun visitCompound_statement(ctx: AntlrFLangeParser.Compound_statementContext): CompoundStatement {
        val statements = ctx.statement().map { visit(it) as Statement }
        return CompoundStatement(statements)
    }

    override fun visitFunction_call_statement(ctx: AntlrFLangeParser.Function_call_statementContext): FunctionCallStatement {
        val functionExpr = expressionTranslator.visitFunction_call_expression(ctx.function_call_expression())
        return FunctionCallStatement(functionExpr)
    }

    override fun visitConditional_statement(ctx: AntlrFLangeParser.Conditional_statementContext): ConditionalStatement {
        val condition = expressionTranslator.visit(ctx.condition)
        val thenBranch = visitCompound_statement(ctx.then_statement)
        val elseBranch = if (ctx.else_statement != null) visitCompound_statement(ctx.else_statement) else null

        return ConditionalStatement(condition, thenBranch, elseBranch)
    }

    override fun visitWhile_loop_statement(ctx: AntlrFLangeParser.While_loop_statementContext): WhileStatement {
        val condition = expressionTranslator.visit(ctx.condition)
        val body = visitCompound_statement(ctx.body)
        return WhileStatement(condition, body)
    }

    override fun visitAssignment_statement(ctx: AntlrFLangeParser.Assignment_statementContext): AssignmentStatement {
        val variableName = expressionTranslator.visitIdentifier(ctx.identifier())
        val value = expressionTranslator.visit(ctx.expression())
        return AssignmentStatement(variableName, value)
    }
}

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

private class AntlrToFlangeExpressionTranslator : AntlrFLangeParserBaseVisitor<Expression>() {
    override fun visitFunction_call_expression(ctx: AntlrFLangeParser.Function_call_expressionContext): FunctionCallExpression {
        val functionName = visitIdentifier(ctx.identifier())
        val arguments = ctx.arguments.expression().map { visit(it) as Expression }

        return FunctionCallExpression(functionName, arguments)
    }

    override fun visitBinary_expr(ctx: com.soarex.flange.parser.FLangeParser.Binary_exprContext): BinaryExpression {
        val lhs = visit(ctx.lhs) as Expression
        val rhs = visit(ctx.rhs) as Expression
        val operator = parseBinaryOperator(ctx.operator.text)
        return BinaryExpression(lhs, operator, rhs)
    }

    override fun visitAssoc_binary_expr(ctx: com.soarex.flange.parser.FLangeParser.Assoc_binary_exprContext): BinaryExpression {
        val lhs = visit(ctx.lhs) as Expression
        val rhs = visit(ctx.rhs) as Expression
        val operator = parseBinaryOperator(ctx.operator.text)
        return BinaryExpression(lhs, operator, rhs)
    }

    override fun visitComparison_expression(ctx: com.soarex.flange.parser.FLangeParser.Comparison_expressionContext): BinaryExpression {
        val lhs = visit(ctx.lhs) as Expression
        val rhs = visit(ctx.rhs) as Expression
        val operator = parseBinaryOperator(ctx.operator.text)
        return BinaryExpression(lhs, operator, rhs)
    }

    override fun visitUnary_expr(ctx: com.soarex.flange.parser.FLangeParser.Unary_exprContext): UnaryExpression {
        val innerExpression = visit(ctx.expression()) as Expression
        val operator = parseUnaryOperator(ctx.operator.text)
        return UnaryExpression(operator, innerExpression)
    }

    override fun visitAssoc_unary_expr(ctx: com.soarex.flange.parser.FLangeParser.Assoc_unary_exprContext): UnaryExpression {
        val innerExpression = visit(ctx.inner) as Expression
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