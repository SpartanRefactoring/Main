package il.org.spartan.refactoring.utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.dom.CompilationUnit;

import il.org.spartan.utils.FileUtils;

/**
 * An accessible container for the source code, and other global resources
 *
 * @author Ori Roth
 * @since 2016-04-17
 */
public class Source {
  private static Map<String, String> sm = new HashMap<>();
  private static IPath p;

  /**
   * Get up to date source code
   *
   * @param u current CompilationUnit
   * @return source
   */
  public static String get(CompilationUnit u) {
    if (u != null) {
      final IJavaElement je = u.getJavaElement();
      return je == null ? null : Source.get(je.getPath());
    }
    return null;
  }
  /**
   * @param pt project path
   */
  public static synchronized void setProjectPath(IPath pt) {
    p = pt;
  }
  /**
   * Get source code which was set before. Updated source not guaranteed
   *
   * @param pt current file path
   * @return source code of compilation unit
   */
  public static synchronized String get(IPath pt) {
    return sm.get(pt.toString());
  }
  /**
   * Set current {@link CompilationUnit} in use
   *
   * @param cu current compilation unit
   */
  public static synchronized void set(CompilationUnit cu) {
    final IJavaElement je = cu.getJavaElement();
    if (je == null)
      return;
    final IPath fp = je.getPath();
    try {
      sm.put(fp.toString(), FileUtils.readFromFile(p.append(fp.removeFirstSegments(1)).toString()));
    } catch (final IOException x) {
      // TODO Ori: check it out
      x.printStackTrace();
    }
  }
}
