package il.org.spartan.spartanizer.engine;

import static org.eclipse.jdt.core.dom.ASTNode.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import java.util.*;
import java.util.stream.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.cmdline.*;
import il.org.spartan.spartanizer.java.*;
import il.org.spartan.utils.*;

/** Encapsulates the operation of replacing a variable with an expression in a
 * certain location.
 * @year 2015
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @since 2017-03-16 */
public final class Replacement {
  /** What to replace by {@link #replacement} */
  public final SimpleName what;
  /** What replaces of {@link #what} */
  public final Expression replacement;
  /** Where to replace {@link #what} by {@link #replacement} */
  public final ASTNode where;
  /** Occurrences of {@link #what} in {@link #where} */
  public final List<SimpleName> spots;

  /** Factory method: FAPI factory chain
   * @author Yossi Gil {@code yossi.gil@gmail.com}
   * @since 2017-03-16 */
  public static Of of(final SimpleName of) {
    return by -> location -> new Replacement(of, by, location);
  }

  private Replacement(final SimpleName what, final Expression replacement, final ASTNode where) {
    spots = collect.usesOf(this.what = what).in(this.where = where);
    this.replacement = replacement;
  }

  /** Computes the number of AST nodes added as a result of the replacement
   * operation.
   * @param es JD
   * @return A non-negative integer, computed from the number of occurrences of
   *         {@link #what} in the operands, and the size of the replacement. */
  public int addedSize() {
    return spots.size() * (metrics.size(replacement) - metrics.size(what));
  }

  public boolean ok() {
    if (unsafeUse())
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

  private boolean unsafeUse() {
    return spots.stream().anyMatch(this::potentialMultipleExecutions);
  }

  private boolean potentialMultipleExecutions(final SimpleName location) {
    for (ASTNode ancestor: ancestors.of(location)) {
      if (ancestor == where)
        return false;
    }
    return false;
  }

  private boolean multipleInlineOK() {
    return iz.deterministic(replacement);
  }

  private boolean singletonInlineOK() {
    return sideEffects.free(replacement);
  }

  private boolean nullaryInlineOK() {
    return iz.sink(replacement);
  }

  public ASTRewrite fire(final ASTRewrite $, final TextEditGroup g) {
    return null;
  }


  public Expression protect(final Expression initializer, final VariableDeclarationStatement currentStatement) {
    if (!iz.arrayInitializer(initializer))
      return initializer;
    final ArrayCreation $ = initializer.getAST().newArrayCreation();
    $.setType(az.arrayType(copy.of(type(currentStatement))));
    // TODO: Marco causes IllegalArgumentException
    $.setInitializer(copy.of(az.arrayInitializer(initializer)));
    return $;
  }


  /** FAPI factory chain
   * @author Yossi Gil <tt>yossi.gil@gmail.com</tt>
   * @since 2017-03-16 */
  public interface Of {
    By by(Expression by);
  }

  /** FAPI factory chain
   * @author Yossi Gil <tt>yossi.gil@gmail.com</tt>
   * @since 2017-03-16 */
  public interface By {
    Replacement in(ASTNode location);
  }
}
