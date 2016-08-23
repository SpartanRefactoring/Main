package il.org.spartan.refactoring.wring;

import static il.org.spartan.refactoring.utils.Funcs.*;
import static il.org.spartan.refactoring.utils.Plant.*;
import static il.org.spartan.refactoring.wring.Wrings.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.*;
import il.org.spartan.refactoring.utils.*;
import il.org.spartan.refactoring.wring.LocalInliner.*;

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

    boolean canInlineInto(final ASTNode... ns) {
      return Collect.definitionsOf(name).in(ns).isEmpty() && (Is.sideEffectFree(get()) || uses(ns).size() <= 1);
    }

    boolean canSafelyInlineInto(final ASTNode... ns) {
      return canInlineInto(ns) && unsafeUses(ns).isEmpty();
    }

    @SafeVarargs protected final void inlineInto(final ASTNode... ns) {
      inlineInto(wrap(ns));
    }

    private void inlineInto(final Wrapper<ASTNode>... ns) {
      for (final Wrapper<ASTNode> n : ns)
        inlineIntoSingleton(get(), n);
    }

    private void inlineIntoSingleton(final ASTNode replacement, final Wrapper<ASTNode> ns) {
      final ASTNode oldExpression = ns.get();
      final ASTNode newExpression = duplicate(ns.get());
      ns.set(newExpression);
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