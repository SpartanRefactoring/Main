package il.org.spartan.spartanizer.research.nanos.common;

import static il.org.spartan.spartanizer.ast.navigate.step.javadoc;
import static il.org.spartan.spartanizer.ast.navigate.step.name;

import org.eclipse.jdt.core.dom.Javadoc;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.text.edits.TextEditGroup;

import il.org.spartan.spartanizer.ast.factory.misc;
import il.org.spartan.spartanizer.ast.navigate.MethodPatternUtilitiesTrait;
import il.org.spartan.spartanizer.tipping.Tip;

/** Base class for all nanos which match a full method, those patterns add a
 * Javadoc marker to the method rather than replacing it with some other syntax.
 * Once a method is marked by a nano, it won't be marked again by the same nano.
 * @author Ori Marcovitch */
public abstract class JavadocMarkerNanoPattern extends NanoPatternTipper<MethodDeclaration> implements MethodPatternUtilitiesTrait {
  private static final long serialVersionUID = -0x303DCBB7633226E5L;

  @Override public final boolean canTip(final MethodDeclaration ¢) {
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
    return new Tip(description(d), getClass(), d) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        misc.addJavaDoc(d, r, g, tag());
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
