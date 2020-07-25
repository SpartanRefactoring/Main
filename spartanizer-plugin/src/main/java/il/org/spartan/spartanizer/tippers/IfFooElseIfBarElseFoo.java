package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.factory.subject.operand;
import static il.org.spartan.spartanizer.ast.factory.subject.operands;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.CONDITIONAL_OR;
import static org.eclipse.jdt.core.dom.PrefixExpression.Operator.NOT;

import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.text.edits.TextEditGroup;

import il.org.spartan.spartanizer.ast.factory.copy;
import il.org.spartan.spartanizer.ast.factory.subject;
import il.org.spartan.spartanizer.ast.navigate.extract;
import il.org.spartan.spartanizer.ast.navigate.wizard;
import il.org.spartan.spartanizer.ast.safety.az;
import il.org.spartan.spartanizer.ast.safety.iz;
import il.org.spartan.spartanizer.issues.Issue1129;
import il.org.spartan.spartanizer.java.sideEffects;
import il.org.spartan.spartanizer.tipping.categories.Category;
import il.org.spartan.utils.Examples;

/** Merges if and else blocks when they are the same and there is an else if
 * clause. Tests in {@link Issue1129}
 * @author Niv Shalmon <tt>shalmon.niv@gmail.com</tt>
 * @since 2017-03-26 */
public class IfFooElseIfBarElseFoo extends IfElseIfAbstractPattern //
    implements Category.Inlining {
  private static final long serialVersionUID = -0x7B7B219B59AB5C5DL;

  public IfFooElseIfBarElseFoo() {
    andAlso("if and final else must be the same", //
        () -> wizard.eq(then, elzeElze));
    andAlso("else if expression must have no side effects", //
        () -> sideEffects.free(elzeIfCondition));
  }
  @Override public String description() {
    return "Merges if and else blocks when they are the same and there is an else if clause.";
  }
  @Override public Examples examples() {
    return //
    convert("if(a) f(); else if(b) g(); else f();")//
        .to("if(a || !b) f(); else if(b) g();")//
        .ignores("if(a) f(); else if (x()) g(); else f();");
  }
  @Override protected ASTRewrite go(final ASTRewrite ret, final TextEditGroup g) {
    final IfStatement s2 = copy.of(az.ifStatement(elze)), $ = copy.of(current);
    $.setExpression(operands(condition, operand(elzeIfCondition).to(NOT))//
        .to(CONDITIONAL_OR));
    s2.setElseStatement(null);
    $.setElseStatement(s2);
    final IfStatement p = az.ifStatement(current.getParent());
    if (p == null || current.getLocationInParent().equals(IfStatement.ELSE_STATEMENT_PROPERTY))
      ret.replace(current, $, g);
    else {
      final Block bl = subject.statement($).toBlock();
      IfStatement originalParent = p, newParent = copy.of(originalParent);
      newParent.setThenStatement(bl);
      while (originalParent.getLocationInParent().equals(IfStatement.THEN_STATEMENT_PROPERTY)) {
        final Statement child = newParent;
        originalParent = az.ifStatement(originalParent.getParent());
        newParent = copy.of(originalParent);
        newParent.setThenStatement(child);
      }
      ret.replace(current, !iz.blockEssential(extract.statements(bl).get(0)) ? $ : subject.statement($).toBlock(), g);
    }
    return ret;
  }
}
