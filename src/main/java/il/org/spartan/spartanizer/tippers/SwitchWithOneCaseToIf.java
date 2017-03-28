package il.org.spartan.spartanizer.tippers;

import static java.util.stream.Collectors.*;

import static il.org.spartan.lisp.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import java.util.*;
import java.util.stream.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.InfixExpression.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.issues.*;
import il.org.spartan.spartanizer.java.*;
import il.org.spartan.spartanizer.tipping.*;

/** convert {@code switch (x) { case a: (commands) break; default: (other
 * commands) } } into {@code if(x == a) { (commands) } else { (other commands) }
 * } . Tested in {@link Issue0916}
 * @author Yuval Simon
 * @since 2016-12-18 */
public class SwitchWithOneCaseToIf extends ReplaceCurrentNode<SwitchStatement>//
    implements TipperCategory.Unite {
  private static final long serialVersionUID = 0x513C764E326D1A98L;

  @Override public String description(@SuppressWarnings("unused") final SwitchStatement __) {
    return "Convert switch statement to if-else statement";
  }

  // TODO: Yuval Simon: this is one of the worst bits of code I have seen.
  // Simplify it massively. I suspect it is buggy. I do not trust any Switcht
  // transformation --yg
  @Override public ASTNode replacement(final SwitchStatement s) {
    final List<switchBranch> bs = switchBranch.intoBranches(s);
    if (bs.size() != 2)
      return null;
    final switchBranch first = first(bs);
    if (iz.stringLiteral(expression(first((!first.hasDefault() ? first : last(bs)).cases))))
      return null;
    final switchBranch last = last(bs);
    if (!first.hasDefault() && !last.hasDefault() || first.hasFallThrough() || last.hasFallThrough() || !first.hasStatements()
        || !last.hasStatements() || haz.sideEffects(expression(s)) && (first.hasDefault() ? last : first).cases.size() > 1)
      return null;
    final AST a = s.getAST();
    final Block b1 = a.newBlock(), b2 = a.newBlock();
    final switchBranch switchBranch = first.hasDefault() ? first : last(bs);
    statements(b2).addAll(removeBreakSequencer(switchBranch.statements));
    final il.org.spartan.spartanizer.ast.navigate.switchBranch branch = !first.hasDefault() ? first : last(bs);
    statements(b1).addAll(removeBreakSequencer(branch.statements));
    final Block $ = a.newBlock();
    statements($).add(subject.pair(b1, b2).toIf(makeFrom(s, branch.cases)));
    return $;
  }

  private List<Statement> statements;

  @SuppressWarnings("unused") private List<Statement> functionalCommands() {
    final List<Statement> $ = IntStream.range(0, statements.size() - 1).mapToObj(statements::get).collect(toList());
    if (!iz.breakStatement(lisp.last(statements)))
      $.add(lisp.last(statements));
    return $;
  }

  public boolean hasFallThrough() {
    return statements.stream().anyMatch(iz::switchCase);
  }

  public static Statement removeBreakSequencer(final Statement s) {
    if (s == null)
      return null;
    if (!iz.sequencerComplex(s, ASTNode.BREAK_STATEMENT))
      return copy.of(s);
    final AST a = s.getAST();
    Statement $ = null;
    if (iz.ifStatement(s)) {
      final IfStatement t = az.ifStatement(s);
      $ = subject.pair(removeBreakSequencer(step.then(t)), removeBreakSequencer(step.elze(t))).toIf(copy.of(step.expression(t)));
    } else if (!iz.block(s)) {
      if (iz.breakStatement(s) && iz.block(s.getParent()))
        $ = a.newEmptyStatement();
    } else {
      final Block b = subject.ss(removeBreakSequencer(statements(az.block(s)))).toBlock();
      statements(b).addAll(removeBreakSequencer(statements(az.block(s))));
      $ = b;
    }
    return $;
  }

  public static List<Statement> removeBreakSequencer(final Iterable<Statement> ss) {
    final List<Statement> $ = new ArrayList<>();
    for (final Statement ¢ : ss) {
      final Statement s = removeBreakSequencer(¢);
      if (s != null)
        $.add(s);
    }
    return $;
  }

  private static InfixExpression makeFrom(final SwitchStatement s, final Iterable<SwitchCase> cs) {
    InfixExpression $ = null;
    for (final SwitchCase c : cs) {
      if (c.isDefault())
        continue;
      final InfixExpression n = subject.pair(expression(s), expression(c)).to(Operator.EQUALS);
      $ = $ == null ? n : subject.pair($, n).to(Operator.CONDITIONAL_OR);
    }
    return $;
  }
}
