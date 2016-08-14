package il.org.spartan.refactoring.utils;

import java.util.*;

import org.eclipse.core.runtime.*;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

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
   * Path for java programs without file
   */
  public static final IPath NONE_PATH = new Path("");

  /**
   * @param p
   *          file path
   * @param s
   *          file source
   */
  public synchronized static void set(final IPath p, final String s) {
    if (p == null || s == null)
      return;
    sm.put(p.toString(), s);
  }
  /**
   * @param u
   *          compilation unit
   * @return disabler, able of determining whether an {@link ASTNode} is
   *         spartanization disabled
   */
  public static Disable makeDisable(final CompilationUnit u) {
    return new Disable(u);
  }
  /**
   * @param u
   *          compilation unit
   * @param r
   *          rewriter
   * @param g
   *          text edit group
   * @return scalpel for replacement operation
   */
  public static Scalpel getScalpel(final CompilationUnit u, final ASTRewrite r, final TextEditGroup g) {
    return new Scalpel(u, get(u), r, g);
  }
  private static String get(final CompilationUnit u) {
    if (u == null)
      return null;
    final IJavaElement je = u.getJavaElement();
    return sm.get((je == null ? NONE_PATH : je.getPath()).toString());
  }
}
