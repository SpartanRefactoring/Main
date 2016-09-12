package il.org.spartan.spartanizer.wring;

import static il.org.spartan.spartanizer.wring.Wrings.*;

import org.eclipse.jdt.core.dom.*;

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
public final class IfShortestFirst extends Wring.ReplaceCurrentNode<IfStatement> implements Kind.Sorting {
  @Override String description(@SuppressWarnings("unused") final IfStatement __) {
    return "Invert logical conditiona and swap branches of 'if' to make the shortest branch first";
  }

  @Override Statement replacement(final IfStatement s) {
    return Wrings.thenIsShorter(s) ? null : invert(s);
  }
}
