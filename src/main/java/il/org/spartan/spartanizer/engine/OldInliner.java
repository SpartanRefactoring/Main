package il.org.spartan.spartanizer.engine;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import static il.org.spartan.spartanizer.ast.navigate.extract.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.java.*;
import il.org.spartan.spartanizer.utils.*;

/** Replace a variable with an expression
 * @year 2015
 * @author Yossi Gil
 * @since Sep 13, 2016 */
public final class OldInliner {
  static Wrapper<ASTNode>[] wrap(final ASTNode[] ns) {
    @SuppressWarnings("unchecked") final Wrapper<ASTNode>[] $ = new Wrapper[ns.length];
    final Int i = new Int();
    as.list(ns).forEach(λ -> $[i.next()] = new Wrapper<>(λ));
    return $;
  }

  final TextEditGroup editGroup;
  final SimpleName name;
  final ASTRewrite rewriter;

  public OldInliner(final SimpleName n) {
    this(n, null, null);
  }

  public OldInliner(final SimpleName name, final ASTRewrite rewriter, final TextEditGroup editGroup) {
    this.name = name;
    this.rewriter = rewriter;
    this.editGroup = editGroup;
  }

  public InlinerWithValue byValue(final Expression replacement) {
    return new InlinerWithValue(replacement);
  }

  public final class InlinerWithValue extends Wrapper<Expression> {
    InlinerWithValue(final Expression replacement) {
      super(core(replacement));
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
      return collect.definitionsOf(name).in(¢).isEmpty() && (sideEffects.free(get()) || uses(¢).size() <= 1);
    }

    public boolean canSafelyInlineinto(final ASTNode... ¢) {
      return canInlineinto(¢) && unsafeUses(¢).isEmpty();
    }

    @SuppressWarnings("unchecked") private void inlineinto(final Wrapper<ASTNode>... ns) {
      as.list(ns).forEach(λ -> inlineintoSingleton(get(), λ));
    }

    @SafeVarargs public final void inlineInto(final ASTNode... ¢) {
      inlineinto(wrap(¢));
    }

    private void inlineintoSingleton(final ASTNode replacement, final Wrapper<ASTNode> n) {
      final ASTNode oldExpression = n.get(), newExpression = copy.of(n.get());
      n.set(newExpression);
      rewriter.replace(oldExpression, newExpression, editGroup);
      collect.usesOf(name).in(newExpression)
          .forEach(λ -> rewriter.replace(λ, !iz.expression(λ) ? replacement : make.plant((Expression) replacement).into(λ.getParent()), editGroup));
    }

    /** Computes the total number of AST nodes in the replaced parameters
     * @param es JD
     * @return A non-negative integer, computed from original size of the
     *         parameters, the number of occurrences of {@link #name} in the
     *         operands, and the size of the replacement. */
    public int replacedSize(final ASTNode... ¢) {
      return metrics.size(¢) + uses(¢).size() * (metrics.size(get()) - 1);
    }

    private List<SimpleName> unsafeUses(final ASTNode... ¢) {
      return collect.unsafeUsesOf(name).in(¢);
    }

    private List<SimpleName> uses(final ASTNode... ¢) {
      return collect.usesOf(name).in(¢);
    }
  }
}
