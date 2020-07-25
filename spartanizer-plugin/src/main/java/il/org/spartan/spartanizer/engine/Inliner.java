package il.org.spartan.spartanizer.engine;

import static il.org.spartan.spartanizer.ast.navigate.step.body;
import static il.org.spartan.spartanizer.ast.navigate.step.condition;
import static il.org.spartan.spartanizer.ast.navigate.step.parent;
import static il.org.spartan.spartanizer.ast.navigate.step.statements;
import static il.org.spartan.spartanizer.ast.navigate.step.updaters;
import static org.eclipse.jdt.core.dom.ASTNode.ANONYMOUS_CLASS_DECLARATION;
import static org.eclipse.jdt.core.dom.ASTNode.LAMBDA_EXPRESSION;
import static org.eclipse.jdt.core.dom.ASTNode.SYNCHRONIZED_STATEMENT;
import static org.eclipse.jdt.core.dom.ASTNode.TRY_STATEMENT;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.IntFunction;
import java.util.stream.Stream;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ArrayCreation;
import org.eclipse.jdt.core.dom.ArrayType;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.text.edits.TextEditGroup;

import il.org.spartan.Wrapper;
import il.org.spartan.spartanizer.ast.factory.copy;
import il.org.spartan.spartanizer.ast.factory.make;
import il.org.spartan.spartanizer.ast.navigate.extract;
import il.org.spartan.spartanizer.ast.navigate.yieldAncestors;
import il.org.spartan.spartanizer.ast.nodes.metrics.Metrics;
import il.org.spartan.spartanizer.ast.safety.az;
import il.org.spartan.spartanizer.ast.safety.iz;
import il.org.spartan.spartanizer.java.sideEffects;

/** Replace a variable with an expression
 * @year 2015
 * @author Yossi Gil
 * @since Sep 13, 2016 */
public final class Inliner {
  static Wrapper<ASTNode>[] wrap(final ASTNode... ¢) {
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
  public InlinerWithValue byValue(final Expression replacement) {
    return new InlinerWithValue(replacement);
  }
  public static boolean isPresentOnAnonymous(final SimpleName n, final Statement s) {
    return az.stream(yieldAncestors.until(s).ancestors(n)).anyMatch(λ -> iz.nodeTypeEquals(λ, ANONYMOUS_CLASS_DECLARATION));
  }
  public static boolean never(final SimpleName n, final Statement s) {
    return az.stream(yieldAncestors.until(s).ancestors(n)).anyMatch(λ -> iz.nodeTypeIn(λ, TRY_STATEMENT, SYNCHRONIZED_STATEMENT, LAMBDA_EXPRESSION));
  }
  public static boolean isArrayInitWithUnmatchingTypes(final VariableDeclarationFragment f) {
    if (!(f.getParent() instanceof VariableDeclarationStatement))
      return false;
    final String $ = getElTypeNameFromArrayType(az.variableDeclarationStatement(f.getParent()).getType());
    if (!(f.getInitializer() instanceof ArrayCreation))
      return false;
    final String initializerElementTypeName = getElTypeNameFromArrayType(((ArrayCreation) f.getInitializer()).getType());
    return $ != null && initializerElementTypeName != null && !$.equals(initializerElementTypeName);
  }
  public static String getElTypeNameFromArrayType(final Type t) {
    if (!(t instanceof ArrayType))
      return null;
    final Type et = ((ArrayType) t).getElementType();
    if (!(et instanceof SimpleType))
      return null;
    final Name $ = ((SimpleType) et).getName();
    return !($ instanceof SimpleName) ? null : ((SimpleName) $).getIdentifier();
  }
  public static Expression protect(final Expression initializer) {
    if (!iz.arrayInitializer(initializer))
      return copy.of(initializer);
    final VariableDeclarationStatement parent = az.variableDeclarationStatement(parent(parent(initializer)));
    assert parent != null;
    final ArrayCreation $ = initializer.getAST().newArrayCreation();
    $.setType(az.arrayType(copy.of(extract.type(az.variableDeclrationFragment(parent(initializer))))));
    $.setInitializer(copy.of(az.arrayInitializer(initializer)));
    // TODO // causes // IllegalArgumentException // (--om)
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
  public static boolean variableNotUsedAfterStatement(final Statement s, final SimpleName n) {
    final Block b = az.block(s.getParent());
    assert b != null : "For loop's parent is not a block";
    final List<Statement> statements = statements(b);
    boolean passedFor = false;
    for (final Statement ¢ : statements) {
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
      return uses(¢).size() * (Metrics.size(get()) - 1);
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
      return Metrics.size(¢) + uses(¢).size() * (Metrics.size(get()) - 1);
    }
    @SuppressWarnings("unchecked") private void inlineinto(final Wrapper<ASTNode>... ¢) {
      Stream.of(¢).forEach(this::inlineIntoSingleton);
    }
    private void inlineIntoSingleton(final Wrapper<ASTNode> n) {
      assert n != null;
      final ASTNode oldExpression = n.get(), newExpression = copy.of(oldExpression);
      assert oldExpression != null;
      final Expression replacement = get();
      assert replacement != null;
      assert rewriter != null;
      rewriter.replace(oldExpression, newExpression, editGroup);
      collect.usesOf(name)//
          .in(newExpression).stream()//
          .filter(Objects::nonNull)//
          .forEach(λ -> rewriter.replace(λ, make.plant(replacement).into(λ.getParent()), editGroup));
      n.set(newExpression);
    }
    private Collection<SimpleName> unsafeUses(final ASTNode... ¢) {
      return collect.unsafeUsesOf(name).in(¢);
    }
    private Collection<SimpleName> uses(final ASTNode... ¢) {
      return collect.usesOf(name).in(¢);
    }
  }
}
