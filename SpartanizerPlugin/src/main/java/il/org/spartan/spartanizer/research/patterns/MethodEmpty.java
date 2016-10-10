package il.org.spartan.spartanizer.research.patterns;

import org.eclipse.jdt.core.dom.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;

public final class MethodEmpty extends NanoPatternTipper<MethodDeclaration> {
  @Override protected boolean prerequisite(MethodDeclaration ¢) {
    return ¢.getBody() != null && ¢.getBody().statements().isEmpty();
  }

  @Override public String description(MethodDeclaration ¢) {
    return "Empty method " + ¢.getName();
  }

  @Override public Tip tip(MethodDeclaration __) throws TipperFailure {
    throw new TipperFailure.TipNotImplementedException();
  }
}