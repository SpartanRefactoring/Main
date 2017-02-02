package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.lisp.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.java.*;
import il.org.spartan.spartanizer.tipping.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** convert {@code switch (x) { case a: } switch(x) { default: (some commands) }
 * } into {@code
 * (some commands)
 * } . Tested in {@link Issue233}
 * @author Yuval Simon
 * @since 2016-11-20 */
public final class SwitchEmpty extends CarefulTipper<SwitchStatement>//
    implements TipperCategory.SyntacticBaggage {
  @Nullable
  @Override public Tip tip(@NotNull final SwitchStatement s) {
    return new Tip(description(s), s, getClass()) {
      @Override public void go(@NotNull final ASTRewrite r, final TextEditGroup g) {
        final List<Statement> ll = statements(s);
        final ExpressionStatement ss = s.getAST().newExpressionStatement(copy.of(expression(s)));
        if (noSideEffectCommands(s)) {
          r.remove(s, g);
          if (!sideEffects.free(expression(s)))
            r.replace(s, ss, g);
          return;
        }
        if (iz.breakStatement(last(ll)))
          ll.remove(ll.size() - 1);
        ll.remove(0);
        r.replace(s, wizard.ast((sideEffects.free(expression(s)) ? "" : ss + "") + statementsToString(ll)), g);
      }
    };
  }

  @NotNull
  static String statementsToString(@NotNull final List<Statement> ¢) {
    final StringBuilder $ = new StringBuilder();
    ¢.forEach($::append);
    return $ + "";
  }

  @Override protected boolean prerequisite(final SwitchStatement ¢) {
    final List<SwitchCase> $ = extract.switchCases(¢);
    return noSideEffectCommands(¢) || $.isEmpty() || $.size() == 1 && first($).isDefault();
  }

  static boolean noSideEffectCommands(final SwitchStatement s) {
    return statements(s).stream().allMatch(λ -> iz.switchCase(λ) || iz.breakStatement(λ));
  }

  @Override public String description(@SuppressWarnings("unused") final SwitchStatement __) {
    return "Remove empty switch statement or switch statement with only default case";
  }
}
