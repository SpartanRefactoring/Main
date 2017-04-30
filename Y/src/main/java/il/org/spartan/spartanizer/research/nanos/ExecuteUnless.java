package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.research.TipperFactory.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.nanos.common.*;

/** Replace if(X) Y; when(X).eval(Y);
 * @author Ori Marcovitch
 * @since Nov 7, 2016 */
public final class ExecuteUnless extends NanoPatternTipper<IfStatement> {
  private static final long serialVersionUID = 0x3B67CFAA91EE1E8EL;
  private static final List<UserDefinedTipper<IfStatement>> tippers = as.list(//
      patternTipper("if($X) $N($A);", "execute(() -> $N($A)).when($X);", "turn into when(X).execute(Y)"), //
      patternTipper("if($X1) $X2.$N($A);", "execute(() -> $X2.$N($A)).when($X1);", "turn into when(X).execute(Y)")//
  );

  @Override public boolean canTip(final IfStatement x) {
    return anyTips(tippers, x)//
        && !throwing(then(x))//
        && !iz.returnStatement(then(x))//
        && doesNotReferenceNonFinal(x)//
        && isNotContainedInSimpleLoop(x);
  }

  private static boolean isNotContainedInSimpleLoop(final IfStatement x) {
    return !iz.enhancedFor(parent(x)) //
        && !iz.simpleLoop(parent(parent(x)));
  }

  /** First order approximation - does statement reference non effective final
   * names? meanwhile we take care just assignments...
   * @param ¢ statement
   * @return */
  private static boolean doesNotReferenceNonFinal(final IfStatement ¢) {
    return findFirst.assignment(¢) == null;
  }

  /** First order approximation - does statement throw?
   * @param ¢ statement
   * @return */
  private static boolean throwing(final Statement ¢) {
    if (yieldAncestors.untilClass(TryStatement.class).from(¢) != null)
      return true;
    final MethodDeclaration $ = az.methodDeclaration(yieldAncestors.untilClass(MethodDeclaration.class).from(¢));
    return $ != null && !$.thrownExceptionTypes().isEmpty();
  }

  @Override public Tip pattern(final IfStatement ¢) {
    return firstTip(tippers, ¢);
  }

  @Override public String description() {
    return "Execute a statement only if condition holds";
  }

  @Override public String technicalName() {
    return "IfXThenExecuteS";
  }

  @Override public String example() {
    return firstPattern(tippers);
  }

  @Override public NanoPatternTipper.Category category() {
    return Category.Conditional;
  }

  @Override public String symbolycReplacement() {
    return firstReplacement(tippers);
  }
}
