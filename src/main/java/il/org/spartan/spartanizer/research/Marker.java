package il.org.spartan.spartanizer.research;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.safety.*;

/** A marker to mark an ASTNode as matched by a NanoPattern.
 * @author Ori Marcovitch
 * @since 2016 */
public class Marker {
  public final String np;
  private static final String AST_PROPERTY_NAME_NP_LIST = "MARKER";
  private static final String AST_PROPERTY_NAME_MARKED_FOR_REPORT = "MARKER";

  private Marker(final String np) {
    this.np = np;
  }

  @SuppressWarnings("unchecked") public static void mark(final String p, final ASTNode n) {
    if (n.getProperty(Marker.AST_PROPERTY_NAME_NP_LIST) == null)
      n.setProperty(Marker.AST_PROPERTY_NAME_NP_LIST, new ArrayList<Marker>());
    ((List<Marker>) n.getProperty(Marker.AST_PROPERTY_NAME_NP_LIST)).add(new Marker(p));
  }

  public static boolean isMarked(final ASTNode ¢) {
    return ¢.getProperty(Marker.AST_PROPERTY_NAME_NP_LIST) != null;
  }

  @SuppressWarnings("unchecked") public static List<Marker> getMarkers(final ASTNode ¢) {
    return (List<Marker>) ¢.getProperty(Marker.AST_PROPERTY_NAME_NP_LIST);
  }

  /** @param ¢ JD
   * @return */
  public static boolean isMarkedForReport(final MethodDeclaration ¢) {
    return ¢.getProperty(Marker.AST_PROPERTY_NAME_MARKED_FOR_REPORT) != null
        && ¢.getProperty(Marker.AST_PROPERTY_NAME_MARKED_FOR_REPORT) == Boolean.TRUE;
  }

  /** @param ¢ JD
   * @return */
  public static void markForReport(final ASTNode ¢) {
    findMethodAncestor(¢).setProperty(Marker.AST_PROPERTY_NAME_MARKED_FOR_REPORT, Boolean.TRUE);
  }

  /** @param ¢
   * @return */
  private static MethodDeclaration findMethodAncestor(final ASTNode ¢) {
    System.out.println("been here done that");
    ASTNode n = ¢;
    while (!iz.methodDeclaration(n))
      n = n.getParent();
    return az.methodDeclaration(n);
  }

  /** @param ¢ JD
   * @return */
  public static void unmarkForReport(final MethodDeclaration ¢) {
    ¢.setProperty(Marker.AST_PROPERTY_NAME_MARKED_FOR_REPORT, Boolean.FALSE);
  }
}
