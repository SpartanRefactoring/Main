package il.org.spartan.athenizer.inflate;

import java.util.*;
import java.util.function.*;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.jdt.internal.core.dom.rewrite.*;
import org.eclipse.jdt.internal.ui.javaeditor.*;
import org.eclipse.jface.text.*;
import org.eclipse.jface.text.source.*;
import org.eclipse.jface.text.source.projection.*;
import org.eclipse.ltk.core.refactoring.*;
import org.eclipse.text.edits.*;
import org.eclipse.ui.texteditor.*;

import il.org.spartan.plugin.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.research.Matcher.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.spartanizer.utils.*;

/** A tool for committing a single change to a {@link CompilationUnit}.
 * @author Ori Roth <tt>ori.rothh@gmail.com</tt>
 * @since 2016-12-20 */
public class SingleFlater {
  private static final boolean SELECT_CHANGES = false;
  private CompilationUnit compilationUnit;
  private OperationsProvider operationsProvider;
  private TextSelection textSelection;

  private SingleFlater() {}

  /** Creates a new {@link SingleFlater} for a {@link CompilationUnit}.
   * @param ¢ JD
   * @return new {@link SingleFlater} */
  public static SingleFlater in(final CompilationUnit ¢) {
    final SingleFlater $ = new SingleFlater();
    $.compilationUnit = ¢;
    return $;
  }

  /** Sets {@link OperationProvider} for this flater.
   * @param ¢ JD
   * @return this flater */
  public SingleFlater from(final OperationsProvider ¢) {
    operationsProvider = ¢;
    return this;
  }

  /** Sets text selection limits for this flater.
   * @param ¢ JD
   * @return this flater */
  public SingleFlater limit(final TextSelection ¢) {
    textSelection = ¢;
    return this;
  }

  /** Main operation. Commit a single change to the {@link CompilationUnit}.
   * @param flaterChooser a {@link Function} to choose an {@link Operation} to
   *        make out of a collection of {@link Option}s.
   * @param r JD
   * @param g JD
   * @return true iff a change has been commited */
  @SuppressWarnings({ "unchecked", "rawtypes" }) public boolean go(final ASTRewrite r, final TextEditGroup g) {
    if (compilationUnit == null || operationsProvider == null)
      return false;
    final List<Operation<?>> operations = new LinkedList<>();
    disabling.scan(compilationUnit);
    compilationUnit.accept(new DispatchingVisitor() {
      @Override @SuppressWarnings("synthetic-access") protected <N extends ASTNode> boolean go(final N n) {
        if (!inRange(n) || disabling.on(n))
          return true;
        Tipper<N> w = null;
        try {
          w = operationsProvider.getTipper(n);
        } catch (final Exception ¢) {
          monitor.debug(this, ¢);
          monitor.log(¢);
        }
        if (w == null)
          return true;
        operations.add(Operation.of(n, w));
        return true;
      }
    });
    if (operations.isEmpty())
      return false;
    final Operation $ = operationsProvider.getFunction().apply(operations);
    try {
      $.tipper.tip($.node).go(r, g);
    } catch (final Exception ¢) {
      monitor.debug(this, ¢);
      monitor.log(¢);
    }
    return true;
  }

  /** @param wcu - the WrappedCompilationUnit which is worked on */
  static void commitChanges(final SingleFlater f, final ASTRewrite r, final WrappedCompilationUnit u, final ITextEditor e) {
    try {
      final TextFileChange textChange = new TextFileChange(u.descriptor.getElementName(), (IFile) u.descriptor.getResource());
      textChange.setTextType("java");
      if (f.go(r, null)) {
        textChange.setEdit(r.rewriteAST());
        if (textChange.getEdit().getLength() != 0)
          changeNFocus(e, textChange, u.compilationUnit);
      }
    } catch (final CoreException ¢) {
      monitor.log(¢);
    }
  }

  /** @param ¢ JD
   * @return true iff node is inside predeclared range */
  boolean inRange(final ASTNode ¢) {
    final int $ = ¢.getStartPosition();
    return textSelection == null || $ >= textSelection.getOffset() && $ < textSelection.getLength() + textSelection.getOffset();
  }

  /** @param startChar1 - starting char of first interval
   * @param lenth1 - length of first interval
   * @param startChar2 - starting char of second interval
   * @param length2 - length of second interval SPARTANIZED - should use
   *        Athenizer one day to understand it */
  static boolean intervalsIntersect(final int startChar1, final int length1, final int startChar2, final int length2) {
    return length1 != 0 && length2 != 0 && (startChar1 < startChar2 ? length1 + startChar1 > startChar2
        : startChar1 != startChar2 ? length2 + startChar2 > startChar1 : length1 > 0 && length2 > 0);
  }

  @SuppressWarnings("restriction") private static void changeNFocus(final ITextEditor e, final TextFileChange tc, final CompilationUnit u)
      throws CoreException {
    if (!(e instanceof CompilationUnitEditor)) {
      tc.perform(new NullProgressMonitor());
      return;
    }
    final ISourceViewer v = ((CompilationUnitEditor) e).getViewer();
    if (!(v instanceof ProjectionViewer)) {
      tc.perform(new NullProgressMonitor());
      return;
    }
    final ProjectionViewer pv = (ProjectionViewer) v;
    final LineInformation i = LineInformation.create(u);
    final int ob = pv.getBottomIndex(), ot = pv.getTopIndex(), cb = i.getLineOfOffset(tc.getEdit().getOffset() + tc.getEdit().getLength()),
        ct = i.getLineOfOffset(tc.getEdit().getOffset());
    tc.perform(new NullProgressMonitor());
    e.selectAndReveal(tc.getEdit().getOffset(), !SELECT_CHANGES ? 0 : tc.getEdit().getLength());
    if (pv.getTopIndex() != ot && ot <= ct && ob >= cb)
      pv.setTopIndex(ot);
  }

  /** describes a single change operation, containing both an {@link ASTNode}
   * and a matching {@link Tipper}.
   * @param <N> JD
   * @author Ori Roth <tt>ori.rothh@gmail.com</tt>
   * @since 2016-12-20 */
  protected static class Operation<N extends ASTNode> {
    public final N node;
    public final Tipper<N> tipper;

    private Operation(final N n, final Tipper<N> t) {
      node = n;
      tipper = t;
    }

    /** [[SuppressWarningsSpartan]] */
    public static <N extends ASTNode> Operation<N> of(final N node, final Tipper<N> tipper) {
      return new Operation<>(node, tipper);
    }
  }
}
