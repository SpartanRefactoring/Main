package il.org.spartan.athenizer.inflate.expanders;

import java.util.function.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import static il.org.spartan.spartanizer.ast.navigate.step.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;

/** converts
 *
 * <pre>
 * arr[i++] = y; arr[++i] = z;
 *
 * <pre>
 * to
 *
 * <pre>
 * arr[i] = y; ++i; ++i; arr[i] = z;
 *
 * <pre>
 * does not expand if right hand side includes access index operand, such as in
 * arr[i]=i
 * @author YuvalSimon <tt>yuvaltechnion@gmail.com</tt>
 * @since 2016-12-25 */
public class OutlineArrayAccess extends CarefulTipper<ArrayAccess> implements TipperCategory.InVain {
  @Override @SuppressWarnings("unused") public String description(final ArrayAccess n) {
    return null;
  }

  @Override public Tip tip(final ArrayAccess a) {
    final Expression $ = duplicate.of(a.getIndex());
    final ASTNode b = extract.containingStatement(a);
    final AST t = b.getParent().getAST();
    return new Tip(description(a), a, this.getClass()) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        final ListRewrite l = r.getListRewrite(b.getParent(), Block.STATEMENTS_PROPERTY);
        final ArrayAccess newa = duplicate.of(a);
        if (iz.postfixExpression($)) {
          newa.setIndex(a.getAST().newSimpleName(identifier(az.simpleName(operand(az.postfixExpression($))))));
          l.insertAfter(t.newExpressionStatement($), b, g);
        } else {
          newa.setIndex(a.getAST().newSimpleName(identifier(az.simpleName(operand(az.prefixExpression($))))));
          l.insertBefore(t.newExpressionStatement($), b, g);
        }
        r.replace(a, newa, g);
      }
    };
  }

  @Override protected boolean prerequisite(final ArrayAccess a) {
    final Expression e = a.getIndex();
    SimpleName $;
    final ASTNode b = extract.containingStatement(a);
    if (!iz.block(b.getParent()) || !iz.expressionStatement(b) || !iz.incrementOrDecrement(e) || iz.assignment(e))
      return false;
    $ = iz.prefixExpression(e) ? az.simpleName(az.prefixExpression(e)) : az.simpleName(az.postfixExpression(e));
    if ($ == null)
      return false;
    final Expression s = expression(az.expressionStatement(b));
    return iz.assignment(s) && left(az.assignment(s)).equals(a) && iz.plainAssignment(az.assignment(s)) && !containsName($, right(az.assignment(s)));
  }

  private static boolean containsName(final SimpleName n, final Expression x) {
    return !searchDescendants.forClass(SimpleName.class).suchThat(t -> identifier(t).equals(identifier(n))).inclusiveFrom(x).isEmpty();
  }
}