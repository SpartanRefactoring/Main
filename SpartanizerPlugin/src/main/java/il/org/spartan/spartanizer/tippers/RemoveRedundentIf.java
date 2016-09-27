package il.org.spartan.spartanizer.tippers;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.assemble.*;
import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.java.*;
import il.org.spartan.spartanizer.tipping.*;

/**
 * Simplify if statements as much as possible (or remove them or parts of them) if and only if </br>
 * it doesn't have any side-effect.
 * 
 * @author Dor Ma'ayan
 * @since 2016-09-26
 *
 */
public class RemoveRedundentIf extends ReplaceCurrentNode<IfStatement> implements Kind.Collapse{

  @Override public ASTNode replacement(IfStatement n) {
    if(n==null)
      return null;
    boolean condition = sideEffects.free(n.getExpression());
    boolean then = RemoveRedundent.checkBlock(n.getThenStatement());
    boolean elze = RemoveRedundent.checkBlock(n.getElseStatement());
    if(condition && then && elze || (condition && then && n.getElseStatement()==null))
      return n.getAST().newBlock();
    if(condition && then && !elze && (n.getElseStatement()!=null)){
      return subject.pair(duplicate.of(n.getElseStatement()),null).toNot(duplicate.of(n.getExpression()));
    }
    return null;
  }

  @Override public String description(IfStatement n) {
    return "remove :" + n;
  }
}
