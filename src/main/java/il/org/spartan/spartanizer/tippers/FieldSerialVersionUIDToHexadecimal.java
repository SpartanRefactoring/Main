package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.utils.Example.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.engine.type.Primitive.*;
import il.org.spartan.spartanizer.java.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.utils.*;

/** See {@link #examples()} for documentation
 * @author Yossi Gil <tt>yossi.gil@gmail.com</tt>
 * @since 2017-03-24 */
public final class FieldSerialVersionUIDToHexadecimal extends Tipper<FieldDeclaration> implements TipperCategory.Idiomatic {
  private static final long serialVersionUID = 0x2A2A1B1B2BFBD6A5L;
  public static final String SERIAL_VERSION_UID = "serialVersionUID";
  private VariableDeclarationFragment fragment;
  NumberLiteral initializer;
  long replacement;

  @Override public Example[] examples() {
    return new Example[] { //
        convert("private static long " + SERIAL_VERSION_UID + " = 12345677899L;")//
            .to("private static long " + SERIAL_VERSION_UID + " = 1234567799;"),
        ignores("private long a = 3;"), //
        ignores("private static long a = 3;"), //
        ignores("long a = 3;"), //
    };
  }

  @Override public String description() {
    return String.format("Convert %s to hexadecimal", initializer == null ? "initializer" : "'" + initializer.getToken() + "'");
  }

  @Override public Tip tip(final FieldDeclaration ¢) {
    canTip(¢);
    assert ¢ == fragment.getParent();
    return new Tip(description(), initializer, ¢, getClass()) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        final NumberLiteral $ = copy.of(initializer);
        $.setToken(asLiteral());
        r.replace(initializer, $, g);
      }
    };
  }

  @Override public boolean canTip(final FieldDeclaration ¢) {
    if ((fragment = wizard.findFragment(¢)) == null || (initializer = az.numberLiteral(fragment.getInitializer())) == null)
      return false;
    String $ = initializer.getToken();
    if (NumericLiteralClassifier.of($) != Certain.LONG || $.matches("^0[xX]?.*"))
      return false;
    if ($.matches(".*[lL]$"))
      $ = lisp2.chopLast($);
    return parse($, $.matches("^[+\\-]?0") ? 8 : 10);
  }

  private boolean parse(final String token, final int radix) {
    try {
      replacement = Long.parseLong(token, radix);
      return true;
    } catch (final NumberFormatException ¢) {
      monitor.logEvaluationError(this, ¢);
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