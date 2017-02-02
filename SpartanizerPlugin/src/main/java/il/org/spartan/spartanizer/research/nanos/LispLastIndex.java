package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.research.TipperFactory.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.nanos.common.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** @nano last index in collection, lisp style
 * @author orimarco <tt>marcovitch.ori@gmail.com</tt>
 * @since 2016-12-20 */
public final class LispLastIndex extends NanoPatternTipper<InfixExpression> {
  private static final List<UserDefinedTipper<InfixExpression>> tippers = new ArrayList<UserDefinedTipper<InfixExpression>>() {
    static final long serialVersionUID = 1L;
    {
      add(patternTipper("$X.size()-1", "lastIndex($X)", "lisp: lastIndex"));
    }
  };
  static final Last rival = new Last();

  @Override public boolean canTip(final InfixExpression ¢) {
    return anyTips(tippers, ¢) && rival.cantTip(az.methodInvocation(parent(¢)));
  }

  @Nullable
  @Override public Tip pattern(final InfixExpression ¢) {
    return firstTip(tippers, ¢);
  }

  @NotNull
  @Override public Category category() {
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
