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
   * @param pt project path
   */
  public static void setProjectPath(IPath pt) {
    p = pt.removeLastSegments(1);
  }
  /**
   * Get source code which was set before. Updated source not guaranteed
   *
   * @param cu current compilation unit
   * @return source code of compilation unit
   */
  public static String get(IPath pt) {
    return sm.get(pt.toString());
  }
  /**
   * Set current {@link CompilationUnit} in use
   *
   * @param cu current compilation unit
   */
  public static void set(CompilationUnit cu) {
    final IJavaElement je = cu.getJavaElement();
    if (je == null)
      return;
    final IPath fp = je.getPath();
    try {
      sm.put(fp.toString(), FileUtils.readFromFile(p.append(fp).toString()));
    } catch (final IOException x) {
      x.printStackTrace();
    }
  }
}
