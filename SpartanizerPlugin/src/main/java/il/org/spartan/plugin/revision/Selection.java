package il.org.spartan.plugin.revision;

import java.util.*;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;
import org.eclipse.jdt.core.*;
import org.eclipse.jface.text.*;
import org.eclipse.jface.viewers.*;
import org.eclipse.ui.*;
import org.eclipse.ui.views.markers.*;

import il.org.spartan.plugin.*;

/** Describes a selection, containing selected compilation unit(s) and text
 * selection
 * @author Ori Roth
 * @since 2016 */
public class Selection {
  public List<ICompilationUnit> compilationUnits;
  public ITextSelection textSelection;

  public Selection(final List<ICompilationUnit> compilationUnits, final ITextSelection textSelection) {
    this.compilationUnits = compilationUnits != null ? compilationUnits : new ArrayList<>();
    this.textSelection = textSelection;
  }

  public Selection setCompilationUnits(final List<ICompilationUnit> ¢) {
    compilationUnits = ¢ != null ? ¢ : new ArrayList<>();
    return this;
  }

  public Selection setTextSelection(final ITextSelection ¢) {
    textSelection = ¢;
    return this;
  }

  public Selection add(final ICompilationUnit ¢) {
    if (¢ != null)
      compilationUnits.add(¢);
    return this;
  }

  public Selection add(final List<ICompilationUnit> ¢) {
    if (¢ != null)
      compilationUnits.addAll(¢);
    return this;
  }

  /** [[SuppressWarningsSpartan]] */
  public Selection add(final ICompilationUnit... ¢) {
    for (final ICompilationUnit u : ¢)
      compilationUnits.add(u);
    return this;
  }

  public Selection unify(final Selection ¢) {
    compilationUnits.addAll(¢.compilationUnits);
    return this;
  }

  public static Selection empty() {
    return new Selection(null, null);
  }

  public static Selection of(final List<ICompilationUnit> ¢) {
    return new Selection(¢, null);
  }

  public static Selection of(final ICompilationUnit ¢) {
    return new Selection(¢ == null ? null : Collections.singletonList(¢), null);
  }

  public static Selection of(final ICompilationUnit u, final ITextSelection s) {
    return new Selection(u == null ? null : Collections.singletonList(u), s);
  }

  public static Selection of(final ICompilationUnit[] ¢) {
    return new Selection(Arrays.asList(¢), null);
  }

  public Selection fixEmptyTextSelection() {
    if (compilationUnits == null || compilationUnits.size() != 1 || textSelection == null || textSelection.getLength() > 0)
      return this;
    final ICompilationUnit u = compilationUnits.get(0);
    final IResource r = u.getResource();
    if (!(r instanceof IFile))
      return this;
    final int o = textSelection.getOffset();
    try {
      for (final IMarker m : ((IFile) r).findMarkers(Builder.MARKER_TYPE, true, IResource.DEPTH_INFINITE)) {
        final int cs = ((Integer) m.getAttribute(IMarker.CHAR_START)).intValue();
        final int ce = ((Integer) m.getAttribute(IMarker.CHAR_END)).intValue();
        if (cs <= o && ce >= o)
          return setTextSelection(new TextSelection(cs, ce - cs));
      }
    } catch (final CoreException x) {
      monitor.log(x);
      return this;
    }
    return this;
  }

  @Override public String toString() {
    if (compilationUnits == null || compilationUnits.isEmpty())
      return "{empty}";
    final int s = compilationUnits == null ? 0 : compilationUnits.size();
    return "{" + (compilationUnits == null ? null : s + " " + RefactorerUtil.plurals("file", s)) + ", "
        + (textSelection == null ? null : printable(textSelection)) + "}";
  }

  public static String printable(final ITextSelection ¢) {
    return "(" + ¢.getOffset() + "," + ¢.getLength() + ")";
  }

  /** TODO Roth: spartanize class TODO Roth: check for circles
   * [[SuppressWarningsSpartan]] */
  public static class Util {
    public static Selection getCurrentCompilationUnit() {
      final ISelection s = getSelection();
      if (s == null)
        return Selection.empty();
      if (s instanceof ITextSelection)
        return by((ITextSelection) s).setTextSelection(null);
      return Selection.empty();
    }

    /** We may use {@link selection#getProject} instead. */
    public static Selection getAllCompilationUnits() {
      final ISelection s = getSelection();
      if (s == null)
        return Selection.empty();
      if (s instanceof ITextSelection)
        return by(getJavaProject()).setTextSelection(null);
      return Selection.empty();
    }

    public static Selection get() {
      final ISelection s = getSelection();
      if (s == null)
        return Selection.empty();
      if (s instanceof ITextSelection)
        return by((ITextSelection) s);
      if (s instanceof ITreeSelection)
        return by((ITreeSelection) s);
      return Selection.empty();
    }

    public static IProject project() {
      final ISelection s = getSelection();
      if (s == null || s instanceof ITextSelection)
        return getProject();
      if (s instanceof ITreeSelection) {
        final Object o = ((ITreeSelection) s).getFirstElement();
        if (o == null)
          return null;
        if (o instanceof MarkerItem) {
          final IMarker m = ((MarkerItem) o).getMarker();
          if (m == null)
            return null;
          final IResource r = m.getResource();
          return r == null ? null : r.getProject();
        }
        if (o instanceof IJavaElement) {
          final IJavaProject p = ((IJavaElement) o).getJavaProject();
          return p == null ? null : p.getProject();
        }
      }
      return null;
    }

    public static Selection by(final IMarker ¢) {
      if (¢ == null || !¢.exists())
        return null;
      final ITextSelection s = getTextSelection(¢);
      if (s == null)
        return Selection.empty();
      return by(¢.getResource()).setTextSelection(s);
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
    private static IProject getProject() {
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
      return r.getProject();
    }

    private static IJavaProject getJavaProject() {
      final IProject p = getProject();
      return p == null ? null : JavaCore.create(p);
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
      return i == null ? Selection.empty() : by(i.getAdapter(IResource.class)).setTextSelection(s).fixEmptyTextSelection();
    }

    private static Selection by(final IResource ¢) {
      return ¢ == null || !(¢ instanceof IFile) || !((IFile) ¢).getName().endsWith(".java") ? Selection.empty() : by((IFile) ¢);
    }

    private static Selection by(final IFile ¢) {
      return ¢ == null ? Selection.empty() : Selection.of(JavaCore.createCompilationUnitFrom(¢));
    }

    private static Selection by(final MarkerItem ¢) {
      return ¢ == null ? Selection.empty() : by(¢.getMarker());
    }

    private static Selection by(final ITreeSelection s) {
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
      return Selection.empty();
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

    private static ITextSelection getTextSelection(final IMarker ¢) {
      int cs;
      try {
        cs = ((Integer) ¢.getAttribute(IMarker.CHAR_START)).intValue();
        return new TextSelection(cs, ((Integer) ¢.getAttribute(IMarker.CHAR_END)).intValue() - cs);
      } catch (final CoreException x) {
        monitor.log(x);
      }
      return null;
    }
  }
}
