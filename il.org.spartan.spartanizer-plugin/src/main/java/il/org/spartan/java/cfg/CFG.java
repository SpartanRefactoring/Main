package il.org.spartan.java.cfg;

import static il.org.spartan.java.cfg.CFG.Edges.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import java.util.*;

import static org.eclipse.jdt.core.dom.ASTNode.*;

import org.eclipse.jdt.core.dom.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;

/** The main class of the CFG implementation
 * @author Dor Ma'ayan
 * @author Ori Roth
 * @since 2017-06-14 */
public interface CFG {
  enum Edges {
    beginnings, // Nodes which are first to be evaluated inside the node
    ends, // Nodes which are last to be evaluated inside the node
    incoming, // The node evaluated before you
    outgoing; // The node evaluated after you
    interface Of {
      Of add(ASTNode what);
      Of addAll(Collection<? extends ASTNode> what);
      Of addAll(Of what);
      Of clear();
      Nodes get();
      default Of set(final ASTNode what) {
        return clear().add(what);
      }
    }

    Nodes getNodes(ASTNode ¢) {
      return property.get(¢, getClass().getCanonicalName() + "." + this, Nodes::new);
    }
    Nodes nodes(ASTNode ¢) {
      if (!property.has(¢, getClass().getCanonicalName() + "." + this))
        compute(az.bodyDeclaration(yieldAncestors.untilClass(BodyDeclaration.class).from(¢)));
      return getNodes(¢);
    }
    Of of(final ASTNode to) {
      return new Of() {
        @Override public Of add(final ASTNode what) {
          if (what != null && to != null)
            get().add(what);
          return this;
        }
        @Override public Of addAll(Collection<? extends ASTNode> what) {
          if (what != null && to != null)
            get().addAll(what);
          return this;
        }
        @Override public Of addAll(Of what) {
          if (what != null && to != null)
            get().addAll(what.get().asSet());
          return this;
        }
        @Override public Of clear() {
          if (to != null)
            get().clear();
          return this;
        }
        @Override public Nodes get() {
          return Edges.this.getNodes(to);
        }
      };
    }
  }

  static void compute(final BodyDeclaration d) {
    if (d != null && beginnings.of(d).get().isEmpty())
      d.accept(new ASTVisitor() {
        Stack<List<BreakStatement>> breakTarget = new Stack<>();
        Stack<List<ContinueStatement>> continueTarget = new Stack<>();
        Map<String, ASTNode> labelMap = new LinkedHashMap<>(); // for supporting label continue and label breal
        Stack<ASTNode> returnTarget = anonymous.ly(() -> {
          final Stack<ASTNode> $ = new Stack<>();
          $.push(parent(d));
          return $;
        });

        /** To all the ends of the first node, put the outgoings of the second node */
        void chain(ASTNode n1, ASTNode n2) {
          ends.of(n1).get().stream().forEach(λ -> outgoing.of(λ).addAll(beginnings.of(n2)));
          beginnings.of(n2).get().stream().forEach(λ -> incoming.of(λ).addAll(ends.of(n1)));
        }
        void chain(List<? extends ASTNode> ns) {
          for (int ¢ = 0; ¢ < ns.size() - 1; ++¢)
            chain(ns.get(¢), ns.get(¢ + 1));
        }
        void chainReturn(ASTNode n) {
          ends.of(n).get().stream().forEach(λ -> outgoing.of(λ).add(returnTarget.peek()));
        }
        /** chian with hierarchy */
        void chainShallow(ASTNode n1, ASTNode n2) {
          ends.of(n1).get().stream().forEach(λ -> outgoing.of(λ).add(n2));
          incoming.of(n2).addAll(ends.of(n1));
        }
        void chainThrow(ASTNode n) {
          chainReturn(n);
        }
        void delegateBeginnings(ASTNode n1, ASTNode n2) {
          if (n1 == n2)
            selfBeginnings(n1);
          else
            beginnings.of(n1).addAll(beginnings.of(n2));
        }
        void delegateEnds(ASTNode n1, ASTNode n2) {
          if (n1 == n2)
            selfEnds(n1);
          else
            ends.of(n1).addAll(ends.of(n2));
        }
        private void delegateTo(ASTNode n1, ASTNode n2) {
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
          breakTarget.peek().add(node);
        }
        @Override public void endVisit(CatchClause node) {
          SingleVariableDeclaration e = node.getException();
          Block body = node.getBody();
          delegateBeginnings(node, e);
          delegateEnds(node, e);
          delegateEnds(node, body);
          chain(e, body);
        }
        @Override public void endVisit(ClassInstanceCreation node) {
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
          continueTarget.peek().add(node);
        }
        @Override public void endVisit(DoStatement node) {
          Statement b = node.getBody();
          Expression c = step.condition(node);
          List<ASTNode> route = new LinkedList<>();
          for (int i = 0; i < 2; ++i) {
            if (c != null)
              route.add(c);
            if (!isEmpty(b))
              route.add(b);
          }
          chain(route);
          if (route.isEmpty()) {
            selfBeginnings(node);
          } else if (c == null && breakTarget.peek().isEmpty()) {
            delegateBeginnings(node, route.get(0));
          } else {
            delegateBeginnings(node, route.get(0));
            delegateEnds(node, c);
            if (!breakTarget.isEmpty())
              for (BreakStatement bs : breakTarget.pop()) {
                delegateEnds(node, bs);
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
          List<ASTNode> route = new LinkedList<>();
          route.addAll(is);
          for (int i = 0; i < 2; ++i) {
            if (c != null)
              route.add(c);
            if (!isEmpty(b))
              route.add(b);
            route.addAll(us);
          }
          chain(route);
          if (route.isEmpty()) {
            selfBeginnings(node);
          } else if (c == null && breakTarget.peek().isEmpty()) {
            delegateBeginnings(node, route.get(0));
          } else {
            delegateBeginnings(node, route.get(0));
            if (c != null)
              delegateEnds(node, c);
            for (BreakStatement bs : breakTarget.pop())
              delegateEnds(node, bs);
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
          if (elze != null && !isEmpty(elze)) {
            chain(condition, elze);
            delegateEnds(node, elze);
          }
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
        @Override public void endVisit(TryStatement node) {
          Block body = node.getBody();
          List<CatchClause> catches = step.catchClauses(node);
          Block finaly = node.getFinally();
          delegateBeginnings(node, body);
          delegateEnds(node, body);
          for (CatchClause c : catches) {
            chain(body, c);
          }
          if (finaly == null) {
            for (CatchClause c : catches) {
              delegateEnds(node, c);
            }
          } else {
            chain(body, finaly);
            for (CatchClause c : catches) {
              chain(c, finaly);
            }
            delegateEnds(node, finaly);
          }
        }
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
          for (int i = 0; i < 2; ++i) {
            if (c != null)
              route.add(c);
            if (!isEmpty(b))
              route.add(b);
          }
          chain(route);
          if (route.isEmpty()) {
            selfBeginnings(node);
          } else if (c == null && breakTarget.peek().isEmpty()) {
            delegateBeginnings(node, route.get(0));
          } else {
            delegateBeginnings(node, route.get(0));
            delegateEnds(node, c);
            if (!breakTarget.isEmpty())
              for (BreakStatement bs : breakTarget.pop()) {
                delegateEnds(node, bs);
              }
          }
        }
        boolean isBreakTarget(ASTNode ¢) {
          return iz.isOneOf(¢, SWITCH_STATEMENT, FOR_STATEMENT, ENHANCED_FOR_STATEMENT, WHILE_STATEMENT, DO_STATEMENT);
        }
        boolean isContinueTarget(ASTNode ¢) {
          return iz.isOneOf(¢, FOR_STATEMENT, ENHANCED_FOR_STATEMENT, WHILE_STATEMENT, DO_STATEMENT);
        }
        boolean isEmpty(ASTNode ¢) {
          return beginnings.of(¢).get().isEmpty() && ends.of(¢).get().isEmpty();
        }
        boolean isIllegalLeaf(@SuppressWarnings("unused") ASTNode __) {
          // TOO Roth: complete
          return false;
        }
        boolean isInfiniteLoop(ASTNode ¢) {
          return !beginnings.of(¢).get().isEmpty() && ends.of(¢).get().isEmpty();
        }
        boolean isReturnTarget(@SuppressWarnings("unused") ASTNode __) {
          return false;
        }
        void leaf(ASTNode ¢) {
          if (isIllegalLeaf(¢))
            return;
          beginnings.of(¢).add(¢);
          ends.of(¢).add(¢);
        }
        private List<Statement> nonEmptySequence(List<Statement> statements) {
          final List<Statement> $ = new LinkedList<>();
          for (final Statement s : statements) {
            if (isEmpty(s))
              continue;
            $.add(s);
            if (isInfiniteLoop(s))
              break;
          }
          return $;
        }
        @Override public void postVisit(ASTNode ¢) {
          if (isReturnTarget(¢))
            returnTarget.pop();
          if (iz.labeledStatement(¢))
            labelMap.remove(((LabeledStatement) ¢).getLabel().getIdentifier());
        }
        @Override public void preVisit(ASTNode ¢) {
          if (isBreakTarget(¢))
            breakTarget.push(new ArrayList<>());
          if (isContinueTarget(¢))
            continueTarget.push(new ArrayList<>());
          if (isReturnTarget(¢))
            returnTarget.push(¢);
          if (iz.labeledStatement(¢))
            labelMap.put(((LabeledStatement) ¢).getLabel().getIdentifier(), ¢);
        }
        void selfBeginnings(ASTNode ¢) {
          beginnings.of(¢).add(¢);
        }
        void selfEnds(ASTNode ¢) {
          ends.of(¢).add(¢);
        }
      });
  }
}
