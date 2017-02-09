package il.org.spartan.spartanizer.research.classifier.patterns;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.nanos.common.*;

/** TODO: Ori Marcovitch please add a description
 * @author Ori Marcovitch
 * @since 2016 */
public class ForEachEnhanced extends NanoPatternTipper<ForStatement> {
  final Collection<UserDefinedTipper<ForStatement>> tippers = new HashSet<UserDefinedTipper<ForStatement>>() {
    static final long serialVersionUID = 1L;
  };

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
