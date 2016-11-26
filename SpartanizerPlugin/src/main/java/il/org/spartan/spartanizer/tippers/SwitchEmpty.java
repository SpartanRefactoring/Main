package il.org.spartan.spartanizer.tippers;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;

/** convert
 *
 * <pre>
 * switch (x) {
 * }
 * </pre>
 *
 * into
 *
 * <pre>
 * </pre>
 *
 * .
 * @author Yuval Simon
 * @since 2016-11-20 */
public final class SwitchEmpty extends ReplaceCurrentNode<SwitchStatement> implements TipperCategory.Collapse {
  
  @Override public ASTNode replacement(@SuppressWarnings("unused") final SwitchStatement s) {
    if(s == null)
      return null;
    
    @SuppressWarnings("unchecked") List<Statement> ll = s.statements();
      return null;
//    if(ll.isEmpty())
//      return wizard.ast(";");
//    
//    if (!(ll.get(0) + "").contains("default"))
//      return s;
//    
//    if ((ll.get(ll.size() - 1) + "").contains("break"))
//      ll.remove(ll.size() - 1);
//    ll.remove(0);
//    return subject.ss(ll).toBlock();
  }

  @Override public String description(@SuppressWarnings("unused") final SwitchStatement __) {
    return "Remove empty switch statement or switch statement with only default case";
  }
}
