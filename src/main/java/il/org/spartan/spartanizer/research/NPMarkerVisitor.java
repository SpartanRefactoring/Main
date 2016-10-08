package il.org.spartan.spartanizer.research;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

/** @author Ori Marcovitch
 * @since 2016 */
public class NPMarkerVisitor extends ASTVisitor {
  static Map<Class<?>, List<NanoPattern>> nanoPatterns = new HashMap<>();

  @Override public void postVisit(ASTNode n) {
    for (NanoPattern ¢ : nanoPatterns.get(n.getClass()))
      if (¢.matches(n))
        mark(¢, n);
  }

  /** @param p
   * @param n */
  private void mark(NanoPattern p, ASTNode n) {
    // TODO Auto-generated method stub
  }
}
