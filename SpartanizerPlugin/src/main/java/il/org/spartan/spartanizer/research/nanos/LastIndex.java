package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.research.TipperFactory.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.jetbrains.annotations.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.nanos.common.*;

/** @nano last index in collection, lisp style
 * @author orimarco <tt>marcovitch.ori@gmail.com</tt>
 * @since 2016-12-20 */
public final class LastIndex extends NanoPatternTipper<InfixExpression> {
  private static final long serialVersionUID = -5764445432502726533L;
  private static final List<UserDefinedTipper<InfixExpression>> tippers = as.list(patternTipper("$X.size()-1", "lastIndex($X)", "lisp: lastIndex"));
  static final Last rival = new Last();

  @Override public boolean canTip(final InfixExpression ¢) {
    return anyTips(tippers, ¢) && rival.cantTip(az.methodInvocation(parent(¢)));
  }

  @Nullable @Override public Fragment pattern(final InfixExpression ¢) {
    return firstTip(tippers, ¢);
  }

  @NotNull @Override public Category category() {
    return Category.Functional;
  }

  @Override public String description() {
    return "Index of last element in collection";
  }

  @Override public String example() {
    return firstPattern(tippers);
  }

  @Override public String symbolycReplacement() {
    return firstReplacement(tippers);
  }
}
