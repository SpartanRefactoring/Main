package il.org.spartan.spartanizer.wring;

import static il.org.spartan.spartanizer.assemble.plant.*;
import static il.org.spartan.spartanizer.wring.Wrings.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.assemble.*;
import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.java.*;

final class LocalInliner {
  class LocalInlineWithValue extends Wrapper<Expression> {
    LocalInlineWithValue(final Expression replacement) {
      super(extract.core(replacement));
    }

    /** Computes the number of AST nodes added as a result of the replacement
     * operation.
     * @param es JD
     * @return A non-negative integer, computed from the number of occurrences
     *         of {@link #name} in the operands, and the size of the
     *         replacement. */
    int addedSize(final ASTNode... ns) {
      return uses(ns).size() * (size(get()) - 1);
    }

    boolean canInlineinto(final ASTNode... ns) {
      return Collect.definitionsOf(name).in(ns).isEmpty() && (sideEffects.free(get()) || uses(ns).size() <= 1);
    }

    boolean canSafelyInlineinto(final ASTNode... ns) {
      return canInlineinto(ns) && unsafeUses(ns).isEmpty();
    }

    @SafeVarargs protected final void inlineinto(final ASTNode... ns) {
      inlineinto(wrap(ns));
    }

    @SuppressWarnings("unchecked") private void inlineinto(final Wrapper<ASTNode>... ns) {
      for (final Wrapper<ASTNode> n : ns)
        inlineintoSingleton(get(), n);
    }

    private void inlineintoSingleton(final ASTNode replacement, final Wrapper<ASTNode> n) {
      final ASTNode oldExpression = n.get();
      final ASTNode newExpression = duplicate.of(n.get());
      n.set(newExpression);
      rewriter.replace(oldExpression, newExpression, editGroup);
      for (final ASTNode use : Collect.usesOf(name).in(newExpression))
        rewriter.replace(use, !(use instanceof Expression) ? replacement : plant((Expression) replacement).into(use.getParent()), editGroup);
    }

    /** Computes the total number of AST nodes in the replaced parameters
     * @param es JD
     * @return A non-negative integer, computed from original size of the
     *         parameters, the number of occurrences of {@link #name} in the
     *         operands, and the size of the replacement. */
    int replacedSize(final ASTNode... ns) {
      return size(ns) + uses(ns).size() * (size(get()) - 1);
    }

    private List<SimpleName> unsafeUses(final ASTNode... ns) {
      return Collect.unsafeUsesOf(name).in(ns);
    }

    private List<SimpleName> uses(final ASTNode... ns) {
      return Collect.usesOf(name).in(ns);
    }
  }

  static Wrapper<ASTNode>[] wrap(final ASTNode[] ns) {
    @SuppressWarnings("unchecked") final Wrapper<ASTNode>[] $ = new Wrapper[ns.length];
    int i = 0;
    for (final ASTNode t : ns)
      $[i++] = new Wrapper<>(t);
    return $;
  }

  final SimpleName name;
  final ASTRewrite rewriter;
  final TextEditGroup editGroup;

  LocalInliner(final SimpleName n) {
    this(n, null, null);
  }

  LocalInliner(final SimpleName n, final ASTRewrite rewriter, final TextEditGroup g) {
    name = n;
    this.rewriter = rewriter;
    editGroup = g;
  }

  LocalInlineWithValue byValue(final Expression replacement) {
    return new LocalInlineWithValue(replacement);
  }
}