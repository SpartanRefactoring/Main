package il.org.spartan.spartanizer.research.patterns;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.patterns.common.*;

/** @author Ori Marcovitch
 * @since 2016 */
public class Getter extends JavadocMarkerNanoPattern<MethodDeclaration> {
  private static Set<UserDefinedTipper<Statement>> tippers = new HashSet<UserDefinedTipper<Statement>>() {
    static final long serialVersionUID = 1L;
    {
      add(TipperFactory.patternTipper("return $N;", "", ""));
      add(TipperFactory.patternTipper("return this.$N;", "", ""));
      add(TipperFactory.patternTipper("return ($T)$N;", "", ""));
      add(TipperFactory.patternTipper("return ($T)this.$N;", "", ""));
    }
  };

  @Override protected boolean prerequisites(final MethodDeclaration ¢) {
    return hazOneStatement(¢) && hazNoParams(¢) && anyTips(tippers, onlyStatement(¢));
  }
}
