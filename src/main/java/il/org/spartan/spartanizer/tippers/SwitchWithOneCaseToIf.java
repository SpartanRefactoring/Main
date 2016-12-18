package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;

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
  @SuppressWarnings("unused")
  @Override public ASTNode replacement(SwitchStatement __) {
    //TODO: check if should convert to if(x==a) or if(x.equals(a))
    return null;
  }

  @SuppressWarnings("unused")
  @Override public String description(SwitchStatement __) {
    return "Convert switch statement to if-else statement";
  }
}
