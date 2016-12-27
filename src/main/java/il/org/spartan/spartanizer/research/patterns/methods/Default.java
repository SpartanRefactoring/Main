package il.org.spartan.spartanizer.research.patterns.methods;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.patterns.common.*;
import static il.org.spartan.spartanizer.research.TipperFactory.patternTipper;

/** @author Ori Marcovitch
 * @since 2016 */
public class Default extends JavadocMarkerNanoPattern {
  private static Set<UserDefinedTipper<Statement>> tippers = new HashSet<UserDefinedTipper<Statement>>() {
    static final long serialVersionUID = 1L;
    {
      add(patternTipper("return;", "", ""));
      add(patternTipper("return 0;", "", ""));
      add(patternTipper("return false;", "", ""));
      add(patternTipper("return 0L;", "", ""));
      add(patternTipper("return null;", "", ""));
      add(patternTipper("return 0.;", "", ""));
    }
  };

  @Override protected boolean prerequisites(final MethodDeclaration ¢) {
    return anyTips(tippers, onlyStatement(¢));
  }
}
