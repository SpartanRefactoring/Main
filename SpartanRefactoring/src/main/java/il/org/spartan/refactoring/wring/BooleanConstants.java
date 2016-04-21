package il.org.spartan.refactoring.wring;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.refactoring.preferences.PluginPreferencesResources.WringGroup;

/**
 * A {@link Wring} to remove unnecessary uses of Boolean.valueOf, for example by
 * converting
 * <code><pre>Boolean b = Boolean.valueOf(true)</pre><code> into <code><pre>Boolean b = Boolean.TRUE</pre><code>
 *
 * @author Daniel Mittelman <code><mittelmania [at] gmail.com></code>
 *
 * @since 2016-04-04
 */
public class BooleanConstants extends Wring.ReplaceCurrentNode<MethodInvocation> {
  @Override ASTNode replacement(final MethodInvocation i) {
    return i.getExpression() == null || !i.getExpression().toString().equals("Boolean")
        || !i.getName().getIdentifier().equals("valueOf") || i.arguments().size() != 1 ? null
            : i.getAST().newQualifiedName(i.getAST().newName("Boolean"),
                i.getAST().newSimpleName(((BooleanLiteral) i.arguments().get(0)).booleanValue() ? "TRUE" : "FALSE"));
  }
  @Override String description(final MethodInvocation i) {
    return "Use the built-in boolean constant instead of valueOf()";
  }
  @Override WringGroup wringGroup() {
    return WringGroup.CONSOLIDATE_ASSIGNMENTS_STATEMENTS; // TODO fix this
  }
}