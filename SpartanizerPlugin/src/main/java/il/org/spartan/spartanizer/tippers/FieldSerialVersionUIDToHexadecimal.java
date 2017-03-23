package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

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
  private static final long serialVersionUID = 918113390332393279L;
  private VariableDeclarationFragment fragment;
  NumberLiteral initializer;
  long replacement;

  @Override public String description() {
    return String.format("Convert %s to hexadecimal", initializer == null ? "initializer" : "'" + initializer.getToken() + "'");
  }

  @Override public Tip tip(FieldDeclaration ¢) {
    assert ¢ == fragment.getParent();
    return new Tip(description(), initializer, getClass()) {
      @Override public void go(ASTRewrite r, TextEditGroup g) {
        NumberLiteral $ = copy.of(initializer);
        $.setToken(asLiteral());
        r.replace(initializer, $, g);
      }
    };
  }

  static VariableDeclarationFragment findFragment(FieldDeclaration n) {
    for (VariableDeclarationFragment f : fragments(n))
      if (f.getName().toString().equals(SERIAL_VERSION_UID))
        return f;
    return null;
  }

  @Override public boolean canTip(FieldDeclaration n) {
    if ((fragment = findFragment(n)) == null)
      return false;
    if ((initializer = az.numberLiteral(fragment.getInitializer())) == null)
      return false;
    String token = initializer.getToken();
    if (!iz.in(NumericLiteralClassifier.of(token), Certain.CHAR, Certain.INT, Certain.LONG))
      return false;
    if (token.matches("^0[xX]?.*"))
      return false;
    if (token.matches(".*[lL]$"))
      token = system.chopLast(token);
    try {
      replacement = Long.parseLong(token);
    } catch (NumberFormatException x) {
      monitor.logEvaluationError(this, x);
      return false;
    }
    return true;
  }

  @Override public String description(FieldDeclaration n) {
    return null;
  }

  private String asLiteral() {
    if (replacement < 10 && replacement > -10)
      return String.format("%d", Long.valueOf(replacement));
    if (replacement < Integer.MAX_VALUE && replacement > Integer.MIN_VALUE)
      return String.format("0x%X", Long.valueOf(replacement));
    return String.format("0x%XL", Long.valueOf(replacement));
  }
}