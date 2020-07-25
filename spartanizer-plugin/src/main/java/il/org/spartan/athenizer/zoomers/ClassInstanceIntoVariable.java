package il.org.spartan.athenizer.zoomers;

import static il.org.spartan.spartanizer.ast.navigate.step.expression;

import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.text.edits.TextEditGroup;

import il.org.spartan.athenizer.zoom.zoomers.Issue0978;
import il.org.spartan.spartanizer.ast.factory.copy;
import il.org.spartan.spartanizer.ast.factory.make;
import il.org.spartan.spartanizer.ast.safety.az;
import il.org.spartan.spartanizer.ast.safety.iz;
import il.org.spartan.spartanizer.java.namespace.scope;
import il.org.spartan.spartanizer.tipping.CarefulTipper;
import il.org.spartan.spartanizer.tipping.Tip;
import il.org.spartan.spartanizer.tipping.categories.Category;

/** new Z(); >> Z z1 = new Z(); {@link Issue0978}
 * @author Doron Meshulam {@code doronmmm@hotmail.com}
 * @since 2016-12-24 */
public class ClassInstanceIntoVariable extends CarefulTipper<ExpressionStatement>//
    implements Category.Bloater {
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
