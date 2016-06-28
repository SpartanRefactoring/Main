package il.org.spartan.refactoring.wring;

import static il.org.spartan.refactoring.wring.Wrings.*;
import il.org.spartan.refactoring.preferences.*;

import org.eclipse.jdt.core.dom.*;

/**
 * A {@link Wring} to convert <code>a ? (f,g,h) : c(d,e)</code> into <code>a ?
 * c(d,e) : f(g,h)</code>
 *
 * @author Yossi Gil
 * @since 2015-08-15
 */
public final class IfShortestFirst extends Wring.ReplaceCurrentNode<IfStatement> implements Kind.ReorganizeExpression {
  @Override Statement replacement(final IfStatement s) {
    return Wrings.thenIsShorter(s) ? null : invert(s);
  }
  @Override String description(final IfStatement s) {
    return "Invert logical conditiona and swap branches of if(" + s.getExpression() + ") ... to make the shortest branch first";
  }
}