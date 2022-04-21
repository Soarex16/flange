package com.soarex.flange.js

import com.soarex.flange.ast.FLangeProgram

typealias SymbolMapper = (String) -> String

interface JsTranspiler {
    fun transform(program: FLangeProgram, mapper: SymbolMapper = defaultMapper): String
}

val defaultMapper: SymbolMapper = { it }