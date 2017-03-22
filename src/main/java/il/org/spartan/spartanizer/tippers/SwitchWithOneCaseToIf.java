package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.navigate.switchBranch.*;

import static il.org.spartan.lisp.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.InfixExpression.*;
import org.jetbrains.annotations.*;

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
  private static final long serialVersionUID = 5853683693900339864L;

  @Override @NotNull public String description(@SuppressWarnings("unused") final SwitchStatement __) {
    return "Convert switch statement to if-else statement";
  }

  // TODO Yuval Simon: this is one of the worst bits of code I have seen.
  // Simplify it massively. I suspect it is buggy. I do not trust any Switcht
  // transformation --yg
  @Override @Nullable public ASTNode replacement(@NotNull final SwitchStatement s) {
    @NotNull final List<switchBranch> bs = switchBranch.intoBranches(s);
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

  @Nullable private static InfixExpression makeFrom(final SwitchStatement s, @NotNull final Iterable<SwitchCase> cs) {
    @Nullable InfixExpression $ = null;
    for (@NotNull final SwitchCase c : cs) {
      if (c.isDefault())
        continue;
      final InfixExpression n = subject.pair(expression(s), expression(c)).to(Operator.EQUALS);
      $ = $ == null ? n : subject.pair($, n).to(Operator.CONDITIONAL_OR);
    }
    return $;
  }
}
