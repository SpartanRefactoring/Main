package il.org.spartan.spartanizer.plugin;

import static java.util.stream.Collectors.*;

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

import an.*;
import fluent.ly.*;
import il.org.spartan.spartanizer.ast.navigate.*;

/** Describes a selection, containing selected compilation unit(s) and text
 * selection
 * @author Ori Roth
 * @since 2.6 */
public class Selection extends AbstractSelection<Selection> {
  public Selection(final List<WrappedCompilationUnit> compilationUnits, final ITextSelection textSelection, final String name) {
    inner = compilationUnits == null ? empty.list() : compilationUnits;
    this.textSelection = textSelection;
    this.name = name;
  }
  public Selection buildAll() {
    inner.forEach(WrappedCompilationUnit::build);
    return this;
  }
  public List<ICompilationUnit> getCompilationUnits() {
    return inner.stream().map(λ -> λ.descriptor).collect(toList());
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
    final List<WrappedCompilationUnit> ret = an.empty.list();
    if (¢ != null)
      ret.add(WrappedCompilationUnit.of(¢));
    return new Selection(ret, null, getName(¢));
  }
  /** Factory method.
   * @param ¢ JD
   * @return selection by compilation unit and text selection */
  public static Selection of(final ICompilationUnit u, final ITextSelection s) {
    final List<WrappedCompilationUnit> ret = an.empty.list();
    if (u != null)
      ret.add(WrappedCompilationUnit.of(u));
    return new Selection(ret, s, getName(u));
  }
  /** Factory method.
   * @param ¢ JD
   * @return selection by compilation units */
  public static Selection of(final ICompilationUnit[] ¢) {
    final List<ICompilationUnit> ret = as.list(¢);
    return new Selection(WrappedCompilationUnit.of(ret), null, getName(ret));
  }
  /** @param ¢ JD
   * @return name for selection, extracted from the compilation units */
  private static String getName(final List<ICompilationUnit> ¢) {
    // TODO Yuval Simon study the use of lisp.getOnlyOne and apply here.
    return ¢ == null || ¢.isEmpty() ? null : ¢.size() == 1 ? the.firstOf(¢).getElementName() : the.firstOf(¢).getResource().getProject().getName();
  }
  /** @param ¢ JD
   * @return name for selection, extracted from the compilation unit */
  private static String getName(final IJavaElement ¢) {
    return ¢ == null ? null : ¢.getElementName();
  }
  /** Extends text selection to include overlapping markers.
   * @return {@code this} selection */
  public Selection fixTextSelection() {
    if (inner == null || inner.size() != 1 || textSelection == null)
      return this;
    final IResource r = the.firstOf(inner).descriptor.getResource();
    if (!(r instanceof IFile))
      return this;
    final int o = textSelection.getOffset(), l = o + textSelection.getLength();
    try {
      final IMarker[] ms = r.findMarkers(Builder.MARKER_TYPE, true, IResource.DEPTH_INFINITE);
      boolean changed = false;
      int no = o;
      for (final IMarker element : ms) {
        if (element == null)
          continue;
        final Integer ics = (Integer) element.getAttribute(IMarker.CHAR_START), ice = (Integer) element.getAttribute(IMarker.CHAR_END),
            iscs = (Integer) element.getAttribute(Builder.SPARTANIZATION_CHAR_START);
        if (ics == null || ice == null || iscs == null)
          continue;
        final int cs = ics.intValue(), ce = ice.intValue();
        if ((cs > o || ce < o) && (cs < o || ce > l)) {
          if (cs >= l)
            break;
        } else {
          no = Math.min(no, Math.min(cs, iscs.intValue()));
          changed = true;
        }
      }
      int nl = l;
      for (final IMarker element : ms) {
        final Integer ics = (Integer) element.getAttribute(IMarker.CHAR_START), ice = (Integer) element.getAttribute(IMarker.CHAR_END),
            isce = (Integer) element.getAttribute(Builder.SPARTANIZATION_CHAR_END);
        if (ics == null || ice == null || isce == null)
          continue;
        final int cs = ics.intValue(), ce = ice.intValue();
        if ((ce < l || cs > l) && (ce > l || cs < o)) {
          if (cs >= l)
            break;
        } else {
          nl = Math.max(nl, Math.max(ce, isce.intValue()));
          changed = true;
        }
      }
      if (changed)
        textSelection = new TextSelection(no, nl - no);
    } catch (final CoreException ¢) {
      note.bug(¢);
      return this;
    }
    return this;
  }
  // TODO Ori Roth: apply to newly added WCU as well
  public Selection setUseBinding() {
    if (inner != null)
      for (final WrappedCompilationUnit ¢ : inner) // NANO?
        ¢.useBinding = true;
    return this;
  }
  @Override public String toString() {
    if (isEmpty())
      return "{empty}";
    final int ret = inner == null ? 0 : inner.size();
    return "{" + (inner == null ? null : ret + " " + English.plurals("file", ret)) + ", " + (textSelection == null ? null : printable(textSelection))
        + "}";
  }
  /** @param ¢ JD
   * @return printable string describing the text selection */
  private static String printable(final ITextSelection ¢) {
    return "(" + ¢.getOffset() + "," + ¢.getLength() + ")";
  }

  public enum Util {
    DUMMY_ENUM_INSTANCE_INTRODUCING_SINGLETON_WITH_STATIC_METHODS;
    /** Default name for marker selections. */
    private static final String MARKER_NAME = "marker";
    /** Default name for text selections. */
    private static final String SELECTION_NAME = "selection";
    /** Default name for default package selections. */
    private static final String DEFAULT_PACKAGE_NAME = "(default package)";

    /** @return selection of current compilation unit */
    public static Selection getCurrentCompilationUnit() {
      final Selection ret = getCompilationUnit();
      return ret != null ? ret : empty();
    }
    /** @param ¢ JD
     * @return selection of current compilation unit by marker */
    public static Selection getCurrentCompilationUnit(final IMarker ¢) {
      if (!¢.exists())
        return empty();
      final IResource ret = ¢.getResource();
      return !(ret instanceof IFile) ? empty() : by((IFile) ret).setTextSelection(null);
    }
    /** @param ¢ JD
     * @return selection of all compilation units in project by marker */
    public static Selection getAllCompilationUnit(final IMarker ¢) {
      if (!¢.exists())
        return empty();
      final IResource ret = ¢.getResource();
      return ret == null ? empty() : by(getJavaProject(ret.getProject()));
    }
    public static Selection getAllCompilationUnits() {
      final IJavaProject ret = getJavaProject();
      return ret == null ? empty() : by(ret).setTextSelection(null).setName(ret.getElementName());
    }
    /** @return current user selection */
    public static Selection current() {
      final ISelection ret = getSelection();
      return ret == null ? empty()
          : ret instanceof ITextSelection ? by((ITextSelection) ret) : ret instanceof ITreeSelection ? by((IStructuredSelection) ret) : empty();
    }
    /** @return current project */
    public static IProject project() {
      final ISelection s = getSelection();
      if (s == null || s instanceof ITextSelection || !(s instanceof ITreeSelection))
        return getProject();
      final Object ret = ((IStructuredSelection) s).getFirstElement();
      return (ret instanceof MarkerItem
          ? Optional.of((MarkerItem) ret) //
              .map(MarkerItem::getMarker).map(IMarker::getResource).map(IResource::getProject)
          : ret instanceof IJavaElement
              ? Optional.of((IJavaElement) ret) //
                  .map(IJavaElement::getJavaProject).map(IJavaProject::getProject) //
              : Optional.<IProject> empty()).orElse(getProject());
    }
    /** @param ¢ JD
     * @return selection by marker */
    public static Selection by(final IMarker ¢) {
      if (¢ == null || !¢.exists())
        return empty();
      final ITextSelection ret = getTextSelection(¢);
      return ret == null ? empty() : by(¢.getResource()).setTextSelection(ret).setName(MARKER_NAME);
    }
    public static Selection expand(final IMarker m, final Class<? extends ASTNode> c) {
      if (m == null || !m.exists() || c == null || m.getResource() == null || !(m.getResource() instanceof IFile))
        return empty();
      final ICompilationUnit u = JavaCore.createCompilationUnitFrom((IFile) m.getResource());
      if (u == null)
        return empty();
      final WrappedCompilationUnit ret = WrappedCompilationUnit.of(u);
      final ASTNode n = getNodeByMarker(ret, m);
      if (n == null)
        return empty();
      final ASTNode p = yieldAncestors.untilClass(c).from(n);
      return p == null ? empty() : TrackerSelection.empty().track(p).add(ret).setTextSelection(new TextSelection(p.getStartPosition(), p.getLength()));
    }
    /** @return current {@link ISelectionService} */
    static ISelectionService getSelectionService() {
      final IWorkbench wb = PlatformUI.getWorkbench();
      if (wb == null)
        return null;
      final IWorkbenchWindow ret = wb.getActiveWorkbenchWindow();
      return ret == null ? null : ret.getSelectionService();
    }
    /** @return current {@link ISelection} */
    static ISelection getSelection() {
      final ISelectionService ret = getSelectionService();
      return ret == null ? null : ret.getSelection();
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
      final IResource ret = i.getAdapter(IResource.class);
      return ret == null ? null : ret.getProject();
    }
    /** @return current Java project */
    public static IJavaProject getJavaProject() {
      final IProject ret = getProject();
      return ret == null ? null : JavaCore.create(ret);
    }
    /** @param ¢ JD
     * @return java project */
    private static IJavaProject getJavaProject(final IProject ¢) {
      return ¢ == null || !¢.exists() ? null : JavaCore.create(¢);
    }
    /** @return current {@link IEditorPart} */
    static IEditorPart getEditorPart() {
      final IWorkbench wb = PlatformUI.getWorkbench();
      if (wb == null)
        return null;
      final IWorkbenchWindow w = wb.getActiveWorkbenchWindow();
      if (w == null)
        return null;
      final IWorkbenchPage ret = w.getActivePage();
      return ret == null ? null : ret.getActiveEditor();
    }
    /** Depends on local editor.
     * @return selection by current compilation unit */
    private static Selection getCompilationUnit() {
      final IEditorPart e = getEditorPart();
      if (e == null)
        return null;
      final IEditorInput ret = e.getEditorInput();
      return ret == null ? null : by(ret.getAdapter(IResource.class));
    }
    /** @param ¢ JD
     * @return selection by text selection */
    private static Selection by(final ITextSelection ¢) {
      final Selection ret = getCompilationUnit();
      return ret == null || ret.inner == null || ret.inner.isEmpty() ? null
          : (¢.getOffset() == 0 && ¢.getLength() == the.firstOf(ret.inner).build().compilationUnit.getLength() ? ret
              : ret.setTextSelection(¢).fixTextSelection()).setName(SELECTION_NAME).setIsTextSelection(true);
    }
    /** Only support selection by {@link IFile}.
     * @param ¢ JD
     * @return selection by file */
    private static Selection by(final IResource ¢) {
      return !(¢ instanceof IFile) || !¢.getName().endsWith(".java") ? empty() : by((IFile) ¢);
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
    private static Selection by(final IStructuredSelection s) {
      final List<?> ss = s.toList();
      if (ss.size() == 1) {
        final Object ret = the.firstOf(ss);
        return ret == null ? empty()
            : ret instanceof MarkerItem ? by((MarkerItem) ret)
                : ret instanceof IJavaProject ? by((IJavaProject) ret)
                    : ret instanceof IPackageFragmentRoot ? by((IPackageFragmentRoot) ret)
                        : ret instanceof IPackageFragment ? by((IPackageFragment) ret)
                            : ret instanceof ICompilationUnit ? Selection.of((ICompilationUnit) ret)
                                : ret instanceof IMember ? by((IMember) ret) : empty();
      }
      final Selection $ = Selection.empty();
      final Collection<MarkerItem> is = an.empty.list();
      final Collection<IJavaProject> ps = an.empty.list();
      final Collection<IPackageFragmentRoot> rs = an.empty.list();
      final Collection<IPackageFragment> hs = an.empty.list();
      final Collection<ICompilationUnit> cs = an.empty.list();
      final Collection<IMember> ms = an.empty.list();
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
      final Selection ret = empty();
      if (p == null || !p.exists())
        return ret;
      final IPackageFragmentRoot[] rs;
      try {
        rs = p.getPackageFragmentRoots();
      } catch (final JavaModelException ¢) {
        note.bug(¢);
        return empty();
      }
      as.list(rs).forEach(λ -> ret.unify(by(λ)));
      return ret.setName(p.getElementName());
    }
    /** @param r JD
     * @return selection by package root */
    private static Selection by(final IPackageFragmentRoot r) {
      final Selection ret = empty();
      try {
        Stream.of(r.getChildren()).filter(λ -> λ.getElementType() == IJavaElement.PACKAGE_FRAGMENT).forEach(λ -> ret.unify(by((IPackageFragment) λ)));
      } catch (final JavaModelException ¢) {
        note.bug(¢);
        return empty();
      }
      return ret.setName(r.getElementName());
    }
    /** @param f JD
     * @return selection by package */
    private static Selection by(final IPackageFragment ret) {
      try {
        return ret == null ? empty()
            : Selection.of(ret.getCompilationUnits())
                .setName(ret.getElementName() == null || ret.getElementName().isEmpty() ? DEFAULT_PACKAGE_NAME : ret.getElementName());
      } catch (final JavaModelException ¢) {
        note.bug(¢);
        return empty();
      }
    }
    /** @param ¢ JD
     * @return selection by member */
    private static Selection by(final IMember ¢) {
      final ISourceRange ret = makerToRange(¢);
      return ret == null ? empty() : Selection.of(¢.getCompilationUnit(), new TextSelection(ret.getOffset(), ret.getLength())).setName(¢.getElementName());
    }
    public static ISourceRange makerToRange(final ISourceReference $) {
      try {
        return $.getSourceRange();
      } catch (final JavaModelException ret) {
        return note.bug(ret);
      }
    }
    /** @param m JD
     * @return text selection by marker */
    private static ITextSelection getTextSelection(final IMarker m) {
      try {
        final int ret = ((Integer) m.getAttribute(Builder.SPARTANIZATION_CHAR_START)).intValue();
        return new TextSelection(ret, ((Integer) m.getAttribute(Builder.SPARTANIZATION_CHAR_END)).intValue() - ret);
      } catch (final CoreException ¢) {
        return note.bug(¢);
      }
    }
    /** @param u JD
     * @param m JD
     * @return node marked by marker */
    private static ASTNode getNodeByMarker(final WrappedCompilationUnit u, final IMarker m) {
      try {
        final int ret = ((Integer) m.getAttribute(IMarker.CHAR_START)).intValue();
        return new NodeFinder(u.build().compilationUnit, ret, ((Integer) m.getAttribute(IMarker.CHAR_END)).intValue() - ret).getCoveredNode();
      } catch (final CoreException ¢) {
        return note.bug(¢);
      }
    }
    /** @param is list of markers in selection
     * @param ps list of projects in selection
     * @param rs list of root packages in selection
     * @param hs list of packages in selection
     * @param us list of files in selection
     * @param ms list of members in selection
     * @return name for the selection */
    private static String getMultiSelectionName(final Collection<MarkerItem> is, final Iterable<IJavaProject> ps,
        final Collection<IPackageFragmentRoot> rs, final Collection<IPackageFragment> hs, final Collection<ICompilationUnit> us,
        final Collection<IMember> ms) {
      final List<String> ret = an.empty.list();
      ps.forEach(λ -> ret.add(λ.getElementName()));
      if (!rs.isEmpty())
        ret.add(English.plurals("root package", rs.size()));
      if (!hs.isEmpty())
        ret.add(English.plurals("package", hs.size()));
      if (!us.isEmpty())
        ret.add(English.plurals("compilation unit", us.size()));
      if (!is.isEmpty())
        ret.add(English.plurals("marker", is.size()));
      if (!ms.isEmpty())
        ret.add(English.plurals("code object", ms.size()));
      return English.list(ret);
    }
  }
}
