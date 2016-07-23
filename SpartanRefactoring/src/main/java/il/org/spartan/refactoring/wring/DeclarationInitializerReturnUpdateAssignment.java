package il.org.spartan.refactoring.wring;

import il.org.spartan.refactoring.preferences.*;
import il.org.spartan.refactoring.preferences.PluginPreferencesResources.WringGroup;
import il.org.spartan.refactoring.utils.*;
import il.org.spartan.refactoring.wring.LocalInliner.LocalInlineWithValue;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.Assignment.Operator;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import static il.org.spartan.refactoring.utils.Funcs.*;
import static org.eclipse.jdt.core.dom.Assignment.Operator.*;

/**
 * A {@link Wring} to convert <code>int a = 3; return a;</code> into
 * <code>return a;</code>
 *
 * @author Yossi Gil
 * @since 2015-08-07
 */
public final class DeclarationInitializerReturnUpdateAssignment extends Wring.VariableDeclarationFragementAndStatement implements Kind.InlineVariable {
  @Override ASTRewrite go(final ASTRewrite r, final VariableDeclarationFragment f, final SimpleName n, final Expression initializer, final Statement nextStatement, final TextEditGroup g) {
    if (initializer == null || hasAnnotation(f))
      return null;
    final ReturnStatement s = asReturnStatement(nextStatement);
    if (s == null)
      return null;
    final Assignment a = asAssignment(extract.expression(s));
    if (a == null || !same(n, left(a)))
      return null;
    final Operator o = a.getOperator();
    if (o == ASSIGN)
      return null;
    final Expression newReturnValue = assignmentAsExpression(a);
    final LocalInlineWithValue i = new LocalInliner(n, r, g).byValue(initializer);
    if (!i.canInlineInto(newReturnValue) || i.replacedSize(newReturnValue) - eliminationSaving(f) - size(newReturnValue) > 0)
      return null;
    r.replace(a, newReturnValue, g);
    i.inlineInto(newReturnValue);
    eliminate(f, r, g);
    return r;
  }
  @Override String description(final VariableDeclarationFragment f) {
    return "Eliminate temporary " + f.getName() + " and inline its value into the expression of the subsequent return statement";
  }
  @Override public WringGroup kind() {
    // TODO Auto-generated method stub
    return null;
  }
  @Override public void go(final ASTRewrite r, final TextEditGroup g) {
    // TODO Auto-generated method stub
  }
}