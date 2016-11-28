package il.org.spartan.plugin;

import static il.org.spartan.plugin.old.eclipse.*;
import static il.org.spartan.spartanizer.utils.fault.*;
import static il.org.spartan.spartanizer.utils.fault.done;

import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.*;

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

import il.org.spartan.plugin.old.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.utils.*;
import il.org.spartan.utils.*;

/** the base class for all GUI applicators contains common functionality
 * @author Artium Nihamkin (original)
 * @author Boris van Sosin <boris.van.sosin [at] gmail.com>} (v2)
 * @author Yossi Gil <code><yossi.gil [at] gmail.com></code>: major refactoring
 *         2013/07/10
 * @author Ori Roth: new plugin logic interfaces
 * @since 2013/01/01 */
public abstract class AbstractGUIApplicator extends Refactoring {
  public IProgressMonitor progressMonitor = nullProgressMonitor;
  final Collection<TextFileChange> changes = new ArrayList<>();
  private CompilationUnit compilationUnit;
  private ICompilationUnit iCompilationUnit;
  private IMarker marker;
  protected String name;
  private ITextSelection selection;
  final List<Tip> tips = new ArrayList<>();
  private int totalChanges;

  /*** Instantiates this class, with message identical to name
   * @param name a short name of this instance */
  protected AbstractGUIApplicator(final String name) {
    this.name = name;
  }

  public boolean apply(final ICompilationUnit cu) {
    return apply(cu, new Range(0, 0));
  }

  public boolean apply(final ICompilationUnit cu, final Range r) {
    return fuzzyImplementationApply(cu, r == null || r.isEmpty() ? new TextSelection(0, 0) : new TextSelection(r.from, r.size())) > 0;
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
  public final List<Tip> collectSuggestions(final CompilationUnit ¢) {
    final List<Tip> $ = new ArrayList<>();
    ¢.accept(makeTipsCollector($));
    return $;
  }

  public IFile compilationUnitIFile() {
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
    } catch (final OperationCanceledException ¢) {
      monitor.logCancellationRequest(this, ¢);
    } catch (final CoreException ¢) {
      monitor.logEvaluationError(this, ¢);
    }
    return totalChanges;
  }

  @Override public final Change createChange(final IProgressMonitor pm) throws OperationCanceledException {
    progressMonitor = pm;
    return new CompositeChange(getName(), changes.toArray(new Change[changes.size()]));
  }

  /** creates an ASTRewrite which contains the changes
   * @param counter
   * @param u the Compilation Unit (outermost ASTNode in the Java Grammar)
   * @param m a progress monitor in which the progress of the refactoring is
   *        displayed
   * @return an ASTRewrite which contains the changes */
  public final ASTRewrite createRewrite(final CompilationUnit ¢, final AtomicInteger counter) {
    return rewriterOf(¢, (IMarker) null, counter);
  }

  public final ASTRewrite createRewrite(final CompilationUnit ¢) {
    return rewriterOf(¢, (IMarker) null, new AtomicInteger(0));
  }

  public boolean follow() throws CoreException {
    progressMonitor.beginTask("Preparing the change ...", IProgressMonitor.UNKNOWN);
    final ASTRewrite astRewrite = ASTRewrite.create(compilationUnit.getAST());
    for (final Tip ¢ : tips) {
      progressMonitor.worked(1);
      ¢.go(astRewrite, new TextEditGroup("spartanization: textEditGroup"));
    }
    progressMonitor.done();
    final TextEdit rewriteAST = astRewrite.rewriteAST();
    final TextFileChange textFileChange = new TextFileChange(compilationUnitName(), compilationUnitIFile());
    textFileChange.setTextType("java");
    textFileChange.setEdit(rewriteAST);
    final boolean $ = textFileChange.getEdit().getLength() != 0;
    if ($)
      textFileChange.perform(progressMonitor);
    progressMonitor.done();
    return $;
  }

  public int fuzzyImplementationApply(final ICompilationUnit cu, final ITextSelection s) {
    try {
      setICompilationUnit(cu);
      setSelection(s != null && s.getLength() > 0 && !s.isEmpty() ? s : null);
      return performRule(cu);
    } catch (final CoreException ¢) {
      monitor.logEvaluationError(this, ¢);
    }
    return 0;
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
        } catch (final CoreException ¢) {
          monitor.logEvaluationError(this, ¢);
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
          new RefactoringWizardOpenOperation(new Wizard(AbstractGUIApplicator.this)).run(Display.getCurrent().getActiveShell(),
              "Laconization: " + s + AbstractGUIApplicator.this);
        } catch (final InterruptedException ¢) {
          monitor.logCancellationRequest(this, ¢);
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

  public int go() throws CoreException {
    progressMonitor.beginTask("Creating change for a single compilation unit...", IProgressMonitor.UNKNOWN);
    final TextFileChange textChange = new TextFileChange(compilationUnitName(), compilationUnitIFile());
    textChange.setTextType("java");
    final IProgressMonitor m = newSubMonitor(progressMonitor);
    final AtomicInteger counter = new AtomicInteger(0);
    textChange.setEdit(createRewrite((CompilationUnit) Make.COMPILATION_UNIT.parser(iCompilationUnit).createAST(m), counter).rewriteAST());
    if (textChange.getEdit().getLength() != 0)
      textChange.perform(progressMonitor);
    progressMonitor.done();
    return counter.get();
  }

  /** .
   * @return <code><b>true</b></code> <em>iff</em>there are tipss which can be
   *         performed on the compilation unit. */
  public final boolean haveTips() {
    return countTips() > 0;
  }

  /** @param m marker which represents the range to apply the tipper within
   * @param n the node which needs to be within the range of
   *        <code><b>m</b></code>
   * @return <code><b>true</b></code> <em>iff</em>the node is within range */
  public final boolean inRange(final IMarker m, final ASTNode n) {
    return m != null ? !eclipse.facade.isNodeOutsideMarker(n, m) : !isTextSelected() || !isNodeOutsideSelection(n);
  }

  /** Performs the current tipper on the provided compilation unit
   * @param u the compilation to Spartanize
   * @param pm progress monitor for long operations (could be
   *        {@link NullProgressMonitor} for light operations)
   * @throws CoreException exception from the <code>pm</code> */
  public int performRule(final ICompilationUnit u) throws CoreException {
    progressMonitor.beginTask("Creating change for a single compilation unit...", IProgressMonitor.UNKNOWN);
    final TextFileChange textChange = new TextFileChange(u.getElementName(), (IFile) u.getResource());
    textChange.setTextType("java");
    final IProgressMonitor m = newSubMonitor(progressMonitor);
    final AtomicInteger counter = new AtomicInteger(0);
    textChange.setEdit(createRewrite((CompilationUnit) Make.COMPILATION_UNIT.parser(u).createAST(m), counter).rewriteAST());
    if (textChange.getEdit().getLength() != 0)
      textChange.perform(progressMonitor);
    progressMonitor.done();
    return counter.get();
  }

  public ASTRewrite rewriterOf(final CompilationUnit u, final IMarker m, final AtomicInteger counter) {
    progressMonitor.beginTask("Creating rewrite operation...", IProgressMonitor.UNKNOWN);
    final ASTRewrite $ = ASTRewrite.create(u.getAST());
    consolidateTips($, u, m, counter);
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

  protected abstract void consolidateTips(ASTRewrite r, CompilationUnit u, IMarker m, final AtomicInteger counter);

  public void consolidateTips(final ASTRewrite r, final CompilationUnit u, final IMarker m) {
    consolidateTips(r, u, m, new AtomicInteger(0));
  }

  /** Determines if the node is outside of the selected text.
   * @return <code><b>true</b></code> <em>iff</em>the node is not inside
   *         selection. If there is no selection at all will return false.
   * @DisableSpartan */
  protected boolean isNodeOutsideSelection(final ASTNode ¢) {
    return !isSelected(¢.getStartPosition());
  }

  protected abstract ASTVisitor makeTipsCollector(final List<Tip> $);

  public void parse() {
    compilationUnit = (CompilationUnit) Make.COMPILATION_UNIT.parser(iCompilationUnit).createAST(progressMonitor);
  }

  public void scan() {
    tips.clear();
    compilationUnit.accept(makeTipsCollector(tips));
  }

  /** @param u JD
   * @throws CoreException */
  protected int scanCompilationUnit(final ICompilationUnit u, final IProgressMonitor m) throws CoreException {
    m.beginTask("Collecting tips for " + u.getElementName(), IProgressMonitor.UNKNOWN);
    final TextFileChange textChange = new TextFileChange(u.getElementName(), (IFile) u.getResource());
    textChange.setTextType("java");
    final CompilationUnit cu = (CompilationUnit) Make.COMPILATION_UNIT.parser(u).createAST(m);
    final AtomicInteger counter = new AtomicInteger(0);
    textChange.setEdit(createRewrite(cu, counter).rewriteAST());
    if (textChange.getEdit().getLength() != 0)
      changes.add(textChange);
    totalChanges += collectSuggestions(cu).size();
    m.done();
    return counter.get();
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
    return rewriterOf((CompilationUnit) makeAST.COMPILATION_UNIT.from(¢, progressMonitor), ¢, new AtomicInteger(0));
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

  protected boolean isTextSelected() {
    return selection != null && !selection.isEmpty();
  }

  public int apply(final WrappedCompilationUnit u, final AbstractSelection<?> s) {
    if (s != null && s.textSelection != null)
      setSelection(s.textSelection);
    if (s instanceof TrackerSelection)
      return apply(u, (TrackerSelection) s);
    try {
      return apply(u);
    } catch (final CoreException ¢) {
      monitor.logEvaluationError(this, ¢);
      return 0;
    }
  }

  private int apply(final WrappedCompilationUnit u) throws JavaModelException, CoreException {
    final TextFileChange textChange = init(u);
    assert textChange != null;
    final AtomicInteger $ = new AtomicInteger();
    final WrappedCompilationUnit u1 = u.build();
    final CompilationUnit u2 = u1.compilationUnit;
    final ASTRewrite r = createRewrite(u2, $);
    try {
      textChange.setEdit(r.rewriteAST());
    } catch (final AssertionError x) { // assert unreachable():
      System.out.println(dump() + //
          "\n x=" + x + //
          "\n $=" + $ + //
          "\n u=" + u + //
          "\n u=" + u.name() + //
          "\n u1=" + u1 + //
          "\n u2=" + u2 + //
          "\n r=" + r + //
          "\n textchange=" + textChange + //
          "\n textchange.getEdit=" + textChange.getEdit() + //
          "\n textchange.getEdit.length=" + (textChange.getEdit() == null ? "??" : textChange.getEdit().getLength() + "") + //
          done(x));
      return 0;
    }
    if (textChange.getEdit().getLength() != 0)
      textChange.perform(progressMonitor);
    progressMonitor.done();
    return $.get();
  }

  private TextFileChange init(final WrappedCompilationUnit ¢) {
    setICompilationUnit(¢.descriptor);
    progressMonitor.beginTask("Creating change for compilation unit...", IProgressMonitor.UNKNOWN);
    final TextFileChange $ = new TextFileChange(¢.descriptor.getElementName(), (IFile) ¢.descriptor.getResource());
    $.setTextType("java");
    return $;
  }

  public int apply(final WrappedCompilationUnit u, final TrackerSelection s) {
    try {
      final TextFileChange textChange = init(u);
      setSelection(s == null || s.textSelection == null || s.textSelection.getLength() <= 0 || s.textSelection.isEmpty() ? null : s.textSelection);
      final AtomicInteger $ = new AtomicInteger();
      textChange.setEdit(createRewrite(u.build().compilationUnit, $).rewriteAST());
      if (textChange.getEdit().getLength() != 0)
        textChange.perform(progressMonitor);
      if (s != null)
        s.update();
      return $.get();
    } catch (final CoreException ¢) {
      monitor.logEvaluationError(this, ¢);
      return 0;
    } finally {
      progressMonitor.done();
    }
  }
}