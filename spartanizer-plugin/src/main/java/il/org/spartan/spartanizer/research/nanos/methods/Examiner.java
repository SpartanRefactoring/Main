package il.org.spartan.spartanizer.research.nanos.methods;

import static il.org.spartan.spartanizer.research.TipperFactory.patternTipper;

import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Statement;

import il.org.spartan.spartanizer.java.haz;
import il.org.spartan.spartanizer.research.nanos.common.JavadocMarkerNanoPattern;
import il.org.spartan.spartanizer.research.nanos.common.NanoPatternContainer;

/** One statement method returning boolean expression
 * @author Ori Marcovitch */
public class Examiner extends JavadocMarkerNanoPattern {
  private static final long serialVersionUID = 0x6629391C4F21B227L;
  private static final NanoPatternContainer<Statement> tippers = new NanoPatternContainer<>(//
      patternTipper("return $X;", "", ""), //
      patternTipper("synchronized ($X1) { return $X2;}", "", "")//
  );

  @Override protected boolean prerequisites(final MethodDeclaration ¢) {
    return haz.booleanReturnType(¢)//
        && anyTips(tippers, onlyStatement(¢))//
    ;
  }
}
