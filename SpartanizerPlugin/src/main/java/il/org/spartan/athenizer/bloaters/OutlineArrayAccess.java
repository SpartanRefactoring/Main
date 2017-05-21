package il.org.spartan.athenizer.bloaters;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.athenizer.*;
import il.org.spartan.athenizer.zoomin.expanders.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;

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
    implements BloaterCategory.Splitting {
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