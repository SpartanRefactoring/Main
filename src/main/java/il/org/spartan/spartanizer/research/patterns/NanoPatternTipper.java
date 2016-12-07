package il.org.spartan.spartanizer.research.patterns;

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
    return ns.stream().anyMatch(t -> t.canTip(n));
  }

  protected static <N extends ASTNode> UserDefinedTipper<N> firstThatTips(final Collection<UserDefinedTipper<N>> ns, final N n) {
    return ns.stream().filter(t -> t.canTip(n)).findFirst().get();
  }

  protected static <N extends ASTNode> Tip firstTip(final Collection<UserDefinedTipper<N>> ns, final N n) {
    return firstThatTips(ns, n).tip(n);
  }

  @Override public final Tip tip(final N ¢) {
    return new Tip(description(¢), ¢, this.getClass()) {
      @Override public void go(ASTRewrite r, TextEditGroup g) {
        Logger.logNP(¢, className());
        pattern(¢).go(r, g);
      }
    };
  }

  String className() {
    return this.getClass().getSimpleName();
  }

  protected abstract Tip pattern(final N ¢);
}
