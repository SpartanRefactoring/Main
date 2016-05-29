package il.org.spartan.refactoring.wring;

import java.util.Arrays;
import java.util.List;

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
  // String array contains all primitive class (and String) identifiers
  final String[] pi = { "java.lang.Byte", "java.lang.Short", "java.lang.Integer", "java.lang.Long", "java.lang.Float",
      "java.lang.Double", "java.lang.Character", "java.lang.String", "java.lang.Boolean" };

  @SuppressWarnings("unchecked") @Override ASTNode replacement(final ClassInstanceCreation n) {
    if (!n.getAST().hasResolvedBindings() || n.getType().resolveBinding() == null
        || !Arrays.asList(pi).contains(n.getType().resolveBinding().getBinaryName()) || n.arguments().size() != 1)
      return null;
    final MethodInvocation $ = n.getAST().newMethodInvocation();
    $.setExpression(n.getAST().newSimpleName(n.getType().toString()));
    $.setName(n.getAST().newSimpleName("valueOf"));
    for (final ASTNode e : (List<ASTNode>) n.arguments())
      $.arguments().add(ASTNode.copySubtree(n.getAST(), e));
    return $;
  }
  @Override String description(final ClassInstanceCreation c) {
    return "Use factory method " + c.getType() + ".valueOf() instead of initialization";
  }
  @Override WringGroup wringGroup() {
    return WringGroup.REPLACE_CLASS_INSTANCE_CREATION;
  }
}