package il.org.spartan.spartanizer.engine;

import static il.org.spartan.Utils.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import java.util.*;
import java.util.stream.*;

import org.eclipse.jdt.core.dom.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.plugin.*;

/** A utility class for finding occurrences of an {@link Expression} in an
 * {@link ASTNode}.
 * @author Boris van Sosin <boris.van.sosin @ gmail.com>
 * @author Yossi Gil (major refactoring 2013/07/10)
 * @since 2013/07/01 */
public enum collect {
  /** collects semantic (multiple uses for loops) uses of an variable */
  USES_SEMANTIC {
    @Override ASTVisitor[] collectors(final SimpleName n, final List<SimpleName> into) {
      return as.array(new UsesCollector(into, n));
    }
  },
  /** collects assignments of an variable */
  DEFINITIONS {
    @Override ASTVisitor[] collectors(final SimpleName n, final List<SimpleName> into) {
      return as.array(definitionsCollector(into, n));
    }
  },
  /** collects assignments AND semantic (multiple uses for loops) uses of a
   * variable */
  BOTH_SEMANTIC {
    @Override ASTVisitor[] collectors(final SimpleName n, final List<SimpleName> into) {
      return as.array(new UsesCollector(into, n), lexicalUsesCollector(into, n), definitionsCollector(into, n));
    }
  },
  /** collects assignments AND lexical (single use for loops) uses of an
   * expression */
  BOTH_LEXICAL {
    @Override ASTVisitor[] collectors(final SimpleName n, final List<SimpleName> into) {
      return as.array(lexicalUsesCollector(into, n), definitionsCollector(into, n));
    }
  };
  static final ASTMatcher matcher = new ASTMatcher();

  /** Creates a new instance which holds the occurrences of the provided name in
   * declarations.
   * @param n JD
   * @return A {@link NewGUIApplicator}, with the uses of the provided
   *         identifier within declarations. */
  public static Collector declarationsOf(final SimpleName n) {
    return new Collector(n) {
      @Override public List<SimpleName> in(final ASTNode... ns) {
        final List<SimpleName> $ = an.empty.list();
        as.list(ns).forEach(λ -> λ.accept(declarationsCollector($, name)));
        return $;
      }
    };
  }
  public static Collector definitionsOf(final SimpleName n) {
    return new Collector(n) {
      @Override public List<SimpleName> in(final ASTNode... ns) {
        final List<SimpleName> $ = an.empty.list();
        as.list(ns).forEach(λ -> λ.accept(definitionsCollector($, name)));
        return $;
      }
    };
  }
  /** Finds all the rest (not declarations or definitions) identifier (n) uses.
   * @param n same as "name"
   * @return {@link NewGUIApplicator} of all occurrences which are not
   *         definitions. */
  public static Collector forAllOccurencesExcludingDefinitions(final SimpleName n) {
    return new Collector(n) {
      @Override public List<SimpleName> in(final ASTNode... ns) {
        final List<SimpleName> $ = an.empty.list();
        as.list(ns).forEach(λ -> λ.accept(new UsesCollectorIgnoreDefinitions($, name)));
        return $;
      }
    };
  }
  /** finds all the occurrences of the given name (n) in which it is a
   * {@link ClassInstanceCreation}
   * @param n JD
   * @return a new instance with all unsafe uses of the identifier (n) */
  public static Collector unsafeUsesOf(final SimpleName n) {
    return new Collector(n) {
      @Override public List<SimpleName> in(final ASTNode... ns) {
        final List<SimpleName> $ = an.empty.list();
        as.list(ns).forEach(λ -> λ.accept(new UnsafeUsesCollector($, name)));
        return $;
      }
    };
  }
  /** Creates a new instance which holds all the occurrences of the provided
   * name.
   * @param n JD
   * @return A {@link NewGUIApplicator}, with the uses of the provided
   *         identifier within the provided {@link ASTNode}s array to the in
   *         function.. */
  public static Collector usesOf(final SimpleName n) {
    return new Collector(n) {
      @Override public List<SimpleName> in(final ASTNode... ns) {
        final List<SimpleName> $ = an.empty.list();
        Stream.of(ns).filter(Objects::nonNull).forEach(λ -> λ.accept(new UsesCollector($, name)));
        return $;
      }
    };
  }
  public static Collector usesOf(final String s) {
    return new Collector(s) {
      @Override public List<SimpleName> in(@SuppressWarnings("unused") final ASTNode... __) {
        return null;
      }
      @Override public List<String> inside(final ASTNode... ns) {
        final List<String> $ = an.empty.list();
        Stream.of(ns).filter(Objects::nonNull).forEach(λ -> λ.accept(new StringCollector($, stringName)));
        return $;
      }
    };
  }
  /** Creates an ASTVisitor that adds to the provided SimpleName list all the
   * identifiers of variable declarations expressions, which are identical the
   * provided ASTNode's.
   * @param into - The ASTVisitor's output parameter
   * @param n JD
   * @return <b>ASTVisitor</b> as described above. */
  static ASTVisitor declarationsCollector(final Collection<SimpleName> into, final ASTNode n) {
    // noinspection SameReturnValue,SameReturnValue,SameReturnValue
    return new MethodExplorer.IgnoreNestedMethods() {
      @Override public boolean visit(final ForStatement ¢) {
        return consider(initializers(¢));
      }
      @Override public boolean visit(final TryStatement ¢) {
        return consider(resources(¢));
      }
      @Override public boolean visit(final VariableDeclarationFragment ¢) {
        return add(step.name(¢));
      }
      @Override public boolean visit(final VariableDeclarationStatement ¢) {
        addFragments(fragments(¢));
        return true;
      }
      /** Adds to the list provided by the closure (into) the name of the given
       * candidate.
       * @param candidate to be inserter to the list provided by the closure
       *        (into).
       * @return whether the identifier of the given {@SimpleName} is equal to
       *         the ASTnode's provided by the closure (n) */
      boolean add(final SimpleName ¢) {
        if (wizard.eq(¢, n))
          into.add(¢);
        return true;
      }
      /** Tries to add to the list provided by the closure (into) the names of
       * the {@VariableDeclarationFragment}s given in the param (fs).
       * @param fs is a {@link List} of a {@link VariableDeclarationFragment} */
      void addFragments(final Iterable<VariableDeclarationFragment> fs) {
        fs.forEach(λ -> add(step.name(λ)));
      }
      /** Tries to add to the list provided by the closure (into) the
       * identifiers from all the {@link VariableDeclarationExpression}s from
       * the given list (es).
       * @param xs is a {@link List} of any __ which extends a
       *        {@link Expression}
       * @return whether addFragment() succeeds with the
       *         {@link VariableDeclarationFragment}s from each (extended)
       *         Expression in the parameter. */
      boolean consider(final Iterable<? extends Expression> xs) {
        xs.forEach(λ -> addFragments(fragments(az.variableDeclarationExpression(λ))));
        return true;
      }
    };
  }
  /** @see {@link declarationsCollector} specific comments are provided to
   *      methods which are not taking place in the
   *      {@link declarationsCollector}. */
  static ASTVisitor definitionsCollector(final Collection<SimpleName> into, final ASTNode n) {
    // noinspection SameReturnValue,SameReturnValue,SameReturnValue
    return new MethodExplorer.IgnoreNestedMethods() {
      @Override public boolean visit(final Assignment ¢) {
        return consider(to(¢));
      }
      @Override public boolean visit(final ForStatement ¢) {
        return consider(initializers(¢));
      }
      /** {@link PostfixExpression} can be only INCREMENT OR DECREMENT.
       * @param it JD
       * @return identifier of the operand. */
      @Override public boolean visit(final PostfixExpression it) {
        return consider(it.getOperand());
      }
      /** {@link PrefixExpression} can be more then only INCREMENT OR DECREMENT,
       * but only on that cases it is a definition.
       * @param it JD
       * @return identifier of the operand. */
      @Override public boolean visit(final PrefixExpression it) {
        return !is.in(it.getOperator(), PrefixExpression.Operator.INCREMENT, PrefixExpression.Operator.DECREMENT) || consider(it.getOperand());
      }
      @Override public boolean visit(final TryStatement ¢) {
        return consider(resources(¢));
      }
      @Override public boolean visit(final VariableDeclarationFragment ¢) {
        return add(step.name(¢));
      }
      @Override public boolean visit(final VariableDeclarationStatement ¢) {
        addFragments(fragments(¢));
        return true;
      }
      boolean add(final SimpleName candidate) {
        if (wizard.eq(candidate, n))
          into.add(candidate);
        return true;
      }
      void addFragments(final Iterable<VariableDeclarationFragment> fs) {
        fs.forEach(λ -> add(step.name(λ)));
      }
      /** ThiWs function is needed cause a definition can be not in a
       * declaration form, and then #asVariableDeclarationExpression() will fail
       * @param x JD
       * @return whether the identifier of the given {@link Expression} is equal
       *         to the ASTnode's provided by the closure (n) */
      boolean consider(final Expression ¢) {
        return add(az.simpleName(¢));
      }
      boolean consider(final Iterable<? extends Expression> initializers) {
        initializers.forEach(λ -> addFragments(fragments(az.variableDeclarationExpression(λ))));
        return true;
      }
    };
  }
  // didn't find any use case in which it will be different of
  // usesCollector
  /** Creates an ASTVisitor that adds all explicit uses (by name) of a
   * SimpleName to the provided list.
   * @param into JD
   * @param what JD
   * @return ASTVisitor that adds uses by name of the SimpleName 'what' to the
   *         list 'into' */
  static ASTVisitor lexicalUsesCollector(final Collection<SimpleName> into, final SimpleName what) {
    return usesCollector(what, into, true);
  }
  /** Creates an ASTVisitor that returns all the instances in which the provided
   * SimpleName was used. The instances will be inserted into the provided list.
   * @param what JD
   * @param into JD
   * @param lexicalOnly - True if only explicit matches (by name) are required.
   * @return ASTVisitor that adds all the uses of the SimpleName to the provided
   *         list. */
  private static ASTVisitor usesCollector(final SimpleName what, final Collection<SimpleName> into, final boolean lexicalOnly) {
    // noinspection
    // SameReturnValue,SameReturnValue,SameReturnValue,SameReturnValue,SameReturnValue,SameReturnValue,SameReturnValue,SameReturnValue
    return new ASTVisitor(true) {
      int loopDepth;

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
        return getFieldsOfClass(d).stream().noneMatch(λ -> step.name(λ).subtreeMatch(matcher, what));
      }
      @Override public boolean visit(final Assignment ¢) {
        return collect(from(¢));
      }
      @Override public boolean visit(final CastExpression ¢) {
        return collect(expression(¢));
      }
      @Override public boolean visit(final ClassInstanceCreation ¢) {
        collect(expression(¢));
        return collect(arguments(¢));
      }
      @Override public boolean visit(final DoStatement ¢) {
        ++loopDepth;
        return collect(expression(¢));
      }
      @Override public boolean visit(@SuppressWarnings("unused") final EnhancedForStatement __) {
        ++loopDepth;
        return true;
      }
      @Override public boolean visit(final FieldAccess n) {
        collect(expression(n));
        return false;
      }
      @Override public boolean visit(@SuppressWarnings("unused") final ForStatement __) {
        ++loopDepth;
        return true;
      }
      @Override public boolean visit(final InstanceofExpression ¢) {
        return collect(left(¢));
      }
      @Override public boolean visit(final MethodDeclaration d) {
        return parameters(d).stream().noneMatch(λ -> step.name(λ).subtreeMatch(matcher, what));
      }
      @Override public boolean visit(final MethodInvocation ¢) {
        collect(receiver(¢));
        collect(arguments(¢));
        return false;
      }
      @Override public boolean visit(final QualifiedName ¢) {
        collectExpression(step.name(¢));
        return false;
      }
      @Override public boolean visit(final SimpleName ¢) {
        return collect(¢);
      }
      @Override public boolean visit(@SuppressWarnings("unused") final WhileStatement __) {
        ++loopDepth;
        return true;
      }
      boolean add(final Object ¢) {
        return collect((Expression) ¢);
      }
      boolean collect(final Expression ¢) {
        collectExpression(¢);
        return true;
      }
      boolean collect(final Iterable<?> os) {
        os.forEach(this::add);
        return true;
      }
      void collectExpression(final Expression ¢) {
        if (¢ instanceof SimpleName)
          collectExpression((SimpleName) ¢);
      }
      void collectExpression(final SimpleName ¢) {
        if (!wizard.eq(what, ¢))
          return;
        into.add(¢);
        if (repeated())
          into.add(¢);
      }
      Collection<VariableDeclarationFragment> getFieldsOfClass(final ASTNode classNode) {
        final Collection<VariableDeclarationFragment> $ = an.empty.list();
        // noinspection SameReturnValue
        classNode.accept(new ASTVisitor(true) {
          @Override public boolean visit(final FieldDeclaration ¢) {
            $.addAll(fragments(¢));
            return false;
          }
        });
        return $;
      }
      boolean repeated() {
        return !lexicalOnly && loopDepth > 0;
      }
    };
  }
  /** Creates a function object for searching for a given value.
   * @param n what to search for
   * @return a function object to be used for searching for the parameter in a
   *         given location */
  public Of of(final SimpleName n) {
    return new Of() {
      @Override public List<SimpleName> in(final ASTNode... ¢) {
        return uses(n, ¢);
      }
    };
  }
  /** Creates a function object for searching for a given {@link SimpleName}, as
   * specified by the {@link VariableDeclarationFragment},
   * @param f JD
   * @return a function object to be used for searching for the
   *         {@link SimpleName} embedded in the parameter. */
  public Of of(final VariableDeclarationFragment ¢) {
    return of(step.name(¢));
  }
  /** Lists the required occurrences
   * @param what the expression to search for
   * @param ns the n in which to counted
   * @return list of uses */
  List<SimpleName> uses(final SimpleName what, final ASTNode... ns) {
    final List<SimpleName> $ = an.empty.list();
    as.list(ns).forEach(λ -> as.list(collectors(what, $)).forEach(λ::accept));
    removeDuplicates($);
    $.sort(Comparator.comparingInt(ASTNode::getStartPosition));
    return $;
  }
  abstract ASTVisitor[] collectors(SimpleName n, List<SimpleName> into);
  /** Determines whether a specific SimpleName was used in a
   * {@link ForStatement}.
   * @param s JD
   * @param n JD
   * @return whether the SimpleName is used in a ForStatement's condition,
   *         updaters, or body. */
  public static boolean variableUsedInWhile(final WhileStatement s, final SimpleName n) {
    return !usesOf(n).in(condition(s), body(s)).isEmpty();
  }

  /** An abstract class to carry out the collection process. Should not be
   * instantiated or used directly by clients, other than the use as part of
   * fluent API.
   * @author Yossi Gil
   * @since 2015-09-06 */
  public abstract static class Collector {
    final SimpleName name;
    final String stringName;

    Collector(final SimpleName name) {
      this.name = name;
      stringName = name + "";
    }
    @SuppressWarnings("static-method") public List<String> inside(@SuppressWarnings("unused") final ASTNode... __) {
      return an.empty.list();
    }
    public final Collection<SimpleName> in(final Collection<? extends ASTNode> ¢) {
      return in(¢.toArray(new ASTNode[¢.size()]));
    }
    Collector(final String name) {
      this.name = null;
      stringName = name;
    }
    public abstract List<SimpleName> in(ASTNode... ns);
  }

  /** An auxiliary class which makes it possible to use an easy invocation
   * sequence for the various offerings of the containing class. This class
   * should never be instantiated or inherited by clients.
   * <p>
   * This class realizes the function object concept; an instance of it records
   * the value we search for; it represents the function that, given a location
   * for the search, will carry out the search for the captured value in its
   * location parameter.
   * @see collect#of
   * @author Yossi Gil
   * @since 2013/14/07 */
  public abstract static class Of {
    /** Determine whether this instance occurs in a bunch of expressions
     * @param ns JD
     * @return whether this instance occurs in the Parameter. */
    public boolean existIn(final ASTNode... ¢) {
      return !in(¢).isEmpty();
    }
    /** the method that will carry out the search
     * @param ns where to search
     * @return a list of occurrences of the captured value in the parameter. */
    public abstract List<SimpleName> in(ASTNode... ns);
  }
}
