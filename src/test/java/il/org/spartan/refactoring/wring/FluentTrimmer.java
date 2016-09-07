package il.org.spartan.refactoring.wring;

import org.eclipse.jdt.core.dom.*;

public class FluentTrimmer extends Trimmer {
  @SafeVarargs public <N extends ASTNode> FluentTrimmer(final Class<N> clazz, final Wring<N>... ws) {
    super(Toolbox.make(clazz, ws));
  }

  public FluentTrimmerApplication of(final String codeFragment) {
    return new FluentTrimmerApplication(this, codeFragment);
  }
}