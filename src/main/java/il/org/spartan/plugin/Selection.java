package il.org.spartan.plugin;

import static il.org.spartan.lisp.*;

import java.util.*;
import java.util.stream.*;

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
import il.org.spartan.spartanizer.engine.nominal.*;
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
    final List<WrappedCompilationUnit> $ = new ArrayList<>();
    if (¢ != null)
      $.add(WrappedCompilationUnit.of(¢));
    return new Selection($, null, getName(¢));
  }

  /** Factory method.
   * @param ¢ JD
   * @return selection by compilation unit and text selection */
  public static Selection of(final ICompilationUnit u, final ITextSelection s) {
    final List<WrappedCompilationUnit> $ = new ArrayList<>();
    if (u != null)
      $.add(WrappedCompilationUnit.of(u));
    return new Selection($, s, getName(u));
  }

  /** Factory method.
   * @param ¢ JD
   * @return selection by compilation units */
  public static Selection of(final ICompilationUnit[] ¢) {
    final List<ICompilationUnit> $ = Arrays.asList(¢);
    return new Selection(WrappedCompilationUnit.of($), null, getName($));
  }

  /** @param ¢ JD
   * @return name for selection, extracted from the compilation units */
  private static String getName(final List<ICompilationUnit> ¢) {
    // TODO: Yuval Simon study the use of lisp.getOnlyOne and apply here.
    return ¢ == null || ¢.isEmpty() ? null : ¢.size() == 1 ? first(¢).getElementName() : first(¢).getResource().getProject().getName();
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
    final IResource r = first(inner).descriptor.getResource();
    if (!(r instanceof IFile))
      return this;
    final int o = textSelection.getOffset(), l = o + textSelection.getLength();
    int no = o, nl = l;
    try {
      final IMarker[] ms = ((IFile) r).findMarkers(Builder.MARKER_TYPE, true, IResource.DEPTH_INFINITE);
      boolean changed = false;
      int i = 0;
      for (; i < ms.length; ++i) {
        if (ms[i] == null)
          continue;
        final Integer ics = (Integer) ms[i].getAttribute(IMarker.CHAR_START), ice = (Integer) ms[i].getAttribute(IMarker.CHAR_END);
        if (ics == null || ice == null)
          continue;
        final int cs = ics.intValue();
        if (cs <= o && ice.intValue() >= o) {
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

  // TODO Ori Roth: apply to newly added WCU as well
  public Selection setUseBinding() {
    if (inner != null)
      for (final WrappedCompilationUnit ¢ : inner)
        ¢.useBinding = true;
    return this;
  }

  @Override @SuppressWarnings("deprecation") public String toString() {
    if (isEmpty())
      return "{empty}";
    final int $ = inner == null ? 0 : inner.size();
    return "{" + (inner == null ? null : $ + " " + RefactorerUtil.plurals("file", $)) + ", "
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

    /** @return selection of current compilation unit */
    public static Selection getCurrentCompilationUnit() {
      final Selection $ = getCompilationUnit();
      return $ != null ? $ : empty();
    }

    /** @param ¢ JD
     * @return selection of current compilation unit by marker */
    public static Selection getCurrentCompilationUnit(final IMarker ¢) {
      if (!¢.exists())
        return empty();
      final IResource $ = ¢.getResource();
      return !($ instanceof IFile) ? empty() : by((IFile) $).setTextSelection(null);
    }

    /** @param ¢ JD
     * @return selection of all compilation units in project by marker */
    public static Selection getAllCompilationUnit(final IMarker ¢) {
      if (!¢.exists())
        return empty();
      final IResource $ = ¢.getResource();
      return $ == null ? empty() : by(getJavaProject($.getProject()));
    }

    public static Selection getAllCompilationUnits() {
      final IJavaProject $ = getJavaProject();
      return $ == null ? empty() : by($).setTextSelection(null).setName($.getElementName());
    }

    /** @return current user selection */
    public static Selection current() {
      final ISelection $ = getSelection();
      return $ == null ? empty()
          : $ instanceof ITextSelection ? by((ITextSelection) $) : $ instanceof ITreeSelection ? by((ITreeSelection) $) : empty();
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
        final IResource $ = m.getResource();
        return $ == null ? getProject() : $.getProject();
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
      final ITextSelection $ = getTextSelection(¢);
      return $ == null ? empty() : by(¢.getResource()).setTextSelection($).setName(MARKER_NAME);
    }

    public static Selection expand(final IMarker m, final Class<? extends ASTNode> c) {
      if (m == null || !m.exists() || c == null || m.getResource() == null || !(m.getResource() instanceof IFile))
        return empty();
      final ICompilationUnit u = JavaCore.createCompilationUnitFrom((IFile) m.getResource());
      if (u == null)
        return empty();
      final WrappedCompilationUnit $ = WrappedCompilationUnit.of(u);
      final ASTNode n = getNodeByMarker($, m);
      if (n == null)
        return empty();
      final ASTNode p = searchAncestors.forClass(c).from(n);
      return p == null ? empty() : TrackerSelection.empty().track(p).add($).setTextSelection(new TextSelection(p.getStartPosition(), p.getLength()));
    }

    /** @return current {@link ISelection} */
    private static ISelection getSelection() {
      final IWorkbench wb = PlatformUI.getWorkbench();
      if (wb == null)
        return null;
      final IWorkbenchWindow w = wb.getActiveWorkbenchWindow();
      if (w == null)
        return null;
      final ISelectionService $ = w.getSelectionService();
      return $ == null ? null : $.getSelection();
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
      final IResource $ = i.getAdapter(IResource.class);
      return $ == null ? null : $.getProject();
    }

    /** @return current Java project */
    private static IJavaProject getJavaProject() {
      final IProject $ = getProject();
      return $ == null ? null : JavaCore.create($);
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
      final IEditorInput $ = e.getEditorInput();
      return $ == null ? null : by($.getAdapter(IResource.class));
    }

    /** @param ¢ JD
     * @return selection by text selection */
    private static Selection by(final ITextSelection ¢) {
      final Selection $ = getCompilationUnit();
      return $ == null || $.inner == null || $.inner.isEmpty() ? null
          : (¢.getOffset() == 0 && ¢.getLength() == first($.inner).build().compilationUnit.getLength() ? $ : $.setTextSelection(¢).fixTextSelection())
              .setName(SELECTION_NAME).setIsTextSelection(true);
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
        final Object o = first(ss);
        return o == null ? empty()
            : o instanceof MarkerItem ? by((MarkerItem) o)
                : o instanceof IJavaProject ? by((IJavaProject) o)
                    : o instanceof IPackageFragmentRoot ? by((IPackageFragmentRoot) o)
                        : o instanceof IPackageFragment ? by((IPackageFragment) o)
                            : o instanceof ICompilationUnit ? Selection.of((ICompilationUnit) o)
                                : !(o instanceof IMember) ? empty() : by((IMember) o);
      }
      final Selection $ = Selection.empty();
      final List<MarkerItem> is = new LinkedList<>();
      final List<IJavaProject> ps = new LinkedList<>();
      final List<IPackageFragmentRoot> rs = new LinkedList<>();
      final List<IPackageFragment> hs = new LinkedList<>();
      final List<ICompilationUnit> cs = new LinkedList<>();
      final List<IMember> ms = new LinkedList<>();
      for (final Object ¢ : ss) {
        $.unify(¢ == null ? null
            : ¢ instanceof MarkerItem ? by((MarkerItem) ¢)
                : ¢ instanceof IJavaProject ? by((IJavaProject) ¢)
                    : ¢ instanceof IPackageFragmentRoot ? by((IPackageFragmentRoot) ¢)
                        : ¢ instanceof IPackageFragment ? by((IPackageFragment) ¢)
                            : ¢ instanceof ICompilationUnit ? Selection.of((ICompilationUnit) ¢) : ¢ instanceof IMember ? by((IMember) ¢) : null);
        if (¢ instanceof MarkerItem)
          is.add((MarkerItem) ¢);
        else if (¢ instanceof IJavaProject)
          ps.add((IJavaProject) ¢);
        else if (¢ instanceof IPackageFragmentRoot)
          rs.add((IPackageFragmentRoot) ¢);
        else if (¢ instanceof IPackageFragment)
          hs.add((IPackageFragment) ¢);
        else if (¢ instanceof ICompilationUnit)
          cs.add((ICompilationUnit) ¢);
        else if (¢ instanceof IMember)
          ms.add((IMember) ¢);
      }
      return $.setName(getMultiSelectionName(is, ps, rs, hs, cs, ms));
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
    private static Selection by(final IPackageFragment $) {
      try {
        return $ == null ? empty()
            : Selection.of($.getCompilationUnits()).setName(!"".equals($.getElementName()) ? $.getElementName() : DEFAULT_PACKAGE_NAME);
      } catch (final JavaModelException ¢) {
        monitor.log(¢);
        return empty();
      }
    }

    /** @param ¢ JD
     * @return selection by member */
    private static Selection by(final IMember ¢) {
      final ISourceRange $ = makertToRange(¢);
      return $ == null ? empty() : Selection.of(¢.getCompilationUnit(), new TextSelection($.getOffset(), $.getLength())).setName(¢.getElementName());
    }

    public static ISourceRange makertToRange(final IMember $) {
      try {
        return $.getSourceRange();
      } catch (final JavaModelException ¢) {
        monitor.log(¢);
        return null;
      }
    }

    /** @param m JD
     * @return text selection by marker */
    private static ITextSelection getTextSelection(final IMarker m) {
      try {
        final int $ = ((Integer) m.getAttribute(IMarker.CHAR_START)).intValue();
        return new TextSelection($, ((Integer) m.getAttribute(IMarker.CHAR_END)).intValue() - $);
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
        final int $ = ((Integer) m.getAttribute(IMarker.CHAR_START)).intValue();
        return new NodeFinder(u.build().compilationUnit, $, ((Integer) m.getAttribute(IMarker.CHAR_END)).intValue() - $).getCoveredNode();
      } catch (final CoreException ¢) {
        monitor.logEvaluationError(¢);
        return null;
      }
    }

    /** @param is list of markers in selection
     * @param ps list of projects in selection
     * @param rs list of root packages in selection
     * @param hs list of packages in selection
     * @param us list of files in selection
     * @param ms list of members in selection
     * @return name for the selection */
    private static String getMultiSelectionName(final List<MarkerItem> is, final List<IJavaProject> ps, final List<IPackageFragmentRoot> rs,
        final List<IPackageFragment> hs, final List<ICompilationUnit> us, final List<IMember> ms) {
      final List<String> $ = new LinkedList<>();
      ps.forEach(¢ -> $.add(¢.getElementName()));
      if (!rs.isEmpty())
        $.add(Linguistic.plurals("root package", rs.size()));
      if (!hs.isEmpty())
        $.add(Linguistic.plurals("package", hs.size()));
      if (!us.isEmpty())
        $.add(Linguistic.plurals("compilation unit", us.size()));
      if (!is.isEmpty())
        $.add(Linguistic.plurals("marker", is.size()));
      if (!ms.isEmpty())
        $.add(Linguistic.plurals("code object", ms.size()));
      return Linguistic.list($);
    }
  }
}
