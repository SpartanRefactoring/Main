package il.org.spartan.plugin;

import java.util.*;
import java.util.stream.Collectors;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jface.text.*;
import org.eclipse.jface.viewers.*;
import org.eclipse.ui.*;
import org.eclipse.ui.views.markers.*;

import il.org.spartan.plugin.old.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.utils.*;

/** Describes a selection, containing selected compilation unit(s) and text
 * selection
 * @author Ori Roth
 * @since 2.6 */
public class Selection extends AbstractSelection<Selection> {
  public Selection(final List<WrappedCompilationUnit> compilationUnits, final ITextSelection textSelection, final String name) {
    inner = compilationUnits != null ? compilationUnits : new ArrayList<>();
    this.textSelection = textSelection;
    this.name = name;
  }

  public Selection buildAll() {
    inner.forEach(WrappedCompilationUnit::build);
    return this;
  }

  public List<ICompilationUnit> getCompilationUnits() {
    return inner.stream().map(¢ -> ¢.descriptor).collect(Collectors.toList());
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
    return new Selection(WrappedCompilationUnit.of(¢), null, getName(¢));
  }

  /** Factory method.
   * @param ¢ JD
   * @return selection by compilation unit */
  public static Selection of(final ICompilationUnit ¢) {
    final List<WrappedCompilationUnit> us = new ArrayList<>();
    if (¢ != null)
      us.add(WrappedCompilationUnit.of(¢));
    return new Selection(us, null, getName(¢));
  }

  /** Factory method.
   * @param ¢ JD
   * @return selection by compilation unit and text selection */
  public static Selection of(final ICompilationUnit u, final ITextSelection s) {
    final List<WrappedCompilationUnit> us = new ArrayList<>();
    if (u != null)
      us.add(WrappedCompilationUnit.of(u));
    return new Selection(us, s, getName(u));
  }

  /** Factory method.
   * @param ¢ JD
   * @return selection by compilation units */
  public static Selection of(final ICompilationUnit[] ¢) {
    final List<ICompilationUnit> l = Arrays.asList(¢);
    return new Selection(WrappedCompilationUnit.of(l), null, getName(l));
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

  /** Extends text selection to include overlapping markers.
   * @return this selection */
  public Selection fixTextSelection() {
    if (inner == null || inner.size() != 1 || textSelection == null)
      return this;
    final WrappedCompilationUnit u = inner.get(0);
    final IResource r = u.descriptor.getResource();
    if (!(r instanceof IFile))
      return this;
    final int o = textSelection.getOffset();
    final int l = o + textSelection.getLength();
    int no = o, nl = l;
    try {
      final IMarker[] ms = ((IFile) r).findMarkers(Builder.MARKER_TYPE, true, IResource.DEPTH_INFINITE);
      boolean changed = false;
      int i = 0;
      for (; i < ms.length; ++i) {
        final int cs = ((Integer) ms[i].getAttribute(IMarker.CHAR_START)).intValue();
        if (cs <= o && ((Integer) ms[i].getAttribute(IMarker.CHAR_END)).intValue() >= o) {
          no = cs;
          changed = true;
          break;
        }
      }
      for (; i < ms.length; ++i) {
        final int ce = ((Integer) ms[i].getAttribute(IMarker.CHAR_END)).intValue();
        if (((Integer) ms[i].getAttribute(IMarker.CHAR_START)).intValue() <= l && ce >= l) {
          nl = ce;
          changed = true;
          break;
        }
      }
      if (changed)
        textSelection = new TextSelection(no, nl - no);
    } catch (final CoreException ¢) {
      monitor.log(¢);
      return this;
    }
    return this;
  }

  @Override public String toString() {
    if (isEmpty())
      return "{empty}";
    final int s = inner == null ? 0 : inner.size();
    return "{" + (inner == null ? null : s + " " + RefactorerUtil.plurals("file", s)) + ", "
        + (textSelection == null ? null : printable(textSelection)) + "}";
  }

  /** @param ¢ JD
   * @return printable string describing the text selection */
  private static String printable(final ITextSelection ¢) {
    return "(" + ¢.getOffset() + "," + ¢.getLength() + ")";
  }

  public static class Util {
    /** Default name for marker selections. */
    private static final String MARKER_NAME = "marker";
    /** Default name for text selections. */
    private static final String SELECTION_NAME = "selection";
    /** Default name for default package selections. */
    private static final String DEFAULT_PACKAGE_NAME = "(default package)";
    /** Default name for multi selection. */
    private static final String MULTI_SELECTION_NAME = "selections";

    /** @return selection of current compilation unit */
    public static Selection getCurrentCompilationUnit() {
      final Selection $ = getCompilationUnit();
      return $ != null ? $ : empty();
    }

    /** @param m JD
     * @return selection of current compilation unit by marker */
    public static Selection getCurrentCompilationUnit(final IMarker m) {
      if (!m.exists())
        return empty();
      final IResource r = m.getResource();
      return !(r instanceof IFile) ? empty() : by((IFile) r).setTextSelection(null);
    }

    /** @param m JD
     * @return selection of all compilation units in project by marker */
    public static Selection getAllCompilationUnit(final IMarker m) {
      if (!m.exists())
        return empty();
      final IResource r = m.getResource();
      return r == null ? empty() : by(getJavaProject(r.getProject()));
    }

    public static Selection getAllCompilationUnits() {
      final IJavaProject p = getJavaProject();
      return p == null ? empty() : by(p).setTextSelection(null).setName(p.getElementName());
    }

    /** @return current user selection */
    public static Selection current() {
      final ISelection s = getSelection();
      return s == null ? empty()
          : s instanceof ITextSelection ? by((ITextSelection) s) : s instanceof ITreeSelection ? by((ITreeSelection) s) : empty();
    }

    /** @return current project */
    public static IProject project() {
      final ISelection s = getSelection();
      if (s == null || s instanceof ITextSelection || !(s instanceof ITreeSelection))
        return getProject();
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
      if (!(o instanceof IJavaElement))
        return getProject();
      final IJavaProject p = ((IJavaElement) o).getJavaProject();
      return p == null ? getProject() : p.getProject();
    }

    /** @param ¢ JD
     * @return selection by marker */
    public static Selection by(final IMarker ¢) {
      if (¢ == null || !¢.exists())
        return empty();
      final ITextSelection s = getTextSelection(¢);
      return s == null ? empty() : by(¢.getResource()).setTextSelection(s).setName(MARKER_NAME);
    }

    public static Selection expand(final IMarker m, final Class<? extends ASTNode> c) {
      if (m == null || !m.exists() || c == null || m.getResource() == null || !(m.getResource() instanceof IFile))
        return empty();
      final ICompilationUnit u = JavaCore.createCompilationUnitFrom((IFile) m.getResource());
      if (u == null)
        return empty();
      final WrappedCompilationUnit cu = WrappedCompilationUnit.of(u);
      final ASTNode n = getNodeByMarker(cu, m);
      if (n == null)
        return empty();
      final ASTNode p = searchAncestors.forClass(c).from(n);
      return p == null ? empty() : TrackerSelection.empty().track(p).add(cu).setTextSelection(new TextSelection(p.getStartPosition(), p.getLength()));
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
      return r == null ? null : r.getProject();
    }

    /** @return current Java project */
    private static IJavaProject getJavaProject() {
      final IProject p = getProject();
      return p == null ? null : JavaCore.create(p);
    }

    /** @param ¢ JD
     * @return java project */
    private static IJavaProject getJavaProject(final IProject ¢) {
      return ¢ == null || !¢.exists() ? null : JavaCore.create(¢);
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

    // feature
    /** @param ¢ JD
     * @return selection by text selection */
    private static Selection by(final ITextSelection ¢) {
      final Selection $ = getCompilationUnit();
      return $ == null || $.inner == null || $.inner.isEmpty() ? null
          : (¢.getOffset() == 0 && ¢.getLength() == $.inner.get(0).build().compilationUnit.getLength() ? $ : $.setTextSelection(¢).fixTextSelection())
              .setName(SELECTION_NAME);
    }

    /** Only support selection by {@link IFile}.
     * @param ¢ JD
     * @return selection by file */
    private static Selection by(final IResource ¢) {
      return ¢ == null || !(¢ instanceof IFile) || !((IFile) ¢).getName().endsWith(".java") ? empty() : by((IFile) ¢);
    }

    /** @param ¢ JD
     * @return selection by file */
    private static Selection by(final IFile ¢) {
      return ¢ == null ? empty() : Selection.of(JavaCore.createCompilationUnitFrom(¢)).setName(¢.getName());
    }

    /** @param ¢ JD
     * @return selection by marker item */
    private static Selection by(final MarkerItem ¢) {
      return ¢ == null ? empty() : by(¢.getMarker()).setName(MARKER_NAME);
    }

    /** @param s JD
     * @return selection by tree selection */
    private static Selection by(final ITreeSelection s) {
      final List<?> ss = s.toList();
      if (ss.size() == 1) {
        final Object o = ss.get(0);
        return o == null ? empty()
            : o instanceof MarkerItem ? by((MarkerItem) o)
                : o instanceof IJavaProject ? by((IJavaProject) o)
                    : o instanceof IPackageFragmentRoot ? by((IPackageFragmentRoot) o)
                        : o instanceof IPackageFragment ? by((IPackageFragment) o)
                            : o instanceof ICompilationUnit ? Selection.of((ICompilationUnit) o)
                                : !(o instanceof IMember) ? empty() : by((IMember) o);
      }
      final Selection $ = Selection.empty();
      for (final Object ¢ : ss)
        $.unify(¢ == null ? null
            : ¢ instanceof MarkerItem ? by((MarkerItem) ¢)
                : ¢ instanceof IJavaProject ? by((IJavaProject) ¢)
                    : ¢ instanceof IPackageFragmentRoot ? by((IPackageFragmentRoot) ¢)
                        : ¢ instanceof IPackageFragment ? by((IPackageFragment) ¢)
                            : ¢ instanceof ICompilationUnit ? Selection.of((ICompilationUnit) ¢) : ¢ instanceof IMember ? by((IMember) ¢) : null);
      return $.setName(MULTI_SELECTION_NAME);
    }

    /** @param p JD
     * @return selection by java project */
    private static Selection by(final IJavaProject p) {
      final Selection $ = empty();
      if (p == null || !p.exists())
        return $;
      final IPackageFragmentRoot[] rs;
      try {
        rs = p.getPackageFragmentRoots();
      } catch (final JavaModelException ¢) {
        monitor.log(¢);
        return empty();
      }
      for (final IPackageFragmentRoot ¢ : rs)
        $.unify(by(¢));
      return $.setName(p.getElementName());
    }

    /** @param r JD
     * @return selection by package root */
    private static Selection by(final IPackageFragmentRoot r) {
      final Selection $ = empty();
      try {
        for (final IJavaElement ¢ : r.getChildren())
          if (¢.getElementType() == IJavaElement.PACKAGE_FRAGMENT)
            $.unify(by((IPackageFragment) ¢));
      } catch (final JavaModelException ¢) {
        monitor.log(¢);
        return empty();
      }
      return $.setName(r.getElementName());
    }

    /** @param f JD
     * @return selection by package */
    private static Selection by(final IPackageFragment f) {
      try {
        return f == null ? empty()
            : Selection.of(f.getCompilationUnits()).setName(!"".equals(f.getElementName()) ? f.getElementName() : DEFAULT_PACKAGE_NAME);
      } catch (final JavaModelException ¢) {
        monitor.log(¢);
        return empty();
      }
    }

    /** @param m JD
     * @return selection by member */
    private static Selection by(final IMember m) {
      final ISourceRange r = makertToRange(m);
      return r == null ? empty() : Selection.of(m.getCompilationUnit(), new TextSelection(r.getOffset(), r.getLength())).setName(m.getElementName());
    }

    public static ISourceRange makertToRange(final IMember m) {
      try {
        return m.getSourceRange();
      } catch (final JavaModelException ¢) {
        monitor.log(¢);
        return null;
      }
    }

    /** @param m JD
     * @return text selection by marker */
    private static ITextSelection getTextSelection(final IMarker m) {
      try {
        final int cs = ((Integer) m.getAttribute(IMarker.CHAR_START)).intValue();
        return new TextSelection(cs, ((Integer) m.getAttribute(IMarker.CHAR_END)).intValue() - cs);
      } catch (final CoreException ¢) {
        monitor.log(¢);
        return null;
      }
    }

    /** @param u JD
     * @param m JD
     * @return node marked by marker */
    private static ASTNode getNodeByMarker(final WrappedCompilationUnit u, final IMarker m) {
      try {
        final int s = ((Integer) m.getAttribute(IMarker.CHAR_START)).intValue();
        return new NodeFinder(u.build().compilationUnit, s, ((Integer) m.getAttribute(IMarker.CHAR_END)).intValue() - s).getCoveredNode();
      } catch (final CoreException ¢) {
        monitor.logEvaluationError(¢);
        return null;
      }
    }
  }
}
