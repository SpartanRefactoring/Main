package il.org.spartan.spartanizer.research.patterns.methods;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;
import static il.org.spartan.lisp.onlyOne;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.patterns.common.*;

/** @author Ori Marcovitch
 * @since 2016 */
public class ForEachApplier extends JavadocMarkerNanoPattern {
  Set<UserDefinedTipper<Statement>> tippers = new HashSet<UserDefinedTipper<Statement>>() {
    static final long serialVersionUID = 1L;
    {
      add(TipperFactory.patternTipper("for($N1 $N2 : $X) $N2.$N3($A);", "", ""));
      add(TipperFactory.patternTipper("for($N1 $N2 : $X) $N3($N2);", "", ""));
      add(TipperFactory.patternTipper("$X1.stream().forEach($X2);", "", ""));
      add(TipperFactory.patternTipper("$X1.stream().filter($X2).forEach($X3);", "", ""));
    }
  };

  @Override protected boolean prerequisites(final MethodDeclaration ¢) {
    return hazOneStatement(¢) && anyTips(tippers, onlyOne(statements(¢)));
  }
}
