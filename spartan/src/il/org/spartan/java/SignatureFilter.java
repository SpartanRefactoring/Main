/**
 *
 */
package il.org.spartan.java;

/** @author Yossi Gil
 * @since 2011-11-19 */
public class SignatureFilter extends CodeOnlyFilter {
  private static boolean usefulKeyword(final Token t) {
    switch (t) {
      default:
        return false;
      case __class:
      case __interface:
      case __enum:
      case __new:
      case __abstract:
      case AT__INTERFACE:
        return true;
    }
  }

  /** @param t a token */
  private static boolean usefulPucntuation(final Token t) {
    switch (t) {
      default:
        return false;
      case SEMICOLON:
      case LBRACK:
      case RBRACK:
      case LBRACE:
      case RBRACE:
      case LPAREN:
      case RPAREN:
      case EQ:
        return true;
    }
  }

  @Override protected boolean ok(final Token t) {
    switch (t.kind) {
      case KEYWORD:
        return usefulKeyword(t);
      case PUNCTUATION:
        return usefulPucntuation(t);
      default:
        return false;
    }
  }
}
