package il.org.spartan.spartanizer.wrings;

import static il.org.spartan.lisp.*;
import static il.org.spartan.spartanizer.ast.step.*;
import static il.org.spartan.spartanizer.ast.wizard.*;
import static org.eclipse.jdt.core.dom.ASTNode.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.assemble.*;
import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.wringing.*;

/** convert <code>polite?"Eat your meal.":"Eat your meal, please"
 * </code>, <code>polite?"thanks for the meal":"I hated the meal"</code>, and
 * <code>a?"abracadabra":"abba"</code> into
 * <code>"Eat your meal"+(polite?".":", please")</code>,
 * <code>(polite?"thanks for":"I hated")+"the meal"</code>, and,
 * <code>"ab"+(a?"racadabr":"b")+"a"</code>
 * @author Dor Ma'ayan
 * @author Niv Shalmon
 * @since 2016-09-1 */
public final class TernaryPusdownStrings extends ReplaceCurrentNode<ConditionalExpression> implements Kind.Ternarization {
  public static Expression replacement(final Expression condition, final Expression then, final Expression elze) {
    return iz.is(then, STRING_LITERAL) && iz.is(elze, STRING_LITERAL) ? simplify(condition, az.stringLiteral(then), az.stringLiteral(elze))
        : iz.is(then, STRING_LITERAL) && iz.is(elze, INFIX_EXPRESSION) ? simplify(condition, az.stringLiteral(then), az.infixExpression(elze))
            : iz.is(then, INFIX_EXPRESSION) && iz.is(elze, STRING_LITERAL)
                ? simplify(subject.operand(condition).to(PrefixExpression.Operator.NOT), az.stringLiteral(elze), az.infixExpression(then))
                : iz.is(then, INFIX_EXPRESSION) && iz.is(elze, INFIX_EXPRESSION)
                    ? simplify(condition, az.infixExpression(then), az.infixExpression(elze)) : null; //
  }

  static String longer(final String s1, final String s2) {
    return s1 == shorter(s1, s2) ? s2 : s1;
  }

  private static Expression as(final List<Expression> elzeOperands) {
    return first(elzeOperands);
  }

  private static int firstDifference(final String s1, final String s2) {
    if (s1 != shorter(s1, s2))
      return firstDifference(s2, s1);
    assert s1.length() <= s2.length();
    int $ = 0;
    for (int ¢ = 0; ¢ < s1.length(); ++¢) {
      if (!Character.isAlphabetic(s1.charAt(¢)) && !Character.isAlphabetic(s2.charAt(¢))//
          || ¢ == s1.length() - 1 && !Character.isAlphabetic(s2.charAt(¢)))
        $ = ¢;
      if (first(s1, ¢) != first(s2, ¢))
        return $;
    }
    return s1.length();
  }

  /** @param s JD
   * @param i the length of the prefix
   * @param n an ASTNode to create the StringLiteral from
   * @return a StringLiteral whose literal value is the prefix of length i of
   *         s */
  private static StringLiteral getPrefix(final String s, final int i, final ASTNode n) {
    return make.from(n).literal(i == 0 ? "" : s.substring(0, i));
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
    // TODO: Matteo/Ori: the WLOG nano, abbreviated.
    if (s1 != shorter(s1, s2))
      return lastDifference(s2, s1);
    assert s1.length() <= s2.length();
    int $ = 0;
    for (; $ < s1.length(); ++$)
      if (last(s1, $) != last(s2, $))
        break;
    if ($ == 0)
      return 0;
    if ($ == s1.length() && s2.length() == s1.length() || $ == s1.length() && !Character.isAlphabetic(s2.charAt(s2.length() - $ - 1)))
      return $;
    for (int j = s1.length() - $; j < s1.length(); ++j)
      if (!Character.isAlphabetic(s1.charAt(j)))
        return s1.length() - j;
    return 0;
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
    assert first(es).getNodeType() == STRING_LITERAL;
    final StringLiteral l = (StringLiteral) first(es);
    final StringLiteral suffix = getSuffix(l.getLiteralValue(), i, x);
    replaceFirst(es, suffix);
    return subject.operands(es).to(PLUS2);
  }

  private static InfixExpression replaceSuffix(final InfixExpression x, final int i) {
    assert x.getOperator() == PLUS2;
    final List<Expression> es = extract.allOperands(x);
    assert last(es).getNodeType() == STRING_LITERAL;
    final StringLiteral l = (StringLiteral) last(es);
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
    if (as(elzeOperands).getNodeType() == STRING_LITERAL) {
      final String elzeStr = ((StringLiteral) as(elzeOperands)).getLiteralValue();
      final int commonPrefixIndex = firstDifference(thenStr, elzeStr);
      if (commonPrefixIndex != 0)
        return subject.pair(getPrefix(thenStr, commonPrefixIndex, condition), subject.pair(getSuffix(thenStr, commonPrefixIndex, condition), //
            replacePrefix(elze, commonPrefixIndex)).toCondition(condition)).to(PLUS2);
    }
    if (elzeOperands.get(elzeOperands.size() - 1).getNodeType() == STRING_LITERAL) {
      final String elzeStr = ((StringLiteral) elzeOperands.get(elzeOperands.size() - 1)).getLiteralValue();
      final int commonSuffixIndex = lastDifference(thenStr, elzeStr);
      if (commonSuffixIndex != 0) {
        final StringLiteral elzePre = getPrefix(elzeStr, elzeStr.length() - commonSuffixIndex, condition);
        replaceLast(elzeOperands, elzePre);
        return subject
            .pair(subject.operand(subject
                .pair(getPrefix(thenStr, thenStr.length() - commonSuffixIndex, condition)//
                    , replaceSuffix(elze, commonSuffixIndex))//
                .toCondition(condition)).parenthesis(), getSuffix(thenStr, thenStr.length() - commonSuffixIndex, condition))//
            .to(PLUS2);
      }
    }
    return null;
  }

  private static Expression simplify(final Expression condition, final StringLiteral then, final StringLiteral elze) {
    return simplify(condition, then.getLiteralValue(), elze.getLiteralValue());
  }

  private static Expression simplifyStrings(final InfixExpression then, final InfixExpression elze, final Expression condition) {
    assert then.getOperator() == PLUS2;
    final List<Expression> thenOperands = extract.allOperands(then);
    assert elze.getOperator() == PLUS2;
    final List<Expression> elzeOperands = extract.allOperands(elze);
    if (first(thenOperands).getNodeType() == STRING_LITERAL && first(elzeOperands).getNodeType() == STRING_LITERAL) {
      final String thenStr = ((StringLiteral) first(thenOperands)).getLiteralValue();
      final String elzeStr = ((StringLiteral) first(elzeOperands)).getLiteralValue();
      final int commonPrefixIndex = firstDifference(thenStr, elzeStr);
      if (commonPrefixIndex != 0)
        return subject.pair(getPrefix(thenStr, commonPrefixIndex, condition),
            subject
                .pair(//
                    replacePrefix(then, commonPrefixIndex), replacePrefix(elze, commonPrefixIndex))//
                .toCondition(condition))
            .to(PLUS2);
    }
    if (last(thenOperands).getNodeType() == STRING_LITERAL && last(elzeOperands).getNodeType() == STRING_LITERAL) {
      final String thenStr = ((StringLiteral) last(thenOperands)).getLiteralValue();
      final String elzeStr = ((StringLiteral) last(thenOperands)).getLiteralValue();
      final int commonSuffixIndex = lastDifference(thenStr, elzeStr);
      if (commonSuffixIndex != 0)
        return subject.pair(subject.operand(subject
            .pair(replaceSuffix(then, commonSuffixIndex)//
                , replaceSuffix(elze, commonSuffixIndex))//
            .toCondition(condition)).parenthesis(), getSuffix(thenStr, thenStr.length() - commonSuffixIndex, condition)).to(PLUS2);
    }
    return null;
  }

  @Override public String description(@SuppressWarnings("unused") final ConditionalExpression __) {
    return "Replace ternarization with more clever one";
  }

  @Override public Expression replacement(final ConditionalExpression ¢) {
    return replacement(expression(¢), then(¢), elze(¢));
  }
}
