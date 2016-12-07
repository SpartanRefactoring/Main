package il.org.spartan.spartanizer.research.patterns;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;
import static il.org.spartan.lisp.onlyOne;
import il.org.spartan.spartanizer.research.*;

/** @author Ori Marcovitch
 * @since 2016 */
public class Mapper extends JavadocMarkerNanoPattern<MethodDeclaration> {
  Set<UserDefinedTipper<Statement>> tippers = new HashSet<UserDefinedTipper<Statement>>() {
    static final long serialVersionUID = 1L;
    {
      add(TipperFactory.patternTipper("for($N1 $N2 : $X) $N2.$N3($A);", "", ""));
      add(TipperFactory.patternTipper("for($N1 $N2 : $X) $N3($N2);", "", ""));
    }
  };

  @Override protected boolean prerequisites(final MethodDeclaration d) {
    if (statements(d) == null || statements(d).size() != 1)
      return false;
    for (final UserDefinedTipper<Statement> ¢ : tippers)
      if (¢.canTip(onlyOne(statements(d))))
        return true;
    return false;
  }
}
