package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.type.Primitive.*;
import il.org.spartan.spartanizer.java.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.spartanizer.tipping.categories.*;
import il.org.spartan.utils.*;

/** See {@link #examples()} for documentation
 * @author Yossi Gil
 * @since 2017-03-24 */
public final class FieldInitializedSerialVersionUIDToHexadecimal extends Tipper<FieldDeclaration> implements Category.Idiomatic {
  private static final long serialVersionUID = 0x2A2A1B1B2BFBD6A5L;
  public static final String SERIAL_VERSION_UID = "serialVersionUID";
  private VariableDeclarationFragment fragment;
  NumberLiteral initializer;
  long replacement;

  // TODO: Ori Roth, example tests not working, please fix -rr
  @Override public Examples examples() {
    return null;
    // return //
    // convert("private static long " + SERIAL_VERSION_UID + " =
    // 12345677899L;")//
    // .to("private static long " + SERIAL_VERSION_UID + " = 1234567799;").//
    // convert("private static long " + SERIAL_VERSION_UID + " =
    // -12345677899L;")//
    // .to("private static long " + SERIAL_VERSION_UID + " = -1234567799;") //
    // .ignores("private long a = 3;") //
    // .ignores("private static long a = 3;") //
    // .ignores("long a = 3;") //
    // ;
  }
  @Override public String description() {
    return String.format("Convert %s to hexadecimal", initializer == null ? "initializer" : "'" + initializer.getToken() + "'");
  }
  @Override public Tip tip(final FieldDeclaration ¢) {
    canTip(¢);
    assert ¢ == fragment.getParent();
    return new Tip(description(), myClass(), ¢, initializer) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        final NumberLiteral $ = copy.of(initializer);
        $.setToken(asLiteral());
        r.replace(initializer, $, g);
      }
    };
  }
  @Override public boolean canTip(final FieldDeclaration ¢) {
    if ((fragment = wizard.findFragment(¢)) == null)
      return false;
    final Expression i = fragment.getInitializer();
    if ((initializer = az.numberLiteral(i)) == null
        && (!iz.prefixExpression(i) || (initializer = az.numberLiteral(az.prefixExpression(i).getOperand())) == null))
      return false;
    String $ = initializer.getToken();
    if (NumericLiteralClassifier.of($) != Certain.LONG || $.matches("^0[xX]?.*"))
      return false;
    if ($.matches(".*[lL]$"))
      $ = lisp.chopLast($);
    return parse($, $.matches("^0.*") ? 8 : 10);
  }
  private boolean parse(final String token, final int radix) {
    try {
      replacement = Long.parseLong(token, radix);
      return true;
    } catch (final NumberFormatException ¢) {
      note.bug(this, ¢);
      return false;
    }
  }
  String asLiteral() {
    return String.format(//
        replacement < 10 && replacement > -10 ? "%d" //
            : replacement < Integer.MAX_VALUE && replacement > Integer.MIN_VALUE ? "0x%X" //
                : "0x%XL",
        Long.valueOf(replacement));
  }
  @Override public String description(@SuppressWarnings("unused") final FieldDeclaration __) {
    return description();
  }
}