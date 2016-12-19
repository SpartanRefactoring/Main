package il.org.spartan.spartanizer.java;

import il.org.spartan.spartanizer.engine.type.*;

/** A utility to determine the exact type of a Java character or numerical
 * literal.
 * @author Yossi Gil
 * @since 2015-08-30 */
public final class NumericLiteralClassifier {
  public static Primitive.Certain of(final String literal) {
    return literal == null ? null : new NumericLiteralClassifier(literal).type();
  }

  final String inner;

  /** Instantiates this class.
   * @param literal JD */
  public NumericLiteralClassifier(final String literal) {
    this.inner = literal;
  }

  /** @return the type of this literal.
   * @see PrudentType */
  public Primitive.Certain type() {
    if (inner.charAt(0) == '\'')
      return Primitive.Certain.CHAR;
    switch (inner.charAt(inner.length() - 1)) {
      case 'l':
      case 'L':
        return Primitive.Certain.LONG;
      case 'f':
      case 'F':
        return Primitive.Certain.FLOAT;
      case 'd':
      case 'D':
      case 'p':
      case 'P':
        return Primitive.Certain.DOUBLE;
      default:
        if (inner.indexOf('.') >= 0)
          return Primitive.Certain.DOUBLE;
        if (inner.indexOf('E') >= 0 || inner.indexOf('e') >= 0)
          return Primitive.Certain.DOUBLE;
        return Primitive.Certain.INT;
    }
  }
}
