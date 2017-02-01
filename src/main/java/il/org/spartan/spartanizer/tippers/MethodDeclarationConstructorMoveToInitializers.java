package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.lisp.*;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.java.namespace.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.spartanizer.utils.*;
import org.jetbrains.annotations.NotNull;

/** As per {@link Issue1008}
 * @author Yossi Gil <tt>yossi.gil@gmail.com</tt>
 * @since 2017-01-21 */
public class MethodDeclarationConstructorMoveToInitializers extends CarefulTipper<MethodDeclaration>//
    implements TipperCategory.Idiomatic {
  @Override protected boolean prerequisite(@NotNull final MethodDeclaration ¢) {
    if (!¢.isConstructor() || !¢.parameters().isEmpty())
      return false;
    final ASTNode $ = containing.typeDeclaration(¢);
    return step.constructors($).size() == 1 && step.initializersInstance($).isEmpty();
  }

  @NotNull
  @Override public String description(final MethodDeclaration ¢) {
    return "Match parameter names to fields in constructor '" + ¢ + "'";
  }

  @Override public Tip tip(final MethodDeclaration ¢) {
    return tip(first(statements(¢)));
  }

  private static Tip tip(final Statement s) {
    final Assignment x = az.assignment(expression(az.expressionStatement(s)));
    assert fault.unreachable() || !fault.unreachable() : fault.specifically(Environment.of(to(x)).description(), to(x), from(x));
    return null;
  }
}