package il.org.spartan.spartanizer.research.patterns;

import static il.org.spartan.spartanizer.research.TipperFactory.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.patterns.common.*;

/** @author orimarco <tt>marcovitch.ori@gmail.com</tt>
 * @since 2016-12-20 */
public final class LispLastElement extends NanoPatternTipper<MethodInvocation> {
  private static final List<UserDefinedTipper<MethodInvocation>> tippers = new ArrayList<UserDefinedTipper<MethodInvocation>>() {
    static final long serialVersionUID = 1L;
    {
      add(patternTipper("$X.get($X.size()-1) ", "last($X)", "lisp: last"));
    }
  };

  @Override public boolean canTip(final MethodInvocation ¢) {
    return anyTips(tippers, ¢);
  }

  @Override public Tip pattern(final MethodInvocation ¢) {
    return firstTip(tippers, ¢);
  }

  @Override public String category() {
    return Category.Functional + "";
  }

  @Override public String description() {
    return "Last element in collection";
  }
}
