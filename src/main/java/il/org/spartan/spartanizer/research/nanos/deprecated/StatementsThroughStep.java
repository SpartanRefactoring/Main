package il.org.spartan.spartanizer.research.nanos.deprecated;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.nanos.common.*;
import org.jetbrains.annotations.Nullable;

/** * @year 2016
 * @author Ori Marcovitch
 * @since Jan 8, 2017 */
public final class StatementsThroughStep extends NanoPatternTipper<MethodInvocation> {
  final List<UserDefinedTipper<MethodInvocation>> tippers = new ArrayList<UserDefinedTipper<MethodInvocation>>() {
    static final long serialVersionUID = 1L;
    {
      add(TipperFactory.patternTipper("$X.statements()", "statements($X)", "step: statements"));
      add(TipperFactory.patternTipper("$X.types()", "types($X)", "step: types"));
    }
  };

  @Override public String description(@SuppressWarnings("unused") final MethodInvocation __) {
    return "lisp: first";
  }

  @Override public boolean canTip(final MethodInvocation ¢) {
    return anyTips(tippers, ¢);
  }

  @Override @Nullable public Tip pattern(final MethodInvocation ¢) {
    return firstTip(tippers, ¢);
  }
}
