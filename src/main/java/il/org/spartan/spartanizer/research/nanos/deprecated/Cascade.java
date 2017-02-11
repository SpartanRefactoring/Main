package il.org.spartan.spartanizer.research.nanos.deprecated;

import static il.org.spartan.spartanizer.research.TipperFactory.*;

import java.util.*;
import java.util.stream.IntStream;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.nanos.common.*;

/** TODO: orimarco <tt>marcovitch.ori@gmail.com</tt> please add a description
 * @author orimarco <tt>marcovitch.ori@gmail.com</tt>
 * @since 2017-01-01 */
public final class Cascade extends NotImplementedNanoPattern<Block> {
  @Override public boolean canTip(final Block x) {
    if (iz.emptyBlock(x))
      return false;
    return IntStream.range(0, statements(x).size() - 1).anyMatch(¢ -> initializeThenUse(x, ¢));
  }

  @Override public Tip pattern(@SuppressWarnings("unused") final Block __) {
    return null;
    // return new Tip("", x, getClass()) {
    // @Override public void go(ASTRewrite r, TextEditGroup g) {
    // final Matcher m = Matcher.creation(x);
    // int j;
    // for (j = m.creationIdx + 1; j < statements(x).size() - 1; ++j) {
    // if (!m.usage(j + 1))
    // break;
    // }
    // final List<Statement> ss = statements(x);
    // throw new RuntimeErrorException();
    //
    // };
  }

  private static boolean initializeThenUse(final Block x, final int idx) {
    return Matcher.matcher(x).createsThenUses(idx);
  }

  static final class Matcher {
    private final List<Statement> ss;
    public String name;
    static final UserDefinedTipper<Expression> creation = patternTipper("$T1 $N = new $T2()", "$T $N = with(new $T2())", "");
    public int creationIdx;

    public static Matcher matcher(final Block ¢) {
      return new Matcher(¢);
    }

    private Matcher(final Block b) {
      ss = statements(b);
    }

    private Matcher(final List<Statement> ss) {
      this.ss = ss;
    }

    public boolean creates(final int idx) {
      final Statement $ = ss.get(idx);
      return iz.expressionStatement($) && iz.assignment(expression($)) && creation.canTip(expression($));
    }

    public boolean createsThenUses(final int idx) {
      return creates(idx) && new Matcher(ss).creation(idx).usage(idx + 1);
    }

    public Matcher creation(final int idx) {
      name = creation.getMatching(ss.get(idx), "$N") + "";
      creationIdx = idx;
      return this;
    }

    public Matcher findCreation() {
      for (int $ = 0; $ < ss.size() - 1; ++$)
        if (creates($))
          return creation($);
      return null;
    }

    public static Matcher creation(final Block ¢) {
      return new Matcher(¢).findCreation();
    }

    public boolean usage(final int idx) {
      return iz.expressionStatement(ss.get(idx)) && analyze.dependencies(ss.get(idx)).contains(name);
    }
  }
}
