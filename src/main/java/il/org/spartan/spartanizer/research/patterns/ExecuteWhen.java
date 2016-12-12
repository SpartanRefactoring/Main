package il.org.spartan.spartanizer.research.patterns;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.patterns.common.*;

/** Replace if(X) Y; when(X).eval(Y);
 * @author Ori Marcovitch
 * @since Nov 7, 2016 */
public final class ExecuteWhen extends NanoPatternTipper<IfStatement> {
  Set<UserDefinedTipper<IfStatement>> tippers = new HashSet<UserDefinedTipper<IfStatement>>() {
    static final long serialVersionUID = 1L;
    {
      add(TipperFactory.patternTipper("if($X) $N($A);", "execute((__) -> $N($A)).when($X);", "turn into when(X).execute(Y)"));
      add(TipperFactory.patternTipper("if($X1) $X2.$N($A);", "execute((__) -> $X2.$N($A)).when($X1);", "turn into when(X).execute(Y)"));
    }
  };

  @Override public String description(@SuppressWarnings("unused") final IfStatement __) {
    return "turn into when(x).execute(()->y)";
  }

  @Override public boolean canTip(final IfStatement x) {
    return anyTips(tippers, x) && !throwing(then(x)) && !iz.returnStatement(then(x)) && !containsReferencesToNonFinal(x);
  }

  /** @param x
   * @return */
  private static boolean containsReferencesToNonFinal(@SuppressWarnings("unused") final IfStatement __) {
    // TODO: Marco
    // Set<Name> names = analyze.dependencies(x);
    // Set<VariableDeclaration> enviromentVariables =
    // analyze.enviromentVariables(x);
    // TypeDeclaration t = searchAncestors.forContainingType().from(x);
    // FieldDeclaration[] fieldDeclarationsVariables =
    // step.fieldDeclarations(t);
    return false;
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
