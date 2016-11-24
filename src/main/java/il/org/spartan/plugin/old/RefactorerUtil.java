package il.org.spartan.plugin.old;

import static il.org.spartan.lisp.*;

import java.util.*;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;
import org.eclipse.jdt.core.*;
import org.eclipse.jface.operation.*;

import il.org.spartan.plugin.*;
import il.org.spartan.plugin.old.Refactorer.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.spartanizer.utils.*;

/** A utility class for {@link Refactorer} concrete implementation, containing
 * common method overrides.
 * @author Ori Roth
 * @since 2016 */
public class RefactorerUtil {
  public static final int MANY_PASSES = 20;

  public static String getTipperName(final Map<attribute, Object> a) {
    if (Refactorer.unknown.equals(a.get(attribute.TIPPER)))
      try {
        final IMarker iMarker = (IMarker) a.get(attribute.MARKER);
        final Object att = iMarker.getAttribute(Builder.SPARTANIZATION_TIPPER_KEY);
        @SuppressWarnings("unchecked") final Class<? extends Tipper<?>> ¢ = (Class<? extends Tipper<?>>) att;
        a.put(attribute.TIPPER, ¢.getSimpleName());
      } catch (final CoreException ¢) {
        monitor.log(¢);
        a.put(attribute.TIPPER, "tip-core exception");
      }
    return a.get(attribute.TIPPER) + "";
  }

  public static String projectName(final Map<attribute, Object> ¢) {
    final IMarker $ = (IMarker) ¢.get(attribute.MARKER);
    return $.getResource() == null ? null : $.getResource().getProject().getName();
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
    while (i.length() < s.length())
      i = " " + i;
    return i + "/" + s;
  }

  public static String plurals(final String s, final int i) {
    return i == 1 ? s : s + "s";
  }

  public static String plurales(final String s, final int i) {
    return i == 1 ? s : s + "es";
  }

  public static IRunnableWithProgress countTipsInProject(@SuppressWarnings("unused") final AbstractGUIApplicator __, final List<ICompilationUnit> us,
      final Map<attribute, Object> a, final attribute t) {
    if (us.isEmpty())
      return null;
    final Trimmer tr = new Trimmer();
    return new IRunnableWithProgress() {
      @Override @SuppressWarnings("boxing") public void run(final IProgressMonitor pm) {
        pm.beginTask("Counting tips in " + first(us).getResource().getProject().getName(), IProgressMonitor.UNKNOWN);
        tr.setICompilationUnit(first(us));
        a.put(t, tr.countTips());
        pm.done();
      }
    };
  }
}
