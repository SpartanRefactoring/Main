package il.org.spartan.spartanizer.plugin;

import static il.org.spartan.plugin.old.eclipse.*;
import static il.org.spartan.utils.fault.*;

import static il.org.spartan.spartanizer.ast.navigate.wizard.*;

import java.util.*;
import java.util.List;
import java.util.function.*;

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

import il.org.spartan.plugin.old.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.utils.*;
import il.org.spartan.utils.fluent.*;

/** the base class for all GUI applicators contains common functionality
 * @author Artium Nihamkin (original)
 * @author Boris van Sosin <boris.van.sosin [at] gmail.com>} (v2)
 * @author Yossi Gil: major refactoring 2013/07/10
 * @author Ori Roth: new plugin logic interfaces
 * @since 2013/01/01 */
public abstract class GUIConfigurationApplicator extends Refactoring {
  /*** Instantiates this class, with message identical to name
   * @param name a short name of this instance */
  protected GUIConfigurationApplicator(final String name) {
    this.name = name;
  }

  public boolean apply(final ICompilationUnit cu) {
    return apply(cu, new Range(0, 0));
  }

  public int apply(final WrappedCompilationUnit $, final AbstractSelection<?> s) {
    if (s != null && s.textSelection != null)
      setSelection(s.textSelection);
    if (s instanceof TrackerSelection)
      return apply($, (TrackerSelection) s);
    try {
      return apply($);
    } catch (final CoreException ¢) {
      note.bug(this, ¢);
      return 0;
    }
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
   * @return a collection of {@link Tip} objects each containing a
   *         spartanization tip */
  public final Tips collectTips(final CompilationUnit ¢) {
    final Tips $ = Tips.empty();
    ¢.accept(tipsCollector($));
    return $;
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
      note.cancel(this, ¢);
    } catch (final CoreException ¢) {
      note.bug(this, ¢);
    }
    return totalChanges;
  }

  @Override public final Change createChange(final IProgressMonitor pm) throws OperationCanceledException {
    progressMonitor = pm;
    return new CompositeChange(getName(), changes.toArray(new Change[changes.size()]));
  }

  public ASTRewrite createRewrite(final CompilationUnit ¢) {
    return rewriterOf(¢, null, new Int());
  }

  /** creates an ASTRewrite which contains the changes
   * @param u the Compilation Unit (outermost ASTNode in the Java Grammar)
   * @param m a progress monitor in which the progress of the refactoring is
   *        displayed
   * @return an ASTRewrite which contains the changes */
  public ASTRewrite createRewrite(final CompilationUnit ¢, final Int counter) {
    return rewriterOf(¢, null, counter);
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

  public int fuzzyImplementationApply(final ICompilationUnit $, final ITextSelection s) {
    try {
      setICompilationUnit($);
      setSelection(s != null && s.getLength() > 0 && !s.isEmpty() ? s : null);
      return performRule($);
    } catch (final CoreException ¢) {
      note.bug(this, ¢);
    }
    return 0;
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

  @Override public final String getName() {
    return name;
  }

  public IProgressMonitor progressMonitor() {
    return progressMonitor;
  }

  public ITextSelection getSelection() {
    return selection;
  }

  public int go() throws CoreException {
    progressMonitor.beginTask("Creating change for a single compilation unit...", IProgressMonitor.UNKNOWN);
    final TextFileChange textChange = new TextFileChange(compilationUnitName(), compilationUnitIFile());
    textChange.setTextType("java");
    final IProgressMonitor m = eclipse.newSubMonitor(progressMonitor);
    final Int $ = new Int();
    textChange.setEdit(createRewrite((CompilationUnit) make.COMPILATION_UNIT.parser(iCompilationUnit).createAST(m), $).rewriteAST());
    if (textChange.getEdit().getLength() != 0)
      textChange.perform(progressMonitor);
    progressMonitor.done();
    return $.get();
  }

  /** .
   * @return whether there are tips which can be performed on the compilation
   *         unit. */
  public final boolean haveTips() {
    return countTips() > 0;
  }

  /** @param m marker which represents the range to apply the tipper within
   * @param n the node which needs to be within the range of {@code m}
   * @return whether the node is within range */
  public final boolean inRange(final IMarker m, final ASTNode n) {
    return m == null ? !isTextSelected() || !isNotSelected(n) : !eclipse.facade.isNodeOutsideMarker(n, m);
  }

  public void parse() {
    compilationUnit = (CompilationUnit) make.COMPILATION_UNIT.parser(iCompilationUnit).createAST(progressMonitor);
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

  private boolean apply(final ICompilationUnit cu, final Range r) {
    return fuzzyImplementationApply(cu, r == null || r.isEmpty() ? new TextSelection(0, 0) : new TextSelection(r.from, r.size())) > 0;
  }

  private int apply(final WrappedCompilationUnit u) throws CoreException {
    final TextFileChange textChange = init(u);
    assert textChange != null;
    final Int $ = new Int();
    final WrappedCompilationUnit u1 = u.build();
    final CompilationUnit u2 = u1.compilationUnit;
    final ASTRewrite r = createRewrite(u2, $);
    try {
      textChange.setEdit(r.rewriteAST());
    } catch (final AssertionError x) {
      assert unreachable() : dump() + //
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
          done(x);
      return 0;
    }
    if (textChange.getEdit().getLength() != 0)
      textChange.perform(progressMonitor);
    progressMonitor.done();
    return $.get();
  }

  private int apply(final WrappedCompilationUnit u, final TrackerSelection s) {
    try {
      final TextFileChange textChange = init(u);
      setSelection(s == null || s.textSelection == null || s.textSelection.getLength() <= 0 || s.textSelection.isEmpty() ? null : s.textSelection);
      final Int $ = new Int();
      textChange.setEdit(createRewrite(u.build().compilationUnit, $).rewriteAST());
      if (textChange.getEdit().getLength() != 0)
        textChange.perform(progressMonitor);
      if (s != null)
        s.update();
      return $.get();
    } catch (final CoreException ¢) {
      note.bug(this, ¢);
      return 0;
    } finally {
      progressMonitor.done();
    }
  }

  private void collectAllTips() throws CoreException {
    progressMonitor.beginTask("Collecting tips...", IProgressMonitor.UNKNOWN);
    scanCompilationUnits(getUnits());
    progressMonitor.done();
  }

  private IFile compilationUnitIFile() {
    return (IFile) iCompilationUnit.getResource();
  }

  private String compilationUnitName() {
    return iCompilationUnit.getElementName();
  }

  /** creates an ASTRewrite, under the context of a text marker, which contains
   * the changes
   * @param pm a progress monitor in which to display the progress of the
   *        refactoring
   * @param m the marker
   * @return an ASTRewrite which contains the changes */
  private ASTRewrite createRewrite(final IMarker ¢) {
    return rewriterOf((CompilationUnit) makeAST.COMPILATION_UNIT.from(¢, progressMonitor), ¢, new Int());
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
          new RefactoringWizardOpenOperation(new Wizard(GUIConfigurationApplicator.this)).run(Display.getCurrent().getActiveShell(),
              "Spartanization: " + s + GUIConfigurationApplicator.this);
        } catch (final InterruptedException ¢) {
          note.cancel(this, ¢);
        }
      }
    };
  }

  private Collection<ICompilationUnit> getUnits() throws JavaModelException {
    if (!isTextSelected())
      return compilationUnits(iCompilationUnit != null ? iCompilationUnit : currentCompilationUnit(), newSubMonitor(progressMonitor));
    final List<ICompilationUnit> $ = new ArrayList<>();
    $.add(iCompilationUnit);
    return $;
  }

  private TextFileChange init(final WrappedCompilationUnit ¢) {
    setICompilationUnit(¢.descriptor);
    progressMonitor.beginTask("Creating change for compilation unit...", IProgressMonitor.UNKNOWN);
    final TextFileChange $ = new TextFileChange(¢.descriptor.getElementName(), (IFile) ¢.descriptor.getResource());
    $.setTextType("java");
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

  /** Determines if the node is outside of the selected text.
   * @return whether the node is not inside selection. If there is no selection
   *         at all will return false. */
  private boolean isNotSelected(final ASTNode ¢) {
    return !isSelected(¢.getStartPosition());
  }

  private boolean isSelected(final int offset) {
    return isTextSelected() && offset >= selection.getOffset() && offset < selection.getLength() + selection.getOffset();
  }

  private boolean isTextSelected() {
    return selection != null && !selection.isEmpty();
  }

  /** Performs the current tipper on the provided compilation unit
   * @param u the compilation to Spartanize
   * @param pm progress monitor for long operations (could be
   *        {@link NullProgressMonitor} for light operations)
   * @throws CoreException exception from the {@code pm} */
  private int performRule(final ICompilationUnit u) throws CoreException {
    progressMonitor.beginTask("Creating change for a single compilation unit...", IProgressMonitor.UNKNOWN);
    final TextFileChange textChange = new TextFileChange(u.getElementName(), (IFile) u.getResource());
    textChange.setTextType("java");
    final IProgressMonitor m = eclipse.newSubMonitor(progressMonitor);
    final Int $ = new Int();
    textChange.setEdit(createRewrite((CompilationUnit) make.COMPILATION_UNIT.parser(u).createAST(m), $).rewriteAST());
    if (textChange.getEdit().getLength() != 0)
      textChange.perform(progressMonitor);
    progressMonitor.done();
    return $.get();
  }

  private ASTRewrite rewriterOf(final CompilationUnit u, final IMarker m, final Int counter) {
    note.logger.fine("Weaving maximal rewrite of " + u);
    progressMonitor.beginTask("Weaving maximal rewrite ...", IProgressMonitor.UNKNOWN);
    final Int count = new Int();
    final ASTRewrite $ = computeMaximalRewrite(u, m, __ -> count.step());
    counter.add(count);
    progressMonitor.done();
    return $;
  }

  private void scan() {
    tips.clear();
    compilationUnit.accept(tipsCollector(tips));
  }

  /** @param u JD
   * @throws CoreException */
  private int scanCompilationUnit(final ICompilationUnit u, final IProgressMonitor m) throws CoreException {
    m.beginTask("Collecting tips for " + u.getElementName(), IProgressMonitor.UNKNOWN);
    final TextFileChange textChange = new TextFileChange(u.getElementName(), (IFile) u.getResource());
    textChange.setTextType("java");
    final CompilationUnit cu = (CompilationUnit) make.COMPILATION_UNIT.parser(u).createAST(m);
    final Int $ = new Int();
    textChange.setEdit(createRewrite(cu, $).rewriteAST());
    if (textChange.getEdit().getLength() != 0)
      changes.add(textChange);
    totalChanges += collectTips(cu).size();
    m.done();
    return $.get();
  }

  private void scanCompilationUnitForMarkerFix(final IMarker m, final boolean preview) throws CoreException {
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
  private void scanCompilationUnits(final Collection<ICompilationUnit> us) throws IllegalArgumentException, CoreException {
    progressMonitor.beginTask("Iterating over eligible compilation units...", us.size());
    for (final ICompilationUnit ¢ : us) // NANO - can't, throws...
      scanCompilationUnit(¢, eclipse.newSubMonitor(progressMonitor));
    progressMonitor.done();
  }

  protected abstract ASTRewrite computeMaximalRewrite(CompilationUnit u, IMarker m, Consumer<ASTNode> nodeLogger);

  protected abstract ASTVisitor tipsCollector(Tips into);

  boolean apply() {
    return apply(iCompilationUnit, new Range(0, 0));
  }

  void collectTips() {
    progressMonitor.beginTask("Collecting tips...", IProgressMonitor.UNKNOWN);
    scan();
    progressMonitor.done();
  }

  public TextEditGroup currentEditGroup() {
    return currentEditGroup;
  }

  private IProgressMonitor progressMonitor = nullProgressMonitor;
  private final Collection<TextFileChange> changes = new ArrayList<>();
  protected CompilationUnit compilationUnit;
  private ICompilationUnit iCompilationUnit;
  private IMarker marker;
  private ITextSelection selection;
  private final Tips tips = Tips.empty();
  private int totalChanges;
  protected String name;
  private TextEditGroup currentEditGroup;
}