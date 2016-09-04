package il.org.spartan.refactoring.wring;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.refactoring.assemble.*;
import il.org.spartan.refactoring.ast.*;
import il.org.spartan.refactoring.utils.*;

/** convert
 *
 * <pre>
 * polite ? "Eat your meal." : "Eat your meal, please"
 * </pre>
 *
 * <pre>
 * polite ? "thanks for the meal" : "I hated the meal"
 * </pre>
 *
 * <pre>
 * a ? "abracadabra" : "abba"
 * </pre>
 *
 * into
 *
 * <pre>
 * "Eat your meal" + (polite ? "." : ", please")
 * </pre>
 *
 * <pre>
 * (polite ? "thanks for" : "I hated") + "the meal"
 * </pre>
 *
 * <pre>
 * "ab" + (a ? "racadabr" : "b") + "a"
 * </pre>
 *
 * @author Dor Ma'ayan
 * @author Niv Shalmon
 * @since 2016-09-1 */
public final class CleverTernarization extends Wring.ReplaceCurrentNode<ConditionalExpression> implements Kind.Ternarization {
  private static int findCommonPrefix(final String str1, final String str2) {
    final char[] str1Array = str1.toCharArray();
    final char[] str2Array = str2.toCharArray();
    int $ = 0;
    for (; $ < str1Array.length && $ < str2Array.length; ++$)
      if (str1Array[$] != str2Array[$])
        break;
    return $;
  }

  private static int findCommonSuffix(final String str1, final String str2) {
    int i = 0;
    String sub = "";
    for (; i < Math.max(str1.length(), str2.length()); ++i) {
      sub = str2.substring(i);
      if (str1.endsWith(sub))
        break;
    }
    return i == Math.max(str1.length(), str2.length()) ? 0 : sub.length();
  }

  /** @param s JD
   * @param i the length of the prefix
   * @param n an ASTNode to create the StringLiteral from
   * @return a StringLiteral whose literal value is the prefix of length i of
   *         s */
  private static StringLiteral getPrefix(final String s, final int i, final ASTNode n) {
    return makeStringLiteral(i == 0 ? "" : s.substring(0, i), n);
  }

  /** @param s JD
   * @param i the length of the suffix
   * @param n an ASTNode to create the StringLiteral from
   * @return a StringLiteral whose literal value is the suffix which begins on
   *         the i'th character of s */
  private static StringLiteral getSuffix(final String s, final int i, final ASTNode n) {
    return makeStringLiteral(s.length() == i ? "" : s.substring(i), n);
  }

  private static StringLiteral makeStringLiteral(final String s, final ASTNode n) {
    final StringLiteral $ = n.getAST().newStringLiteral();
    $.setLiteralValue(s);
    return $;
  }

  private static Expression replacementPrefix(final String thenStr, final String elzeStr, final int commonPrefixIndex, final Expression condition) {
    final StringLiteral prefix = getPrefix(thenStr, commonPrefixIndex, condition);
    final StringLiteral thenPost = getSuffix(thenStr, commonPrefixIndex, condition);
    final StringLiteral elzePost = getSuffix(elzeStr, commonPrefixIndex, condition);
    return subject.pair(prefix, subject.pair(thenPost, //
        elzePost).toCondition(condition)).to(wizard.PLUS2);
  }

  private static Expression replacementSuffix(final String thenStr, final String elzeStr, final int commonSuffixLength, final Expression condition) {
    final StringLiteral suffix = getSuffix(thenStr, thenStr.length() - commonSuffixLength, condition);
    final StringLiteral thenPre = getPrefix(thenStr, thenStr.length() - commonSuffixLength, condition);
    final StringLiteral elzePre = getPrefix(elzeStr, elzeStr.length() - commonSuffixLength, condition);
    final ParenthesizedExpression pe = condition.getAST().newParenthesizedExpression();
    pe.setExpression(subject.pair(thenPre, elzePre).toCondition(condition));
    return subject.pair(pe, suffix).to(wizard.PLUS2);
  }

  private static Expression simplify(final StringLiteral then, final InfixExpression elze, final Expression condition) {
    final String thenStr = then.getLiteralValue();
    assert elze.getOperator() == wizard.PLUS2;
    final List<Expression> elzeOperands = extract.allOperands(elze);
    if (elzeOperands.get(0).getNodeType() == ASTNode.STRING_LITERAL) {
      final String elzeStr = ((StringLiteral) elzeOperands.get(0)).getLiteralValue();
      final int commonPrefixIndex = findCommonPrefix(thenStr, elzeStr);
      if (commonPrefixIndex != 0) {
        final StringLiteral prefix = getPrefix(thenStr, commonPrefixIndex, condition);
        final StringLiteral thenPost = getSuffix(thenStr, commonPrefixIndex, condition);
        final StringLiteral elzePost = getSuffix(elzeStr, commonPrefixIndex, condition);
        lisp.chop(elzeOperands);
        elzeOperands.add(0, elzePost);
        return subject.pair(prefix, subject.pair(thenPost, //
            subject.operands(elzeOperands).to(wizard.PLUS2)).toCondition(condition)).to(wizard.PLUS2);
      }
    }
    if (elzeOperands.get(elzeOperands.size() - 1).getNodeType() == ASTNode.STRING_LITERAL) {
      final String elzeStr = ((StringLiteral) elzeOperands.get(elzeOperands.size() - 1)).getLiteralValue();
      final int commonSuffixIndex = findCommonSuffix(thenStr, elzeStr);
      if (commonSuffixIndex != 0) {
        final StringLiteral suffix = getSuffix(thenStr, thenStr.length() - commonSuffixIndex, condition);
        final StringLiteral thenPre = getPrefix(thenStr, thenStr.length() - commonSuffixIndex, condition);
        final StringLiteral elzePre = getPrefix(elzeStr, elzeStr.length() - commonSuffixIndex, condition);
        elzeOperands.remove(elzeOperands.size() - 1);
        elzeOperands.add(elzeOperands.size(), elzePre);
        final ParenthesizedExpression pe = condition.getAST().newParenthesizedExpression();
        pe.setExpression(subject.pair(thenPre, subject.operands(elzeOperands).to(wizard.PLUS2)).toCondition(condition));
        return subject.pair(pe, suffix).to(wizard.PLUS2);
      }
    }
    return null;
  }

  private static Expression simplify(final StringLiteral then, final StringLiteral elze, final Expression condition) {
    final String thenStr = then.getLiteralValue();
    final String elzeStr = elze.getLiteralValue();
    final int commonPrefixIndex = findCommonPrefix(thenStr, elzeStr);
    if (commonPrefixIndex != 0)
      return replacementPrefix(thenStr, elzeStr, commonPrefixIndex, condition);
    final int commonSuffixLength = findCommonSuffix(thenStr, elzeStr);
    return commonSuffixLength == 0 ? null : replacementSuffix(thenStr, elzeStr, commonSuffixLength, condition);
  }

  @Override String description(@SuppressWarnings("unused") final ConditionalExpression __) {
    return "Replace ternarization with more clever one";
  }

  @Override Expression replacement(final ConditionalExpression x) {
    final Expression then = x.getThenExpression();
    final Expression elze = x.getElseExpression();
    final Expression condition = x.getExpression();
    if (then.getNodeType() == ASTNode.STRING_LITERAL && elze.getNodeType() == ASTNode.STRING_LITERAL)
      return simplify((StringLiteral) then, (StringLiteral) elze, condition);
    if (then.getNodeType() == ASTNode.STRING_LITERAL && elze.getNodeType() == ASTNode.INFIX_EXPRESSION)
      return simplify((StringLiteral) then, (InfixExpression) elze, condition);
    if (then.getNodeType() == ASTNode.INFIX_EXPRESSION && elze.getNodeType() == ASTNode.STRING_LITERAL)
      return simplify((StringLiteral) elze, (InfixExpression) then, //
          subject.operand(condition).to(PrefixExpression.Operator.NOT));
    return null;
  }
}
