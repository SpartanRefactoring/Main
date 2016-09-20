package il.org.spartan.spartanizer.wrings;

import static il.org.spartan.lisp.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.wizard.*;

import static il.org.spartan.spartanizer.ast.step.*;

import il.org.spartan.spartanizer.assemble.*;
import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.wringing.*;

/** convert</br>
 * <code>polite?"Eat your meal.":"Eat your meal, please"</code>,
 * <code>polite?"thanks for the meal":"I hated the meal"</code> </br>
 * into </br>
 * <code>"Eat your meal"+(polite?".":", please")</code>,
 * <code>(polite?"thanks for":"I hated")+"the meal"</code></br>
 * Will not separate words, for example <code> f() ? "True" : "False" </code>
 * will not be changed
 * @author Dor Ma'ayan
 * @author Niv Shalmon
 * @since 2016-09-1 */
public final class TernaryPushdownStrings extends ReplaceCurrentNode<ConditionalExpression> implements Kind.Ternarization {
  private static int firstDifference(final String s1, final String s2) {
    if (s1 != shorter(s1, s2))
      return firstDifference(s2, s1);
    assert s1.length() <= s2.length();
    if (s1.length() == 0)
      return 0;
    int $ = 0;
    for (int ¢ = 0; ¢ < s1.length(); ++¢) {
      if (!Character.isAlphabetic(first(s1, ¢)) && !Character.isAlphabetic(first(s2, ¢))
          || ¢ == s1.length() - 1 && !Character.isAlphabetic(first(s2, ¢)))
        $ = ¢;
      if (first(s1, ¢) != first(s2, ¢))
        return $;
    }
    return s1.length() != s2.length() && Character.isAlphabetic(first(s2, s1.length())) && Character.isAlphabetic(first(s2, s1.length() - 1)) ? 0
        : s1.length();
  }

  /** @param s JD
   * @param i the length of the prefix
   * @param n an ASTNode to create the StringLiteral from
   * @return a StringLiteral whose literal value is the prefix of length i of
   *         s */
  private static StringLiteral getPrefix(final String s, final int i, final ASTNode n) {
    return make.from(n).literal(i <= 0 ? "" : s.substring(0, i));
    // Hack for issue #236
  }

  /** @param s JD
   * @param i the length of the suffix
   * @param n an ASTNode to create the StringLiteral from
   * @return a StringLiteral whose literal value is the suffix which begins on
   *         the i'th character of s */
  private static StringLiteral getSuffix(final String s, final int i, final ASTNode n) {
    return make.from(n).literal(s.length() == i ? "" : s.substring(i));
  }

  // TODO: Yossi: the swap nano
  private static int lastDifference(final String s1, final String s2) {
    if (s1 != shorter(s1, s2))
      return lastDifference(s2, s1);
    assert s1.length() <= s2.length();
    if (s1.length() == 0)
      return 0;
    int $ = 0;
    for (int ¢ = 0; ¢ < s1.length(); ++¢) {
      if (!Character.isAlphabetic(last(s1, ¢)) && !Character.isAlphabetic(last(s2, ¢))
          || ¢ == s1.length() - 1 && !Character.isAlphabetic(last(s2, ¢)))
        $ = ¢ + 1;
      if (last(s1, ¢) != last(s2, ¢))
        return $;
    }
    return s1.length() != s2.length() && Character.isAlphabetic(last(s2, s1.length())) && Character.isAlphabetic(last(s2, s1.length() - 1)) ? 0 : s1.length();
  }

  static String longer(final String s1, final String s2) {
    return s1 == shorter(s1, s2) ? s2 : s1;
  }

  public static Expression replacement(final Expression condition, final Expression then, final Expression elze) {
    return iz.stringLiteral(then) && iz.stringLiteral(elze) ? simplify(condition, az.stringLiteral(then), az.stringLiteral(elze))
        : iz.stringLiteral(then) && iz.infixExpression(elze) ? simplify(condition, az.stringLiteral(then), az.infixExpression(elze))
            : iz.infixExpression(then) && iz.stringLiteral(elze)
                ? simplify(subject.operand(condition).to(PrefixExpression.Operator.NOT), az.stringLiteral(elze), az.infixExpression(then))
                : iz.infixExpression(then) && iz.infixExpression(elze) ? simplify(condition, az.infixExpression(then), az.infixExpression(elze))
                    : null; //
  }

  private static Expression replacementPrefix(final String then, final String elze, final int commonPrefixIndex, final Expression condition) {
    return subject.pair(getPrefix(then, commonPrefixIndex, condition), subject.pair(getSuffix(then, commonPrefixIndex, condition), //
        getSuffix(elze, commonPrefixIndex, condition)).toCondition(condition)).to(PLUS2);
  }

  private static Expression replacementSuffix(final String then, final String elze, final int commonSuffixLength, final Expression condition) {
    return subject.pair(
        subject.operand(subject.pair(getPrefix(then, then.length() - commonSuffixLength, condition)//
            , getPrefix(elze, elze.length() - commonSuffixLength, condition)).toCondition(condition)).parenthesis()//
        , getSuffix(then, then.length() - commonSuffixLength, condition)).to(PLUS2);
  }

  private static InfixExpression replacePrefix(final InfixExpression x, final int i) {
    assert x.getOperator() == PLUS2;
    final List<Expression> es = extract.allOperands(x);
    final StringLiteral l = az.stringLiteral(first(es));
    assert l != null;
    assert l.getLiteralValue().length() >= i;
    final StringLiteral suffix = getSuffix(l.getLiteralValue(), i, x);
    replaceFirst(es, suffix);
    return subject.operands(es).to(PLUS2);
  }

  private static InfixExpression replaceSuffix(final InfixExpression x, final int i) {
    assert x.getOperator() == PLUS2;
    final List<Expression> es = extract.allOperands(x);
    final StringLiteral l = az.stringLiteral(last(es));
    assert l != null;
    assert l.getLiteralValue().length() >= i;
    final StringLiteral prefix = getPrefix(l.getLiteralValue(), l.getLiteralValue().length() - i, x);
    replaceLast(es, prefix);
    return subject.operands(es).to(PLUS2);
  }

  private static String shorter(final String s1, final String s2) {
    return s1.length() > s2.length() ? s2 : s1;
  }

  private static Expression simplify(final Expression condition, final InfixExpression then, final InfixExpression elze) {
    return type.isNotString(then) || type.isNotString(elze) ? null : simplifyStrings(then, elze, condition);
  }

  private static Expression simplify(final Expression condition, final String then, final String elze) {
    return simplify(condition, then, elze, firstDifference(then, elze));
  }

  private static Expression simplify(final Expression condition, final String then, final String elze, final int commonPrefixIndex) {
    if (commonPrefixIndex != 0)
      return replacementPrefix(then, elze, commonPrefixIndex, condition);
    final int commonSuffixLength = lastDifference(then, elze);
    return commonSuffixLength == 0 ? null : replacementSuffix(then, elze, commonSuffixLength, condition);
  }

  private static Expression simplify(final Expression condition, final StringLiteral then, final InfixExpression elze) {
    final String thenStr = then.getLiteralValue();
    assert elze.getOperator() == PLUS2;
    final List<Expression> elzeOperands = extract.allOperands(elze);
    if (iz.stringLiteral(first(elzeOperands))) {
      final String elzeStr = az.stringLiteral(first(elzeOperands)).getLiteralValue();
      final int commonPrefixIndex = firstDifference(thenStr, elzeStr);
      if (commonPrefixIndex != 0)
        return subject.pair(getPrefix(thenStr, commonPrefixIndex, condition), subject.pair(getSuffix(thenStr, commonPrefixIndex, condition), //
            replacePrefix(elze, commonPrefixIndex)).toCondition(condition)).to(PLUS2);
    }
    if (!iz.stringLiteral(last(elzeOperands)))
      return null;
    final String elzeStr = az.stringLiteral(last(elzeOperands)).getLiteralValue();
    final int commonSuffixIndex = lastDifference(thenStr, elzeStr);
    if (commonSuffixIndex == 0)
      return null;
    final StringLiteral elzePre = getPrefix(elzeStr, elzeStr.length() - commonSuffixIndex, condition);
    replaceLast(elzeOperands, elzePre);
    return subject
        .pair(subject.operand(subject
            .pair(getPrefix(thenStr, thenStr.length() - commonSuffixIndex, condition)//
                , replaceSuffix(elze, commonSuffixIndex))//
            .toCondition(condition)).parenthesis(), getSuffix(thenStr, thenStr.length() - commonSuffixIndex, condition))//
        .to(PLUS2);
  }

  private static Expression simplify(final Expression condition, final StringLiteral then, final StringLiteral elze) {
    return simplify(condition, then.getLiteralValue(), elze.getLiteralValue());
  }

  private static Expression simplifyStrings(final InfixExpression then, final InfixExpression elze, final Expression condition) {
    assert then.getOperator() == PLUS2;
    final List<Expression> thenOperands = extract.allOperands(then);
    assert elze.getOperator() == PLUS2;
    final List<Expression> elzeOperands = extract.allOperands(elze);
    if (iz.stringLiteral(first(thenOperands)) && iz.stringLiteral(first(elzeOperands))) {
      final String thenStr = az.stringLiteral(first(thenOperands)).getLiteralValue();
      final String elzeStr = az.stringLiteral(first(elzeOperands)).getLiteralValue();
      final int commonPrefixIndex = firstDifference(thenStr, elzeStr);
      if (commonPrefixIndex != 0)
        return subject.pair(getPrefix(thenStr, commonPrefixIndex, condition),
            subject
                .pair(//
                    replacePrefix(then, commonPrefixIndex), replacePrefix(elze, commonPrefixIndex))//
                .toCondition(condition))
            .to(PLUS2);
    }
    if (!iz.stringLiteral(last(thenOperands)) || !iz.stringLiteral(last(elzeOperands)))
      return null;
    final String thenStr = ((StringLiteral) last(thenOperands)).getLiteralValue();
    final String elzeStr = ((StringLiteral) last(thenOperands)).getLiteralValue();
    final int commonSuffixIndex = lastDifference(thenStr, elzeStr);
    return commonSuffixIndex == 0 ? null
        : subject.pair(subject.operand(subject
            .pair(replaceSuffix(then, commonSuffixIndex)//
                , replaceSuffix(elze, commonSuffixIndex))//
            .toCondition(condition)).parenthesis(), getSuffix(thenStr, thenStr.length() - commonSuffixIndex, condition)).to(PLUS2);
  }

  @Override public String description(@SuppressWarnings("unused") final ConditionalExpression __) {
    return "Replace ternarization with more clever one";
  }

  @Override public Expression replacement(final ConditionalExpression ¢) {
    return replacement(expression(¢), then(¢), elze(¢));
  }
}
