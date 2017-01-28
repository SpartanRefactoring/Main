package il.org.spartan.spartanizer.research.nanos.methods;

import static il.org.spartan.spartanizer.research.TipperFactory.*;

import java.util.*;

import il.org.spartan.spartanizer.java.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.nanos.common.*;

/** TODO: Ori Marcovitch please add a description
 * @author Ori Marcovitch
 * @since 2016 */
public class Examiner extends JavadocMarkerNanoPattern {
  private static final Set<UserDefinedTipper<Statement>> tippers = new HashSet<UserDefinedTipper<Statement>>() {
    static final long serialVersionUID = 1L;
    {
      add(patternTipper("return $X;", "", ""));
      add(patternTipper("synchronized ($X1) { return $X2;}", "", ""));
    }
  };
  // private static final JavadocMarkerNanoPattern rival = new TypeChecker();

  @Override protected boolean prerequisites(final MethodDeclaration ¢) {
    return haz.booleanReturnType(¢)//
        && anyTips(tippers, onlyStatement(¢))//
    // && !rival.matches(¢)//
    ;
  }
}
