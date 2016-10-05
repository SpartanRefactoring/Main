package il.org.spartan.plugin;

import static il.org.spartan.plugin.eclipse.*;

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
import org.eclipse.text.edits.*;
import org.eclipse.ui.*;

import static il.org.spartan.spartanizer.ast.navigate.wizard.*;

import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.utils.*;

/** the base class for all GUI applicators contains common functionality
 * @author Artium Nihamkin (original)
 * @author Boris van Sosin <boris.van.sosin [at] gmail.com>} (v2)
 * @author Yossi Gil <code><yossi.gil [at] gmail.com></code>: major refactoring
 *         2013/07/10
 * @since 2013/01/01 */
public abstract class GUI$Applicator extends Refactoring {
  public IProgressMonitor progressMonitor = nullProgressMonitor;
  final Collection<TextFileChange> changes = new ArrayList<>();
  private CompilationUnit compilationUnit;
  private ICompilationUnit iCompilationUnit;
  private IMarker marker;
  private final String name;
  private ITextSelection selection;
  final List<Tip> tips = new ArrayList<>();
  private int totalChanges;

  /*** Instantiates this class, with message identical to name
   * @param name a short name of this instance */
  protected GUI$Applicator(final String name) {
    this.name = name;
  }

  public boolean apply(final ICompilationUnit cu) {
    return apply(cu, new Range(0, 0));
  }

  public boolean apply(final ICompilationUnit cu, final Range r) {
    return fuzzyImplementationApply(cu, r == null || r.isEmpty() ? new TextSelection(0, 0) : new TextSelection(r.from, r.size()));
  }

  @Override public RefactoringStatus checkFinalConditions(final IProgressMonitor pm) throws CoreException, OperationCanceledException {
    changes.clear();
    totalChanges = 0;
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

  /** Checks a Compilation Unit (outermost ASTNode in the Java Grammar) for
   * tipper tips
   * @param u what to check
   * @return a collection of {@link Tip} objects each containing a laconic
   *         tip */
  public final List<Tip> collectSuggesions(final CompilationUnit ¢) {
    final List<Tip> $ = new ArrayList<>();
    ¢.accept(makeTipsCollector($));
    return $;
  }

  public IFile compilatinUnitIFile() {
    return (IFile) iCompilationUnit.getResource();
  }

  public String compilationUnitName() {
    return iCompilationUnit.getElementName();
  }

  /** Count the number of tips offered by this instance.
   * <p>
   * This is a slow operation. Do not call light-headedly.
   * @return total number of tips offered by this instance */
  public int countTips() {
    setMarker(null);
    try {
      checkFinalConditions(progressMonitor);
    } catch (final OperationCanceledException e) {
      monitor.logCancellationRequest(this, e);
    } catch (final CoreException x) {
      monitor.logEvaluationError(this, x);
    }
    return totalChanges;
  }

  @Override public final Change createChange(final IProgressMonitor pm) throws OperationCanceledException {
    progressMonitor = pm;
    return new CompositeChange(getName(), changes.toArray(new Change[changes.size()]));
  }

  /** creates an ASTRewrite which contains the changes
   * @param u the Compilation Unit (outermost ASTNode in the Java Grammar)
   * @param m a progress monitor in which the progress of the refactoring is
   *        displayed
   * @return an ASTRewrite which contains the changes */
  public final ASTRewrite createRewrite(final CompilationUnit ¢) {
    return rewriterOf(¢, (IMarker) null);
  }

  public boolean follow() throws CoreException {
    progressMonitor.beginTask("Preparing the change ...", IProgressMonitor.UNKNOWN);
    final ASTRewrite astRewrite = ASTRewrite.create(compilationUnit.getAST());
    final TextEditGroup g = new TextEditGroup("spartanization: textEditGroup");
    for (final Tip ¢ : tips) {
      progressMonitor.worked(1);
      ¢.go(astRewrite, g);
    }
    progressMonitor.done();
    final TextEdit rewriteAST = astRewrite.rewriteAST();
    final TextFileChange textFileChange = new TextFileChange(compilationUnitName(), compilatinUnitIFile());
    textFileChange.setTextType("java");
    textFileChange.setEdit(rewriteAST);
    final boolean $ = textFileChange.getEdit().getLength() != 0;
    if ($)
      textFileChange.perform(progressMonitor);
    progressMonitor.done();
    return $;
  }

  public boolean fuzzyImplementationApply(final ICompilationUnit cu, final ITextSelection s) {
    try {
      setICompilationUnit(cu);
      setSelection(s.getLength() > 0 && !s.isEmpty() ? s : null);
      return performRule(cu);
    } catch (final CoreException x) {
      monitor.logEvaluationError(this, x);
    }
    return false;
  }

  /** a quickfix which automatically performs the tip
   * @author Boris van Sosin <code><boris.van.sosin [at] gmail.com></code>
   * @since 2013/07/01 */
  /** @return a quick fix for this instance */
  public IMarkerResolution getFix() {
    return new IMarkerResolution() {
      @Override public String getLabel() {
        return getName();
      }

      @Override public void run(final IMarker m) {
        try {
          runAsMarkerFix(m);
        } catch (final CoreException x) {
          monitor.logEvaluationError(this, x);
        }
      }
    };
  }

  /** @return a quick fix with a preview for this instance. */
  public IMarkerResolution getFixWithPreview() {
    return getFixWithPreview(getName());
  }

  /** @param s Text for the preview dialog
   * @return a quickfix which opens a refactoring wizard with the tipper */
  public IMarkerResolution getFixWithPreview(final String s) {
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
          new RefactoringWizardOpenOperation(new Wizard(GUI$Applicator.this)).run(Display.getCurrent().getActiveShell(),
              "Laconization: " + s + GUI$Applicator.this);
        } catch (final InterruptedException e) {
          monitor.logCancellationRequest(this, e);
        }
      }
    };
  }

  /** @return compilationUnit */
  public ICompilationUnit getiCompilationUnit() {
    return iCompilationUnit;
  }

  @Override public final String getName() {
    return name;
  }

  public IProgressMonitor getProgressMonitor() {
    return progressMonitor;
  }

  /** @return selection */
  public ITextSelection getSelection() {
    return selection;
  }

  public List<Tip> getTips() {
    return tips;
  }

  public boolean go() throws CoreException {
    progressMonitor.beginTask("Creating change for a single compilation unit...", IProgressMonitor.UNKNOWN);
    final TextFileChange textChange = new TextFileChange(compilationUnitName(), compilatinUnitIFile());
    textChange.setTextType("java");
    final IProgressMonitor m = newSubMonitor(progressMonitor);
    textChange.setEdit(createRewrite((CompilationUnit) Make.COMPILATION_UNIT.parser(iCompilationUnit).createAST(m)).rewriteAST());
    final boolean $ = textChange.getEdit().getLength() != 0;
    if ($)
      textChange.perform(progressMonitor);
    progressMonitor.done();
    return $;
  }

  /** .
   * @return True if there are tipss which can be performed on the compilation
   *         unit. */
  public final boolean haveTips() {
    return countTips() > 0;
  }

  /** @param m marker which represents the range to apply the tipper within
   * @param n the node which needs to be within the range of
   *        <code><b>m</b></code>
   * @return True if the node is within range */
  public final boolean inRange(final IMarker m, final ASTNode n) {
    return m != null ? !eclipse.facade.isNodeOutsideMarker(n, m) : !isTextSelected() || !isNodeOutsideSelection(n);
  }

  /** Performs the current tipper on the provided compilation unit
   * @param u the compilation to Spartanize
   * @param pm progress monitor for long operations (could be
   *        {@link NullProgressMonitor} for light operations)
   * @throws CoreException exception from the <code>pm</code> */
  public boolean performRule(final ICompilationUnit u) throws CoreException {
    progressMonitor.beginTask("Creating change for a single compilation unit...", IProgressMonitor.UNKNOWN);
    final TextFileChange textChange = new TextFileChange(u.getElementName(), (IFile) u.getResource());
    textChange.setTextType("java");
    final IProgressMonitor m = newSubMonitor(progressMonitor);
    textChange.setEdit(createRewrite((CompilationUnit) Make.COMPILATION_UNIT.parser(u).createAST(m)).rewriteAST());
    final boolean $ = textChange.getEdit().getLength() != 0;
    if ($)
      textChange.perform(progressMonitor);
    progressMonitor.done();
    return $;
  }

  public ASTRewrite rewriterOf(final CompilationUnit u, final IMarker m) {
    progressMonitor.beginTask("Creating rewrite operation...", IProgressMonitor.UNKNOWN);
    final ASTRewrite $ = ASTRewrite.create(u.getAST());
    consolidateTips($, u, m);
    progressMonitor.done();
    return $;
  }

  /** @param pm a progress monitor in which to display the progress of the
   *        refactoring
   * @param m the marker for which the refactoring needs to system
   * @return a RefactoringStatus
   * @throws CoreException the JDT core throws it */
  public RefactoringStatus runAsMarkerFix(final IMarker ¢) throws CoreException {
    return innerRunAsMarkerFix(¢, false);
  }

  /** @param iCompilationUnit the compilationUnit to set */
  public void setICompilationUnit(final ICompilationUnit ¢) {
    iCompilationUnit = ¢;
  }

  /** @param marker the marker to set for the refactoring */
  public final void setMarker(final IMarker ¢) {
    marker = ¢;
  }

  public void setProgressMonitor(final IProgressMonitor ¢) {
    progressMonitor = ¢;
  }

  /** @param subject the selection to set */
  public void setSelection(final ITextSelection ¢) {
    selection = ¢;
  }

  public int TipsCount() {
    return tips.size();
  }

  @Override public String toString() {
    return name;
  }

  protected abstract void consolidateTips(ASTRewrite r, CompilationUnit u, IMarker m);

  /** Determines if the node is outside of the selected text.
   * @return true if the node is not inside selection. If there is no selection
   *         at all will return false.
   * @DisableSpartan */
  protected boolean isNodeOutsideSelection(final ASTNode ¢) {
    return !isSelected(¢.getStartPosition());
  }

  protected abstract ASTVisitor makeTipsCollector(final List<Tip> $);

  protected void parse() {
    compilationUnit = (CompilationUnit) Make.COMPILATION_UNIT.parser(iCompilationUnit).createAST(progressMonitor);
  }

  protected void scan() {
    tips.clear();
    compilationUnit.accept(makeTipsCollector(tips));
  }

  /** @param u JD
   * @throws CoreException */
  protected void scanCompilationUnit(final ICompilationUnit u, final IProgressMonitor m) throws CoreException {
    m.beginTask("Collecting tips for " + u.getElementName(), IProgressMonitor.UNKNOWN);
    final TextFileChange textChange = new TextFileChange(u.getElementName(), (IFile) u.getResource());
    textChange.setTextType("java");
    final CompilationUnit cu = (CompilationUnit) Make.COMPILATION_UNIT.parser(u).createAST(m);
    textChange.setEdit(createRewrite(cu).rewriteAST());
    if (textChange.getEdit().getLength() != 0)
      changes.add(textChange);
    totalChanges += collectSuggesions(cu).size();
    m.done();
  }

  protected void scanCompilationUnitForMarkerFix(final IMarker m, final boolean preview) throws CoreException {
    progressMonitor.beginTask("Parsing of " + m, IProgressMonitor.UNKNOWN);
    final ICompilationUnit u = makeAST.iCompilationUnit(m);
    progressMonitor.done();
    final TextFileChange textChange = new TextFileChange(u.getElementName(), (IFile) u.getResource());
    textChange.setTextType("java");
    progressMonitor.beginTask("Collecting tips for " + m, IProgressMonitor.UNKNOWN);
    textChange.setEdit(createRewrite(m).rewriteAST());
    progressMonitor.done();
    if (textChange.getEdit().getLength() != 0)
      if (preview)
        changes.add(textChange);
      else {
        progressMonitor.beginTask("Applying tips", IProgressMonitor.UNKNOWN);
        textChange.perform(progressMonitor);
        progressMonitor.done();
      }
  }

  /** Creates a change from each compilation unit and stores it in the changes
   * list
   * @throws IllegalArgumentException
   * @throws CoreException */
  protected void scanCompilationUnits(final List<ICompilationUnit> us) throws IllegalArgumentException, CoreException {
    progressMonitor.beginTask("Iterating over laconizeable compilation units...", us.size());
    for (final ICompilationUnit ¢ : us)
      scanCompilationUnit(¢, newSubMonitor(progressMonitor));
    progressMonitor.done();
  }

  boolean apply() {
    return apply(iCompilationUnit, new Range(0, 0));
  }

  void collectAllTips() throws JavaModelException, CoreException {
    progressMonitor.beginTask("Collecting tips...", IProgressMonitor.UNKNOWN);
    scanCompilationUnits(getUnits());
    progressMonitor.done();
  }

  void collectTips() {
    progressMonitor.beginTask("Collecting tips...", IProgressMonitor.UNKNOWN);
    scan();
    progressMonitor.done();
  }

  /** creates an ASTRewrite, under the context of a text marker, which contains
   * the changes
   * @param pm a progress monitor in which to display the progress of the
   *        refactoring
   * @param m the marker
   * @return an ASTRewrite which contains the changes */
  private ASTRewrite createRewrite(final IMarker ¢) {
    return rewriterOf((CompilationUnit) makeAST.COMPILATION_UNIT.from(¢, progressMonitor), ¢);
  }

  private List<ICompilationUnit> getUnits() throws JavaModelException {
    if (!isTextSelected())
      return compilationUnits(iCompilationUnit != null ? iCompilationUnit : currentCompilationUnit(), newSubMonitor(progressMonitor));
    final List<ICompilationUnit> $ = new ArrayList<>();
    $.add(iCompilationUnit);
    return $;
  }

  private RefactoringStatus innerRunAsMarkerFix(final IMarker m, final boolean preview) throws CoreException {
    marker = m;
    progressMonitor.beginTask("Running refactoring...", IProgressMonitor.UNKNOWN);
    scanCompilationUnitForMarkerFix(m, preview);
    marker = null;
    progressMonitor.done();
    return new RefactoringStatus();
  }

  private boolean isSelected(final int offset) {
    return isTextSelected() && offset >= selection.getOffset() && offset < selection.getLength() + selection.getOffset();
  }

  private boolean isTextSelected() {
    return selection != null && !selection.isEmpty() && selection.getLength() != 0;
  }
}