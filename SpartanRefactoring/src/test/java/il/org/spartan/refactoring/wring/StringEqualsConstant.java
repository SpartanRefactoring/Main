package il.org.spartan.refactoring.wring;

import java.util.Arrays;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.StringLiteral;

import il.org.spartan.refactoring.preferences.PluginPreferencesResources.WringGroup;
import il.org.spartan.refactoring.utils.Funcs;
import il.org.spartan.refactoring.wring.Wring.ReplaceCurrentNode;

/**
 * Used to replace
 *
 * <pre>
 * <code>
 * s.equals("s")
 * </pre></code> with safer
 *
 * <pre>
 * <code>
 * "s".equals(s)
 * </pre></code>
 *
 * @author Ori Roth
 * @since 2016/05/08
 */
public class StringEqualsConstant extends ReplaceCurrentNode<MethodInvocation> {
  final static String[] _mns = { "equals", "equalsIgnoreCase" };
  final static List<String> mns = Arrays.asList(_mns);

  @SuppressWarnings("unchecked") @Override ASTNode replacement(MethodInvocation n) {
    if (!mns.contains(n.getName().toString()) || n.arguments().size() != 1 || n.getExpression() == null
        || n.getExpression() instanceof StringLiteral || !(n.arguments().get(0) instanceof StringLiteral))
      return null;
    final MethodInvocation $ = n.getAST().newMethodInvocation();
    $.setExpression(Funcs.duplicate((Expression) n.arguments().get(0)));
    $.setName(Funcs.duplicate(n.getName()));
    $.arguments().add(Funcs.duplicate(n.getExpression()));
    return $;
  }
  @Override String description(MethodInvocation n) {
    return "use " + n.arguments().get(0) + "." + n.getName().toString() + "(" + n.getExpression().toString() + ") instead of "
        + n.toString();
  }
  @Override WringGroup wringGroup() {
    return WringGroup.REORDER_EXPRESSIONS;
  }
}
