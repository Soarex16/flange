package com.soarex.flange

import com.soarex.flange.ast.FLangeProgram

interface Evaluator {
    fun execute(program: FLangeProgram)
}