package il.org.spartan.spartanizer.research.nanos.common;

import static il.org.spartan.lisp.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;
import org.jetbrains.annotations.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.tipping.*;

/** A {@link Tipper} which represents a NanoPattern.
 * @author Ori Marcovitch
 * @since Jan 8, 2017 */
public abstract class NanoPatternTipper<N extends ASTNode> extends Tipper<N>//
    implements TipperCategory.Nanos {
  private static final long serialVersionUID = 8068722088108674947L;
  @Nullable public final N nodeTypeHolder = null;

  protected static <N extends ASTNode> boolean anyTips(@NotNull final Collection<UserDefinedTipper<N>> ts, @Nullable final N n) {
    return n != null && ts.stream().anyMatch(λ -> λ.check(n));
  }

  protected static <N extends ASTNode> boolean nonTips(@NotNull final Collection<NanoPatternTipper<N>> ts, @Nullable final N n) {
    return n == null || ts.stream().allMatch(λ -> λ.cantTip(n));
  }

  protected static <N extends ASTNode> UserDefinedTipper<N> firstTipper(@NotNull final Collection<UserDefinedTipper<N>> ts, @NotNull final N n) {
    return ts.stream().filter(λ -> λ.check(n)).findFirst().get();
  }

  @Nullable public static <N extends ASTNode> Fragment firstTip(@NotNull final Collection<UserDefinedTipper<N>> ts, @NotNull final N n) {
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

  @Nullable protected static Block containingBlock(final ASTNode ¢) {
    return yieldAncestors.untilContainingBlock().from(¢);
  }

  @NotNull @Override public final Fragment tip(@NotNull final N ¢) {
    @Nullable final Fragment $ = pattern(¢);
    @NotNull @SuppressWarnings("unchecked") final Class<? extends NanoPatternTipper<N>> c = (Class<? extends NanoPatternTipper<N>>) getClass();
    return new Fragment($.description, ¢, c) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        Logger.logNP(¢, nanoName());
        $.go(r, g);
      }
    };
  }

  @Nullable @Override public String description(@SuppressWarnings("unused") final N __) {
    return "";
  }

  @Nullable @Override public String technicalName() {
    return nanoName();
  }

  @Nullable @SuppressWarnings("static-method") public String example() {
    return null;
  }

  @SuppressWarnings("static-method") public String symbolycReplacement() {
    return "";
  }

  @NotNull @Override public String[] akas() {
    return new String[] { nanoName() };
  }

  @Nullable protected abstract Fragment pattern(N ¢);

  @Nullable @SuppressWarnings("static-method") public Category category() {
    return null;
  }

  public enum Category {
    Iterative, Field, Conditional, Exception, Safety, MethodBody {
      @NotNull @Override public String toString() {
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
