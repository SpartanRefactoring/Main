package org.spartan.refactoring.wring;

import static org.spartan.refactoring.wring.Wrings.invert;

import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.Statement;

/**
 * A {@link Wring} to convert <code>a ? (f,g,h) : c(d,e)</code> into
 * <code>a ? c(d,e) : f(g,h)</code>
 *
 * @author Yossi Gil
 * @since 2015-08-15
 */
public final class IfShortestFirst extends Wring.ReplaceCurrentNode<IfStatement> {
  @Override Statement replacement(final IfStatement s) {
    return Wrings.thenIsShorter(s) ? null : invert(s);
  }
  @Override String description(@SuppressWarnings("unused") final IfStatement _) {
    return "Invert logical conditiona and swap branches of 'if' to make the shortest branch first";
  }
}