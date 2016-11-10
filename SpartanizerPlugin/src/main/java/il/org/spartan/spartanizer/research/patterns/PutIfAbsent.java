package il.org.spartan.spartanizer.research.patterns;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.*;

/** if (!$X1.containsKey($X2)) <br>
 * <tab> $X1.put($X2, $X3); <br>
 * into: <br>
 * $X1.putIfAbsent($X2, $X3);
 * @author Ori Marcovitch
 * @year 2016 */
public final class PutIfAbsent extends NanoPatternTipper<IfStatement> {
  static UserDefinedTipper<IfStatement> tipper = TipperFactory.tipper("if (!$X1.containsKey($X2)) $X1.put($X2, $X3);", "$X1.putIfAbsent($X2, $X3);",
      "");

  @Override public String description(@SuppressWarnings("unused") final IfStatement __) {
    return "replace with putIfAbsent";
  }
  @Override public boolean canTip(final IfStatement ¢) {
    return tipper.canTip(¢) && analyze.type(az.simpleName(tipper.getMatching(¢, "$X1"))).startsWith("Map<");
  }
  @Override public Tip tip(final IfStatement s) {
    return new Tip(description(s), s, this.getClass()) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        Logger.logNP(s, getClass().getSimpleName());
        tipper.tip(s).go(r, g);
      }
    };
  }
}
