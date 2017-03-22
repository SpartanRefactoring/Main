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

/** Catch(...) { return;}
 * @author orimarco <tt>marcovitch.ori@gmail.com</tt>
 * @since 2016-12-27 */
public final class ReturnOnException extends NanoPatternTipper<CatchClause> {
  private static final long serialVersionUID = -5974484928980948225L;
  private static final List<UserDefinedTipper<TryStatement>> tippers = as.list(
      patternTipper("try $B1 catch($T $N){ return $D; }", "If.throwz(() -> $B1).returnDefault();", "Go Fluent: IfThrowsReturnNull"),
      patternTipper("try $B1 catch($T $N){ return; }", "If.throwz(() -> $B1).returns();", "Go Fluent: IfThrowsReturns"));

  @Override public boolean canTip(final CatchClause ¢) {
    return anyTips(tippers, parentAsTryStatement(¢));
  }

  @Nullable @Override public Tip pattern(final CatchClause ¢) {
    return firstTip(tippers, parentAsTryStatement(¢));
  }

  @NotNull @Override public Category category() {
    return Category.Exception;
  }

  private static TryStatement parentAsTryStatement(final CatchClause ¢) {
    return az.tryStatement(parent(¢));
  }

  @Override public String example() {
    return firstPattern(tippers);
  }

  @Override public String symbolycReplacement() {
    return firstReplacement(tippers);
  }
}
