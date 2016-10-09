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

  /** TODO Roth: spartanize class TODO Roth: check for circles
   * [[SuppressWarningsSpartan]] */
  public static class selection {
    public static Selection getCurrentCompilationUnit() {
      final ISelection s = getSelection();
      if (s == null)
        return Selection.empty();
      if (s instanceof ITextSelection)
        return by((ITextSelection) s).setTextSelection(null);
      return Selection.empty();
    }

    public static Selection getAllCompilationUnits() {
      final ISelection s = getSelection();
      if (s == null)
        return Selection.empty();
      if (s instanceof ITextSelection)
        return by(getProject()).setTextSelection(null);
      return Selection.empty();
    }

    public static Selection get() {
      final ISelection s = getSelection();
      if (s == null)
        return Selection.empty();
      if (s instanceof ITextSelection)
        return by((ITextSelection) s);
      if (s instanceof TreeSelection)
        return by((TreeSelection) s);
      return Selection.empty();
    }

    private static ISelection getSelection() {
      final IWorkbench wb = PlatformUI.getWorkbench();
      if (wb == null)
        return null;
      final IWorkbenchWindow w = wb.getActiveWorkbenchWindow();
      if (w == null)
        return null;
      final ISelectionService s = w.getSelectionService();
      return s == null ? null : s.getSelection();
    }

    /** Depends on local editor */
    private static IJavaProject getProject() {
      final IWorkbench wb = PlatformUI.getWorkbench();
      if (wb == null)
        return null;
      final IWorkbenchWindow w = wb.getActiveWorkbenchWindow();
      if (w == null)
        return null;
      final IWorkbenchPage p = w.getActivePage();
      if (p == null)
        return null;
      final IEditorPart e = p.getActiveEditor();
      if (e == null)
        return null;
      final IEditorInput i = e.getEditorInput();
      if (i == null)
        return null;
      final IResource r = i.getAdapter(IResource.class);
      if (r == null)
        return null;
      return JavaCore.create(r.getProject());
    }

    /** Depends on local editor */
    private static Selection by(final ITextSelection s) {
      final IWorkbench wb = PlatformUI.getWorkbench();
      if (wb == null)
        return null;
      final IWorkbenchWindow w = wb.getActiveWorkbenchWindow();
      if (w == null)
        return null;
      final IWorkbenchPage p = w.getActivePage();
      if (p == null)
        return null;
      final IEditorPart e = p.getActiveEditor();
      if (e == null)
        return null;
      final IEditorInput i = e.getEditorInput();
      return i == null ? null : by(i.getAdapter(IResource.class)).setTextSelection(s);
    }

    private static Selection by(final IResource ¢) {
      return ¢ == null || !(¢ instanceof IFile) ? null : by((IFile) ¢);
    }

    private static Selection by(final IFile ¢) {
      return ¢ == null ? null : Selection.of(JavaCore.createCompilationUnitFrom(¢));
    }

    private static Selection by(final MarkerItem ¢) {
      return ¢ == null ? null : by(¢.getMarker());
    }

    private static Selection by(final IMarker ¢) {
      return ¢ == null || !¢.exists() ? null : by(¢.getResource());
    }

    private static Selection by(final TreeSelection s) {
      final Object o = s.getFirstElement();
      if (o instanceof MarkerItem)
        return by((MarkerItem) o);
      if (o instanceof IJavaProject)
        return by((IJavaProject) o);
      if (o instanceof IPackageFragmentRoot)
        return by((IPackageFragmentRoot) o);
      if (o instanceof IPackageFragment)
        return by((IPackageFragment) o);
      if (o instanceof ICompilationUnit)
        return Selection.of((ICompilationUnit) o);
      if (o instanceof IMember)
        return by((IMember) o);
      return null;
    }

    private static Selection by(final IJavaProject p) {
      if (p == null)
        return Selection.empty();
      final Selection $ = Selection.empty();
      final IPackageFragmentRoot[] rs;
      try {
        rs = p.getPackageFragmentRoots();
      } catch (final JavaModelException x) {
        monitor.log(x);
        return Selection.empty();
      }
      for (final IPackageFragmentRoot ¢ : rs)
        $.unify(by(¢));
      return $;
    }

    private static Selection by(final IPackageFragmentRoot r) {
      final Selection $ = Selection.empty();
      try {
        for (final IJavaElement ¢ : r.getChildren())
          if (¢.getElementType() == IJavaElement.PACKAGE_FRAGMENT)
            $.unify(by((IPackageFragment) ¢));
      } catch (final JavaModelException x) {
        monitor.log(x);
        return Selection.empty();
      }
      return $;
    }

    private static Selection by(final IPackageFragment f) {
      try {
        return f == null ? Selection.empty() : Selection.of(f.getCompilationUnits());
      } catch (final JavaModelException x) {
        monitor.log(x);
        return Selection.empty();
      }
    }

    // TODO Roth: maybe the return of empty selection is unjustified (file
    // closed for instance)
    private static Selection by(final IMember m) {
      ISourceRange r;
      try {
        r = m.getSourceRange();
      } catch (final JavaModelException x) {
        monitor.log(x);
        return Selection.empty();
      }
      return Selection.of(m.getCompilationUnit(), new TextSelection(r.getOffset(), r.getLength()));
    }
  }
}
