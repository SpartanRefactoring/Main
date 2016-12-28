package il.org.spartan.spartanizer.research.patterns;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.patterns.common.*;
import static il.org.spartan.spartanizer.research.TipperFactory.patternTipper;

/** Replace if(X) Y; when(X).eval(Y);
 * @author Ori Marcovitch
 * @since Nov 7, 2016 */
public final class ExecuteWhen extends NanoPatternTipper<IfStatement> {
  private static final Set<UserDefinedTipper<IfStatement>> tippers = new HashSet<UserDefinedTipper<IfStatement>>() {
    static final long serialVersionUID = 1L;
    {
      add(patternTipper("if($X) $N($A);", "execute(() -> $N($A)).when($X);", "turn into when(X).execute(Y)"));
      add(patternTipper("if($X1) $X2.$N($A);", "execute(() -> $X2.$N($A)).when($X1);", "turn into when(X).execute(Y)"));
    }
  };

  @Override public boolean canTip(final IfStatement x) {
    return anyTips(tippers, x)//
        && !throwing(then(x))//
        && !iz.returnStatement(then(x))//
        && doesNotReferenceNonFinal(x);
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
    if (searchAncestors.forClass(TryStatement.class).from(¢) != null)
      return true;
    final MethodDeclaration $ = az.methodDeclaration(searchAncestors.forClass(MethodDeclaration.class).from(¢));
    return $ != null && !$.thrownExceptionTypes().isEmpty();
  }

  @Override public Tip pattern(final IfStatement x) {
    return firstTip(tippers, x);
  }
}
