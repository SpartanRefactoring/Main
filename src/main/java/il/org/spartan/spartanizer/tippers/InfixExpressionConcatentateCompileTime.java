package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.lisp.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.tipping.*;
import nano.ly.*;

/** Concat some strings to one string {@code
 * "abcde"
 * } to {@code
 * "abcde"
 * }
 * @author Dor Ma'ayan
 * @author Nov Shalmon
 * @since 2016-09-04 */
public final class InfixExpressionConcatentateCompileTime extends ReplaceCurrentNode<InfixExpression>//
    implements TipperCategory.Unite {
  private static final long serialVersionUID = -0x349BFB6422E9DC98L;

  @Override public String description() {
    return "Concat the strings to a one string";
  }

  @Override public String description(@SuppressWarnings("unused") final InfixExpression __) {
    return "Concat the string literals to a single string";
  }

  @Override public ASTNode replacement(final InfixExpression x) {
    if (x.getOperator() != op.PLUS2)
      return null;
    final List<Expression> $ = extract.allOperands(x);
    assert $.size() >= 2;
    boolean clean = true;
    for (int i = 0; i < $.size() - 1;)
      if ($.get(i).getNodeType() != ASTNode.STRING_LITERAL || $.get(i + 1).getNodeType() != ASTNode.STRING_LITERAL)
        ++i;
      else {
        clean = false;
        final StringLiteral l = x.getAST().newStringLiteral();
        l.setLiteralValue(((StringLiteral) $.get(i)).getLiteralValue() + ((StringLiteral) $.get(i + 1)).getLiteralValue());
        $.remove(i);
        $.remove(i);
        $.add(i, l);
      }
    if (clean)
      return null;
    assert !$.isEmpty();
    return $.size() <= 1 ? the.headOf($) : subject.operands($).to(op.PLUS2);
  }
}
