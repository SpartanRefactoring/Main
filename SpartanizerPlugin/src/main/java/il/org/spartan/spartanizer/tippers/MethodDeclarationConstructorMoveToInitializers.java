package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.lisp.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.issues.*;
import il.org.spartan.spartanizer.java.namespace.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.utils.*;
import nano.ly.*;

/** As per {@link Issue1008}
 * @author Yossi Gil
 * @since 2017-01-21 */
public class MethodDeclarationConstructorMoveToInitializers extends CarefulTipper<MethodDeclaration>//
    implements TipperCategory.Idiomatic {
  private static final long serialVersionUID = -0x57FBD74AB91AB9ECL;

  @Override protected boolean prerequisite(final MethodDeclaration ¢) {
    if (!¢.isConstructor() || !¢.parameters().isEmpty())
      return false;
    final ASTNode $ = containing.typeDeclaration(¢);
    return constructors($).size() == 1 && initializersInstance($).isEmpty();
  }

  @Override public String description(final MethodDeclaration ¢) {
    return "Match parameter names to fields in constructor '" + ¢ + "'";
  }

  @Override public Tip tip(final MethodDeclaration ¢) {
    return tip(the.headOf(statements(¢)));
  }

  private static Tip tip(final Statement s) {
    final Assignment x = az.assignment(expression(az.expressionStatement(s)));
    assert fault.unreachable() || !fault.unreachable() : fault.specifically(Environment.of(to(x)).description(), to(x), from(x));
    return null;
  }
}