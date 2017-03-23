package il.org.spartan.plugin;

import static java.util.stream.Collectors.*;

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
import org.jetbrains.annotations.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.utils.*;

/** Describes a selection, containing selected compilation unit(s) and text
 * selection
 * @author Ori Roth
 * @since 2.6 */
public class Selection extends AbstractSelection<Selection> {
  public Selection(@Nullable final List<WrappedCompilationUnit> compilationUnits, final ITextSelection textSelection, final String name) {
    inner = compilationUnits != null ? compilationUnits : new ArrayList<>();
    this.textSelection = textSelection;
    this.name = name;
  }

  @NotNull public Selection buildAll() {
    inner.forEach(WrappedCompilationUnit::build);
    return this;
  }

  public List<ICompilationUnit> getCompilationUnits() {
    return inner.stream().map(λ -> λ.descriptor).collect(toList());
  }

  /** Factory method.
   * @return empty selection */
  @Nullable public static Selection empty() {
    return new Selection(null, null, null);
  }

  /** Factory method.
   * @param ¢ JD
   * @return selection by compilation units */
  @NotNull public static Selection of(@NotNull final List<ICompilationUnit> ¢) {
    return new Selection(WrappedCompilationUnit.of(¢), null, getName(¢));
  }

  /** Factory method.
   * @param ¢ JD
   * @return selection by compilation unit */
  @Nullable public static Selection of(@Nullable final ICompilationUnit ¢) {
    @NotNull final List<WrappedCompilationUnit> $ = new ArrayList<>();
    if (¢ != null)
      $.add(WrappedCompilationUnit.of(¢));
    return new Selection($, null, getName(¢));
  }

  /** Factory method.
   * @param ¢ JD
   * @return selection by compilation unit and text selection */
  @NotNull public static Selection of(@Nullable final ICompilationUnit u, final ITextSelection s) {
    @NotNull final List<WrappedCompilationUnit> $ = new ArrayList<>();
    if (u != null)
      $.add(WrappedCompilationUnit.of(u));
    return new Selection($, s, getName(u));
  }

  /** Factory method.
   * @param ¢ JD
   * @return selection by compilation units */
  @NotNull public static Selection of(final ICompilationUnit[] ¢) {
    final List<ICompilationUnit> $ = as.list(¢);
    return new Selection(WrappedCompilationUnit.of($), null, getName($));
  }

  /** @param ¢ JD
   * @return name for selection, extracted from the compilation units */
  private static String getName(@Nullable final List<ICompilationUnit> ¢) {
    // TODO Yuval Simon study the use of lisp.getOnlyOne and apply here.
    return ¢ == null || ¢.isEmpty() ? null : ¢.size() == 1 ? first(¢).getElementName() : first(¢).getResource().getProject().getName();
  }

  /** @param ¢ JD
   * @return name for selection, extracted from the compilation unit */
  private static String getName(@Nullable final IJavaElement ¢) {
    return ¢ == null ? null : ¢.getElementName();
  }

  /** Extends text selection to include overlapping markers.
   * @return {@code this} selection */
  @NotNull public Selection fixTextSelection() {
    if (inner == null || inner.size() != 1 || textSelection == null)
      return this;
    final IResource r = first(inner).descriptor.getResource();
    if (!(r instanceof IFile))
      return this;
    final int o = textSelection.getOffset(), l = o + textSelection.getLength();
    try {
      final IMarker[] ms = r.findMarkers(Builder.MARKER_TYPE, true, IResource.DEPTH_INFINITE);
      boolean changed = false;
      int i = 0, no = o;
      for (; i < ms.length; ++i) {
        if (ms[i] == null)
          continue;
        @NotNull final Integer ics = (Integer) ms[i].getAttribute(IMarker.CHAR_START), ice = (Integer) ms[i].getAttribute(IMarker.CHAR_END);
        if (ics == null || ice == null)
          continue;
        final int cs = ics.intValue();
        if (cs <= o && ice.intValue() >= o) {
          no = cs;
          changed = true;
          break;
        }
      }
      int nl = l;
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
    } catch (@NotNull final CoreException ¢) {
      monitor.log(¢);
      return this;
    }
    return this;
  }

  // TODO Ori Roth: apply to newly added WCU as well
  @NotNull public Selection setUseBinding() {
    if (inner != null)
      for (@NotNull final WrappedCompilationUnit ¢ : inner) // NANO?
        ¢.useBinding = true;
    return this;
  }

  @Override @NotNull public String toString() {
    if (isEmpty())
      return "{empty}";
    final int $ = inner == null ? 0 : inner.size();
    return "{" + (inner == null ? null : $ + " " + English.plurals("file", $)) + ", " + (textSelection == null ? null : printable(textSelection))
        + "}";
  }

  /** @param ¢ JD
   * @return printable string describing the text selection */
  @NotNull private static String printable(@NotNull final ITextSelection ¢) {
    return "(" + ¢.getOffset() + "," + ¢.getLength() + ")";
  }

  public enum Util {
    ;
    /** Default name for marker selections. */
    private static final String MARKER_NAME = "marker";
    /** Default name for text selections. */
    private static final String SELECTION_NAME = "selection";
    /** Default name for default package selections. */
    private static final String DEFAULT_PACKAGE_NAME = "(default package)";

    /** @return selection of current compilation unit */
    @Nullable public static Selection getCurrentCompilationUnit() {
      @Nullable final Selection $ = getCompilationUnit();
      return $ != null ? $ : empty();
    }

    /** @param ¢ JD
     * @return selection of current compilation unit by marker */
    @Nullable public static Selection getCurrentCompilationUnit(@NotNull final IMarker ¢) {
      if (!¢.exists())
        return empty();
      final IResource $ = ¢.getResource();
      return !($ instanceof IFile) ? empty() : by((IFile) $).setTextSelection(null);
    }

    /** @param ¢ JD
     * @return selection of all compilation units in project by marker */
    @Nullable public static Selection getAllCompilationUnit(@NotNull final IMarker ¢) {
      if (!¢.exists())
        return empty();
      final IResource $ = ¢.getResource();
      return $ == null ? empty() : by(getJavaProject($.getProject()));
    }

    @Nullable public static Selection getAllCompilationUnits() {
      @NotNull final IJavaProject $ = getJavaProject();
      return $ == null ? empty() : by($).setTextSelection(null).setName($.getElementName());
    }

    /** @return current user selection */
    @Nullable public static Selection current() {
      @NotNull final ISelection $ = getSelection();
      return $ == null ? empty()
          : $ instanceof ITextSelection ? by((ITextSelection) $) : $ instanceof ITreeSelection ? by((IStructuredSelection) $) : empty();
    }

    /** @return current project */
    @Nullable public static IProject project() {
      @NotNull final ISelection s = getSelection();
      if (s == null || s instanceof ITextSelection || !(s instanceof ITreeSelection))
        return getProject();
      final Object $ = ((IStructuredSelection) s).getFirstElement();
      return ($ instanceof MarkerItem
          ? Optional.of((MarkerItem) $) //
              .map(λ -> λ.getMarker()).map(λ -> λ.getResource()).map(λ -> λ.getProject())
          : $ instanceof IJavaElement
              ? Optional.of((IJavaElement) $) //
                  .map(λ -> λ.getJavaProject()).map(λ -> λ.getProject()) //
              : Optional.<IProject> empty()).orElse(getProject());
    }

    /** @param ¢ JD
     * @return selection by marker */
    @Nullable public static Selection by(@Nullable final IMarker ¢) {
      if (¢ == null || !¢.exists())
        return empty();
      @Nullable final ITextSelection $ = getTextSelection(¢);
      return $ == null ? empty() : by(¢.getResource()).setTextSelection($).setName(MARKER_NAME);
    }

    @Nullable public static Selection expand(@Nullable final IMarker m, @Nullable final Class<? extends ASTNode> c) {
      if (m == null || !m.exists() || c == null || m.getResource() == null || !(m.getResource() instanceof IFile))
        return empty();
      final ICompilationUnit u = JavaCore.createCompilationUnitFrom((IFile) m.getResource());
      if (u == null)
        return empty();
      @NotNull final WrappedCompilationUnit $ = WrappedCompilationUnit.of(u);
      @Nullable final ASTNode n = getNodeByMarker($, m);
      if (n == null)
        return empty();
      @Nullable final ASTNode p = yieldAncestors.untilClass(c).from(n);
      return p == null ? empty() : TrackerSelection.empty().track(p).add($).setTextSelection(new TextSelection(p.getStartPosition(), p.getLength()));
    }

    /** @return current {@link ISelectionService} */
    static ISelectionService getSelectionService() {
      final IWorkbench wb = PlatformUI.getWorkbench();
      if (wb == null)
        return null;
      final IWorkbenchWindow $ = wb.getActiveWorkbenchWindow();
      return $ == null ? null : $.getSelectionService();
    }

    /** @return current {@link ISelection} */
    static ISelection getSelection() {
      @Nullable final ISelectionService $ = getSelectionService();
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
      @Nullable final IProject $ = getProject();
      return $ == null ? null : JavaCore.create($);
    }

    /** @param ¢ JD
     * @return java project */
    private static IJavaProject getJavaProject(@Nullable final IProject ¢) {
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
      final IWorkbenchPage $ = w.getActivePage();
      return $ == null ? null : $.getActiveEditor();
    }

    /** Depends on local editor.
     * @return selection by current compilation unit */
    private static Selection getCompilationUnit() {
      @Nullable final IEditorPart e = getEditorPart();
      if (e == null)
        return null;
      final IEditorInput $ = e.getEditorInput();
      return $ == null ? null : by($.getAdapter(IResource.class));
    }

    /** @param ¢ JD
     * @return selection by text selection */
    private static Selection by(@NotNull final ITextSelection ¢) {
      @Nullable final Selection $ = getCompilationUnit();
      return $ == null || $.inner == null || $.inner.isEmpty() ? null
          : (¢.getOffset() == 0 && ¢.getLength() == first($.inner).build().compilationUnit.getLength() ? $ : $.setTextSelection(¢).fixTextSelection())
              .setName(SELECTION_NAME).setIsTextSelection(true);
    }

    /** Only support selection by {@link IFile}.
     * @param ¢ JD
     * @return selection by file */
    @Nullable private static Selection by(final IResource ¢) {
      return !(¢ instanceof IFile) || !¢.getName().endsWith(".java") ? empty() : by((IFile) ¢);
    }

    /** @param ¢ JD
     * @return selection by file */
    @Nullable private static Selection by(@Nullable final IFile ¢) {
      return ¢ == null ? empty() : Selection.of(JavaCore.createCompilationUnitFrom(¢)).setName(¢.getName());
    }

    /** @param ¢ JD
     * @return selection by marker item */
    @Nullable private static Selection by(@Nullable final MarkerItem ¢) {
      return ¢ == null ? empty() : by(¢.getMarker()).setName(MARKER_NAME);
    }

    /** @param s JD
     * @return selection by tree selection */
    @Nullable private static Selection by(@NotNull final IStructuredSelection s) {
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
      @Nullable final Selection $ = Selection.empty();
      @NotNull final Collection<MarkerItem> is = new ArrayList<>();
      @NotNull final Collection<IJavaProject> ps = new ArrayList<>();
      @NotNull final Collection<IPackageFragmentRoot> rs = new ArrayList<>();
      @NotNull final Collection<IPackageFragment> hs = new ArrayList<>();
      @NotNull final Collection<ICompilationUnit> cs = new ArrayList<>();
      @NotNull final Collection<IMember> ms = new ArrayList<>();
      for (@Nullable final Object ¢ : ss) {
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
    @Nullable private static Selection by(@Nullable final IJavaProject p) {
      @Nullable final Selection $ = empty();
      if (p == null || !p.exists())
        return $;
      final IPackageFragmentRoot[] rs;
      try {
        rs = p.getPackageFragmentRoots();
      } catch (@NotNull final JavaModelException ¢) {
        monitor.log(¢);
        return empty();
      }
      as.list(rs).forEach(λ -> $.unify(by(λ)));
      return $.setName(p.getElementName());
    }

    /** @param r JD
     * @return selection by package root */
    @Nullable private static Selection by(@NotNull final IPackageFragmentRoot r) {
      @Nullable final Selection $ = empty();
      try {
        Stream.of(r.getChildren()).filter(λ -> λ.getElementType() == IJavaElement.PACKAGE_FRAGMENT).forEach(λ -> $.unify(by((IPackageFragment) λ)));
      } catch (@NotNull final JavaModelException ¢) {
        monitor.log(¢);
        return empty();
      }
      return $.setName(r.getElementName());
    }

    /** @param f JD
     * @return selection by package */
    @Nullable private static Selection by(@Nullable final IPackageFragment $) {
      try {
        return $ == null ? empty()
            : Selection.of($.getCompilationUnits())
                .setName($.getElementName() != null && !$.getElementName().isEmpty() ? $.getElementName() : DEFAULT_PACKAGE_NAME);
      } catch (@NotNull final JavaModelException ¢) {
        monitor.log(¢);
        return empty();
      }
    }

    /** @param ¢ JD
     * @return selection by member */
    @Nullable private static Selection by(@NotNull final IMember ¢) {
      @Nullable final ISourceRange $ = makertToRange(¢);
      return $ == null ? empty() : Selection.of(¢.getCompilationUnit(), new TextSelection($.getOffset(), $.getLength())).setName(¢.getElementName());
    }

    public static ISourceRange makertToRange(@NotNull final ISourceReference $) {
      try {
        return $.getSourceRange();
      } catch (@NotNull final JavaModelException ¢) {
        monitor.log(¢);
        return null;
      }
    }

    /** @param m JD
     * @return text selection by marker */
    private static ITextSelection getTextSelection(@NotNull final IMarker m) {
      try {
        final int $ = ((Integer) m.getAttribute(IMarker.CHAR_START)).intValue();
        return new TextSelection($, ((Integer) m.getAttribute(IMarker.CHAR_END)).intValue() - $);
      } catch (@NotNull final CoreException ¢) {
        monitor.log(¢);
        return null;
      }
    }

    /** @param u JD
     * @param m JD
     * @return node marked by marker */
    private static ASTNode getNodeByMarker(@NotNull final WrappedCompilationUnit u, @NotNull final IMarker m) {
      try {
        final int $ = ((Integer) m.getAttribute(IMarker.CHAR_START)).intValue();
        return new NodeFinder(u.build().compilationUnit, $, ((Integer) m.getAttribute(IMarker.CHAR_END)).intValue() - $).getCoveredNode();
      } catch (@NotNull final CoreException ¢) {
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
    @NotNull private static String getMultiSelectionName(@NotNull final Collection<MarkerItem> is, @NotNull final Iterable<IJavaProject> ps,
        @NotNull final Collection<IPackageFragmentRoot> rs, @NotNull final Collection<IPackageFragment> hs,
        @NotNull final Collection<ICompilationUnit> us, @NotNull final Collection<IMember> ms) {
      @NotNull final List<String> $ = new ArrayList<>();
      ps.forEach(λ -> $.add(λ.getElementName()));
      if (!rs.isEmpty())
        $.add(English.plurals("root package", rs.size()));
      if (!hs.isEmpty())
        $.add(English.plurals("package", hs.size()));
      if (!us.isEmpty())
        $.add(English.plurals("compilation unit", us.size()));
      if (!is.isEmpty())
        $.add(English.plurals("marker", is.size()));
      if (!ms.isEmpty())
        $.add(English.plurals("code object", ms.size()));
      return English.list($);
    }
  }
}
