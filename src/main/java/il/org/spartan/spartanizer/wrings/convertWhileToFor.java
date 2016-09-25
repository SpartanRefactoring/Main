//package il.org.spartan.spartanizer.wrings;
//
//import java.util.*;
//
//import org.eclipse.jdt.core.dom.*;
//import org.eclipse.jdt.core.dom.rewrite.*;
//import org.eclipse.text.edits.*;
//
//import il.org.spartan.spartanizer.ast.*;
//import il.org.spartan.spartanizer.dispatch.*;
//import il.org.spartan.spartanizer.engine.*;
//import il.org.spartan.spartanizer.wringing.*;
//
///** @author Dor Ma'ayan
// * @since 2016-09-23 */
//public class convertWhileToFor extends ReplaceCurrentNode<WhileStatement> implements Kind.Collapse {
//  @Override public String description(WhileStatement n) {
//    return "Convert the while loop : /n" + n //
//        + "/n to a traditional for loop";
//  }
//
//  @Override public boolean prerequisite(final WhileStatement ¢) {
//    return ¢ != null && !iz.containsContinueStatement(¢.getBody());
//  }
//
//  @Override public ASTNode replacement(WhileStatement s) {
//    final Statement body = s.getBody();
//    final Expression whileExpression = s.getExpression();
//    final ASTNode lastStatement = hop.lastStatement(body);
//    if (iz.assignment(lastStatement) || iz.incrementOrDecrement(lastStatement) || iz.expressionStatement(lastStatement)) {
//      final ForStatement forLoop = s.getAST().newForStatement();
//      // System.out.println(forLoop.updaters());
//      System.out.println(lastStatement.getNodeType());
//      List<Expression> lst = new ArrayList<Expression>();
//      forLoop.getAST().newExpression();
//      forLoop.updaters().add(az.expressionStatement(lastStatement).getExpression());
//      // add(az.expressionStatement(lastStatement));
//      forLoop.initializers().add(az.expression(whileExpression));
//      return forLoop;
//    }
//    return null;
//  }
//}
