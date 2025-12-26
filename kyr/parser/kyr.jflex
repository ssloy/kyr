package kyr.parser;

import kyr.exceptions.*;
import java_cup.runtime.*;

%%

%class Lexer
%public

%line
%column

%type Symbol
%eofval{
        return symbol(Symbols.EOF);
%eofval}

%cup

%{
    private StringBuilder string = new StringBuilder();

    private Symbol symbol(int type) {
        return new Symbol(type, yyline, yycolumn);
    }

    private Symbol symbol(int type, Object value) {
        return new Symbol(type, yyline, yycolumn, value);
    }
%}

Digit = [0-9]
Integer = {Digit}+

LineTerminator = \r|\n|\r\n
WhiteSpace = {LineTerminator} | [ \t\f]

%state STRING

%%

/* String literal */

<YYINITIAL> {
  \"                  { yybegin(STRING); string.setLength(0); }
}

<STRING> {
  \"                  { yybegin(YYINITIAL); return symbol(Symbols.STRING, string.toString()); }

  [^\r\n\"\\]+        { string.append( yytext() ); }
  "\\b"               { string.append( '\b' ); }
  "\\t"               { string.append( '\t' ); }
  "\\n"               { string.append( '\n' ); }
  "\\f"               { string.append( '\f' ); }
  "\\r"               { string.append( '\r' ); }
  "\\\""              { string.append( '\"' ); }
  "\\'"               { string.append( '\'' ); }
  "\\\\"              { string.append( '\\' ); }

  \\.                 { throw new LexicalError("illegal escape sequence in line " + yyline + ", column " + yycolumn); }
  {LineTerminator}    { throw new LexicalError("unterminated string at end of line " + yyline); }
}

"//".*                { /* DO NOTHING */ }

"debut"               { return symbol(Symbols.BEGIN); }
"fin"                 { return symbol(Symbols.END); }
"ecrire"              { return symbol(Symbols.PRINT); }
";"                   { return symbol(Symbols.SEMICOLON); }

{Integer}             { return symbol(Symbols.INTEGER, yytext()); }
{WhiteSpace}          { }
.                     { throw new LexicalError("line " + yyline + ", column " + yycolumn + ": " + yytext()); }
