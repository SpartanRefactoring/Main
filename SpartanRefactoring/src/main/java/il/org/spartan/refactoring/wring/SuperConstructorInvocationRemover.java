package il.org.spartan.refactoring.wring;

import il.org.spartan.refactoring.preferences.*;
import il.org.spartan.refactoring.preferences.PluginPreferencesResources.WringGroup;
import il.org.spartan.refactoring.suggestions.*;
import il.org.spartan.refactoring.utils.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

/**
 * A wring to remove <code>super()</code> calls which take no arguments, as
 * typically created by Eclipse's template for constructors.
 *
 * @author Yossi Gil
 * @since 2015-08-26
 */
public class SuperConstructorInvocationRemover extends Wring<SuperConstructorInvocation> implements Kind.Simplify {
  @Override boolean scopeIncludes(final SuperConstructorInvocation i) {
    return i.arguments().isEmpty();
  }
  @Override Suggestion make(final SuperConstructorInvocation i) {
    return new Suggestion(description(i), i) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        r.remove(i, g);
      }
    };
  }
  @Override String description(@SuppressWarnings("unused") final SuperConstructorInvocation __) {
    return "Remove empty 'super()' invocation";
  }
  @Override public WringGroup kind() {
    // TODO Auto-generated method stub
    return null;
  }
  @Override public void go(final ASTRewrite r, final TextEditGroup g) {
    // TODO Auto-generated method stub
  }
}
