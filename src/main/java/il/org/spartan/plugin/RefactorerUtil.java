package il.org.spartan.plugin;

import java.util.*;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;
import org.eclipse.jdt.core.*;
import org.eclipse.jface.operation.*;
import org.eclipse.jface.text.*;
import org.eclipse.jface.viewers.*;
import org.eclipse.ui.*;
import org.eclipse.ui.views.markers.*;

import il.org.spartan.plugin.Refactorer.*;
import il.org.spartan.plugin.revision.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;

/** A utility class for {@link Refactorer} concrete implementation, containing
 * common method overrides.
 * @author Ori Roth
 * @since 2016 */
public class RefactorerUtil {
  public static final int MANY_PASSES = 20;

  @SuppressWarnings({ "rawtypes", "unchecked" }) public static String getTipperName(final Map<attribute, Object> ¢) {
    if (Refactorer.unknown.equals(¢.get(attribute.TIPPER)))
      try {
        ¢.put(attribute.TIPPER,
            ((Class<? extends Tipper>) ((IMarker) ¢.get(attribute.MARKER)).getAttribute(Builder.SPARTANIZATION_TIPPER_KEY)).getSimpleName());
      } catch (final CoreException x) {
        monitor.log(x);
        ¢.put(attribute.TIPPER, "tip");
      }
    return ¢.get(attribute.TIPPER) + "";
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
    for (; i.length() < s.length();)
      i = " " + i;
    return i + "/" + s;
  }

  public static String plurals(final String s, final int i) {
    return i == 1 ? s : s + "s";
  }

  public static String plurales(final String s, final int i) {
    return i == 1 ? s : s + "es";
  }

  /** [[SuppressWarningsSpartan]] */
  public static IRunnableWithProgress countTipsInProject(@SuppressWarnings("unused") final GUI$Applicator __, final List<ICompilationUnit> us,
      final Map<attribute, Object> m, final attribute t) {
    if (us.isEmpty())
      return null;
    final Trimmer tr = new Trimmer();
    return new IRunnableWithProgress() {
      @SuppressWarnings("boxing") @Override public void run(final IProgressMonitor pm) {
        pm.beginTask("Counting tips in " + us.get(0).getResource().getProject().getName(), IProgressMonitor.UNKNOWN);
        tr.setICompilationUnit(us.get(0));
        m.put(t, tr.countTips());
        pm.done();
      }
    };
  }
}
