package com.soarex.flange

import com.github.ajalt.clikt.core.subcommands

fun main(args: Array<String>) = FLangeCLI().subcommands(FLangeToJSCompiler(), FLangeRuntime()).main(args)