package il.org.spartan.spartanizer.wring;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.engine.*;

/**
 * Convert loops with return statements to shorter ones : </br>
 * Convert <br/>
 * <code>
 * for (..) { <br/>
 *  asdfasdfasdf <br/>
 *   return XXX; <br/>
 * } <br/>
 *return XXX; <br/>
 * </code>
 * to : <br/>
 * <code>
 * for (..) { <br/>
 *  asdfasdfasdf <br/>
 *   break; <br/>
 * } <br/>
 *return XXX; <br/>
 * </code>
 * @author Dor Ma'ayan
 * @since 2016-09-07
 */
public class ReturnToBreakFiniteFor extends Wring<Block> implements Kind.Canonicalization {

  @Override public String description() {
     return "Convert the Return inside the loop to break";
  }

  @Override String description(Block n) {
    return "Convert the Return inside the loop to break";
  }

  private static boolean isInfiniteLoop(ForStatement n){
    if(az.booleanLiteral(n.getExpression()) == null)
      return false;
    return true;
  }
  
  
  private static boolean compareReturnStatements(ReturnStatement r1, ReturnStatement r2){
    if(r1==null || r2==null)
      return false;
    return r1.getExpression().toString().equals(r2.getExpression().toString());
  }
  
  @SuppressWarnings("all") @Override Rewrite make(Block n) {
    List<Statement>statementList = n.statements();
    ForStatement forStatement = (ForStatement)statementList.get(0);
    ReturnStatement nextReturn = (ReturnStatement)statementList.get(1);
    if(isInfiniteLoop(forStatement))
        return null;
      Statement body = forStatement.getBody();
      if(iz.block(body)){
         List<Statement> blockStatements = ((Block)body).statements();
         for(Statement s : blockStatements){
           if(compareReturnStatements(nextReturn,az.returnStatement(s)))
             return new Rewrite(description(), s) {
               @Override public void go(final ASTRewrite r, final TextEditGroup g) {
                 r.replace(s,(ASTNode) ((Block)into.s("break;")).statements().get(0), g);
               }
           };
         }
      }
      return null;
  }
  
  @Override boolean scopeIncludes(final Block s) {
    List<Statement>statementList = s.statements();
    if(s!=null && statementList.size()>1 && statementList.get(0) instanceof ForStatement //
        && statementList.get(1) instanceof ReturnStatement)
      return true;
    return false;
  }

}
