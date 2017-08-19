package il.org.spartan.java.cfg;

import static org.eclipse.jdt.core.dom.ASTNode.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;
import static il.org.spartan.java.cfg.CFG.Edges.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;

/** The main class of the CFG implementation
 * @author Dor Ma'ayan
 * @author Ori Roth
 * @since 2017-06-14 */
public interface CFG {
  static void compute(final BodyDeclaration d) {
    if (d != null && beginnings.of(d).get().isEmpty())
      d.accept(new ASTVisitor() {
        Stack<ASTNode> breakTarget = new Stack<>();
        Stack<ASTNode> continueTarget = new Stack<>();
        Map<String, ASTNode> labelMap = new LinkedHashMap<>();
        Stack<ASTNode> returnTarget = anonymous.ly(() -> {
          final Stack<ASTNode> $ = new Stack<>();
          $.push(parent(d));
          return $;
        });

        @Override public void preVisit(ASTNode n) {
          if (isBreakTarget(n))
            breakTarget.push(n);
          if (isContinueTarget(n))
            continueTarget.push(n);
          if (isReturnTarget(n))
            returnTarget.push(n);
          if (iz.labeledStatement(n))
            labelMap.put(((LabeledStatement) n).getLabel().getIdentifier(), n);
        }
        @Override public void postVisit(ASTNode n) {
          if (isBreakTarget(n))
            breakTarget.pop();
          if (isContinueTarget(n))
            continueTarget.pop();
          if (isReturnTarget(n))
            returnTarget.pop();
          if (iz.labeledStatement(n))
            labelMap.remove(((LabeledStatement) n).getLabel().getIdentifier());
        }
        @Override public void endVisit(SimpleName node) {
          leaf(node);
        }
        @Override public void endVisit(NumberLiteral node) {
          leaf(node);
        }
        @Override public void endVisit(InfixExpression node) {
          Expression left = node.getLeftOperand(), right = node.getRightOperand();
          delegateBeginnings(node, left);
          selfEnds(node);
          chain(left, right);
          chainShallow(right, node);
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
        @Override public void endVisit(Assignment node) {
          Expression left = node.getLeftHandSide(), right = node.getRightHandSide();
          delegateBeginnings(node, left);
          selfEnds(node);
          chain(left, right);
          chainShallow(right,node);
        }
        @Override public void endVisit(VariableDeclarationStatement node) {
          List<VariableDeclarationFragment> fs = step.fragments(node);
          delegateBeginnings(node, fs.get(0));
          delegateEnds(node, fs.get(fs.size() - 1));
          chain(fs);
        }
        @Override public void endVisit(Block node) {
          List<Statement> ss = step.statements(node);
          if (ss.isEmpty())
            return;
          delegateBeginnings(node, ss.get(0));
          delegateEnds(node, ss.get(ss.size() - 1));
          chain(ss);
        }
        @Override public void endVisit(MethodDeclaration node) {
          List<SingleVariableDeclaration> ps = step.parameters(node);
          Block b = step.body(node);
          if (ps.isEmpty()) {
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
        @Override public void endVisit(SingleVariableDeclaration node) {
          Expression e = node.getInitializer();
          SimpleName n = node.getName();
          if (e == null) {
            delegateBeginnings(node, n);
            delegateEnds(node, n);
            return;
          }
          delegateBeginnings(node, e);
          delegateEnds(node, n);
          chain(e, n);
        }
        @Override public void endVisit(MethodInvocation node) {
          List<Expression> es = an.empty.list();
          es.addAll(step.arguments(node));
          Expression e = step.expression(node);
          if (e != null)
            es.add(e);
          if (es.isEmpty()) {
            leaf(node);
            return;
          }
          delegateBeginnings(node, es.get(0));
          selfEnds(node);
          chain(es);
          chainShallow(es.get(es.size() - 1), node);
        }
        @Override public void endVisit(ExpressionStatement node) {
          Expression e = step.expression(node);
          if (e == null)
            return;
          delegateBeginnings(node, e);
          delegateEnds(node, e);
        }
        @Override public void endVisit(ArrayInitializer node) {
          List<Expression> es = step.expressions(node);
          if (es.isEmpty()) {
            leaf(node);
            return;
          }
          chain(es);
          chainShallow(es.get(es.size() - 1), node);
        }
        @SuppressWarnings("unchecked") @Override public void endVisit(ArrayCreation node) {
          List<Expression> es = an.empty.list();
          es.addAll(node.dimensions());
          ArrayInitializer i = node.getInitializer();
          if (i != null)
            es.add(i);
          if (es.isEmpty()) {
            leaf(node);
            return;
          }
          delegateBeginnings(node, es.get(0));
          selfEnds(node);
          chain(es);
          chainShallow(es.get(es.size() - 1), node);
        }
        void leaf(ASTNode n) {
          if (!isIllegalLeaf(n)) {
            beginnings.of(n).add(n);
            ends.of(n).add(n);
          }
        }
        boolean isIllegalLeaf(@SuppressWarnings("unused") ASTNode n) {
          // TOO Roth: complete
          return false;
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
        void selfBeginnings(ASTNode n) {
          beginnings.of(n).add(n);
        }
        void selfEnds(ASTNode n) {
          ends.of(n).add(n);
        }
        void chain(List<? extends ASTNode> ns) {
          for (int i = 0; i < ns.size() - 1; ++i)
            chain(ns.get(i), ns.get(i + 1));
        }
        void chain(ASTNode n1, ASTNode n2) {
          ends.of(n1).get().stream().forEach(λ -> outgoing.of(λ).addAll(beginnings.of(n2)));
          beginnings.of(n2).get().stream().forEach(λ -> incoming.of(λ).addAll(ends.of(n1)));
        }
        void chainShallow(ASTNode n1, ASTNode n2) {
          ends.of(n1).get().stream().forEach(λ -> outgoing.of(λ).add(n2));
          incoming.of(n2).addAll(ends.of(n1));
        }
        void chainReturn(ASTNode n) {
          ends.of(n).get().stream().forEach(λ -> outgoing.of(λ).add(returnTarget.peek()));
        }
        boolean isBreakTarget(ASTNode n) {
          return iz.isOneOf(n, SWITCH_STATEMENT, FOR_STATEMENT, ENHANCED_FOR_STATEMENT, WHILE_STATEMENT, DO_STATEMENT);
        }
        boolean isContinueTarget(ASTNode n) {
          return iz.isOneOf(n, FOR_STATEMENT, ENHANCED_FOR_STATEMENT, WHILE_STATEMENT, DO_STATEMENT);
        }
        boolean isReturnTarget(@SuppressWarnings("unused") ASTNode __) {
          return false;
        }
        boolean isEmpty(ASTNode n) {
          return beginnings.of(n).get().isEmpty();
        }
      });
  }

  enum Edges {
    beginnings, //
    ends, //
    incoming, //
    outgoing; //
    Of of(final ASTNode to) {
      return new Of() {
        @Override public Nodes get() {
          return Edges.this.getNodes(to);
        }
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
      };
    }
    Nodes getNodes(ASTNode ¢) {
      return property.get(¢, getClass().getCanonicalName() + "." + this, Nodes::new);
    }
    Nodes nodes(ASTNode n) {
      if (!property.has(n, getClass().getCanonicalName() + "." + this))
        compute(az.bodyDeclaration(yieldAncestors.untilClass(BodyDeclaration.class).from(n)));
      return getNodes(n);
    }

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
  }
}
