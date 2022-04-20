package com.soarex.flange.js

import com.soarex.flange.FLangeAntlrParser
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import kotlin.test.assertEquals

internal class JsTranspilerImplTest {

    @ParameterizedTest
    @MethodSource("transformTestData")
    fun transform(source: String, expectedResult: String) {
        val program = FLangeAntlrParser().parse(source)
        val transpiler: JsTranspiler = JsTranspilerImpl()

        val actualResult = transpiler.transform(program)
        assertEquals(expectedResult, actualResult)
    }

    companion object {
        @JvmStatic
        fun transformTestData() = listOf(
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
                """
                    (function() {
                        var x;
                        x = 4;
                        if ((x === 4)) {
                            x = (x + 1);
                        } else {
                            x = (x - 1);
                        }
                        print(x);
                    })()
                """.trimIndent()
            ),
            Arguments.of(
                """
                    x = 4
                    y = x
                    z = 44
                    abcde = x + y + z
                """.trimIndent(),
                """
                    (function() {
                        var x;
                        var y;
                        var z;
                        var abcde;
                        x = 4;
                        y = x;
                        z = 44;
                        abcde = ((x + y) + z);
                    })()
                """.trimIndent()
            ),
            Arguments.of(
                """
                    x = input()
                    while (x /= 0) {
                        y = x * 2,
                        print(y),
                        x = input()
                    }
                """.trimIndent(),
                """
                    (function() {
                        var x;
                        var y;
                        x = input();
                        while ((x != 0)) {
                            y = (x * 2);
                            print(y);
                            x = input();
                        }
                    })()
                """.trimIndent()
            )
        )
    }
}