package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.jetbrains.annotations.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;

/** concate same line string literal
 * @author Doron Mehsulam <tt>doronmmm@hotmail.com</tt>
 * @author Niv Shalmon <tt>shalmon.niv@gmail.com</tt>
 * @since 2017-03-22 */
public class InfixStringLiteralsConcatenate extends ReplaceCurrentNode<InfixExpression> //
    implements TipperCategory.NOP {
  private static final long serialVersionUID = -4282740939895750794L;

  @Override public ASTNode replacement(@NotNull final InfixExpression n) {
    @Nullable final List<Expression> es = hop.operands(n);
    Expression prev = copy.of(lisp.first(es));
    @Nullable final CompilationUnit u = az.compilationUnit(n.getRoot());
    @NotNull final List<Expression> es2 = new LinkedList<>();
    for (@NotNull final Expression e : lisp.rest(es))
      if (u.getLineNumber(prev.getStartPosition()) == u.getLineNumber(e.getStartPosition()) && iz.stringLiteral(prev) && iz.stringLiteral(e)) {
        @Nullable final StringLiteral l = az.stringLiteral(prev);
        @Nullable final StringLiteral l2 = az.stringLiteral(e);
        l.setLiteralValue(l.getLiteralValue() + l2.getLiteralValue());
      } else {
        es2.add(prev);
        prev = copy.of(e);
      }
    es2.add(prev);
    if (es2.size() >= 2)
      return subject.operands(es2).to(wizard.PLUS2);
    final StringLiteral $ = n.getAST().newStringLiteral();
    $.setLiteralValue(az.stringLiteral(lisp.first(es2)).getLiteralValue());
    return $;
  }

  @Override protected boolean prerequisite(@NotNull final InfixExpression n) {
    if (operator(n) != wizard.PLUS2)
      return false;
    @Nullable final List<Expression> es = hop.operands(n);
    Expression prev = lisp.first(es);
    if (!iz.compilationUnit(n.getRoot()))
      return false;
    @Nullable final CompilationUnit u = az.compilationUnit(n.getRoot());
    for (@NotNull final Expression e : lisp.rest(es)) {
      if (u.getLineNumber(prev.getStartPosition()) == u.getLineNumber(e.getStartPosition()) && iz.stringLiteral(prev) && iz.stringLiteral(e))
        return true;
      prev = e;
    }
    return false;
  }

  @Override public String description(@SuppressWarnings("unused") final InfixExpression __) {
    return "concate same line string literal";
  }
}
