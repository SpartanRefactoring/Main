package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.tipping.*;

/** convert {@code
 * a ? (f,g,h) : c(d,e)
 * } into {@code
 * a ? c(d, e) : f(g, h)
 * }
 * @author Yossi Gil
 * @since 2015-08-15 */
public final class IfShortestFirst extends ReplaceCurrentNode<IfStatement>//
    implements TipperCategory.Sorting {
  private static final long serialVersionUID = 0x2C75A73E71D3D17L;

  @Override public String description(@SuppressWarnings("unused") final IfStatement __) {
    return "Invert logical conditiona and swap branches of 'if' to make the shortest branch first";
  }

  @Override public Statement replacement(final IfStatement ¢) {
    return wizard.thenIsShorter(¢) ? null : make.invert(¢);
  }
}
