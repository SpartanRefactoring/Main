package il.org.spartan.spartanizer.ast.navigate;

import static org.eclipse.jdt.core.dom.ASTNode.*;
import static org.eclipse.jdt.core.dom.PrefixExpression.Operator.*;
import static il.org.spartan.java.cfg.CFG.*;

import static java.util.stream.Collectors.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import java.util.*;
import java.util.stream.*;

import org.eclipse.jdt.core.dom.*;

import fluent.ly.*;
import il.org.spartan.java.cfg.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.nominal.*;
import il.org.spartan.utils.*;

/** TODO Yossi Gil: document class
 * @author Yossi Gil
 * @since 2017-04-01 */
@UnderConstruction("Dor -- 10/07/2017")
public enum compute {
  ;
  public static List<ReturnStatement> returns(final ASTNode n) {
    return new ASTMapReducer<List<ReturnStatement>>() {
      @Override protected int[] protect() {
        return new int[] { //
            LAMBDA_EXPRESSION, //
            FOR_STATEMENT, //
            ENHANCED_FOR_STATEMENT, //
            DO_STATEMENT, //
            ANONYMOUS_CLASS_DECLARATION, //
            SWITCH_STATEMENT, //
            TYPE_DECLARATION_STATEMENT, //
        };
      }
      @Override public List<ReturnStatement> map(final ReturnStatement ¢) {
        return as.list(¢);
      }
      @Override public List<ReturnStatement> reduce() {
        return an.empty.list();
      }
      @Override public List<ReturnStatement> reduce(final List<ReturnStatement> ss1, final List<ReturnStatement> ss2) {
        if (ss1 == null)
          return ss2;
        if (ss2 == null)
          return ss1;
        ss1.addAll(ss2);
        return ss1;
      }
    }.map(n);
  }
  public static List<ASTNode> usedSpots(final ASTNode x) {
    return new ASTMapReducer<List<ASTNode>>() {
      @Override public List<ASTNode> reduce() {
        return an.empty.list();
      }
      @Override public List<ASTNode> reduce(final List<ASTNode> ss1, final List<ASTNode> ss2) {
        if (ss1 == null && ss2 == null)
          return an.empty.list();
        if (ss1 == null)
          return ss2;
        if (ss2 == null)
          return ss1;
        ss1.addAll(ss2);
        return ss1;
      }
      @Override protected List<ASTNode> map(final SimpleName ¢) {
        final String $ = ¢.getIdentifier();
        return guessName.of($) != guessName.METHOD_OR_VARIABLE ? reduce() : as.list(¢);
      }
      @Override protected List<ASTNode> map(@SuppressWarnings("unused") final ThisExpression ¢) {
        return reduce();
      }
    }.map(x);
  }
  public static Stream<String> usedIdentifiers(final ASTNode x) {
    return usedSpots(x).stream().map(λ -> λ + "");
  }
  public static List<String> useSpots(final Expression x) {
    return new ASTMapReducer<List<String>>() {
      @Override public List<String> reduce() {
        return an.empty.list();
      }
      @Override public List<String> reduce(final List<String> ss1, final List<String> ss2) {
        if (ss1 == null && ss2 == null)
          return an.empty.list();
        if (ss1 == null)
          return ss2;
        if (ss2 == null)
          return ss1;
        ss1.addAll(ss2);
        return ss1;
      }
      @Override protected List<String> map(final SimpleName ¢) {
        final String $ = ¢.getIdentifier();
        return guessName.of($) != guessName.METHOD_OR_VARIABLE ? reduce() : as.list($);
      }
      @Override protected List<String> map(@SuppressWarnings("unused") final ThisExpression ¢) {
        return reduce();
      }
    }.map(x);
  }
  public static List<ASTNode> updateSpots(final ASTNode... ¢) {
    return Stream.of(¢).map(compute::updateSpots).flatMap(List<ASTNode>::stream).collect(toList());
  }
  public static List<ASTNode> updateSpots(final ASTNode x) {
    final List<ASTNode> $ = new ASTMapReducer<List<ASTNode>>() {
      @Override public List<ASTNode> reduce() {
        return an.empty.list();
      }
      public List<ASTNode> reduce(final ASTNode n, final List<ASTNode> ns) {
        if (n == null)
          return ns;
        if (ns == null)
          return as.list(n);
        ns.add(0, n);
        return ns;
      }
      @Override public List<ASTNode> reduce(final List<ASTNode> l1, final List<ASTNode> l2) {
        if (l1 == null)
          return l2;
        if (l2 == null)
          return l1;
        l1.addAll(l2);
        return l1;
      }
      @Override protected List<ASTNode> map(final Assignment ¢) {
        return reduce(to(¢), super.map(¢));
      }
      @Override protected List<ASTNode> map(final PostfixExpression ¢) {
        return reduce(¢.getOperand(), super.map(¢));
      }
      @Override protected List<ASTNode> map(final PrefixExpression ¢) {
        return reduce(!iz.updating(¢) ? reduce() : as.list(¢.getOperand()), super.map(¢));
      }
    }.map(x);
    return $ != null ? $ : an.empty.list();
  }
  public static List<Statement> decompose(final Expression x) {
    return new ASTMapReducer<List<Statement>>() {
      @Override public List<Statement> reduce() {
        return an.empty.list();
      }
      @Override public List<Statement> reduce(final List<Statement> $, final List<Statement> ss) {
        $.addAll(ss);
        return $;
      }
      @Override protected List<Statement> map(final Assignment ¢) {
        return wizard.listMe(¢);
      }
      @Override protected List<Statement> map(final ClassInstanceCreation ¢) {
        return wizard.listMe(¢);
      }
      @Override protected List<Statement> map(final MethodInvocation ¢) {
        return wizard.listMe(¢);
      }
      @Override protected List<Statement> map(final PostfixExpression ¢) {
        return wizard.listMe(¢);
      }
      /** the operator is not in INCREMENT DECREMENT x \not\in \{\} */
      @Override protected List<Statement> map(final PrefixExpression ¢) {
        return !is.in(¢.getOperator(), INCREMENT, DECREMENT) ? reduce() : wizard.listMe(¢);
      }
      @Override protected List<Statement> map(final SuperMethodInvocation ¢) {
        return wizard.listMe(¢);
      }
    }.map(x);
  }
  public static void cfg(BodyDeclaration root) {
    /* Fill firsts */
    final Map<ASTNode, List<ASTNode>> firsts = new HashMap<>();
    new ASTMapReducer<List<ASTNode>>() {
      @Override protected List<ASTNode> map(MethodDeclaration ¢) {
        List<ASTNode> firstParameter = fold(step.parameters(¢));
        List<ASTNode> firstBody = map(step.body(¢));
        return setFirst(¢, firstParameter.isEmpty() ? firstBody : firstParameter);
      };
      @Override protected List<ASTNode> map(Block ¢) {
        return setFirst(¢, fold(step.statements(¢)));
      };
      @Override protected List<ASTNode> map(VariableDeclarationStatement ¢) {
        return setFirst(¢, fold(step.fragments(¢)));
      };
      @Override protected List<ASTNode> map(VariableDeclarationFragment ¢) {
        List<ASTNode> i = map(step.initializer(¢));
        return i != null ? setFirst(¢, i) : setFirst(¢, ¢);
      };
      @Override protected List<ASTNode> map(InfixExpression ¢) {
        map(step.right(¢));
        return setFirst(¢, map(step.left(¢)));
      };
      @Override protected List<ASTNode> map(SimpleName ¢) {
        return setFirst(¢, ¢);
      };
      @Override public List<ASTNode> reduce() {
        return an.empty.list();
      }
      @Override public List<ASTNode> reduce(List<ASTNode> r1, List<ASTNode> r2) {
        r1.addAll(r2);
        return r1;
      }
      private List<ASTNode> fold(List<? extends ASTNode> parameters) {
        return parameters.stream().map(a -> map(a)).filter(a -> !a.isEmpty()).findFirst().orElse(reduce());
      }
      protected List<ASTNode> setFirst(ASTNode node, List<ASTNode> fs) {
        firsts.put(node, fs);
        return fs;
      }
      protected List<ASTNode> setFirst(ASTNode node, ASTNode f) {
        return setFirst(node, as.list(f));
      }
    }.map(root);
    /* Fill lasts */
    final Map<ASTNode, List<ASTNode>> lasts = new HashMap<>();
    new ASTMapReducer<List<ASTNode>>() {
      @Override protected List<ASTNode> map(MethodDeclaration ¢) {
        List<ASTNode> lastParameter = fold(step.parameters(¢));
        List<ASTNode> lastBody = map(step.body(¢));
        return setLast(¢, lastParameter.isEmpty() ? lastBody : lastParameter);
      };
      @Override protected List<ASTNode> map(Block ¢) {
        return setLast(¢, fold(step.statements(¢)));
      };
      @Override protected List<ASTNode> map(VariableDeclarationStatement ¢) {
        return setLast(¢, fold(step.fragments(¢)));
      };
      @Override protected List<ASTNode> map(VariableDeclarationFragment ¢) {
        List<ASTNode> i = map(step.initializer(¢));
        return i != null ? setLast(¢, i) : setLast(¢, ¢);
      };
      @Override protected List<ASTNode> map(InfixExpression ¢) {
        map(step.left(¢));
        return setLast(¢, map(step.right(¢)));
      };
      @Override protected List<ASTNode> map(SimpleName ¢) {
        return setLast(¢, ¢);
      };
      @Override public List<ASTNode> reduce() {
        return an.empty.list();
      }
      @Override public List<ASTNode> reduce(List<ASTNode> r1, List<ASTNode> r2) {
        r1.addAll(r2);
        return r1;
      }
      private List<ASTNode> fold(List<? extends ASTNode> parameters) {
        List<List<ASTNode>> $ = parameters.stream().map(a -> map(a)).filter(a -> !a.isEmpty()).collect(Collectors.toList());
        return $.isEmpty() ? reduce() : $.get($.size() - 1);
      }
      protected List<ASTNode> setLast(ASTNode node, List<ASTNode> ls) {
        lasts.put(node, ls);
        return ls;
      }
      protected List<ASTNode> setLast(ASTNode node, ASTNode l) {
        return setLast(node, as.list(l));
      }
    }.map(root);
    /* Conclusion out */
    root.accept(new ASTVisitor() {
      @Override public boolean visit(MethodDeclaration node) {
        List<SingleVariableDeclaration> ps = step.parameters(node);
        Block b = step.body(node);
        out(node).addAll(firsts(node));
        if (!ps.isEmpty()) {
          chain(ps);
          chain(ps.get(ps.size() - 1), b);
        }
        return true;
      }
      @Override public boolean visit(Block node) {
        chain(step.statements(node));
        return true;
      }
      @Override public boolean visit(VariableDeclarationStatement node) {
        chain(step.fragments(node));
        return true;
      }
      @Override public boolean visit(VariableDeclarationFragment node) {
        out(step.initializer(node)).add(node);
        return true;
      }
      @Override public boolean visit(InfixExpression node) {
        chain(step.left(node), step.right(node));
        lasts(step.right(node)).stream().forEach(x -> out(x).add(node));
        return true;
      }
      private <T extends ASTNode> void chain(List<T> ts) {
        List<T> ns = ts.stream().filter(t -> !firsts(t).isEmpty()).collect(Collectors.toList());
        for (int i = 0; i < ns.size() - 1; ++i) {
          final int ii = i;
          lasts(ns.get(i)).stream().forEach(l -> out(l).addAll(firsts(ns.get(ii + 1))));
        }
      }
      private void chain(ASTNode n1, ASTNode n2) {
        if (n1 != null && n2 != null)
          lasts(n1).stream().forEach(l -> out(l).addAll(firsts(n2)));
      }
      private List<ASTNode> firsts(ASTNode n) {
        return firsts.containsKey(n) ? firsts.get(n) : an.empty.list();
      }
      private List<ASTNode> lasts(ASTNode n) {
        return lasts.containsKey(n) ? lasts.get(n) : an.empty.list();
      }
    });
  };
  static Nodes in(ASTNode n) {
    if (!property.has(n, CFG.keyIn))
      property.set(n, CFG.keyIn, InNodes.empty());
    return property.get(n, CFG.keyIn);
  }
  static Nodes out(ASTNode n) {
    if (!property.has(n, CFG.keyOut))
      property.set(n, CFG.keyOut, OutNodes.empty());
    return property.get(n, CFG.keyOut);
  }
}
