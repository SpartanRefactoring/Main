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
import org.jetbrains.annotations.NotNull;

/** A utility class for {@link Refactorer} concrete implementation, containing
 * common method overrides.
 * @author Ori Roth
 * @since 2016 */
@Deprecated
public enum RefactorerUtil {
  ;
  public static final int MANY_PASSES = 20;

  @NotNull @SuppressWarnings("unchecked") public static String getTipperName(@NotNull final Map<attribute, Object> $) {
    if (Refactorer.unknown.equals($.get(attribute.TIPPER)))
      try {
        $.put(attribute.TIPPER,
            ((Class<? extends Tipper<?>>) ((IMarker) $.get(attribute.MARKER)).getAttribute(Builder.SPARTANIZATION_TIPPER_KEY)).getSimpleName());
      } catch (final CoreException ¢) {
        monitor.log(¢);
        $.put(attribute.TIPPER, "tip-core exception");
      }
    return $.get(attribute.TIPPER) + "";
  }

  public static String projectName(@NotNull final Map<attribute, Object> ¢) {
    final IMarker $ = (IMarker) ¢.get(attribute.MARKER);
    return $.getResource() == null ? null : $.getResource().getProject().getName();
  }

  @SuppressWarnings("unchecked") public static int getCUsCount(@NotNull final Map<attribute, Object> ¢) {
    return ((Collection<ICompilationUnit>) ¢.get(attribute.CU)).size();
  }

  @SuppressWarnings("unchecked") public static int getChangesCount(@NotNull final Map<attribute, Object> ¢) {
    return ((Collection<ICompilationUnit>) ¢.get(attribute.CHANGES)).size();
  }

  public static IRunnableWithProgress countTipsInProject(@SuppressWarnings("unused") final AbstractGUIApplicator __,
      @NotNull final List<ICompilationUnit> us, @NotNull final Map<attribute, Object> m, final attribute a) {
    if (us.isEmpty())
      return null;
    final Trimmer $ = new Trimmer();
    return λ -> {
      λ.beginTask("Counting tips in " + first(us).getResource().getProject().getName(), IProgressMonitor.UNKNOWN);
      $.setICompilationUnit(first(us));
      m.put(a, Integer.valueOf($.countTips()));
      λ.done();
    };
  }
}
