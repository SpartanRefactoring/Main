package il.org.spartan.spartanizer.research.nanos.common;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.engine.*;

public class NotImplementedNanoPattern<N extends ASTNode> extends NanoPatternTipper<N> {
  @Override public String technicalName() {
    return null;
  }

  @Override protected Tip pattern(@SuppressWarnings("unused") N ¢) {
    return null;
  }

  @Override public boolean canTip(@SuppressWarnings("unused") N __) {
    return false;
  }
}
