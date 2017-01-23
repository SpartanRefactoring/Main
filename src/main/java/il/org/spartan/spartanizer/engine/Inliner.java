package il.org.spartan.spartanizer.engine;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.java.*;
import il.org.spartan.spartanizer.utils.*;

/**  Replace a variable with an expression
 * @year 2015 
 * @author Yossi Gil
 * @since Sep 13, 2016
 */

public final class Inliner {
  static Wrapper<ASTNode>[] wrap(final ASTNode[] ns) {
    @SuppressWarnings("unchecked") final Wrapper<ASTNode>[] $ = new Wrapper[ns.length];
    final Int i = new Int();
    Arrays.asList(ns).forEach(¢ -> $[i.inner++] = new Wrapper<>(¢));
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

  /** Determines whether a specific SimpleName was used in a
   * {@link ForStatement}.
   * @param s JD
   * @param n JD
   * @return <code><b>true</b></code> <em>iff</em> the SimpleName is used in a
   *         ForStatement's condition, updaters, or body. */
  public static boolean variableUsedInFor(final ForStatement s, final SimpleName n) {
    return !collect.usesOf(n).in(step.condition(s), step.body(s)).isEmpty() || !collect.usesOf(n).in(step.updaters(s)).isEmpty();
  }

  public static boolean variableNotUsedAfterStatement(final Statement s, final SimpleName n) {
    final Block b = az.block(s.getParent());
    assert b != null : "For loop's parent is not a block";
    final List<Statement> statements = step.statements(b);
    boolean passedFor = false;
    for (final Statement ¢ : statements) {
      if (passedFor && !collect.usesOf(n).in(¢).isEmpty())
        return false;
      if (¢.equals(s))
        passedFor = true;
    }
    return true;
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
      return collect.definitionsOf(name).in(¢).isEmpty() && (sideEffects.free(get()) || uses(¢).size() <= 1);
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
      Arrays.asList(ns).forEach(¢ -> inlineintoSingleton(get(), ¢));
    }

    private void inlineintoSingleton(final ASTNode replacement, final Wrapper<ASTNode> n) {
      final ASTNode oldExpression = n.get(), newExpression = copy.of(n.get());
      n.set(newExpression);
      rewriter.replace(oldExpression, newExpression, editGroup);
      collect.usesOf(name).in(newExpression).forEach(
          use -> rewriter.replace(use, !iz.expression(use) ? replacement : make.plant((Expression) replacement).into(use.getParent()), editGroup));
    }

    private List<SimpleName> unsafeUses(final ASTNode... ¢) {
      return collect.unsafeUsesOf(name).in(¢);
    }

    private List<SimpleName> uses(final ASTNode... ¢) {
      return collect.usesOf(name).in(¢);
    }
  }
}
