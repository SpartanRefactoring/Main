package il.org.spartan.refactoring.suggestions;

import il.org.spartan.refactoring.utils.*;

import java.util.*;

import org.eclipse.core.runtime.*;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.ltk.core.refactoring.*;

import static org.eclipse.core.runtime.IProgressMonitor.*;

class Operations<Extender extends Operations<?>> extends Project<Operations<Extender>> {
  public void scanCompilationUnitForMarkerFix(final boolean preview) throws CoreException {
    progressMonitor().beginTask("Creating change(s) for a single compilation unit...", UNKNOWN);
    final ICompilationUnit u = ast.iCompilationUnit(marker());
    final TextFileChange textChange = textChange(root());
    Source.set(u.getPath(), textChange.getCurrentDocument(null).get());
    textChange.setTextType("java");
    textChange.setEdit(astRewrite(new SubProgressMonitor(progressMonitor(), 1, SubProgressMonitor.SUPPRESS_SUBTASK_LABEL), progressMonitor()).rewriteAST());
    if (textChange.getEdit().getLength() != 0)
      if (preview)
        changes.add(textChange);
      else
        textChange.perform(progressMonitor());
    progressMonitor().done();
  }
  RefactoringStatus innerRunAsMarkerFix(final boolean preview) throws CoreException {
    progressMonitor().beginTask("Running refactoring...", UNKNOWN);
    scanCompilationUnitForMarkerFix(preview);
    progressMonitor().done();
    return new RefactoringStatus();
  }
  void runAsManualCall() throws JavaModelException, CoreException {
    progressMonitor().beginTask("Checking preconditions...", UNKNOWN);
    scanCompilationUnits(allCompilationUnits(), new SubProgressMonitor(progressMonitor(), 1, SubProgressMonitor.SUPPRESS_SUBTASK_LABEL));
    progressMonitor().done();
  }
  /**
   * Count the number files of that would change after Spartanization. <p> This
   * is a slow operation. Do not call lightheadedly.
   *
   * @return the total number of files with suggestions
   */
  int countFilesChanges() {
    try {
      checkFinalConditions();
    } catch (final OperationCanceledException e) {
      // Ignore
    } catch (final CoreException e) {
      e.printStackTrace();
    }
    return changes.size();
  }
  @SuppressWarnings("static-method") String go() {
    return null;
  }
  /**
   * Creates a change from each compilation unit and stores ¢ in the changes
   * array
   *
   * @throws IllegalArgumentException
   * @throws CoreException
   */
  public void scanCompilationUnits(final List<ICompilationUnit> us) throws IllegalArgumentException, CoreException {
    progressMonitor().beginTask("Iterating over gathered compilation units...", us.size());
    for (final ICompilationUnit u : us)
      scanCompilationUnit(u, new SubProgressMonitor(progressMonitor(), 1, SubProgressMonitor.SUPPRESS_SUBTASK_LABEL));
    progressMonitor().done();
  }
  /**
   * @param u
   *          JD
   * @throws CoreException
   */
  public void scanCompilationUnit() throws CoreException {
    progressMonitor().beginTask("Creating change for a single compilation unit...", 2);
    final TextChange textChange = inContext().set(compilationUnitInteface.getPath()).Source.set(u.getPath(), textChange.getCurrentDocument(null).get());
    textChange.setTextType("java");
    final SubProgressMonitor subProgressMonitor = new SubProgressMonitor(progressMonitor(), 1, SubProgressMonitor.SUPPRESS_SUBTASK_LABEL);
    final CompilationUnit cu = (CompilationUnit) Make.COMPILIATION_UNIT.parser(u).createAST(subProgressMonitor);
    textChange.setEdit(set(cu).set(subProgressMonitor).rewriteAST());
    if (textChange.getEdit().getLength() != 0)
      changes.add(textChange);
    totalChanges += suggestions().size();
    progressMonitor().done();
  }
}