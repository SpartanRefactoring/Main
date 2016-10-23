/**
 *
 */
package il.org.spartan.java;

import org.jetbrains.annotations.NotNull;

/** @author Yossi Gil
 * @since 2011-11-19 */
public class SignatureFilter extends CodeOnlyFilter {
  private static boolean usefulKeyword(@NotNull final Token ¢) {
    switch (¢) {
      default:
        return false;
      case __class:
      case __interface:
      case __enum:
      case __new:
      case __abstract:
      case AT_INTERFACE:
        return true;
    }
  }

  /** @param ¢ a token */
  private static boolean usefulPucntuation(@NotNull final Token ¢) {
    switch (¢) {
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

  @Override protected boolean ok(@NotNull final Token ¢) {
    switch (¢.kind) {
      case KEYWORD:
        return usefulKeyword(¢);
      case PUNCTUATION:
        return usefulPucntuation(¢);
      default:
        return false;
    }
  }
}
