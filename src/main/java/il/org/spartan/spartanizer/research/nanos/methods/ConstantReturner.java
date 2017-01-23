package il.org.spartan.spartanizer.research.nanos.methods;

import static il.org.spartan.spartanizer.research.TipperFactory.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.nanos.common.*;

/** TODO:  Ori Marcovitch
 please add a description 
 @author Ori Marcovitch
 * @since 2016 
 */

public class ConstantReturner extends JavadocMarkerNanoPattern {
  private static final JavadocMarkerNanoPattern rival = new Default();
  private static final Set<UserDefinedTipper<Statement>> tippers = new HashSet<UserDefinedTipper<Statement>>() {
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

  @Override public Category category() {
    return Category.Default;
  }
}

