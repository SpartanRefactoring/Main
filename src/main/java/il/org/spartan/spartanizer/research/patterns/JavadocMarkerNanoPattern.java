package il.org.spartan.spartanizer.research.patterns;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.engine.*;

/** @author Ori Marcovitch
 * @since 2016 */
public abstract class JavadocMarkerNanoPattern<N extends MethodDeclaration> extends NanoPatternTipper<N> implements MethodPatternUtilitiesTrait {
  @Override public final boolean canTip(final N ¢) {
    // System.out.println("checking " + javadoc());
    final Javadoc $ = ¢.getJavadoc();
    return ($ == null || !($ + "").contains(javadoc())) && prerequisites(¢);
  }

  protected abstract boolean prerequisites(N ¢);

  @Override public final Tip pattern(final N n) {
    return new Tip(description(n), n, this.getClass()) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        wizard.addJavaDoc(n, r, g, javadoc());
      }
    };
  }

  @Override public final String description(final MethodDeclaration ¢) {
    return ¢.getName() + " is a " + this.getClass().getSimpleName() + " method";
  }

  protected final String javadoc() {
    return "[[" + this.getClass().getSimpleName() + "]]";
  }
}
