package il.org.spartan.spartanizer.research.nanos.common;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.engine.*;

/** Base class for all nanos which match a full method, those patterns add a
 * Javadoc marker to the method rather than replacing it with some other syntax.
 * Once a method is marked by a nano, it won't be marked again by the same nano.
 * @author Ori Marcovitch */
public abstract class JavadocMarkerNanoPattern extends NanoPatternTipper<MethodDeclaration> implements MethodPatternUtilitiesTrait {
  private static final long serialVersionUID = -3476158475903575781L;

  @Override public final boolean interesting(final MethodDeclaration ¢) {
    final Javadoc $ = javadoc(¢);
    return ($ == null || !($ + "").contains(tag()))//
        && prerequisites(¢)//
    ;
  }

  public final boolean matches(final MethodDeclaration ¢) {
    return prerequisites(¢);
  }

  protected abstract boolean prerequisites(MethodDeclaration ¢);

  @Override public final Tip pattern(final MethodDeclaration d) {
    return new Tip(description(d), d, getClass()) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        wizard.addJavaDoc(d, r, g, tag());
      }
    };
  }

  @Override public final String description(final MethodDeclaration ¢) {
    return name(¢) + " is a " + getClass().getSimpleName() + " method";
  }

  public final String tag() {
    return "[[" + getClass().getSimpleName() + "]]";
  }
}
