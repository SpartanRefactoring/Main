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

/** A {@link Tipper} which represents a NanoPattern.
 * @author Ori Marcovitch
 * @year 2016 */
public abstract class NanoPatternTipper<N extends ASTNode> extends Tipper<N> implements TipperCategory.Nanos {
  public final N nodeTypeHolder = null;

  protected static <N extends ASTNode> boolean anyTips(final Collection<UserDefinedTipper<N>> ts, final N n) {
    return n != null && ts.stream().anyMatch(t -> t.canTip(n));
  }

  protected static <N extends ASTNode> boolean nonTips(final Collection<NanoPatternTipper<N>> ts, final N n) {
    return n != null && ts.stream().allMatch(t -> t.cantTip(n));
  }

  protected static <N extends ASTNode> UserDefinedTipper<N> firstTipper(final Collection<UserDefinedTipper<N>> ts, final N n) {
    return ts.stream().filter(t -> t.canTip(n)).findFirst().get();
  }

  public static <N extends ASTNode> Tip firstTip(final Collection<UserDefinedTipper<N>> ts, final N n) {
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

  @Override public final Tip tip(final N ¢) {
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

  @SuppressWarnings("static-method") public String example() {
    return null;
  }

  @SuppressWarnings("static-method") public String symbolycReplacement() {
    return "";
  }

  public String[] akas() {
    return new String[] { className() };
  }

  String className() {
    return getClass().getSimpleName();
  }

  protected abstract Tip pattern(N ¢);

  @SuppressWarnings("static-method") public Category category() {
    return null;
  }

  public enum Category {
    Collection, Field, Conditional, Exception, Safety, MethodBody {
      @Override public String toString() {
        return "Method Body";
      }
    },
    Quantifier, Functional, Default;
    public static String pretty(final String name) {
      if (name.startsWith("Lisp"))
        return name.replaceAll("^Lisp", "");
      switch (name) {
        case "IfStatement":
          return "Conditional Statement";
        case "ConditionalExpression":
          return Conditional + "";
        case "EnhancedForStatement":
          return Collection + "";
        case "MethodDeclaration":
          return MethodBody + "";
        default:
          return name;
      }
    }
  }
}
