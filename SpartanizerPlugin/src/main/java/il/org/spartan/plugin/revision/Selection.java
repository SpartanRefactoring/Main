package il.org.spartan.plugin.revision;

import java.util.*;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jface.text.*;
import org.eclipse.jface.viewers.*;
import org.eclipse.ui.*;
import org.eclipse.ui.views.markers.*;

import il.org.spartan.plugin.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.engine.*;

/** Describes a selection, containing selected compilation unit(s) and text
 * selection
 * @author Ori Roth
 * @since 2.6 */
public class Selection {
  /** Compilation units in selection. */
  public List<ICompilationUnit> compilationUnits;
  /** Text selection in selection. Nullable. */
  public ITextSelection textSelection;
  /** Selection's name. */
  public String name;

  public Selection(final List<ICompilationUnit> compilationUnits, final ITextSelection textSelection, final String name) {
    this.compilationUnits = compilationUnits != null ? compilationUnits : new ArrayList<>();
    this.textSelection = textSelection;
    this.name = name;
  }

  /** Set compilation units for this selection.
   * @param ¢ JD
   * @return this selection */
  public Selection setCompilationUnits(final List<ICompilationUnit> ¢) {
    compilationUnits = ¢ != null ? ¢ : new ArrayList<>();
    return this;
  }

  /** Set text selection for this selection.
   * @param ¢ JD
   * @return this selection */
  public Selection setTextSelection(final ITextSelection ¢) {
    textSelection = ¢;
    return this;
  }

  /** Set name for this selection.
   * @param ¢ JD
   * @return this selection */
  public Selection setName(final String ¢) {
    name = ¢;
    return this;
  }

  /** Add a compilation unit for this selection.
   * @param ¢ JD
   * @return this selection */
  public Selection add(final ICompilationUnit ¢) {
    if (¢ != null)
      compilationUnits.add(¢);
    return this;
  }

  /** Add compilation units for this selection.
   * @param ¢ JD
   * @return this selection */
  public Selection add(final List<ICompilationUnit> ¢) {
    if (¢ != null)
      compilationUnits.addAll(¢);
    return this;
  }

  /** Add compilation units for this selection.
   * @param ¢ JD
   * @return this selection [[SuppressWarningsSpartan]] */
  public Selection add(final ICompilationUnit... ¢) {
    for (final ICompilationUnit u : ¢)
      compilationUnits.add(u);
    return this;
  }

  /** Extend current selection using compilation units from another selection.
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
    final List<ICompilationUnit> l = Arrays.asList(¢);
    return new Selection(l, null, getName(l));
  }

  /** @param ¢ JD
   * @return name for selection, extracted from the compilation units */
  private static String getName(final List<ICompilationUnit> ¢) {
    return ¢ == null || ¢.isEmpty() ? null : ¢.size() == 1 ? ¢.get(0).getElementName() : ¢.get(0).getResource().getProject().getName();
  }

  /** @param ¢ JD
   * @return name for selection, extracted from the compilation unit */
  private static String getName(final ICompilationUnit ¢) {
    return ¢ == null ? null : ¢.getElementName();
  }

  /** Extends selection with empty (yet existing) text selection to include
   * overlapping marker.
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

  /** @return selection's size in compilation units */
  public int size() {
    return isEmpty() ? 0 : compilationUnits.size();
  }

  /** @return true iff the selection is empty, i.e. it contains no compilation
   *         units */
  public boolean isEmpty() {
    return compilationUnits == null || compilationUnits.isEmpty() || textSelection != null && textSelection.getLength() <= 0;
  }

  /** @param ¢ JD
   * @return printable string describing the text selection */
  private static String printable(final ITextSelection ¢) {
    return "(" + ¢.getOffset() + "," + ¢.getLength() + ")";
  }

  /** TODO Roth: spartanize class TODO Roth: check for circles
   * [[SuppressWarningsSpartan]] */
  public static class Util {
    /** Default name for marker selections. */
    private static final String MARKER_NAME = "marker";
    /** Default name for text selections. */
    private static final String SELECTION_NAME = "selection";
    /** Default name for default package selections. */
    private static final String DEFAULT_PACKAGE_NAME = "(default package)";

    // TODO Roth: delete this ASAP
    /** @return selection of current compilation unit */
    public static Selection getCurrentCompilationUnit() {
      final Selection $ = getCompilationUnit();
      return $ == null ? Selection.empty() : $;
    }

    /** @param m JD
     * @return selection of current compilation unit by marker */
    public static Selection getCurrentCompilationUnit(final IMarker m) {
      if (!m.exists())
        return null;
      final IResource r = m.getResource();
      if (!(r instanceof IFile))
        return null;
      return by((IFile) r).setTextSelection(null);
    }

    // TODO Roth: delete this ASAP
    public static Selection getAllCompilationUnits() {
      final ISelection s = getSelection();
      if (s == null)
        return Selection.empty();
      if (s instanceof ITextSelection) {
        final IJavaProject p = getJavaProject();
        return by(p).setTextSelection(null).setName(p.getElementName());
      }
      return Selection.empty();
    }

    /** @return current user selection */
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

    /** @return current project */
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
     * @return selection by marker */
    public static Selection by(final IMarker ¢) {
      if (¢ == null || !¢.exists())
        return null;
      final ITextSelection s = getTextSelection(¢);
      if (s == null)
        return Selection.empty();
      return by(¢.getResource()).setTextSelection(s).setName(MARKER_NAME);
    }

    /** TODO Roth: do not create an ICompilationUnit for this Extends the marker
     * to contain parent node of some kind.
     * @param s JD
     * @param c JD
     * @return selection by extended marker */
    public static Selection expend(final IMarker m, final Class<? extends ASTNode> c) {
      if (m == null || !m.exists() || c == null || m.getResource() == null || !(m.getResource() instanceof IFile))
        return Selection.empty();
      final ICompilationUnit u = JavaCore.createCompilationUnitFrom((IFile) m.getResource());
      if (u == null)
        return Selection.empty();
      ASTNode n = getNodeByMarker(u, m);
      if (n == null)
        return Selection.empty();
      n = searchAncestors.forClass(c).from(n);
      if (n == null)
        return Selection.empty();
      return Selection.empty().add(u).setTextSelection(new TextSelection(n.getStartPosition(), n.getLength()));
    }

    /** @return current {@link ISelection} */
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

    /** @return current project */
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

    /** @return current java project */
    private static IJavaProject getJavaProject() {
      final IProject p = getProject();
      return p == null ? null : JavaCore.create(p);
    }

    /** Depends on local editor.
     * @return selection by current compilation unit */
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

    /** @param s JD
     * @return selection by text selection */
    private static Selection by(final ITextSelection s) {
      final Selection $ = getCompilationUnit();
      return $ == null ? Selection.empty() : $.setTextSelection(s).fixEmptyTextSelection().setName(SELECTION_NAME);
    }

    /** Only support selection by {@link IFile}.
     * @param ¢ JD
     * @return selection by file */
    private static Selection by(final IResource ¢) {
      return ¢ == null || !(¢ instanceof IFile) || !((IFile) ¢).getName().endsWith(".java") ? Selection.empty() : by((IFile) ¢);
    }

    /** @param ¢ JD
     * @return selection by file */
    private static Selection by(final IFile ¢) {
      return ¢ == null ? Selection.empty() : Selection.of(JavaCore.createCompilationUnitFrom(¢)).setName(¢.getName());
    }

    /** @param ¢ JD
     * @return selection by marker item */
    private static Selection by(final MarkerItem ¢) {
      return ¢ == null ? Selection.empty() : by(¢.getMarker()).setName(MARKER_NAME);
    }

    /** @param s JD
     * @return selection by tree selection */
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

    /** @param p JD
     * @return selection by java project */
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

    /** @param r JD
     * @return selection by package root */
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

    /** @param f JD
     * @return selection by package */
    private static Selection by(final IPackageFragment f) {
      try {
        return f == null ? Selection.empty()
            : Selection.of(f.getCompilationUnits()).setName("".equals(f.getElementName()) ? DEFAULT_PACKAGE_NAME : f.getElementName());
      } catch (final JavaModelException x) {
        monitor.log(x);
        return Selection.empty();
      }
    }

    /** @param m JD
     * @return selection by member */
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
     * @return text selection by marker */
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

    /** TODO Roth: do not create a new AST for this
     * @param u JD
     * @param m JD
     * @return node marked by maker */
    private static ASTNode getNodeByMarker(final ICompilationUnit u, final IMarker m) {
      try {
        final int s = ((Integer) m.getAttribute(IMarker.CHAR_START)).intValue();
        return new NodeFinder(Make.COMPILATION_UNIT.parser(u).createAST(new NullProgressMonitor()), s,
            ((Integer) m.getAttribute(IMarker.CHAR_END)).intValue() - s).getCoveredNode();
      } catch (final CoreException x) {
        monitor.logEvaluationError(x);
      }
      return null;
    }
  }
}
