package il.org.spartan.refactoring.wring;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.refactoring.preferences.*;
import il.org.spartan.refactoring.preferences.PluginPreferencesResources.*;

/**
 * A {@link Wring} to replace primitive class (and String) instance creation
 * with recommended factory method <code>valueOf()</code>:
 *
 * <pre> Integer x = new Integer(2); </pre>
 *
 * can be replaced with
 *
 * <pre> Integer x = Integer.valueOf(2); </pre>
 *
 * </code>
 *
 * @author Ori Roth <code><ori.rothh [at] gmail.com></code>
 * @since 2016-04-06
 */
public class WrapperReplaceWithFactory extends Wring.ReplaceCurrentNode<ClassInstanceCreation> implements Kind.REPLACE_CLASS_INSTANCE_CREATION {
  // String array contains all primitive class (and String) identifiers
  final String[] pi = { "java.lang.Byte", "java.lang.Short", "java.lang.Integer", "java.lang.Long", "java.lang.Float", "java.lang.Double", "java.lang.Character", "java.lang.String",
      "java.lang.Boolean" };

  @SuppressWarnings("unchecked") @Override ASTNode replacement(final ClassInstanceCreation c) {
    if (!c.getAST().hasResolvedBindings() || c.getType().resolveBinding() == null || !Arrays.asList(pi).contains(c.getType().resolveBinding().getBinaryName()) || c.arguments().size() != 1)
      return null;
    final MethodInvocation $ = c.getAST().newMethodInvocation();
    $.setExpression(c.getAST().newSimpleName(c.getType().toString()));
    $.setName(c.getAST().newSimpleName("valueOf"));
    for (final ASTNode e : (List<ASTNode>) c.arguments())
      $.arguments().add(ASTNode.copySubtree(c.getAST(), e));
    return $;
  }
  @Override String description(final ClassInstanceCreation c) {
    return "Use factory method " + c.getType() + ".valueOf() instead of initialization";
  }
  @Override public WringGroup kind() {
    // TODO Auto-generated method stub
    return null;
  }
  @Override public void go(final ASTRewrite r, final TextEditGroup g) {
    // TODO Auto-generated method stub
  }
}