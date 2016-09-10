package il.org.spartan.spartanizer.wring;

import static il.org.spartan.spartanizer.ast.step.*;
import static il.org.spartan.spartanizer.wring.Wrings.*;
import static org.eclipse.jdt.core.dom.Assignment.Operator.*;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.Assignment.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.assemble.*;
import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.engine.*;

/** A wring is a transformation that works on an AstNode. Such a transformation
 * make a single simplification of the tree. A wring is so small that it is
 * idempotent: Applying a wring to the output of itself is the empty operation.
 * @param <N> type of node which triggers the transformation.
 * @author Yossi Gil
 * @author Daniel Mittelman <code><mittelmania [at] gmail.com></code>
 * @since 2015-07-09 */
public abstract class Wring<N extends ASTNode> implements Kind {
  abstract String description(N n);

  /** Determine whether the parameter is "eligible" for application of this
   * instance. The parameter must be within the scope of the current instance.
   * @param n JD
   * @return <code><b>true</b></code> <i>iff</i> the argument is eligible for
   *         the simplification offered by this object. */
  boolean eligible(@SuppressWarnings("unused") final N ____) {
    return true;
  }

  Rewrite make(final N n) {
    return make(n, null);
  }

  Rewrite make(final N n, final ExclusionManager m) {
    return m != null && m.isExcluded(n) ? null : make(n);
  }

  /** Determines whether this {@link Wring} object is not applicable for a given
   * {@link PrefixExpression} is within the "scope" of this . Note that a
   * {@link Wring} is applicable in principle to an object, but that actual
   * application will be vacuous.
   * @param e JD
   * @return <code><b>true</b></code> <i>iff</i> the argument is noneligible for
   *         the simplification offered by this object.
   * @see #eligible(InfixExpression) */
  final boolean nonEligible(final N n) {
    return !eligible(n);
  }

  /** Determines whether this {@link Wring} object is applicable for a given
   * {@link InfixExpression} is within the "scope" of this . Note that it could
   * be the case that a {@link Wring} is applicable in principle to an object,
   * but that actual application will be vacuous.
   * @param n JD
   * @return <code><b>true</b></code> <i>iff</i> the argument is within the
   *         scope of this object @ */
  boolean scopeIncludes(final N n) {
    return make(n, null) != null;
  }

  static abstract class AbstractSorting extends ReplaceCurrentNode<InfixExpression> {
    @Override final String description(final InfixExpression x) {
      return "Reorder operands of " + x.getOperator();
    }

    abstract boolean sort(List<Expression> operands);
  }

  static abstract class InfixSorting extends AbstractSorting {
    @Override boolean eligible(final InfixExpression x) {
      final List<Expression> es = extract.allOperands(x);
      return !Wrings.mixedLiteralKind(es) && sort(es);
    }

    @Override Expression replacement(final InfixExpression x) {
      final List<Expression> operands = extract.allOperands(x);
      return !sort(operands) ? null : subject.operands(operands).to(x.getOperator());
    }
  }

  static abstract class InfixSortingOfCDR extends AbstractSorting {
    @Override boolean eligible(final InfixExpression x) {
      final List<Expression> es = extract.allOperands(x);
      es.remove(0);
      return !Wrings.mixedLiteralKind(es) && sort(es);
    }

    @Override Expression replacement(final InfixExpression x) {
      final List<Expression> operands = extract.allOperands(x);
      final Expression first = operands.remove(0);
      if (!sort(operands))
        return null;
      operands.add(0, first);
      return subject.operands(operands).to(x.getOperator());
    }
  }

  /** MultipleReplaceCurrentNode replaces multiple nodes in current statement
   * with multiple nodes (or a single node).
   * @author Ori Roth <code><ori.rothh [at] gmail.com></code>
   * @since 2016-04-25 */
  static abstract class MultipleReplaceCurrentNode<N extends ASTNode> extends Wring<N> {
    abstract ASTRewrite go(ASTRewrite r, N n, TextEditGroup g, List<ASTNode> bss, List<ASTNode> crs);

    @Override Rewrite make(final N n) {
      return new Rewrite(description(n), n) {
        @Override public void go(final ASTRewrite r, final TextEditGroup g) {
          final List<ASTNode> input = new ArrayList<>();
          final List<ASTNode> output = new ArrayList<>();
          MultipleReplaceCurrentNode.this.go(r, n, g, input, output);
          if (output.size() == 1)
            for (final ASTNode ¢ : input)
              r.replace(¢, output.get(0), g);
          else if (input.size() == output.size())
            for (int i = 0; i < input.size(); ++i)
              r.replace(input.get(i), output.get(i), g);
        }
      };
    }

    @Override boolean scopeIncludes(final N n) {
      return go(ASTRewrite.create(n.getAST()), n, null, new ArrayList<>(), new ArrayList<>()) != null;
    }
  }

  static abstract class ReplaceCurrentNode<N extends ASTNode> extends Wring<N> {
    @Override final Rewrite make(final N n) {
      return !eligible(n) ? null : new Rewrite(description(n), n) {
        @Override public void go(final ASTRewrite r, final TextEditGroup g) {
          r.replace(n, replacement(n), g);
        }
      };
    }

    abstract ASTNode replacement(N n);

    @Override boolean scopeIncludes(final N n) {
      return replacement(n) != null;
    }
  }

  /** Similar to {@link ReplaceCurrentNode}, but with an
   * {@link ExclusionManager} */
  static abstract class ReplaceCurrentNodeExclude<N extends ASTNode> extends Wring<N> {
    @Override final Rewrite make(final N n, final ExclusionManager m) {
      return !eligible(n) ? null : new Rewrite(description(n), n) {
        @Override public void go(final ASTRewrite r, final TextEditGroup g) {
          r.replace(n, replacement(n, m), g);
        }
      };
    }

    abstract ASTNode replacement(N n, final ExclusionManager m);

    @Override boolean scopeIncludes(final N n) {
      return replacement(n, new ExclusionManager()) != null;
    }
  }

  static abstract class ReplaceToNextStatement<N extends ASTNode> extends Wring<N> {
    abstract ASTRewrite go(ASTRewrite r, N n, Statement nextStatement, TextEditGroup g);

    @Override Rewrite make(final N n, final ExclusionManager exclude) {
      final Statement nextStatement = extract.nextStatement(n);
      if (nextStatement == null || !eligible(n))
        return null;
      exclude.exclude(nextStatement);
      return new Rewrite(description(n), n, nextStatement) {
        @Override public void go(final ASTRewrite r, final TextEditGroup g) {
          ReplaceToNextStatement.this.go(r, n, nextStatement, g);
        }
      };
    }

    @Override boolean scopeIncludes(final N n) {
      final Statement nextStatement = extract.nextStatement(n);
      return nextStatement != null && go(ASTRewrite.create(n.getAST()), n, nextStatement, null) != null;
    }
  }

  static abstract class VariableDeclarationFragementAndStatement extends ReplaceToNextStatement<VariableDeclarationFragment> {
    static InfixExpression.Operator asInfix(final Assignment.Operator o) {
      return o == PLUS_ASSIGN ? InfixExpression.Operator.PLUS
          : o == MINUS_ASSIGN ? MINUS
              : o == TIMES_ASSIGN ? TIMES
                  : o == DIVIDE_ASSIGN ? DIVIDE
                      : o == BIT_AND_ASSIGN ? AND
                          : o == BIT_OR_ASSIGN ? OR
                              : o == BIT_XOR_ASSIGN ? XOR
                                  : o == REMAINDER_ASSIGN ? REMAINDER
                                      : o == LEFT_SHIFT_ASSIGN ? LEFT_SHIFT //
                                          : o == RIGHT_SHIFT_SIGNED_ASSIGN ? RIGHT_SHIFT_SIGNED //
                                              : o == RIGHT_SHIFT_UNSIGNED_ASSIGN ? RIGHT_SHIFT_UNSIGNED : null;
    }

    static Expression assignmentAsExpression(final Assignment a) {
      final Operator o = a.getOperator();
      return o == ASSIGN ? duplicate.of(step.right(a)) : subject.pair(step.left(a), step.right(a)).to(asInfix(o));
    }

    static boolean doesUseForbiddenSiblings(final VariableDeclarationFragment f, final ASTNode... ns) {
      for (final VariableDeclarationFragment b : forbiddenSiblings(f))
        if (Collect.BOTH_SEMANTIC.of(b).existIn(ns))
          return true;
      return false;
    }

    /** Eliminates a {@link VariableDeclarationFragment}, with any other
     * fragment fragments which are not live in the containing
     * {@link VariabelDeclarationStatement}. If no fragments are left, then this
     * containing node is eliminated as well.
     * @param f
     * @param r
     * @param g */
    static void eliminate(final VariableDeclarationFragment f, final ASTRewrite r, final TextEditGroup g) {
      final VariableDeclarationStatement parent = (VariableDeclarationStatement) f.getParent();
      final List<VariableDeclarationFragment> live = live(f, fragments(parent));
      if (live.isEmpty()) {
        r.remove(parent, g);
        return;
      }
      final VariableDeclarationStatement newParent = duplicate.of(parent);
      fragments(newParent).clear();
      fragments(newParent).addAll(live);
      r.replace(parent, newParent, g);
    }

    static int eliminationSaving(final VariableDeclarationFragment f) {
      final VariableDeclarationStatement parent = (VariableDeclarationStatement) f.getParent();
      final List<VariableDeclarationFragment> live = live(f, fragments(parent));
      final int $ = size(parent);
      if (live.isEmpty())
        return $;
      final VariableDeclarationStatement newParent = duplicate.of(parent);
      fragments(newParent).clear();
      fragments(newParent).addAll(live);
      return $ - size(newParent);
    }

    static List<VariableDeclarationFragment> forbiddenSiblings(final VariableDeclarationFragment f) {
      final List<VariableDeclarationFragment> $ = new ArrayList<>();
      boolean collecting = false;
      final VariableDeclarationStatement parent = (VariableDeclarationStatement) f.getParent();
      for (final VariableDeclarationFragment brother : fragments(parent)) {
        if (brother == f) {
          collecting = true;
          continue;
        }
        if (collecting)
          $.add(brother);
      }
      return $;
    }

    static boolean hasAnnotation(final List<IExtendedModifier> ms) {
      for (final IExtendedModifier m : ms)
        if (m.isAnnotation())
          return true;
      return false;
    }

    static boolean hasAnnotation(final VariableDeclarationFragment f) {
      return hasAnnotation((VariableDeclarationStatement) f.getParent());
    }

    static boolean hasAnnotation(final VariableDeclarationStatement s) {
      return hasAnnotation(step.modifiers(s));
    }

    static int removalSaving(final VariableDeclarationFragment f) {
      final VariableDeclarationStatement parent = (VariableDeclarationStatement) f.getParent();
      final int $ = size(parent);
      if (parent.fragments().size() <= 1)
        return $;
      final VariableDeclarationStatement newParent = duplicate.of(parent);
      newParent.fragments().remove(parent.fragments().indexOf(f));
      return $ - size(newParent);
    }

    /** Removes a {@link VariableDeclarationFragment}, leaving intact any other
     * fragment fragments in the containing {@link VariabelDeclarationStatement}
     * . Still, if the containing node left empty, it is removed as well.
     * @param f
     * @param r
     * @param g */
    static void remove(final VariableDeclarationFragment f, final ASTRewrite r, final TextEditGroup g) {
      final VariableDeclarationStatement parent = (VariableDeclarationStatement) f.getParent();
      r.remove(parent.fragments().size() > 1 ? f : parent, g);
    }

    private static List<VariableDeclarationFragment> live(final VariableDeclarationFragment f, final List<VariableDeclarationFragment> fs) {
      final List<VariableDeclarationFragment> $ = new ArrayList<>();
      for (final VariableDeclarationFragment brother : fs)
        if (brother != null && brother != f && brother.getInitializer() != null)
          $.add(duplicate.of(brother));
      return $;
    }

    abstract ASTRewrite go(ASTRewrite r, VariableDeclarationFragment f, SimpleName n, Expression initializer, Statement nextStatement,
        TextEditGroup g);

    @Override final ASTRewrite go(final ASTRewrite r, final VariableDeclarationFragment f, final Statement nextStatement, final TextEditGroup g) {
      if (!iz.variableDeclarationStatement(f.getParent()))
        return null;
      final SimpleName n = f.getName();
      return n == null ? null : go(r, f, n, f.getInitializer(), nextStatement, g);
    }

    @Override Rewrite make(final VariableDeclarationFragment f, final ExclusionManager exclude) {
      final Rewrite $ = super.make(f, exclude);
      if ($ != null && exclude != null)
        exclude.exclude(f.getParent());
      return $;
    }
  }
}
