package il.org.spartan.java.cfg;

import static il.org.spartan.java.cfg.CFG.Edges.*;
import static org.eclipse.jdt.core.dom.ASTNode.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;

/** The ASTVisitor of the CFG implementation
 * A simple ASTVisitor which add to each ASTNode
 * entry and exit points according to it's structure.
 * @author Dor Ma'ayan
 * @author Ori Roth
 * @since 2017-06-14 */
public class CFGTraversal extends ASTVisitor {
  /** To all the ends of the first node, put the outgoings of the second node */
  static void chain(final ASTNode n1, final ASTNode n2) {
    ends.of(n1).get().stream().forEach(λ -> outgoing.of(λ).addAll(beginnings.of(n2)));
    beginnings.of(n2).get().stream().forEach(λ -> incoming.of(λ).addAll(ends.of(n1)));
  }
  static void chain(final List<? extends ASTNode> ns) {
    for (int ¢ = 0; ¢ < ns.size() - 1; ++¢)
      chain(ns.get(¢), ns.get(¢ + 1));
  }
  /** Chain with hierarchy */
  static void chainShallow(final ASTNode n1, final ASTNode n2) {
    ends.of(n1).get().stream().forEach(λ -> outgoing.of(λ).add(n2));
    incoming.of(n2).addAll(ends.of(n1));
  }
  static void delegateBeginnings(final ASTNode n1, final ASTNode n2) {
    if (n1 == n2)
      selfBeginnings(n1);
    else
      beginnings.of(n1).addAll(beginnings.of(n2));
  }
  static void delegateEnds(final ASTNode n1, final ASTNode n2) {
    if (n1 == n2)
      selfEnds(n1);
    else
      ends.of(n1).addAll(ends.of(n2));
  }
  static void delegateTo(final ASTNode n1, final ASTNode n2) {
    delegateBeginnings(n1, n2);
    delegateEnds(n1, n2);
  }
  static boolean isBreakTarget(final ASTNode ¢) {
    return iz.isOneOf(¢, SWITCH_STATEMENT, FOR_STATEMENT, ENHANCED_FOR_STATEMENT, WHILE_STATEMENT, DO_STATEMENT);
  }
  static boolean isContinueTarget(final ASTNode ¢) {
    return iz.isOneOf(¢, FOR_STATEMENT, ENHANCED_FOR_STATEMENT, WHILE_STATEMENT, DO_STATEMENT);
  }
  static boolean isEmpty(final ASTNode ¢) {
    return beginnings.of(¢).get().isEmpty() && ends.of(¢).get().isEmpty();
  }
  static boolean isIllegalLeaf(@SuppressWarnings("unused") final ASTNode __) {
    // TOO Roth: complete
    return false;
  }
  static boolean isInfiniteLoop(final ASTNode ¢) {
    return !beginnings.of(¢).get().isEmpty() && ends.of(¢).get().isEmpty();
  }
  static boolean isReturnTarget(final ASTNode ¢) {
    return iz.isOneOf(¢, METHOD_DECLARATION);
  }
  static void leaf(final ASTNode ¢) {
    if (isIllegalLeaf(¢))
      return;
    beginnings.of(¢).add(¢);
    ends.of(¢).add(¢);
  }
  static List<Statement> nonEmptySequence(final List<Statement> statements) {
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
  static void selfBeginnings(final ASTNode ¢) {
    beginnings.of(¢).add(¢);
  }
  static void selfEnds(final ASTNode ¢) {
    ends.of(¢).add(¢);
  }

  Alist trace = new Alist();

  void chainReturn(final ASTNode n) {
    ends.of(n).get().stream().forEach(λ -> outgoing.of(λ).addAll(trace.peek("return")));
  }
  void chainThrow(final ASTNode ¢) {
    chainReturn(¢);
  }
  @Override public void endVisit(final AnonymousClassDeclaration node) {
    final List<BodyDeclaration> bodies = step.bodyDeclarations(node);
    if (bodies.isEmpty())
      return;
    delegateBeginnings(node, bodies.get(0));
    delegateEnds(node, bodies.get(bodies.size() - 1));
    chain(bodies);
  }
  @Override public void endVisit(final ArrayAccess node) {
    final Expression array = node.getArray(), index = node.getIndex();
    delegateBeginnings(node, array);
    selfEnds(node);
    chain(array, index);
    chainShallow(index, node);
  }
  @Override public void endVisit(final ArrayCreation node) {
    selfEnds(node);
    final ArrayInitializer i = node.getInitializer();
    if (i != null) {
      delegateBeginnings(node, i);
      chainShallow(i, node);
    } else {
      @SuppressWarnings("unchecked") final List<Expression> es = node.dimensions();
      delegateBeginnings(node, es.get(0));
      chainShallow(es.get(es.size() - 1), node);
      chain(es);
    }
  }
  @Override public void endVisit(final ArrayInitializer node) {
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
  @Override public void endVisit(final ArrayType node) {
    final List<Expression> dimensions = step.dimensions(node);
    delegateBeginnings(node, dimensions.get(0));
    chain(dimensions);
    chainShallow(dimensions.get(dimensions.size() - 1), node);
    selfEnds(node);
  }
  @Override public void endVisit(final AssertStatement node) {
    final Expression condition = node.getExpression(), message = node.getMessage();
    delegateBeginnings(node, condition);
    delegateEnds(node, condition);
    chain(condition, message);
    chainThrow(message);
  }
  @Override public void endVisit(final Assignment node) {
    final Expression left = node.getLeftHandSide(), right = node.getRightHandSide();
    delegateBeginnings(node, left);
    selfEnds(node);
    chain(left, right);
    chainShallow(right, node);
  }
  @Override public void endVisit(final Block node) {
    final List<Statement> ss = nonEmptySequence(step.statements(node));
    if (ss.isEmpty())
      return;
    delegateBeginnings(node, ss.get(0));
    delegateEnds(node, ss.get(ss.size() - 1));
    chain(ss);
  }
  @Override public void endVisit(final BooleanLiteral node) {
    leaf(node);
  }
  @Override public void endVisit(final BreakStatement node) {
    leaf(node);
    trace.append("break", node);
  }
  @Override public void endVisit(final CastExpression node) {
    final Expression e = node.getExpression();
    delegateBeginnings(node, e);
    chainShallow(e, node);
    selfEnds(node);
  }
  @Override public void endVisit(final CatchClause node) {
    final SingleVariableDeclaration e = node.getException();
    final Block body = node.getBody();
    delegateBeginnings(node, e);
    delegateEnds(node, e);
    delegateEnds(node, body);
    chain(e, body);
  }
  @Override public void endVisit(final CharacterLiteral node) {
    leaf(node);
  }
  @Override public void endVisit(final ClassInstanceCreation creation) {
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
  @Override public void endVisit(final ConditionalExpression node) {
    final Expression condition = node.getExpression(), thenExpression = node.getThenExpression(), elseExpression = node.getElseExpression();
    delegateBeginnings(node, condition);
    delegateEnds(node, thenExpression);
    delegateEnds(node, elseExpression);
    chain(condition, thenExpression);
    chain(condition, elseExpression);
  }
  @Override public void endVisit(final ContinueStatement node) {
    selfBeginnings(node);
    trace.append("continue", node);
  }
  @Override public void endVisit(final DoStatement node) {
    final Statement b = node.getBody();
    final Expression c = step.condition(node);
    final List<ASTNode> route = new LinkedList<>();
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
          for (final ASTNode ¢ : trace.pop("break"))
            delegateEnds(node, az.breakStatement(¢));
      }
    }
  }
  @Override public void endVisit(final ExpressionMethodReference node) {
    leaf(node);
  }
  @Override public void endVisit(final ExpressionStatement node) {
    delegateTo(node, node.getExpression());
  }
  @Override public void endVisit(final FieldAccess node) {
    final Expression left = node.getExpression();
    final SimpleName right = node.getName();
    delegateBeginnings(node, left);
    selfEnds(node);
    chain(left, right);
    chainShallow(right, node);
  }
  @Override public void endVisit(final ForStatement node) {
    final Statement b = node.getBody();
    final List<Expression> is = step.initializers(node);
    final Expression c = step.condition(node);
    final List<Expression> us = step.updaters(node);
    final List<ASTNode> route = new LinkedList<>(is);
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
        for (final ASTNode ¢ : trace.pop("break"))
          delegateEnds(node, az.breakStatement(¢));
      }
    }
  }
  @Override public void endVisit(final IfStatement node) {
    final Expression condition = node.getExpression();
    final Statement then = node.getThenStatement(), elze = node.getElseStatement();
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
  @Override public void endVisit(final InfixExpression node) {
    final Expression left = node.getLeftOperand(), right = node.getRightOperand();
    delegateBeginnings(node, left);
    selfEnds(node);
    chain(left, right);
    chainShallow(right, node);
  }
  @Override public void endVisit(final InstanceofExpression node) {
    final Expression e = node.getLeftOperand();
    delegateBeginnings(node, e);
    chainShallow(e, node);
    selfEnds(node);
  }
  @Override public void endVisit(final LambdaExpression node) {
    leaf(node);
  }
  @Override public void endVisit(final MethodDeclaration node) {
    final List<SingleVariableDeclaration> ps = step.parameters(node);
    final Block b = step.body(node);
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
  @Override public void endVisit(final MethodInvocation node) {
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
  @Override public void endVisit(final NumberLiteral node) {
    leaf(node);
  }
  @Override public void endVisit(final ParenthesizedExpression node) {
    delegateTo(node, node.getExpression());
  }
  @Override public void endVisit(final PostfixExpression node) {
    final Expression e = node.getOperand();
    delegateBeginnings(node, e);
    selfEnds(node);
    chainShallow(e, node);
  }
  @Override public void endVisit(final PrefixExpression node) {
    final Expression e = node.getOperand();
    delegateBeginnings(node, e);
    selfEnds(node);
    chainShallow(e, node);
  }
  @Override public void endVisit(final SimpleName node) {
    leaf(node);
  }
  @Override public void endVisit(final SingleVariableDeclaration node) {
    final Expression i = node.getInitializer();
    if (i == null)
      leaf(node);
    else {
      delegateBeginnings(node, i);
      selfEnds(node);
      chainShallow(i, node);
    }
  }
  @Override public void endVisit(final SuperFieldAccess node) {
    leaf(node);
  }
  @Override public void endVisit(final SuperMethodInvocation node) {
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
  @Override public void endVisit(final ThisExpression node) {
    leaf(node);
  }
  @Override public void endVisit(final TryStatement s) {
    final Block body = s.getBody();
    final List<CatchClause> catches = step.catchClauses(s);
    final Block finaly = s.getFinally();
    delegateBeginnings(s, body);
    delegateEnds(s, body);
    for (final CatchClause ¢ : catches)
      chain(body, ¢);
    if (finaly == null)
      for (final CatchClause ¢ : catches)
        delegateEnds(s, ¢);
    else {
      chain(body, finaly);
      for (final CatchClause ¢ : catches)
        chain(¢, finaly);
      delegateEnds(s, finaly);
    }
  }
  @Override public void endVisit(final VariableDeclarationExpression node) {
    final List<VariableDeclarationFragment> fs = step.fragments(node);
    delegateBeginnings(node, fs.get(0));
    chain(fs);
    chainShallow(fs.get(fs.size() - 1), node);
    selfEnds(node);
  }
  @Override public void endVisit(final VariableDeclarationFragment node) {
    final Expression i = step.initializer(node);
    if (i == null) {
      leaf(node);
      return;
    }
    delegateBeginnings(node, i);
    selfEnds(node);
    chainShallow(i, node);
  }
  @Override public void endVisit(final VariableDeclarationStatement node) {
    final List<VariableDeclarationFragment> fs = step.fragments(node);
    delegateBeginnings(node, fs.get(0));
    selfEnds(node);
    chain(fs);
    chainShallow(fs.get(fs.size() - 1), node);
  }
  @Override public void endVisit(final WhileStatement node) {
    final Statement b = node.getBody();
    final Expression c = step.condition(node);
    final List<ASTNode> route = new LinkedList<>();
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
          for (final ASTNode ¢ : trace.pop("break"))
            delegateEnds(node, ¢);
      }
    }
  }
  @Override public void preVisit(final ASTNode ¢) {
    if (isBreakTarget(¢))
      trace.push("break");
    if (isContinueTarget(¢))
      trace.push("continue");
    if (isReturnTarget(¢))
      trace.push("return");
    // if (iz.labeledStatement(¢))
    // labelMap.put(((LabeledStatement) ¢).getLabel().getIdentifier(), ¢);
  }
}
