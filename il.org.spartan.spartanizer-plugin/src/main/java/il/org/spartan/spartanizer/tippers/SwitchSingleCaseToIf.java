package il.org.spartan.spartanizer.tippers;

import static org.eclipse.jdt.core.dom.ASTNode.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import java.util.*;
import java.util.stream.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.PrefixExpression.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.issues.*;
import il.org.spartan.spartanizer.tipping.categories.*;
import il.org.spartan.utils.*;

/** See {@link #examples()} Tested in {@link Issue0916}
 * @author Yuval Simon
 * @since 2016-12-18 */
public class SwitchSingleCaseToIf extends Switch//
    implements Category.Collapse {
  private static final long serialVersionUID = 0x513C764E326D1A98L;

  @Override public String description() {
    return "Convert switch statement to if-else statement";
  }
  public SwitchSingleCaseToIf() {
    andAlso(Proposition.that("Exactly two cases", () -> (cases.size() == 2)));
    andAlso(Proposition.that("Has default case", () -> (the.firstOf(cases).isDefault() || the.lastOf(cases).isDefault())));
    andAlso(Proposition.that("Different branches", () -> {
      assert cases.size() == 2; // I assume this proposition is checked only if
                                // the first propositions is true
      return statements.subList(statements.indexOf(cases.get(0)), statements.indexOf(cases.get(1))).stream().anyMatch(iz::sequencerComplex);
    }));
  }
  @Override protected ASTRewrite go(final ASTRewrite ret, final TextEditGroup g) {
    final boolean firstDefault = the.firstOf(cases()).isDefault();
    final SwitchCase thenCase = firstDefault ? the.lastOf(cases()) : the.firstOf(cases());
    List<Statement> l1 = removeBreaks(statements.subList(1, statements.indexOf(the.lastOf(cases())))),
        l2 = removeBreaks(statements.subList(statements.indexOf(the.lastOf(cases())) + 1, statements.size()));
    if (firstDefault) {
      final List<Statement> tmp = l1;
      l1 = l2;
      l2 = tmp;
    }
    if (l1.isEmpty() && l2.isEmpty())
      ret.remove(current, g);
    else
      ret.replace(current,
          l1.isEmpty()
              ? subject.pair(subject.ss(l2).toBlock(), null)
                  .toIf(subject.operand(subject.pair(expression, thenCase.getExpression()).to(InfixExpression.Operator.EQUALS)).to(Operator.NOT))
              : subject.pair(subject.ss(l1).toBlock(), l2.isEmpty() ? null : subject.ss(l2).toBlock())
                  .toIf(subject.pair(expression, thenCase.getExpression()).to(InfixExpression.Operator.EQUALS)),
          g);
    return ret;
  }
  private static List<Statement> removeBreaks(final List<Statement> src) {
    return src.stream().map(SwitchSingleCaseToIf::cleanBreaks).filter(λ -> !iz.emptyStatement(λ)).collect(Collectors.toList());
  }
  // TODO Yuval Simon: use map-reduce
  private static Statement cleanBreaks(final Statement ¢) {
    if (¢ == null)
      return null;
    switch (¢.getNodeType()) {
      case BREAK_STATEMENT:
        return ¢.getAST().newEmptyStatement();
      case BLOCK:
        return subject.ss(statements(az.block(¢)).stream().map(SwitchSingleCaseToIf::cleanBreaks).collect(Collectors.toList())).toBlock();
      case IF_STATEMENT:
        final IfStatement ret = copy.of(az.ifStatement(¢));
        ret.setThenStatement(cleanBreaks(then(ret)));
        ret.setElseStatement(cleanBreaks(elze(ret)));
        return ret;
      default:
        return copy.of(¢);
    }
  }
  @Override public Examples examples() {
    return convert("switch(x){case a:f(); g();break; default:g();h();}")//
        .to("if(x==a){f();g();}else{g();h();}");
  }
}
