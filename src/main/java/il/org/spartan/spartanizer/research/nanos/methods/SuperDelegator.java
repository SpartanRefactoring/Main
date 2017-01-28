package il.org.spartan.spartanizer.research.nanos.methods;

import static il.org.spartan.spartanizer.research.TipperFactory.*;

import java.util.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.research.*;

/** TODO: Ori Marcovitch please add a description
 * @author Ori Marcovitch
 * @since 2016 */
public class SuperDelegator extends Delegator {
  private static final Set<UserDefinedTipper<Expression>> tippers = new HashSet<UserDefinedTipper<Expression>>() {
    static final long serialVersionUID = 1L;
    {
      add(patternTipper("super.$N($A)", "", ""));
      add(patternTipper("($T)super.$N($A)", "", ""));
    }
  };

  @Override
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
