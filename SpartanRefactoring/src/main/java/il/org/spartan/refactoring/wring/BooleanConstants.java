package il.org.spartan.refactoring.wring;

import il.org.spartan.refactoring.preferences.*;
import il.org.spartan.refactoring.preferences.PluginPreferencesResources.WringGroup;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import static il.org.spartan.refactoring.utils.Funcs.*;

/**
 * A {@link Wring} to remove unnecessary uses of Boolean.valueOf, for example by
 * converting <code>
 *
 * <pre> Boolean b = Boolean.valueOf(true) </pre>
 *
 * <code> into <code>
 *
 * <pre> Boolean b = Boolean.TRUE </pre>
 *
 * <code>
 *
 * @author Daniel Mittelman <code><mittelmania [at] gmail.com></code>
 *
 @since 2016-04-04
 */
public class BooleanConstants extends Wring.ReplaceCurrentNode<MethodInvocation> implements Kind.ConsolidateStatements {
  @Override ASTNode replacement(final MethodInvocation i) {
    return i.getExpression() == null || !"Boolean".equals(i.getExpression().toString()) || !"valueOf".equals(i.getName().getIdentifier()) || i.arguments().size() != 1
        || !"true".equals(i.arguments().get(0).toString()) && !"false".equals(i.arguments().get(0).toString()) ? null : i.getAST().newQualifiedName(i.getAST().newName("Boolean"),
        newSimpleName(i, ((BooleanLiteral) i.arguments().get(0)).booleanValue() ? "TRUE" : "FALSE"));
  }
  @Override String description(@SuppressWarnings("unused") final MethodInvocation __) {
    return "Use built-in boolean constant instead of valueOf()";
  }
  @Override public WringGroup kind() {
    // TODO Auto-generated method stub
    return null;
  }
  @Override public void go(final ASTRewrite r, final TextEditGroup g) {
    // TODO Auto-generated method stub
  }
}