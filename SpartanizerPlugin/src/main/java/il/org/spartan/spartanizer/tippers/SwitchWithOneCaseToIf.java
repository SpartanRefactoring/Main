package il.org.spartan.spartanizer.tippers;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;

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
  @SuppressWarnings("unused") @Override public ASTNode replacement(SwitchStatement __) {
    // TODO: check if should convert to if(x==a) or if(x.equals(a))
    return null;
  }

  @Override protected boolean prerequisite(SwitchStatement s) {
    List<SwitchCase> l = extract.switchCases(s);
    List<Statement> ll = step.statements(s);
    int ind = firstBreakIndex(s);
    return l.size() == 1 && !l.get(0).isDefault() && ll.size() > 1 && ind != 1
        || l.size() == 2 && ind > 1 && ind < ll.size() - 1 && !iz.switchCase(ll.get(ll.size() - 1)) && (l.get(0).isDefault() || l.get(1).isDefault());
  }

  @SuppressWarnings("unused") @Override public String description(SwitchStatement __) {
    return "Convert switch statement to if-else statement";
  }

  private static int firstBreakIndex(SwitchStatement s) {
    List<Statement> l = step.statements(s);
    for (int $ = 0; $ < l.size(); ++$)
      if (iz.breakStatement(l.get($)))
        return $;
    return -1;
  }
}
