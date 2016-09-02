package il.org.spartan.refactoring.wring;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.refactoring.assemble.*;
import il.org.spartan.refactoring.ast.*;

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
public final class cleverStringTernarization extends Wring.ReplaceCurrentNode<ConditionalExpression> implements Kind.Ternarization {
  @Override String description(@SuppressWarnings("unused") final ConditionalExpression __) {
    return "Replace ternarization with more clever one";
  }

  @Override Expression replacement(final ConditionalExpression x) {
    final Expression then = x.getThenExpression();
    final Expression elze = x.getElseExpression();
    if (then.getNodeType() != ASTNode.STRING_LITERAL || elze.getNodeType() != ASTNode.STRING_LITERAL)
      return null;
    final String thenStr = ((StringLiteral) then).getLiteralValue();
    final String elseStr = ((StringLiteral) elze).getLiteralValue();
    final int commonPrefixIndex = findCommonPrefix(thenStr, elseStr);
    if (commonPrefixIndex != 0)
      return replacementPrefix(thenStr, elseStr, commonPrefixIndex, x);
    final int commonSuffixLength = findCommonSuffix(thenStr, elseStr);
    return commonSuffixLength == 0 ? null : replacementSuffix(thenStr, elseStr, commonSuffixLength, x);
  }

  private static Expression replacementPrefix(final String thenStr, final String elseStr, final int commonPrefixIndex,
      final ConditionalExpression x) {
    final Expression condition = x.getExpression();
    final StringLiteral prefix = x.getAST().newStringLiteral();
    prefix.setLiteralValue(thenStr.substring(0, commonPrefixIndex));
    final StringLiteral thenPost = x.getAST().newStringLiteral();
    thenPost.setLiteralValue(thenStr.length() == commonPrefixIndex ? //
        "" : thenStr.substring(commonPrefixIndex));
    final StringLiteral elsePost = x.getAST().newStringLiteral();
    elsePost.setLiteralValue(elseStr.length() == commonPrefixIndex ? //
        "" : elseStr.substring(commonPrefixIndex));
    return subject.pair(prefix, subject.pair(thenPost, //
        elsePost).toCondition(condition)).to(wizard.PLUS2);
  }

  private static Expression replacementSuffix(final String thenStr, final String elseStr, final int commonSuffixLength,
      final ConditionalExpression x) {
    final Expression condition = x.getExpression();
    final StringLiteral suffix = x.getAST().newStringLiteral();
    suffix.setLiteralValue(thenStr.substring(thenStr.length() - commonSuffixLength));
    final StringLiteral thenPre = x.getAST().newStringLiteral();
    thenPre.setLiteralValue(thenStr.length() == commonSuffixLength ? //
        "" : thenStr.substring(0, thenStr.length() - commonSuffixLength));
    final StringLiteral elsePre = x.getAST().newStringLiteral();
    elsePre.setLiteralValue(elseStr.length() == commonSuffixLength ? //
        "" : elseStr.substring(0, elseStr.length() - commonSuffixLength));
    final ParenthesizedExpression pe = x.getAST().newParenthesizedExpression();
    pe.setExpression(subject.pair(thenPre, elsePre).toCondition(condition));
    return subject.pair(pe, suffix).to(wizard.PLUS2);
  }

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
}
