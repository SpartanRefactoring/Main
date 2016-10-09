package il.org.spartan.spartanizer.research;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

/** @author Ori Marcovitch
 * @since 2016 */
public class NPMarkerVisitor extends ASTVisitor {
  static Map<Class<?>, List<NanoPattern>> nanoPatterns = new HashMap<>();

  @Override public void postVisit(final ASTNode n) {
    for (final NanoPattern ¢ : nanoPatterns.get(n.getClass()))
      if (¢.matches(n))
        mark(¢, n);
  }

  /** @param p
   * @param n */
  private void mark(final NanoPattern p, final ASTNode n) {
    // TODO Auto-generated method stub
  }
}
