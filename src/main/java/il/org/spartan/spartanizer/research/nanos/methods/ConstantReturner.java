package il.org.spartan.spartanizer.research.nanos.methods;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.nanos.common.*;

import static il.org.spartan.spartanizer.research.TipperFactory.patternTipper;

/** @author Ori Marcovitch
 * @since 2016 */
public class ConstantReturner extends JavadocMarkerNanoPattern {
  private static final JavadocMarkerNanoPattern rival = new Default();
  private static Set<UserDefinedTipper<Statement>> tippers = new HashSet<UserDefinedTipper<Statement>>() {
    static final long serialVersionUID = 1L;
    {
      add(patternTipper("return $L;", "", ""));
      add(patternTipper("return -$L;", "", ""));
    }
  };

  @Override protected boolean prerequisites(final MethodDeclaration ¢) {
    return anyTips(tippers, onlyStatement(¢))//
        && !rival.matches(¢);
  }
}
