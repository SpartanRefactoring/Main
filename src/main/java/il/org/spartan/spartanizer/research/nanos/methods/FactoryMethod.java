package il.org.spartan.spartanizer.research.nanos.methods;

import static il.org.spartan.spartanizer.research.TipperFactory.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.nanos.common.*;

/** Method Creating new object and returning it
 * @author Ori Marcovitch */
public class FactoryMethod extends JavadocMarkerNanoPattern {
  private static final long serialVersionUID = -2789090530674070291L;
  private static final Collection<UserDefinedTipper<Statement>> tippers = new HashSet<UserDefinedTipper<Statement>>() {
    @SuppressWarnings("hiding")
    static final long serialVersionUID = 1L;
    {
      add(patternTipper("return new $T();", "", ""));
      add(patternTipper("return new $T[$X];", "", ""));
      add(patternTipper("return new $T() $B;", "", ""));
    }
  };

  @Override protected boolean prerequisites(final MethodDeclaration ¢) {
    return hazOneStatement(¢)//
        && anyTips(tippers, onlyStatement(¢));
  }
}
