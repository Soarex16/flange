package com.soarex.flange

import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ArgumentsSource
import kotlin.test.assertEquals

internal class FLangeAntlrParserTest {

    @ParameterizedTest
    @ArgumentsSource(ParserTestArgumentsProvider::class)
    fun parse(input: String, expectedAst: String) {
        val parser = FLangeAntlrParser()

        val actualAst = parser.parse(input).toString()

        assertEquals(expectedAst, actualAst)
    }
}