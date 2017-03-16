package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.research.TipperFactory.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.nanos.common.*;

/** Catch without body
 * @author orimarco <tt>marcovitch.ori@gmail.com</tt>
 * @since 2017-01-05 */
public final class SuppressException extends NanoPatternTipper<CatchClause> {
  private static final long serialVersionUID = -8859938289108985941L;
  private static final List<UserDefinedTipper<TryStatement>> tippers = new ArrayList<UserDefinedTipper<TryStatement>>() {
    
    private static final long serialVersionUID = -9018706510367059136L;

    {
      add(patternTipper("try $B1 catch($T $N){}", "try $B1 catch($T $N){ignore();};", ""));
    }
  };

  @Override public boolean canTip(final CatchClause ¢) {
    return anyTips(tippers, parentAsTryStatement(¢));
  }

  @Override public Tip pattern(final CatchClause ¢) {
    return firstTip(tippers, parentAsTryStatement(¢));
  }

  @Override public Category category() {
    return Category.Exception;
  }

  private static TryStatement parentAsTryStatement(final CatchClause ¢) {
    return az.tryStatement(parent(¢));
  }

  @Override public String technicalName() {
    return "catchXIgnore";
  }

  @Override public String example() {
    return firstPattern(tippers);
  }

  @Override public String symbolycReplacement() {
    return firstReplacement(tippers);
  }
}
