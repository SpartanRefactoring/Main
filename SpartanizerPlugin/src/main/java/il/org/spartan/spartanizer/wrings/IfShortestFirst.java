package il.org.spartan.spartanizer.wrings;

import static il.org.spartan.spartanizer.wring.dispatch.Wrings.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.wring.dispatch.*;
import il.org.spartan.spartanizer.wring.strategies.*;

/** convert
 *
 * <pre>
 * a ? (f,g,h) : c(d,e)
 * </pre>
 *
 * into
 *
 * <pre>
 * a ? c(d, e) : f(g, h)
 * </pre>
 *
 * @author Yossi Gil
 * @since 2015-08-15 */
public final class IfShortestFirst extends ReplaceCurrentNode<IfStatement> implements Kind.Sorting {
  @Override public String description(@SuppressWarnings("unused") final IfStatement __) {
    return "Invert logical conditiona and swap branches of 'if' to make the shortest branch first";
  }

  @Override public Statement replacement(final IfStatement ¢) {
    return Wrings.thenIsShorter(¢) ? null : invert(¢);
  }
}
