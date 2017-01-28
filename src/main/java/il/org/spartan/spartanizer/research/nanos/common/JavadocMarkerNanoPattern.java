package il.org.spartan.spartanizer.research.nanos.common;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.engine.*;

/** TODO: Ori Marcovitch please add a description
 * @author Ori Marcovitch
 * @since 2016 */
public abstract class JavadocMarkerNanoPattern extends NanoPatternTipper<MethodDeclaration> implements MethodPatternUtilitiesTrait {
  @Override public final boolean canTip(final MethodDeclaration ¢) {
    final Javadoc $ = javadoc(¢);
    return ($ == null || !($ + "").contains(tag())) && prerequisites(¢)
        && (!(extract.annotations(¢) + "").contains("({") || !containedInInstanceCreation(¢));
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

  private static boolean containedInInstanceCreation(final ASTNode ¢) {
    return yieldAncestors.untilClass(ClassInstanceCreation.class).from(¢) != null;
  }
}
