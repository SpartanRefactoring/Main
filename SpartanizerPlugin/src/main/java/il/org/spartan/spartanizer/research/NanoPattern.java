package il.org.spartan.spartanizer.research;

import org.eclipse.jdt.core.dom.*;

/** @author Ori Marcovitch
 * @since 2016 */
public abstract class NanoPattern {
  public abstract boolean matches(ASTNode n);
}
