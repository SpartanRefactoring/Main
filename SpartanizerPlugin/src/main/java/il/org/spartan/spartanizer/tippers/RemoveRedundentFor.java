package il.org.spartan.spartanizer.tippers;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.java.*;
import il.org.spartan.spartanizer.tipping.*;

import il.org.spartan.spartanizer.tipping.RemoveRedundent;
/**
 * Simplify for statements as much as possible (or remove them or parts of them) if and only if </br>
 * it doesn't have any side-effect.
 * 
 * @author Dor Ma'ayan
 * @since 2016-09-26
 *
 */
public class RemoveRedundentFor extends ReplaceCurrentNode<ForStatement> implements Kind.Collapse{

  @Override public ASTNode replacement(ForStatement s) {
    if (s == null)
      return null;
    boolean condition = sideEffects.free(s.getExpression());
    boolean initializers = RemoveRedundent.checkListOfExpressions(s.initializers());
    boolean updaters = RemoveRedundent.checkListOfExpressions(s.updaters());
    return condition && initializers && updaters && RemoveRedundent.checkBlock(s.getBody()) ? s.getAST().newBlock() : null;
  }

  @Override public String description(ForStatement ¢) {
    return "remove :" + ¢;
  }
}
