package il.org.spartan.refactoring.wring;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.refactoring.assemble.*;

import il.org.spartan.refactoring.ast.*;

/** convert
 *
 * <pre>
 * polite ? "Eat your meal." :  "Eat your meal, please"
 * </pre>
 * 
 * <pre>
 * polite ? "thanks for the meal" :  "I hated the meal"
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
    final Expression elze =ce.getElseExpression();
    if(then.getNodeType()!=ASTNode.STRING_LITERAL || elze.getNodeType()!=ASTNode.STRING_LITERAL)
      return null;
    String thenStr = ((StringLiteral) then).getLiteralValue();
    String elseStr = ((StringLiteral) elze).getLiteralValue();
    int commonPrefixIndex = findCommonPrefix(thenStr,elseStr);
    if(commonPrefixIndex!=0)
      return replacementPrefix(thenStr,elseStr, commonPrefixIndex ,ce); 
    int commonSuffixLength = findCommonSuffix(thenStr,elseStr);
    if(commonSuffixLength!=0)
      return replacementSuffix(thenStr,elseStr,commonSuffixLength,ce);
    return null;
  }
  
  private static Expression replacementPrefix(String thenStr, String elseStr, int commonPrefixIndex , ConditionalExpression ce){
    final Expression condition = ce.getExpression();
    StringLiteral  prefix = ce.getAST().newStringLiteral();
    prefix.setLiteralValue(thenStr.substring(0, commonPrefixIndex));
    StringLiteral  thenPost = ce.getAST().newStringLiteral();
    thenPost.setLiteralValue(thenStr.length()==commonPrefixIndex ? //
        "" : thenStr.substring(commonPrefixIndex));
    StringLiteral  elsePost = ce.getAST().newStringLiteral();
    elsePost.setLiteralValue( elseStr.length()==commonPrefixIndex ? //
        "" : elseStr.substring(commonPrefixIndex));
    return subject.pair(prefix , subject.pair(thenPost, //
    elsePost).toCondition(condition)).to(wizard.PLUS2);
  }
  
  private static Expression replacementSuffix(String thenStr, String elseStr, int commonSuffixLength , ConditionalExpression ce){
    final Expression condition = ce.getExpression();
    StringLiteral  suffix = ce.getAST().newStringLiteral();
    suffix.setLiteralValue(thenStr.substring(thenStr.length()-commonSuffixLength));
    StringLiteral  thenPre = ce.getAST().newStringLiteral();
    thenPre.setLiteralValue(thenStr.length()==commonSuffixLength ? //
        "" : thenStr.substring(0,thenStr.length()-commonSuffixLength));
    StringLiteral  elsePre = ce.getAST().newStringLiteral();
    elsePre.setLiteralValue( elseStr.length()== commonSuffixLength ? //
        "" : elseStr.substring(0,elseStr.length()-commonSuffixLength));
    ParenthesizedExpression pe = ce.getAST().newParenthesizedExpression();
    pe.setExpression(subject.pair(thenPre,elsePre).toCondition(condition));
    return subject.pair(pe,suffix).to(wizard.PLUS2);
  }
  
  private static int findCommonPrefix(String str1, String str2){
    char[] str1Array = str1.toCharArray();
    char[] str2Array = str2.toCharArray();
    int i=0;
    for (;i<str1Array.length && i<str2Array.length;i++){
      if(str1Array[i]!=str2Array[i])
        break;
    }
    return i;
  }

  private static int findCommonSuffix(String str1, String str2){
    int i=0;
    String sub = "";
    for (;i<Math.max(str1.length(),str2.length());i++){
      sub = str2.substring(i);
      if(str1.endsWith(sub))
        break;
    }
    if(i==Math.max(str1.length(),str2.length()))
      return 0;
    return sub.length();
  }
}

