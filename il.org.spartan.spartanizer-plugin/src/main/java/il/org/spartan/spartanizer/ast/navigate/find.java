package il.org.spartan.spartanizer.ast.navigate;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import static il.org.spartan.spartanizer.ast.navigate.wizard.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;

/** TODO orimarco {@code marcovitch.ori@gmail.com} please add a description
 * @author orimarco {@code marcovitch.ori@gmail.com}
 * @since 2016-12-22 */
public enum find {
  DUMMY_ENUM_INSTANCE_INTRODUCING_SINGLETON_WITH_STATIC_METHODS;
  public static <N extends ASTNode> Operand<N> first(final Class<N> c) {
    return new Operand<N>() {
      @Override public N under(final ASTNode ¢) {
        return the.firstOf(descendants.whoseClassIs(c).from(¢));
      }
    };
  }
  public static List<SimpleName> occurencesOf(final ASTNode $, final String id) {
    return descendants.whoseClassIs(SimpleName.class).suchThat(λ -> identifier(λ).equals(id)).from($);
  }

  /** @param <N> JD
   * @author Yossi Gil
   * @since 2017-01-27 */
  public abstract static class Operand<N extends ASTNode> {
    public abstract N under(ASTNode n);
  }

  public static <N extends ASTNode> Expression singleExpressionDifference(final List<N> ns) {
    final Expression ret;
    if (ns.size() < 2 || (ret = singleExpressionDifference(the.firstOf(ns), ns.get(1))) == null)
      return null;
    for (int ¢ = 2; ¢ < ns.size(); ++¢)
      if (!ret.equals(singleExpressionDifference(the.firstOf(ns), ns.get(¢))))
        return null;
    return ret;
  }
  private static Expression singleExpressionDifference(final ASTNode n1, final ASTNode n2) {
    if (n1 == null || n2 == null)
      return null;
    if (areSelfDifferent(n1, n2))
      return az.expression(n1);
    final List<ASTNode> children1 = Recurser.allChildren(n1), children2 = Recurser.allChildren(n2);
    if (children1.size() != children2.size())
      return az.expression(n1);
    if (children1.isEmpty())
      return eq(n1, n2) ? null : az.expression(n1);
    Expression ret = singleExpressionDifference(the.firstOf(children1), the.firstOf(children2));
    for (int i = 1; i < children1.size(); ++i) {
      final Expression diff = singleExpressionDifference(children1.get(i), children2.get(i));
      // If two children aren't the same and not with same expression, the whole
      // of n1 is the difference
      if (ret != null && diff != null && !eq(ret, diff))
        return az.expression(n1);
      ret = ret != null ? ret : diff;
    }
    return ret;
  }
  public static <N extends ASTNode> List<String> singleAtomicDifferences(final List<N> ¢) {
    final List<String> ret = an.empty.list();
    ¢.forEach(λ -> ret.add(λ != the.firstOf(¢) ? singleAtomicDifference(λ, the.firstOf(¢)) : singleAtomicDifference(the.firstOf(¢), the.secondOf(¢))));
    return ret;
  }
  public static <N extends ASTNode> List<Expression> findSingleExpressionDifferences(final List<N> ¢) {
    final List<Expression> ret = an.empty.list();
    ¢.forEach(λ -> ret
        .add(λ != the.firstOf(¢) ? singleExpressionDifference(λ, the.firstOf(¢)) : singleExpressionDifference(the.firstOf(¢), the.secondOf(¢))));
    return ret;
  }
  /** Gets two nodes and returns the identifier of the only name i n1 which is
   * different from n2. If the nodes subtrees differ with other then one name or
   * any node, -1 is returned. */
  public static <N extends ASTNode> String singleAtomicDifference(final N n1, final N n2) {
    if (n1 == null || n2 == null)
      return null;
    if ((n1 + "").equals(n2 + ""))
      return "";
    if (iz.atomic(n1) && iz.atomic(n2))
      return n1 + "";
    if (areSelfDifferent(n1, n2))
      return null;
    final List<ASTNode> ns1 = Recurser.allChildren(n1), ns2 = Recurser.allChildren(n2);
    if (ns1.size() != ns2.size())
      return null;
    String ret = singleAtomicDifference(the.firstOf(ns1), the.firstOf(ns2));
    ret = ret == null ? "" : ret;
    for (int i = 1; i < ns1.size(); ++i) {
      final String diff = singleAtomicDifference(ns1.get(i), ns2.get(i));
      ret = !Objects.equals(ret, "") || diff == null ? ret : diff;
      if (!ret.equals(diff) && diff != null && !diff.isEmpty())
        return null;
    }
    return ret;
  }
  static <N extends ASTNode> boolean areSelfDifferent(final N n1, final N n2) {
    return iz.infixExpression(n1) && (!iz.infixExpression(n2) || !operator(az.infixExpression(n1)).equals(operator(az.infixExpression(n2))));
  }
  /** like the other one but for a list
   * @param ns
   * @return */
  public static <N extends ASTNode> String singleAtomicDifference(final List<N> ns) {
    if (ns.size() < 2)
      return null;
    String ret = singleAtomicDifference(the.firstOf(ns), the.secondOf(ns));
    if (ret == null)
      return null;
    for (int i = 2; i < ns.size(); ++i) {
      final String diff = singleAtomicDifference(the.firstOf(ns), ns.get(i));
      ret = !Objects.equals(ret, "") || diff == null ? ret : diff;
      if (!ret.equals(diff) && diff != null && !diff.isEmpty())
        return null;
    }
    return ret;
  }
  public static <N extends ASTNode> boolean differsInSingleAtomic(final List<N> ¢) {
    if (¢ == null || ¢.isEmpty())
      return false;
    final String $ = singleAtomicDifference(¢);
    return $ != null && !$.isEmpty();
  }
  public static <N extends ASTNode> boolean differsInSingleExpression(final List<N> ¢) {
    return ¢ != null && !¢.isEmpty() && singleExpressionDifference(¢) != null;
  }
}
