package il.org.spartan.spartanizer.tippers;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.PostfixExpression.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.spartanizer.utils.*;

/** convert : {@code
 * array[i];
 * ++i;
 * } to : {@code
 * array[i++];
 * }
 * @author Dor Ma'ayan
 * @since 25-11-2016 */
public final class ArrayAccessAndIncrement extends EagerTipper<ArrayAccess>//
    implements TipperCategory.Inlining {
  private static final long serialVersionUID = -5043727367787844893L;

  @Override public String description(@SuppressWarnings("unused") final ArrayAccess ¢) {
    return "Inline the prefix expression after the access to the array";
  }

  @Override public Tip tip(final ArrayAccess a) {
    return checkInput(a) || !prerequisite(a) ? null : new Tip(description(a), a, getClass()) {
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
        || !wizard.same(extract.nextPrefix(a).getOperand(), a.getIndex()))
      return true;
    if (iz.assignment(a.getParent()) && iz.infixExpression(az.assignment(a.getParent()).getRightHandSide()))
      for (final Expression ¢ : extract.allOperands(az.infixExpression(az.assignment(a.getParent()).getRightHandSide())))
        if (iz.arrayAccess(¢))
          return true;
    if (!iz.infixExpression(a.getParent()) || !iz.assignment(a.getParent().getParent()))
      return false;
    final Int $ = new Int();
    final List<Expression> lst = extract.allOperands(az.infixExpression(a.getParent()));
    lst.add(az.assignment(a.getParent().getParent()).getLeftHandSide());
    lst.stream().filter(iz::arrayAccess).forEach(λ -> ++$.inner);
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
