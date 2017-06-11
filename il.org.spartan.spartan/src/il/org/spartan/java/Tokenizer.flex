/** 
 * A general purpose Java tokenizer,
 *  @author Yossi Gil
 *  @since 2007/04/02
 *  on 2012/10/10 bug fix to deal with the multiline - comment construct
 */
package il.org.spartan.java;
import static il.org.spartan.java.Token.*;
@SuppressWarnings("all")
%%
%public %class RawTokenizer
%type Token %function next
%unicode %pack %char %line %column
%xstate RESET SCAN_CODE SCAN_STRING_LITERAL SCAN_CHAR_LITERAL SCAN_LINE_COMMENT SCAN_DOC_COMMENT SCAN_BLOCK_COMMENT BLOCK_EOLN DOC_EOLN

%init{  
  reset(); 
%init}

%{
  public String text() { return $.length() >  0 ? $.toString() : yytext(); }
  public void error(String s) { System.err.println(notify(s)); reset(); }
  public String notify(String s) { return location() + s + " " + token(); }
  public String token() { return "<" + text() + ">"; } 
  public String location() { return "[" + line() + "," + column() + "]: " ; }
  public void reset() { truncate(); yybegin(SCAN_CODE); }
  public int line()   { return yyline + 1; }
  public int column() { return yycolumn + 1; }
  public int chars()  { return yychar + 1; }
  
  private void endIncluding() {extend(); end(); }
  private void endExcluding() {regret(); end(); }
  private void gotoExcluding(int state) {regret(); goTo(state); }
  private void extend() { $.append(yytext()); }
  private void regret() { yypushback(yylength()); }
  private void end() { goTo(RESET); }
  private void goTo(int state) { yybegin(state); }
  private void begin(int state) { truncate(); extend(); yybegin(state); }
  private void truncate() { $.setLength(0); }
  private StringBuffer $ = new StringBuffer();
%}

LineTerminator = \n|\r|\r\n
WhiteSpace = ([ \t\f])

Identifier = [:jletter:] [:jletterdigit:]*
DecimalNumeral = 0 | [1-9][0-9]*
HexNumeral = 0 [xX] [0-9a-fA-F]+
OctalNumeral = 0 [0-7]+
FloatingPointLiteral = {FloatingPart}  {ExponentPart}? | {DecimalNumeral} {ExponentPart}
FloatingPart =  [0-9]+ "." [0-9]*  | "." [0-9]+ 
ExponentPart = [eE] {SignedInteger}
SignedInteger = [-+]? [0-9]+
  
%%
<YYINITIAL,RESET> {
  .|{LineTerminator} { regret(); reset(); continue; }
  <<EOF>>            { return EOF; }
}

<SCAN_CODE> { 
  "/**/"             { return EMPTY_BLOCK_COMMENT;            }
  "/**"              { begin(SCAN_DOC_COMMENT); continue;     }
  "/*"               { begin(SCAN_BLOCK_COMMENT); continue;   }
  "//"               { begin(SCAN_LINE_COMMENT); continue;    }
  \"                 { begin(SCAN_STRING_LITERAL); continue;  }
  \'                 { begin(SCAN_CHAR_LITERAL); continue;    }
  "@interface"       { return AT_INTERFACE;  }
  @{Identifier}      { return ANNOTATION;    }
  {Identifier}       { try {
                         return Token.valueOf("_"+  yytext());
                       } catch (IllegalArgumentException e) {
                         return IDENTIFIER;
                       }
                     }

// Operators
  "("    { return LPAREN;    }
  ")"    { return RPAREN;    }
  "{"    { return LBRACE;    }
  "}"    { return RBRACE;    }
  "["    { return LBRACK;    }
  "]"    { return RBRACK;    }
  ";"    { return SEMICOLON; }
  ","    { return COMMA;     }
  "."    { return DOT;       }
  "="    { return EQ;         }
  ">"    { return GT;         }
  "<"    { return LT;         }
  "!"    { return NOT;        }
  "~"    { return COMP;       }
  "?"    { return QUESTION;   }
  ":"    { return COLON;      }
  "=="   { return EQEQ;       }
  "<="   { return LTEQ;       }
  ">="   { return GTEQ;       }
  "!="   { return NOTEQ;      }
  "&&"   { return ANDAND;     }
  "||"   { return OROR;       }
  "++"   { return PLUSPLUS;   }
  "--"   { return MINUSMINUS; }
  "+"    { return PLUS;       }
  "-"    { return MINUS;      }
  "*"    { return MULT;       }
  "/"    { return DIV;        }
  "&"    { return AND;        }
  "|"    { return OR;         }
  "^"    { return XOR;        }
  "%"    { return MOD;        }
  "<<"   { return LSHIFT;     }
  ">>"   { return RSHIFT;     }
  ">>>"  { return URSHIFT;    }
  "+="   { return PLUSEQ;     }
  "-="   { return MINUSEQ;    }
  "*="   { return MULTEQ;     }
  "/="   { return DIVEQ;      }
  "&="   { return ANDEQ;      }
  "|="   { return OREQ;       }
  "^="   { return XOREQ;      }
  "%="   { return MODEQ;      }
  "<<="  { return LSHIFTEQ;   }
  ">>="  { return RSHIFTEQ;   }
  ">>>=" { return URSHIFTEQ;  }

// Integral Literals 
  {OctalNumeral} [lL]          { return INTEGER_LITERAL; }
  {HexNumeral} [lL]            { return INTEGER_LITERAL; }
  {DecimalNumeral} [lL]        { return INTEGER_LITERAL; }
  {OctalNumeral}               { return INTEGER_LITERAL; }
  {HexNumeral}                 { return INTEGER_LITERAL; }
  {DecimalNumeral}             { return INTEGER_LITERAL; }
  
// Floating point literals
  {FloatingPointLiteral} [fF]  { return FLOAT_LITERAL; }
  {DecimalNumeral} [fF]        { return FLOAT_LITERAL; }
  {FloatingPointLiteral} [dD]  { return DOUBLE_LITERAL; }
  {DecimalNumeral} [dD]        { return DOUBLE_LITERAL; }
  {FloatingPointLiteral}       { return DOUBLE_LITERAL; }
  
// White space
  {WhiteSpace}                 { return SPACE; }
  {LineTerminator}             { return NL; }
  
// Fall-through case: anything not matched above is an error 
  .                            { return UNKNOWN_CHARACTER; }
  <<EOF>>                      { return EOF; }
}

<SCAN_STRING_LITERAL> {
  {LineTerminator} { endExcluding(); return UNTERMINATED_STRING_LITERAL; }
  \"               { endIncluding(); return STRING_LITERAL; }
  \\\" | \\\\      { extend(); continue; }
  .                { extend(); continue; }
  <<EOF>>          { regret(); end(); return UNTERMINATED_STRING_LITERAL; }
}

<SCAN_CHAR_LITERAL> {
  {LineTerminator} { endExcluding(); return UNTERMINATED_CHARACTER_LITERAL; }
  \'               { endIncluding(); return CHARACTER_LITERAL; }
  \\\' | \\\\      { extend(); continue; }
  .                { extend(); continue; }
  <<EOF>>          { endExcluding(); return UNTERMINATED_CHARACTER_LITERAL; }
}

<SCAN_LINE_COMMENT> {
  {LineTerminator} { endExcluding(); return LINE_COMMENT; }
  .                { extend(); continue; }
  <<EOF>>          { endExcluding(); return LINE_COMMENT; }
}

<SCAN_BLOCK_COMMENT> {
  {LineTerminator} { gotoExcluding(BLOCK_EOLN); return PARTIAL_BLOCK_COMMENT; }
  "*/"             { endIncluding(); return BLOCK_COMMENT;}
  .                { extend(); continue; }
  <<EOF>>          { endExcluding(); return UNTERMINATED_BLOCK_COMMENT; }
}

<BLOCK_EOLN> {
  {LineTerminator} { truncate(); goTo(SCAN_BLOCK_COMMENT); return NL_BLOCK_COMMENT; }
}

<SCAN_DOC_COMMENT> {
  {LineTerminator} { gotoExcluding(DOC_EOLN); return PARTIAL_DOC_COMMENT; }
  "*/"             { endIncluding(); return DOC_COMMENT; }
  .                { extend(); continue; }
  <<EOF>>          { endExcluding(); return UNTERMINATED_DOC_COMMENT; }
}

<DOC_EOLN> {
  {LineTerminator} { truncate(); goTo(SCAN_DOC_COMMENT); return NL_DOC_COMMENT; }
}

