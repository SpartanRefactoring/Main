package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.utils.Example.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;
import org.jetbrains.annotations.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.engine.type.Primitive.*;
import il.org.spartan.spartanizer.java.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.utils.*;

public final class FieldSerialVersionUIDToHexadecimal extends Tipper<FieldDeclaration> implements TipperCategory.Idiomatic {
  private static final String SERIAL_VERSION_UID = "serialVersionUID";
  private static final long serialVersionUID = 0xCBDCB439E4AB73FL;
  @Nullable private VariableDeclarationFragment fragment;
  @Nullable NumberLiteral initializer;
  long replacement;

  @Override @NotNull public Example[] examples() {
    return new Example[] { //
        convert("private long " + SERIAL_VERSION_UID + " = 1234567799L;")//
            .to("private long " + SERIAL_VERSION_UID + " = 1234567799;"),
        ignores("private long int a = 3;"), //
        ignores("long int a = 3;"), //
    };
  }

  @Override public String description() {
    return String.format("Convert %s to hexadecimal", initializer == null ? "initializer" : "'" + initializer.getToken() + "'");
  }

  @Override public Tip tip(final FieldDeclaration ¢) {
    canTip(¢);
    assert ¢ == fragment.getParent();
    return new Tip(description(), initializer, getClass()) {
      @Override public void go(@NotNull final ASTRewrite r, final TextEditGroup g) {
        final NumberLiteral $ = copy.of(initializer);
        $.setToken(asLiteral());
        r.replace(initializer, $, g);
      }
    };
  }

  static VariableDeclarationFragment findFragment(final FieldDeclaration ¢) {
    for (@NotNull final VariableDeclarationFragment $ : fragments(¢))
      if (($.getName() + "").equals(SERIAL_VERSION_UID))
        return $;
    return null;
  }

  @Override public boolean canTip(final FieldDeclaration d) {
    if ((fragment = findFragment(d)) == null || (initializer = az.numberLiteral(fragment.getInitializer())) == null)
      return false;
    String token = initializer.getToken();
    if (NumericLiteralClassifier.of(token) != Certain.LONG || token.matches("^0[xX]?.*"))
      return false;
    if (token.matches(".*[lL]$"))
      token = system.chopLast(token);
    try {
      replacement = Long.parseLong(token);
    } catch (@NotNull final NumberFormatException x) {
      monitor.logEvaluationError(this, x);
      return false;
    }
    return true;
  }

  String asLiteral() {
    return String.format(//
        replacement < 10 && replacement > -10 ? "%d" //
            : replacement < Integer.MAX_VALUE && replacement > Integer.MIN_VALUE ? "0x%X" //
                : "0x%XL",
        Long.valueOf(replacement));
  }

  @Override public String description(@SuppressWarnings("unused") final FieldDeclaration d) {
    return description();
  }
}