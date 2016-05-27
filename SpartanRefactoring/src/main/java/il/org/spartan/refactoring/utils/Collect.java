package il.org.spartan.refactoring.utils;

import static il.org.spartan.refactoring.utils.Funcs.asSimpleName;
import static il.org.spartan.refactoring.utils.Funcs.left;
import static il.org.spartan.refactoring.utils.Funcs.right;
import static il.org.spartan.refactoring.utils.Funcs.same;
import static il.org.spartan.utils.Utils.asArray;
import static il.org.spartan.utils.Utils.in;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTMatcher;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.AnonymousClassDeclaration;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.CastExpression;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.DoStatement;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.FieldAccess;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.InstanceofExpression;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.PostfixExpression;
import org.eclipse.jdt.core.dom.PrefixExpression;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.TryStatement;
import org.eclipse.jdt.core.dom.VariableDeclarationExpression;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.WhileStatement;

import il.org.spartan.utils.Utils;

/**
 * A utility class for finding occurrences of an {@link Expression} in an
 * {@link ASTNode}.
 *
 * @author Boris van Sosin <boris.van.sosin @ gmail.com>
 * @author Yossi Gil <yossi.gil @ gmail.com> (major refactoring 2013/07/10)
 * @since 2013/07/01
 */
public enum Collect {
  /** collects semantic (multiple uses for loops) uses of an expression */
  USES_SEMANTIC {
    @Override ASTVisitor[] collectors(final SimpleName n, final List<SimpleName> into) {
      return asArray(new UsesCollector(into, n));
    }
  },
  /** collects assignments of an expression */
  DEFINITIONS {
    @Override ASTVisitor[] collectors(final SimpleName n, final List<SimpleName> into) {
      return asArray(definitionsCollector(into, n));
    }
  },
  /**
   * collects assignments AND semantic (multiple uses for loops) uses of an
   * expression
   */
  BOTH_SEMANTIC {
    @Override ASTVisitor[] collectors(final SimpleName n, final List<SimpleName> into) {
      return asArray(new UsesCollector(into, n), lexicalUsesCollector(into, n), definitionsCollector(into, n));
    }
  },
  /**
   * collects assignments AND lexical (single use for loops) uses of an
   * expression
   */
  BOTH_LEXICAL {
    @Override ASTVisitor[] collectors(final SimpleName n, final List<SimpleName> into) {
      return asArray(lexicalUsesCollector(into, n), definitionsCollector(into, n));
    }
  };
  static final ASTMatcher matcher = new ASTMatcher();

  public static Collector definitionsOf(final SimpleName n) {
    return new Collector(n) {
      @Override public List<SimpleName> in(final ASTNode... ns) {
        final List<SimpleName> $ = new ArrayList<>();
        for (final ASTNode n : ns)
          n.accept(definitionsCollector($, name));
        return $;
      }
    };
  }
  public static Collector forAllOccurencesExcludingDefinitions(final SimpleName n) {
    return new Collector(n) {
      @Override public List<SimpleName> in(final ASTNode... ns) {
        final List<SimpleName> $ = new ArrayList<>();
        for (final ASTNode n : ns)
          n.accept(new UsesCollectorIgnoreDefinitions($, name));
        return $;
      }
    };
  }
  public static Collector usesOf(final SimpleName n) {
    return new Collector(n) {
      @Override public List<SimpleName> in(final ASTNode... ns) {
        return this.usesOf(new ArrayList<>(), ns);
      }
    };
  }
  static ASTVisitor definitionsCollector(final List<SimpleName> into, final ASTNode n) {
    return new MethodExplorer.IgnoreNestedMethods() {
      @Override public boolean visit(final Assignment a) {
        return consider(left(a));
      }
      @Override public boolean visit(final ForStatement s) {
        return consider(expose.initializers(s));
      }
      @Override public boolean visit(final PostfixExpression it) {
        return !in(it.getOperator(), PostfixExpression.Operator.INCREMENT, PostfixExpression.Operator.DECREMENT)
            || consider(it.getOperand());
      }
      @Override public boolean visit(final PrefixExpression it) {
        return consider(it.getOperand());
      }
      @Override public boolean visit(final TryStatement s) {
        return consider(expose.resources(s));
      }
      @Override public boolean visit(final VariableDeclarationFragment f) {
        return add(f.getName());
      }
      @Override public boolean visit(final VariableDeclarationStatement s) {
        addFragments(expose.fragments(s));
        return true;
      }
      boolean add(final SimpleName candidate) {
        if (same(candidate, n))
          into.add(candidate);
        return true;
      }
      boolean consider(final Expression e) {
        return add(asSimpleName(e));
      }
      private void addFragments(final List<VariableDeclarationFragment> fs) {
        for (final VariableDeclarationFragment f : fs)
          add(f.getName());
      }
      private boolean consider(final List<VariableDeclarationExpression> initializers) {
        for (final Object o : initializers)
          if (o instanceof VariableDeclarationExpression)
            addFragments(expose.fragments((VariableDeclarationExpression) o));
        return true;
      }
    };
  }
  static ASTVisitor lexicalUsesCollector(final List<SimpleName> into, final SimpleName what) {
    return usesCollector(what, into, true);
  }
  private static ASTVisitor usesCollector(final SimpleName what, final List<SimpleName> into, final boolean lexicalOnly) {
    return new ASTVisitor() {
      private int loopDepth = 0;

      @Override public void endVisit(@SuppressWarnings("unused") final DoStatement __) {
        --loopDepth;
      }
      @Override public void endVisit(@SuppressWarnings("unused") final EnhancedForStatement __) {
        --loopDepth;
      }
      @Override public void endVisit(@SuppressWarnings("unused") final ForStatement __) {
        --loopDepth;
      }
      @Override public void endVisit(@SuppressWarnings("unused") final WhileStatement __) {
        --loopDepth;
      }
      @Override public boolean visit(final AnonymousClassDeclaration d) {
        for (final VariableDeclarationFragment f : getFieldsOfClass(d))
          if (f.getName().subtreeMatch(matcher, what))
            return false;
        return true;
      }
      @Override public boolean visit(final Assignment a) {
        return collect(right(a));
      }
      @Override public boolean visit(final CastExpression e) {
        return collect(e.getExpression());
      }
      @Override public boolean visit(final ClassInstanceCreation c) {
        collect(c.getExpression());
        return collect(c.arguments());
      }
      @Override public boolean visit(final DoStatement s) {
        ++loopDepth;
        return collect(s.getExpression());
      }
      @Override public boolean visit(@SuppressWarnings("unused") final EnhancedForStatement __) {
        ++loopDepth;
        return true;
      }
      @Override public boolean visit(final FieldAccess n) {
        collect(n.getExpression());
        return false;
      }
      @Override public boolean visit(final ForStatement s) {
        ++loopDepth;
        return true;
      }
      @Override public boolean visit(final InstanceofExpression e) {
        return collect(left(e));
      }
      @Override public boolean visit(final MethodDeclaration d) {
        /* Now: this is a bit complicated. Java allows declaring methods in
         * anonymous classes in which the formal parameters hide variables in
         * the enclosing scope. We don't want to collect them as uses of the
         * variable */
        for (final Object o : d.parameters())
          if (((SingleVariableDeclaration) o).getName().subtreeMatch(matcher, what))
            return false;
        return true;
      }
      @Override public boolean visit(final MethodInvocation i) {
        collect(i.getExpression());
        collect(i.arguments());
        return false;
      }
      @Override public boolean visit(final QualifiedName n) {
        collectExpression(n.getName());
        return false;
      }
      @Override public boolean visit(final SimpleName n) {
        return collect(n);
      }
      @Override public boolean visit(final WhileStatement s) {
        ++loopDepth;
        return true;
      }
      void collectExpression(final Expression e) {
        if (e instanceof SimpleName)
          collectExpression((SimpleName) e);
      }
      void collectExpression(final SimpleName n) {
        if (!same(what, n))
          return;
        into.add(n);
        if (repeated())
          into.add(n);
      }
      private boolean add(final Object o) {
        return collect((Expression) o);
      }
      private boolean collect(final Expression e) {
        collectExpression(e);
        return true;
      }
      private boolean collect(@SuppressWarnings("rawtypes") final List os) {
        for (final Object o : os)
          add(o);
        return true;
      }
      private List<VariableDeclarationFragment> getFieldsOfClass(final ASTNode classNode) {
        final List<VariableDeclarationFragment> $ = new ArrayList<>();
        classNode.accept(new ASTVisitor() {
          @Override public boolean visit(final FieldDeclaration d) {
            $.addAll(expose.fragments(d));
            return false;
          }
        });
        return $;
      }
      private boolean repeated() {
        return !lexicalOnly && loopDepth > 0;
      }
    };
  }
  /**
   * Creates a function object for searching for a given value.
   *
   * @param n what to search for
   * @return a function object to be used for searching for the parameter in a
   *         given location
   */
  public Of of(final SimpleName n) {
    return new Of() {
      @Override public List<SimpleName> in(final ASTNode... ns) {
        return collect(n, ns);
      }
    };
  }
  /**
   * Creates a function object for searching for a given {@link SimpleName}, as
   * specified by the {@link VariableDeclarationFragment},
   *
   * @param f JD
   * @return a function object to be used for searching for the
   *         {@link SimpleName} embedded in the parameter.
   */
  public Of of(final VariableDeclarationFragment f) {
    return of(f.getName());
  }
  /**
   * Lists the required occurrences
   *
   * @param what the expression to search for
   * @param ns the n in which to counted
   * @return the list of uses
   */
  final List<SimpleName> collect(final SimpleName what, final ASTNode... ns) {
    final List<SimpleName> $ = new ArrayList<>();
    for (final ASTNode n : ns)
      for (final ASTVisitor v : collectors(what, $))
        n.accept(v);
    Utils.removeDuplicates($);
    Collections.sort($, new Comparator<Expression>() {
      @Override public int compare(final Expression e1, final Expression e2) {
        return e1.getStartPosition() - e2.getStartPosition();
      }
    });
    return $;
  }
  abstract ASTVisitor[] collectors(final SimpleName n, final List<SimpleName> into);

  /**
   * An auxiliary class which makes it possible to use an easy invocation
   * sequence for the various offerings of the containing class. This class
   * should never be instantiated or inherited by clients.
   * <p>
   * This class realizes the function object concept; an instance of it records
   * the value we search for; it represents the function that, given a location
   * for the search, will carry out the search for the captured value in its
   * location parameter.
   *
   * @see Collect#of
   * @author Yossi Gil <yossi.gil @ gmail.com>
   * @since 2013/14/07
   */
  public static abstract class Of {
    /**
     * Determine whether this instance occurs in a bunch of expressions
     *
     * @param ns JD
     * @return <code><b>true</b></code> <i>iff</i> this instance occurs in the
     *         Parameter.
     */
    public boolean existIn(final ASTNode... ns) {
      return !in(ns).isEmpty();
    }
    /**
     * the method that will carry out the search
     *
     * @param ns where to search
     * @return a list of occurrences of the captured value in the parameter.
     */
    public abstract List<SimpleName> in(ASTNode... ns);
  }

  /**
   * An abstract class to carry out the collection process. Should not be
   * instantiated or used directly by clients, other than the use as part of
   * fluent API.
   *
   * @author Yossi Gil
   * @since 2015-09-06
   */
  public abstract static class Collector {
    protected final SimpleName name;

    Collector(final SimpleName name) {
      this.name = name;
    }
    public abstract List<SimpleName> in(final ASTNode... ns);
    protected List<SimpleName> usesOf(final List<SimpleName> $, final ASTNode... ns) {
      for (final ASTNode n : ns)
        n.accept(new UsesCollector($, name));
      return $;
    }
  }
}
