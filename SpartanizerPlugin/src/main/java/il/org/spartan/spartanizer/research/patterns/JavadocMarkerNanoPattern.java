package il.org.spartan.spartanizer.research.patterns;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.*;

/** @author Ori Marcovitch
 * @since 2016 */
public abstract class JavadocMarkerNanoPattern<N extends MethodDeclaration> extends NanoPatternTipper<N> {
  @Override public final boolean canTip(final N ¢) {
    final Javadoc j = ¢.getJavadoc();
    return (j == null || !(j + "").contains(javadoc())) && prerequisites(¢);
  }

  protected abstract boolean prerequisites(N ¢);

  @Override public Tip tip(final N n) {
    return new Tip(description(n), n, this.getClass()) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        wizard.addJavaDoc(n, r, g, javadoc());
        Logger.logNP(n, javadoc());
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
