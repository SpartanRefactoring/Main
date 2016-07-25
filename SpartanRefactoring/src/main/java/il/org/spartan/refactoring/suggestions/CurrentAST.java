package il.org.spartan.refactoring.suggestions;

import static org.eclipse.jdt.core.dom.ASTParser.*;
import il.org.spartan.*;
import il.org.spartan.idiomatic.Producer;

import static il.org.spartan.idiomatic.run;
import static il.org.spartan.idiomatic.take;
import static il.org.spartan.idiomatic.Producer.katching;

import il.org.spartan.lazy.*;
import il.org.spartan.refactoring.preferences.*;
import il.org.spartan.refactoring.utils.*;
import il.org.spartan.refactoring.utils.Funcs.*;
import il.org.spartan.refactoring.wring.*;
import il.org.spartan.utils.*;
import il.org.spartan.lazy.Cookbook.Ingredient;
import il.org.spartan.lazy.Cookbook.Cell;

import static il.org.spartan.lazy.Cookbook.*;

import static il.org.spartan.idiomatic.*;

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
import static org.eclipse.jdt.core.JavaCore.*;

/**
 * @author Yossi Gil
 *
 * @since 2016`
 */
@SuppressWarnings("javadoc")//
public class CurrentAST extends CurrentCompilationUnit implements Selfie<CurrentAST> {
  // @formatter:off
  public ASTNode root() { return root.get(); }
  public char[] array() { return array.get(); }
  public Document document() { return document.get(); }
  public IMarker marker() { return marker.get(); }
  public int kind() { return kind.get().intValue(); }
  public int nodeCount() { return allNodes().size(); }
  public ITextSelection selection() { return selection.get(); }
  public List<ASTNode> allNodes() { return allNodes.get(); }
  public List<Suggestion> suggestions() { return suggestions.get(); }
  public Range range() { return range.get(); }
  public String text() { return text.get(); }
  // @formatter:on

  // Values:
  final Cell<Integer> kind = value(ASTParser.K_COMPILATION_UNIT);
  // Inputs:
  final Cell<Document> document = input();
  final Cell<IMarker> marker = input();
  final Cell<ITextSelection> selection = input();
  // Lazy values
  final Cell<?> toolbox = from().make(() -> new Toolbox());
  // Recipes:
  final Cell<ASTNode> root = cook(() -> Make.COMPILIATION_UNIT.parser(compilationUnit()).createAST(progressMonitor()));
  final Cell<String> text = cook(() -> document().get());
  final Cell<char[]> array = cook(() -> text().toCharArray());
  final Cell<ASTParser> parser = from(array, kind).make(() -> {
    final ASTParser $ = ASTParser.newParser(AST.JLS8);
    $.setKind(kind());
    $.setResolveBindings(PluginPreferencesResources.getResolveBindingEnabled());
    $.setSource(array());
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
  final Cell<List<Suggestion>> suggestions = from(toolbox, root, allNodes).make(() -> {
    begin("Searching for suggestions...", nodeCount());
    final List<Suggestion> $ = new ArrayList<>();
    root().accept(new TransformAndPrune<Suggestion>($) {
      /** Simply return null by default */
      @Override protected Suggestion transform(@SuppressWarnings("unused") ASTNode __) {
        return null;
      }
    });
    end();
    return $;
  });


  private Range computeRange() {
    return katching<Range>(()-> new Range(intValue(IMarker.CHAR_START), intValue(IMarker.CHAR_END)));
  }
  /**
   * factory method for this class,
   *
   * @return a new empty instance
   */
  public static CurrentAST inContext() {
    return new CurrentAST();
  }

  private CurrentAST() {
    // Keep it private
  }
  /**
   * Returns an exact copy of this instance
   *
   * @return Created clone object
   */
  @SuppressWarnings("unchecked") @Override public CurrentAST clone() {
    try {
      return (CurrentAST) super.clone();
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
  @SuppressWarnings("static-method") public void activate(final Action ¢) {
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
    };
  }

  protected ProgressVisitor computeSuggestions(Wring<N> w) {
    return new TransformAndPrune<Suggestion>() {
      @Override protected Suggestion transform(ASTNode n) {
        if (w.scopeIncludes(n) || w.nonEligible(n))
          return null;
        return w.make(n);
      }
    };
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
  private Void getCompilationUnits() throws JavaModelException {
    if (super.compilationUnit() == null)
      return announce("Cannot find current compilation unit " + u);
    final IJavaProject javaProject = u.getJavaProject();
    if (javaProject() == null)
      return announce("Cannot find project of " + u);
    final IPackageFragmentRoot[] rs = javaProject.getPackageFragmentRoots();
    if (rs == null)
      return announce("Cannot find roots of " + javaProject);
    for (final IPackageFragmentRoot r : rs) {
      work();
      if (r.getKind() != IPackageFragmentRoot.K_SOURCE)
        break;
      work();
      for (final IJavaElement e : r.getChildren()) {
        work();
        if (e.getElementType() != IJavaElement.PACKAGE_FRAGMENT)
          break;
        $.addAll(Arrays.asList(((IPackageFragment) e).getCompilationUnits()));
        work();
      }
      work();
    }
    return null;
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
  final boolean containedIn(final ASTNode n) {
    return range().includedIn(Funcs.range(n));
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
   *          JD
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
  @SuppressWarnings("static-method") void exec(final Runnable r) {
    r.run();
  }

  /** To be extended by clients */
  public abstract class Action {
    /** instantiates this class */
    public Action() {
      go();
    }
    /** Execute something within this context */
    protected abstract void go();

    /** the enclosing context */
    public final @NonNull CurrentAST context = CurrentAST.this;
  }

  /**
   * To be extended by clients
   *
   * @param <T>
   *          JD
   */
  public abstract class Provider<T> implements Supplier<T> {
    /** the enclosing context */
    public final @NonNull CurrentAST context = CurrentAST.this;
    // to be filled with clients
  }

  public abstract class ProgressVisitor extends ASTVisitor {
    @Override public final boolean preVisit2(final ASTNode n) {
      return filter(n);
    }
    public boolean filter(final ASTNode n) {
      return n != null;
    }
    @Override public final void preVisit(final ASTNode n) {
      progressMonitor().worked(1);
      go(n);
    }
    protected void go(final ASTNode n) {
      /** empty by default */
    }
  }

  abstract class TransformAndPrune<T> extends ProgressVisitor {
    /** this is where we collect what's {@link #worthy(Object)} */
    protected final List<T> pruned;

    TransformAndPrune() {
      this(new ArrayList<>());
    }
    TransformAndPrune(List<T> pruned) {
      this.pruned = pruned;
    }
    @Override protected final void go(final ASTNode ¢) {
      go(transform(¢));
    }
    private void go(final T ¢) {
      run(() -> {
        pruned.add(¢);
      }).unless(not(worthy(¢)));
    }
    private boolean not(final boolean b) {
      return !b;
    }
    /**
     * determine whether a product of {@link #transform(ASTNode)} is worthy of
     * collecting
     *
     * @param ¢
     *          JD
     * @return true iff the parameter is worthy; by default all products which
     *         are not null are worthy; clients may override.
     */
    protected boolean worthy(final T ¢) {
      return ¢ != null;
    }
    /**
     * to be implemented by client: a function to convert nodes to a given type.
     *
     * @param n
     * @return T TODO Javadoc(2016) automatically generated for returned value
     *         of method <code>transform</code>
     */
    protected abstract T transform(ASTNode n);
  }
}