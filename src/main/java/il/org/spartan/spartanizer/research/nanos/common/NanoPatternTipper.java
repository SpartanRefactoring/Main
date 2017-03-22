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

  protected static <N extends ASTNode> UserDefinedTipper<N> firstTipper(@NotNull final Collection<UserDefinedTipper<N>> ts, final N n) {
    return ts.stream().filter(λ -> λ.check(n)).findFirst().get();
  }

  @Nullable public static <N extends ASTNode> Tip firstTip(@NotNull final Collection<UserDefinedTipper<N>> ts, final N n) {
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

  @Override @NotNull public final Tip tip(@NotNull final N ¢) {
    @Nullable final Tip $ = pattern(¢);
    return new Tip($.description, ¢, getClass()) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        Logger.logNP(¢, nanoName());
        $.go(r, g);
      }
    };
  }

  @Override @Nullable public String description(@SuppressWarnings("unused") final N __) {
    return "";
  }

  @Override @Nullable public String technicalName() {
    return nanoName();
  }

  @SuppressWarnings("static-method") @Nullable public String example() {
    return null;
  }

  @SuppressWarnings("static-method") public String symbolycReplacement() {
    return "";
  }

  @Override @NotNull public String[] akas() {
    return new String[] { nanoName() };
  }

  @Nullable protected abstract Tip pattern(N ¢);

  @SuppressWarnings("static-method") @Nullable public Category category() {
    return null;
  }

  public enum Category {
    Iterative, Field, Conditional, Exception, Safety, MethodBody {
      @Override @NotNull public String toString() {
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
