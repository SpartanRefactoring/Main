package il.org.spartan.refactoring.wring;

import java.util.Arrays;

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
public class WrapperReplaceWithFactory extends Wring.ReplaceCurrentNode<ClassInstanceCreation> {
  // String array contains all primitive class (and String) identifiers
  final String[] pi = { "Byte", "Short", "Integer", "Long", "Float", "Double", "Character", "String", "Boolean" };
  @Override
  ASTNode replacement(final ClassInstanceCreation c) {
    String tn = c.getType().toString();
    if (!Arrays.asList(pi).contains(tn))
      return null;
    final MethodInvocation $ = c.getAST().newMethodInvocation();
    $.setExpression(c.getAST().newSimpleName(tn));
    $.setName(c.getAST().newSimpleName("valueOf"));
    $.arguments().add(Expression.copySubtree(c.getAST(), (ASTNode) c.arguments().get(0)));
    return $;
  }
  @Override
  String description(final ClassInstanceCreation c) {
    return "Use Java's built-in factory constructor valueOf() instead of initialization";
  }
  @Override
  WringGroup wringGroup() {
    return WringGroup.REPLACE_CLASS_INSTANCE_CREATION;
  }
}