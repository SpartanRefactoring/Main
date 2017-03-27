package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.factory.subject.*;
import static il.org.spartan.utils.Example.*;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;
import static org.eclipse.jdt.core.dom.PrefixExpression.Operator.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.java.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.utils.*;

/** Merges if and else blocks when they are the same and there is an else if
 * clause.
 * @author Niv Shalmon <tt>shalmon.niv@gmail.com</tt>
 * @since 2017-03-26 */
public class IfFooElseIfBarElseFoo extends ReplaceCurrentNode<IfStatement> //
    implements TipperCategory.Inlining {
  private static final long serialVersionUID = -8897742439908596829L;

  @Override public Example[] examples() {
    return new Example[] { convert("if(a) f(); else if(b) g(); else f();")//
        .to("if(a || !b) f(); else if(b) g();"), ignores("if(a) f(); else if (x()) g(); else f();") };
  }

  @Override public ASTNode replacement(final IfStatement s) {
    final IfStatement s2 = copy.of(az.ifStatement(s.getElseStatement())), $ = copy.of(s);
    $.setExpression(operands(s.getExpression(), operand(s2.getExpression()).to(NOT))//
        .to(CONDITIONAL_OR));
    s2.setElseStatement(null);
    $.setElseStatement(s2);
    final IfStatement p = az.ifStatement(s.getParent());
    if (p == null || s.getLocationInParent() == IfStatement.ELSE_STATEMENT_PROPERTY)
      return $;
    final Block bl = subject.statement($).toBlock();
    boolean b;
    try {
      p.setThenStatement(bl);
      b = iz.blockEssential(extract.statements(bl).get(0));
    } finally {
      p.setThenStatement(s);
    }
    return !b ? $ : subject.statement($).toBlock();
  }

  @Override protected boolean prerequisite(final IfStatement s) {
    final IfStatement s2 = az.ifStatement(s.getElseStatement());
    return s2 != null && wizard.same(s.getThenStatement(), s2.getElseStatement()) //
        && sideEffects.free(s2.getExpression());
  }

  @Override public String description(@SuppressWarnings("unused") final IfStatement n) {
    return "Merges if and else blocks when they are the same and there is an else if clause.";
  }
}
