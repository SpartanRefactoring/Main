package il.org.spartan.spartanizer.engine;

import static org.eclipse.jdt.core.dom.ASTNode.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import java.util.*;
import java.util.stream.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.cmdline.*;
import il.org.spartan.spartanizer.java.*;
import il.org.spartan.utils.*;

/** Replace a variable with an expression
 * @year 2015
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @since Sep 13, 2016 */
public final class DefunctInliner {
  public boolean never(final Statement s) {
    return system.stream(yieldAncestors.until(s).ancestors(name)).anyMatch(λ -> !canInlineInto(λ));
  }

  public boolean canInlineInto(final ASTNode $) {
    return !iz.nodeTypeIn($, ANONYMOUS_CLASS_DECLARATION, TRY_STATEMENT, SYNCHRONIZED_STATEMENT, LAMBDA_EXPRESSION, WHILE_STATEMENT, DO_STATEMENT)
        && ($ instanceof ForStatement ? canInlineInto((ForStatement) $) : //
            $ instanceof EnhancedForStatement && canInlineInto((EnhancedForStatement) $));
  }

  public boolean canInlineInto(final EnhancedForStatement s) {
    return descendants.of(s.getExpression()).contains(name);
  }

  public boolean canInlineInto(final ForStatement s) {
    return !updaters(s).stream().anyMatch(a -> descendants.of(a).contains(name));
  }

  public Expression protect(final Expression initializer, final VariableDeclarationStatement currentStatement) {
    if (!iz.arrayInitializer(initializer))
      return initializer;
    final ArrayCreation $ = initializer.getAST().newArrayCreation();
    $.setType(az.arrayType(copy.of(type(currentStatement))));
    // TODO: Marco causes IllegalArgumentException
    $.setInitializer(copy.of(az.arrayInitializer(initializer)));
    return $;
  }

  public boolean variableNotUsedAfterStatement(final Statement s, final SimpleName n) {
    final Block b = az.block(s.getParent());
    assert b != null : "For loop's parent is not a block";
    final List<Statement> statements = statements(b);
    boolean passedFor = false;
    for (final Statement ¢ : statements) {
      if (passedFor && !collect.usesOf(n).in(¢).isEmpty())
        return false;
      if (¢.equals(s))
        passedFor = true;
    }
    return true;
  }

  /** Determines whether a specific SimpleName was used in a
   * {@link ForStatement}.
   * @param s JD
   * @param n JD
   * @return whether the SimpleName is used in a ForStatement's condition,
   *         updaters, or body. */
  public boolean variableUsedInFor(final ForStatement s) {
    return !collect.usesOf(name).in(condition(s), body(s)).isEmpty() || !collect.usesOf(name).in(updaters(s)).isEmpty();
  }

  static Wrapper<ASTNode>[] wrap(final ASTNode... ns) {
    @SuppressWarnings("unchecked") final Wrapper<ASTNode>[] $ = new Wrapper[ns.length];
    final Int i = new Int();
    Stream.of(ns).forEach(λ -> $[i.inner++] = new Wrapper<>(λ));
    return $;
  }

  final TextEditGroup editGroup;
  final SimpleName name;
  final ASTRewrite rewriter;

  public DefunctInliner(final SimpleName n) {
    this(n, null, null);
  }

  public DefunctInliner(final SimpleName name, final ASTRewrite rewriter, final TextEditGroup editGroup) {
    this.name = name;
    this.rewriter = rewriter;
    this.editGroup = editGroup;
  }

  public InlinerWithValue byValue(final Expression replacement) {
    return new InlinerWithValue(replacement);
  }

  public final class InlinerWithValue extends Wrapper<Expression> {
    InlinerWithValue(final Expression replacement) {
      super(replacement);
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

    public boolean canInlineInto(final ASTNode... ¢) {
      return collect.definitionsOf(name).in(¢).isEmpty() && (sideEffects.free(get()) || uses(¢).size() <= 1);
    }

    public boolean canSafelyInlineinto(final ASTNode... ¢) {
      return canInlineInto(¢) && unsafeUses(¢).isEmpty();
    }

    @SuppressWarnings("unchecked") private void inlineinto(final Wrapper<ASTNode>... ns) {
      Arrays.asList(ns).forEach(λ -> inlineIntoSingleton(get(), λ));
    }

    @SafeVarargs public final void inlineInto(final ASTNode... ¢) {
      inlineinto(wrap(¢));
    }

    private void inlineIntoSingleton(final ASTNode replacement, final Wrapper<ASTNode> n) {
      final ASTNode oldExpression = n.get(), newExpression = copy.of(oldExpression);
      n.set(newExpression);
      rewriter.replace(oldExpression, newExpression, editGroup);
      collect.usesOf(name).in(newExpression).forEach(λ -> replace(λ, replacement));
    }

    private void replace(final SimpleName λ, final ASTNode replacement) {
      rewriter.replace(λ, !iz.expression(λ) ? replacement : make.plant((Expression) replacement).into(λ.getParent()), editGroup);
    }

    /** Computes the total number of AST nodes in the replaced parameters
     * @param es JD
     * @return A non-negative integer, computed from original size of the
     *         parameters, the number of occurrences of {@link #name} in the
     *         operands, and the size of the replacement. */
    public int replacedSize(final ASTNode... ¢) {
      return metrics.size(¢) + uses(¢).size() * (metrics.size(get()) - 1);
    }

    private Collection<SimpleName> unsafeUses(final ASTNode... ¢) {
      return collect.unsafeUsesOf(name).in(¢);
    }

    private Collection<SimpleName> uses(final ASTNode... ¢) {
      return collect.usesOf(name).in(¢);
    }
  }
}
