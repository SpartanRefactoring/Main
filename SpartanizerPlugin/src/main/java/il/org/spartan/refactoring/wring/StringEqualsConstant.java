package il.org.spartan.refactoring.wring;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.*;
import il.org.spartan.refactoring.preferences.PluginPreferencesResources.*;
import il.org.spartan.refactoring.utils.*;
import il.org.spartan.refactoring.wring.Wring.*;

/** Replace <code> s.equals("s")</code> by <code>"s".equals(s)</code>
 * @author Ori Roth
 * @since 2016/05/08 */
public class StringEqualsConstant extends ReplaceCurrentNode<MethodInvocation> {
  final static String[] _mns = { "equals", "equalsIgnoreCase" };
  final static List<String> mns = as.list(_mns);

  @Override String description(final MethodInvocation i) {
    return "use " + i.arguments().get(0) + "." + i.getName() + "(" + i.getExpression() + ") instead of " + i;
  }
  @SuppressWarnings("unchecked") @Override ASTNode replacement(final MethodInvocation i) {
    if (!mns.contains(i.getName().toString()) || i.arguments().size() != 1 || i.getExpression() == null || i.getExpression() instanceof StringLiteral
        || !(i.arguments().get(0) instanceof StringLiteral))
      return null;
    final MethodInvocation $ = i.getAST().newMethodInvocation();
    $.setExpression(Funcs.duplicate((Expression) i.arguments().get(0)));
    $.setName(Funcs.duplicate(i.getName()));
    $.arguments().add(Funcs.duplicate(i.getExpression()));
    return $;
  }
  @Override WringGroup wringGroup() {
    return WringGroup.REORDER_EXPRESSIONS;
  }
}
