package il.org.spartan.spartanizer.engine;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.assemble.*;
import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.java.*;

/** Replace a variable with an expression
 * @author Yossi Gil
 * @year 2015 */
public final class Inliner {
  static Wrapper<ASTNode>[] wrap(final ASTNode[] ns) {
    @SuppressWarnings("unchecked") final Wrapper<ASTNode>[] $ = new Wrapper[ns.length];
    int i = 0;
    for (final ASTNode ¢ : ns)
      $[i++] = new Wrapper<>(¢);
    return $;
  }

  final SimpleName name;
  final ASTRewrite rewriter;
  final TextEditGroup editGroup;

  public Inliner(final SimpleName n) {
    this(n, null, null);
  }

  public Inliner(final SimpleName name, final ASTRewrite rewriter, final TextEditGroup editGroup) {
    this.name = name;
    this.rewriter = rewriter;
    this.editGroup = editGroup;
  }

  public InlinerWithValue byValue(final Expression replacement) {
    return new InlinerWithValue(replacement);
  }

  public final class InlinerWithValue extends Wrapper<Expression> {
    InlinerWithValue(final Expression replacement) {
      super(extract.core(replacement));
    }

    /** Computes the number of AST nodes added as a result of the replacement
     * operation.
     * @param es JD
     * @return A non-negative integer, computed from the number of occurrences
     *         of {@link #name} in the operands, and the size of the
     *         replacement. */
    public int addedSize(final ASTNode... ¢) {
      return uses(¢).size() * (metrics.size(get()) - 1);
    }

    public boolean canInlineinto(final ASTNode... ¢) {
      return Collect.definitionsOf(name).in(¢).isEmpty() && (sideEffects.free(get()) || uses(¢).size() <= 1);
    }

    public boolean canSafelyInlineinto(final ASTNode... ¢) {
      return canInlineinto(¢) && unsafeUses(¢).isEmpty();
    }

    @SafeVarargs public final void inlineInto(final ASTNode... ¢) {
      inlineinto(wrap(¢));
    }

    /** Computes the total number of AST nodes in the replaced parameters
     * @param es JD
     * @return A non-negative integer, computed from original size of the
     *         parameters, the number of occurrences of {@link #name} in the
     *         operands, and the size of the replacement. */
    public int replacedSize(final ASTNode... ¢) {
      return metrics.size(¢) + uses(¢).size() * (metrics.size(get()) - 1);
    }

    @SuppressWarnings("unchecked") private void inlineinto(final Wrapper<ASTNode>... ns) {
      for (final Wrapper<ASTNode> ¢ : ns)
        inlineintoSingleton(get(), ¢);
    }

    private void inlineintoSingleton(final ASTNode replacement, final Wrapper<ASTNode> n) {
      final ASTNode oldExpression = n.get();
      final ASTNode newExpression = duplicate.of(n.get());
      n.set(newExpression);
      rewriter.replace(oldExpression, newExpression, editGroup);
      for (final ASTNode use : Collect.usesOf(name).in(newExpression))
        rewriter.replace(use, !(use instanceof Expression) ? replacement : make.plant((Expression) replacement).into(use.getParent()), editGroup);
    }

    private List<SimpleName> unsafeUses(final ASTNode... ¢) {
      return Collect.unsafeUsesOf(name).in(¢);
    }

    private List<SimpleName> uses(final ASTNode... ¢) {
      return Collect.usesOf(name).in(¢);
    }
  }
}