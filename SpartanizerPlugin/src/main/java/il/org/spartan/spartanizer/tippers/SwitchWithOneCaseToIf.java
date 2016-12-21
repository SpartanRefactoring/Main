package il.org.spartan.spartanizer.tippers;

import java.util.*;
import static il.org.spartan.lisp.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;
import static il.org.spartan.spartanizer.ast.navigate.step.*;

/** convert
 *
 * <pre>
* switch (x) {
* case a: (commands)
*   break;
* default: (other commands)
* }
 * </pre>
 *
 * into
 *
 * <pre>
* if(x == a) {
*   (commands)
* }
* else {
*   (other commands)
* }
 * </pre>
 *
 * . Tested in {@link Issue916}
 * @author Yuval Simon
 * @since 2016-12-18 */
public class SwitchWithOneCaseToIf extends ReplaceCurrentNode<SwitchStatement> implements TipperCategory.Collapse {
  @Override public ASTNode replacement(final SwitchStatement s) {
    if (s == null)
      return null;
    final List<SwitchCase> $ = extract.switchCases(s);
    final List<Statement> ll = statements(s);
    final int ind = firstBreakIndex(s);
    final int sz = ll.size();
    final String b1 = statementsToString(ll, 1, ind == -1 ? sz : ind);
    if ($.size() == 1)
      return wizard.ast("if(" + expression(s) + "==" + expression(first($)) + "){" + b1 + "}");
    final String b2 = statementsToString(ll, ind + 2, !iz.breakStatement(ll.get(sz - 1)) ? sz : sz - 1);
    return wizard.ast("if(" + (first($).isDefault() ? expression(s) + "==" + expression($.get(1)) + "){" + b2 + "} else{" + b1
        : expression(s) + "==" + expression(first($)) + "){" + b1 + "} else{" + b2) + "}");
  }

  @Override protected boolean prerequisite(final SwitchStatement s) {
    final List<SwitchCase> $ = extract.switchCases(s);
    final List<Statement> ll = statements(s);
    final int ind = firstBreakIndex(s);
    final int sz = ll.size();
    return $.size() == 2 && ind > 1 && ind < sz && !iz.switchCase(ll.get(sz - 2))
        || !iz.breakStatement(last(ll)) && !iz.switchCase(last(ll)) && (first($).isDefault() || $.get(1).isDefault());
  }

  @Override @SuppressWarnings("unused") public String description(final SwitchStatement __) {
    return "Convert switch statement to if-else statement";
  }

  private static int firstBreakIndex(final SwitchStatement s) {
    final List<Statement> l = step.statements(s);
    for (int $ = 0; $ < l.size(); ++$)
      if (iz.breakStatement(l.get($)))
        return $;
    return -1;
  }

  static String statementsToString(final List<Statement> ss, final int start, final int end) {
    String $ = new String();
    for (int ¢ = start; ¢ < end; ++¢)
      $ += ss.get(¢);
    return $;
  }
}
