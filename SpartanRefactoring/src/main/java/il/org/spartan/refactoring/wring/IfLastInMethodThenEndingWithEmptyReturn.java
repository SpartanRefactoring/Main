package il.org.spartan.refactoring.wring;

import il.org.spartan.refactoring.preferences.*;
import il.org.spartan.refactoring.preferences.PluginPreferencesResources.WringGroup;
import il.org.spartan.refactoring.suggestions.*;
import il.org.spartan.refactoring.utils.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import static il.org.spartan.Utils.*;
import static il.org.spartan.refactoring.utils.Funcs.*;

import static il.org.spartan.refactoring.utils.extract.*;

/**
 * A {@link Wring} to convert <code>if (a) { return x; } </code> into
 * <code>return x;</code> provided that this <code><b>if</b></code> statement is
 * the last statement in a method.
 *
 * @author Yossi Gil
 * @author Daniel Mittelman <code><mittelmania [at] gmail.com></code>
 * @since 2015-09-09
 */
public class IfLastInMethodThenEndingWithEmptyReturn extends Wring<IfStatement> implements Kind.Simplify {
  @Override String description(final IfStatement s) {
    return "Remove redundant return statement in 'then' branch of if(" + s.getExpression() + ") ... statement that terminates this method";
  }
  @Override Suggestion make(final IfStatement s, final ExclusionManager exclude) {
    final Block b = asBlock(s.getParent());
    if (b == null || !(b.getParent() instanceof MethodDeclaration) || !lastIn(s, b.statements()))
      return null;
    final ReturnStatement deleteMe = asReturnStatement(extract.lastStatement(then(s)));
    if (deleteMe == null || deleteMe.getExpression() != null)
      return null;
    if (exclude != null)
      exclude.equals(s);
    return new Suggestion(description(s), s) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        r.replace(deleteMe, s.getAST().newEmptyStatement(), g);
      }
    };
  }
  @Override public WringGroup kind() {
    // TODO Auto-generated method stub
    return null;
  }
  @Override public void go(final ASTRewrite r, final TextEditGroup g) {
    // TODO Auto-generated method stub
  }
}
