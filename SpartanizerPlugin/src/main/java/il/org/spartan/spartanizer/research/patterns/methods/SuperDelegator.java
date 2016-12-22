package il.org.spartan.spartanizer.research.patterns.methods;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.research.*;
import static il.org.spartan.spartanizer.research.TipperFactory.patternTipper;

/** @author Ori Marcovitch
 * @since 2016 */
public class SuperDelegator extends Delegator {
  private static Set<UserDefinedTipper<Statement>> tippers = new HashSet<UserDefinedTipper<Statement>>() {
    static final long serialVersionUID = 1L;
    {
      add(patternTipper("return super.$N($A);", "", ""));
      add(patternTipper("return ($T)super.$N($A);", "", ""));
    }
  };

  @Override protected boolean prerequisites(final MethodDeclaration ¢) {
    return hazOneStatement(¢)//
        && anyTips(tippers, onlyStatement(¢))//
        && parametersNames(¢).containsAll(analyze.dependencies(arguments(findFirst.superMethodDeclaration(onlyStatement(¢)))));
  }
}
