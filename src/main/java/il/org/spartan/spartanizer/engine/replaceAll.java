package il.org.spartan.spartanizer.engine;

import static org.eclipse.jdt.core.dom.ASTNode.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import static il.org.spartan.spartanizer.ast.navigate.wizard.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.java.*;
import il.org.spartan.spartanizer.utils.*;

/** Replace an occurrence of a {@link SimpleName} with an {@link Expression} in
 * an array of
 * @year 2015
 * @author Yossi Gil
 *         {@code yossi dot (optional) gil at gmail dot (required) com}
 * @since Sep 13, 2016 */
public interface replaceAll {
  static ASTRewrite go(final TextEditGroup g, final ASTRewrite r) {
    return new Inner().go(g, r);
  }

  static Inner in(final ASTNode... ¢) {
    return new Inner().in(¢);
  }

  static Inner of(final SimpleName ¢) {
    return new Inner().of(¢);
  }

  static Inner with(final Expression ¢) {
    return new Inner().with(¢);
  }

  static Wrapper<ASTNode>[] wrap(final ASTNode[] ns) {
    @SuppressWarnings("unchecked") final Wrapper<ASTNode>[] $ = new Wrapper[ns.length];
    final Int i = new Int();
    as.list(ns).forEach(λ -> $[i.next()] = new Wrapper<>(λ));
    return $;
  }

  class Inner extends Wrapper<Expression> {
    private SimpleName name;
    private ASTNode[] range;
    private Expression with;
    private List<SimpleName> occurrences;

    ASTRewrite go(final TextEditGroup g, final ASTRewrite $) {
      occurrences.forEach(λ -> $.replace(λ, !iz.expression(λ) ? copy.of(with) : make.plant(with).into(λ.getParent()), g));
      return $;
    }

    /** Computes the number of AST nodes added as a result of the replacement
     * operation.
     * @param es JD
     * @return A non-negative integer, computed from the number of occurrences
     *         of {@link #name} in the operands, and the size of the
     *         replacement. */
    public int addedSize() {
      return occurrences.size() * (metrics.size(with) - 1);
    }

    public boolean canGo() {
      if (with == null || occurrences.isEmpty())
        return false;
      switch (with.getNodeType()) {
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
        default:
      }
      final SimpleName occurrence = lisp.onlyOne(occurrences);
      if (occurrence == null)
        return false;
      switch (Coupling.of(occurrence).withRespectTo(commonAncestor(occurrence, with))) {
        case IFF:
          return !iz.fragile(with);
        case IMPLIED:
          return !haz.sideEffects(with);
        case INDEPENDENT:
          return !iz.deterministic(with);
        default:
          return true;
      }
    }

    boolean noHiding() {
      noHiding();
      return collect.definitionsOf(name).in().isEmpty();
    }

    public Inner in(final ASTNode[] ¢) {
      occurrences = collect.usesOf(name).in(range = ¢);
      return null;
    }

    public Inner of(final SimpleName ¢) {
      occurrences = collect.usesOf(name = ¢).in(range);
      return null;
    }

    /** Computes the total number of AST nodes in the replaced parameters
     * @return A non-negative integer, computed from original size of the
     *         parameters, the number of occurrences of {@link #name} in the
     *         operands, and the size of the replacement. */
    public int replacedSize() {
      return metrics.size(range) + occurrences.size() * (metrics.size(get()) - 1);
    }

    public Inner with(final Expression ¢) {
      with = ¢;
      return this;
    }
  }
}
