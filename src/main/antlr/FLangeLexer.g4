lexer grammar FLangeLexer;

@header {
    package com.soarex.flange.parser;
}

channels {
  WSCHANNEL,
  COMMENTS
}

LINE_COMMENT: '\\' INPUT_CHARACTER* -> channel(COMMENTS);

// Punctuations
OPEN_PARENS:              '(';
CLOSE_PARENS:             ')';
OPEN_BRACE:               '{';
CLOSE_BRACE:              '}';
COMMA:                    ',';
SEMICOLON:                ';';

// Operators
PLUS:                     '+';
MINUS:                    '-';
MUL:                      '*';
DIV:                      '/';
POW:                      '^';
ASSIGNMENT:               '=';

OP_AND:                   '&&';
OP_OR:                    '||';
OP_LT:                    '<';
OP_GT:                    '>';
OP_NOT:                   '!';
OP_EQ:                    '==';
OP_NE:                    '/=';
OP_LE:                    '<=';
OP_GE:                    '>=';

// Keywords
IF:     'if';
THEN:   'then';
ELSE:   'else';
WHILE:  'while';

// Numbers and symbols
NUMBER_LITERAL:     DIGIT+;
IDENTIFIER:         (UNDERSCORE | ALPHA) (UNDERSCORE | ALPHA_NUM)*;

WHITESPACES: (NEW_LINE | WHITESPACE)+ -> channel(WSCHANNEL);

// Fragments
fragment ALPHA: [a-zA-Z];
fragment DIGIT: [0-9];
fragment ALPHA_NUM: ALPHA | DIGIT;
fragment UNDERSCORE: '_';

fragment NEW_LINE:              '\r\n' | '\r' | '\n';
fragment WHITESPACE:            [ \t];
fragment INPUT_CHARACTER:       ~[\r\n\u0085\u2028\u2029];