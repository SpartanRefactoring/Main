package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;

/** converts
 *
 * <pre>
 * void f(){return;}
 * </pre>
 * 
 * into
 * 
 * <pre>
 * void f(){}
 * </pre>
 * 
 * @author Dan Abramovich
 * @since 28-11-2016 */
public class RedundentReturnStatementInVoidTypeMethod extends ReplaceCurrentNode<MethodDeclaration> implements TipperCategory.Collapse {
  @Override public ASTNode replacement(final MethodDeclaration ¢) {
    if (¢.getBody().statements().size() != 1 || !"return;\n".equals((¢.getBody().statements().get(0) + ""))
        || !"return;\n".equals((¢.getBody().statements().get(0) + "")))
      return null;
    MethodDeclaration $ = duplicate.of(¢);
    $.getBody().statements().clear();
    return $;
  }

  @Override public String description(@SuppressWarnings("unused") final MethodDeclaration ¢) {
    return "Remove redundent return in void type method";
  }
}
