package com.soarex.flange

import com.soarex.flange.ast.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import kotlin.test.assertEquals

internal class FLangeAntlrParserTest {

    @ParameterizedTest
    @MethodSource("parseTestData")
    fun parse(input: String, expectedResult: FLangeProgram) {
        val parser = FLangeAntlrParser()

        val actualResult = parser.parse(input)

        assertEquals(expectedResult, actualResult)
    }

    companion object {
        @JvmStatic
        fun parseTestData() = listOf(
            Arguments.of(
                "x = 4",
                FLangeProgram(
                    listOf<Statement>(
                        AssignmentStatement(
                            Identifier("x"),
                            NumberLiteral(4),
                        ),
                    ),
                ),
            ),
            Arguments.of(
                """
                    x = 4
                    if (x == 4) then {
                        x = x + 1
                    } else {
                        x = x - 1
                    }
                    print(x)
                """.trimIndent(),
                FLangeProgram(
                    listOf(
                        AssignmentStatement(
                            Identifier("x"),
                            NumberLiteral(4),
                        ),
                        ConditionalStatement(
                            BinaryExpression(
                                Identifier("x"),
                                BinaryOperator.EQUAL,
                                NumberLiteral(4),
                            ),
                            CompoundStatement(
                                listOf(
                                    AssignmentStatement(
                                        Identifier("x"),
                                        BinaryExpression(
                                            Identifier("x"),
                                            BinaryOperator.PLUS,
                                            NumberLiteral(1),
                                        ),
                                    )
                                )
                            ),
                            CompoundStatement(
                                listOf(
                                    AssignmentStatement(
                                        Identifier("x"),
                                        BinaryExpression(
                                            Identifier("x"),
                                            BinaryOperator.MINUS,
                                            NumberLiteral(1),
                                        ),
                                    )
                                )
                            ),
                        ),
                        FunctionCallStatement(
                            FunctionCallExpression(
                                Identifier("print"),
                                listOf(
                                    Identifier("x"),
                                )
                            )
                        )
                    )
                )
            ),
            Arguments.of(
                "x = ---4 + 2 - 3",
                FLangeProgram(
                    listOf<Statement>(
                        AssignmentStatement(
                            Identifier("x"),
                            BinaryExpression(
                                BinaryExpression(
                                    UnaryExpression(
                                        UnaryOperator.MINUS,
                                        UnaryExpression(
                                            UnaryOperator.MINUS,
                                            UnaryExpression(
                                                UnaryOperator.MINUS,
                                                NumberLiteral(4),
                                            ),
                                        ),
                                    ),
                                    BinaryOperator.PLUS,
                                    NumberLiteral(2),
                                ),
                                BinaryOperator.MINUS,
                                NumberLiteral(3),
                            ),
                        ),
                    ),
                ),
            ),
            Arguments.of(
                "x = 1 * 2 + 3",
                FLangeProgram(
                    listOf<Statement>(
                        AssignmentStatement(
                            Identifier("x"),
                            BinaryExpression(
                                BinaryExpression(
                                    NumberLiteral(1),
                                    BinaryOperator.MUL,
                                    NumberLiteral(2),
                                ),
                                BinaryOperator.PLUS,
                                NumberLiteral(3),
                            ),
                        ),
                    ),
                ),
            ),
            Arguments.of(
                "x = 1 + 2 - 3 * 4 / 5",
                FLangeProgram(
                    listOf<Statement>(
                        AssignmentStatement(
                            Identifier("x"),
                            BinaryExpression(
                                BinaryExpression(
                                    NumberLiteral(1),
                                    BinaryOperator.PLUS,
                                    NumberLiteral(2),
                                ),
                                BinaryOperator.MINUS,
                                BinaryExpression(
                                    BinaryExpression(
                                        NumberLiteral(3),
                                        BinaryOperator.MUL,
                                        NumberLiteral(4),
                                    ),
                                    BinaryOperator.DIV,
                                    NumberLiteral(5),
                                ),
                            ),
                        ),
                    ),
                ),
            ),
        )
    }
}