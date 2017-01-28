package il.org.spartan.bloater;

import java.util.*;
import java.util.function.*;

import il.org.spartan.plugin.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.research.Matcher.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.spartanizer.utils.*;

/** A tool for committing a single change to a {@link CompilationUnit}.
 * @author Ori Roth <tt>ori.rothh@gmail.com</tt>
 * @since 2016-12-20 */
public class SingleFlater {
  private ASTNode root;
  private OperationsProvider operationsProvider;
  @Deprecated private TextSelection textSelection;
  private boolean usesDisabling = true;
  private WindowInformation windowInformation;

  private SingleFlater() {}

  /** Creates a new {@link SingleFlater} for a {@link CompilationUnit}.
   * @param ¢ JD
   * @return new {@link SingleFlater} */
  public static SingleFlater in(final ASTNode ¢) {
    final SingleFlater $ = new SingleFlater();
    $.root = ¢;
    return $;
  }

  /** Sets {@link OperationProvider} for this flater.
   * @param ¢ JD
   * @return <code><b>this</b></code> flater */
  public SingleFlater from(final OperationsProvider ¢) {
    operationsProvider = ¢;
    return this;
  }

  /** Sets text selection limits for this flater.
   * @param ¢ JD
   * @return <code><b>this</b></code> flater */
  @Deprecated public SingleFlater limit(final TextSelection ¢) {
    textSelection = ¢;
    return this;
  }

  /** Sets text selection limits of window to this flater.
   * @param ¢ JD
   * @return <code><b>this</b></code> flater */
  public SingleFlater limit(final WindowInformation ¢) {
    windowInformation = ¢;
    return this;
  }

  /** Set disabling for this flater.
   * @return <code><b>this</b></code> flater */
  public SingleFlater usesDisabling(final boolean ¢) {
    usesDisabling = ¢;
    return this;
  }

  /** Main operation. Commit a single change to the {@link CompilationUnit}.
   * @param flaterChooser a {@link Function} to choose an {@link Operation} to
   *        make out of a collection of {@link Option}s.
   * @param r JD
   * @param g JD
   * @return true iff a change has been commited */
  @SuppressWarnings({ "unchecked", "rawtypes" }) public boolean go(final ASTRewrite r, final TextEditGroup g) {
    if (root == null || operationsProvider == null)
      return false;
    final List<Operation<?>> operations = new LinkedList<>();
    disabling.scan(root);
    root.accept(new DispatchingVisitor() {
      @Override @SuppressWarnings("synthetic-access") protected <N extends ASTNode> boolean go(final N n) {
        if (!inWindow(n) || usesDisabling && disabling.on(n))
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
    final List<Operation<?>> $ = operationsProvider.getFunction().apply(operations);
    for (final Operation o : $)
      try {
        o.tipper.tip(o.node).go(r, g);
      } catch (final Exception ¢) {
        monitor.debug(this, ¢);
        monitor.log(¢);
      }
    return true;
  }

  /** @param wcu - the WrappedCompilationUnit which is worked on */
  public static boolean commitChanges(final SingleFlater f, final ASTRewrite r, final WrappedCompilationUnit u, final StyledText t,
      final ITextEditor e, final WindowInformation i) {
    boolean $ = false;
    try {
      final TextFileChange textChange = new TextFileChange(u.descriptor.getElementName(), (IFile) u.descriptor.getResource());
      textChange.setTextType("java");
      if (f.go(r, null)) {
        textChange.setEdit(r.rewriteAST());
        if (textChange.getEdit().getLength() != 0)
          $ = changeNFocus(e, t, textChange, i);
      }
    } catch (final CoreException ¢) {
      monitor.log(¢);
    }
    u.dispose();
    return $;
  }

  /** @param ¢ JD
   * @return true iff node is inside predeclared range */
  @Deprecated boolean inRange(final ASTNode ¢) {
    final int $ = ¢.getStartPosition();
    return textSelection == null || $ >= textSelection.getOffset() && $ < textSelection.getLength() + textSelection.getOffset();
  }

  /** @param startChar1 - starting char of first interval
   * @param lenth1 - length of first interval
   * @param startChar2 - starting char of second interval
   * @param length2 - length of second interval SPARTANIZED - should use Bloater
   *        one day to understand it */
  static boolean intervalsIntersect(final int startChar1, final int length1, final int startChar2, final int length2) {
    return length1 != 0 && length2 != 0 && (startChar1 < startChar2 ? length1 + startChar1 > startChar2
        : startChar1 != startChar2 ? length2 + startChar2 > startChar1 : length1 > 0 && length2 > 0);
  }

  private static boolean changeNFocus(final ITextEditor e, final StyledText t, final TextFileChange tc, final WindowInformation i)
      throws CoreException {
    if (i == null || t == null || e == null) {
      tc.perform(new NullProgressMonitor());
      return true;
    }
    tc.perform(new NullProgressMonitor());
    e.getSelectionProvider().setSelection(new TextSelection(tc.getEdit().getOffset(), tc.getEdit().getLength()));
    if (!i.invalid())
      t.setTopIndex(i.startLine);
    return false;
  }

  private boolean inWindow(final ASTNode ¢) {
    return windowInformation == null || windowInformation.invalid()
        || ¢ != null && ¢.getStartPosition() >= windowInformation.startChar && ¢.getLength() + ¢.getStartPosition() <= windowInformation.endChar;
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

    public static <N extends ASTNode> Operation<N> of(final N node, final Tipper<N> t) {
      return new Operation<>(node, t);
    }
  }

  /** Contains information about the current window
   * @author Ori Roth <tt>ori.rothh@gmail.com</tt>
   * @since 2017-01-10 */
  protected static class WindowInformation {
    private static final int INVALID = -1;
    public int startChar;
    public int endChar;
    public int startLine;
    public int endLine;

    @Deprecated @SuppressWarnings("restriction") private WindowInformation(final ITextEditor e) {
      if (!(e instanceof CompilationUnitEditor)) {
        invalidate();
        return;
      }
      final ISourceViewer v = ((CompilationUnitEditor) e).getViewer();
      if (v == null) {
        invalidate();
        return;
      }
      startChar = v.getTopIndexStartOffset();
      endChar = v.getBottomIndexEndOffset();
      startLine = v.getTopIndex();
      endLine = v.getBottomIndex();
    }

    public WindowInformation(final StyledText ¢) {
      startLine = ¢.getTopIndex();
      endLine = JFaceTextUtil.getBottomIndex(¢);
      startChar = ¢.getOffsetAtLine(startLine);
      endChar = ¢.getOffsetAtLine(endLine);
    }

    @Deprecated public static WindowInformation of(final ITextEditor ¢) {
      return new WindowInformation(¢);
    }

    public static WindowInformation of(final StyledText ¢) {
      return new WindowInformation(¢);
    }

    public boolean invalid() {
      return startChar == INVALID;
    }

    public void invalidate() {
      startChar = INVALID;
    }
  }
}
