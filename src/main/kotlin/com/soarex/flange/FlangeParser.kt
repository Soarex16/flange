package com.soarex.flange

import com.soarex.flange.ast.FLangeProgram

interface FLangeParser {
    fun parse(input: String): FLangeProgram
}