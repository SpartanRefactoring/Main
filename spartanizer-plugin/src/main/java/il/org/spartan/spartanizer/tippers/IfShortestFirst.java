package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.Statement;

import il.org.spartan.spartanizer.ast.factory.cons;
import il.org.spartan.spartanizer.ast.factory.make;
import il.org.spartan.spartanizer.ast.navigate.step;
import il.org.spartan.spartanizer.ast.navigate.wizard;
import il.org.spartan.spartanizer.ast.safety.az;
import il.org.spartan.spartanizer.tipping.ReplaceCurrentNode;
import il.org.spartan.spartanizer.tipping.categories.Category;

/** convert {@code
 * a ? (f,g,h) : c(d,e)
 * } into {@code
 * a ? c(d, e) : f(g, h)
 * }
 * @author Yossi Gil
 * @since 2015-08-15 */
public final class IfShortestFirst extends ReplaceCurrentNode<IfStatement>//
    implements Category.Transformation.Sort {
  private static final long serialVersionUID = 0x2C75A73E71D3D17L;

  @Override public String description(@SuppressWarnings("unused") final IfStatement __) {
    return "Invert logical conditiona and swap branches of 'if' to make the shortest branch first";
  }
  @Override public Statement replacement(final IfStatement ¢) {
    return wizard.thenIsShorter(¢) ? null : make.newSafeIf(az.statement(¢.getParent()), step.elze(¢), step.then(¢), cons.not(¢.getExpression()));
  }
}
