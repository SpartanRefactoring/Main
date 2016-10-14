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
 * selection.
 * @author Ori Roth
 * @since 2.6 */
public class Selection {
  /** Compilation units selected. Has to contain at least one compilation unit
   * for the selection to be considered non empty. */
  public List<ICompilationUnit> compilationUnits;
  /** Text selected, if any. */
  public ITextSelection textSelection;
  /** Name of the selection. */
  public String name;

  public Selection(final List<ICompilationUnit> compilationUnits, final ITextSelection textSelection, final String name) {
    this.compilationUnits = compilationUnits != null ? compilationUnits : new ArrayList<>();
    this.textSelection = textSelection;
    this.name = name;
  }

  /** Setter for {@link Selection#compilationUnits}.
   * @param ¢ JD
   * @return this selection */
  public Selection setCompilationUnits(final List<ICompilationUnit> ¢) {
    compilationUnits = ¢ != null ? ¢ : new ArrayList<>();
    return this;
  }

  /** Setter for {@link Selection#textSelection}.
   * @param ¢ JD
   * @return this selection */
  public Selection setTextSelection(final ITextSelection ¢) {
    textSelection = ¢;
    return this;
  }

  /** Setter for {@link Selection#name}.
   * @param ¢ JD
   * @return this selection */
  public Selection setName(final String ¢) {
    name = ¢;
    return this;
  }

  /** Adds a compilation unit to this selection.
   * @param ¢ JD
   * @return this selection */
  public Selection add(final ICompilationUnit ¢) {
    if (¢ != null)
      compilationUnits.add(¢);
    return this;
  }

  /** Adds compilation units to this selection.
   * @param ¢ JD
   * @return this selection */
  public Selection add(final List<ICompilationUnit> ¢) {
    if (¢ != null)
      compilationUnits.addAll(¢);
    return this;
  }

  /** Adds compilation units to this selection.
   * @param ¢ JD
   * @return this selection [[SuppressWarningsSpartan]] */
  public Selection add(final ICompilationUnit... ¢) {
    for (final ICompilationUnit u : ¢)
      compilationUnits.add(u);
    return this;
  }

  /** Merge a selection into this selection.
   * @param ¢ JD
   * @return this selection */
  public Selection unify(final Selection ¢) {
    compilationUnits.addAll(¢.compilationUnits);
    return this;
  }

  /** Factory method.
   * @return empty selection */
  public static Selection empty() {
    return new Selection(null, null, null);
  }

  /** Factory method.
   * @param ¢ JD
   * @return selection by compilation units */
  public static Selection of(final List<ICompilationUnit> ¢) {
    return new Selection(¢, null, getName(¢));
  }

  /** Factory method.
   * @param ¢ JD
   * @return selection by compilation unit */
  public static Selection of(final ICompilationUnit ¢) {
    return new Selection(¢ == null ? null : Collections.singletonList(¢), null, getName(¢));
  }

  /** Factory method.
   * @param ¢ JD
   * @return selection by compilation unit and text selection */
  public static Selection of(final ICompilationUnit u, final ITextSelection s) {
    return new Selection(u == null ? null : Collections.singletonList(u), s, getName(u));
  }

  /** Factory method.
   * @param ¢ JD
   * @return selection by compilation units */
  public static Selection of(final ICompilationUnit[] ¢) {
    List<ICompilationUnit> l = Arrays.asList(¢);
    return new Selection(l, null, getName(l));
  }

  /** Extract a name for a selection from list of compilation units.
   * @param ¢ JD
   * @return name of selection */
  private static String getName(List<ICompilationUnit> ¢) {
    return ¢ == null || ¢.isEmpty() ? null : ¢.size() == 1 ? ¢.get(0).getElementName() : ¢.get(0).getResource().getProject().getName();
  }

  /** Extract a name for a selection from a compilation unit.
   * @param ¢ JD
   * @return name of selection */
  private static String getName(ICompilationUnit ¢) {
    return ¢ == null ? null : ¢.getElementName();
  }

  /** Fix selection with empty (but existing) text selection, to contain
   * containing marker.
   * @return this selection */
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
    if (isEmpty())
      return "{empty}";
    final int s = compilationUnits == null ? 0 : compilationUnits.size();
    return "{" + (compilationUnits == null ? null : s + " " + RefactorerUtil.plurals("file", s)) + ", "
        + (textSelection == null ? null : printable(textSelection)) + "}";
  }

  /** @return size of this selection by compilation units */
  public int size() {
    return isEmpty() ? 0 : compilationUnits.size();
  }

  /** @return true iff the selection is empty, i.e. contain no compilation
   *         units */
  public boolean isEmpty() {
    return compilationUnits == null || compilationUnits.isEmpty() || (textSelection != null && textSelection.getLength() <= 0);
  }

  /** @param ¢ JD
   * @return printable string of the text selection */
  private static String printable(final ITextSelection ¢) {
    return "(" + ¢.getOffset() + "," + ¢.getLength() + ")";
  }

  // TODO Roth: spartanize class TODO Roth: check for circles (probably there
  // are none though)
  /** Utility class for finding selections, and file/project by open editor.
   * @author Ori Roth
   * @since 2.6 [[SuppressWarningsSpartan]] */
  public static class Util {
    /** Default name for marker selections. */
    private static final String MARKER_NAME = "marker";
    /** Default name for text selections. */
    private static final String SELECTION_NAME = "selection";
    /** Default name for default package selections. */
    private static final String DEFAULT_PACKAGE_NAME = "(default package)";

    // TODO Roth: delete this ASAP
    public static Selection getCurrentCompilationUnit() {
      final Selection $ = getCompilationUnit();
      return $ != null ? $ : Selection.empty();
    }

    // TODO Roth: delete this ASAP
    // We may use {@link selection#getProject} instead.
    public static Selection getAllCompilationUnits() {
      final ISelection s = getSelection();
      if (s == null)
        return Selection.empty();
      if (s instanceof ITextSelection) {
        IJavaProject p = getJavaProject();
        return by(p).setTextSelection(null).setName(p.getElementName());
      }
      return Selection.empty();
    }

    /** Gets eclipse selection by the user - could be icons from the package
     * explorer, markers from the problems view, text selected in editor and
     * more.
     * @return current user selection */
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

    /** @return current project by selection */
    public static IProject project() {
      final ISelection s = getSelection();
      if (s == null || s instanceof ITextSelection)
        return getProject();
      if (s instanceof ITreeSelection) {
        final Object o = ((ITreeSelection) s).getFirstElement();
        if (o == null)
          return getProject();
        if (o instanceof MarkerItem) {
          final IMarker m = ((MarkerItem) o).getMarker();
          if (m == null)
            return null;
          final IResource r = m.getResource();
          return r == null ? getProject() : r.getProject();
        }
        if (o instanceof IJavaElement) {
          final IJavaProject p = ((IJavaElement) o).getJavaProject();
          return p == null ? getProject() : p.getProject();
        }
      }
      return getProject();
    }

    /** @param ¢ JD
     * @return current user selection by marker */
    public static Selection by(final IMarker ¢) {
      if (¢ == null || !¢.exists())
        return null;
      final ITextSelection s = getTextSelection(¢);
      if (s == null)
        return Selection.empty();
      return by(¢.getResource()).setTextSelection(s).setName(MARKER_NAME);
    }

    /** @return current user unprocessed selection */
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

    /** Depends on local editor.
     * @return current project by user selection */
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

    /** Depends on local editor.
     * @return current java project by user selection */
    private static IJavaProject getJavaProject() {
      final IProject p = getProject();
      return p == null ? null : JavaCore.create(p);
    }

    /** Depends on local editor.
     * @return current compilation unit by user selection */
    private static Selection getCompilationUnit() {
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
      return i == null ? null : by(i.getAdapter(IResource.class));
    }

    /** Depends on local editor. We assume an editor is open while the text is
     * been selected - if not, there s no point of the selection.
     * @param ¢ JD
     * @return current user selection by text selection */
    private static Selection by(final ITextSelection s) {
      final Selection $ = getCompilationUnit();
      return $ == null ? Selection.empty() : $.setTextSelection(s).fixEmptyTextSelection().setName(SELECTION_NAME);
    }

    /** Only for {@link IFile} resources.
     * @param ¢ JD
     * @return current user selection by resource */
    private static Selection by(final IResource ¢) {
      return ¢ != null && ¢ instanceof IFile && ((IFile) ¢).getName().endsWith(".java") ? by((IFile) ¢) : Selection.empty();
    }

    /** @param ¢ JD
     * @return current user selection by file */
    private static Selection by(final IFile ¢) {
      return ¢ == null ? Selection.empty() : Selection.of(JavaCore.createCompilationUnitFrom(¢)).setName(¢.getName());
    }

    /** @param ¢ JD
     * @return current user selection by marker item */
    private static Selection by(final MarkerItem ¢) {
      return ¢ == null ? Selection.empty() : by(¢.getMarker()).setName(MARKER_NAME);
    }

    /** @param ¢ JD
     * @return current user selection by tree selection */
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

    /** @param ¢ JD
     * @return current user selection by java project */
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
      return $.setName(p.getElementName());
    }

    /** @param ¢ JD
     * @return current user selection by package root */
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
      return $.setName(r.getElementName());
    }

    /** @param ¢ JD
     * @return current user selection by package */
    private static Selection by(final IPackageFragment f) {
      try {
        return f == null ? Selection.empty()
            : Selection.of(f.getCompilationUnits()).setName("".equals(f.getElementName()) ? DEFAULT_PACKAGE_NAME : f.getElementName());
      } catch (final JavaModelException x) {
        monitor.log(x);
        return Selection.empty();
      }
    }

    /** @param ¢ JD
     * @return current user selection by member */
    private static Selection by(final IMember m) {
      ISourceRange r;
      try {
        r = m.getSourceRange();
      } catch (final JavaModelException x) {
        monitor.log(x);
        return Selection.empty();
      }
      return Selection.of(m.getCompilationUnit(), new TextSelection(r.getOffset(), r.getLength())).setName(m.getElementName());
    }

    /** @param ¢ JD
     * @return text selection for marker */
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
