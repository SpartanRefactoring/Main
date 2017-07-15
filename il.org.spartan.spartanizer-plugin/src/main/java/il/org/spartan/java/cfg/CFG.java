package il.org.spartan.java.cfg;

import static il.org.spartan.spartanizer.ast.navigate.step.*;
import static il.org.spartan.java.cfg.CFG.Edges.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.ast.safety.*;

/** The main class of the CFG implementation
 * @author Dor Ma'ayan
 * @author Ori Roth
 * @since 2017-06-14 */
public interface CFG {
  static void compute(final BodyDeclaration d) {
    new VoidMapReducer() {
      Stack<ASTNode> breakTarget = new Stack<>();
      Stack<ASTNode> continueTarget = new Stack<>();
      Map<String, ASTNode> labelMap = new LinkedHashMap<>();
      Stack<ASTNode> returnTarget = anonymous.ly(() -> {
        final Stack<ASTNode> $ = new Stack<>();
        $.push(parent(d));
        return $;
      });

      @Override public Void map(final ASTNode ¢) {
        if (iz.isOneOf(¢, ASTNode.LINE_COMMENT))
          return null;
        return super.map(¢);
      }
      @Override protected Void map(final BreakStatement ¢) {
        outgoing.of(¢).set(breakTarget.peek());
        return super.map(¢);
      }
      @Override protected Void map(final ContinueStatement ¢) {
          outgoing.of(¢).clear();
        final SimpleName label = ¢.getLabel();
        if (label == null)
          outgoing.of(¢).add(continueTarget.peek());
        else {
          final ASTNode astNode = labelMap.get(label + "");
          if (astNode != null)
            outgoing.of(¢).add(astNode);
        }
        return super.map(¢);
      }
      @Override protected Void map(final IfStatement ¢) {
        beginnings.of(¢).add(expression(¢));
        outgoing.of(expression(¢)).add(then(¢)).add(elze(¢));
        return super.map(¢);
      }
      @Override protected Void map(final LabeledStatement ¢) {
        labelMap.put(¢.getLabel() + "", ¢);
        return super.map(¢);
      }
      @Override protected Void map(final List<Statement> ss) {
        Statement previous = null;
        for (final Statement current : ss) {
          incoming.of(current).add(previous);
          outgoing.of(previous).add(current);
          previous = current;
        }
        return super.map(ss);
      }
      @Override protected Void map(final ReturnStatement ¢) {
        outgoing.of(¢).set(returnTarget.peek());
        return super.map(¢);
      }
    }.map(d);
  }

  enum Edges {
    beginnings, //
    ends, //
    incoming, //
    outgoing; //
    Of of(final ASTNode to) {
      return new Of() {
        Nodes nodes() {
          return Edges.this.nodes(to);
        }
        @Override public Of add(final ASTNode what) {
          if (what != null && to != null)
            nodes().add(what);
          return this;
        }
        @Override public Of clear() {
          if (to != null)
            nodes().clear();
          return this;
        }
      };
    }
    Nodes nodes(ASTNode ¢) {
      return property.get(¢, getClass().getCanonicalName() + "." + this, Nodes::new);
    }

    interface Of {
      Of add(ASTNode what);
      Of clear();
      default Of set(final ASTNode what) {
        return clear().add(what);
      }
    }
  }
}
