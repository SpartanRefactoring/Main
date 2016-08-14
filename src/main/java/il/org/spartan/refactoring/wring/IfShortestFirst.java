package il.org.spartan.refactoring.wring;

import static il.org.spartan.refactoring.wring.Wrings.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.refactoring.preferences.*;
import il.org.spartan.refactoring.preferences.PluginPreferencesResources.*;

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
  @Override public WringGroup kind() {
    // TODO Auto-generated method stub
    return null;
  }
  @Override public void go(final ASTRewrite r, final TextEditGroup g) {
    // TODO Auto-generated method stub
  }
}