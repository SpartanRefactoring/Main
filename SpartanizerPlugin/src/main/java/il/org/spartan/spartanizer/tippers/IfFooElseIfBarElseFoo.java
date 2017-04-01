package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.factory.subject.*;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;
import static org.eclipse.jdt.core.dom.PrefixExpression.Operator.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.issues.*;
import il.org.spartan.spartanizer.java.*;
import il.org.spartan.spartanizer.patterns.*;
import il.org.spartan.utils.*;

/** Merges if and else blocks when they are the same and there is an else if
 * clause. Tests in {@link Issue1129}
 * @author Niv Shalmon <tt>shalmon.niv@gmail.com</tt>
 * @since 2017-03-26 */
public class IfFooElseIfBarElseFoo extends IfElseIfAbstractPattern //
    implements TipperCategory.Inlining {
  private static final long serialVersionUID = -8897742439908596829L;

  public IfFooElseIfBarElseFoo() {
    andAlso("if and final else must be the same",() -> wizard.eq(then, elzeElze));
    andAlso("else if expression must have no side effects",() ->//
      sideEffects.free(elzeIfCondition));
  }
  
  @Override public Examples examples() {
    return convert("if(a) f(); else if(b) g(); else f();")//
            .to("if(a || !b) f(); else if(b) g();");
  }

  @Override public String description(@SuppressWarnings("unused") final IfStatement __) {
    return "Merges if and else blocks when they are the same and there is an else if clause.";
  }

  @Override protected ASTRewrite go(ASTRewrite r, TextEditGroup g) {
    final IfStatement s2 = copy.of(az.ifStatement(elze)), $ = copy.of(current);
    $.setExpression(operands(condition, operand(elzeIfCondition).to(NOT))//
        .to(CONDITIONAL_OR));
    s2.setElseStatement(null);
    $.setElseStatement(s2);
    final IfStatement p = az.ifStatement(current.getParent());
    if (p == null || current.getLocationInParent().equals(IfStatement.ELSE_STATEMENT_PROPERTY)){
      r.replace(current, $, g);
      return r;
    }
    final Block bl = subject.statement($).toBlock();
    IfStatement originalParent = p, newParent = copy.of(originalParent);
    newParent.setThenStatement(bl);
    /* copies the tree as long as only IfStatements are the parent and the current node
     * is part of the then branch, so
     * iz.blockEssential can tell us whether the block is essential or not. */
    while (originalParent.getLocationInParent().equals(IfStatement.THEN_STATEMENT_PROPERTY)) {
      Statement child = newParent;
      originalParent = az.ifStatement(originalParent.getParent());
      newParent = copy.of(originalParent);
      newParent.setThenStatement(child);
    }
    r.replace(current, !iz.blockEssential(extract.statements(bl).get(0)) ? $ //
        : subject.statement($).toBlock(), g);
    return r;
  }
}
