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
public abstract class Spartanization extends Refactoring {
  private ITextSelection selection = null;
  private ICompilationUnit compilationUnit = null;
  private IMarker marker = null;
  final Collection<TextFileChange> changes = new ArrayList<>();
  private final String name;
  private int totalChanges;

  /*** Instantiates this class, with message identical to name
   * @param name a short name of this instance */
  protected Spartanization(final String name) {
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

  protected abstract ASTVisitor collect(final List<Rewrite> $, final CompilationUnit u);

  /** Count the number files that would change after Spartanization.
   * <p>
   * This is an slow operation. Do not call light-headedly.
   * @return total number of files with suggestions */
  public int countFilesChanges() {
    // TODO OriRoth: not sure if this function is necessary - if it is, it could
    // be easily optimized when called after countSuggestions()
    setMarker(null);
    try {
      checkFinalConditions(new NullProgressMonitor());
    } catch (final OperationCanceledException e) {
      e.printStackTrace();
    } catch (final CoreException e) {
      e.printStackTrace();
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
      checkFinalConditions(new NullProgressMonitor());
    } catch (final OperationCanceledException e) {
      e.printStackTrace();
    } catch (final CoreException e) {
      e.printStackTrace();
    }
    return totalChanges;
  }

  @Override public final Change createChange(@SuppressWarnings("unused") final IProgressMonitor __) throws OperationCanceledException {
    return new CompositeChange(getName(), changes.toArray(new Change[changes.size()]));
  }

  /** creates an ASTRewrite which contains the changes
   * @param u the Compilation Unit (outermost ASTNode in the Java Grammar)
   * @param pm a progress monitor in which the progress of the refactoring is
   *        displayed
   * @return an ASTRewrite which contains the changes */
  public final ASTRewrite createRewrite(final CompilationUnit u, final IProgressMonitor pm) {
    return createRewrite(pm, u, (IMarker) null);
  }

  private ASTRewrite createRewrite(final IProgressMonitor pm, final CompilationUnit u, final IMarker m) {
      pm.beginTask("Creating rewrite operation...", 1);
    final ASTRewrite $ = ASTRewrite.create(u.getAST());
    fillRewrite($, u, m);
      pm.done();
    return $;
  }

  /** creates an ASTRewrite, under the context of a text marker, which contains
   * the changes
   * @param pm a progress monitor in which to display the progress of the
   *        refactoring
   * @param m the marker
   * @return an ASTRewrite which contains the changes */
  private ASTRewrite createRewrite(final IProgressMonitor pm, final IMarker m) {
    return createRewrite(pm, (CompilationUnit) makeAST.COMPILATION_UNIT.from(m, pm), m);
  }

  protected abstract void fillRewrite(ASTRewrite r, CompilationUnit u, IMarker m);

  /** Checks a Compilation Unit (outermost ASTNode in the Java Grammar) for
   * spartanization suggestions
   * @param u what to check
   * @return a collection of {@link Rewrite} objects each containing a
   *         spartanization opportunity */
  public final List<Rewrite> findOpportunities(final CompilationUnit u) {
    final List<Rewrite> $ = new ArrayList<>();
    u.accept(collect($, u));
    return $;
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
          runAsMarkerFix(new NullProgressMonitor(), m);
        } catch (final CoreException e) {
          throw new RuntimeException(e);
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
          new RefactoringWizardOpenOperation(new Wizard(Spartanization.this)).run(Display.getCurrent().getActiveShell(),
              "Spartan refactoring: " + s + Spartanization.this);
        } catch (final InterruptedException e) {
          e.printStackTrace();
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

  @SuppressWarnings("static-method") private IMarkerResolution getToggle(final ToggleSpartanization.Type t, final String l) {
    return new IMarkerResolution() {
      @Override public String getLabel() {
        return l;
      }

      @Override public void run(final IMarker m) {
        try {
          ToggleSpartanization.diactivate(new NullProgressMonitor(), m, t);
        } catch (IllegalArgumentException | CoreException e) {
          e.printStackTrace();
        }
      }
    };
  }

  public IMarkerResolution getToggleClass() {
    return getToggle(ToggleSpartanization.Type.CLASS, "Disable spartanization for class");
  }

  public IMarkerResolution getToggleDeclaration() {
    return getToggle(ToggleSpartanization.Type.DECLARATION, "Disable spartanization");
  }

  public IMarkerResolution getToggleFile() {
    return getToggle(ToggleSpartanization.Type.FILE, "Disable spartanization for file");
  }

  private List<ICompilationUnit> getUnits(final IProgressMonitor pm) throws JavaModelException {
    if (!isTextSelected())
      return compilationUnits(compilationUnit != null ? compilationUnit : currentCompilationUnit(), newSubMonitor(pm));
    final List<ICompilationUnit> $ = new ArrayList<>();
    $.add(compilationUnit);
    return $;
  }

  /** .
   * @return True if there are spartanizations which can be performed on the
   *         compilation unit. */
  public final boolean haveSuggestions() {
    return countSuggestions() > 0;
  }

  private RefactoringStatus innerRunAsMarkerFix(final IProgressMonitor pm, final IMarker m, final boolean preview) throws CoreException {
    marker = m;
    pm.beginTask("Running refactoring...", 2);
    scanCompilationUnitForMarkerFix(m, pm, preview);
    marker = null;
    pm.done();
    return new RefactoringStatus();
  }

  /** @param m marker which represents the range to apply the Spartanization
   *        within
   * @param n the node which needs to be within the range of
   *        <code><b>m</b></code>
   * @return True if the node is within range */
  public final boolean inRange(final IMarker m, final ASTNode n) {
    return m != null ? !isNodeOutsideMarker(n, m) : !isTextSelected() || !isNodeOutsideSelection(n);
  }

  /** Determines if the node is outside of the selected text.
   * @return true if the node is not inside selection. If there is no selection
   *         at all will return false. */
  protected boolean isNodeOutsideSelection(final ASTNode n) {
    return !isSelected(n.getStartPosition());
  }

  private boolean isSelected(final int offset) {
    return isTextSelected() && offset >= selection.getOffset() && offset < selection.getLength() + selection.getOffset();
  }

  private boolean isTextSelected() {
    return selection != null && !selection.isEmpty() && selection.getLength() != 0;
  }

  /** Performs the current Spartanization on the provided compilation unit
   * @param cu the compilation to Spartanize
   * @param pm progress monitor for long operations (could be
   *        {@link NullProgressMonitor} for light operations)
   * @throws CoreException exception from the <code>pm</code> */
  public void performRule(final ICompilationUnit cu, final IProgressMonitor pm) throws CoreException {
    pm.beginTask("Creating change for a single compilation unit...", 2);
    final TextFileChange textChange = new TextFileChange(cu.getElementName(), (IFile) cu.getResource());
    textChange.setTextType("java");
    final IProgressMonitor m = newSubMonitor(pm);
    textChange.setEdit(createRewrite((CompilationUnit) Make.COMPILATION_UNIT.parser(cu).createAST(m), m).rewriteAST());
    if (textChange.getEdit().getLength() != 0)
      textChange.perform(pm);
    pm.done();
  }

  private void runAsManualCall(final IProgressMonitor pm) throws JavaModelException, CoreException {
    pm.beginTask("Checking preconditions...", 2);
    scanCompilationUnits(getUnits(pm), newSubMonitor(pm));
  }

  /** @param pm a progress monitor in which to display the progress of the
   *        refactoring
   * @param m the marker for which the refactoring needs to run
   * @return a RefactoringStatus
   * @throws CoreException the JDT core throws it */
  public RefactoringStatus runAsMarkerFix(final IProgressMonitor pm, final IMarker m) throws CoreException {
    return innerRunAsMarkerFix(pm, m, false);
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
    totalChanges += findOpportunities(cu).size();
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
   * array
   * @throws IllegalArgumentException
   * @throws CoreException */
  protected void scanCompilationUnits(final List<ICompilationUnit> cus, final IProgressMonitor pm) throws IllegalArgumentException, CoreException {
    pm.beginTask("Iterating over gathered compilation units...", cus.size());
    for (final ICompilationUnit cu : cus)
      scanCompilationUnit(cu, newSubMonitor(pm));
    pm.done();
  }

  /** @param u the compilationUnit to set */
  public void setCompilationUnit(final ICompilationUnit u) {
    compilationUnit = u;
  }

  /** @param m the marker to set for the refactoring */
  public final void setMarker(final IMarker m) {
    marker = m;
  }

  /** @param s the selection to set */
  public void setSelection(final ITextSelection s) {
    selection = s;
  }

  @Override public String toString() {
    return name;
  }
}