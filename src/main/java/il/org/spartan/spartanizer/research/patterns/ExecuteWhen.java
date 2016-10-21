package il.org.spartan.spartanizer.research.patterns;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.tipping.*;

/** Replace if(X) Y; when(X).eval(Y);
 * @author Ori Marcovitch
 * @year 2016 */
public final class ExecuteWhen extends NanoPatternTipper<IfStatement> {
  Set<UserDefinedTipper<IfStatement>> tippers = new HashSet<UserDefinedTipper<IfStatement>>() {
    static final long serialVersionUID = 1L;
    {
      add(TipperFactory.tipper("if($X) $N($A);", "execute((x) -> $N($A)).when($X);", "turn into when(X).execute(Y)"));
      add(TipperFactory.tipper("if($X1) $X2.$N($A);", "execute((x) -> $X2.$N($A)).when($X1);", "turn into when(X).execute(Y)"));
    }
  };

  @Override public String description(@SuppressWarnings("unused") final IfStatement __) {
    return "turn into when(x).execute(()->y)";
  }

  @Override public boolean canTip(final IfStatement x) {
    for (final UserDefinedTipper<IfStatement> ¢ : tippers)
      if (¢.canTip(x) && !throwing(step.then(x)) && !iz.returnStatement(step.then(x)))
        return true;
    return false;
  }

  /** @param then
   * @return */
  private static boolean throwing(@SuppressWarnings("unused") final Statement __) {
    // TODO: check if method throws
    return false;
  }

  @Override public Tip tip(final IfStatement x) throws TipperFailure {
    Logger.logNP(x, "ApplyWhen");
    for (final UserDefinedTipper<IfStatement> ¢ : tippers)
      if (¢.canTip(x))
        return ¢.tip(x);
    assert false;
    return null;
  }
}
