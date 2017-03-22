package il.org.spartan.bloater.bloaters;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;
import org.jetbrains.annotations.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.zoomer.zoomin.expanders.*;

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
 * @author YuvalSimon <tt>yuvaltechnion@gmail.com</tt>
 * @since 2016-12-25 */
public class OutlineArrayAccess extends CarefulTipper<ArrayAccess>//
    implements TipperCategory.Bloater {
  private static final long serialVersionUID = 3783281424560338347L;

  @Nullable @Override @SuppressWarnings("unused") public String description(final ArrayAccess n) {
    return null;
  }

  @NotNull @Override public Fragment tip(@NotNull final ArrayAccess a) {
    final Expression $ = copy.of(a.getIndex());
    @Nullable final ASTNode b = extract.containingStatement(a);
    final AST t = b.getAST();
    return new Fragment(description(a), a, getClass()) {
      @Override public void go(@NotNull final ASTRewrite r, final TextEditGroup g) {
        final ListRewrite l = r.getListRewrite(b.getParent(), Block.STATEMENTS_PROPERTY);
        final ArrayAccess newa = copy.of(a);
        if (iz.postfixExpression($)) {
          newa.setIndex(make.from(a).identifier(az.simpleName(operand(az.postfixExpression($)))));
          l.insertAfter(t.newExpressionStatement($), b, g);
        } else {
          newa.setIndex(make.from(a).identifier(az.simpleName(operand(az.prefixExpression($)))));
          l.insertBefore(t.newExpressionStatement($), b, g);
        }
        r.replace(a, newa, g);
      }
    };
  }

  /** [[SuppressWarningsSpartan]] */
  @Override protected boolean prerequisite(@NotNull final ArrayAccess a) {
    final Expression e = a.getIndex();
    @Nullable final Statement b = extract.containingStatement(a);
    if (!iz.expressionStatement(b) || !iz.block(parent(b)) || !iz.incrementOrDecrement(e) || iz.assignment(e))
      return false;
    final SimpleName n = iz.prefixExpression(e) ? extract.simpleName(az.prefixExpression(e)) : extract.simpleName(az.postfixExpression(e));
    if (n == null)
      return false;
    @Nullable final Assignment $ = az.assignment(expression(az.expressionStatement(b)));
    return $ != null && left($).equals(a) && iz.plainAssignment($) && !iz.containsName(n, right($));
  }
}