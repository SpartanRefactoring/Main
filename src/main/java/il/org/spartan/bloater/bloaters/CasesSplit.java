package il.org.spartan.bloater.bloaters;

import static il.org.spartan.lisp.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;
import org.jetbrains.annotations.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.zoomer.zoomin.expanders.*;

/** Expand cases in a {@link SwitchStatement}: {@code switch (x) { case 1: f(1);
 * case 2: f(2); throw new Exception(); default: f(3); } } turns into
 * {@code switch (x) { case 1: f(1); f(2); throw new Exception(); case 2: f(2);
 * throw new Exception(); default: f(3); } } Test file: {@link Issue0977}
 * @author Ori Roth <tt>ori.rothh@gmail.com</tt>
 * @since 2016-12-28 */
public class CasesSplit extends CarefulTipper<SwitchStatement>//
    implements TipperCategory.Bloater {
  private static final long serialVersionUID = 5172128174751851623L;

  @Override @NotNull public String description(@SuppressWarnings("unused") final SwitchStatement __) {
    return "split cases within switch";
  }

  @Override @NotNull public Tip tip(@NotNull final SwitchStatement s) {
    @NotNull final List<Statement> $ = getAdditionalStatements(statements(s), caseWithNoSequencer(s));
    @NotNull final Statement n = (Statement) s.statements().get(s.statements().indexOf(first($)) - 1);
    return new Tip(description(s), s, getClass()) {
      @Override public void go(@NotNull final ASTRewrite r, final TextEditGroup g) {
        final ListRewrite l = r.getListRewrite(s, SwitchStatement.STATEMENTS_PROPERTY);
        $.forEach(λ -> l.insertBefore(copy.of(λ), n, g));
        if (!iz.sequencerComplex(last($)))
          l.insertBefore(s.getAST().newBreakStatement(), n, g);
      }
    };
  }

  @Override protected boolean prerequisite(final SwitchStatement ¢) {
    return caseWithNoSequencer(¢) != null;
  }

  private static SwitchCase caseWithNoSequencer(final SwitchStatement x) {
    @Nullable SwitchCase $ = null;
    for (final Statement ¢ : statements(x)) // TOUGH
      if (iz.sequencerComplex(¢))
        $ = null;
      else if (¢ instanceof SwitchCase) {
        if ($ != null)
          return $;
        $ = az.switchCase(¢);
      }
    return null;
  }

  @NotNull private static List<Statement> getAdditionalStatements(@NotNull final List<Statement> ss, final SwitchCase c) {
    @NotNull final List<Statement> $ = new ArrayList<>();
    boolean additionalStatements = false;
    for (final Statement ¢ : ss.subList(ss.indexOf(c) + 1, ss.size())) {
      if (¢ instanceof SwitchCase)
        additionalStatements = true;
      else if (additionalStatements)
        $.add(¢);
      if (iz.sequencerComplex(¢))
        return $;
    }
    return $;
  }
}
