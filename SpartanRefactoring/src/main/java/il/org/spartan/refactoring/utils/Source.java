package il.org.spartan.refactoring.utils;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.text.edits.TextEditGroup;

/**
 * Give access to source code per file. Creates {@link Disable}rs and
 * {@link Scalpel}s to manipulate source code and change it.
 *
 * @author Ori Roth
 * @since 2016-04-17
 */
public class Source {
  private static Map<String, String> sm = new HashMap<>();

  /**
   * @param p file path
   * @param s file source
   */
  public static void set(IPath p, String s) {
    if (p == null || s == null)
      return;
    sm.put(p.toString(), s);
  }
  /**
   * @param u compilation unit
   * @return disabler, able of determining whether an {@link ASTNode} is
   *         spartanization disabled
   */
  public static Disable getDisable(CompilationUnit u) {
    return new Disable(u);
  }
  /**
   * @param u compilation unit
   * @param r rewriter
   * @param g text edit group
   * @return scalpel for replacement operation
   */
  public static Scalpel getScalpel(CompilationUnit u, ASTRewrite r, TextEditGroup g) {
    return new Scalpel(u, get(u), r, g);
  }
  private static String get(CompilationUnit u) {
    if (u == null)
      return null;
    final IJavaElement je = u.getJavaElement();
    return je == null ? null : sm.get(je.getPath().toString());
  }
}
