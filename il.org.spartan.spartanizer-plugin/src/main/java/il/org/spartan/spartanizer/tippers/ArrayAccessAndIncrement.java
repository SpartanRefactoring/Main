package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.PostfixExpression.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.spartanizer.tipping.categories.*;
import il.org.spartan.utils.*;

/** Use {@link #examples()} for documentation
 * @author Dor Ma'ayan
 * @since 25-11-2016 */
public final class ArrayAccessAndIncrement extends EagerTipper<ArrayAccess>//
    implements Category.Inlining {
  private static final long serialVersionUID = -0x45FEEB5174E9151DL;

  @Override public String description(final ArrayAccess ¢) {
    return "Inline next increment/decrement of " + ¢.getIndex() + " into array access";
  }
  @Override public Examples examples() {
    return convert("array[i] = 1; ++i;") //
        .to("array[i++] = 1;") //
        .ignores("array[i].f(); ++i;") //
        .ignores("f(array[i]); ++i;") //
    ;
  }
  @Override public Tip tip(final ArrayAccess a) {
    return checkInput(a) || !prerequisite(a) ? null : new Tip(description(a), getClass(), a) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        final PostfixExpression newpost = a.getAST().newPostfixExpression();
        newpost.setOperand(copy.of(a.getIndex()));
        newpost.setOperator(Operator.INCREMENT);
        r.replace(a.getIndex(), newpost, g);
        r.remove(extract.nextStatement(a), g);
      }
    };
  }
  private static boolean checkInput(final ArrayAccess a) {
    if (a == null || extract.nextPrefix(a) == null || extract.nextPrefix(a).getOperand() == null
        || !wizard.eq(extract.nextPrefix(a).getOperand(), a.getIndex()))
      return true;
    if (iz.assignment(a.getParent()) && iz.infixExpression(az.assignment(a.getParent()).getRightHandSide()))
      for (final Expression ¢ : extract.allOperands(az.infixExpression(az.assignment(a.getParent()).getRightHandSide())))
        if (iz.arrayAccess(¢))
          return true;
    if (!iz.infixExpression(a.getParent()) || !iz.assignment(a.getParent().getParent()))
      return false;
    final Int $ = new Int();
    final List<Expression> xs = extract.allOperands(az.infixExpression(a.getParent()));
    xs.add(az.assignment(a.getParent().getParent()).getLeftHandSide());
    xs.stream().filter(iz::arrayAccess).forEach(λ -> ++$.inner);
    return $.inner != 1;
  }
  protected static boolean prerequisite(final ArrayAccess a) {
    if (a == null)
      return false;
    final SimpleName $ = az.simpleName(a.getIndex());
    final Expression bb = az.expression(a.getParent());
    return bb != null && $ != null && (iz.assignment(bb) && (!left(az.assignment(bb)).equals(a) || !iz.containsName($, right(az.assignment(bb))))
        || iz.infixExpression(bb) && (!left(az.infixExpression(bb)).equals(a) || !iz.containsName($, right(az.infixExpression(bb)))));
  }
}
