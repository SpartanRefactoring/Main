package il.org.spartan.refactoring.wring;

import static il.org.spartan.refactoring.utils.Funcs.*;
import static il.org.spartan.refactoring.utils.expose.*;
import static il.org.spartan.refactoring.utils.extract.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.*;
import il.org.spartan.refactoring.preferences.PluginPreferencesResources.*;
import il.org.spartan.refactoring.utils.*;
import il.org.spartan.refactoring.wring.Wring.*;

/** Replace <code> s.equals("s")</code> by <code>"s".equals(s)</code>
 * @author Ori Roth
 * @since 2016/05/08 */
public class StringEqualsConstant extends ReplaceCurrentNode<MethodInvocation> implements Kind.Canonicalization {
  final static String[] _mns = { "equals", "equalsIgnoreCase" };
  final static List<String> mns = as.list(_mns);

  @Override String description(final MethodInvocation i) {
    return "use " + arguments(i).get(0) + "." + name(i) + "(" + extract.expression(i) + ") instead of " + i;
  }
  @SuppressWarnings("unchecked") @Override ASTNode replacement(final MethodInvocation i) {
    final List<Expression> as = arguments(i);
    if (!mns.contains(name(i).toString()) || as.size() != 1 || expression(i) == null || expression(i) instanceof StringLiteral
        || !(as.get(0) instanceof StringLiteral))
      return null;
    final MethodInvocation $ = i.getAST().newMethodInvocation();
    $.setExpression(duplicate(as.get(0)));
    $.setName(duplicate(name(i)));
    $.arguments().add(duplicate(expression(i)));
    return $;
  }
}
