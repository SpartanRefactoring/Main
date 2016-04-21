package il.org.spartan.refactoring.wring;

import static il.org.spartan.refactoring.utils.Funcs.duplicate;
import static il.org.spartan.refactoring.utils.Funcs.newSimpleName;
import static il.org.spartan.utils.Utils.asList;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.MethodInvocation;

import il.org.spartan.refactoring.preferences.PluginPreferencesResources.WringGroup;

/**
 * A {@link Wring} to replace primitive class (and String) instance creation
 * with recommended factory method <code>valueOf()</code>:
 *
 * <pre>
 * Integer x = new Integer(2);
 * </pre>
 *
 * can be replaced with
 *
 * <pre>
 * Integer x = Integer.valueOf(2);
 * </pre>
 *
 * </code>
 *
 * @author Ori Roth <code><ori.rothh [at] gmail.com></code>
 * @since 2016-04-06
 */
public class WrapperReplaceWithFactory extends Wring.ReplaceCurrentNode<ClassInstanceCreation> {
  @Override ASTNode replacement(final ClassInstanceCreation c) {
    final String tn = c.getType().toString();
    if (!asList("Byte", "Short", "Integer", "Long", "Float", "Double", "Character", "String", "Boolean").contains(tn))
      return null;
    final MethodInvocation $ = c.getAST().newMethodInvocation();
    $.setExpression(newSimpleName(c, tn));
    $.setName(newSimpleName(c, "valueOf"));
    $.arguments().add(duplicate((ASTNode) c.arguments().get(0)));
    return $;
  }
  @Override String description(final ClassInstanceCreation c) {
    return "Use factory method " + c.getType() + ".valueOf() instead of initialization";
  }
  @Override WringGroup wringGroup() {
    return WringGroup.REPLACE_CLASS_INSTANCE_CREATION;
  }
}