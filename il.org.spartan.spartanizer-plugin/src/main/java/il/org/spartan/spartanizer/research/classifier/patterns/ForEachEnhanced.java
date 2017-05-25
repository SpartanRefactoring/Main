package il.org.spartan.spartanizer.research.classifier.patterns;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.nanos.common.*;

/** ForEachEnhanced examples
 * @author Ori Marcovitch
 * @since 2016 */
public class ForEachEnhanced extends NanoPatternTipper<ForStatement> {
  private static final long serialVersionUID = -0x5ECC284B07301193L;
  final Collection<UserDefinedTipper<ForStatement>> tippers = Collections.emptyList();

  @Override public boolean canTip(final ForStatement ¢) {
    return anyTips(tippers, ¢);
  }
  @Override public String description(@SuppressWarnings("unused") final ForStatement __) {
    return "ForEach: conevrt to fluent API";
  }
  @Override public Tip pattern(final ForStatement ¢) {
    return firstTipper(tippers, ¢).tip(¢);
  }
}
