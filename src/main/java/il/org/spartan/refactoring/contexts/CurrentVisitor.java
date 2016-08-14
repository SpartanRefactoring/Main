package il.org.spartan.refactoring.contexts;

import static il.org.spartan.lazy.Environment.*;

import java.util.*;

import org.eclipse.jdt.annotation.*;
import org.eclipse.jdt.core.dom.*;

import il.org.spartan.lazy.Environment.*;
import il.org.spartan.refactoring.suggestions.*;
import il.org.spartan.refactoring.wring.*;

/** @author Yossi Gil
 * @since 2016` */
@SuppressWarnings("javadoc") //
public class CurrentVisitor extends CurrentRoot.¢ {
  /** instantiates this class */
  public CurrentVisitor(CurrentRoot ¢) {
    ¢.super();
  }
  /** Returns an exact copy of this instance
   * @return Created clone object */
  @Override public CurrentVisitor clone() {
    try {
      return (CurrentVisitor) super.clone();
    } catch (final CloneNotSupportedException e) {
      throw new RuntimeException(e);
    }
  }
  protected <N extends ASTNode> ProgressVisitor computeSuggestions(final Wring<N> w) {
    return new TransformAndPrune<Suggestion>() {
      @SuppressWarnings("unchecked") @Override protected Suggestion transform(final ASTNode n) {
        if (w.scopeIncludes((N) n) || w.nonEligible((N) n))
          return null;
        return w.make((N) n);
      }
    };
  }

  final Property<List<@NonNull ASTNode>> allNodes = bind((final ASTNode ¢) -> {
    if (¢ == null) // If parameter is undefined
      return null;
    final List<@NonNull ASTNode> $ = new ArrayList<>();
    ¢.accept(new ProgressVisitor() {
      @Override public void go(final ASTNode n) {
        $.add(n);
      }
    });
    return $;
  }).to(¢.root());
  final Property<Integer> nodeCount = bind((final List<@NonNull ASTNode> c) -> Integer.valueOf(c.size())).to(allNodes);
  // Simple recipes:
  final Property<List<Suggestion>> suggestions = bind((final ASTNode n, Integer work) -> {
    ¢.¢.begin("Searching for suggestions...", work.intValue());
    final List<Suggestion> $ = new ArrayList<>();
    n.accept(new TransformAndPrune<Suggestion>($) {
      /** Simply return null by default */
      /** @param n
       * @return {@link Suggestion} made for this node. */
      @Override protected Suggestion transform(final ASTNode n) {
        return null;
      }
    });
    ¢.¢.end();
    return $;
  }).to(¢.root(), nodeCount);
  // Lazy values
  // Sort alphabetically and placed columns; VIM: +,/^\s*\/\//-!sort -u | column
  // -t | sed "s/^/ /"
  final Property<?> toolbox = from().make(() -> new Toolbox());

  public abstract class ProgressVisitor extends ASTVisitor {
    public boolean filter(final ASTNode n) {
      return n != null;
    }
    @Override public final void preVisit(final ASTNode n) {
      ¢.¢.work();
      go(n);
    }
    @Override public final boolean preVisit2(final ASTNode n) {
      return filter(n);
    }
    protected abstract void go(final ASTNode n);
  }

  abstract class TransformAndPrune<T> extends ProgressVisitor {
    TransformAndPrune() {
      this(new ArrayList<>());
    }
    TransformAndPrune(final List<T> pruned) {
      this.pruned = pruned;
    }
    private void go(final T ¢) {
      run(() -> {
        pruned.add(¢);
      }).unless(not(worthy(¢)));
    }
    private boolean not(final boolean b) {
      return !b;
    }
    @Override protected final void go(final ASTNode ¢) {
      go(transform(¢));
    }
    /** to be implemented by client: a function to convert nodes to a given
     * type.
     * @param n JD
     * @return T TODO Javadoc(2016) automatically generated for returned value
     *         of method <code>transform</code> */
    protected abstract T transform(ASTNode n);
    /** determine whether a product of {@link #transform(ASTNode)} is worthy of
     * collecting
     * @param ¢ JD
     * @return true iff the parameter is worthy; by default all products which
     *         are not null are worthy; clients may override. */
    protected boolean worthy(final T ¢) {
      return ¢ != null;
    }

    /** this is where we collect what's {@link #worthy(Object)} */
    protected final List<T> pruned;
  }
}
