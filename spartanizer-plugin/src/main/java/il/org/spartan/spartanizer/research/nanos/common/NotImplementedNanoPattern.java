package il.org.spartan.spartanizer.research.nanos.common;

import org.eclipse.jdt.core.dom.ASTNode;

import il.org.spartan.spartanizer.tipping.Tip;

public class NotImplementedNanoPattern<N extends ASTNode> extends NanoPatternTipper<N> {
  private static final long serialVersionUID = -0x6E6184F07B57A04CL;

  @Override public String technicalName() {
    return null;
  }
  @Override protected Tip pattern(@SuppressWarnings("unused") final N ¢) {
    return null;
  }
  @Override public boolean canTip(@SuppressWarnings("unused") final N __) {
    return false;
  }
}
