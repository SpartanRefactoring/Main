package il.org.spartan.spartanizer.research.nanos.methods;

import static il.org.spartan.spartanizer.research.TipperFactory.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.nanos.common.*;

/** Method that does nothing but throwing exception
 * @author Ori Marcovitch */
public class Thrower extends JavadocMarkerNanoPattern {
  private static final long serialVersionUID = -0x6BE441E1CE75DDE0L;
  private static final Collection<UserDefinedTipper<Statement>> tippers = as.list(patternTipper("throw $X;", "", ""));

  @Override protected boolean prerequisites(final MethodDeclaration ¢) {
    return hazOneStatement(¢)//
        && anyTips(tippers, onlyStatement(¢));
  }
}
