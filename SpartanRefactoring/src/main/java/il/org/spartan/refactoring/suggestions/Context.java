package il.org.spartan.refactoring.suggestions;

import il.org.spartan.*;
import il.org.spartan.refactoring.utils.*;
import il.org.spartan.spreadsheet.*;
import il.org.spartan.utils.*;

import java.util.*;
import java.util.function.*;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;
import org.eclipse.jdt.annotation.*;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.jface.text.*;
import org.eclipse.ui.*;

import static il.org.spartan.refactoring.suggestions.DialogBoxes.*;
import static org.eclipse.core.runtime.IProgressMonitor.*;

/**
 * @author Yossi Gil
 *
 * @since 2016`
 */
public class Context implements Selfie<Context>, Cookbook {
  /** To be extended by clients */
  public abstract class Action {
    /** instantiates this class */
    public Action() {
      go();
    }
    /** Execute something within this context */
    protected abstract void go();

    /** the enclosing context */
    public final @NonNull Context context = Context.this;
  }

  /** @return the current {@link IWorkbenchWindow} */
  public static IWorkbenchWindow getCurrentWorkbenchWindow() {
    return PlatformUI.getWorkbench().getActiveWorkbenchWindow();
  }
  /**
   * factory method for this class,
   *
   * @return a new empty context
   */
  public static Context inContext() {
    return new Context();
  }
  private static ICompilationUnit getCompilationUnit(final IEditorPart ep) {
    return getCompilationUnit((IResource) ep.getEditorInput().getAdapter(IResource.class));
  }
  private static ICompilationUnit getCompilationUnit(final IFile f) {
    return f == null ? null : JavaCore.createCompilationUnitFrom(f);
  }
  private static ICompilationUnit getCompilationUnit(final IResource r) {
    return getCompilationUnit((IFile) r);
  }
  private Context() {
    // Keep it private
  }
  public List<@NonNull ASTNode> allNodes() {
    return allNodes.get();
  }
  /**
   * Returns an exact copy of this instance
   *
   * @return Created clone object
   */
  @SuppressWarnings("unchecked") @Override public Context clone() {
    try {
      return (Context) super.clone();
    } catch (final CloneNotSupportedException e) {
      throw new RuntimeException(e);
    }
  }
  /**
   * Compute a value within this context
   *
   * @param ¢
   *          JD
   * @return the computed value
   */
  @SuppressWarnings("static-method") public <T> T eval(final Provider<T> ¢) {
    return ¢.get();
  }
  public final void fillRewrite() {
    root().accept(new ASTVisitor() {
      @Override public boolean visit(final Block e) {
        return go(e);
      }
      @Override public boolean visit(final ConditionalExpression e) {
        return go(e);
      }
      @Override public boolean visit(final IfStatement s) {
        return go(s);
      }
      @Override public boolean visit(final InfixExpression e) {
        return go(e);
      }
      @Override public boolean visit(final PrefixExpression e) {
        return go(e);
      }
      @Override public boolean visit(final VariableDeclarationFragment f) {
        return go(f);
      }
      private <N extends ASTNode> boolean go(final N n) {
        if (applicable(n))
          return false;
        // inner.set(u).createScalpel(r, null).make(n).go(r, null);
        return true;
      }
    });
  }
  /** @return number of nodes in this {@link Context} */
  public int nodeCount() {
    return allNodes().size();
  }
  /**
   * @return the compilation unit of this instance
   */
  public ASTNode root() {
    return root.get();
  }
  /**
   * Evaluate a sequence of commands within this context
   *
   * @param ¢
   *          JD
   */
  @SuppressWarnings("static-method") public void run(final Action ¢) {
    ¢.go();
  }
  private ProgressVisitor collect(final List<Suggestion> $) {
    return new ProgressVisitor() {
      @Override public boolean visit(final Block ¢) {
        return process(¢);
      }
      @Override public boolean visit(final ConditionalExpression ¢) {
        return process(¢);
      }
      @Override public boolean visit(final IfStatement ¢) {
        return process(¢);
      }
      @Override public boolean visit(final InfixExpression ¢) {
        return process(¢);
      }
      @Override public boolean visit(final PrefixExpression ¢) {
        return process(¢);
      }
      @Override public boolean visit(final VariableDeclarationFragment ¢) {
        return process(¢);
      }
      <N extends ASTNode> boolean process(final N n) {
        if (!inner.set(root()).createScalpel(null, null).scopeIncludes(n) || inner.nonEligible(n))
          return true;
        $.add(inner.make(n));
        return true;
      }
    };
  }
  protected ProgressVisitor collector(final List<Suggestion> $) {
    return new ProgressVisitor() {
    };
  }
  /** @return List of all compilation units in the current project */
  List<ICompilationUnit> allCompilationUnits() {
    return allCompilationUnits.get();
  }
  /**
   * @param n
   *          the node which needs to be within the range of
   *          <code><b>m</b></code>
   * @return True if the node is within range
   */
  final boolean applicable(final ASTNode n) {
    return marker() != null ? !isMarked(n) : !hasSelection() || !notSelected(n);
  }
  /**
   * creates an ASTRewrite which contains the changes
   *
   * @return an ASTRewrite which contains the changes
   */
  ASTRewrite astRewrite() {
    progressMonitor().beginTask("Creating rewrite operation...", UNKNOWN);
    final ASTRewrite $ = ASTRewrite.create(root().getAST());
    rewrite($);
    progressMonitor().done();
    return $;
  }
  /**
   * Collects all compilation units from a given starting point
   *
   * @param u
   *          JD
   * @param $
   *          result
   * @return nothing
   */
  Void collectInto(final ICompilationUnit u, final Collection<ICompilationUnit> $) {
    progressMonitor().worked(1);
    if (u == null)
      return DialogBoxes.announce("Cannot find current compilation unit " + u);
    progressMonitor().worked(1);
    final IJavaProject j = u.getJavaProject();
    if (j == null)
      return announce("Cannot find project of " + u);
    progressMonitor().worked(1);
    final IPackageFragmentRoot[] rs = retrieve.roots(j);
    if (rs == null)
      return announce("Cannot find roots of " + j);
    progressMonitor().worked(1);
    return collectInto($, rs);
  }
  /**
   * TODO Javadoc(2016): automatically generated for method
   * <code>collectInto</code>
   *
   * @param $
   * @param rs
   *          void TODO Javadoc(2016) automatically generated for returned value
   *          of method <code>collectInto</code>
   */
  private Void collectInto(final Collection<ICompilationUnit> $, final IPackageFragmentRoot[] rs) {
    for (final IPackageFragmentRoot r : rs)
      try {
        progressMonitor().worked(1);
        if (r.getKind() != IPackageFragmentRoot.K_SOURCE)
          continue;
        progressMonitor().worked(1);
        for (final IJavaElement e : r.getChildren()) {
          progressMonitor().worked(1);
          if (e.getElementType() != IJavaElement.PACKAGE_FRAGMENT)
            break;
          $.addAll(as.list(((IPackageFragment) e).getCompilationUnits()));
          progressMonitor().worked(1);
        }
        progressMonitor().worked(1);
      } catch (final JavaModelException x) {
        x.printStackTrace();
        continue;
      }
  }
  @Nullable ICompilationUnit compilationUnitInterface() {
    return compilationUnit.get();
  }
  final boolean containedIn(final ASTNode n) {
    return range().includedIn(range(n));
  }
  final boolean hasSelection() {
    return selection() != null && !selection().isEmpty() && selection().getLength() != 0;
  }
  int intValue(final String propertyName) throws CoreException {
    return ((Integer) marker().getAttribute(propertyName)).intValue();
  }
  /**
   * determine whether a given node is included in the marker
   *
   * @param n
   * @return boolean whether a parameter is included in the marker
   *
   */
  boolean isMarked(final ASTNode n) {
    try {
      return n.getStartPosition() < intValue(IMarker.CHAR_START) || n.getLength() + n.getStartPosition() > intValue(IMarker.CHAR_END);
    } catch (final CoreException e) {
      e.printStackTrace();
      return true;
    }
  }
  boolean isSelected(final int offset) {
    return hasSelection() && offset >= selection().getOffset() && offset < selection().getLength() + selection().getOffset();
  }
  IMarker marker() {
    return marker.get();
  }
  /**
   * Determines if the node is outside of the selected text.
   *
   * @return true if the node is not inside selection. If there is no selection
   *         at all will return false.
   */
  boolean notSelected(final ASTNode n) {
    return !isSelected(n.getStartPosition());
  }
  final boolean outOfRange(final ASTNode n) {
    return marker() != null ? !containedIn(n) : !hasSelection() || !notSelected(n);
  }
  IProgressMonitor progressMonitor() {
    return progressMonitor;
  }
  Range range() {
    return range.get();
  }
  @SuppressWarnings("static-method") void run(final Runnable r) {
    r.run();
  }
  ITextSelection selection() {
    return selection.get();
  }
  @NonNull List<@NonNull Suggestion> suggestions() {
    return suggestions.get();
  }

  private IProgressMonitor progressMonitor;
  // );
  // // new Supplier<List<@NonNull ASTNode>>() {
  // };
  // );
  // return $;
  // final List<@NonNull ASTNode> $ = new ArrayList<@NonNull ASTNode>();
  // root().accept(new ASTVisitor() {
  // @Override public void preVisit(final ASTNode n) {
  // if (n != null)
  // $.add(n);
  // }
  // });
  // }
  @NonNull List<@NonNull ASTNode> $ = new ArrayList<>();
  final Recipe<ASTNode> root = new Recipe<>(//
      () -> Make.COMPILIATION_UNIT.parser(compilationUnitInterface()).createAST(progressMonitor())//
  );
  @SuppressWarnings("unused")//
  /** all nodes found under the current root */
  final Cell<List<@NonNull ASTNode>> allNodes = new Recipe<>(() -> {
    final List<@NonNull ASTNode> $ = new ArrayList<@NonNull ASTNode>();
    root().accept(new ASTVisitor() {
      @Override public void preVisit(final ASTNode n) {
        if (n != null)
          $.add(n);
      }
    });
    return $;
  }).ingredient(root);
  final Cell<Integer> countNodes = new Recipe<>(//
      () -> Integer.valueOf(allNodes().size())//
  ).ingredient(allNodes);
  final Cell<List<ICompilationUnit>> allCompilationUnits = new Cookbook.Recipe<>(//
      () -> {
        progressMonitor().beginTask("Collecting all project's compilation units...", 1);
        final List<ICompilationUnit> $ = new ArrayList<>();
        getCompilationUnits(progressMonitor(), compilationUnitInterface(), $);
        progressMonitor().done();
        return $;
      }).ingredient(root);
  /** current text selected or null */
  @SuppressWarnings("unused")//
  private @Nullable final Cell<ITextSelection> selection = new Ingredient<ITextSelection>(null);
  @SuppressWarnings("unused")//
  private final Cell<List<Suggestion>> suggestions = new Recipe<List<Suggestion>>(() -> {
    progressMonitor().beginTask("Gathering suggestions for ", nodeCount());
    final List<Suggestion> $ = new ArrayList<>();
    root().accept(collector($));
    progressMonitor().done();
    return $;
  });
  /** The current compilation unit */
  @SuppressWarnings("unused")//
  Cell<ICompilationUnit> currentCompilationUnit = //
  new Recipe<ICompilationUnit>(//
      () -> getCompilationUnit(getCurrentWorkbenchWindow().getActivePage().getActiveEditor())//
  );
  /** the current compilation unit */
  @SuppressWarnings("unused")//
  final Cell<ICompilationUnit> compilationUnit = //
  new Recipe<ICompilationUnit>(//
      () -> currentCompilationUnit.get()//
  ).ingredient(currentCompilationUnit);
  @SuppressWarnings("unused")//
  /** the current compilation unit */
  final @NonNull String description = null;
  final @Nullable Cell<IMarker> marker = Cookbook.ingredient();
  public final Cell<Range> range = new Recipe<>(() -> {
    try {
      return new Range(intValue(CHAR_START), intValue(CHAR_END));
    } catch (final CoreException e) {
      e.printStackTrace();
      return null;
    }
  }).ingredients(marker);

  /**
   * To be extended by clients
   *
   * @param <T>
   *          JD
   */
  public abstract class Provider<T> implements Supplier<T> {
    /** the enclosing context */
    public final @NonNull Context context = Context.this;
    // to be filled with clients
  }

  class ProgressVisitor extends ASTVisitor {
    @Override public void preVisit(final ASTNode __) {
      progressMonitor().worked(1);
    }
  }
}

/**
 * @author Yossi Gil
 * @param <Self>
 *          Type of current class, keep to the idiom <code> <pre> class X
 *          extends Context&lt;X&gt; { } </pre> </code<
 * @since 2016`
 */
interface Selfie<Self extends Selfie<Self>> {
  /**
   * a type correct version of <code><b>this</b></code>, as long as extending
   * classes keep using the idiom of extending this class
   *
   * @return <code><b>this</b></code> properly downcasted to
   *         <code>Context<Self></code>
   */
  @SuppressWarnings("unchecked") default Self self() {
    return (Self) this;
  }
}
