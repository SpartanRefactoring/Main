package il.org.spartan.spartanizer.engine;

import static org.eclipse.jdt.core.dom.ASTNode.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import static il.org.spartan.spartanizer.ast.navigate.extract.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.java.*;
import il.org.spartan.spartanizer.utils.*;

/** Replace a variable with an expression
 * @year 2015
 * @author Yossi Gil
 * @since Sep 13, 2016 */
public final class Inliner {
  public static boolean doesUseForbiddenSiblings(final VariableDeclarationFragment f, final ASTNode... ns) {
    return Inliner.forbiddenSiblings(f).stream().anyMatch(λ -> collect.BOTH_SEMANTIC.of(λ).existIn(ns));
  }

  public static int eliminationSaving(final VariableDeclarationFragment f) {
    final VariableDeclarationStatement parent = (VariableDeclarationStatement) f.getParent();
    final List<VariableDeclarationFragment> live = live(f, fragments(parent));
    final int $ = metrics.size(parent);
    if (live.isEmpty())
      return $;
    final VariableDeclarationStatement newParent = copy.of(parent);
    fragments(newParent).clear();
    fragments(newParent).addAll(live);
    return $ - metrics.size(newParent);
  }
  public static boolean forbiddenOperationOnPrimitive(final VariableDeclarationFragment f, final Statement nextStatement) {
    if (!iz.literal(f.getInitializer()) || !iz.expressionStatement(nextStatement))
      return false;
    final ExpressionStatement x = (ExpressionStatement) nextStatement;
    if (iz.methodInvocation(x.getExpression())) {
      final Expression $ = core(expression(x.getExpression()));
      return iz.simpleName($) && ((SimpleName) $).getIdentifier().equals(f.getName().getIdentifier());
    }
    if (!iz.fieldAccess(x.getExpression()))
      return false;
    final Expression e = core(((FieldAccess) x.getExpression()).getExpression());
    return iz.simpleName(e) && ((SimpleName) e).getIdentifier().equals(f.getName().getIdentifier());
  }
  public static List<VariableDeclarationFragment> forbiddenSiblings(final VariableDeclarationFragment f) {
    final List<VariableDeclarationFragment> $ = new ArrayList<>();
    boolean collecting = false;
    for (final VariableDeclarationFragment brother : fragments((VariableDeclarationStatement) f.getParent())) {
      if (brother == f) {
        collecting = true;
        continue;
      }
      if (collecting)
        $.add(brother);
    }
    return $;
  }

  public static String getElementTypeNameFromArrayType(final Type t) {
    if (!(t instanceof ArrayType))
      return null;
    final Type et = ((ArrayType) t).getElementType();
    if (!(et instanceof SimpleType))
      return null;
    final Name $ = ((SimpleType) et).getName();
    return !($ instanceof SimpleName) ? null : ((SimpleName) $).getIdentifier();
  }

  public static boolean isArrayInitWithUnmatchingTypes(final VariableDeclarationFragment f) {
    if (!(f.getParent() instanceof VariableDeclarationStatement))
      return false;
    final String $ = getElementTypeNameFromArrayType(az.variableDeclarationStatement(f.getParent()).getType());
    if (!(f.getInitializer() instanceof ArrayCreation))
      return false;
    final String initializerElementTypeName = getElementTypeNameFromArrayType(((ArrayCreation) f.getInitializer()).getType());
    return $ != null && initializerElementTypeName != null && !$.equals(initializerElementTypeName);
  }

  public static boolean isPresentOnAnonymous(final SimpleName n, final Statement s) {
    return az.stream(yieldAncestors.until(s).ancestors(n)).anyMatch(λ -> iz.nodeTypeEquals(λ, ANONYMOUS_CLASS_DECLARATION));
  }

  public static List<VariableDeclarationFragment> live(final VariableDeclarationFragment f, final List<VariableDeclarationFragment> fs) {
    final List<VariableDeclarationFragment> $ = new ArrayList<>();
    fs.stream().filter(λ -> λ != null && λ != f && λ.getInitializer() != null).forEach(λ -> $.add(copy.of(λ)));
    return $;
  }

  public static boolean never(final SimpleName n, final Statement s) {
    return az.stream(yieldAncestors.until(s).ancestors(n)).anyMatch(λ -> iz.nodeTypeIn(λ, TRY_STATEMENT, SYNCHRONIZED_STATEMENT, LAMBDA_EXPRESSION));
  }

  public static Expression protect(final Expression initializer, final VariableDeclarationStatement currentStatement) {
    if (!iz.arrayInitializer(initializer))
      return initializer;
    final ArrayCreation $ = initializer.getAST().newArrayCreation();
    $.setType(az.arrayType(copy.of(type(currentStatement))));
    $.setInitializer(copy.of(az.arrayInitializer(initializer)));
    return $;
  }

  public static boolean variableNotUsedAfterStatement(final Statement s, final SimpleName n) {
    final Block b = az.block(s.getParent());
    assert b != null : "For loop's parent is not a block";
    final List<Statement> statements = step.statements(b);
    boolean passedFor = false;
    for (final Statement ¢ : statements) {
      if (passedFor && !collect.usesOf(n).in(¢).isEmpty())
        return false;
      if (¢.equals(s))
        passedFor = true;
    }
    return true;
  }

  /** Determines whether a specific SimpleName was used in a
   * {@link ForStatement}.
   * @param s JD
   * @param n JD
   * @return <code><b>true</b></code> <em>iff</em> the SimpleName is used in a
   *         ForStatement's condition, updaters, or body. */
  public static boolean variableUsedInFor(final ForStatement s, final SimpleName n) {
    return !collect.usesOf(n).in(step.condition(s), step.body(s)).isEmpty() || !collect.usesOf(n).in(step.updaters(s)).isEmpty();
  }

  static Wrapper<ASTNode>[] wrap(final ASTNode[] ns) {
    @SuppressWarnings("unchecked") final Wrapper<ASTNode>[] $ = new Wrapper[ns.length];
    final Int i = new Int();
    Arrays.asList(ns).forEach(λ -> $[i.inner++] = new Wrapper<>(λ));
    return $;
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

  public final class InlinerWithValue extends Wrapper<Expression> {
    InlinerWithValue(final Expression replacement) {
      super(core(replacement));
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

    @SuppressWarnings("unchecked") private void inlineinto(final Wrapper<ASTNode>... ns) {
      Arrays.asList(ns).forEach(λ -> inlineintoSingleton(get(), λ));
    }

    @SafeVarargs public final void inlineInto(final ASTNode... ¢) {
      inlineinto(wrap(¢));
    }

    private void inlineintoSingleton(final ASTNode replacement, final Wrapper<ASTNode> n) {
      final ASTNode oldExpression = n.get(), newExpression = copy.of(n.get());
      n.set(newExpression);
      rewriter.replace(oldExpression, newExpression, editGroup);
      collect.usesOf(name).in(newExpression)
          .forEach(λ -> rewriter.replace(λ, !iz.expression(λ) ? replacement : make.plant((Expression) replacement).into(λ.getParent()), editGroup));
    }

    /** Computes the total number of AST nodes in the replaced parameters
     * @param es JD
     * @return A non-negative integer, computed from original size of the
     *         parameters, the number of occurrences of {@link #name} in the
     *         operands, and the size of the replacement. */
    public int replacedSize(final ASTNode... ¢) {
      return metrics.size(¢) + uses(¢).size() * (metrics.size(get()) - 1);
    }

    private List<SimpleName> unsafeUses(final ASTNode... ¢) {
      return collect.unsafeUsesOf(name).in(¢);
    }

    private List<SimpleName> uses(final ASTNode... ¢) {
      return collect.usesOf(name).in(¢);
    }
  }
}
