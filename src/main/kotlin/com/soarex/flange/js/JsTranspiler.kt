package com.soarex.flange.js

import com.soarex.flange.ast.FLangeProgram

interface JsTranspiler {
    fun transform(program: FLangeProgram): String
}