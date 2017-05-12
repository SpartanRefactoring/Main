package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.java.*;
import il.org.spartan.spartanizer.tipping.*;

/** convert {@code switch (x) { case a: } switch(x) { default: (some commands) }
 * } into {@code
 * (some commands)
 * } . Tested in {@link Issue0233}
 * @author Yuval Simon
 * @since 2016-11-20 */
public final class SwitchEmpty extends CarefulTipper<SwitchStatement>//
    implements TipperCategory.SyntacticBaggage {
  private static final long serialVersionUID = 0x26848136FD202ECEL;

  @Override public Tip tip(final SwitchStatement s) {
    return new Tip(description(s), getClass(), s) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        final List<Statement> ll = statements(s);
        final ExpressionStatement ss = s.getAST().newExpressionStatement(copy.of(expression(s)));
        if (noSideEffectCommands(s)) {
          r.remove(s, g);
          if (!sideEffects.free(expression(s)))
            r.replace(s, ss, g);
          return;
        }
        if (iz.breakStatement(the.lastOf(ll)))
          ll.remove(ll.size() - 1);
        ll.remove(0);
        r.replace(s, make.ast((sideEffects.free(expression(s)) ? "" : ss + "") + statementsToString(ll)), g);
      }
    };
  }

  static String statementsToString(final Iterable<Statement> ¢) {
    final StringBuilder $ = new StringBuilder();
    ¢.forEach($::append);
    return $ + "";
  }

  @Override protected boolean prerequisite(final SwitchStatement ¢) {
    final List<SwitchCase> $ = extract.switchCases(¢);
    return noSideEffectCommands(¢) || $.isEmpty() || $.size() == 1 && the.headOf($).isDefault();
  }

  static boolean noSideEffectCommands(final SwitchStatement s) {
    return statements(s).stream().allMatch(λ -> iz.switchCase(λ) || iz.breakStatement(λ));
  }

  @Override public String description(@SuppressWarnings("unused") final SwitchStatement __) {
    return "Remove empty switch statement or switch statement with only default case";
  }
}
