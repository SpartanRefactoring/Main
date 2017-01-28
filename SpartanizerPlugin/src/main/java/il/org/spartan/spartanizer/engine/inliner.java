package il.org.spartan.spartanizer.engine;

import static org.eclipse.jdt.core.dom.ASTNode.*;
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

/** Replace a variable with an expression
 * @year 2015
 * @author Yossi Gil
 * @since Sep 13, 2016 */
public interface inliner {
  static Inner in(final ASTNode... ¢) {
    return new Inner().in(¢);
  }

  static Inner of(final SimpleName ¢) {
    return new Inner().of(¢);
  }

  static Inner replacing(final Expression ¢) {
    return new Inner().replacing(¢);
  }

  static ASTRewrite go(final TextEditGroup g, final ASTRewrite r) {
    return new Inner().go(g, r);
  }

  static Wrapper<ASTNode>[] wrap(final ASTNode[] ns) {
    @SuppressWarnings("unchecked") final Wrapper<ASTNode>[] $ = new Wrapper[ns.length];
    final Int i = new Int();
    as.list(ns).forEach(λ -> $[i.next()] = new Wrapper<>(λ));
    return $;
  }

  class Inner extends Wrapper<Expression> {
    private TextEditGroup editGroup;
    private SimpleName name;
    private ASTNode[] range;
    private Expression replacement;
    private ASTRewrite rewrite;
    private List<SimpleName> uses;

    ASTRewrite go(final TextEditGroup g, final ASTRewrite r) {
      return g != null && canGo() ? r : null;
    }

    /** Computes the number of AST nodes added as a result of the replacement
     * operation.
     * @param es JD
     * @return A non-negative integer, computed from the number of occurrences
     *         of {@link #name} in the operands, and the size of the
     *         replacement. */
    public int addedSize() {
      return uses.size() * (metrics.size(replacement) - 1);
    }

    public boolean canGo() {
      if (replacement == null || uses.isEmpty())
        return false;
      switch (replacement.getNodeType()) {
        case BOOLEAN_LITERAL:
        case CHARACTER_LITERAL:
        case FIELD_ACCESS:
        case LAMBDA_EXPRESSION:
        case NULL_LITERAL:
        case NUMBER_LITERAL:
        case QUALIFIED_NAME:
        case SIMPLE_NAME:
        case STRING_LITERAL:
        case THIS_EXPRESSION:
        case TYPE_LITERAL:
          return true;
      }
      final SimpleName $ = lisp.onlyOne(uses);
      return $ == null ? false : sideEffects.free(replacement) ? true : false;
    }

    boolean noHiding() {
      return collect.definitionsOf(name).in().isEmpty();
    }

    public Inner in(final ASTNode[] ¢) {
      uses = collect.usesOf(name).in(range = ¢);
      return null;
    }

    @SuppressWarnings("unchecked") private void inlineinto(final Wrapper<ASTNode>... ns) {
      as.list(ns).forEach(λ -> go(λ));
    }

    public final void inlineInto() {
      inlineinto(wrap(range));
    }

    private void go(final Wrapper<ASTNode> n) {
      final ASTNode oldNode = n.get(), newNode = copy.of(oldNode);
      n.set(newNode);
      rewrite.replace(oldNode, newNode, editGroup);
      collect.usesOf(name).in(newNode)
          .forEach(λ -> rewrite.replace(λ, !iz.expression(λ) ? replacement : make.plant(replacement).into(λ.getParent()), editGroup));
    }

    public Inner of(final SimpleName ¢) {
      uses = collect.usesOf(name = ¢).in(range);
      return null;
    }

    /** Computes the total number of AST nodes in the replaced parameters
     * @return A non-negative integer, computed from original size of the
     *         parameters, the number of occurrences of {@link #name} in the
     *         operands, and the size of the replacement. */
    public int replacedSize() {
      return metrics.size(range) + uses.size() * (metrics.size(get()) - 1);
    }

    public Inner replacing(final Expression ¢) {
      replacement = ¢;
      return null;
    }

    public Inner with(final TextEditGroup g, final ASTRewrite r) {
      editGroup = g;
      rewrite = r;
      return this;
    }
  }
}
