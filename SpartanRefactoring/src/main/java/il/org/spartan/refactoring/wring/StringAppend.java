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
public class StringAppend extends Wring.ReplaceCurrentNode<ClassInstanceCreation> {
  @Override
  ASTNode replacement(final ClassInstanceCreation n) {
    return null;
  }
  @Override
  String description(final ClassInstanceCreation n) {
    return "Use Java's built-in factory constructor valueOf() instead of initialization";
  }
  @Override
  WringGroup wringGroup() {
    return WringGroup.REPLACE_CLASS_INSTANCE_CREATION;
  }
}