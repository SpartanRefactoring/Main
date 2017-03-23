package il.org.spartan.spartanizer.ast.navigate;

import static il.org.spartan.lisp.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import static il.org.spartan.spartanizer.ast.navigate.wizard.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.jetbrains.annotations.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;

/** TODO: orimarco {@code marcovitch.ori@gmail.com} please add a description
 * @author orimarco {@code marcovitch.ori@gmail.com}
 * @since 2016-12-22 */
public enum find {
  ;
  @NotNull public static <N extends ASTNode> Operand<N> first(final Class<N> c) {
    return new Operand<N>() {
      @Override public N under(final ASTNode ¢) {
        return lisp.first(descendants.whoseClassIs(c).from(¢));
      }
    };
  }

  /** @param <N> JD
   * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
   * @since 2017-01-27 */
  public abstract static class Operand<N extends ASTNode> {
    public abstract N under(ASTNode n);
  }

  public static <N extends ASTNode> Expression singleExpressionDifference(@NotNull final List<N> ns) {
    @Nullable final Expression $;
    if (ns.size() < 2 || ($ = singleExpressionDifference(lisp.first(ns), ns.get(1))) == null)
      return null;
    for (int ¢ = 2; ¢ < ns.size(); ++¢)
      if (!$.equals(singleExpressionDifference(lisp.first(ns), ns.get(¢))))
        return null;
    return $;
  }

  @Nullable private static Expression singleExpressionDifference(@Nullable final ASTNode n1, @Nullable final ASTNode n2) {
    if (n1 == null || n2 == null)
      return null;
    if (areSelfDifferent(n1, n2))
      return az.expression(n1);
    @Nullable final List<ASTNode> children1 = Recurser.allChildren(n1), children2 = Recurser.allChildren(n2);
    if (children1.size() != children2.size())
      return az.expression(n1);
    if (children1.isEmpty())
      return same(n1, n2) ? null : az.expression(n1);
    @Nullable Expression $ = singleExpressionDifference(lisp.first(children1), lisp.first(children2));
    for (int i = 1; i < children1.size(); ++i) {
      @Nullable final Expression diff = singleExpressionDifference(children1.get(i), children2.get(i));
      // If two children aren't the same and not with same expression, the whole
      // of n1 is the difference
      if ($ != null && diff != null && !same($, diff))
        return az.expression(n1);
      $ = $ != null ? $ : diff;
    }
    return $;
  }

  @NotNull public static <N extends ASTNode> List<String> singleAtomicDifferences(@NotNull final List<N> ¢) {
    @NotNull final List<String> $ = new ArrayList<>();
    ¢.forEach(λ -> $.add(λ != lisp.first(¢) ? singleAtomicDifference(λ, lisp.first(¢)) : singleAtomicDifference(lisp.first(¢), second(¢))));
    return $;
  }

  @NotNull public static <N extends ASTNode> List<Expression> findSingleExpressionDifferences(@NotNull final List<N> ¢) {
    @NotNull final List<Expression> $ = new ArrayList<>();
    ¢.forEach(λ -> $.add(λ != lisp.first(¢) ? singleExpressionDifference(λ, lisp.first(¢)) : singleExpressionDifference(lisp.first(¢), second(¢))));
    return $;
  }

  /** Gets two nodes and returns the identifier of the only name i n1 which is
   * different from n2. If the nodes subtrees differ with other then one name or
   * any node, -1 is returned. */
  @Nullable public static <N extends ASTNode> String singleAtomicDifference(@Nullable final N n1, @Nullable final N n2) {
    if (n1 == null || n2 == null)
      return null;
    if ((n1 + "").equals(n2 + ""))
      return "";
    if (iz.atomic(n1) && iz.atomic(n2))
      return n1 + "";
    if (areSelfDifferent(n1, n2))
      return null;
    @Nullable final List<ASTNode> children1 = Recurser.allChildren(n1), children2 = Recurser.allChildren(n2);
    if (children1.size() != children2.size())
      return null;
    @Nullable String $ = singleAtomicDifference(lisp.first(children1), lisp.first(children2));
    $ = $ != null ? $ : "";
    for (int i = 1; i < children1.size(); ++i) {
      @Nullable final String diff = singleAtomicDifference(children1.get(i), children2.get(i));
      $ = !Objects.equals($, "") || diff == null ? $ : diff;
      if (!$.equals(diff) && diff != null && !diff.isEmpty())
        return null;
    }
    return $;
  }

  static <N extends ASTNode> boolean areSelfDifferent(final N n1, final N n2) {
    return iz.infixExpression(n1) && (!iz.infixExpression(n2) || !operator(az.infixExpression(n1)).equals(operator(az.infixExpression(n2))));
  }

  /** like the other one but for a list
   * @param ns
   * @return */
  public static <N extends ASTNode> String singleAtomicDifference(@NotNull final List<N> ns) {
    if (ns.size() < 2)
      return null;
    @Nullable String $ = singleAtomicDifference(lisp.first(ns), second(ns));
    if ($ == null)
      return null;
    for (int i = 2; i < ns.size(); ++i) {
      @Nullable final String diff = singleAtomicDifference(lisp.first(ns), ns.get(i));
      $ = !Objects.equals($, "") || diff == null ? $ : diff;
      if (!$.equals(diff) && diff != null && !diff.isEmpty())
        return null;
    }
    return $;
  }

  public static <N extends ASTNode> boolean differsInSingleAtomic(@Nullable final List<N> ¢) {
    if (¢ == null || ¢.isEmpty())
      return false;
    @Nullable final String $ = singleAtomicDifference(¢);
    return $ != null && !$.isEmpty();
  }

  public static <N extends ASTNode> boolean differsInSingleExpression(@Nullable final List<N> ¢) {
    return ¢ != null && !¢.isEmpty() && singleExpressionDifference(¢) != null;
  }
}
