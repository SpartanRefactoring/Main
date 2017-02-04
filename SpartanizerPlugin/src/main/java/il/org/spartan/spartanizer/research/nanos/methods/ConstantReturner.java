package il.org.spartan.spartanizer.research.nanos.methods;

import static il.org.spartan.spartanizer.research.TipperFactory.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.nanos.common.*;
import org.jetbrains.annotations.NotNull;

/** @nano a method returns some constant
 * @author Ori Marcovitch
 * @since Jan 8, 2017 */
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

  @Override @NotNull public Category category() {
    return Category.Default;
  }
}
