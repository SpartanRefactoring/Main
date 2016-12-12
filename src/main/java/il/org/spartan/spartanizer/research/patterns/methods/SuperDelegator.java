package il.org.spartan.spartanizer.research.patterns.methods;

import static il.org.spartan.lisp.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.research.*;

/** @author Ori Marcovitch
 * @since 2016 */
public class SuperDelegator extends Delegator {
  private static Set<UserDefinedTipper<Statement>> tippers = new HashSet<UserDefinedTipper<Statement>>() {
    static final long serialVersionUID = 1L;
    {
      add(TipperFactory.patternTipper("return super.$N($A);", "", ""));
      add(TipperFactory.patternTipper("return ($T)super.$N($A);", "", ""));
    }
  };

  @Override protected boolean prerequisites(final MethodDeclaration ¢) {
    super.prerequisites(¢);
    if (statements(¢) == null || statements(¢).size() != 1 || !anyTips(onlyOne(statements(¢))))
      return false;
    final SuperMethodInvocation $ = findFirst.superMethodDeclaration(onlyOne(statements(¢)));
    return $ != null && parametersNames(¢).containsAll(dependencies(arguments($)));
  }

  static boolean anyTips(final Statement s) {
    return tippers.stream().anyMatch(t -> t.canTip(s));
  }
}
