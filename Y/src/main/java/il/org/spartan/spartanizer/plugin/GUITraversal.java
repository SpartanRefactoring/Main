package il.org.spartan.spartanizer.plugin;

import static il.org.spartan.plugin.old.eclipse.*;
import static il.org.spartan.utils.fault.*;

import java.util.*;

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

import fluent.ly.*;
import il.org.spartan.plugin.old.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.traversal.*;
import il.org.spartan.utils.*;

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
    final ASTRewrite r = go(u.build().compilationUnit);
    try {
      textChange.setEdit(r.rewriteAST());
      if (textChange.getEdit().getLength() != 0)
        textChange.perform(getProgressMonitor());
    } catch (final CoreException ¢) {
      note.bug(this, ¢);
    } catch (final AssertionError x) {
      note.bug(dump() + //
          "\n x=" + x + //
          "\n $=" + traversal.rewriteCount() + //
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
    return traversal.rewriteCount();
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

  public int go() {
    getProgressMonitor().beginTask("Creating change for " + compilationUnitName(), IProgressMonitor.UNKNOWN);
    final TextFileChange textChange = textFileChange();
    final ASTParser parser = make.COMPILATION_UNIT.parser(iCompilationUnit);
    assert parser != null;
    final CompilationUnit u = (CompilationUnit) parser.createAST(newSubProgressMonitor());
    assert u != null;
    try {
      textChange.setEdit(go(u).rewriteAST());
    } catch (JavaModelException | IllegalArgumentException $) {
      return zero.forgetting(note.bug($));
    }
    if (textChange.getEdit().getLength() != 0)
      try {
        textChange.perform(newSubProgressMonitor());
      } catch (final CoreException $) {
        return zero.forgetting(note.bug($));
      }
    getProgressMonitor().done();
    return traversal.rewriteCount();
  }

  private IProgressMonitor newSubProgressMonitor() {
    return eclipse.newSubMonitor(getProgressMonitor());
  }

  /** @param iCompilationUnit the compilationUnit to set
   * @return */
  public GUITraversal iCompilationUnit(final ICompilationUnit ¢) {
    iCompilationUnit = ¢;
    return self();
  }

  public int run(final ICompilationUnit $, final ITextSelection s) {
    try {
      iCompilationUnit($);
      setSelection(s);
      getProgressMonitor().beginTask("Creating change for a single compilation unit...", IProgressMonitor.UNKNOWN);
      final TextFileChange textChange = new TextFileChange($.getElementName(), (IFile) $.getResource());
      textChange.setTextType("java");
      final IProgressMonitor m = newSubProgressMonitor();
      final ASTParser p = make.COMPILATION_UNIT.parser($);
      assert p != null;
      final CompilationUnit u = az.compilationUnit(p.createAST(m));
      assert u != null;
      final ASTRewrite r = go(u);
      assert r != null;
      textChange.setEdit(r.rewriteAST());
      if (textChange.getEdit().getLength() != 0)
        textChange.perform(newSubProgressMonitor());
      getProgressMonitor().done();
    } catch (final CoreException ¢) {
      note.bug(this, ¢);
    }
    return traversal.rewriteCount();
  }

  /** @param pm a progress monitor in which to display the progress of the
   *        refactoring
   * @param ¢ the marker for which the refactoring needs to system
   * @return a RefactoringStatus
   * @throws CoreException the JDT core throws it */
  public RefactoringStatus runAsMarkerFix(final IMarker ¢) throws CoreException {
    return innerRunAsMarkerFix(¢, false);
  }

  @Override public GUITraversal self() {
    return this;
  }

  public GUITraversal setMarker(final IMarker ¢) {
    traversal.setRange(Ranger.make(¢));
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
    return setSelection(Ranger.make(¢));
  }

  private GUITraversal setSelection(final Range ¢) {
    traversal.setRange(¢);
    return this;
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
    return compilationUnits(iCompilationUnit != null ? iCompilationUnit : currentCompilationUnit(), newSubMonitor(getProgressMonitor()));
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

  private ASTRewrite go(final CompilationUnit ¢) {
    note.logger.fine("Weaving maximal rewrite of " + ¢);
    getProgressMonitor().beginTask("Weaving maximal rewrite ...", IProgressMonitor.UNKNOWN);
    final ASTRewrite $ = traversal.go(¢);
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
    return traversal.rewriteCount();
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
      scanCompilationUnit(¢, newSubProgressMonitor());
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

  public final Traversal traversal = new TraversalImplementation().push((TraversalTickingTapper) λ -> getProgressMonitor().worked(λ))
      .push(new TraversalTapper() {
        @Override public void begin() {
          TraversalTapper.super.begin();
        }

        @Override public void end() {
          TraversalTapper.super.end();
        }
      });
  private final Collection<TextFileChange> changes = an.empty.list();
  private ICompilationUnit iCompilationUnit;
  private IMarker marker;
  private String name = "Spartanizing";
  private IProgressMonitor progressMonitor = wizard.nullProgressMonitor;
  private int totalTips;
}