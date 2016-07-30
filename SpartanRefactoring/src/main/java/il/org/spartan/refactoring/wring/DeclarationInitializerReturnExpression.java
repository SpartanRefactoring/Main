package il.org.spartan.refactoring.wring;

import static il.org.spartan.refactoring.utils.Funcs.asReturnStatement;
import static il.org.spartan.refactoring.utils.Funcs.same;
import static il.org.spartan.refactoring.wring.Wrings.size;

import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.text.edits.TextEditGroup;

import il.org.spartan.refactoring.preferences.PluginPreferencesResources.WringGroup;
import il.org.spartan.refactoring.utils.Extract;
import il.org.spartan.refactoring.wring.LocalInliner.LocalInlineWithValue;

/**
 * A {@link Wring} to convert <code>int a = 3;
 * return a;</code> into <code>return a;</code>
 *
 * @author Yossi Gil
 * @since 2015-08-07
 */
public final class DeclarationInitializerReturnExpression extends Wring.VariableDeclarationFragementAndStatement {
  @Override ASTRewrite go(final ASTRewrite r, final VariableDeclarationFragment f, final SimpleName n, final Expression initializer, final Statement nextStatement,
      final TextEditGroup g) {
    if (initializer == null || hasAnnotation(f))
      return null;
    final ReturnStatement s = asReturnStatement(nextStatement);
    if (s == null)
      return null;
    final Expression newReturnValue = Extract.expression(s);
    final LocalInlineWithValue i = new LocalInliner(n, r, g).byValue(initializer);
    if (newReturnValue == null || same(n, newReturnValue) || !i.canSafelyInlineInto(newReturnValue) || i.replacedSize(newReturnValue) - eliminationSaving(f) - size(newReturnValue) > 0)
      return null;
    r.replace(s.getExpression(), newReturnValue, g);
    i.inlineInto(newReturnValue);
    eliminate(f, r, g);
    return r;
  }
  @Override String description(final VariableDeclarationFragment f) {
    return "Eliminate temporary " + f.getName() + " and inline its value into the expression of the subsequent return statement";
  }
  @Override WringGroup wringGroup() {
	return WringGroup.ELIMINATE_TEMP;
  }
}