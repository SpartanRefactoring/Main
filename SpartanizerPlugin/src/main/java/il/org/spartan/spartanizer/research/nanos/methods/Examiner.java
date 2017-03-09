package il.org.spartan.spartanizer.research.nanos.methods;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.java.*;
import il.org.spartan.spartanizer.research.nanos.common.*;

/** One statement method returning boolean expressio
 * @author Ori Marcovitch */
public class Examiner extends JavadocMarkerNanoPattern {
  private static final long serialVersionUID = 7361477859663262247L;
  private static final NanoPatternContainer<Statement> tippers = new NanoPatternContainer<Statement>() {
    @SuppressWarnings("hiding") static final long serialVersionUID = 1L;
    {
      patternTipper("return $X;", "", "");
      patternTipper("synchronized ($X1) { return $X2;}", "", "");
    }
  };

  @Override protected boolean prerequisites(final MethodDeclaration ¢) {
    return haz.booleanReturnType(¢)//
        && tippers.canTip(onlyStatement(¢))//
    ;
  }
}
