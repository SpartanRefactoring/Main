package il.org.spartan.spartanizer.tippers;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.PostfixExpression.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;

/** convert :
 *
 * <pre>
 * array[i];
 * ++i;
 * </pre>
 *
 * to :
 *
 * <pre>
 * array[i++];
 * </pre>
 *
 * @author Dor Ma'ayan
 * @since 25-11-2016 */
public final class InliningPrefix extends EagerTipper<ArrayAccess> implements TipperCategory.Inlining {
  @Override public String description(@SuppressWarnings("unused") final ArrayAccess ¢) {
    return "Iniline the prefix expression after the access to the array";
  }

  @Override public Tip tip(final ArrayAccess a) {
    return checkInput(a) ? null : new Tip(description(a), a, getClass()) {
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
    int $ = 0;
    final List<Expression> lst = extract.allOperands(az.infixExpression(a.getParent()));
    lst.add(az.assignment(a.getParent().getParent()).getLeftHandSide());
    for (final Expression ¢ : lst)
      if (iz.arrayAccess(¢))
        ++$;
    return $ != 1;
  }
}
