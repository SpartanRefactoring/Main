package il.org.spartan.research.nanos.methods;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.research.nanos.common.*;

/** Matches getter methods
 * @author Ori Marcovitch */
public class Getter extends JavadocMarkerNanoPattern {
  private static final long serialVersionUID = -0x5A1DABB581A4D6E5L;

  @Override protected boolean prerequisites(final MethodDeclaration ¢) {
    return hazOneStatement(¢)//
        && hazNoParameters(¢)//
        && getter(¢);
  }
}
