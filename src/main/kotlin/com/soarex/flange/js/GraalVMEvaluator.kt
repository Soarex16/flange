package com.soarex.flange.js

import com.soarex.flange.Evaluator
import com.soarex.flange.ast.FLangeProgram
import javax.script.Bindings
import javax.script.ScriptContext
import javax.script.ScriptEngine
import javax.script.ScriptEngineManager


const val GLOBAL_CONTEXT = "global"

class GraalVMEvaluator(
    private val transpiler: JsTranspiler,
    private val context: Any = DefaultEvaluationContext()
) : Evaluator {
    override fun execute(program: FLangeProgram) {
        val jsProgram = transpiler.transform(program) {
            "$GLOBAL_CONTEXT.$it"
        }

        val engine: ScriptEngine = ScriptEngineManager().getEngineByName("js")
        val bindings: Bindings = engine.getBindings(ScriptContext.ENGINE_SCOPE)
        bindings["polyglot.js.allowAllAccess"] = true
        engine.put(GLOBAL_CONTEXT, context)

        try {
            engine.eval(jsProgram)
        } catch (e: Exception) {
            throw RuntimeException("Code execution exception", e)
        }
    }
}