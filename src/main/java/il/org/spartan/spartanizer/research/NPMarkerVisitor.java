package il.org.spartan.spartanizer.research;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

/** @author Ori Marcovitch
 * @since 2016 */
public class NPMarkerVisitor extends ASTVisitor {

  @Override public void postVisit(ASTNode n) {
    for (NanoPattern ¢ : NanoPatternsPool.get(n.getClass()))
      if (¢.matches(n))
        mark(¢, n);
  }

  /** @param p
   * @param n */
  @SuppressWarnings("unchecked") private static void mark(NanoPattern p, ASTNode n) {
    if (n.getProperty(Marker.AST_PROPERTY_NAME_NP_LIST) == null)
      n.setProperty(Marker.AST_PROPERTY_NAME_NP_LIST, new ArrayList<Marker>());
    ((List<Marker>) n.getProperty(Marker.AST_PROPERTY_NAME_NP_LIST)).add(new Marker(p));
  }
}
