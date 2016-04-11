package il.org.spartan.refactoring.wring;

import java.util.Vector;

import org.eclipse.jdt.core.dom.*;
import il.org.spartan.refactoring.preferences.PluginPreferencesResources.WringGroup;

/**
 * A {@link Wring} to replace primitive class (and String) instance creation
 * with recommended factory method valueOf():
 * <code><pre>Integer x = new Integer(2);</pre></code> can be replaced with
 * <code><pre>Integer x = Integer.valueOf(2);</pre></code>
 *
 * @author Ori Roth <code><ori.rothh [at] gmail.com></code>
 * @since 2016-04-06
 */
public class StringFromStringBuilder extends Wring.ReplaceCurrentNode<MethodInvocation> {
  @Override
  ASTNode replacement(final MethodInvocation n) {
    if (!n.getName().getIdentifier().equals("toString")) {
      return null;
    }
    String t;
    Vector<Expression> sll = new Vector<>();
    MethodInvocation r = n;
    while (true) {
      Expression e = r.getExpression();
      if (e instanceof ClassInstanceCreation) {
        t = ((ClassInstanceCreation) n.getExpression()).getType().toString();
        if (t.equals("StringBuffer") || t.equals("StringBuilder")) {
          break;
        }
        return null;
      } else if (e instanceof MethodInvocation) {
        if (!((MethodInvocation) e).getName().getIdentifier().equals("append")) {
          return null;
        }
        sll.insertElementAt(((Expression) ((MethodInvocation) e).arguments().get(0)), 0);
        r = (MethodInvocation) e;
      } else {
        return null;
      }
    }
    NumberLiteral nl = n.getAST().newNumberLiteral();
    nl.setToken(String.valueOf(sll.size()));
    return nl;
  }
  @Override
  String description(final MethodInvocation n) {
    return "Use Java's built-in factory constructor valueOf() instead of initialization";
  }
  @Override
  WringGroup wringGroup() {
    return WringGroup.REPLACE_CLASS_INSTANCE_CREATION;
  }
}