package il.org.spartan.plugin;

import static il.org.spartan.plugin.eclipse.*;
import static il.org.spartan.spartanizer.ast.wizard.*;

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

import il.org.spartan.spartanizer.engine.*;

/** the base class for all Spartanization Refactoring classes, contains common
 * functionality
 * @author Artium Nihamkin (original)
 * @author Boris van Sosin <boris.van.sosin [at] gmail.com>} (v2)
 * @author Yossi Gil <code><yossi.gil [at] gmail.com></code>: major refactoring
 *         2013/07/10
 * @since 2013/01/01 */
// TODO: Ori, check if we can eliminate this dependency on Refactoring...
public abstract class Applicator extends Refactoring {
  private ITextSelection selection = null;
  private ICompilationUnit compilationUnit = null;
  private IMarker marker = null;
  final Collection<TextFileChange> changes = new ArrayList<>();
  private final String name;
  private int totalChanges;

  /*** Instantiates this class, with message identical to name
   * @param name a short name of this instance */
  protected Applicator(final String name) {
    this.name = name;
  }

  @Override public RefactoringStatus checkFinalConditions(final IProgressMonitor pm) throws CoreException, OperationCanceledException {
    changes.clear();
    totalChanges = 0;
    if (marker == null)
      runAsManualCall(pm);
    else {
      innerRunAsMarkerFix(pm, marker, true);
      marker = null; // consume marker
    }
    pm.done();
    return new RefactoringStatus();
  }

  @Override public RefactoringStatus checkInitialConditions(@SuppressWarnings("unused") final IProgressMonitor __) {
    final RefactoringStatus $ = new RefactoringStatus();
    if (compilationUnit == null && marker == null)
      $.merge(RefactoringStatus.createFatalErrorStatus("Nothing to refactor."));
    return $;
  }

  /** Checks a Compilation Unit (outermost ASTNode in the Java Grammar) for
   * spartanization suggestions
   * @param u what to check
   * @return a collection of {@link Suggestion} objects each containing a
   *         spartanization suggestion */
  public final List<Suggestion> collectSuggesions(final CompilationUnit u) {
    final List<Suggestion> $ = new ArrayList<>();
    u.accept(collectSuggestions(u, $));
    return $;
  }

  /** Count the number files that would change after Spartanization.
   * <p>
   * This is an slow operation. Do not call light-headedly.
   * @return total number of files with suggestions */
  public int countFilesChanges() {
    // TODO OriRoth: not sure if this function is necessary - if it is, it could
    // be easily optimized when called after countSuggestions()
    setMarker(null);
    try {
      checkFinalConditions(nullProgressMonitor);
    } catch (final OperationCanceledException e) {
      Plugin.log(e);
    } catch (final CoreException e) {
      Plugin.log(e);
    }
    return changes.size();
  }

  /** Count the number of suggestions offered by this instance.
   * <p>
   * This is an slow operation. Do not call light-headedly.
   * @return total number of suggestions offered by this instance */
  public int countSuggestions() {
    setMarker(null);
    try {
      checkFinalConditions(nullProgressMonitor);
    } catch (final OperationCanceledException e) {
      // TODO: what should we do here? This is not an error
    } catch (final CoreException e) {
      Plugin.log(e);
    }
    return totalChanges;
  }

  @Override public final Change createChange(@SuppressWarnings("unused") final IProgressMonitor __) throws OperationCanceledException {
    return new CompositeChange(getName(), changes.toArray(new Change[changes.size()]));
  }

  /** creates an ASTRewrite which contains the changes
   * @param u the Compilation Unit (outermost ASTNode in the Java Grammar)
   * @param m a progress monitor in which the progress of the refactoring is
   *        displayed
   * @return an ASTRewrite which contains the changes */
  public final ASTRewrite createRewrite(final CompilationUnit u, final IProgressMonitor m) {
    return rewriterOf(u, m, (IMarker) null);
  }

  /** @return compilationUnit */
  public ICompilationUnit getCompilationUnit() {
    return compilationUnit;
  }

  /** @return a quick fix for this instance */
  public IMarkerResolution getFix() {
    return getFix(getName());
  }

  /** @param s Spartanization's name
   * @return a quickfix which automatically performs the spartanization */
  public IMarkerResolution getFix(final String s) {
    /** a quickfix which automatically performs the spartanization
     * @author Boris van Sosin <code><boris.van.sosin [at] gmail.com></code>
     * @since 2013/07/01 */
    return new IMarkerResolution() {
      @Override public String getLabel() {
        return s;
      }

      @Override public void run(final IMarker m) {
        try {
          runAsMarkerFix(nullProgressMonitor, m);
        } catch (final CoreException e) {
          Plugin.log(e);
        }
      }
    };
  }

  /** @return a quick fix with a preview for this instance. */
  public IMarkerResolution getFixWithPreview() {
    return getFixWithPreview(getName());
  }

  /** @param s Text for the preview dialog
   * @return a quickfix which opens a refactoring wizard with the
   *         spartanization */
  public IMarkerResolution getFixWithPreview(final String s) {
    return new IMarkerResolution() {
      /** a quickfix which opens a refactoring wizard with the spartanization
       * @author Boris van Sosin <code><boris.van.sosin [at] gmail.com></code>
       *         (v2) */
      @Override public String getLabel() {
        return "Show spartanization preview";
      }

      @Override public void run(final IMarker m) {
        setMarker(m);
        try {
          new RefactoringWizardOpenOperation(new Wizard(Applicator.this)).run(Display.getCurrent().getActiveShell(),
              "Spartan refactoring: " + s + Applicator.this);
        } catch (final InterruptedException e) {
          // TODO: What should we do here?
          Plugin.log(e);
        }
      }
    };
  }

  @Override public final String getName() {
    return name;
  }

  /** @return selection */
  public ITextSelection getSelection() {
    return selection;
  }

  public IMarkerResolution getToggleClass() {
    return getToggle(ToggleSpartanization.Type.CLASS, "Disable spartanization for class");
  }

  public IMarkerResolution getToggleDeclaration() {
    return getToggle(ToggleSpartanization.Type.DECLARATION, "Disable spartanization for scope");
  }

  public IMarkerResolution getToggleFile() {
    return getToggle(ToggleSpartanization.Type.FILE, "Disable spartanization for file");
  }

  public IMarkerResolution getWringCommitDeclaration() {
    return getWringCommit(WringCommit.Type.DECLARATION, "Commit change for scope");
  }

  public IMarkerResolution getWringCommitFile() {
    return getWringCommit(WringCommit.Type.FILE, "Commit change for file");
  }

  public IMarkerResolution getWringCommitProject() {
    return getWringCommit(WringCommit.Type.PROJECT, "Commit change for project");
  }

  /** .
   * @return True if there are spartanizations which can be performed on the
   *         compilation unit. */
  public final boolean haveSuggestions() {
    return countSuggestions() > 0;
  }

  /** @param m marker which represents the range to apply the Spartanization
   *        within
   * @param n the node which needs to be within the range of
   *        <code><b>m</b></code>
   * @return True if the node is within range */
  public final boolean inRange(final IMarker m, final ASTNode n) {
    return m != null ? !isNodeOutsideMarker(n, m) : !isTextSelected() || !isNodeOutsideSelection(n);
  }

  /** Performs the current Spartanization on the provided compilation unit
   * @param u the compilation to Spartanize
   * @param pm progress monitor for long operations (could be
   *        {@link NullProgressMonitor} for light operations)
   * @throws CoreException exception from the <code>pm</code> */
  public boolean performRule(final ICompilationUnit u, final IProgressMonitor pm) throws CoreException {
    pm.beginTask("Creating change for a single compilation unit...", IProgressMonitor.UNKNOWN);
    final TextFileChange textChange = new TextFileChange(u.getElementName(), (IFile) u.getResource());
    textChange.setTextType("java");
    final IProgressMonitor m = newSubMonitor(pm);
    textChange.setEdit(createRewrite((CompilationUnit) Make.COMPILATION_UNIT.parser(u).createAST(m), m).rewriteAST());
    final boolean $ = textChange.getEdit().getLength() != 0;
    if ($)
      textChange.perform(pm);
    pm.done();
    return $;
  }

  public ASTRewrite rewriterOf(final CompilationUnit u, final IProgressMonitor pm, final IMarker m) {
    pm.beginTask("Creating rewrite operation...", IProgressMonitor.UNKNOWN);
    final ASTRewrite $ = ASTRewrite.create(u.getAST());
    consolidateSuggestions($, u, m);
    pm.done();
    return $;
  }

  /** @param pm a progress monitor in which to display the progress of the
   *        refactoring
   * @param m the marker for which the refactoring needs to run
   * @return a RefactoringStatus
   * @throws CoreException the JDT core throws it */
  public RefactoringStatus runAsMarkerFix(final IProgressMonitor pm, final IMarker m) throws CoreException {
    return innerRunAsMarkerFix(pm, m, false);
  }

  /** @param compilationUnit the compilationUnit to set */
  public void setCompilationUnit(final ICompilationUnit ¢) {
    compilationUnit = ¢;
  }

  /** @param marker the marker to set for the refactoring */
  public final void setMarker(final IMarker ¢) {
    marker = ¢;
  }

  /** @param subject the selection to set */
  public void setSelection(final ITextSelection ¢) {
    selection = ¢;
  }

  @Override public String toString() {
    return name;
  }

  protected abstract ASTVisitor collectSuggestions(final CompilationUnit u, final List<Suggestion> $);

  protected abstract void consolidateSuggestions(ASTRewrite r, CompilationUnit u, IMarker m);

  /** Determines if the node is outside of the selected text.
   * @return true if the node is not inside selection. If there is no selection
   *         at all will return false.
   * @DisableSpartan */
  protected boolean isNodeOutsideSelection(final ASTNode n) {
    return !isSelected(n.getStartPosition());
  }

  /** @param u JD
   * @throws CoreException */
  protected void scanCompilationUnit(final ICompilationUnit u, final IProgressMonitor m) throws CoreException {
    m.beginTask("Creating change for a single compilation unit...", IProgressMonitor.UNKNOWN);
    final TextFileChange textChange = new TextFileChange(u.getElementName(), (IFile) u.getResource());
    textChange.setTextType("java");
    final IProgressMonitor m1 = newSubMonitor(m);
    final CompilationUnit cu = (CompilationUnit) Make.COMPILATION_UNIT.parser(u).createAST(m1);
    textChange.setEdit(createRewrite(cu, m1).rewriteAST());
    if (textChange.getEdit().getLength() != 0)
      changes.add(textChange);
    totalChanges += collectSuggesions(cu).size();
    m.done();
  }

  protected void scanCompilationUnitForMarkerFix(final IMarker m, final IProgressMonitor pm, final boolean preview) throws CoreException {
    pm.beginTask("Creating change(s) for a single compilation unit...", 2);
    final ICompilationUnit u = makeAST.iCompilationUnit(m);
    final TextFileChange textChange = new TextFileChange(u.getElementName(), (IFile) u.getResource());
    textChange.setTextType("java");
    textChange.setEdit(createRewrite(newSubMonitor(pm), m).rewriteAST());
    if (textChange.getEdit().getLength() != 0)
      if (!preview)
        textChange.perform(pm);
      else
        changes.add(textChange);
    pm.done();
  }

  /** Creates a change from each compilation unit and stores it in the changes
   * list
   * @throws IllegalArgumentException
   * @throws CoreException */
  protected void scanCompilationUnits(final List<ICompilationUnit> us, final IProgressMonitor m) throws IllegalArgumentException, CoreException {
    m.beginTask("Iterating over gathered compilation units...", us.size());
    for (final ICompilationUnit u : us)
      scanCompilationUnit(u, newSubMonitor(m));
    m.done();
  }

  /** creates an ASTRewrite, under the context of a text marker, which contains
   * the changes
   * @param pm a progress monitor in which to display the progress of the
   *        refactoring
   * @param m the marker
   * @return an ASTRewrite which contains the changes */
  private ASTRewrite createRewrite(final IProgressMonitor pm, final IMarker m) {
    return rewriterOf((CompilationUnit) makeAST.COMPILATION_UNIT.from(m, pm), pm, m);
  }

  @SuppressWarnings("static-method") private IMarkerResolution getToggle(final ToggleSpartanization.Type t, final String l) {
    return new IMarkerResolution() {
      @Override public String getLabel() {
        return l;
      }

      @Override public void run(final IMarker m) {
        try {
          ToggleSpartanization.deactivate(nullProgressMonitor, m, t);
        } catch (IllegalArgumentException | CoreException x) {
          Plugin.log(x);
        }
      }
    };
  }

  private List<ICompilationUnit> getUnits(final IProgressMonitor pm) throws JavaModelException {
    if (!isTextSelected())
      return compilationUnits(compilationUnit != null ? compilationUnit : currentCompilationUnit(), newSubMonitor(pm));
    final List<ICompilationUnit> $ = new ArrayList<>();
    $.add(compilationUnit);
    return $;
  }

  @SuppressWarnings("static-method") private IMarkerResolution getWringCommit(final WringCommit.Type t, final String l) {
    return new IMarkerResolution() {
      @Override public String getLabel() {
        return l;
      }

      @Override public void run(final IMarker m) {
        try {
          WringCommit.go(nullProgressMonitor, m, t);
        } catch (IllegalArgumentException | CoreException e) {
          Plugin.log(e);
        }
      }
    };
  }

  private RefactoringStatus innerRunAsMarkerFix(final IProgressMonitor pm, final IMarker m, final boolean preview) throws CoreException {
    marker = m;
    pm.beginTask("Running refactoring...", IProgressMonitor.UNKNOWN);
    scanCompilationUnitForMarkerFix(m, pm, preview);
    marker = null;
    pm.done();
    return new RefactoringStatus();
  }

  private boolean isSelected(final int offset) {
    return isTextSelected() && offset >= selection.getOffset() && offset < selection.getLength() + selection.getOffset();
  }

  private boolean isTextSelected() {
    return selection != null && !selection.isEmpty() && selection.getLength() != 0;
  }

  private void runAsManualCall(final IProgressMonitor pm) throws JavaModelException, CoreException {
    pm.beginTask("Checking preconditions...", IProgressMonitor.UNKNOWN);
    scanCompilationUnits(getUnits(pm), newSubMonitor(pm));
    pm.done();
  }
}