package il.org.spartan.bloater.bloaters;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.java.namespace.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.zoomer.zoomin.expanders.*;

/** new Z(); >> Z z1 = new Z(); {@link Issue0978}
 * @author Doron Meshulam <tt>doronmmm@hotmail.com</tt>
 * @since 2016-12-24 */
public class ClassInstanceIntoVariable extends CarefulTipper<ExpressionStatement> implements TipperCategory.Expander {
  @Override public String description(@SuppressWarnings("unused") final ExpressionStatement __) {
    return "Split assignment statement";
  }

  @Override public Tip tip(final ExpressionStatement ¢) {
    Expression e = expression(¢);
    boolean flag = iz.classInstanceCreation(e);
    return !flag ? null : new Tip(description(¢), ¢, getClass()) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        ClassInstanceCreation cic = az.classInstanceCreation(expression(¢));
        Type t = copy.of(cic.getType());
        VariableDeclarationStatement vds = make.variableDeclarationStatement(t, scope.newName(cic, t), copy.of(cic));
        final ListRewrite l = r.getListRewrite(¢.getParent(), Block.STATEMENTS_PROPERTY);
        l.replace(¢, vds, g);
      }
    };
  }
}
