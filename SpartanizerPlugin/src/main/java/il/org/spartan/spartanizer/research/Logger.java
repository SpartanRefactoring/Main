package il.org.spartan.spartanizer.research;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.safety.*;

/** @author Ori Marcovitch
 * @since 2016 */
public class Logger {
  private static Map<String, Integer> npCounter = new HashMap<>();
  private static String currentFile;

  public static void enterFile(final String file) {
    currentFile = file;
  }

  public static void summarizeFile() {
    for (String k : npCounter.keySet())
      System.out.println(npCounter.get(k) + " : " + k);
    npCounter = new HashMap<>();
  }

  public static void markNP(final ASTNode n, final String np) {
    MethodDeclaration m = findMethodAncestor(n);
    String s = findTypeAncestor(n) + "~" + m.getName() + m.parameters();
    if (!npCounter.containsKey(s))
      npCounter.put(s, Integer.valueOf(0));
    npCounter.put(s, Integer.valueOf(npCounter.get(s).intValue() + 1));
  }

  /** @param ¢
   * @return */
  private static MethodDeclaration findMethodAncestor(final ASTNode ¢) {
    ASTNode n = ¢;
    while (!iz.methodDeclaration(n))
      n = n.getParent();
    return az.methodDeclaration(n);
  }

  /** @param ¢
   * @return */
  private static String findTypeAncestor(final ASTNode ¢) {
    ASTNode n = ¢;
    String $ = "";
    while (n != null) {
      while (!iz.abstractTypeDeclaration(n) && n != null)
        n = n.getParent();
      if (n == null)
        break;
      $ += "." + az.abstractTypeDeclaration(n).getName();
      n = n.getParent();
    }
    return $.substring(1);
  }
  // private boolean hasAncestorType(final ASTNode ¢) {
  // if (¢ == null)
  // return false;
  // ASTNode n = ¢.getParent();
  // while (!iz.abstractTypeDeclaration(n))
  // n = n.getParent();
  // return az.abstractTypeDeclaration(n).getName() + "";
  // }
}
