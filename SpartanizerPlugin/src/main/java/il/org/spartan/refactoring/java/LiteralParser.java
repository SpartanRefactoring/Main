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
  public enum Types {
    /** <code>int</code> */
    INTEGER, //
    /** <code>long</code> */
    LONG, //
    /** <code>char</code> */
    CHARACTER, //
    /** <code>float</code> */
    FLOAT, //
    /** <code>double</code> */
    DOUBLE;
  }

  final String literal;

  /** Instantiates this class.
   * @param literal JD */
  public LiteralParser(final String literal) {
    this.literal = literal;
  }

  /** @return an integer representing the type of this literal.
   * @see LiteralParser.Types */
  public int type() {
    if (literal.charAt(0) == '\'')
      return Types.CHARACTER.ordinal();
    switch (literal.charAt(literal.length() - 1)) {
      case 'l':
      case 'L':
        return Types.LONG.ordinal();
      case 'f':
      case 'F':
        return Types.FLOAT.ordinal();
      case 'd':
      case 'D':
      case 'p':
      case 'P':
        return Types.DOUBLE.ordinal();
      default:
        if (literal.indexOf('.') >= 0)
          return Types.DOUBLE.ordinal();
        if (literal.indexOf('E') >= 0 || literal.indexOf('e') >= 0)
          return Types.DOUBLE.ordinal();
        return Types.INTEGER.ordinal();
    }
  }
}
