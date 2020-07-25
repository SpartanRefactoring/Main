package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.navigate.step.constructors;
import static il.org.spartan.spartanizer.ast.navigate.step.expression;
import static il.org.spartan.spartanizer.ast.navigate.step.from;
import static il.org.spartan.spartanizer.ast.navigate.step.initializersInstance;
import static il.org.spartan.spartanizer.ast.navigate.step.statements;
import static il.org.spartan.spartanizer.ast.navigate.step.to;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Statement;

import fluent.ly.the;
import il.org.spartan.spartanizer.ast.navigate.containing;
import il.org.spartan.spartanizer.ast.safety.az;
import il.org.spartan.spartanizer.issues.Issue1008;
import il.org.spartan.spartanizer.java.namespace.Environment;
import il.org.spartan.spartanizer.tipping.CarefulTipper;
import il.org.spartan.spartanizer.tipping.Tip;
import il.org.spartan.spartanizer.tipping.categories.Category;
import il.org.spartan.utils.fault;

/** As per {@link Issue1008}
 * @author Yossi Gil
 * @since 2017-01-21 */
public class MethodDeclarationConstructorMoveToInitializers extends CarefulTipper<MethodDeclaration>//
    implements Category.Idiomatic {
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
    return tip(the.firstOf(statements(¢)));
  }
  private static Tip tip(final Statement s) {
    final Assignment x = az.assignment(expression(az.expressionStatement(s)));
    assert fault.unreachable() || !fault.unreachable() : fault.specifically(Environment.of(to(x)).description(), to(x), from(x));
    return null;
  }
}