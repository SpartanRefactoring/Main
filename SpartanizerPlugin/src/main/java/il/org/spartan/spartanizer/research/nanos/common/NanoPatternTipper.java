package il.org.spartan.spartanizer.research.nanos.common;

import static il.org.spartan.lisp.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.tipping.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** A {@link Tipper} which represents a NanoPattern.
 * @year 2016
 * @author Ori Marcovitch
 * @since Jan 8, 2017 */
public abstract class NanoPatternTipper<N extends ASTNode> extends Tipper<N>//
    implements TipperCategory.Nanos {
  @Nullable
  public final N nodeTypeHolder = null;

  protected static <N extends ASTNode> boolean anyTips(@NotNull final Collection<UserDefinedTipper<N>> ts, @Nullable final N n) {
    return n != null && ts.stream().anyMatch(λ -> λ.canTip(n));
  }

  protected static <N extends ASTNode> boolean nonTips(@NotNull final Collection<NanoPatternTipper<N>> ts, @Nullable final N n) {
    return n != null && ts.stream().allMatch(λ -> λ.cantTip(n));
  }

  protected static <N extends ASTNode> UserDefinedTipper<N> firstTipper(@NotNull final Collection<UserDefinedTipper<N>> ts, final N n) {
    return ts.stream().filter(λ -> λ.canTip(n)).findFirst().get();
  }

  @Nullable
  public static <N extends ASTNode> Tip firstTip(@NotNull final Collection<UserDefinedTipper<N>> ts, final N n) {
    return firstTipper(ts, n).tip(n);
  }

  public static <N extends ASTNode> String firstPattern(final List<UserDefinedTipper<N>> ¢) {
    return first(¢).pattern().replaceAll("\\$", "");
  }

  public static <N extends ASTNode> String firstReplacement(final List<UserDefinedTipper<N>> ¢) {
    return first(¢).replacement().replaceAll("\\$", "");
  }

  public static <N extends ASTNode> UserDefinedTipper<N> firstTipper(final List<UserDefinedTipper<N>> ¢) {
    return first(¢);
  }

  @NotNull
  @Override public final Tip tip(@NotNull final N ¢) {
    return new Tip(description(¢), ¢, getClass()) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        Logger.logNP(¢, className());
        pattern(¢).go(r, g);
      }
    };
  }

  @Override public String description(@SuppressWarnings("unused") final N __) {
    return "";
  }

  public String technicalName() {
    return className();
  }

  @Nullable
  @SuppressWarnings("static-method") public String example() {
    return null;
  }

  @SuppressWarnings("static-method") public String symbolycReplacement() {
    return "";
  }

  @NotNull
  public String[] akas() {
    return new String[] { className() };
  }

  @NotNull String className() {
    return getClass().getSimpleName();
  }

  protected abstract Tip pattern(N ¢);

  @Nullable
  @SuppressWarnings("static-method") public Category category() {
    return null;
  }

  public enum Category {
    Iterative, Field, Conditional, Exception, Safety, MethodBody {
      @NotNull
      @Override public String toString() {
        return "Method Body";
      }
    },
    Quantifier, Functional, Default;
    public static String pretty(@NotNull final String name) {
      if (name.startsWith("Lisp"))
        return name.replaceAll("^Lisp", "");
      switch (name) {
        case "IfStatement":
          return "Conditional Statement";
        case "ConditionalExpression":
          return Conditional + "";
        case "EnhancedForStatement":
          return Iterative + "";
        case "MethodDeclaration":
          return MethodBody + "";
        default:
          return name;
      }
    }
  }
}
