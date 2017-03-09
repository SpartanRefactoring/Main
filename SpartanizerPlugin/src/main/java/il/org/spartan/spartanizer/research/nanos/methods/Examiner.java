package il.org.spartan.spartanizer.research.nanos.methods;

import static il.org.spartan.spartanizer.research.TipperFactory.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.java.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.nanos.common.*;

/** One statement method returning boolean expression
 * @author Ori Marcovitch */
public class Examiner extends JavadocMarkerNanoPattern {
  private static final long serialVersionUID = 7361477859663262247L;
  private static final Collection<UserDefinedTipper<Statement>> tippers = as.list(//
      patternTipper("return $X;", "", ""), //
      patternTipper("synchronized ($X1) { return $X2;}", "", "")//
  );

  @Override protected boolean prerequisites(final MethodDeclaration ¢) {
    return haz.booleanReturnType(¢)//
        && anyTips(tippers, onlyStatement(¢))//
    ;
  }
}
