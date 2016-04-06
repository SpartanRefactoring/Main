package il.org.spartan.refactoring.wring;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.refactoring.preferences.PluginPreferencesResources.WringGroup;

/* Three TODOs (from Daniel): 1) Ensure the wring can handle all primitive
 * wrappers in Java's JRE 2) Don't assume the argument is a NumberLiteral; it
 * can be any Expression 3) Add Javadoc (just look in other wrings and follow) */
public class WrapperReplaceWithFactory extends Wring.ReplaceCurrentNode<ClassInstanceCreation> {
  @Override ASTNode replacement(final ClassInstanceCreation n) {
    if (n.getType().toString().equals("Integer")) {
      final MethodInvocation $ = n.getAST().newMethodInvocation();
      $.setExpression(n.getAST().newSimpleName("Integer"));
      $.setName(n.getAST().newSimpleName("valueOf"));
      final NumberLiteral nl = n.getAST().newNumberLiteral();
      nl.setToken(((NumberLiteral) n.arguments().get(0)).getToken());
      $.arguments().add(nl);
      return $;
    }
    return null;
  }
  @Override String description(final ClassInstanceCreation n) {
    return "Use Java's built-in factory constructor valueOf() instead of initialization";
  }
  @Override WringGroup wringGroup() {
    return WringGroup.CONSOLIDATE_ASSIGNMENTS_STATEMENTS;
  }
}