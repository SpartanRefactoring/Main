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
    for (final WrappedCompilationUnit ¢ : inner)
      ¢.build();
    return this;
  }

  public List<ICompilationUnit> getCompilationUnits() {
    final List<ICompilationUnit> $ = new ArrayList<>();
    for (final WrappedCompilationUnit ¢ : inner)
      $.add(¢.descriptor);
    return $;
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
      int i = 0;
      boolean changed = false;
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
    } catch (final CoreException x) {
      monitor.log(x);
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
      return $ == null ? empty() : $;
    }

    /** @param m JD
     * @return selection of current compilation unit by marker */
    public static Selection getCurrentCompilationUnit(final IMarker m) {
      if (!m.exists())
        return empty();
      final IResource r = m.getResource();
      if (!(r instanceof IFile))
        return empty();
      return by((IFile) r).setTextSelection(null);
    }

    /** @param m JD
     * @return selection of all compilation units in project by marker */
    public static Selection getAllCompilationUnit(final IMarker m) {
      if (!m.exists())
        return empty();
      final IResource r = m.getResource();
      if (r == null)
        return empty();
      return by(getJavaProject(r.getProject()));
    }

    // TODO Roth: delete this ASAP
    public static Selection getAllCompilationUnits() {
      final ISelection s = getSelection();
      if (s == null)
        return empty();
      if (s instanceof ITextSelection) {
        final IJavaProject p = getJavaProject();
        return by(p).setTextSelection(null).setName(p.getElementName());
      }
      return empty();
    }

    /** @return current user selection */
    public static Selection current() {
      final ISelection s = getSelection();
      if (s == null)
        return empty();
      if (s instanceof ITextSelection)
        return by((ITextSelection) s);
      if (s instanceof ITreeSelection)
        return by((ITreeSelection) s);
      return empty();
    }

    /** @return current project */
    public static IProject project() {
      final ISelection s = getSelection();
      if (s == null || s instanceof ITextSelection)
        return getProject();
      if (!(s instanceof ITreeSelection))
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
      if (o instanceof IJavaElement) {
        final IJavaProject p = ((IJavaElement) o).getJavaProject();
        return p == null ? getProject() : p.getProject();
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
        return empty();
      return by(¢.getResource()).setTextSelection(s).setName(MARKER_NAME);
    }

    /** TODO Roth: what does expend mean? --yg
     * @param m
     * @param c
     * @return */
    public static Selection expend(final IMarker m, final Class<? extends ASTNode> c) {
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
      if (p == null)
        return empty();
      return TrackerSelection.empty().track(p).add(cu).setTextSelection(new TextSelection(p.getStartPosition(), p.getLength()));
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

    /** @return current Java project */
    private static IJavaProject getJavaProject() {
      final IProject p = getProject();
      return p == null ? null : JavaCore.create(p);
    }

    /** @param p JD
     * @return java project */
    private static IJavaProject getJavaProject(final IProject p) {
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

    // TODO Roth: decide whether to preserve the "full selection multi passes"
    // feature
    /** @param s JD
     * @return selection by text selection */
    private static Selection by(final ITextSelection s) {
      final Selection $ = getCompilationUnit();
      if ($ == null || $.inner == null || $.inner.isEmpty())
        return null;
      if (s.getOffset() == 0 && s.getLength() == $.inner.get(0).build().compilationUnit.getLength())
        return $.setName(SELECTION_NAME);
      return $.setTextSelection(s).fixTextSelection().setName(SELECTION_NAME);
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
      return empty();
    }

    /** @param p JD
     * @return selection by java project */
    private static Selection by(final IJavaProject p) {
      if (p == null)
        return empty();
      final Selection $ = empty();
      final IPackageFragmentRoot[] rs;
      try {
        rs = p.getPackageFragmentRoots();
      } catch (final JavaModelException x) {
        monitor.log(x);
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
      } catch (final JavaModelException x) {
        monitor.log(x);
        return empty();
      }
      return $.setName(r.getElementName());
    }

    /** @param f JD
     * @return selection by package */
    private static Selection by(final IPackageFragment f) {
      try {
        return f == null ? empty()
            : Selection.of(f.getCompilationUnits()).setName("".equals(f.getElementName()) ? DEFAULT_PACKAGE_NAME : f.getElementName());
      } catch (final JavaModelException x) {
        monitor.log(x);
        return empty();
      }
    }

    /** @param m JD
     * @return selection by member */
    private static Selection by(final IMember m) {
      final ISourceRange r = makertToRange(m);
      if (r == null)
        return empty();
      return Selection.of(m.getCompilationUnit(), new TextSelection(r.getOffset(), r.getLength())).setName(m.getElementName());
    }

    public static ISourceRange makertToRange(final IMember m) {
      try {
        return m.getSourceRange();
      } catch (final JavaModelException x) {
        monitor.log(x);
        return null;
      }
    }

    /** @param ¢ JD
     * @return text selection by marker */
    private static ITextSelection getTextSelection(final IMarker ¢) {
      try {
        int cs;
        cs = ((Integer) ¢.getAttribute(IMarker.CHAR_START)).intValue();
        return new TextSelection(cs, ((Integer) ¢.getAttribute(IMarker.CHAR_END)).intValue() - cs);
      } catch (final CoreException x) {
        monitor.log(x);
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
      } catch (final CoreException x) {
        monitor.logEvaluationError(x);
        return null;
      }
    }
  }
}
