package il.org.spartan.spartanizer.engine;

import static org.eclipse.jdt.core.dom.ASTNode.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;
import org.jetbrains.annotations.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.java.*;

/** Replace a variable with an expression
 * @year 2015
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @since Sep 13, 2016 */
public final class Inliner {
  @NotNull static Wrapper<ASTNode>[] wrap(@NotNull final ASTNode... ¢) {
    return Stream.of(¢).map(Wrapper<ASTNode>::new).toArray((IntFunction<Wrapper<ASTNode>[]>) Wrapper[]::new);
  }

  final SimpleName name;
  final ASTRewrite rewriter;
  final TextEditGroup editGroup;

  public Inliner(final SimpleName n) {
    this(n, null, null);
  }

  public Inliner(final SimpleName name, final ASTRewrite rewriter, final TextEditGroup editGroup) {
    this.name = name;
    this.rewriter = rewriter;
    this.editGroup = editGroup;
  }

  @NotNull public InlinerWithValue byValue(final Expression replacement) {
    return new InlinerWithValue(replacement);
  }

  public static boolean isPresentOnAnonymous(final SimpleName n, final Statement s) {
    return az.stream(yieldAncestors.until(s).ancestors(n)).anyMatch(λ -> iz.nodeTypeEquals(λ, ANONYMOUS_CLASS_DECLARATION));
  }

  public static boolean never(final SimpleName n, final Statement s) {
    return az.stream(yieldAncestors.until(s).ancestors(n)).anyMatch(λ -> iz.nodeTypeIn(λ, TRY_STATEMENT, SYNCHRONIZED_STATEMENT, LAMBDA_EXPRESSION));
  }

  public static boolean isArrayInitWithUnmatchingTypes(@NotNull final VariableDeclarationFragment f) {
    if (!(f.getParent() instanceof VariableDeclarationStatement))
      return false;
    @Nullable final String $ = getElTypeNameFromArrayType(az.variableDeclarationStatement(f.getParent()).getType());
    if (!(f.getInitializer() instanceof ArrayCreation))
      return false;
    @Nullable final String initializerElementTypeName = getElTypeNameFromArrayType(((ArrayCreation) f.getInitializer()).getType());
    return $ != null && initializerElementTypeName != null && !$.equals(initializerElementTypeName);
  }

  @Nullable public static String getElTypeNameFromArrayType(final Type t) {
    if (!(t instanceof ArrayType))
      return null;
    final Type et = ((ArrayType) t).getElementType();
    if (!(et instanceof SimpleType))
      return null;
    final Name $ = ((SimpleType) et).getName();
    return !($ instanceof SimpleName) ? null : ((SimpleName) $).getIdentifier();
  }

  public static Expression protect(@NotNull final Expression initializer, final VariableDeclarationStatement currentStatement) {
    if (!iz.arrayInitializer(initializer))
      return initializer;
    final ArrayCreation $ = initializer.getAST().newArrayCreation();
    $.setType(az.arrayType(copy.of(type(currentStatement))));
    // TODO // causes // IllegalArgumentException // (--om)
    $.setInitializer(copy.of(az.arrayInitializer(initializer)));
    return $;
  }

  /** Determines whether a specific SimpleName was used in a
   * {@link ForStatement}.
   * @param s JD
   * @param n JD
   * @return whether the SimpleName is used in a ForStatement's condition,
   *         updaters, or body. */
  public static boolean variableUsedInFor(final ForStatement s, final SimpleName n) {
    return !collect.usesOf(n).in(condition(s), body(s)).isEmpty() || !collect.usesOf(n).in(updaters(s)).isEmpty();
  }

  public static boolean variableNotUsedAfterStatement(@NotNull final Statement s, final SimpleName n) {
    @Nullable final Block b = az.block(s.getParent());
    assert b != null : "For loop's parent is not a block";
    @NotNull final List<Statement> statements = statements(b);
    boolean passedFor = false;
    for (@NotNull final Statement ¢ : statements) {
      if (passedFor && !collect.usesOf(n).in(¢).isEmpty())
        return false;
      if (¢.equals(s))
        passedFor = true;
    }
    return true;
  }

  public final class InlinerWithValue extends Wrapper<Expression> {
    InlinerWithValue(final Expression replacement) {
      super(extract.core(replacement));
    }

    /** Computes the number of AST nodes added as a result of the replacement
     * operation.
     * @param es JD
     * @return A non-negative integer, computed from the number of occurrences
     *         of {@link #name} in the operands, and the size of the
     *         replacement. */
    public int addedSize(final ASTNode... ¢) {
      return uses(¢).size() * (metrics.size(get()) - 1);
    }

    public boolean canInlineinto(final ASTNode... ¢) {
      return collect.definitionsOf(name).in(¢).isEmpty() && (sideEffects.free(get()) || uses(¢).size() <= 1);
    }

    public boolean canSafelyInlineinto(final ASTNode... ¢) {
      return canInlineinto(¢) && unsafeUses(¢).isEmpty();
    }

    @SafeVarargs public final void inlineInto(final ASTNode... ¢) {
      inlineinto(wrap(¢));
    }

    /** Computes the total number of AST nodes in the replaced parameters
     * @param es JD
     * @return A non-negative integer, computed from original size of the
     *         parameters, the number of occurrences of {@link #name} in the
     *         operands, and the size of the replacement. */
    public int replacedSize(final ASTNode... ¢) {
      return metrics.size(¢) + uses(¢).size() * (metrics.size(get()) - 1);
    }

    @SuppressWarnings("unchecked") private void inlineinto(final Wrapper<ASTNode>... ¢) {
      Stream.of(¢).forEach(this::inlineIntoSingleton);
    }

    private void inlineIntoSingleton(@NotNull final Wrapper<ASTNode> n) {
      assert n != null;
      final ASTNode oldExpression = n.get(), newExpression = copy.of(oldExpression);
      assert oldExpression != null;
      final Expression replacement = get();
      assert rewriter !=null;
      assert replacement !=null;
      try {
      rewriter.replace(oldExpression, newExpression, editGroup);
      } catch (NullPointerException x) {
       System.out.println(x); 
      }
      collect.usesOf(name).in(newExpression).stream().filter(Objects::nonNull).forEach(λ -> rewriter.replace(λ, make.plant(replacement).into(λ.getParent()), editGroup));
      n.set(newExpression);
    }

    @Nullable private Collection<SimpleName> unsafeUses(final ASTNode... ¢) {
      return collect.unsafeUsesOf(name).in(¢);
    }

    @Nullable private Collection<SimpleName> uses(final ASTNode... ¢) {
      return collect.usesOf(name).in(¢);
    }
  }
}
