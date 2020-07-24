package il.org.spartan.athenizer.zoomers;

import static il.org.spartan.spartanizer.ast.navigate.step.operand;
import static il.org.spartan.spartanizer.ast.navigate.step.parent;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ArrayAccess;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;
import org.eclipse.text.edits.TextEditGroup;

import il.org.spartan.athenizer.zoom.zoomers.Issue1004;
import il.org.spartan.spartanizer.ast.factory.copy;
import il.org.spartan.spartanizer.ast.factory.make;
import il.org.spartan.spartanizer.ast.navigate.containing;
import il.org.spartan.spartanizer.ast.navigate.extract;
import il.org.spartan.spartanizer.ast.safety.az;
import il.org.spartan.spartanizer.ast.safety.iz;
import il.org.spartan.spartanizer.tipping.CarefulTipper;
import il.org.spartan.spartanizer.tipping.Tip;
import il.org.spartan.spartanizer.tipping.categories.Category;

/** converts {@code
 * arr[i++] = y; arr[++i] = z;
 *
 * } to {@code
 * arr[i] = y; ++i; ++i; arr[i] = z;
 *
 * } does not expand if right hand side includes access index operand, such as
 * in arr[i]=i. works only on ExpressionStatement, varible declaration with
 * assignment will be treated after outlining by other expanders . Test case is
 * {@link Issue1004}
 * @author YuvalSimon {@code yuvaltechnion@gmail.com}
 * @since 2016-12-25 */
public class OutlineArrayAccess extends CarefulTipper<ArrayAccess>//
    implements Category.Bloater {
  private static final long serialVersionUID = 0x3480EA693440B5ABL;

  @Override @SuppressWarnings("unused") public String description(final ArrayAccess n) {
    return null;
  }
  @Override public Tip tip(final ArrayAccess a) {
    final Expression $ = copy.of(a.getIndex());
    final Statement s = containing.statement(a);
    final AST t = s.getAST();
    return new Tip(description(a), getClass(), a) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        final ListRewrite l = r.getListRewrite(s.getParent(), Block.STATEMENTS_PROPERTY);
        final ArrayAccess newa = copy.of(a);
        if (iz.postfixExpression($)) {
          newa.setIndex(make.from(a).identifier(az.simpleName(operand(az.postfixExpression($)))));
          l.insertAfter(t.newExpressionStatement($), s, g);
        } else {
          newa.setIndex(make.from(a).identifier(az.simpleName(operand(az.prefixExpression($)))));
          l.insertBefore(t.newExpressionStatement($), s, g);
        }
        r.replace(a, newa, g);
      }
    };
  }
  @Override protected boolean prerequisite(final ArrayAccess a) {
    final Expression e = a.getIndex();
    final Statement $ = containing.statement(a);
    final SimpleName n = iz.prefixExpression(e) ? extract.simpleName(az.prefixExpression(e))
        : iz.postfixExpression(e) ? extract.simpleName(az.postfixExpression(e)) : null;
    return n != null && iz.expressionStatement($) && iz.block(parent($)) && iz.updating(e) && !iz.assignment(e)
        && extract.countNameInSubtree(n, $) == 1;
  }
}