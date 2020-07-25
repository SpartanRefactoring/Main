package il.org.spartan.spartanizer.research.nanos.deprecated;

import static il.org.spartan.spartanizer.research.TipperFactory.patternTipper;

import java.util.Collection;

import org.eclipse.jdt.core.dom.MethodInvocation;

import fluent.ly.as;
import il.org.spartan.spartanizer.research.UserDefinedTipper;
import il.org.spartan.spartanizer.research.nanos.common.NanoPatternTipper;
import il.org.spartan.spartanizer.tipping.Tip;

/** *
 * @author Ori Marcovitch
 * @since Jan 8, 2017 */
public final class StatementsThroughStep extends NanoPatternTipper<MethodInvocation> {
  private static final long serialVersionUID = 0x492B04A3C7359A85L;
  final Collection<UserDefinedTipper<MethodInvocation>> tippers = as.list(patternTipper("$X.statements()", "statements($X)", "step: statements"), //
      patternTipper("$X.types()", "types($X)", "step: types") //
  );

  @Override public String description(@SuppressWarnings("unused") final MethodInvocation __) {
    return "lisp: first";
  }
  @Override public boolean canTip(final MethodInvocation ¢) {
    return anyTips(tippers, ¢);
  }
  @Override public Tip pattern(final MethodInvocation ¢) {
    return firstTip(tippers, ¢);
  }
}
