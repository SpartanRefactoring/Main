package il.org.spartan.java;

import static il.org.spartan.java.Token.Category.*;
import static il.org.spartan.java.Token.Kind.*;

import java.io.*;

import il.org.spartan.utils.*;

public enum Token {
  // Literals:
  __true, __false, __null,
  // Primitive__types:
  __boolean, __byte, __short, __int, __long, __char, __float, __double, __void, __package, // package__declaration
  __import, // import__declaration
  // Modifiers:
  __public, __protected, __private, __static, __abstract, __final, __native, __synchronized, __transient, __volatile, __strictfp,
  // Type generators:
  __class, __interface, __enum, __extends, __implements, __throws, __this, __super, // explicit__constructor__invocation
  // Control flow:
  __if, __else, __switch, __case, __default, __do, __while, __for, __break, __continue, __return, __throw, __try, __catch, __finally,
  // Operators:
  __instanceof, __new, __assert, __const, __goto, AT__INTERFACE(KEYWORD), ANNOTATION(ID),
  // Other identifiers:
  IDENTIFIER(ID),
  // Punctuation:
  LBRACK(PUNCTUATION), RBRACK(PUNCTUATION), // Brackets
  LBRACE(PUNCTUATION), RBRACE(PUNCTUATION), // Braces
  LPAREN(PUNCTUATION), RPAREN(PUNCTUATION), // Parenthesis
  DOT(PUNCTUATION), // qualified__name
  COLON(PUNCTUATION), // As in switch, loops and goto labels
  SEMICOLON(PUNCTUATION), // Terminator
  COMMA(PUNCTUATION), // Separators
  // Operators:
  MULT(OPERATOR), //
  EQ(OPERATOR), //
  PLUSPLUS(OPERATOR), // postincrement__expression
  MINUSMINUS(OPERATOR), // postdecrement__expression
  COMP(OPERATOR), NOT(OPERATOR), // unary operators
  PLUS(OPERATOR), MINUS(OPERATOR), DIV(OPERATOR), MOD(OPERATOR), // arithmetical
  LSHIFT(OPERATOR), RSHIFT(OPERATOR), URSHIFT(OPERATOR), // shift
  LT(OPERATOR), GT(OPERATOR), LTEQ(OPERATOR), GTEQ(OPERATOR), INSTANCEOF(OPERATOR), // relational
  EQEQ(OPERATOR), NOTEQ(OPERATOR), // equality__expression
  AND(OPERATOR), // and__expression
  XOR(OPERATOR), // exclusive__or__expression
  OR(OPERATOR), // inclusive__or__expression
  ANDAND(OPERATOR), // conditional__and__expression
  OROR(OPERATOR), // conditional__or__expression
  QUESTION(OPERATOR), // conditional__expression
  MULTEQ(OPERATOR), DIVEQ(OPERATOR), MODEQ(OPERATOR), PLUSEQ(OPERATOR), MINUSEQ(OPERATOR), // arithmetical
  // assignment operators
  LSHIFTEQ(OPERATOR), RSHIFTEQ(OPERATOR), URSHIFTEQ(OPERATOR), // shift
  // assignment__operator
  ANDEQ(OPERATOR), XOREQ(OPERATOR), OREQ(OPERATOR), // bit operations
  // assignment__operator
  INTEGER__LITERAL(LITERAL), LONG__LITERAL(LITERAL), // Integral literals
  DOUBLE__LITERAL(LITERAL), FLOAT__LITERAL(LITERAL), // Floating point
  // string literals
  CHARACTER__LITERAL(LITERAL), //
  UNTERMINATED__CHARACTER__LITERAL(LITERAL, true), //
  STRING__LITERAL(LITERAL), //
  UNTERMINATED__STRING__LITERAL(LITERAL, true), //
  // Ordinary comments
  LINE__COMMENT(COMMENT), // Includes the entire comment.
  EMPTY__BLOCK__COMMENT(COMMENT), // That is, the string "/**/"
  BLOCK__COMMENT(COMMENT), // Includes just the last line of the block comment.
  DOC__COMMENT(COMMENT), // Includes just the last line of the DOC comment.
  PARTIAL__BLOCK__COMMENT(COMMENT), // Returned for each line, except the last
                                    // in
  // a block comment.
  PARTIAL__DOC__COMMENT(COMMENT), // Returned for each line, except the last in
                                  // a
  // doc comment.
  /** Returned for each new line occurring within a block comment: */
  NL__BLOCK__COMMENT(COMMENT),
  /** Returned for each new line occurring within a doc comment. */
  NL__DOC__COMMENT(COMMENT),
  // Error comments:
  UNTERMINATED__BLOCK__COMMENT(COMMENT, true), //
  UNTERMINATED__DOC__COMMENT(COMMENT, true), //
  // Specials:
  UNKNOWN__CHARACTER(NONCODE, true), //
  EOF(NONCODE), //
  NL(NONCODE), //
  SPACE(NONCODE),;
  public static void main(final String argv[]) throws IOException {
    main(new RawTokenizer(System.in));
  }

  private static void main(final RawTokenizer tokenizer) throws IOException {
    for (;;) {
      final Token t = tokenizer.next();
      System.out.println(Separate.bySpaces(//
          "t=" + t, //
          "text=" + tokenizer.text(), //
          "kind=" + t.kind, //
          "Category=" + t.kind.category, //
          "error=" + t.isError)//
      );
      if (t == EOF)
        return;
    }
  }

  public final Kind kind;
  protected final boolean isError;

  private Token() {
    this(KEYWORD);
  }

  private Token(final Kind kind) {
    this(kind, false);
  }

  private Token(final Kind kind, final boolean isError) {
    this.kind = kind;
    this.isError = isError;
  }

  public final boolean isError() {
    return isError;
  }

  public boolean isNL() {
    return this == NL || this == NL__BLOCK__COMMENT || this == NL__DOC__COMMENT;
  }

  public enum Category {
    ALPHANUMERIC, PUNCTUATIONAL, IGNORE, ANY;
  }

  public enum Kind {
    KEYWORD(ALPHANUMERIC), //
    ID(ALPHANUMERIC), //
    OPERATOR(PUNCTUATIONAL), //
    PUNCTUATION(PUNCTUATIONAL), //
    LITERAL(ANY), //
    COMMENT(IGNORE), //
    NONCODE(IGNORE);
    public final Category category;

    private Kind(final Category category) {
      this.category = category;
    }
  }
}
