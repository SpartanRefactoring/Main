package il.org.spartan.spartanizer.research.patterns.methods;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.patterns.*;
import il.org.spartan.spartanizer.research.patterns.common.*;
import static il.org.spartan.spartanizer.research.TipperFactory.patternTipper;

/** @author Ori Marcovitch
 * @since 2016 May collide with {@link IfNullThrow} */
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
