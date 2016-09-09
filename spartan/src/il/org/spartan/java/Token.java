package il.org.spartan.java;

import static il.org.spartan.java.Token.Category.*;
import static il.org.spartan.java.Token.Kind.*;

import java.io.*;

import il.org.spartan.utils.*;

public enum Token {
  // Literals:
  _true, _false, _null,
  // Primitive_types:
  _boolean, _byte, _short, _int, _long, _char, _float, _double, _void, _package, // package_declaration
  _import, // import_declaration
  // Modifiers:
  _public, _protected, _private, _static, _abstract, _final, _native, _synchronized, _transient, _volatile, _strictfp,
  // Type generators:
  _class, _interface, _enum, _extends, _implements, _throws, _this, _super, // explicit_constructor_invocation
  // Control flow:
  _if, _else, _switch, _case, _default, _do, _while, _for, _break, _continue, _return, _throw, _try, _catch, _finally,
  // Operators:
  _instanceof, _new, _assert, _const, _goto, AT_INTERFACE(KEYWORD), ANNOTATION(ID),
  // Other identifiers:
  IDENTIFIER(ID),
  // Punctuation:
  LBRACK(PUNCTUATION), RBRACK(PUNCTUATION), // Brackets
  LBRACE(PUNCTUATION), RBRACE(PUNCTUATION), // Braces
  LPAREN(PUNCTUATION), RPAREN(PUNCTUATION), // Parenthesis
  DOT(PUNCTUATION), // qualified_name
  COLON(PUNCTUATION), // As in switch, loops and goto labels
  SEMICOLON(PUNCTUATION), // Terminator
  COMMA(PUNCTUATION), // Separators
  // Operators:
  MULT(OPERATOR), //
  EQ(OPERATOR), //
  PLUSPLUS(OPERATOR), // postincrement_expression
  MINUSMINUS(OPERATOR), // postdecrement_expression
  COMP(OPERATOR), NOT(OPERATOR), // unary operators
  PLUS(OPERATOR), MINUS(OPERATOR), DIV(OPERATOR), MOD(OPERATOR), // arithmetical
  LSHIFT(OPERATOR), RSHIFT(OPERATOR), URSHIFT(OPERATOR), // shift
  LT(OPERATOR), GT(OPERATOR), LTEQ(OPERATOR), GTEQ(OPERATOR), INSTANCEOF(OPERATOR), // relational
  EQEQ(OPERATOR), NOTEQ(OPERATOR), // equality_expression
  AND(OPERATOR), // and_expression
  XOR(OPERATOR), // exclusive_or_expression
  OR(OPERATOR), // inclusive_or_expression
  ANDAND(OPERATOR), // conditional_and_expression
  OROR(OPERATOR), // conditional_or_expression
  QUESTION(OPERATOR), // conditional_expression
  MULTEQ(OPERATOR), DIVEQ(OPERATOR), MODEQ(OPERATOR), PLUSEQ(OPERATOR), MINUSEQ(OPERATOR), // arithmetical
  // assignment operators
  LSHIFTEQ(OPERATOR), RSHIFTEQ(OPERATOR), URSHIFTEQ(OPERATOR), // shift
  // assignment_operator
  ANDEQ(OPERATOR), XOREQ(OPERATOR), OREQ(OPERATOR), // bit operations
  // assignment_operator
  INTEGER_LITERAL(LITERAL), LONG_LITERAL(LITERAL), // Integral literals
  DOUBLE_LITERAL(LITERAL), FLOAT_LITERAL(LITERAL), // Floating point
  // string literals
  CHARACTER_LITERAL(LITERAL), //
  UNTERMINATED_CHARACTER_LITERAL(LITERAL, true), //
  STRING_LITERAL(LITERAL), //
  UNTERMINATED_STRING_LITERAL(LITERAL, true), //
  // Ordinary comments
  LINE_COMMENT(COMMENT), // Includes the entire comment.
  EMPTY_BLOCK_COMMENT(COMMENT), // That is, the string "/**/"
  BLOCK_COMMENT(COMMENT), // Includes just the last line of the block comment.
  DOC_COMMENT(COMMENT), // Includes just the last line of the DOC comment.
  PARTIAL_BLOCK_COMMENT(COMMENT), // Returned for each line, except the last in
                                  // a block comment.
  PARTIAL_DOC_COMMENT(COMMENT), // Returned for each line, except the last in a
                                // doc comment.
  /** Returned for each new line occurring within a block comment: */
  NL_BLOCK_COMMENT(COMMENT),
  /** Returned for each new line occurring within a doc comment. */
  NL_DOC_COMMENT(COMMENT),
  // Error comments:
  UNTERMINATED_BLOCK_COMMENT(COMMENT, true), //
  UNTERMINATED_DOC_COMMENT(COMMENT, true), //
  // Specials:
  UNKNOWN_CHARACTER(NONCODE, true), //
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
    return this == NL || this == NL_BLOCK_COMMENT || this == NL_DOC_COMMENT;
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
