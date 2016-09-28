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

  @Override public ASTNode replacement(IfStatement s) {
    if (s == null)
      return null;
    boolean condition = !haz.sideEffects(s.getExpression());
    boolean then = RemoveRedundent.checkBlock(s.getThenStatement());
    boolean elze = RemoveRedundent.checkBlock(s.getElseStatement());
    return condition && (then && (elze || s.getElseStatement() == null)) ? s.getAST().newBlock()
        : !condition || !then || elze || s.getElseStatement() == null ? null
            : subject.pair(duplicate.of(s.getElseStatement()), null).toNot(duplicate.of(s.getExpression()));
  }

  @Override public String description(IfStatement ¢) {
    return "remove :" + ¢;
  }
}
