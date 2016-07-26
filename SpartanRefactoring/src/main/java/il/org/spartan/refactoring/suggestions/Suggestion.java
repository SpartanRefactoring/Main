package il.org.spartan.refactoring.suggestions;

import static il.org.spartan.lazy.Cookbook.from;
import static il.org.spartan.lazy.Cookbook.cook;
import static il.org.spartan.lazy.Cookbook.input;
import static il.org.spartan.lazy.Cookbook.value;
import static il.org.spartan.lazy.Cookbook.recipe;

import il.org.spartan.*;

import java.util.*;

import org.eclipse.jdt.core.*;

import static il.org.spartan.refactoring.suggestions.DialogBoxes.*;
import il.org.spartan.lazy.*;
import il.org.spartan.refactoring.contexts.*;
import il.org.spartan.refactoring.utils.*;
import il.org.spartan.refactoring.wring.*;
import il.org.spartan.utils.*;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.ltk.core.refactoring.*;
import org.eclipse.text.edits.*;
import org.eclipse.ui.*;

import static org.eclipse.core.runtime.IProgressMonitor.*;

/**
 * A function object representing a sequence of operations on an
 * {@link ASTRewrite} object.
 *
 * @author Yossi Gil
 * @since 2015-08-28
 */
@SuppressWarnings("javadoc") //
public class Suggestion extends Described {
 public Wring<?> wring() { return wring.get(); }
 public ASTNode node() { return node.get(); }
  static Range range(final Range r, final ASTNode... ns) {
    Range $ = r;
    for (final ASTNode n : ns)
      $ = $.merge(singleNodeRange(n));
    return $;
  }
  static Range singleNodeRange(final ASTNode n) {
    final int from = n.getStartPosition();
    return new Range(from, from + n.getLength());
  }
  /**
   * Instantiates this class
   *
   * @param description
   *          a textual description of the changes described by this instance
   * @param n
   *          the node on which change is to be carried out
   * @param ns
   *          additional nodes, defining the scope of this action.
   */
  public Suggestion(final String description, final ASTNode n, final ASTNode... ns) {
    this(range(n, ns));
    lineNumber = ((CompilationUnit) AncestorSearch.forClass(CompilationUnit.class).from(n)).getLineNumber(from);
  }
  public final Change createChange() throws OperationCanceledException {
    return new CompositeChange("" + this, changes.toArray(new Change[changes.size()]));
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
          fix(new NullProgressMonitor(), m);
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
   * Convert the rewrite into changes on an {@link ASTRewrite}
   *
   * @param r
   *          where to place the changes
   * @param g
   *          to be associated with these changes
   */
  public void go(final ASTRewrite r, final TextEditGroup g) {
  }
  /** @return current lineNumber */
  public int lineNumber() {
    return lineNumber.get().intValue();
  }
  public Suggestion of(final Range ¢) {
    set(¢);
    return self();
  }
  /** sets this instance description */
  public void of(final String s) {
    super.description.set(s);
  }
  public Suggestion of(final Wring<?> ¢) {
    wring.set(¢);
    return self();
  }
  /**
   * Performs the current Spartanization on the provided compilation unit
   *
   * @param cu
   *          the compilation to spartanize
   * @param progressMonitor
   *          progress monitor for long operations (could be
   *          {@link NullProgressMonitor} for light operations)
   * @return whether any changes have been made to the compilation unit
   * @throws CoreException
   *           exception from the <code>pm</code>
   */
  public boolean performRule() throws CoreException {
    progressMonitor().beginTask("Creating change for a single compilation unit...", 2);
    final TextFileChange textChange = textChange(cu);
    Source.set(cu.getPath(), textChange.getCurrentDocument(null).get());
    textChange.setTextType("java");
    final SubProgressMonitor spm = new SubProgressMonitor(progressMonitor, 1, SubProgressMonitor.SUPPRESS_SUBTASK_LABEL);
    textChange.setEdit(from((CompilationUnit) Make.COMPILIATION_UNIT.parser(cu).createAST(spm)).with(spm).rewriteAST());
    boolean $ = false;
    if (textChange.getEdit().getLength() != 0) {
      textChange.perform(progressMonitor());
      $ = true;
    }
    progressMonitor().done();
    return $;
  }
  @Override public Suggestion self() {
    return this;
  }
  public void set(final Range r) {
    range.set(r);
  }
  /** @return a textual description of this instance */
  @Override public String toString() {
    return description();
  }
  /**
   * Performs the current Spartanization on the provided compilation unit
   *
   * @param cu
   *          the compilation to spartanize
   * @return whether any changes have been made to the compilation unit
   * @throws CoreException
   *           exception from the <code>pm</code>
   */
  boolean performRule(final ICompilationUnit cu) {
    progressMonitor().beginTask("Creating change for a single compilation unit...", UNKNOWN);
    final TextFileChange textChange = textChange(cu);
    Source.set(cu.getPath(), textChange.getCurrentDocument(null).get());
    textChange.setTextType("java");
    final SubProgressMonitor spm = new SubProgressMonitor(progressMonitor, 1, SubProgressMonitor.SUPPRESS_SUBTASK_LABEL);
    textChange.setEdit(inContext().set((CompilationUnit) Make.COMPILIATION_UNIT.parser(cu).createAST(spm)).set(spm).rewriteAST());
    boolean $ = false;
    if (textChange.getEdit().getLength() != 0) {
      textChange.perform(progressMonitor());
      $ = true;
    }
    progressMonitor().done();
    return $;
  }
  void set(final int lineNumber) {
    this.lineNumber.set(Integer.valueOf(lineNumber));
  }

  final Cell<Integer> lineNumber =  input();
  final Cell<Wring<?>> wring =  input();
  final Cell<ASTNode> node = input();
  final Cell<Range> range = cook(() -> Funcs.range(node()));
}
