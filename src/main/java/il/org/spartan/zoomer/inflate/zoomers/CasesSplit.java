package il.org.spartan.zoomer.inflate.zoomers;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.zoomer.zoomin.expanders.*;

/** Expand cases in a {@link SwitchStatement}: <code>
 * switch (x) {
 *   case 1:
 *     f(1);
 *   case 2:
 *     f(2);
 *     throw new Exception();
 *   default:
 *     f(3);
 * }
 * </code> turns into <code>
 * switch (x) {
 *   case 1:
 *     f(1);
 *     f(2);
 *     throw new Exception();
 *   case 2:
 *     f(2);
 *     throw new Exception();
 *   default:
 *     f(3);
 * }
 * </code> Test file: {@link Issue0977}
 * @author Ori Roth <tt>ori.rothh@gmail.com</tt>
 * @since 2016-12-28 */
public class CasesSplit extends CarefulTipper<SwitchStatement> implements TipperCategory.Expander {
  @Override public String description(@SuppressWarnings("unused") final SwitchStatement __) {
    return "split cases within switch";
  }
  // TODO: Ori Roth use class step if necessary and remove
  // @SuppressWarnings("unchecked") --yg
 
  @Override public Tip tip(final SwitchStatement ¢) {
    @SuppressWarnings("unchecked") final List<Statement> $ = getAdditionalStatements(¢.statements(), caseWithNoSequencer(¢));
    final Statement n = (Statement) ¢.statements().get(¢.statements().indexOf($.get(0)) - 1);
    return new Tip(description(¢), ¢, getClass()) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        final ListRewrite l = r.getListRewrite(¢, SwitchStatement.STATEMENTS_PROPERTY);
        for (@SuppressWarnings("hiding") final Statement ¢ : $)
          l.insertBefore(copy.of(¢), n, g);
        if (!iz.sequencerComplex($.get($.size() - 1)))
          l.insertBefore(¢.getAST().newBreakStatement(), n, g);
      }
    };
  }

  @Override protected boolean prerequisite(final SwitchStatement ¢) {
    return caseWithNoSequencer(¢) != null;
  }
  // TODO: Ori Roth use class step if necessary and remove
  // @SuppressWarnings("unchecked") --yg
 
  @SuppressWarnings("unchecked") private static SwitchCase caseWithNoSequencer(final SwitchStatement x) {
    SwitchCase $ = null;
    for (final Statement ¢ : (List<Statement>) x.statements())
      if (iz.sequencerComplex(¢))
        $ = null;
      else if (¢ instanceof SwitchCase) {
        if ($ != null)
          return $;
        $ = az.switchCase(¢);
      }
    return null;
  }

  private static List<Statement> getAdditionalStatements(final List<Statement> ss, final SwitchCase c) {
    final List<Statement> $ = new LinkedList<>();
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
