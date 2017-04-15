package il.org.spartan.spartanizer.plugin;

import static il.org.spartan.plugin.old.eclipse.*;
import static il.org.spartan.utils.fault.*;

import static il.org.spartan.spartanizer.ast.navigate.wizard.*;

import java.util.*;
import java.util.List;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.jface.text.*;
import org.eclipse.ltk.core.refactoring.*;
import org.eclipse.ltk.ui.refactoring.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.*;

import il.org.spartan.plugin.old.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.trimming.*;
import il.org.spartan.utils.fluent.*;

/** base class for all GUI applicators contains common functionality. The class
 * combines this features: GUI, including extension of and the ability to
 * display {@link #name} and {@link #getProgressMonitor()}
 * @author Artium Nihamkin (original)
 * @author Boris van Sosin <boris.van.sosin [at] gmail.com>} (v2)
 * @author Yossi Gil: major refactoring 2013/07/10
 * @author Ori Roth: new plugin logic interfaces
 * @since 2013/01/01 */
public final class GUITraversal extends Refactoring implements Selfie<GUITraversal> {
  public boolean apply(final ICompilationUnit ¢) {
    return run(¢, new TextSelection(0, 0)) > 0;
  }

  public int apply(final WrappedCompilationUnit u) {
    final TextFileChange textChange = init(u);
    final CompilationUnit compilationUnit = u.build().compilationUnit;
    final ASTRewrite r = go(compilationUnit);
    try {
      if (textChange.getEdit().getLength() != 0)
        textChange.setEdit(r.rewriteAST());
      textChange.perform(getProgressMonitor());
    } catch (final CoreException ¢) {
      note.bug(this, ¢);
    } catch (final AssertionError x) {
      note.bug(dump() + //
          "\n x=" + x + //
          "\n $=" + traversal.rewriteCount.get() + //
          "\n u=" + u + //
          "\n u.name=" + u.name() + //
          "\n r=" + r + //
          "\n textchange=" + textChange + //
          "\n textchange.getEdit=" + textChange.getEdit() + //
          "\n textchange.getEdit.length=" + (textChange.getEdit() == null ? "??" : textChange.getEdit().getLength() + "") + //
          done(x));
    } finally {
      getProgressMonitor().done();
    }
    return traversal.rewriteCount.get();
  }

  public int apply(final WrappedCompilationUnit $, final AbstractSelection<?> s) {
    if (s != null && s.textSelection != null)
      setSelection(s.textSelection);
    if (s instanceof TrackerSelection)
      setSelection((TrackerSelection) s);
    return apply($);
  }

  @Override public RefactoringStatus checkFinalConditions(final IProgressMonitor pm) throws CoreException, OperationCanceledException {
    changes.clear();
    totalTips = 0;
    if (marker == null)
      collectAllTips();
    else {
      innerRunAsMarkerFix(marker, true);
      marker = null;
    }
    pm.done();
    return new RefactoringStatus();
  }

  @Override public RefactoringStatus checkInitialConditions(@SuppressWarnings("unused") final IProgressMonitor __) {
    final RefactoringStatus $ = new RefactoringStatus();
    if (iCompilationUnit == null && marker == null)
      $.merge(RefactoringStatus.createFatalErrorStatus("Nothing to do."));
    return $;
  }

  public Tips collectTips(final CompilationUnit ¢) {
    return traversal.collectTips(¢);
  }

  /** Count the number of tips offered by this instance.
   * <p>
   * This is a slow operation. Do not call light-headedly.
   * @return total number of tips offered by this instance */
  public int countTips() {
    setMarker(null);
    try {
      checkFinalConditions(getProgressMonitor());
    } catch (final OperationCanceledException ¢) {
      note.cancel(this, ¢);
    } catch (final CoreException ¢) {
      note.bug(this, ¢);
    }
    return totalTips;
  }

  @Override public Change createChange(final IProgressMonitor pm) throws OperationCanceledException {
    setProgressMonitor(pm);
    return new CompositeChange(getName(), changes.toArray(new Change[changes.size()]));
  }

  /** creates an ASTRewrite, under the context of a text marker, which contains
   * the changes
   * @param pm a progress monitor in which to display the progress of the
   *        refactoring
   * @param m the marker
   * @return an ASTRewrite which contains the changes */
  public ASTRewrite createRewrite(final IMarker ¢) {
    return computeRewrite(compileCompilationUnit(¢));
  }

  /** a quickfix which automatically performs the tip
   * @author Boris van Sosin <code><boris.van.sosin [at] gmail.com></code>
   * @since 2013/07/01
   * @return a quick fix for this instance */
  public IMarkerResolution getFix() {
    return new IMarkerResolution() {
      @Override public String getLabel() {
        return getName();
      }

      @Override public void run(final IMarker m) {
        try {
          runAsMarkerFix(m);
        } catch (final CoreException ¢) {
          note.bug(this, ¢);
        }
      }
    };
  }

  /** @return a quick fix with a preview for this instance. */
  public IMarkerResolution getFixWithPreview() {
    return getFixWithPreview(getName());
  }

  /** @return compilationUnit */
  public ICompilationUnit getiCompilationUnit() {
    return iCompilationUnit;
  }

  @Override public String getName() {
    return name;
  }

  public IProgressMonitor getProgressMonitor() {
    return progressMonitor;
  }

  public ITextSelection getSelection() {
    return selection;
  }

  public int go() throws CoreException {
    getProgressMonitor().beginTask("Creating change for " + compilationUnitName(), IProgressMonitor.UNKNOWN);
    final TextFileChange textChange = textFileChange();
    final IProgressMonitor m = eclipse.newSubMonitor(getProgressMonitor());
    final ASTParser parser = make.COMPILATION_UNIT.parser(iCompilationUnit);
    final CompilationUnit createAST = (CompilationUnit) parser.createAST(m);
    textChange.setEdit(go(createAST).rewriteAST());
    if (textChange.getEdit().getLength() != 0)
      textChange.perform(getProgressMonitor());
    getProgressMonitor().done();
    return traversal.rewriteCount.get();
  }

  /** @param iCompilationUnit the compilationUnit to set
   * @return */
  public GUITraversal iCompilationUnit(final ICompilationUnit ¢) {
    iCompilationUnit = ¢;
    return self();
  }

  /** Determines if the node is outside of the selected text.
   * @return whether the node is not inside selection. If there is no selection
   *         at all will return false. */
  public boolean isNotSelected(final ASTNode ¢) {
    return !isSelected(¢.getStartPosition());
  }

  public boolean isTextSelected() {
    return selection != null && !selection.isEmpty();
  }

  public int run(final ICompilationUnit $, final ITextSelection s) {
    try {
      iCompilationUnit($);
      setSelection(s);
      traversal.setRange(range(getSelection()));
      getProgressMonitor().beginTask("Creating change for a single compilation unit...", IProgressMonitor.UNKNOWN);
      final TextFileChange textChange = new TextFileChange($.getElementName(), (IFile) $.getResource());
      textChange.setTextType("java");
      final IProgressMonitor m = eclipse.newSubMonitor(getProgressMonitor());
      final ASTParser parser = make.COMPILATION_UNIT.parser($);
      final ASTNode createAST = parser.createAST(m);
      final CompilationUnit createAST2 = (CompilationUnit) createAST;
      final CompilationUnit ¢ = createAST2;
      final ASTRewrite createRewrite = go(¢);
      textChange.setEdit(createRewrite.rewriteAST());
      if (textChange.getEdit().getLength() != 0)
        textChange.perform(getProgressMonitor());
      getProgressMonitor().done();
    } catch (final CoreException ¢) {
      note.bug(this, ¢);
    }
    return traversal.rewriteCount.get();
  }

  /** @param pm a progress monitor in which to display the progress of the
   *        refactoring
   * @param m the marker for which the refactoring needs to system
   * @return a RefactoringStatus
   * @throws CoreException the JDT core throws it */
  public RefactoringStatus runAsMarkerFix(final IMarker ¢) throws CoreException {
    return innerRunAsMarkerFix(¢, false);
  }

  @Override public GUITraversal self() {
    return this;
  }

  public GUITraversal setMarker(final IMarker ¢) {
    traversal.setRange(range(¢));
    return self(() -> marker = ¢);
  }

  public GUITraversal setName(final String ¢) {
    return self(() -> name = ¢);
  }

  public GUITraversal setProgressMonitor(final IProgressMonitor ¢) {
    return self(() -> progressMonitor = ¢);
  }

  public void setSelection(final AbstractSelection<?> ¢) {
    setSelection(¢ == null || ¢.textSelection == null ? null : ¢.textSelection);
  }

  public GUITraversal setSelection(final ITextSelection ¢) {
    return self(() -> selection = ¢ != null && ¢.getLength() > 0 && !¢.isEmpty() ? ¢ : null);
  }

  public void setSelection(final TrackerSelection ¢) {
    setSelection(¢ == null || ¢.textSelection == null || ¢.textSelection.getLength() <= 0 || ¢.textSelection.isEmpty() ? null : ¢.textSelection);
  }

  @Override public String toString() {
    return getName();
  }

  private void collectAllTips() throws CoreException {
    getProgressMonitor().beginTask("Collecting tips...", IProgressMonitor.UNKNOWN);
    scanCompilationUnits(getUnits());
    getProgressMonitor().done();
  }

  private IFile compilationUnitIFile() {
    return (IFile) iCompilationUnit.getResource();
  }

  // TODO: Ori - can't we find Eclipse's version? --yg
  private CompilationUnit compileCompilationUnit(final IMarker ¢) {
    return az.compilationUnit(makeAST.COMPILATION_UNIT.from(¢, getProgressMonitor()));
  }

  private ASTRewrite computeRewrite(final CompilationUnit ¢) {
    note.logger.fine("Weaving maximal rewrite of " + ¢);
    getProgressMonitor().beginTask("Weaving maximal rewrite ...", IProgressMonitor.UNKNOWN);
    final ASTRewrite $ = traversal.go(¢);
    getProgressMonitor().done();
    return $;
  }

  /** @param s Text for the preview dialog
   * @return a quickfix which opens a refactoring wizard with the tipper */
  private IMarkerResolution getFixWithPreview(final String s) {
    return new IMarkerResolution() {
      /** a quickfix which opens a refactoring wizard with the tipper
       * @author Boris van Sosin <code><boris.van.sosin [at] gmail.com></code>
       *         (v2) */
      @Override public String getLabel() {
        return "Apply after preview";
      }

      @Override public void run(final IMarker m) {
        setMarker(m);
        try {
          new RefactoringWizardOpenOperation(new Wizard(GUITraversal.this)).run(Display.getCurrent().getActiveShell(),
              "Spartanization: " + s + GUITraversal.this);
        } catch (final InterruptedException ¢) {
          note.cancel(this, ¢);
        }
      }
    };
  }

  private Collection<ICompilationUnit> getUnits() throws JavaModelException {
    if (!isTextSelected())
      return compilationUnits(iCompilationUnit != null ? iCompilationUnit : currentCompilationUnit(), newSubMonitor(getProgressMonitor()));
    final List<ICompilationUnit> $ = new ArrayList<>();
    $.add(iCompilationUnit);
    return $;
  }

  private TextFileChange init(final WrappedCompilationUnit ¢) {
    iCompilationUnit(¢.descriptor);
    getProgressMonitor().beginTask("Creating change for compilation unit...", IProgressMonitor.UNKNOWN);
    final TextFileChange $ = new TextFileChange(¢.descriptor.getElementName(), (IFile) ¢.descriptor.getResource());
    $.setTextType("java");
    return $;
  }

  private RefactoringStatus innerRunAsMarkerFix(final IMarker m, final boolean preview) throws CoreException {
    marker = m;
    getProgressMonitor().beginTask("Running refactoring...", IProgressMonitor.UNKNOWN);
    scanCompilationUnitForMarkerFix(m, preview);
    marker = null;
    getProgressMonitor().done();
    return new RefactoringStatus();
  }

  private boolean isSelected(final int offset) {
    return isTextSelected() && offset >= selection.getOffset() && offset < selection.getLength() + selection.getOffset();
  }

  private ASTRewrite go(final CompilationUnit ¢) {
    note.logger.fine("Weaving maximal rewrite of " + ¢);
    getProgressMonitor().beginTask("Weaving maximal rewrite ...", IProgressMonitor.UNKNOWN);
    final ASTRewrite $ = traversal.go(¢);
    traversal.pop();
    getProgressMonitor().done();
    return $;
  }

  /** @param u JD
   * @throws CoreException */
  private int scanCompilationUnit(final ICompilationUnit u, final IProgressMonitor m) throws CoreException {
    m.beginTask("Collecting tips for " + u.getElementName(), IProgressMonitor.UNKNOWN);
    final TextFileChange textChange = new TextFileChange(u.getElementName(), (IFile) u.getResource());
    textChange.setTextType("java");
    final CompilationUnit cu = (CompilationUnit) make.COMPILATION_UNIT.parser(u).createAST(m);
    textChange.setEdit(go(cu).rewriteAST());
    if (textChange.getEdit().getLength() != 0)
      changes.add(textChange);
    totalTips += traversal.collectTips(cu).size();
    m.done();
    return traversal.rewriteCount.get();
  }

  private void scanCompilationUnitForMarkerFix(final IMarker m, final boolean preview) throws CoreException {
    getProgressMonitor().beginTask("Parsing of " + m, IProgressMonitor.UNKNOWN);
    final ICompilationUnit u = makeAST.iCompilationUnit(m);
    getProgressMonitor().done();
    final TextFileChange textChange = new TextFileChange(u.getElementName(), (IFile) u.getResource());
    textChange.setTextType("java");
    getProgressMonitor().beginTask("Collecting tips for " + m, IProgressMonitor.UNKNOWN);
    textChange.setEdit(createRewrite(m).rewriteAST());
    getProgressMonitor().done();
    if (textChange.getEdit().getLength() != 0)
      if (preview)
        changes.add(textChange);
      else {
        getProgressMonitor().beginTask("Applying tips", IProgressMonitor.UNKNOWN);
        textChange.perform(getProgressMonitor());
        getProgressMonitor().done();
      }
  }

  /** Creates a change from each compilation unit and stores it in the changes
   * list
   * @throws IllegalArgumentException
   * @throws CoreException */
  private void scanCompilationUnits(final Collection<ICompilationUnit> us) throws IllegalArgumentException, CoreException {
    getProgressMonitor().beginTask("Iterating over eligible compilation units...", us.size());
    for (final ICompilationUnit ¢ : us) // NANO - can't, throws...
      scanCompilationUnit(¢, eclipse.newSubMonitor(getProgressMonitor()));
    getProgressMonitor().done();
  }

  private TextFileChange textFileChange() {
    final TextFileChange $ = new TextFileChange(compilationUnitName(), compilationUnitIFile());
    $.setTextType("java");
    return $;
  }

  protected String compilationUnitName() {
    return iCompilationUnit.getElementName();
  }

  /** @param m marker which represents the range to apply the tipper within
   * @param n the node which needs to be within the range of {@code m}
   * @return whether the node is within range */
  public boolean inRange(final IMarker m, final ASTNode n) {
    return m != null ? !wizard.disjoint(n, m) : !isTextSelected() || !isNotSelected(n);
  }

  public final Traversal traversal = new Traversalmplementation().push((TrimmingTickingTapper) λ -> getProgressMonitor().worked(λ))
      .push(new TrimmingTapper() {
        @Override public void begin() {
          TrimmingTapper.super.begin();
        }

        @Override public void end() {
          TrimmingTapper.super.end();
        }
      });
  private final Collection<TextFileChange> changes = new ArrayList<>();
  private ICompilationUnit iCompilationUnit;
  private IMarker marker;
  private String name;
  private IProgressMonitor progressMonitor = op.nullProgressMonitor;
  private ITextSelection selection;
  private int totalTips;
}