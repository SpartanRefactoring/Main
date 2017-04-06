package il.org.spartan.spartanizer.tippers;

import static org.eclipse.jdt.core.dom.ASTNode.*;

import static il.org.spartan.lisp.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import java.util.*;
import java.util.stream.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.PrefixExpression.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.issues.*;
import il.org.spartan.spartanizer.patterns.*;
import il.org.spartan.utils.*;

/** convert {@code switch (x) { case a: (commands) break; default: (other
 * commands) } } into {@code if(x == a) { (commands) } else { (other commands) }
 * } . Tested in {@link Issue0916}
 * @author Yuval Simon
 * @since 2016-12-18 */
public class SwitchWithOneCaseToIf extends SwitchStatementAbstractPattern//
    implements TipperCategory.Unite {
  private static final long serialVersionUID = 0x513C764E326D1A98L;

  @Override public String description(@SuppressWarnings("unused") final SwitchStatement __) {
    return "Convert switch statement to if-else statement";
  }

  public SwitchWithOneCaseToIf() {
    andAlso(Proposition.that("Exactly twow cases", () -> (cases.size() == 2)));
    andAlso(Proposition.that("Has default case", () -> (first(cases).isDefault() || last(cases).isDefault())));
    andAlso(Proposition.that("Different branches", () -> {
      assert cases.size() == 2; // I assume this proposition is checked only if the first propositions is true
      return statements.subList(statements.indexOf(cases.get(0)), statements.indexOf(cases.get(1))).stream().anyMatch(iz::sequencerComplex);
    }));
  }

  @Override protected ASTRewrite go(final ASTRewrite $, final TextEditGroup g) {
    final boolean firstDefault = first(cases()).isDefault();
    final SwitchCase thenCase = firstDefault ? last(cases()) : first(cases());
    List<Statement> l1 = removeBreaks(statements.subList(1, statements.indexOf(last(cases())))),
        l2 = removeBreaks(statements.subList(statements.indexOf(last(cases())) + 1, statements.size()));
    if (firstDefault) {
      final List<Statement> tmp = l1;
      l1 = l2;
      l2 = tmp;
    }
    if (l1.isEmpty() && l2.isEmpty())
      $.remove(current, g);
    else
      $.replace(current,
          l1.isEmpty()
              ? subject.pair(subject.ss(l2).toBlock(), null)
                  .toIf(subject.operand(subject.pair(expression, thenCase.getExpression()).to(InfixExpression.Operator.EQUALS)).to(Operator.NOT))
              : subject.pair(subject.ss(l1).toBlock(), l2.isEmpty() ? null : subject.ss(l2).toBlock())
                  .toIf(subject.pair(expression, thenCase.getExpression()).to(InfixExpression.Operator.EQUALS)),
          g);
    return $;
  }

  private static List<Statement> removeBreaks(final List<Statement> src) {
    return src.stream().map(SwitchWithOneCaseToIf::cleanBreaks).filter(λ -> !iz.emptyStatement(λ)).collect(Collectors.toList());
  }

  private static Statement cleanBreaks(final Statement ¢) {
    if (¢ == null)
      return null;
    switch (¢.getNodeType()) {
      case BREAK_STATEMENT:
        return ¢.getAST().newEmptyStatement();
      case BLOCK:
        return subject.ss(statements(az.block(¢)).stream().map(SwitchWithOneCaseToIf::cleanBreaks).collect(Collectors.toList())).toBlock();
      case IF_STATEMENT:
        final IfStatement $ = copy.of(az.ifStatement(¢));
        $.setThenStatement(cleanBreaks(then($)));
        $.setElseStatement(cleanBreaks(elze($)));
        return $;
      default:
        return copy.of(¢);
    }
  }
}
