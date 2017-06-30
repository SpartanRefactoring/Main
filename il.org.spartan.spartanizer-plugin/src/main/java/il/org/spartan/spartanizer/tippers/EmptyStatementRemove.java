package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.utils.*;

/** TODO Yossi Gil: document class 
 * 
 * @author Yossi Gil
 * @since 2017-06-30 */
public final class EmptyStatementRemove extends NodeMatcher<EmptyStatement> {

  private static final long serialVersionUID = -7507041551913199814L;

  @Override public Examples examples() {
    return null;
  }

  @Override protected ASTRewrite go(ASTRewrite r, TextEditGroup g) {
    return null;
  }
}
