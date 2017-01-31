package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.lisp.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;

/** Concat some strings to one string
 *
 * <code>
 * "ab" + "c" + "de"
 * </code>
 *
 * to
 *
 * <code>
 * "abcde"
 * </code>
 *
 * @author Dor Ma'ayan
 * @author Nov Shalmon
 * @since 2016-09-04 */
public final class InfixExpressionConcatentateCompileTime extends ReplaceCurrentNode<InfixExpression>//
    implements TipperCategory.Unite {
  @Override public String description() {
    return "Concat the strings to a one string";
  }

  @Override public String description(@SuppressWarnings("unused") final InfixExpression __) {
    return "Concat the string literals to a single string";
  }

  @Override public ASTNode replacement(final InfixExpression x) {
    if (x.getOperator() != wizard.PLUS2)
      return null;
    final List<Expression> $ = extract.allOperands(x);
    assert $.size() >= 2;
    boolean isChanged = false;
    for (int i = 0; i < $.size() - 1;)
      if ($.get(i).getNodeType() != ASTNode.STRING_LITERAL || $.get(i + 1).getNodeType() != ASTNode.STRING_LITERAL)
        ++i;
      else {
        isChanged = true;
        final StringLiteral l = x.getAST().newStringLiteral();
        l.setLiteralValue(((StringLiteral) $.get(i)).getLiteralValue() + ((StringLiteral) $.get(i + 1)).getLiteralValue());
        $.remove(i);
        $.remove(i);
        $.add(i, l);
      }
    if (!isChanged)
      return null;
    assert !$.isEmpty();
    return $.size() <= 1 ? first($) : subject.operands($).to(wizard.PLUS2);
  }
}
