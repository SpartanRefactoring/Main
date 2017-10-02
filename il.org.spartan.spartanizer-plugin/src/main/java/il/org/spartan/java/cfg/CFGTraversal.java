package il.org.spartan.java.cfg;

import static il.org.spartan.java.cfg.CFG.Edges.*;
import static org.eclipse.jdt.core.dom.ASTNode.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;

/** The ASTVisitor of the CFG implementation
 * @author Dor Ma'ayan
 * @author Ori Roth
 * @since 2017-06-14 */
public class CFGTraversal extends ASTVisitor {
  Alist trace = new Alist();

  /** To all the ends of the first node, put the outgoings of the second node */
  static void chain(ASTNode n1, ASTNode n2) {
    ends.of(n1).get().stream().forEach(λ -> outgoing.of(λ).addAll(beginnings.of(n2)));
    beginnings.of(n2).get().stream().forEach(λ -> incoming.of(λ).addAll(ends.of(n1)));
  }
  static void chain(List<? extends ASTNode> ns) {
    for (int ¢ = 0; ¢ < ns.size() - 1; ++¢)
      chain(ns.get(¢), ns.get(¢ + 1));
  }
  void chainReturn(ASTNode n) {
    ends.of(n).get().stream().forEach(λ -> outgoing.of(λ).addAll(trace.peek("return")));
  }
  /** Chain with hierarchy */
  static void chainShallow(ASTNode n1, ASTNode n2) {
    ends.of(n1).get().stream().forEach(λ -> outgoing.of(λ).add(n2));
    incoming.of(n2).addAll(ends.of(n1));
  }
  void chainThrow(ASTNode ¢) {
    chainReturn(¢);
  }
  static void delegateBeginnings(ASTNode n1, ASTNode n2) {
    if (n1 == n2)
      selfBeginnings(n1);
    else
      beginnings.of(n1).addAll(beginnings.of(n2));
  }
  static void delegateEnds(ASTNode n1, ASTNode n2) {
    if (n1 == n2)
      selfEnds(n1);
    else
      ends.of(n1).addAll(ends.of(n2));
  }
  static void delegateTo(ASTNode n1, ASTNode n2) {
    delegateBeginnings(n1, n2);
    delegateEnds(n1, n2);
  }
  @Override public void endVisit(ArrayAccess node) {
    Expression array = node.getArray(), index = node.getIndex();
    delegateBeginnings(node, array);
    selfEnds(node);
    chain(array, index);
    chainShallow(index, node);
  }
  @Override public void endVisit(ArrayCreation node) {
    selfEnds(node);
    ArrayInitializer i = node.getInitializer();
    if (i != null) {
      delegateBeginnings(node, i);
      chainShallow(i, node);
    } else {
      @SuppressWarnings("unchecked") List<Expression> es = node.dimensions();
      delegateBeginnings(node, es.get(0));
      chainShallow(es.get(es.size() - 1), node);
      chain(es);
    }
  }
  @Override public void endVisit(ArrayInitializer node) {
    final List<Expression> es = step.expressions(node);
    if (es.isEmpty())
      leaf(node);
    else {
      delegateBeginnings(node, es.get(0));
      chain(es);
      chainShallow(es.get(es.size() - 1), node);
      selfEnds(node);
    }
  }
  @Override public void endVisit(AssertStatement node) {
    Expression condition = node.getExpression(), message = node.getMessage();
    delegateBeginnings(node, condition);
    delegateEnds(node, condition);
    chain(condition, message);
    chainThrow(message);
  }
  @Override public void endVisit(Assignment node) {
    Expression left = node.getLeftHandSide(), right = node.getRightHandSide();
    delegateBeginnings(node, left);
    selfEnds(node);
    chain(left, right);
    chainShallow(right, node);
  }
  @Override public void endVisit(Block node) {
    List<Statement> ss = nonEmptySequence(step.statements(node));
    if (ss.isEmpty())
      return;
    delegateBeginnings(node, ss.get(0));
    delegateEnds(node, ss.get(ss.size() - 1));
    chain(ss);
  }
  @Override public void endVisit(BreakStatement node) {
    leaf(node);
    trace.append("break", node);
  }
  @Override public void endVisit(CatchClause node) {
    SingleVariableDeclaration e = node.getException();
    Block body = node.getBody();
    delegateBeginnings(node, e);
    delegateEnds(node, e);
    delegateEnds(node, body);
    chain(e, body);
  }
  @Override public void endVisit(ClassInstanceCreation creation) {
    final Expression e = creation.getExpression();
    final List<Expression> arguments = step.arguments(creation), es = new LinkedList<>();
    if (e != null)
      es.add(e);
    es.addAll(arguments);
    if (es.isEmpty())
      leaf(creation);
    else {
      delegateBeginnings(creation, es.get(0));
      selfEnds(creation);
      chain(es);
      chainShallow(es.get(es.size() - 1), creation);
    }
  }
  @Override public void endVisit(ConditionalExpression node) {
    Expression condition = node.getExpression(), thenExpression = node.getThenExpression(), elseExpression = node.getElseExpression();
    delegateBeginnings(node, condition);
    delegateEnds(node, thenExpression);
    delegateEnds(node, elseExpression);
    chain(condition, thenExpression);
    chain(condition, elseExpression);
  }
  @Override public void endVisit(ContinueStatement node) {
    selfBeginnings(node);
    trace.append("continue", node);
  }
  @Override public void endVisit(DoStatement node) {
    Statement b = node.getBody();
    Expression c = step.condition(node);
    List<ASTNode> route = new LinkedList<>();
    for (int ¢ = 0; ¢ < 2; ++¢) {
      if (c != null)
        route.add(c);
      if (!isEmpty(b))
        route.add(b);
    }
    chain(route);
    if (route.isEmpty())
      selfBeginnings(node);
    else {
      delegateBeginnings(node, route.get(0));
      if (c != null || !trace.peek("break").isEmpty()) {
        delegateEnds(node, c);
        if (!trace.peek("break").isEmpty())
          for (ASTNode ¢ : trace.pop("break"))
            delegateEnds(node, az.breakStatement(¢));
      }
    }
  }
  @Override public void endVisit(ExpressionMethodReference node) {
    leaf(node);
  }
  @Override public void endVisit(ExpressionStatement node) {
    delegateTo(node, node.getExpression());
  }
  @Override public void endVisit(FieldAccess node) {
    Expression left = node.getExpression();
    SimpleName right = node.getName();
    delegateBeginnings(node, left);
    selfEnds(node);
    chain(left, right);
    chainShallow(right, node);
  }
  @Override public void endVisit(ForStatement node) {
    Statement b = node.getBody();
    List<Expression> is = step.initializers(node);
    Expression c = step.condition(node);
    List<Expression> us = step.updaters(node);
    List<ASTNode> route = new LinkedList<>(is);
    for (int ¢ = 0; ¢ < 2; ++¢) {
      if (c != null)
        route.add(c);
      if (!isEmpty(b))
        route.add(b);
      route.addAll(us);
    }
    chain(route);
    if (route.isEmpty())
      selfBeginnings(node);
    else {
      delegateBeginnings(node, route.get(0));
      if (c != null || !trace.peek("break").isEmpty()) {
        if (c != null)
          delegateEnds(node, c);
        for (ASTNode ¢ : trace.pop("break"))
          delegateEnds(node, az.breakStatement(¢));
      }
    }
  }
  @Override public void endVisit(IfStatement node) {
    Expression condition = node.getExpression();
    Statement then = node.getThenStatement(), elze = node.getElseStatement();
    delegateBeginnings(node, condition);
    if (then != null && !isEmpty(then)) {
      chain(condition, then);
      delegateEnds(node, then);
    }
    if (elze == null || isEmpty(elze))
      return;
    chain(condition, elze);
    delegateEnds(node, elze);
  }
  @Override public void endVisit(InfixExpression node) {
    Expression left = node.getLeftOperand(), right = node.getRightOperand();
    delegateBeginnings(node, left);
    selfEnds(node);
    chain(left, right);
    chainShallow(right, node);
  }
  @Override public void endVisit(InstanceofExpression node) {
    Expression e = node.getLeftOperand();
    delegateBeginnings(node, e);
    chainShallow(e, node);
    selfEnds(node);
  }
  @Override public void endVisit(LambdaExpression node) {
    leaf(node);
  }
  @Override public void endVisit(MethodDeclaration node) {
    List<SingleVariableDeclaration> ps = step.parameters(node);
    Block b = step.body(node);
    if (ps == null || ps.isEmpty()) {
      chainReturn(b);
      return;
    }
    chain(ps);
    if (isEmpty(b))
      chainReturn(ps.get(ps.size() - 1));
    else {
      chain(ps.get(ps.size() - 1), b);
      chainReturn(b);
    }
  }
  @Override public void endVisit(MethodInvocation node) {
    final Expression e = node.getExpression();
    final List<Expression> arguments = step.arguments(node), es = new LinkedList<>();
    if (e != null)
      es.add(e);
    es.addAll(arguments);
    if (es.isEmpty())
      leaf(node);
    else {
      delegateBeginnings(node, es.get(0));
      selfEnds(node);
      chain(es);
      chainShallow(es.get(es.size() - 1), node);
    }
  }
  @Override public void endVisit(NumberLiteral node) {
    leaf(node);
  }
  @Override public void endVisit(ParenthesizedExpression node) {
    delegateTo(node, node.getExpression());
  }
  @Override public void endVisit(PostfixExpression node) {
    Expression e = node.getOperand();
    delegateBeginnings(node, e);
    selfEnds(node);
    chainShallow(e, node);
  }
  @Override public void endVisit(PrefixExpression node) {
    Expression e = node.getOperand();
    delegateBeginnings(node, e);
    selfEnds(node);
    chainShallow(e, node);
  }
  @Override public void endVisit(SimpleName node) {
    leaf(node);
  }
  @Override public void endVisit(SingleVariableDeclaration node) {
    final Expression i = node.getInitializer();
    if (i == null)
      leaf(node);
    else {
      delegateBeginnings(node, i);
      selfEnds(node);
      chainShallow(i, node);
    }
  }
  @Override public void endVisit(SuperFieldAccess node) {
    leaf(node);
  }
  @Override public void endVisit(SuperMethodInvocation node) {
    final List<Expression> es = step.arguments(node);
    if (es.isEmpty())
      leaf(node);
    else {
      delegateBeginnings(node, es.get(0));
      selfEnds(node);
      chain(es);
      chainShallow(es.get(es.size() - 1), node);
    }
  }
  @Override public void endVisit(ThisExpression node) {
    leaf(node);
  }
  @Override public void endVisit(TryStatement s) {
    Block body = s.getBody();
    List<CatchClause> catches = step.catchClauses(s);
    Block finaly = s.getFinally();
    delegateBeginnings(s, body);
    delegateEnds(s, body);
    for (CatchClause ¢ : catches)
      chain(body, ¢);
    if (finaly == null)
      for (CatchClause ¢ : catches)
        delegateEnds(s, ¢);
    else {
      chain(body, finaly);
      for (CatchClause ¢ : catches)
        chain(¢, finaly);
      delegateEnds(s, finaly);
    }
  }
  // @Override public void endVisit(SwitchStatement node) {
  // Expression condition = node.getExpression();
  // List<SwitchCase> statements = extract.switchCases(node);
  // delegateBeginnings(node,condition);
  // if(trace.peek("break") != null)
  // for(ASTNode.)
  //
  // }
  @Override public void endVisit(VariableDeclarationExpression node) {
    @SuppressWarnings("unchecked") List<VariableDeclarationFragment> fs = node.fragments();
    delegateBeginnings(node, fs.get(0));
    chain(fs);
    chainShallow(fs.get(fs.size() - 1), node);
    selfEnds(node);
  }
  @Override public void endVisit(VariableDeclarationFragment node) {
    Expression i = step.initializer(node);
    if (i == null) {
      leaf(node);
      return;
    }
    delegateBeginnings(node, i);
    selfEnds(node);
    chainShallow(i, node);
  }
  @Override public void endVisit(VariableDeclarationStatement node) {
    List<VariableDeclarationFragment> fs = step.fragments(node);
    delegateBeginnings(node, fs.get(0));
    selfEnds(node);
    chain(fs);
    chainShallow(fs.get(fs.size() - 1), node);
  }
  @Override public void endVisit(WhileStatement node) {
    Statement b = node.getBody();
    Expression c = step.condition(node);
    List<ASTNode> route = new LinkedList<>();
    for (int ¢ = 0; ¢ < 2; ++¢) {
      if (c != null)
        route.add(c);
      if (!isEmpty(b))
        route.add(b);
    }
    chain(route);
    if (route.isEmpty())
      selfBeginnings(node);
    else {
      delegateBeginnings(node, route.get(0));
      if (c != null || !trace.peek("break").isEmpty()) {
        delegateEnds(node, c);
        if (trace.peek("break") != null)
          for (ASTNode ¢ : trace.pop("break"))
            delegateEnds(node, ¢);
      }
    }
  }
  static boolean isBreakTarget(ASTNode ¢) {
    return iz.isOneOf(¢, SWITCH_STATEMENT, FOR_STATEMENT, ENHANCED_FOR_STATEMENT, WHILE_STATEMENT, DO_STATEMENT);
  }
  static boolean isContinueTarget(ASTNode ¢) {
    return iz.isOneOf(¢, FOR_STATEMENT, ENHANCED_FOR_STATEMENT, WHILE_STATEMENT, DO_STATEMENT);
  }
  static boolean isReturnTarget(ASTNode ¢) {
    return iz.isOneOf(¢, METHOD_DECLARATION);
  }
  static boolean isEmpty(ASTNode ¢) {
    return beginnings.of(¢).get().isEmpty() && ends.of(¢).get().isEmpty();
  }
  static boolean isIllegalLeaf(@SuppressWarnings("unused") ASTNode __) {
    // TOO Roth: complete
    return false;
  }
  static boolean isInfiniteLoop(ASTNode ¢) {
    return !beginnings.of(¢).get().isEmpty() && ends.of(¢).get().isEmpty();
  }
  static void leaf(ASTNode ¢) {
    if (isIllegalLeaf(¢))
      return;
    beginnings.of(¢).add(¢);
    ends.of(¢).add(¢);
  }
  static List<Statement> nonEmptySequence(List<Statement> statements) {
    final List<Statement> $ = new LinkedList<>();
    for (final Statement ¢ : statements) {
      if (isEmpty(¢))
        continue;
      $.add(¢);
      if (isInfiniteLoop(¢))
        break;
    }
    return $;
  }
  @Override public void preVisit(ASTNode ¢) {
    if (isBreakTarget(¢))
      trace.push("break");
    if (isContinueTarget(¢))
      trace.push("continue");
    if (isReturnTarget(¢))
      trace.push("return");
    // if (iz.labeledStatement(¢))
    // labelMap.put(((LabeledStatement) ¢).getLabel().getIdentifier(), ¢);
  }
  static void selfBeginnings(ASTNode ¢) {
    beginnings.of(¢).add(¢);
  }
  static void selfEnds(ASTNode ¢) {
    ends.of(¢).add(¢);
  }
}
