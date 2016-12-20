package il.org.spartan.athenizer.inflate.expanders;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;

public class CasesSplit extends CarefulTipper<SwitchStatement> {
  @Override public String description(@SuppressWarnings("unused") final SwitchStatement __) {
    return "split cases within switch";
  }

  @Override public Tip tip(final SwitchStatement ¢) {
    @SuppressWarnings("unchecked") final List<Statement> $ = getAdditionalStatements(¢.statements(), caseWithNoSequencer(¢));
    final Statement n = (Statement) ¢.statements().get(¢.statements().indexOf($.get(0)) - 1);
    return new Tip(description(¢), ¢, this.getClass()) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        @SuppressWarnings("static-access") final ListRewrite l = r.getListRewrite(¢, ¢.STATEMENTS_PROPERTY);
        for (@SuppressWarnings("hiding") final Statement ¢ : $)
          l.insertBefore(duplicate.of(¢), n, g);
        if (!iz.sequencer($.get($.size() - 1)))
          l.insertBefore(¢.getAST().newBreakStatement(), n, g);
      }
    };
  }

  @Override protected boolean prerequisite(final SwitchStatement ¢) {
    return caseWithNoSequencer(¢) != null;
  }

  @SuppressWarnings("unchecked") private static SwitchCase caseWithNoSequencer(final SwitchStatement x) {
    SwitchCase $ = null;
    for (Statement ¢ : (List<Statement>) x.statements())
      if (iz.sequencer(¢))
        $ = null;
      else if (¢ instanceof SwitchCase) {
        if ($ != null)
          return $;
        $ = az.switchCase(¢);
      }
    return null;
  }

  private static List<Statement> getAdditionalStatements(final List<Statement> ss, final SwitchCase c) {
    List<Statement> $ = new LinkedList<>();
    boolean additionalStatements = false;
    for (final Statement ¢ : ss.subList(ss.indexOf(c) + 1, ss.size())) {
      if (¢ instanceof SwitchCase)
        additionalStatements = true;
      else if (additionalStatements)
        $.add(¢);
      if (iz.sequencer(¢))
        return $;
    }
    return $;
  }
}
