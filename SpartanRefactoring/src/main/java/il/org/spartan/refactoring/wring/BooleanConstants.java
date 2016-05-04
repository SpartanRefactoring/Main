package il.org.spartan.refactoring.wring;

import static il.org.spartan.refactoring.utils.Funcs.newSimpleName;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.BooleanLiteral;
import org.eclipse.jdt.core.dom.MethodInvocation;

import il.org.spartan.refactoring.preferences.PluginPreferencesResources.WringGroup;

/**
 * A {@link Wring} to remove unnecessary uses of Boolean.valueOf, for example by
 * converting <code>
 *
 * <pre>
 * Boolean b = Boolean.valueOf(true)
 * </pre>
 *
 * <code> into <code>
 *
 * <pre>
 * Boolean b = Boolean.TRUE
 * </pre>
 *
 * <code>
 *
 * @author Daniel Mittelman <code><mittelmania [at] gmail.com></code>
 *
 * @since 2016-04-04
 */
public class BooleanConstants extends Wring.ReplaceCurrentNode<MethodInvocation> {
  @Override ASTNode replacement(final MethodInvocation i) {
    return i.getExpression() == null || !i.getExpression().toString().equals("Boolean")
        || !i.getName().getIdentifier().equals("valueOf") || i.arguments().size() != 1
        || !(i.arguments().get(0).toString().equals("true") || i.arguments().get(0).toString().equals("false")) ? null
            : i.getAST().newQualifiedName(i.getAST().newName("Boolean"),
                newSimpleName(i, ((BooleanLiteral) i.arguments().get(0)).booleanValue() ? "TRUE" : "FALSE"));
  }
  @Override String description(final MethodInvocation i) {
    return "Use built-in boolean constant instead of valueOf()";
  }
  @Override WringGroup wringGroup() {
    return WringGroup.CONSOLIDATE_ASSIGNMENTS_STATEMENTS; // TODO fix this
  }
}