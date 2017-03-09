package il.org.spartan.spartanizer.research.nanos.methods;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.research.nanos.common.*;

/** Matches getter methods
 * @author Ori Marcovitch */
public class Getter extends JavadocMarkerNanoPattern {
  private static final long serialVersionUID = -6493535033790617317L;

  @Override protected boolean prerequisites(final MethodDeclaration ¢) {
    return hazOneStatement(¢)//
        && hazNoParameters(¢)//
        && getter(¢);
  }
}
