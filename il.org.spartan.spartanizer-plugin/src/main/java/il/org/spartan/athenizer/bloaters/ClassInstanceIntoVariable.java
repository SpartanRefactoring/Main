package il.org.spartan.athenizer.bloaters;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.athenizer.zoomin.expanders.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.java.namespace.*;
import il.org.spartan.spartanizer.tipping.*;

/** new Z(); >> Z z1 = new Z(); {@link Issue0978}
 * @author Doron Meshulam {@code doronmmm@hotmail.com}
 * @since 2016-12-24 */
public class ClassInstanceIntoVariable extends CarefulTipper<ExpressionStatement>//
    implements TipperCategory.Bloater {
  private static final long serialVersionUID = 0x1445C0CE75E59B1BL;

  @Override public String description(@SuppressWarnings("unused") final ExpressionStatement __) {
    return "Split assignment statement";
  }
  @Override public Tip tip(final ExpressionStatement ¢) {
    return !iz.classInstanceCreation(expression(¢)) ? null : new Tip(description(¢), getClass(), ¢) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        final ClassInstanceCreation cic = az.classInstanceCreation(expression(¢));
        final Type t = cic.getType();
        r.getListRewrite(¢.getParent(), Block.STATEMENTS_PROPERTY).replace(¢,
            make.variableDeclarationStatement(copy.of(t), scope.newName(cic, t), copy.of(cic)), g);
      }
    };
  }
}
