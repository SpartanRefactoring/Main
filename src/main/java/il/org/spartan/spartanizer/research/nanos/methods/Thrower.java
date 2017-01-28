package il.org.spartan.spartanizer.research.nanos.methods;

import static il.org.spartan.spartanizer.research.TipperFactory.*;

import java.util.*;

import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.nanos.*;
import il.org.spartan.spartanizer.research.nanos.common.*;

/** TODO: Ori Marcovitch please add a description
 * @author Ori Marcovitch
 * @since 2016 May collide with {@link NotNullOrThrow} */
public class Thrower extends JavadocMarkerNanoPattern {
  private static final Set<UserDefinedTipper<Statement>> tippers = new HashSet<UserDefinedTipper<Statement>>() {
    static final long serialVersionUID = 1L;
    {
      add(patternTipper("throw $X;", "", ""));
    }
  };

  @Override protected boolean prerequisites(final MethodDeclaration ¢) {
    return hazOneStatement(¢) && anyTips(tippers, onlyStatement(¢));
  }
}
