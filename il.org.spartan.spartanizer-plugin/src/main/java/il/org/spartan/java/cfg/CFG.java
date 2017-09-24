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

        @Override public void preVisit(ASTNode ¢) {
          if (isBreakTarget(¢))
            breakTarget.push(¢);
          if (isContinueTarget(¢))
            continueTarget.push(¢);
          if (isReturnTarget(¢))
            returnTarget.push(¢);
          if (iz.labeledStatement(¢))
            labelMap.put(((LabeledStatement) ¢).getLabel().getIdentifier(), ¢);
        }
        @Override public void postVisit(ASTNode ¢) {
          if (isBreakTarget(¢))
            breakTarget.pop();
          if (isContinueTarget(¢))
            continueTarget.pop();
          if (isReturnTarget(¢))
            returnTarget.pop();
          if (iz.labeledStatement(¢))
            labelMap.remove(((LabeledStatement) ¢).getLabel().getIdentifier());
        }
        @Override public void endVisit(SimpleName node) {
          leaf(node);
        }
        @Override public void endVisit(NumberLiteral node) {
          leaf(node);
        }
        @Override public void endVisit(ExpressionMethodReference node) {
          leaf(node);
        }
        @Override public void endVisit(ParenthesizedExpression node) {
          delegateTo(node, node.getExpression());
        }
        @Override public void endVisit(ExpressionStatement node) {
          delegateTo(node, node.getExpression());
        }
        @Override public void endVisit(ArrayAccess node) {
          Expression array = node.getArray(), index = node.getIndex();
          delegateBeginnings(node, array);
          selfEnds(node);
          chain(array, index);
          chainShallow(index, node);
        }
        @Override public void endVisit(ArrayCreation node) {
          List<Expression> dimensions = step.dimensions(node);
          ArrayInitializer initializer = node.getInitializer();
          List<Expression> initializers = step.expressions(initializer);
          if (dimensions.size() != 0) {
            delegateBeginnings(node, dimensions.get(0));
            selfEnds(node);
            chain(dimensions);
            chainShallow(dimensions.get(dimensions.size() - 1), node);
          } else {
            delegateBeginnings(node, initializers.get(0));
            selfEnds(node);
            chain(initializers);
            chainShallow(initializers.get(initializers.size() - 1), node);
          }
        }
        @Override public void endVisit(SuperFieldAccess node) {
          Name name = node.getQualifier();
          SimpleName field = node.getName();
          if (name != null) {
            delegateBeginnings(node, name);
            delegateEnds(node, field);
            chain(name, field);
          } else {
            leaf(field);
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
        @Override public void endVisit(FieldAccess node) {
          Expression left = node.getExpression();
          SimpleName right = node.getName();
          delegateBeginnings(node, left);
          selfEnds(node);
          chain(left, right);
          chainShallow(right, node);
        }
        @Override public void endVisit(ConditionalExpression node) {
          Expression condition = node.getExpression(), thenExpression = node.getThenExpression(), elseExpression = node.getElseExpression();
          delegateBeginnings(node, condition);
          delegateEnds(node, thenExpression);
          delegateEnds(node, elseExpression);
          chain(condition, thenExpression);
          chain(condition, elseExpression);
        }
        @Override public void endVisit(ForStatement node) {
          Statement body = node.getBody();
          List<Expression> initializers = step.initializers(node);
          List<Expression> updaters = step.updaters(node);
          Expression condition = node.getExpression();
          delegateBeginnings(node, initializers.get(0));
          chain(initializers);
          chain(initializers.get(initializers.size() - 1), condition);
          chain(condition, updaters.get(0));
          chain(updaters.get(updaters.size() - 1), body);
          chain(body, condition);
          delegateEnds(node, condition);
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
        @Override public void endVisit(VariableDeclarationStatement node) {
          List<VariableDeclarationFragment> fs = step.fragments(node);
          delegateBeginnings(node, fs.get(0));
          delegateEnds(node, fs.get(fs.size() - 1));
          chain(fs);
        }
        @Override public void endVisit(SingleVariableDeclaration node) {
          if (node.getInitializer() == null)
            leaf(node);
          else {
            Expression initializer = node.getInitializer();
            delegateBeginnings(node, initializer);
            selfEnds(node);
            chain(initializer, node);
          }
        }
        @Override public void endVisit(AssertStatement node) {
          Expression condition = node.getExpression(), message = node.getMessage();
          delegateBeginnings(node, condition);
          delegateEnds(node, message);
          chain(condition, message);
        }
        @Override public void endVisit(Block node) {
          List<Statement> ss = step.statements(node);
          if (ss.size() == 1)
            leaf(ss.get(0));
          else {
            delegateBeginnings(node, ss.get(0));
            delegateEnds(node, ss.get(ss.size() - 1));
            chain(ss);
          }
        }
        @Override public void endVisit(Assignment node) {
          Expression left = node.getLeftHandSide(), right = node.getRightHandSide();
          delegateBeginnings(node, left);
          selfEnds(node);
          chain(left, right);
          chainShallow(right, node);
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
        void leaf(ASTNode ¢) {
          if (isIllegalLeaf(¢))
            return;
          beginnings.of(¢).add(¢);
          ends.of(¢).add(¢);
        }
        boolean isIllegalLeaf(@SuppressWarnings("unused") ASTNode __) {
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
        private void delegateTo(ASTNode n1, ASTNode n2) {
          delegateBeginnings(n1, n2);
          delegateEnds(n1, n2);
        }
        void selfBeginnings(ASTNode ¢) {
          beginnings.of(¢).add(¢);
        }
        void selfEnds(ASTNode ¢) {
          ends.of(¢).add(¢);
        }
        void chain(List<? extends ASTNode> ns) {
          for (int ¢ = 0; ¢ < ns.size() - 1; ++¢)
            chain(ns.get(¢), ns.get(¢ + 1));
        }
        /** To all the ends of the first node, put the outgoings of the second
         * node */
        void chain(ASTNode n1, ASTNode n2) {
          ends.of(n1).get().stream().forEach(λ -> outgoing.of(λ).addAll(beginnings.of(n2)));
          beginnings.of(n2).get().stream().forEach(λ -> incoming.of(λ).addAll(ends.of(n1)));
        }
        /** chian with hierarchy */
        void chainShallow(ASTNode n1, ASTNode n2) {
          ends.of(n1).get().stream().forEach(λ -> outgoing.of(λ).add(n2));
          incoming.of(n2).addAll(ends.of(n1));
        }
        void chainReturn(ASTNode n) {
          ends.of(n).get().stream().forEach(λ -> outgoing.of(λ).add(returnTarget.peek()));
        }
        boolean isBreakTarget(ASTNode ¢) {
          return iz.isOneOf(¢, SWITCH_STATEMENT, FOR_STATEMENT, ENHANCED_FOR_STATEMENT, WHILE_STATEMENT, DO_STATEMENT);
        }
        boolean isContinueTarget(ASTNode ¢) {
          return iz.isOneOf(¢, FOR_STATEMENT, ENHANCED_FOR_STATEMENT, WHILE_STATEMENT, DO_STATEMENT);
        }
        boolean isReturnTarget(@SuppressWarnings("unused") ASTNode __) {
          return false;
        }
        boolean isEmpty(ASTNode ¢) {
          return beginnings.of(¢).get().isEmpty();
        }
      });
  }

  enum Edges {
    beginnings, // Nodes which are first to be evaluated inside the node
    ends, // Nodes which are last to be evaluated inside the node
    incoming, // The node evaluated before you
    outgoing; // The node evaluated after you
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
    Nodes nodes(ASTNode ¢) {
      if (!property.has(¢, getClass().getCanonicalName() + "." + this))
        compute(az.bodyDeclaration(yieldAncestors.untilClass(BodyDeclaration.class).from(¢)));
      return getNodes(¢);
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
