package il.org.spartan.spartanizer.research.patterns.common;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.engine.*;

/** @author Ori Marcovitch
 * @since 2016 */
public abstract class JavadocMarkerNanoPattern extends NanoPatternTipper<MethodDeclaration> implements MethodPatternUtilitiesTrait {
  @Override public final boolean canTip(final MethodDeclaration ¢) {
    final Javadoc $ = step.javadoc(¢);
    return ($ == null || !($ + "").contains(javadoc())) && prerequisites(¢);
  }

  protected abstract boolean prerequisites(MethodDeclaration ¢);

  @Override public final Tip pattern(final MethodDeclaration d) {
    return new Tip(description(d), d, this.getClass()) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        wizard.addJavaDoc(d, r, g, javadoc());
      }
    };
  }

  @Override public final String description(final MethodDeclaration ¢) {
    return ¢.getName() + " is a " + this.getClass().getSimpleName() + " method";
  }

  public final String javadoc() {
    return "[[" + this.getClass().getSimpleName() + "]]";
  }
}
