package il.org.spartan.athenizer;

import java.util.*;
import java.util.function.*;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.jdt.internal.ui.javaeditor.*;
import org.eclipse.jface.text.*;
import org.eclipse.jface.text.source.*;
import org.eclipse.ltk.core.refactoring.*;
import org.eclipse.swt.custom.*;
import org.eclipse.text.edits.*;
import org.eclipse.ui.texteditor.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.plugin.*;
import il.org.spartan.spartanizer.research.Matcher.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.spartanizer.traversal.*;
import il.org.spartan.utils.*;

/** A tool for committing a single change to a {@link CompilationUnit}.
 * @author Ori Roth {@code ori.rothh@gmail.com}
 * @since 2016-12-20 */
public final class SingleFlater {
  private ASTNode root;
  private OperationsProvider operationsProvider;
  private boolean usesDisabling = true;
  private WindowInformation windowInformation;
  private ITextSelection textSelection;

  /** Suppresses default constructor, ensuring non-instantiability */
  private SingleFlater() {}
  /** Creates a new for a {@link CompilationUnit}.
   * @param ¢ JD
   * @return new */
  public static SingleFlater in(final ASTNode ¢) {
    final SingleFlater $ = new SingleFlater();
    $.root = ¢;
    return $;
  }
  /** Sets {@link OperationProvider} for this flater.
   * @param ¢ JD
   * @return {@code this} flater */
  public SingleFlater from(final OperationsProvider ¢) {
    operationsProvider = ¢;
    return this;
  }
  /** Sets text selection limits of window to this flater.
   * @param ¢ JD
   * @return {@code this} flater */
  public SingleFlater limit(final WindowInformation ¢) {
    windowInformation = ¢;
    return this;
  }
  /** Sets text selection limits to this flater.
   * @param ¢ JD
   * @return {@code this} flater */
  public SingleFlater limit(final ITextSelection ¢) {
    textSelection = ¢;
    return this;
  }
  /** Set disabling for this flater.
   * @return {@code this} flater */
  public SingleFlater usesDisabling(final boolean ¢) {
    usesDisabling = ¢;
    return this;
  }
  protected <N extends ASTNode> Tipper<N> getTipper(final N n) {
    return robust.lyNull(() -> {
      final Tipper<N> $ = operationsProvider.getTipper(n);
      setTipper($);
      return $;
    }, λ -> note.bug(this, λ));
  }
  /** Main operation. Commit a single change to the {@link CompilationUnit}.
   * @param flaterChooser a {@link Function} to choose an {@link Operation} to
   *        make out of a collection of {@link Option}s.
   * @param r JD
   * @param g JD
   * @return true iff a change has been commited */
  @SuppressWarnings("rawtypes") public boolean go(final ASTRewrite r, final TextEditGroup g) {
    setRewrite(r);
    if (root == null || operationsProvider == null)
      return false;
    disabling.scan(root);
    final List<Operation<?>> operations = an.empty.list();
    root.accept(new DispatchingVisitor() {
      @Override @SuppressWarnings("synthetic-access") protected <N extends ASTNode> boolean go(final N n) {
        setNode(n);
        if (!inSelection(n) || usesDisabling && disabling.on(n))
          return true;
        final Tipper<N> w = getTipper(n);
        if (w == null)
          return true;
        operations.add(Operation.of(n, w));
        return true;
      }
    });
    if (operations.isEmpty())
      return false;
    final Boxer<Range> touched = new Boxer<>(new Range(-1, -1));
    for (final Operation ¢ : operationsProvider.getFunction().apply(operations))
      perform(¢, g, touched);
    return true;
  }
  @SuppressWarnings({ "rawtypes", "unchecked" }) void perform(final Operation o, final TextEditGroup g, final Boxer<Range> touched) {
    robust.ly(() -> {
      setTipper(o.tipper);
      setNode(o.node);
      o.tipper.check(o.node);
      setTip(o.tipper.tip(o.node));
      if (!Ranger.disjoint(tip().span, touched.inner))
        return;
      touched.inner = touched.inner.merge(tip().span);
      tip().go(rewrite(), g);
      notify.tipRewrite();
    }, λ -> note.bug(this, λ));
  }
  /** @param compoundEditing
   * @param wcu - the WrappedCompilationUnit which is worked on */
  public static boolean commitChanges(final SingleFlater f, final ASTRewrite r, final WrappedCompilationUnit u, final ISourceViewer v,
      final ITextEditor e, final WindowInformation i, final boolean compoundEditing) {
    boolean $ = false;
    if (compoundEditing)
      try {
        if (f.go(r, null)) {
          final TextEdit te = r.rewriteAST();
          if (te != null && te.getLength() > 0)
            $ = changeNFocus(e, v, te, i);
        }
      } catch (final CoreException | BadLocationException ¢) {
        note.bug(¢);
      }
    else
      try {
        final TextFileChange tfc = new TextFileChange(u.descriptor.getElementName(), (IFile) u.descriptor.getResource());
        tfc.setTextType("java");
        if (f.go(r, null)) {
          tfc.setEdit(r.rewriteAST());
          if (tfc.getEdit().getLength() != 0)
            $ = changeNFocus(e, v, tfc, i);
        }
      } catch (final CoreException ¢) {
        note.bug(¢);
      }
    u.dispose();
    return $;
  }
  /** @param from1 - starting char of first interval
   * @param lenth1 - length of first interval
   * @param from2 - starting char of second interval
   * @param length2 - length of second interval SPARTANIZED - should use Bloater
   *        one day to understand it */
  static boolean intervalsIntersect(final int from1, final int length1, final int from2, final int length2) {
    return length1 != 0 && length2 != 0
        && (from1 < from2 ? from1 + length1 > from2 : from1 != from2 ? from2 + length2 > from1 : length1 > 0 && length2 > 0);
  }
  private static boolean changeNFocus(final ITextEditor e, final ISourceViewer v, final TextEdit te, final WindowInformation i)
      throws MalformedTreeException, BadLocationException {
    if (i == null || v == null || e == null) {
      te.apply(Eclipse.document(e));
      return true;
    }
    te.apply(Eclipse.document(e));
    e.getSelectionProvider().setSelection(new TextSelection(te.getOffset(), te.getLength()));
    if (!i.invalid())
      v.setTopIndex(i.startLine);
    return false;
  }
  private static boolean changeNFocus(final ITextEditor e, final ISourceViewer v, final TextFileChange c, final WindowInformation i)
      throws CoreException {
    if (i == null || v == null || e == null) {
      c.perform(new NullProgressMonitor());
      return true;
    }
    c.perform(new NullProgressMonitor());
    e.getSelectionProvider().setSelection(new TextSelection(c.getEdit().getOffset(), c.getEdit().getLength()));
    if (!i.invalid())
      v.setTopIndex(i.startLine);
    return false;
  }
  private boolean inSelection(final ASTNode ¢) {
    final boolean inWindow = windowInformation == null || windowInformation.invalid()
        || ¢ != null && ¢.getStartPosition() >= windowInformation.startChar && ¢.getLength() + ¢.getStartPosition() <= windowInformation.endChar,
        inSelection = textSelection == null || ¢ != null && ¢.getStartPosition() >= textSelection.getOffset()
            && ¢.getLength() + ¢.getStartPosition() <= textSelection.getLength() + textSelection.getOffset();
    return inWindow && inSelection;
  }

  /** describes a single change operation, containing both an {@link ASTNode}
   * and a matching {@link Tipper}.
   * @param <N> JD
   * @author Ori Roth {@code ori.rothh@gmail.com}
   * @since 2016-12-20 */
  protected static final class Operation<N extends ASTNode> {
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
   * @author Ori Roth {@code ori.rothh@gmail.com}
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
      final ISourceViewer v = ((JavaEditor) e).getViewer();
      if (v == null) {
        invalidate();
        return;
      }
      startChar = v.getTopIndexStartOffset();
      endChar = v.getBottomIndexEndOffset();
      startLine = v.getTopIndex();
      endLine = v.getBottomIndex();
    }
    @Deprecated public WindowInformation(final StyledText ¢) {
      startLine = ¢.getTopIndex();
      endLine = JFaceTextUtil.getBottomIndex(¢);
      startChar = ¢.getOffsetAtLine(startLine);
      endChar = ¢.getOffsetAtLine(endLine);
    }
    public WindowInformation(final ISourceViewer ¢) {
      startLine = ¢.getTopIndex();
      endLine = ¢.getBottomIndex();
      startChar = ¢.getTopIndexStartOffset();
      endChar = ¢.getBottomIndexEndOffset();
    }
    @Deprecated public static WindowInformation of(final ITextEditor ¢) {
      return ¢ == null ? null : new WindowInformation(¢);
    }
    @Deprecated public static WindowInformation of(final StyledText ¢) {
      return ¢ == null ? null : new WindowInformation(¢);
    }
    public static WindowInformation of(final ISourceViewer ¢) {
      return ¢ == null ? null : new WindowInformation(¢);
    }
    public boolean invalid() {
      return startChar == INVALID;
    }
    public void invalidate() {
      startChar = INVALID;
    }
  }

  private Tipper<?> tipper;
  protected Tip tip;
  private ASTRewrite rewrite;
  private ASTNode node;
  public final Taps notify = new Taps()//
      .push(new SingleFlaterMonitor(this));

  void setTip(final Tip ¢) {
    tip = ¢;
    if (¢ != null)
      notify.tipperTip();
  }
  public void setNode(final ASTNode currentNode) {
    node = currentNode;
    notify.setNode();
  }
  public void setRewrite(final ASTRewrite currentRewrite) {
    rewrite = currentRewrite;
  }
  public void setTipper(final Tipper<?> currentTipper) {
    tipper = currentTipper;
    if (tipper() == null)
      notify.noTipper();
    else
      notify.tipperAccepts();
  }
  public ASTRewrite rewrite() {
    return rewrite;
  }
  public Tip tip() {
    return tip;
  }
  public ASTNode node() {
    return node;
  }
  public Tipper<?> tipper() {
    return tipper;
  }

  public interface Tap {
    /** @formatter:off */
    default void noTipper() {/**/}
    default void setNode()       {/**/}
    default void tipperAccepts() {/**/}
    default void tipperRejects() {/**/}
    default void tipperTip()     {/**/}
    default void tipPrune()      {/**/}
    default void tipRewrite()    {/**/}
    //@formatter:on
  }

  public static class Taps implements Tap {
    @Override public void noTipper() {
      inner.forEach(Tap::noTipper);
    }
    /** @formatter:off */
    public Taps pop() { inner.remove(inner.size()-1); return this; }
    public Taps push(final Tap ¢) { inner.add(¢); return this; }
    @Override public void setNode() { inner.forEach(Tap::setNode); }
    @Override public void tipperAccepts() { inner.forEach(Tap::tipperAccepts); }
    @Override public void tipperRejects() { inner.forEach(Tap::tipperRejects); }
    @Override public void tipperTip() { inner.forEach(Tap::tipperTip); }
    @Override public void tipPrune() { inner.forEach(Tap::tipPrune); }
    @Override public void tipRewrite() { inner.forEach(Tap::tipRewrite); }
    private final List<Tap> inner = new LinkedList<>();
    //@formatter:on
  }

  public abstract class With {
    public SingleFlater current() {
      return SingleFlater.this;
    }
  }
}
