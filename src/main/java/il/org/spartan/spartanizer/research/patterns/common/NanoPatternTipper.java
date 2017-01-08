package il.org.spartan.spartanizer.research.patterns.common;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.tipping.*;

/** A {@link Tipper} in which represents a NanoPattern.
 * @author Ori Marcovitch
 * @year 2016 */
public abstract class NanoPatternTipper<N extends ASTNode> extends Tipper<N> implements TipperCategory.Nanos {
  protected static <N extends ASTNode> boolean anyTips(final Collection<UserDefinedTipper<N>> ns, final N n) {
    return n != null && ns.stream().anyMatch(t -> t.canTip(n));
  }

  protected static <N extends ASTNode> boolean nonTips(final Collection<NanoPatternTipper<N>> ns, final N n) {
    return n != null && ns.stream().allMatch(t -> t.cantTip(n));
  }

  protected static <N extends ASTNode> UserDefinedTipper<N> firstTipper(final Collection<UserDefinedTipper<N>> ns, final N n) {
    return ns.stream().filter(t -> t.canTip(n)).findFirst().get();
  }

  public static <N extends ASTNode> Tip firstTip(final Collection<UserDefinedTipper<N>> ns, final N n) {
    return firstTipper(ns, n).tip(n);
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

  public String[] technicalName() {
    return new String[] { className() };
  }

  public String[] akas() {
    return new String[] { className() };
  }

  String className() {
    return getClass().getSimpleName();
  }

  protected abstract Tip pattern(final N ¢);

  @SuppressWarnings("static-method") public Category category() {
    return null;
  }

  public enum Category {
    Iterative, Return, Throw, Ternary {
      @Override public String toString() {
        return "Conditional Expression";
      }
    },
    MethodBody {
      @Override public String toString() {
        return "Method Body";
      }
    },
    Quantifier, Functional;
    public static String pretty(final String name) {
      if (name.startsWith("Lisp"))
        return name.replaceAll("^Lisp", "");
      switch (name) {
        case "IfStatement":
          return "Conditional Statement";
        case "MethodDeclaration":
          return MethodBody + "";
        case "ConditionalExpression":
          return Ternary + "";
        case "EnhancedForStatement":
          return Iterative + "";
        default:
          return name;
      }
    }
  }
}
