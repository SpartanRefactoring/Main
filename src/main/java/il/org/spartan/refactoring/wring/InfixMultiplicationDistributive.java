package il.org.spartan.refactoring.wring;

import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.InfixExpression.*;

import il.org.spartan.refactoring.assemble.*;
import il.org.spartan.refactoring.ast.*;
import il.org.spartan.refactoring.utils.*;
import il.org.spartan.refactoring.wring.Wring.*;

/** Apply the distributive rule to multiplication:
 *
 * <pre>
* <b>a*b + a*c</b>
 * </pre>
 *
 * to
 *
 * <pre>
* <b>a * (b + c)</b>
 * </pre>
 *
 * .
 * @author Matteo Orru'
 * @since 2015-07-17 */
public final class InfixMultiplicationDistributive extends ReplaceCurrentNode<InfixExpression> implements Kind.DistributiveRefactoring {
  private static boolean IsSimpleMultiplication(final Expression $) {
    return !iz.simpleName($) && ((InfixExpression) $).getOperator() == TIMES;
  }

  @Override public String description() {
    return "a*b + a*c => a * (b + c)";
  }

  @Override ASTNode replacement(final InfixExpression e) {
    // TODO: YG/Matteo: the following is a hack
    if (e == null)
      return null;
    return e.getOperator() != PLUS ? null : replacement(extract.allOperands(e));
  }

  @Override ASTNode replacement(final InfixExpression x) {
    return x.getOperator() != PLUS ? null : replacement(extract.allOperands(x));
  }

  @Override boolean scopeIncludes(final InfixExpression $) {
    return $ != null && iz.infixPlus($) && IsSimpleMultiplication(step.left($)) && IsSimpleMultiplication(step.right($)); // super.scopeIncludes($);
  }

  private void addCommon(final Expression op, final List<Expression> common) {
    addNewInList(op, common);
  }

  private void addDifferent(final Expression op, final List<Expression> different) {
    addNewInList(op, different);
  }

  private void addNewInList(final Expression item, final List<Expression> xs) {
    if (!isIn(item, xs))
      xs.add(item);
  }

  @SuppressWarnings("static-method") private boolean isIn(final Expression op, final List<Expression> allOperands) {
    for (final Expression $ : allOperands)
      if (wizard.same(op, $))
        return true;
    return false;
  }

  @SuppressWarnings("static-method") private void removeElFromList(final List<Expression> items, final List<Expression> from) {
    for (final Expression item : items)
      from.remove(item);
  }

  @SuppressWarnings("static-method") private List<Expression> removeFirstEl(final List<Expression> xs) {
    final List<Expression> $ = new ArrayList<>(xs);
    $.remove($.get(0));// remove first
    return $;
  }

  private ASTNode replacement(final InfixExpression e1, final InfixExpression e2) {
    assert e1 != null;
    assert e2 != null;
    final List<Expression> common = new ArrayList<>();
    final List<Expression> different = new ArrayList<>();
    for (final Expression op : extract.allOperands(e1))
      (isIn(op, extract.allOperands(e2)) ? common : different).add(op);
    for (final Expression op : extract.allOperands(e2)) // [a c]
      if (!isIn(op, common))
        different.add(op);
    if (!common.isEmpty())
      different.remove(common);
    assert lisp.first(common) != null;
    assert lisp.first(different) != null;
    assert lisp.second(different) != null;
    return subject.pair(lisp.first(common), //
        subject.pair(//
            lisp.first(different), lisp.second(different)//
        ).to(//
            Operator.PLUS)//
    ).to(//
        Operator.TIMES//
    );
  }

  @SuppressWarnings("static-method") private boolean isIn(final Expression op, final List<Expression> allOperands) {
    for (final Expression $ : allOperands)
      if (same(op, $))
        return true;
    return false;
  }

}
