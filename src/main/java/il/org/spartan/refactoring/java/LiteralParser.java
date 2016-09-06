package il.org.spartan.refactoring.java;

/** A utility to determine the exact type of a Java character or numerical
 * literal.
 * @author Yossi Gil
 * @since 2015-08-30 */
public class LiteralParser {
  /** An <code><b>enum</b></code> to give symbolic names to the literal types.
   * @author Yossi Gil
   * @since 2015-08-30
   * @see LiteralParser#persuation */
  final String literal;

  /** Instantiates this class.
   * @param literal JD */
  public LiteralParser(final String literal) {
    this.literal = literal;
  }

  /** @return an the type of this literal.
   * @see PrudentType */
  public PrudentType type() {
    if (literal.charAt(0) == '\'')
      return PrudentType.CHAR;
    switch (literal.charAt(literal.length() - 1)) {
      case 'l':
      case 'L':
        return PrudentType.LONG;
      case 'f':
      case 'F':
        return PrudentType.FLOAT;
      case 'd':
      case 'D':
      case 'p':
      case 'P':
        return PrudentType.DOUBLE;
      default:
        if (literal.indexOf('.') >= 0)
          return PrudentType.DOUBLE;
        if (literal.indexOf('E') >= 0 || literal.indexOf('e') >= 0)
          return PrudentType.DOUBLE;
        return PrudentType.INT;
    }
  }
}
