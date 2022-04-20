parser grammar FLangeParser;

@header {
    package com.soarex.flange.parser;
}

options {
  tokenVocab=FLangeLexer;
}

program
    : statement+ EOF
    ;

statement
    : conditional_statement
    | compound_statement
    | while_loop_statement
    | function_call_statement
    | assignment_statement
    ;

compound_statement
    : OPEN_BRACE (statement? | statement (COMMA statement)*) CLOSE_BRACE
    ;

assignment_statement
    : identifier ASSIGNMENT expression
    ;

conditional_statement
    : IF OPEN_PARENS condition=expression CLOSE_PARENS THEN
    then_statement=compound_statement (ELSE else_statement=compound_statement)?
    ;

while_loop_statement
    : WHILE OPEN_PARENS condition=expression CLOSE_PARENS
    body=compound_statement
    ;

function_call_statement
    : function_call_expression
    ;

//expression
//    : expression_1
//    ;

// TODO: remove logical operators associaitivity
expression
    : atom                                                                      #atomic
    | function_call_expression                                                  #function_call
    | <assoc=right> lhs=expression operator=POW rhs=expression                  #binary_expression
    | operator=MINUS expression                                                 #unary_expression
    | <assoc=left> lhs=expression operator=(MUL | DIV) rhs=expression           #binary_expression
    | <assoc=left> lhs=expression operator=(PLUS | MINUS) rhs=expression        #binary_expression
    | lhs=expression operator=(OP_LT | OP_GT | OP_EQ | OP_NE) rhs=expression    #binary_expression
    | operator=OP_NOT expression                                                #unary_expression
    | <assoc=right> lhs=expression operator=OP_AND rhs=expression               #binary_expression
    | <assoc=right> lhs=expression operator=OP_OR rhs=expression                #binary_expression
    ;

function_call_expression
    : identifier OPEN_PARENS arguments=parameters_list CLOSE_PARENS
    ;

parameters_list
    : expression?
    | expression (COMMA expression)*
    ;

atom
    : OPEN_PARENS expression CLOSE_PARENS               #parens
    | number_literal                                    #number
    | identifier                                        #symbol
    ;

identifier
    : IDENTIFIER
    ;

number_literal
    : NUMBER_LITERAL
    ;