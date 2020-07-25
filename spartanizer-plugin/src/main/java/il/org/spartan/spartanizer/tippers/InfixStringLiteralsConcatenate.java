package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.navigate.step.operator;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.StringLiteral;

import fluent.ly.the;
import il.org.spartan.spartanizer.ast.factory.copy;
import il.org.spartan.spartanizer.ast.factory.subject;
import il.org.spartan.spartanizer.ast.navigate.hop;
import il.org.spartan.spartanizer.ast.navigate.op;
import il.org.spartan.spartanizer.ast.safety.az;
import il.org.spartan.spartanizer.ast.safety.iz;
import il.org.spartan.spartanizer.tipping.ReplaceCurrentNode;
import il.org.spartan.spartanizer.tipping.categories.Category;

/** Concatenate same line string literal
 * @author Doron Mehsulam {@code doronmmm@hotmail.com}
 * @author Niv Shalmon {@code shalmon.niv@gmail.com}
 * @since 2017-03-22 */
public class InfixStringLiteralsConcatenate extends ReplaceCurrentNode<InfixExpression> //
    implements Category.Theory.Strings {
  private static final long serialVersionUID = -0x3B6F5A31A382408AL;

  @Override public ASTNode replacement(final InfixExpression x) {
    final List<Expression> es = hop.operands(x);
    Expression prev = copy.of(the.firstOf(es));
    final CompilationUnit u = az.compilationUnit(x.getRoot());
    final List<Expression> ret = new LinkedList<>();
    for (final Expression e : the.tailOf(es))
      if (u.getLineNumber(prev.getStartPosition()) != u.getLineNumber(e.getStartPosition()) || !iz.stringLiteral(prev) || !iz.stringLiteral(e)) {
        ret.add(prev);
        prev = copy.of(e);
      } else {
        final StringLiteral l = az.stringLiteral(prev);
        l.setLiteralValue(l.getLiteralValue() + az.stringLiteral(e).getLiteralValue());
      }
    ret.add(prev);
    if (ret.size() >= 2)
      return subject.operands(ret).to(op.PLUS2);
    final StringLiteral $ = x.getAST().newStringLiteral();
    $.setLiteralValue(az.stringLiteral(the.firstOf(ret)).getLiteralValue());
    return $;
  }
  @Override protected boolean prerequisite(final InfixExpression x) {
    if (operator(x) != op.PLUS2)
      return false;
    final List<Expression> es = hop.operands(x);
    Expression prev = the.firstOf(es);
    if (!iz.compilationUnit(x.getRoot()))
      return false;
    final CompilationUnit u = az.compilationUnit(x.getRoot());
    for (final Expression ¢ : the.tailOf(es)) {
      if (u.getLineNumber(prev.getStartPosition()) == u.getLineNumber(¢.getStartPosition()) && iz.stringLiteral(prev) && iz.stringLiteral(¢))
        return true;
      prev = ¢;
    }
    return false;
  }
  @Override public String description(@SuppressWarnings("unused") final InfixExpression __) {
    return "Replace concatenated string literals with one";
  }
}
