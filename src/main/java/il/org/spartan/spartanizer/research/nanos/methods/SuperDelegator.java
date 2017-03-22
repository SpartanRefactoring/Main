package il.org.spartan.spartanizer.research.nanos.methods;

import static il.org.spartan.spartanizer.research.TipperFactory.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.research.*;

/** Method delegating to super
 * @author Ori Marcovitch */
public class SuperDelegator extends Delegator {
  private static final long serialVersionUID = 8027005551688144049L;
  private static final Collection<UserDefinedTipper<Expression>> tippers = as.list( //
      patternTipper("super.$N($A)", "", ""), //
      patternTipper("($T)super.$N($A)", "", "")) //
  ;

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
        && parametersNames(¢).containsAll(analyze.dependencies(arguments(findFirst.instanceOf(SuperMethodInvocation.class).in(s))));
  }

  @Override public String nanoName() {
    return Delegator.class.getSimpleName();
  }
}
