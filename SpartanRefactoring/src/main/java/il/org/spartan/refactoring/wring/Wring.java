package il.org.spartan.refactoring.wring;

import il.org.spartan.misc.*;
import il.org.spartan.refactoring.preferences.*;
import il.org.spartan.refactoring.suggestions.*;
import il.org.spartan.refactoring.utils.*;

import java.util.*;

import org.eclipse.jdt.annotation.*;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.Assignment.Operator;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import static il.org.spartan.refactoring.utils.Funcs.*;
import static org.eclipse.jdt.core.dom.Assignment.Operator.*;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;

/**
 * A wring is a refactoring that works on an {@link ASTNode}. Such a
 * transformation make a single simplification of the tree. A wring is so small
 * that it is idempotent: Applying a wring to the output of itself is the empty
 * operation.
 *
 * @param <N>
 *          type of node which triggers the transformation.
 * @author Yossi Gil
 * @author Daniel Mittelman <code><mittelmania [at] gmail.com></code>
 * @since 2015-07-09
 */
public abstract class Wring<N extends ASTNode> extends Suggestion implements Kind {
  protected CompilationUnit compilationUnit;
  protected Scalpel scalpel;

  abstract String description(N n);
  /**
   * @param u
   *          current compilation unit
   * @return this wring
   */
  public Wring<N> set(final CompilationUnit compilationUnit) {
    return Context.this.starting.vrom(compilationUnit);
  }
  /**
   * @param r
   *          rewriter
   * @param g
   *          text edit group
   * @return this wring
   */
  public Wring<N> createScalpel(final ASTRewrite r) {
    scalpel = Source.getScalpel(compilationUnit, r, null);
    return this;
  }
  /**
   * @param r
   *          rewriter
   * @param g
   *          text edit group
   * @return this wring
   */
  public Wring<N> createScalpel(final ASTRewrite r, final TextEditGroup g) {
    scalpel = Source.getScalpel(compilationUnit, r, g);
    return this;
  }
  /**
   * Determine whether the parameter is "eligible" for application of this
   * instance. The parameter must be within the scope of the current instance.
   *
   * @param n
   *          JD
   * @return <code><b>true</b></code> <i>iff</i> the argument is eligible for
   *         the simplification offered by this object.
   */
  boolean eligible(final N n) {
    return true;
  }
  Suggestion make(final N n) {
    return make(n, null);
  }
  Suggestion make(final N n, @SuppressWarnings("unused") final ExclusionManager __) {
    return make(n);
  }
  /**
   * Determines whether this {@link Wring} object is not applicable for a given
   * {@link PrefixExpression} is within the "scope" of this . Note that a
   * {@link Wring} is applicable in principle to an object, but that actual
   * application will be vacuous.
   *
   * @param e
   *          JD
   * @return <code><b>true</b></code> <i>iff</i> the argument is noneligible for
   *         the simplification offered by this object.
   * @see #eligible(InfixExpression)
   */
  public final boolean nonEligible(final N n) {
    return !eligible(n);
  }
  /**
   * Determines whether this {@link Wring} object is applicable for a given
   * {@link InfixExpression} is within the "scope" of this . Note that it could
   * be the case that a {@link Wring} is applicable in principle to an object,
   * but that actual application will be vacuous.
   *
   * @param n
   *          JD
   * @return <code><b>true</b></code> <i>iff</i> the argument is within the
   *         scope of this object
   */
  public boolean scopeIncludes(final N n) {
    return make(n, null) != null;
  }

  abstract class AbstractSorting extends ReplaceCurrentNode<InfixExpression> {
    @Override final String description(final InfixExpression e) {
      return "Reorder operands of " + e.getOperator();
    }
    abstract boolean sort(List<Expression> operands);
  }

  abstract class InfixSorting extends AbstractSorting {
    @Override boolean eligible(final InfixExpression e) {
      final List<Expression> es = extract.allOperands(e);
      return !Wrings.mixedLiteralKind(es) && sort(es);
    }
    @Override Expression replacement(final InfixExpression e) {
      final List<Expression> operands = extract.allOperands(e);
      return !sort(operands) ? null : Subject.operands(operands).to(e.getOperator());
    }
  }

  static abstract class InfixSortingOfCDR extends AbstractSorting {
    @Override boolean eligible(final InfixExpression e) {
      final List<Expression> es = extract.allOperands(e);
      es.remove(0);
      return !Wrings.mixedLiteralKind(es) && sort(es);
    }
    @Override Expression replacement(final InfixExpression e) {
      final List<Expression> operands = extract.allOperands(e);
      final Expression first = operands.remove(0);
      if (!sort(operands))
        return null;
      operands.add(0, first);
      return Subject.operands(operands).to(e.getOperator());
    }
  }

  abstract class ReplaceCurrentNode<N extends ASTNode> extends Wring<N> {
    @Override final Suggestion make(final N n) {
      return !eligible(n) ? null : new Suggestion(description(n), n) {
        @SuppressWarnings("unused") @Override public void go(final ASTRewrite r, final TextEditGroup g) {
          scalpel.operate(n).replaceWith(replacement(n));
        }
      };
    }
    abstract ASTNode replacement(N n);
    @Override boolean scopeIncludes(final N n) {
      return replacement(n) != null;
    }
  }

  /**
   * Similar to {@link ReplaceCurrentNode}, but with an {@link ExclusionManager}
   */
  static abstract class ReplaceCurrentNodeExclude<N extends ASTNode> extends Wring<N> {
    @Override public final Suggestion make(final N n, final ExclusionManager m) {
      return !eligible(n) ? null : new Suggestion(description(n), n) {
        @SuppressWarnings("unused") @Override public void go(final ASTRewrite r, final TextEditGroup g) {
          scalpel.operate(n).replaceWith(replacement(n, m));
        }
      };
    }
    abstract ASTNode replacement(N n, final ExclusionManager m);
    @Override boolean scopeIncludes(final N n) {
      return replacement(n, new ExclusionManager()) != null;
    }
  }

  /**
   * MultipleReplaceCurrentNode replaces multiple nodes in current statement
   * with multiple nodes (or a single node).
   *
   * @author Ori Roth <code><ori.rothh [at] gmail.com></code>
   * @since 2016-04-25
   */
  abstract class MultipleReplaceCurrentNode<N extends ASTNode> extends Wring<N> {
    abstract ASTRewrite go(ASTRewrite r, N n, TextEditGroup g, List<ASTNode> bss, List<ASTNode> crs);
    @Override Suggestion make(final N n) {
      return new Suggestion(description(n), n) {
        @Override public void go(final ASTRewrite r, final TextEditGroup g) {
          final List<ASTNode> bss = new ArrayList<>();
          final List<ASTNode> crs = new ArrayList<>();
          MultipleReplaceCurrentNode.this.go(r, n, g, bss, crs);
          if (bss.size() != crs.size() && crs.size() != 1)
            return; // indicates bad wring design
          final boolean ucr = crs.size() == 1;
          final int s = bss.size();
          for (int i = 0; i < s; ++i)
            scalpel.operate(bss.get(i)).replaceWith(crs.get(ucr ? 0 : i));
        }
      };
    }
    @Override boolean scopeIncludes(final N n) {
      return go(ASTRewrite.create(n.getAST()), n, null, new ArrayList<>(), new ArrayList<>()) != null;
    }
  }

  /* TODO complete cases 1 and 2 of IfCommandsSequencerNoElseSingletonSequencer */
  static abstract class ReplaceToNextStatement<N extends ASTNode> extends Wring<N> {
    abstract ASTRewrite go(ASTRewrite r, N n, Statement nextStatement, TextEditGroup g);
    @Override Suggestion make(final N n, final ExclusionManager exclude) {
      final Statement nextStatement = extract.nextStatement(n);
      if (nextStatement == null || !eligible(n))
        return null;
      exclude.exclude(nextStatement);
      return new Suggestion(description(n), n, nextStatement) {
        @Override public void go(final ASTRewrite r, final TextEditGroup g) {
          ASTNode p = n;
          while (!(p instanceof Statement))
            p = p.getParent();
          scalpel.operate(nextStatement, n);
          ReplaceToNextStatement.this.go(r, n, nextStatement, g);
        }
      };
    }
    @Override boolean scopeIncludes(final N n) {
      final Statement nextStatement = extract.nextStatement(n);
      return nextStatement != null && go(ASTRewrite.create(n.getAST()), n, nextStatement, null) != null;
    }
  }

  /**
   * MultipleReplaceToNextStatement replaces multiple nodes in current statement
   * with multiple nodes (or a single node) in next statement.
   *
   * @author Ori Roth <code><ori.rothh [at] gmail.com></code>
   * @since 2016-04-25
   */
  static abstract class MultipleReplaceToNextStatement<N extends ASTNode> extends Wring<N> {
    abstract ASTRewrite go(ASTRewrite r, N n, Statement nextStatement, TextEditGroup g, List<ASTNode> bss, List<ASTNode> crs);
    @Override Suggestion make(final N n, final ExclusionManager exclude) {
      final Statement nextStatement = extract.nextStatement(n);
      if (nextStatement == null || !eligible(n))
        return null;
      exclude.exclude(nextStatement);
      return new Suggestion(description(n), n, nextStatement) {
        @Override public void go(final ASTRewrite r, final TextEditGroup g) {
          final List<ASTNode> bss = new ArrayList<>();
          final List<ASTNode> crs = new ArrayList<>();
          MultipleReplaceToNextStatement.this.go(r, n, nextStatement, g, bss, crs);
          if (bss.size() != crs.size() && crs.size() != 1)
            return; // indicates bad wring design
          final boolean ucr = crs.size() == 1;
          final int s = bss.size();
          for (int i = 0; i < s; ++i)
            scalpel.operate(bss.get(i)).addComments(nextStatement).replaceWith(crs.get(ucr ? 0 : i));
        }
      };
    }
    @Override boolean scopeIncludes(final N n) {
      final Statement nextStatement = extract.nextStatement(n);
      return nextStatement != null && go(ASTRewrite.create(n.getAST()), n, nextStatement, null, new ArrayList<>(), new ArrayList<>()) != null;
    }
  }

  static abstract class VariableDeclarationFragementAndStatement extends ReplaceToNextStatement<VariableDeclarationFragment> {
    static InfixExpression.Operator asInfix(final Assignment.Operator o) {
      return o == PLUS_ASSIGN ? PLUS : o == MINUS_ASSIGN ? MINUS : o == TIMES_ASSIGN ? TIMES : o == DIVIDE_ASSIGN ? DIVIDE : o == BIT_AND_ASSIGN ? AND : o == BIT_OR_ASSIGN ? OR
          : o == BIT_XOR_ASSIGN ? XOR : o == REMAINDER_ASSIGN ? REMAINDER : o == LEFT_SHIFT_ASSIGN ? LEFT_SHIFT //
              : o == RIGHT_SHIFT_SIGNED_ASSIGN ? RIGHT_SHIFT_SIGNED //
                  : o == RIGHT_SHIFT_UNSIGNED_ASSIGN ? RIGHT_SHIFT_UNSIGNED : null;
    }
    static boolean hasAnnotation(final VariableDeclarationFragment f) {
      return hasAnnotation((VariableDeclarationStatement) f.getParent());
    }
    @SuppressWarnings("unchecked") static boolean hasAnnotation(final VariableDeclarationStatement s) {
      return hasAnnotation(s.modifiers());
    }
    static boolean hasAnnotation(final List<IExtendedModifier> ms) {
      for (final IExtendedModifier m : ms)
        if (m.isAnnotation())
          return true;
      return false;
    }
    static Expression assignmentAsExpression(final Assignment a) {
      final Operator o = a.getOperator();
      return o == ASSIGN ? duplicate(right(a)) : Subject.pair(left(a), right(a)).to(asInfix(o));
    }
    static boolean doesUseForbiddenSiblings(final VariableDeclarationFragment f, final ASTNode... ns) {
      for (final VariableDeclarationFragment b : forbiddenSiblings(f))
        if (Collect.BOTH_SEMANTIC.of(b).existIn(ns))
          return true;
      return false;
    }
    static List<VariableDeclarationFragment> forbiddenSiblings(final VariableDeclarationFragment f) {
      final List<VariableDeclarationFragment> $ = new ArrayList<>();
      boolean collecting = false;
      for (final VariableDeclarationFragment brother : expose.fragments((VariableDeclarationStatement) f.getParent())) {
        if (brother == f) {
          collecting = true;
          continue;
        }
        if (collecting)
          $.add(brother);
      }
      return $;
    }
    static int removalSaving(final VariableDeclarationFragment f) {
      final VariableDeclarationStatement parent = (VariableDeclarationStatement) f.getParent();
      final int $ = size(parent);
      if (parent.fragments().size() <= 1)
        return $;
      final VariableDeclarationStatement newParent = duplicate(parent);
      newParent.fragments().remove(parent.fragments().indexOf(f));
      return $ - size(newParent);
    }
    static int eliminationSaving(final VariableDeclarationFragment f) {
      final VariableDeclarationStatement parent = (VariableDeclarationStatement) f.getParent();
      final List<VariableDeclarationFragment> live = live(f, expose.fragments(parent));
      final int $ = size(parent);
      if (live.isEmpty())
        return $;
      final VariableDeclarationStatement newParent = duplicate(parent);
      final List<VariableDeclarationFragment> fs = expose.fragments(newParent);
      fs.clear();
      fs.addAll(live);
      return $ - size(newParent);
    }
    /**
     * Removes a {@link VariableDeclarationFragment}, leaving intact any other
     * fragment fragments in the containing {@link VariabelDeclarationStatement}
     * . Still, if the containing node left empty, it is removed as well.
     *
     * @param f
     *          JD
     * @param r
     *          JD
     * @param g
     *          JD
     */
    static void remove(final VariableDeclarationFragment f, final ASTRewrite r, final TextEditGroup g) {
      final VariableDeclarationStatement parent = (VariableDeclarationStatement) f.getParent();
      r.remove(parent.fragments().size() > 1 ? f : parent, g);
    }
    /**
     * Eliminates a {@link VariableDeclarationFragment}, with any other fragment
     * fragments which are not live in the containing
     * {@link VariabelDeclarationStatement}. If no fragments are left, then this
     * containing node is eliminated as well.
     *
     * @param f
     *          JD
     * @param r
     *          JD
     * @param g
     *          JD
     */
    static void eliminate(final VariableDeclarationFragment f, final ASTRewrite r, final TextEditGroup g) {
      final VariableDeclarationStatement parent = (VariableDeclarationStatement) f.getParent();
      final List<VariableDeclarationFragment> live = live(f, expose.fragments(parent));
      if (live.isEmpty()) {
        r.remove(parent, g);
        return;
      }
      final VariableDeclarationStatement newParent = duplicate(parent);
      final List<VariableDeclarationFragment> fs = expose.fragments(newParent);
      fs.clear();
      fs.addAll(live);
      r.replace(parent, newParent, g);
    }
    private static List<VariableDeclarationFragment> live(final VariableDeclarationFragment f, final List<VariableDeclarationFragment> fs) {
      final List<VariableDeclarationFragment> $ = new ArrayList<>();
      for (final VariableDeclarationFragment brother : fs)
        if (brother != null && brother != f && brother.getInitializer() != null)
          $.add(duplicate(brother));
      return $;
    }
    abstract ASTRewrite go(ASTRewrite r, VariableDeclarationFragment f, SimpleName n, Expression initializer, Statement nextStatement, TextEditGroup g);
    @Override final ASTRewrite go(final ASTRewrite r, final VariableDeclarationFragment f, final Statement nextStatement, final TextEditGroup g) {
      if (!Is.variableDeclarationStatement(f.getParent()))
        return null;
      final SimpleName n = f.getName();
      return n == null ? null : go(r, f, n, f.getInitializer(), nextStatement, g);
    }
    @Override Suggestion make(final VariableDeclarationFragment f, final ExclusionManager exclude) {
      final Suggestion $ = super.make(f, exclude);
      if ($ != null && exclude != null)
        exclude.exclude(f.getParent());
      return $;
    }
  }
}

final class LocalInliner {
  static Wrapper<ASTNode>[] wrap(final ASTNode @NonNull [] ns) {
    @SuppressWarnings("unchecked") final Wrapper<ASTNode>[] $ = new Wrapper[ns.length];
    int i = 0;
    for (final ASTNode t : ns)
      $[i++] = new Wrapper<>(t);
    return $;
  }

  final SimpleName name;
  final ASTRewrite rewriter;
  final TextEditGroup editGroup;

  LocalInliner(final SimpleName n) {
    this(n, null, null);
  }
  LocalInliner(final SimpleName n, final ASTRewrite rewriter, final TextEditGroup g) {
    name = n;
    this.rewriter = rewriter;
    editGroup = g;
  }
  LocalInlineWithValue byValue(final Expression replacement) {
    return new LocalInlineWithValue(replacement);
  }

  class LocalInlineWithValue extends Wrapper<Expression> {
    LocalInlineWithValue(final Expression replacement) {
      super(extract.core(replacement));
    }
    @SafeVarargs protected final void inlineInto(final ASTNode... ns) {
      inlineInto(wrap(ns));
    }
    @SafeVarargs private final void inlineInto(final Wrapper<ASTNode>... ns) {
      for (final Wrapper<ASTNode> e : ns)
        inlineIntoSingleton(get(), e);
    }
    /**
     * Computes the total number of AST nodes in the replaced parameters
     *
     * @param es
     *          JD
     * @return A non-negative integer, computed from original size of the
     *         parameters, the number of occurrences of {@link #name} in the
     *         operands, and the size of the replacement.
     */
    int replacedSize(final ASTNode... ns) {
      return size(ns) + uses(ns).size() * (size(get()) - 1);
    }
    /**
     * Computes the number of AST nodes added as a result of the replacement
     * operation.
     *
     * @param es
     *          JD
     * @return A non-negative integer, computed from the number of occurrences
     *         of {@link #name} in the operands, and the size of the
     *         replacement.
     */
    int addedSize(final ASTNode... ns) {
      return uses(ns).size() * (size(get()) - 1);
    }
    boolean canInlineInto(final ASTNode... ns) {
      return Collect.definitionsOf(name).in(ns).isEmpty() && (Is.sideEffectFree(get()) || uses(ns).size() <= 1);
    }
    private List<SimpleName> uses(final ASTNode... ns) {
      return Collect.usesOf(name).in(ns);
    }
    private void inlineIntoSingleton(final ASTNode replacement, final Wrapper<ASTNode> ns) {
      final ASTNode oldExpression = ns.get();
      final ASTNode newExpression = duplicate(ns.get());
      ns.set(newExpression);
      rewriter.replace(oldExpression, newExpression, editGroup);
      for (final ASTNode use : Collect.forAllOccurencesExcludingDefinitions(name).in(newExpression))
        rewriter.replace(use, !(use instanceof Expression) ? replacement : new Plant((Expression) replacement).into(use.getParent()), editGroup);
    }
  }
}