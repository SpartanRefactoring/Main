package il.org.spartan.java.cfg;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.safety.*;

/** The main class of the CFG implementation
 * @author Dor Ma'ayan
 * @author Ori Roth
 * @since 2017-06-14 */
public interface CFG {
  enum KEYS {
    INCOMING, //
    OUTGOING, //
    FIRST, //
    LAST; //
    To to(ASTNode to) {
      return new To() {
        @Override public To add(ASTNode what) {
          if (what != null && to != null)
            property.get(to, KEYS.this.getClass().getCanonicalName() + KEYS.this, () -> new Nodes()).add(what);
          return this;
        }
        @Override public To clear() {
          if (to != null)
            property.get(to, KEYS.this.getClass().getCanonicalName() + KEYS.this, () -> new Nodes()).clear();
          return this;
        }
      };
    }

    interface To {
      To add(ASTNode what);
      To clear();
    }
  }

  static void compute(BodyDeclaration n) {
    new VoidMapReducer() {
      Stack<ASTNode> breakTarget = new Stack<>();
      Map<String, ASTNode> labelMap = new LinkedHashMap<>();
      Stack<ASTNode> continueTarget = new Stack<>();
      Stack<ASTNode> returnTarget = new Stack<>();
      {
        returnTarget.push(parent(n));
      }

      @Override public Void map(final ASTNode ¢) {
        if (iz.isOneOf(¢, ASTNode.LINE_COMMENT))
          return null;
        return super.map(¢);
      }
      @Override protected Void map(List<Statement> ss) {
        Statement previous = null;
        for (Statement s : ss) {
          KEYS.INCOMING.to(s).add(previous);
          KEYS.OUTGOING.to(previous).add(s);
          previous = s;
        }
        return super.map(ss);
      }
      @Override protected Void map(IfStatement ¢) {
        KEYS.FIRST.to(¢).add(expression(¢));
        KEYS.OUTGOING.to(expression(¢)).add(then(¢)).add(elze(¢));
        return null;
      }
      @Override protected Void map(LabeledStatement __) {
        return null;
      }
      @Override protected Void map(ReturnStatement ¢) {
        KEYS.OUTGOING.to(¢).clear();
        return super.map(¢);
      }
      @Override protected Void map(BreakStatement ¢) {
        KEYS.OUTGOING.to(¢).clear().add(breakTarget.peek());
        return super.map(¢);
      }
      @Override protected Void map(ContinueStatement ¢) {
        KEYS.OUTGOING.to(¢).clear().add(continueTarget.peek());
        return super.map(¢);
      }
    }.map(n);
  }
}
