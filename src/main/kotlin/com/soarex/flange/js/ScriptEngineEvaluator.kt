package com.soarex.flange.js

import com.soarex.flange.Evaluator
import com.soarex.flange.ast.FLangeProgram
import javax.script.ScriptEngineManager

const val GLOBAL_CONTEXT = "global"

class ScriptEngineEvaluator(
    private val transpiler: JsTranspiler,
    private val context: Any = DefaultEvaluationContext()
) : Evaluator {
    override fun execute(program: FLangeProgram) {
        val engine = ScriptEngineManager()
            .getEngineByMimeType("text/javascript")

        val jsProgram = transpiler.transform(program)

        engine.createBindings()
        engine.put(GLOBAL_CONTEXT, context)
        engine.eval(jsProgram)
    }
}