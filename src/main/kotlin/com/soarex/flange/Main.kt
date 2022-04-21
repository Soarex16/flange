package com.soarex.flange

import com.github.ajalt.clikt.core.subcommands

fun main(args: Array<String>) = FLangeCLI()
    .subcommands(FLangeToJSCompiler(), FLangePrintAst(), FLangeRuntime())
    .main(args)