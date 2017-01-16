package il.org.spartan.spartanizer.tippers;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;

/** remove redundant return from switch in void method.
 * convert
 * <pre>
 * void a() {
 *   switch(x) {
 *    case 1: y=2; break;
 *    default: return;
 *   }
 * }
 * <pre>
 * to
 * <pre>
 * void a() {
 *   switch(x) {
 *    case 1: y=2; break;
 *   }
 * }
 * <pre>
 * 
 * Test case is {@link Issue1070}
 * @author YuvalSimon <tt>yuvaltechnion@gmail.com</tt>
 * @since 2017-01-15
 */
public class RemoveRedundantSwitchReturn extends ReplaceCurrentNode<SwitchStatement> implements TipperCategory.Collapse {
  @Override public ASTNode replacement(SwitchStatement s) {
    if (s == null)
      return null;
    Block b = az.block(s.getParent());
    if (b == null || !iz.methodDeclaration(b.getParent()) || !iz.voidType(step.returnType(az.methodDeclaration(b.getParent())))
        || lisp.last(step.statements(b)) != s)
      return null;
    List<switchBranch> $ = switchBranch.intoBranches(s);
    for (switchBranch ¢ : $)
      if (¢.hasDefault() && ¢.statements().size() == 1 && iz.returnStatement(lisp.first(¢.statements()))) {
        $.remove(¢);
        return switchBranch.makeSwitchStatement($, s.getExpression(), s.getAST());
      }
    return null;
  }

  @Override public String description(@SuppressWarnings("unused") SwitchStatement __) {
    return "Remove redundant switch case";
  }
}
