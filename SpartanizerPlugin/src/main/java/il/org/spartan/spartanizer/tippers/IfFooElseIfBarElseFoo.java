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
import il.org.spartan.spartanizer.issues.*;
import il.org.spartan.spartanizer.java.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.utils.*;

/** Merges if and else blocks when they are the same and there is an else if
 * clause. Tests in {@link Issue1129}
 * @author Niv Shalmon <tt>shalmon.niv@gmail.com</tt>
 * @since 2017-03-26 */
public class IfFooElseIfBarElseFoo extends ReplaceCurrentNode<IfStatement> //
    implements TipperCategory.Inlining {
  private static final long serialVersionUID = -8897742439908596829L;

  @Override public Example[] examples() {
    return new Example[] { //
        convert("if(a) f(); else if(b) g(); else f();")//
            .to("if(a || !b) f(); else if(b) g();"), //
        ignores("if(a) f(); else if (x()) g(); else f();")//
    };
  }

  @Override public ASTNode replacement(final IfStatement s) {
    final IfStatement s2 = copy.of(az.ifStatement(s.getElseStatement())), $ = copy.of(s);
    $.setExpression(operands(s.getExpression(), operand(s2.getExpression()).to(NOT))//
        .to(CONDITIONAL_OR));
    s2.setElseStatement(null);
    $.setElseStatement(s2);
    final IfStatement p = az.ifStatement(s.getParent());
    if (p == null || s.getLocationInParent().equals(IfStatement.ELSE_STATEMENT_PROPERTY))
      return $;
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
    return !iz.blockEssential(extract.statements(bl).get(0)) ? $ : subject.statement($).toBlock();
  }

  @Override protected boolean prerequisite(final IfStatement ¢) {
    final IfStatement $ = az.ifStatement(¢.getElseStatement());
    return $ != null && wizard.eq(¢.getThenStatement(), $.getElseStatement()) //
        && sideEffects.free($.getExpression());
  }

  @Override public String description(@SuppressWarnings("unused") final IfStatement __) {
    return "Merges if and else blocks when they are the same and there is an else if clause.";
  }
}
