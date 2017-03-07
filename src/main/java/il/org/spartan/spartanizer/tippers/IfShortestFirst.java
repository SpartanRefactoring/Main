package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.dispatch.Tippers.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.dispatch.*;
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
  private static final long serialVersionUID = 200228162286468375L;

  @Override public String description(@SuppressWarnings("unused") final IfStatement __) {
    return "Invert logical conditiona and swap branches of 'if' to make the shortest branch first";
  }

  @Override public Statement replacement(final IfStatement ¢) {
    return thenIsShorter(¢) ? null : invert(¢);
  }
}
