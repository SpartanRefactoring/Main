package il.org.spartan.spartanizer.research.patterns;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;

/** **********Under construction************** <br>
 * Replace if(X) Y; with Y #when X
 * @author Ori Marcovitch
 * @year 2016 */
public final class IfApply extends NanoPatternTipper<IfStatement> implements TipperCategory.CommnoFactoring {
  @Override public String description(@SuppressWarnings("unused") final IfStatement __) {
    // TODO: complete this nano
    return "replace .......";
  }

  @Override public boolean prerequisite(final IfStatement ¢) {
    return elze(¢) == null && !iz.block(step.then(¢));
  }

  @Override public Tip tip(IfStatement ¢) {
    // TODO Auto-generated method stub
    return null;
  }
}
