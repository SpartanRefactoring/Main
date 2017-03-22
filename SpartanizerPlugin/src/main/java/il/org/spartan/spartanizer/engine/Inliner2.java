package il.org.spartan.spartanizer.engine;

import static org.eclipse.jdt.core.dom.ASTNode.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;
import org.jetbrains.annotations.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.java.*;
import il.org.spartan.utils.*;

/** Encapsulates the operation of replacing a variable with an expression in a
 * certain location.
 * @year 2015
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @since 2017-03-16 */
public final class Inliner2 {
  /** What to replace by {@link #replacement} */
  public final SimpleName what;
  /** What replaces of {@link #what} */
  public final Expression replacement;
  /** Where to replace {@link #what} by {@link #replacement} */
  public final Collection<? extends ASTNode> where;
  /** Occurrences of {@link #what} in {@link #where} */
  public final Collection<? extends SimpleName> spots;

  /** Factory method: FAPI factory chain
   * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
   * @since 2017-03-16 [[SuppressWarningsSpartan]] */
  public static Of of(final SimpleName of) {
    return by -> location -> new Inliner2(of, by, location);
  }

  public boolean ok() {
    if (spots.stream().anyMatch(Inliner2::isLeftValue))
      return false;
    switch (spots.size()) {
      case 0:
        return nullaryInlineOK();
      case 1:
        return singletonInlineOK();
      default:
        return multipleInlineOK();
    }
  }

  private static boolean isLeftValue(@NotNull final SimpleName ¢) {
    @NotNull final ASTNode $ = parent(¢);
    return iz.prefixExpression($) || iz.postfixExpression($) || ¢ == to(az.assignment(¢.getParent()));
  }

  @NotNull public ASTRewrite fire(@NotNull final ASTRewrite $, final TextEditGroup g) {
    for (final SimpleName ¢ : spots)
      $.replace(¢, copy.of(replacement), g);
    return $;
  }

  private Inliner2(final SimpleName what, @NotNull final Expression replacement, final List<? extends ASTNode> where) {
    this.replacement = protect(replacement);
    spots = collect.usesOf(this.what = what).in(this.where = where);
  }

  /** Computes the number of AST nodes added as a result of the replacement
   * operation.
   * @param es JD
   * @return A non-negative integer, computed from the number of occurrences of
   *         {@link #what} in the operands, and the size of the replacement. */
  public int addedSize() {
    return spots.size() * (metrics.size(replacement) - metrics.size(what));
  }

  /** [[SuppressWarningsSpartan]] */
  private boolean multipleInlineOK() {
    if (iz.deterministic(replacement))
      return true;
    if (PossiblyMultipleExecution.of(what).inContext(where))
      return false;
    return true;
  }

  private boolean singletonInlineOK() {
    return sideEffects.free(replacement);
  }

  private boolean nullaryInlineOK() {
    return sideEffects.sink(replacement);
  }

  public static int removalSaving(@NotNull final VariableDeclarationFragment f) {
    @NotNull final VariableDeclarationStatement parent = (VariableDeclarationStatement) f.getParent();
    final int $ = metrics.size(parent);
    if (parent.fragments().size() <= 1)
      return $;
    final VariableDeclarationStatement newParent = copy.of(parent);
    newParent.fragments().remove(parent.fragments().indexOf(f));
    return $ - metrics.size(newParent);
  }

  public static Expression protect(@NotNull final Expression initializer, final VariableDeclarationStatement currentStatement) {
    if (!iz.arrayInitializer(initializer))
      return initializer;
    final ArrayCreation $ = initializer.getAST().newArrayCreation();
    $.setType(az.arrayType(copy.of(type(currentStatement))));
    // TODO causes IllsegalArgumentException (--om)
    $.setInitializer(copy.of(az.arrayInitializer(initializer)));
    return $;
  }

  public static boolean leftSide(@NotNull final Statement nextStatement, final String id) {
    @NotNull final Bool $ = new Bool();
    // noinspection SameReturnValue
    nextStatement.accept(new ASTVisitor(true) {
      @Override public boolean visit(final Assignment ¢) {
        if (iz.simpleName(left(¢)) && identifier(az.simpleName(left(¢))).equals(id))
          $.inner = true;
        return true;
      }
    });
    return $.inner;
  }

  public static Expression protect(@NotNull final Expression ¢) {
    switch (¢.getNodeType()) {
      case ARRAY_CREATION:
      case CAST_EXPRESSION:
        return subject.operand(¢).parenthesis();
      default:
        return ¢;
    }
  }

  /** FAPI factory chain
   * @author Yossi Gil <tt>Yossi.Gil@GMail.COM</tt>
   * @since 2017-03-16 */
  public interface Of {
    @NotNull By by(Expression by);
  }

  /** FAPI factory chain
   * @author Yossi Gil <tt>Yossi.Gil@GMail.COM</tt>
   * @since 2017-03-16 */
  public interface By {
    @NotNull Inliner2 in(List<? extends ASTNode> ns);
  }
}
