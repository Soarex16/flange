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

expression
    : assocative_expression                                                                         #assoc_expr
    | comparison_expression                                                                         #comparison_expr
    | operator=OP_NOT expression                                                                    #unary_expr
    | <assoc=right> lhs=expression operator=OP_AND rhs=expression                                   #binary_expr
    | <assoc=right> lhs=expression operator=OP_OR rhs=expression                                    #binary_expr
    ;

assocative_expression
    : atom                                                                                          #atomic_expr
    | function_call_expression                                                                      #function_call_expr
    | <assoc=right> lhs=assocative_expression operator=POW rhs=assocative_expression                #assoc_binary_expr
    | operator=MINUS inner=assocative_expression                                                    #assoc_unary_expr
    | <assoc=left> lhs=assocative_expression operator=(MUL | DIV) rhs=assocative_expression         #assoc_binary_expr
    | <assoc=left> lhs=assocative_expression operator=(PLUS | MINUS) rhs=assocative_expression      #assoc_binary_expr
    // here be comparison
    | operator=OP_NOT inner=assocative_expression                                                   #assoc_unary_expr
    | <assoc=right> lhs=assocative_expression operator=OP_AND rhs=assocative_expression             #assoc_binary_expr
    | <assoc=right> lhs=assocative_expression operator=OP_OR rhs=assocative_expression              #assoc_binary_expr
    ;

comparison_expression
    : lhs=assocative_expression operator=(OP_LT | OP_GT | OP_EQ | OP_NE) rhs=assocative_expression
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