package il.org.spartan.spartanizer.research.nanos.methods;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.research.nanos.common.*;
import il.org.spartan.utils.*;

/** @nano a method returns some constant
 * @author Ori Marcovitch
 * @since Jan 8, 2017 */
public class ConstantReturner extends JavadocMarkerNanoPattern {
  private static final long serialVersionUID = 6491594906301190270L;
  private static final lazy<JavadocMarkerNanoPattern> rival = lazy.get(() -> new Default());
  private static final lazy<NanoPatternContainer<Statement>> tippers = lazy.get(() -> new NanoPatternContainer<Statement>() {
    @SuppressWarnings("hiding") static final long serialVersionUID = 1L;
    {
      patternTipper("return $L;", "", "");
      patternTipper("return -$L;", "", "");
    }
  });

  @Override protected boolean prerequisites(final MethodDeclaration ¢) {
    return tippers.get().canTip(onlyStatement(¢))//
        && !rival.get().matches(¢);
  }

  @Override public Category category() {
    return Category.Default;
  }
}
