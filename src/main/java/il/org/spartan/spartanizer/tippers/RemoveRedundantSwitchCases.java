package il.org.spartan.spartanizer.tippers;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;

/** convert
 *
 * <pre>
 * switch (x) {
 *   case a:
 *   case b:
 *     x = 5;
 *     break;
 *   case c:
 *   default:
 *   case d:
 *     break;
 * }
 * switch (x) {
 *   case a:
 *   case b:
 *     x = 5;
 *     break;
 *   default:
 * }
 * </pre>
 *
 * into
 *
 * <pre>
* (some commands)
 * </pre>
 *
 * . Tested in {@link Issue0880}
 * @author Yuval Simon
 * @since 2016-11-27 */
public class RemoveRedundantSwitchCases extends CarefulTipper<SwitchCase> implements TipperCategory.Collapse {
  @Override public Tip tip(SwitchCase n, ExclusionManager exclude) {
    SwitchCase $ = az.switchCase(extract.nextStatementInside(n));
    if(exclude != null)
      exclude.excludeAll(extract.casesOnSameBranch(az.switchStatement(n.getParent()), n));
    return new Tip(description(n), n, getClass()) {
      @Override public void go(ASTRewrite r, TextEditGroup g) {
        r.remove($.isDefault() ? n : $, g);
      }
    };
  }
  
  @Override protected boolean prerequisite(SwitchCase n) {
    SwitchCase $ = az.switchCase(extract.nextStatementInside(n));
    return $ != null && ($.isDefault() || n.isDefault());
  }
  @Override
  @SuppressWarnings("unused") public String description(SwitchCase n) {
    return "remove redundant switch case";
  }
}