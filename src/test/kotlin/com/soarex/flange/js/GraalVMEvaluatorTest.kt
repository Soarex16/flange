package com.soarex.flange.js

import com.soarex.flange.Evaluator
import com.soarex.flange.FLangeAntlrParser
import com.soarex.flange.FLangeParser
import org.junit.jupiter.api.Test
import kotlin.test.assertContentEquals

internal class GraalVMEvaluatorTest {

    @Test
    fun execute() {
        val parser: FLangeParser = FLangeAntlrParser()
        val jsTranspiler: JsTranspiler = JsTranspilerImpl()

        val programOutput = mutableListOf<Int>()

        val evaluator: Evaluator = GraalVMEvaluator(
            jsTranspiler,
            object {
                fun print(obj: Int) {
                    programOutput.add(obj)
                }
            }
        )

        val source = """
            x = 3
            while (x /= 0) {
                print(x),
                x = x - 1
            }
        """.trimIndent()
        val program = parser.parse(source)
        evaluator.execute(program)

        assertContentEquals(listOf(3, 2, 1), programOutput)
    }
}