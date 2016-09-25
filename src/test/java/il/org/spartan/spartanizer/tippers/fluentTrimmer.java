package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.wringing.*;

public class fluentTrimmer extends Trimmer {
  @SafeVarargs public <N extends ASTNode> fluentTrimmer(final Class<N> clazz, final Tipper<N>... ws) {
    super(Toolbox.make(clazz, ws));
  }

  public fluentTrimmerApplication of(final String codeFragment) {
    return new fluentTrimmerApplication(this, codeFragment);
  }
}