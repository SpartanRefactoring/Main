package il.org.spartan.refactoring.wring;

import org.eclipse.jdt.core.dom.SuperConstructorInvocation;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.text.edits.TextEditGroup;
import il.org.spartan.refactoring.utils.Rewrite;

/**
 * A wring to remove <code>super()</code> calls which take no arguments, as
 * typically created by Eclipse's template for constructors.
 *
 * @author Yossi Gil
 * @since 2015-08-26
 */
public class SuperConstructorInvocationRemover extends Wring<SuperConstructorInvocation> {
  @Override boolean scopeIncludes(final SuperConstructorInvocation i) {
    return i.arguments().isEmpty();
  }
  @Override Rewrite make(final SuperConstructorInvocation i) {
    return new Rewrite(description(i), i) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        r.remove(i, g);
      }
    };
  }
  @Override String description(@SuppressWarnings("unused") final SuperConstructorInvocation _) {
    return "Remove empty 'super()' invocation";
  }
}
