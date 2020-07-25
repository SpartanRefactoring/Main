package il.org.spartan.spartanizer.research.nanos.methods;

import static il.org.spartan.spartanizer.ast.navigate.step.arguments;
import static il.org.spartan.spartanizer.ast.navigate.step.expression;
import static il.org.spartan.spartanizer.ast.navigate.step.parametersNames;
import static il.org.spartan.spartanizer.research.TipperFactory.patternTipper;

import java.util.Collection;

import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.SuperMethodInvocation;

import fluent.ly.as;
import il.org.spartan.spartanizer.ast.navigate.analyze;
import il.org.spartan.spartanizer.ast.navigate.findFirst;
import il.org.spartan.spartanizer.research.UserDefinedTipper;

/** Method delegating to super
 * @author Ori Marcovitch */
public class SuperDelegator extends Delegator {
  private static final long serialVersionUID = 0x6F65A7053675D4B1L;
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
  @Override public String tipperName() {
    return Delegator.class.getSimpleName();
  }
}
