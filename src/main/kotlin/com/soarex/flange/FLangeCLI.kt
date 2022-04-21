package com.soarex.flange

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.defaultStdout
import com.github.ajalt.clikt.parameters.types.inputStream
import com.github.ajalt.clikt.parameters.types.outputStream
import com.soarex.flange.js.JsTranspiler
import com.soarex.flange.js.JsTranspilerImpl
import com.soarex.flange.js.GraalVMEvaluator

class FLangeCLI : CliktCommand(printHelpOnEmptyArgs = true) {
    override fun run() = Unit
}

class FLangeToJSCompiler: CliktCommand(name = "compile", help = "Transpiles FLange code to JS") {
    private val input by argument("file", "Input file")
        .inputStream()

    private val output by option("-o", "--output", metavar = "Output file")
        .outputStream(
            createIfNotExist = true,
            truncateExisting = true
        )
        .defaultStdout()

    private val parser: FLangeParser = FLangeAntlrParser()
    private val transpiler: JsTranspiler = JsTranspilerImpl()

    override fun run() {
        val inputProgram = input.bufferedReader().readText()
        val program = parser.parse(inputProgram)
        val jsCode = transpiler.transform(program)
        output.bufferedWriter().use {
            it.write(jsCode)
        }
    }
}

class FLangePrintAst: CliktCommand(name = "ast", help = "Dumps AST of the FLange program") {
    private val input by argument("file", "Input file")
        .inputStream()

    private val output by option("-o", "--output", metavar = "Output file")
        .outputStream(
            createIfNotExist = true,
            truncateExisting = true
        )
        .defaultStdout()

    private val parser: FLangeParser = FLangeAntlrParser()

    override fun run() {
        val inputProgram = input.bufferedReader().readText()
        val program = parser.parse(inputProgram)
        output.bufferedWriter().use {
            it.write(program.toString())
        }
    }
}

class FLangeRuntime: CliktCommand(name = "run", help = "Performs FLange code evaluation") {
    private val input by argument("file", "Input file")
        .inputStream()

    private val parser: FLangeParser = FLangeAntlrParser()
    private val transpiler: JsTranspiler = JsTranspilerImpl()
    private val evaluator: Evaluator = GraalVMEvaluator(transpiler)

    override fun run() {
        val inputProgram = input.bufferedReader().readText()
        val program = parser.parse(inputProgram)
        evaluator.execute(program)
    }
}
