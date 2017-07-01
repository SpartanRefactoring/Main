package il.org.spartan.spartanizer.tippers;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.spartanizer.tipping.categories.*;

/** Concat some strings to one string {@code
 * "abcde"
 * } to {@code
 * "abcde"
 * }
 * @author Dor Ma'ayan
 * @author Nov Shalmon
 * @since 2016-09-04 */
public final class InfixExpressionConcatentateCompileTime extends ReplaceCurrentNode<InfixExpression>//
    implements Category.Collapse {
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
    final List<Expression> ret = extract.allOperands(x);
    assert ret.size() >= 2;
    boolean clean = true;
    for (int i = 0; i < ret.size() - 1;)
      if (ret.get(i).getNodeType() != ASTNode.STRING_LITERAL || ret.get(i + 1).getNodeType() != ASTNode.STRING_LITERAL)
        ++i;
      else {
        clean = false;
        final StringLiteral l = x.getAST().newStringLiteral();
        l.setLiteralValue(((StringLiteral) ret.get(i)).getLiteralValue() + ((StringLiteral) ret.get(i + 1)).getLiteralValue());
        ret.remove(i);
        ret.remove(i);
        ret.add(i, l);
      }
    if (clean)
      return null;
    assert !ret.isEmpty();
    return ret.size() <= 1 ? the.firstOf(ret) : subject.operands(ret).to(op.PLUS2);
  }
}
