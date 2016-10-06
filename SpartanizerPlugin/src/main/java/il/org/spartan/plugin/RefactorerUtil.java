package il.org.spartan.plugin;

import java.util.*;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;
import org.eclipse.jdt.core.*;

import il.org.spartan.plugin.Refactorer.*;
import il.org.spartan.spartanizer.tipping.*;

/** A utility class for {@link Refactorer} concrete implementation, containing
 * common method overrides.
 * @author Ori Roth
 * @since 2016 */
public class RefactorerUtil {
  public static final int MANY_PASSES = 20;

  @SuppressWarnings({ "rawtypes", "unchecked" }) public static String getTipperName(final Map<attribute, Object> ¢) {
    try {
      return ((Class<? extends Tipper>) ((IMarker) ¢.get(attribute.MARKER)).getAttribute(Builder.SPARTANIZATION_TIPPER_KEY)).getSimpleName();
    } catch (final CoreException x) {
      monitor.log(x);
      return "tip";
    }
  }

  public static String projectName(final Map<attribute, Object> ¢) {
    final IMarker m = (IMarker) ¢.get(attribute.MARKER);
    return m.getResource() == null ? null : m.getResource().getProject().getName();
  }

  @SuppressWarnings("unchecked") public static int getCUsCount(final Map<attribute, Object> ¢) {
    return ((Collection<ICompilationUnit>) ¢.get(attribute.CU)).size();
  }

  @SuppressWarnings("unchecked") public static int getChangesCount(final Map<attribute, Object> ¢) {
    return ((Collection<ICompilationUnit>) ¢.get(attribute.CHANGES)).size();
  }

  public static String completionIndex(final List<ICompilationUnit> us, final ICompilationUnit u) {
    final String s = us.size() + "";
    String i = us.indexOf(u) + 1 + "";
    for (; i.length() < s.length(); i = " " + i)
      ;
    return i + "/" + s;
  }

  public static String plurals(final String s, final int i) {
    return i == 1 ? s : s + "s";
  }

  public static String plurales(final String s, final int i) {
    return i == 1 ? s : s + "es";
  }
}
