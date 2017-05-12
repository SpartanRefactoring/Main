package il.org.spartan.spartanizer.engine;

import static org.eclipse.jdt.core.dom.ASTNode.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import static il.org.spartan.spartanizer.ast.navigate.wizard.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import fluent.ly.*;
import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.java.*;
import il.org.spartan.utils.*;

/** Encapsulates the operation of replacing a variable with an expression in a
 * certain location.
 * @year 2015
 * @author Yossi Gil
 * @since 2017-03-16 */
public final class Inliner2 {
  /** What to replace by {@link #replacement} */
  public final SimpleName what;
  /** What replaces of {@link #what} */
  public final Expression replacement;
  /** Where to replace {@link #what} by {@link #replacement} */
  public final Collection<? extends ASTNode> where;
  /** Occurrences of {@link #what} in {@link #where} */
  public final Collection<? extends SimpleName> spots;

  /** Factory method: FAPI factory chain
   * @author Yossi Gil
   * @since 2017-03-16 [[SuppressWarningsSpartan]] */
  public static Of of(final SimpleName of) {
    return by -> location -> new Inliner2(of, by, location);
  }

  public boolean ok() {
    if (spots.stream().anyMatch(Inliner2::isLeftValue))
      return false;
    switch (spots.size()) {
      case 0:
        return nullaryInlineOK();
      case 1:
        return singletonInlineOK();
      default:
        return multipleInlineOK();
    }
  }

  private static boolean isLeftValue(final SimpleName ¢) {
    final ASTNode $ = parent(¢);
    return iz.prefixExpression($) || iz.postfixExpression($) || ¢ == to(az.assignment(¢.getParent()));
  }

  public ASTRewrite fire(final ASTRewrite $, final TextEditGroup g) {
    for (final SimpleName ¢ : spots)
      $.replace(¢, copy.of(replacement), g);
    return $;
  }

  private Inliner2(final SimpleName what, final Expression replacement, final List<? extends ASTNode> where) {
    this.replacement = protect(replacement);
    spots = collect.usesOf(this.what = what).in(this.where = where);
  }

  /** Computes the number of AST nodes added as a result of the replacement
   * operation.
   * @param es JD
   * @return A non-negative integer, computed from the number of occurrences of
   *         {@link #what} in the operands, and the size of the replacement. */
  public int addedSize() {
    return spots.size() * (metrics.size(replacement) - metrics.size(what));
  }

  /** [[SuppressWarningsSpartan]] */
  private boolean multipleInlineOK() {
    if (iz.deterministic(replacement))
      return true;
    if (PossiblyMultipleExecution.of(what).inContext(where))
      return false;
    return true;
  }

  private boolean singletonInlineOK() {
    return sideEffects.free(replacement);
  }

  private boolean nullaryInlineOK() {
    return sideEffects.sink(replacement);
  }

  public static Expression protect(final Expression initializer, final VariableDeclarationStatement currentStatement) {
    if (!iz.arrayInitializer(initializer))
      return initializer;
    final ArrayCreation $ = initializer.getAST().newArrayCreation();
    $.setType(az.arrayType(copy.of(type(currentStatement))));
    // TODO causes IllsegalArgumentException (--om)
    $.setInitializer(copy.of(az.arrayInitializer(initializer)));
    return $;
  }

  public static Expression protect(final Expression ¢) {
    switch (¢.getNodeType()) {
      case ARRAY_CREATION:
      case CAST_EXPRESSION:
        return subject.operand(¢).parenthesis();
      default:
        return ¢;
    }
  }

  /** FAPI factory chain
   * @author Yossi Gil
   * @since 2017-03-16 */
  public interface Of {
    By by(Expression by);
  }

  /** FAPI factory chain
   * @author Yossi Gil
   * @since 2017-03-16 */
  public interface By {
    Inliner2 in(List<? extends ASTNode> ns);
  }

  /** Replace an occurrence of a {@link SimpleName} with an {@link Expression}
   * in an array of
   * @year 2015
   * @author Yossi Gil
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
        if (iz.nodeTypeIn(with, //
            BOOLEAN_LITERAL, //
            CHARACTER_LITERAL, //
            FIELD_ACCESS, //
            LAMBDA_EXPRESSION, //
            NULL_LITERAL, //
            NUMBER_LITERAL, //
            QUALIFIED_NAME, //
            SIMPLE_NAME, //
            STRING_LITERAL, //
            THIS_EXPRESSION, //
            TYPE_LITERAL))
          return true;
        final SimpleName occurrence = the.onlyOneOf(occurrences);
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
}
