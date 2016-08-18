package il.org.spartan.refactoring.wring;

import static il.org.spartan.refactoring.wring.Wrings.*;

import org.eclipse.jdt.core.dom.*;

/** A {@link Wring} to convert
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
public class IfShortestFirst extends Wring.ReplaceCurrentNode<IfStatement> implements Kind.Canonicalization{
  @Override String description(@SuppressWarnings("unused") final IfStatement __) {
    return "Invert logical conditiona and swap branches of 'if' to make the shortest branch first";
  }
  @Override Statement replacement(final IfStatement s) {
    return Wrings.thenIsShorter(s) ? null : invert(s);
  }
}
