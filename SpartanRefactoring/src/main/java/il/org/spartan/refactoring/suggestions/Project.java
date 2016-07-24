package il.org.spartan.refactoring.suggestions;

import static org.eclipse.jdt.core.dom.ASTParser.*;
import il.org.spartan.*;
import static il.org.spartan.idiomatic.*;
import il.org.spartan.lazy.*;
import il.org.spartan.refactoring.preferences.*;
import il.org.spartan.refactoring.utils.*;
import il.org.spartan.refactoring.utils.Funcs.*;
import il.org.spartan.utils.*;
import il.org.spartan.lazy.Cookbook.Ingredient;
import il.org.spartan.lazy.Cookbook.Cell;

import static il.org.spartan.lazy.Cookbook.value;
import static il.org.spartan.lazy.Cookbook.recipe;
import static il.org.spartan.lazy.Cookbook.cook;
import static il.org.spartan.lazy.Cookbook.input;

import static org.eclipse.jdt.core.JavaCore.createCompilationUnitFrom;

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
@SuppressWarnings("javadoc")//
public class Project implements Selfie<Project>, Cookbook {
  // Values:
  final Cell<Integer> kind = value(Integer.valueOf(ASTParser.K_COMPILATION_UNIT));
  final Cell<String> description = value("");
  // Inputs:
  final Cell<IFile> iFile = input();
  final Cell<Document> document = input();
  final Cell<IMarker> marker = input();
  final Cell<ITextSelection> selection = input();
  // First Order Lazy values
  final Cell<IProgressMonitor> progressMonitor = cook(() -> new NullProgressMonitor());
  // Recipes:
  final Cell<ICompilationUnit> currentCompilationUnit = cook(() -> getCompilationUnit(currentWorkbenchWindow().getActivePage().getActiveEditor()));
  final Cell<ICompilationUnit> iCompilationUnit = cook(() -> iFile() == null ? null : createCompilationUnitFrom(iFile()));
  final Cell<ICompilationUnit> compilationUnit = cook(() -> currentCompilationUnit());
  final Cell<ASTNode> root = cook(() -> Make.COMPILIATION_UNIT.parser(compilationUnitInterface()).createAST(progressMonitor()));
  final Cell<String> text = cook(() -> document().get());
  final Cell<IWorkbench> iWorkbench = cook(() -> PlatformUI.getWorkbench());
  final Cell<IWorkbenchWindow> currentWorkbenchWindow = cook(() -> iWorkench().getActiveWorkbenchWindow());
  final Cell<char[]> array = cook(() -> text().toCharArray());
  final Cell<ASTParser> parser = cook(() -> {
    final ASTParser $ = parser();
    $.setSource(array.get());
    return $;
  });
  final Cell<List<@NonNull ASTNode>> allNodes = cook(() -> {
    final List<@NonNull ASTNode> $ = new ArrayList<>();
    root().accept(new ProgressVisitor() {
      @Override public void go(final ASTNode n) {
        $.add(n);
      }
    });
    return $;
  });
  final Cell<Range> range = cook(() -> computeRange());
  final Cell<List<Suggestion>> suggestions = recipe(() -> {
    progressMonitor().beginTask("Gathering suggestions for ", nodeCount());
    final List<Suggestion> $ = new ArrayList<>();
    root().accept(collector($));
    progressMonitor().done();
    return $;
  });
  final Cell<List<ICompilationUnit>> allCompilationUnits = cook(//
  () -> {
    progressMonitor().beginTask("Collecting all project's compilation units...", 1);
    final List<ICompilationUnit> $ = new ArrayList<>();
    collectInto(compilationUnitInterface(), $);
    progressMonitor().done();
    return $;
  });

  private Range computeRange() {
    try {
      return new Range(intValue(IMarker.CHAR_START), intValue(IMarker.CHAR_END));
    } catch (final CoreException x) {
      x.printStackTrace();
      return null;
    }
  }
  /**
   * factory method for this class,
   *
   * @return a new empty instance
   */
  public static Project inContext() {
    return new Project();
  }
  private static ICompilationUnit getCompilationUnit(final IEditorPart ep) {
    return getCompilationUnit((IResource) ep.getEditorInput().getAdapter(IResource.class));
  }
  private static ICompilationUnit getCompilationUnit(final IResource r) {
    return getCompilationUnit(r);
  }
  public Project() {
    // Keep it private
  }
  /**
   * Returns an exact copy of this instance
   *
   * @return Created clone object
   */
  @SuppressWarnings("unchecked") @Override public Project clone() {
    try {
      return (Project) super.clone();
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
        return !applicable(n); // inner.set(u).createScalpel(r,
                               // null).make(n).go(r, null);
      }
    });
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
  ProgressVisitor collect(final List<Suggestion> $) {
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
        if (inner.scopeIncludes(n) || inner.nonEligible(n))
          return true;
        $.add(inner.make(n));
        return true;
      }
    };
  }
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
    return null;
  }
  private ASTParser parser() {
    final ASTParser $ = ASTParser.newParser(AST.JLS8);
    $.setKind(kind());
    $.setResolveBindings(PluginPreferencesResources.getResolveBindingEnabled());
    return $;
  }
  int kind() {
    return kind.get().intValue();
  }
  protected ProgressVisitor collector(final List<Suggestion> $) {
    return new ProgressVisitor() {
      // TODO: make a collector
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
  @SuppressWarnings("static-method") void run(final Runnable r) {
    r.run();
  }

  /**
   * Creates a no-binding parser for a given compilation unit
   *
   * @param u
   *          what to parse
   * @return a newly created parser for the parameter
   */
  final Cell<ASTParser> parser1 = cook(() -> {
    final ASTParser $ = parser();
    $.setSource(compilationUnit.get());
    return $;
  });

  /** To be extended by clients */
  public abstract class Action {
    /** instantiates this class */
    public Action() {
      go();
    }
    /** Execute something within this context */
    protected abstract void go();

    /** the enclosing context */
    public final @NonNull Project context = Project.this;
  }

  /**
   * To be extended by clients
   *
   * @param <T>
   *          JD
   */
  public abstract class Provider<T> implements Supplier<T> {
    /** the enclosing context */
    public final @NonNull Project context = Project.this;
    // to be filled with clients
  }

  public abstract class ProgressVisitor extends ASTVisitor {
    @Override public final boolean preVisit2(ASTNode n) {
      return filter(n);
    }
    public boolean filter(ASTNode n) {
      return n != null;
    }
    @Override public final void preVisit(final ASTNode n) {
      progressMonitor().worked(1);
      go(n);
    }
    protected void go(ASTNode n) {
      /** empty by default */
    }
  }

  public abstract class CollectingVisitor<T> extends ProgressVisitor {
    protected List<T> collection = new ArrayList<>();

    protected final void go(ASTNode ¢) {
      go(transform(¢));
    }
    /**
     * TODO Javadoc(2016): automatically generated for method <code>go</code>
     *
     * @param ¢$
     *          void TODO Javadoc(2016) automatically generated for returned
     *          value of method <code>go</code>
     */
    private void go(T ¢) {
      idiomatic.eval(
          ()->{collection.add(¢)).unless(not(worthy(¢))); 
          }
          );
    }
    private boolean not(boolean b) {
      return !b;
    }
    private boolean worthy(T $) {
      return $ != null;
    }
    protected abstract T transform(ASTNode n);
  }

  // @formatter:off
  public IMarker marker() { return marker.get(); }
  public IProgressMonitor progressMonitor() { return progressMonitor.get(); }
  public ITextSelection selection() { return selection.get(); }
  public @Nullable ICompilationUnit currentCompilationUnit() { return currentCompilationUnit.get(); }
  public @NonNull List<il.org.spartan.refactoring.suggestions.Suggestion> suggestions() { return suggestions.get(); }
  public String text() { return text.get(); }
  public ASTNode root() { return root.get(); }
  public IWorkbenchWindow currentWorkbenchWindow() { return currentWorkbenchWindow.get(); }
  public IFile iFile() { return iFile.get(); }
  public Document document() { return document.get(); }
  public IWorkbench iWorkench() { return iWorkbench.get(); }
  public int nodeCount() { return allNodes().size(); }
  public List<ASTNode> allNodes() { return allNodes.get(); }
  public Range range() { return range.get(); }
  // @formatter:on
}
