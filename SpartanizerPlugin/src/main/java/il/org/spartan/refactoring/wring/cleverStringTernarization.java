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

  @Override Expression replacement(final ConditionalExpression ce) {
    final Expression then = ce.getThenExpression();
    final Expression elze = ce.getElseExpression();
    if (then.getNodeType() != ASTNode.STRING_LITERAL || elze.getNodeType() != ASTNode.STRING_LITERAL)
      return null;
    final String thenStr = ((StringLiteral) then).getLiteralValue();
    final String elseStr = ((StringLiteral) elze).getLiteralValue();
    final int commonPrefixIndex = findCommonPrefix(thenStr, elseStr);
    if (commonPrefixIndex != 0)
      return replacementPrefix(thenStr, elseStr, commonPrefixIndex, ce);
    final int commonSuffixLength = findCommonSuffix(thenStr, elseStr);
    if (commonSuffixLength != 0)
      return replacementSuffix(thenStr, elseStr, commonSuffixLength, ce);
    return null;
  }

  private static Expression replacementPrefix(final String thenStr, final String elseStr, final int commonPrefixIndex,
      final ConditionalExpression ce) {
    final Expression condition = ce.getExpression();
    final StringLiteral prefix = ce.getAST().newStringLiteral();
    prefix.setLiteralValue(thenStr.substring(0, commonPrefixIndex));
    final StringLiteral thenPost = ce.getAST().newStringLiteral();
    thenPost.setLiteralValue(thenStr.length() == commonPrefixIndex ? //
        "" : thenStr.substring(commonPrefixIndex));
    final StringLiteral elsePost = ce.getAST().newStringLiteral();
    elsePost.setLiteralValue(elseStr.length() == commonPrefixIndex ? //
        "" : elseStr.substring(commonPrefixIndex));
    return subject.pair(prefix, subject.pair(thenPost, //
        elsePost).toCondition(condition)).to(wizard.PLUS2);
  }

  private static Expression replacementSuffix(final String thenStr, final String elseStr, final int commonSuffixLength,
      final ConditionalExpression ce) {
    final Expression condition = ce.getExpression();
    final StringLiteral suffix = ce.getAST().newStringLiteral();
    suffix.setLiteralValue(thenStr.substring(thenStr.length() - commonSuffixLength));
    final StringLiteral thenPre = ce.getAST().newStringLiteral();
    thenPre.setLiteralValue(thenStr.length() == commonSuffixLength ? //
        "" : thenStr.substring(0, thenStr.length() - commonSuffixLength));
    final StringLiteral elsePre = ce.getAST().newStringLiteral();
    elsePre.setLiteralValue(elseStr.length() == commonSuffixLength ? //
        "" : elseStr.substring(0, elseStr.length() - commonSuffixLength));
    final ParenthesizedExpression pe = ce.getAST().newParenthesizedExpression();
    pe.setExpression(subject.pair(thenPre, elsePre).toCondition(condition));
    return subject.pair(pe, suffix).to(wizard.PLUS2);
  }

  private static int findCommonPrefix(final String str1, final String str2) {
    final char[] str1Array = str1.toCharArray();
    final char[] str2Array = str2.toCharArray();
    int i = 0;
    for (; i < str1Array.length && i < str2Array.length; i++)
      if (str1Array[i] != str2Array[i])
        break;
    return i;
  }

  private static int findCommonSuffix(final String str1, final String str2) {
    int i = 0;
    String sub = "";
    for (; i < Math.max(str1.length(), str2.length()); i++) {
      sub = str2.substring(i);
      if (str1.endsWith(sub))
        break;
    }
    if (i == Math.max(str1.length(), str2.length()))
      return 0;
    return sub.length();
  }
}
