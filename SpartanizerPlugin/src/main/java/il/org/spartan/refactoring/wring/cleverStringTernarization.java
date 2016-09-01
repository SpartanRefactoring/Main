package il.org.spartan.refactoring.wring;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.refactoring.ast.*;
import il.org.spartan.refactoring.builder.*;

/** 
 * convert
 *
 * <pre>
 * polite ? "Eat your meal." :  "Eat your meal, please";
 * </pre>
 *
 * into
 *
 * <pre>
 * "Eat your meal" + (polite ? "." : ", please");
 * </pre>
 *
 * @author Dor Ma'ayan
 * @author Niv Shalmon
 * @since 2016-09-1 */
public final class cleverStringTernarization extends Wring.ReplaceCurrentNode<ConditionalExpression> implements Kind.Ternarization {
  @Override String description(@SuppressWarnings("unused") final ConditionalExpression __) {
    return "Replace if with a return of a conditional statement";
  }

  @Override Expression replacement(final ConditionalExpression ce) {
    final Expression condition = ce.getExpression();
    final Expression then = ce.getThenExpression();
    final Expression elze =ce.getElseExpression();
    if(then.getNodeType()!=ASTNode.STRING_LITERAL || elze.getNodeType()!=ASTNode.STRING_LITERAL)
      return null;
    String thenStr = ((StringLiteral) then).getLiteralValue();
    String elseStr = ((StringLiteral) elze).getLiteralValue();
    int commonPrefixIndex = findCommonPrefix(thenStr,elseStr);
    if(commonPrefixIndex!=-1){
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
    return null;
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
  

}
