package il.org.spartan.spartanizer.research.nanos.methods;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.research.*;
import static il.org.spartan.spartanizer.research.TipperFactory.patternTipper;

/** @author Ori Marcovitch
 * @since 2016 */
public class SuperDelegator extends Delegator {
  private static final Set<UserDefinedTipper<Expression>> tippers = new HashSet<UserDefinedTipper<Expression>>() {
    static final long serialVersionUID = 1L;
    {
      add(patternTipper("super.$N($A)", "", ""));
      add(patternTipper("($T)super.$N($A)", "", ""));
    }
  };

  @Override protected boolean prerequisites(final MethodDeclaration ¢) {
    return hazOneStatement(¢)//
        && (superDelegator(¢, onlyStatement(¢))//
            || superDelegator(¢, onlySynchronizedStatementStatement(¢)))//
        || hazTwoStatements(¢)//
            && superDelegator(¢, firstStatement(¢))//
            && lastReturnsThis(¢);
  }

  private static boolean superDelegator(final MethodDeclaration ¢, final Statement s) {
    return anyTips(tippers, expression(s))//
        && parametersNames(¢).containsAll(analyze.dependencies(arguments(findFirst.superMethodDeclaration(s))));
  }
}
