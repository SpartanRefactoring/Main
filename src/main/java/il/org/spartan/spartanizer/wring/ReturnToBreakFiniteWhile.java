package il.org.spartan.spartanizer.wring;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.engine.*;

/** Convert loops with return statements to shorter ones : </br>
 * Convert <br/>
 * <code>
 * for (..) { <br/>
 *  asdfasdfasdf <br/>
 *   return XXX; <br/>
 * } <br/>
 *return XXX; <br/>
 * </code> to : <br/>
 * <code>
 * for (..) { <br/>
 *  asdfasdfasdf <br/>
 *   break; <br/>
 * } <br/>
 *return XXX; <br/>
 * </code>
 * @author Dor Ma'ayan
 * @since 2016-09-07 */
public class ReturnToBreakFiniteWhile extends Wring<Block> implements Kind.Canonicalization {
  @Override public String description() {
    return "Convert the Return inside the loop to break";
  }

  @Override String description(Block n) {
    return "Convert the Return inside the loop to break";
  }

  private static boolean isInfiniteLoop(WhileStatement n) {
    if (az.booleanLiteral(n.getExpression()) == null)
      return false;
    return true;
  }

  private static boolean compareReturnStatements(ReturnStatement r1, ReturnStatement r2) {
    if (r1 == null || r2 == null)
      return false;
    return r1.getExpression().toString().equals(r2.getExpression().toString());
  }

  @SuppressWarnings("all") @Override Rewrite make(Block n) {
    List<Statement> statementList = n.statements();
    WhileStatement whileStatement = (WhileStatement) statementList.get(0);
    ReturnStatement nextReturn = (ReturnStatement) statementList.get(1);
    if (isInfiniteLoop(whileStatement))
      return null;
    Statement body = whileStatement.getBody();
    Statement toChange = null;
    if(az.ifStatement(body)!=null){
      toChange = handleIf(body,nextReturn);
    }
    if (iz.block(body)) {
      List<Statement> blockStatements = ((Block) body).statements();
      for (Statement s : blockStatements){
        if(az.ifStatement(s)!=null){
          toChange = handleIf(s,nextReturn);
        }
        if (compareReturnStatements(nextReturn, az.returnStatement(s))) {
          toChange = s;
          break;
        }
      }
    }
    if (iz.returnStatement(body) && //
        compareReturnStatements(nextReturn, az.returnStatement(body)))
      toChange = body;
    if (toChange == null)
      return null;
    Statement theChange = toChange;
    return new Rewrite(description(), theChange) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        r.replace(theChange, (ASTNode) ((Block) into.s("break;")).statements().get(0), g);
      }
    };
  }

  private Statement handleIf(Statement s, ReturnStatement nextReturn) {
     Statement then = az.ifStatement(s).getThenStatement();
     Statement elze = az.ifStatement(s).getElseStatement();
     if (az.ifStatement(then)!=null)
         return handleIf(then,nextReturn);
     if (az.ifStatement(elze)!=null)
       return handleIf(elze,nextReturn);
     if (compareReturnStatements(nextReturn, az.returnStatement(then)))
       return then;
     if (compareReturnStatements(nextReturn, az.returnStatement(elze)))
       return elze;
     if(az.block(then)!=null){
       List<Statement> statementsList = az.block(then).statements();
       for(Statement sl:statementsList){
         if (az.ifStatement(sl)!=null)
           return handleIf(sl,nextReturn);
       if (az.ifStatement(sl)!=null)
         return handleIf(sl,nextReturn);
       if (compareReturnStatements(nextReturn, az.returnStatement(sl)))
         return sl;
       }
     }
     return null;
  }

  @Override boolean scopeIncludes(final Block s) {
    List<Statement> statementList = s.statements();
    if (s != null && statementList.size() > 1 && statementList.get(0) instanceof WhileStatement //
        && statementList.get(1) instanceof ReturnStatement)
      return true;
    return false;
  }
}
