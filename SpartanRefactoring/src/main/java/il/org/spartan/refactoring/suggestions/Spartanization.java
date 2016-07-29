package il.org.spartan.refactoring.suggestions;

import il.org.spartan.refactoring.utils.*;

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

/**
 * the base class for all Spartanization Refactoring classes, contains common
 * functionality
 *
 * @author Artium Nihamkin (original)
 * @author Boris van Sosin <boris.van.sosin [at] gmail.com>} (v2)
 * @author Yossi Gil <code><yossi.gil [at] gmail.com></code>: major refactoring
 *         2013/07/10
 * @since 2013/01/01
 */
public abstract class Spartanization {
  public RefactoringStatus checkFinalConditions(final IProgressMonitor pm) throws CoreException, OperationCanceledException {
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
  /**
   * Count the number files that would change after Spartanization. <p> This is
   * an slow operation. Do not call light-headedly.
   *
   * @return the total number of files with suggestions
   */
  public int countFilesChanges() {
    // TODO not sure if this function is necessary - if it is, it could be
    // easily optimized when called after countSuggestions()
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
  /**
   * Count the number of suggestions offered by this instance. <p> This is an
   * slow operation. Do not call lightheadedly.
   *
   * @return the total number of suggestions offered by this instance
   */
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
  public final Change createChange(@SuppressWarnings("unused") final IProgressMonitor __) throws OperationCanceledException {
    return new CompositeChange("" + this, changes.toArray(new Change[changes.size()]));
  }
  /**
   * creates an ASTRewrite which contains the changes
   *
   * @param u
   *          the Compilation Unit (outermost ASTNode in the Java Grammar)
   * @param pm
   *          a progress monitor in which the progress of the refactoring is
   *          displayed
   * @return an ASTRewrite which contains the changes
   */
  public final ASTRewrite createRewrite(final CompilationUnit u, final SubProgressMonitor pm) {
    return createRewrite(pm, u, (IMarker) null);
  }
  /**
   * Checks a Compilation Unit (outermost ASTNode in the Java Grammar) for
   * spartanization suggestions
   *
   * @param u
   *          what to check
   * @return a collection of {@link Suggestion} objects each containing a
   *         spartanization opportunity
   */
  public final List<Suggestion> findOpportunities(final CompilationUnit u) {
    final List<Suggestion> $ = new ArrayList<>();
    u.accept(collect($, u));
    return $;
  }
  /**
   * @return the compilationUnit
   */
  public ICompilationUnit getCompilationUnit() {
    return compilationUnit;
  }
  /**
   * @return a quick fix for this instance
   */
  public IMarkerResolution getFix() {
    return getFix("" + this);
  }
  /**
   * @param s
   *          Spartanization's name
   * @return a quickfix which automatically performs the spartanization
   */
  public IMarkerResolution getFix(final String s) {
    /**
     * a quickfix which automatically performs the spartanization
     *
     * @author Boris van Sosin <code><boris.van.sosin [at] gmail.com></code>
     * @since 2013/07/01
     */
    return new IMarkerResolution() {
      @Override public String getLabel() {
        return "Spartanize!";
      }
      @Override public void run(final IMarker m) {
        try {
          runAsMarkerFix(new NullProgressMonitor(), m);
        } catch (final CoreException e) {
          e.printStackTrace();
        }
      }
    };
  }
  /**
   * @return a quick fix with a preview for this instance.
   */
  public IMarkerResolution getFixWithPreview() {
    return getFixWithPreview("" + this);
  }
  /**
   * @param s
   *          Text for the preview dialog
   * @return a quickfix which opens a refactoring wizard with the spartanization
   */
  public IMarkerResolution getFixWithPreview(final String s) {
    return new IMarkerResolution() {
      /**
       * a quickfix which opens a refactoring wizard with the spartanization
       *
       * @author Boris van Sosin <code><boris.van.sosin [at] gmail.com></code>
       *         (v2)
       */
      @Override public String getLabel() {
        return "Show spartanization preview";
      }
      @Override public void run(final IMarker m) {
        setMarker(m);
        try {
          new RefactoringWizardOpenOperation(new Wizard(Spartanization.this)).run(Display.getCurrent().getActiveShell(), "Spartan refactoring: " + Spartanization.this);
        } catch (final InterruptedException e) {
          e.printStackTrace();
        }
      }
    };
  }
  public final String getName() {
    return toString();
  }
  /**
   * @return the selection
   */
  public ITextSelection getSelection() {
    return selection;
  }
  /**
   * .
   *
   * @return True if there are Spartanizations which can be performed on the
   *         compilation unit.
   */
  public boolean haveSuggestions() {
    return countSuggestions() > 0;
  }
  /**
   * Performs the current Spartanization on the provided compilation unit
   *
   * @param cu
   *          the compilation to spartanize
   * @param pm
   *          progress monitor for long operations (could be
   *          {@link NullProgressMonitor} for light operations)
   * @return whether any changes have been made to the compilation unit
   * @throws CoreException
   *           exception from the <code>pm</code>
   */
  public boolean performRule(final ICompilationUnit cu, final IProgressMonitor pm) throws CoreException {
    pm.beginTask("Creating change for a single compilation unit...", 2);
    final TextFileChange textChange = new TextFileChange(cu.getElementName(), (IFile) cu.getResource());
    Source.set(cu.getPath(), textChange.getCurrentDocument(null).get());
    textChange.setTextType("java");
    final SubProgressMonitor spm = new SubProgressMonitor(pm, 1, SubProgressMonitor.SUPPRESS_SUBTASK_LABEL);
    textChange.setEdit(createRewrite((CompilationUnit) Make.COMPILIATION_UNIT.parser(cu).createAST(spm), spm).rewriteAST());
    boolean $ = false;
    if (textChange.getEdit().getLength() != 0) {
      textChange.perform(pm);
      $ = true;
    }
    pm.done();
    return $;
  }
  /**
   * @param pm
   *          a progress monitor in which to display the progress of the
   *          refactoring
   * @param m
   *          the marker for which the refactoring needs to run
   * @return a RefactoringStatus
   * @throws CoreException
   *           the JDT core throws it
   */
  public RefactoringStatus runAsMarkerFix(final IProgressMonitor pm, final IMarker m) throws CoreException {
    return innerRunAsMarkerFix(pm, m, false);
  }
  /**
   * @param m
   *          the marker to set for the refactoring
   */
  public final void setMarker(final IMarker marker) {
    this.marker = marker;
  }
  /**
   * @param s
   *          the selection to set
   */
  public void setSelection(final ITextSelection s) {
    selection = s;
  }
  @Override public String toString() {
    return "Spartanize";
  }
  private ASTRewrite createRewrite(final SubProgressMonitor pm, final CompilationUnit u, final IMarker m) {
    if (pm != null)
      pm.beginTask("Creating rewrite operation...", 1);
    final ASTRewrite $ = ASTRewrite.create(u.getAST());
    fillRewrite($, u, m);
    if (pm != null)
      pm.done();
    return $;
  }
  /**
   * creates an ASTRewrite, under the context of a text marker, which contains
   * the changes
   *
   * @param pm
   *          a progress monitor in which to display the progress of the
   *          refactoring
   * @param m
   *          the marker
   * @return an ASTRewrite which contains the changes
   */
  private final ASTRewrite createRewrite(final SubProgressMonitor pm, final IMarker m) {
    return createRewrite(pm, (CompilationUnit) MakeAST.COMPILIATION_UNIT.from(m, pm), m);
  }
  private List<ICompilationUnit> getUnits(final IProgressMonitor pm) throws JavaModelException {
    if (!isTextSelected())
      return retrieve.allCompilationUnits(compilationUnit != null ? compilationUnit : retrieve.currentCompilationUnit(), new SubProgressMonitor(pm, 1, SubProgressMonitor.SUPPRESS_SUBTASK_LABEL));
    final List<ICompilationUnit> $ = new ArrayList<>();
    $.add(compilationUnit);
    return $;
  }
  private RefactoringStatus innerRunAsMarkerFix(final IProgressMonitor pm, final IMarker m, final boolean preview) throws CoreException {
    marker = m;
    pm.beginTask("Running refactoring...", 2);
    scanCompilationUnitForMarkerFix(m, pm, preview);
    marker = null;
    pm.done();
    return new RefactoringStatus();
  }
  private final boolean isTextSelected() {
    return selection != null && !selection.isEmpty() && selection.getLength() != 0;
  }
  private void runAsManualCall(final IProgressMonitor pm) throws JavaModelException, CoreException {
    pm.beginTask("Checking preconditions...", 2);
    scanCompilationUnits(getUnits(pm), new SubProgressMonitor(pm, 1, SubProgressMonitor.SUPPRESS_SUBTASK_LABEL));
  }
  protected abstract ASTVisitor collect(final List<Suggestion> $, CompilationUnit u);
  protected abstract void fillRewrite(ASTRewrite r, CompilationUnit u, IMarker m);
  /**
   * @param u
   *          JD
   * @throws CoreException
   */
  protected void scanCompilationUnit(final ICompilationUnit u, final IProgressMonitor m) throws CoreException {
    m.beginTask("Creating change for a single compilation unit...", 2);
    final TextFileChange textChange = new TextFileChange(u.getElementName(), (IFile) u.getResource());
    Source.set(u.getPath(), textChange.getCurrentDocument(null).get());
    textChange.setTextType("java");
    final SubProgressMonitor subProgressMonitor = new SubProgressMonitor(m, 1, SubProgressMonitor.SUPPRESS_SUBTASK_LABEL);
    final CompilationUnit cu = (CompilationUnit) Make.COMPILIATION_UNIT.parser(u).createAST(subProgressMonitor);
    textChange.setEdit(createRewrite(cu, subProgressMonitor).rewriteAST());
    if (textChange.getEdit().getLength() != 0)
      changes.add(textChange);
    totalChanges += findOpportunities(cu).size();
    m.done();
  }
  protected void scanCompilationUnitForMarkerFix(final IMarker m, final IProgressMonitor pm, final boolean preview) throws CoreException {
    pm.beginTask("Creating change(s) for a single compilation unit...", 2);
    final ICompilationUnit u = MakeAST.iCompilationUnit(m);
    final TextFileChange textChange = new TextFileChange(u.getElementName(), (IFile) u.getResource());
    Source.set(u.getPath(), textChange.getCurrentDocument(null).get());
    textChange.setTextType("java");
    textChange.setEdit(createRewrite(new SubProgressMonitor(pm, 1, SubProgressMonitor.SUPPRESS_SUBTASK_LABEL), m).rewriteAST());
    if (textChange.getEdit().getLength() != 0)
      if (!preview)
        textChange.perform(pm);
      else
        changes.add(textChange);
    pm.done();
  }
  /**
   * Creates a change from each compilation unit and stores it in the changes
   * array
   *
   * @throws IllegalArgumentException
   * @throws CoreException
   */
  protected void scanCompilationUnits(final List<ICompilationUnit> us, final IProgressMonitor pm) throws IllegalArgumentException, CoreException {
    pm.beginTask("Iterating over gathered compilation units...", us.size());
    for (final ICompilationUnit u : us)
      scanCompilationUnit(u, new SubProgressMonitor(pm, 1, SubProgressMonitor.SUPPRESS_SUBTASK_LABEL));
    pm.done();
  }

  private final ICompilationUnit compilationUnit = null;
  private IMarker marker;
  private ITextSelection selection = null;
  private int totalChanges;
  final Collection<TextFileChange> changes = new ArrayList<>();
}