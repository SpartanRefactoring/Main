package il.org.spartan.spartanizer.wring;

import org.eclipse.jdt.core.dom.*;

public class fluentTrimmer extends Trimmer {
  @SafeVarargs public <N extends ASTNode> fluentTrimmer(final Class<N> clazz, final Wring<N>... ws) {
    super(Toolbox.make(clazz, ws));
  }

  public fluentTrimmerApplication of(final String codeFragment) {
    return new fluentTrimmerApplication(this, codeFragment);
  }
}