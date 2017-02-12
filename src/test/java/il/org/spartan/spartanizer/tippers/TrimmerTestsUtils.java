/** TODO: Yossi Gil <yossi.gil@gmail.com> please add a description
 * @author Yossi Gil <yossi.gil@gmail.com>
 * @since Sep 25, 2016 */
package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.azzert.*;
import static il.org.spartan.spartanizer.tippers.TESTUtils.*;
import static il.org.spartan.spartanizer.utils.Wrap.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jface.text.*;

import il.org.spartan.*;
import il.org.spartan.plugin.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.spartanizer.utils.*;

public enum TrimmerTestsUtils {
  ;
  static String apply(final Tipper<? extends ASTNode> t, final String from) {
    final CompilationUnit $ = (CompilationUnit) makeAST.COMPILATION_UNIT.from(from);
    assert $ != null;
    return rewrite(new TipperApplicator(t), $, new Document(from)).get();
  }

  public static String applyTrimmer(final Trimmer t, final String from) {
    final CompilationUnit u = (CompilationUnit) makeAST.COMPILATION_UNIT.from(from);
    assert u != null;
    final Document $ = rewrite(t, u, new Document(from));
    assert $ != null;
    return $.get();
  }

  /** [[SuppressWarningsSpartan]] */
  static void assertSimplifiesTo(final String from, final String expected, final Tipper<? extends ASTNode> n, final Wrap w) {
    final String wrap = w.on(from), unpeeled = apply(n, wrap);
    if (wrap.equals(unpeeled))
      azzert.fail("Nothing done on " + from);
    final String peeled = w.off(unpeeled);
    if (peeled.equals(from))
      azzert.that("No similification of " + from, peeled, is(not(from)));
    if (tide.clean(peeled).equals(tide.clean(from)))
      azzert.that("Simpification of " + from + " is just reformatting", tide.clean(from), is(not(tide.clean(peeled))));
    assertSimilar(expected, peeled);
  }

  public static int countOpportunities(final AbstractGUIApplicator a, final CompilationUnit u) {
    return a.collectSuggestions(u).size();
  }

  static <N extends ASTNode> OperandToTipper<N> included(final String from, final Class<N> clazz) {
    return new OperandToTipper<>(from, clazz);
  }

  public static Operand trimmingOf(final String from) {
    return new Operand(from);
  }

  public static class Operand extends Wrapper<String> {
    private final Trimmer trimmer;

    public Operand(final String inner) {
      super(inner);
      trimmer = new Trimmer();
    }

    void checkExpected(final String expected) {
      final Wrap w = Wrap.find(get());
      final String wrap = w.on(get()), unpeeled = TrimmerTestsUtils.applyTrimmer(new Trimmer(), wrap);
      if (wrap.equals(unpeeled))
        azzert.fail("Nothing done on " + get());
      final String peeled = w.off(unpeeled);
      if (peeled.equals(get()))
        azzert.that("No trimming of " + get(), peeled, is(not(get())));
      if (tide.clean(peeled).equals(tide.clean(get())))
        azzert.that("Trimming of " + get() + "is just reformatting", tide.clean(get()), is(not(tide.clean(peeled))));
      assertSimilar(expected, peeled);
    }

    public void doesNotCrash() {
      final Wrap w = Wrap.find(get());
      assertNotEquals("Trimming of " + get() + " crashed", essence(get()),
          essence(w.off(TrimmerTestsUtils.applyTrimmer(trimmer, w.on(get())))));
    }

    public Operand gives(final String $) {
      final Wrap w = Wrap.find(get());
      final String wrap = w.on(get()), unpeeled = applyTrimmer(trimmer, wrap);
      if (wrap.equals(unpeeled)) {
        System.err.printf("Quick fix by mark, copy and paste is:\n        .stays()//\n    ;");
        azzert.fail("Nothing done on " + get());
      }
      final String peeled = w.off(unpeeled);
      if (peeled.equals(get()))
        azzert.that("No trimming of " + get(), peeled, is(not(get())));
      if (tide.clean(peeled).equals(tide.clean(get())))
        azzert.that("Trimming of " + get() + "is just reformatting", tide.clean(get()), is(not(tide.clean(peeled))));
      if (!$.equals(peeled) && !essence(peeled).equals(essence($))) {
        System.err.printf(//
            "Quick fix by mark, copy and paste is:\n" + //
                "\n        .gives(\"%s\") //\n\n\n" + //
                "Compare with current " + //
                "\n        .gives(\"%s\") //\n",
            trivia.escapeQuotes(essence(peeled)), //
            trivia.escapeQuotes(essence($)));
        azzert.that(essence(peeled), is(essence($)));
      }
      return new Operand($);
    }

    /** Check whether one of the code options is correct
     * @param options
     * @return Operand
     * @author Dor Ma'ayan
     * @since 09-12-2016 */
    public Operand givesEither(final String... options) {
      assert options != null;
      final Wrap w = Wrap.find(get());
      final String wrap = w.on(get()), unpeeled = TrimmerTestsUtils.applyTrimmer(trimmer, wrap);
      if (wrap.equals(unpeeled))
        azzert.fail("Nothing done on " + get());
      final String peeled = w.off(unpeeled);
      if (peeled.equals(get()))
        azzert.that("No trimming of " + get(), peeled, is(not(get())));
      if (tide.clean(peeled).equals(tide.clean(get())))
        azzert.that("Trimming of " + get() + "is just reformatting", tide.clean(get()), is(not(tide.clean(peeled))));
      for (final String $ : options)
        if (essence($).equals(essence(peeled)))
          return new Operand($);
      azzert.fail("Expects: " + peeled + " But None of the given options match");
      return null;
    }

    public void stays() {
      final Wrap w = Wrap.find(get());
      final String wrap = w.on(get()), unpeeled = TrimmerTestsUtils.applyTrimmer(trimmer, wrap);
      if (wrap.equals(unpeeled))
        return;
      final String peeled = w.off(unpeeled);
      if (peeled.equals(get()) || tide.clean(peeled).equals(tide.clean(get())))
        return;
      final String expected = get();
      if (expected.equals(peeled) || !essence(peeled).equals(essence(expected)))
        return;
      System.err.printf(
          "Quick fix by mark, copy and paste is:\n" + //
              "        .gives(\"%s\") //\n\n\n\n" + //
              "Compare with \n" + //
              "        .gives(\"%s\") //\n", //
          essence(peeled), //
          essence(expected));
      azzert.that(essence(peeled), is(essence(expected)));
    }

    public <N extends ASTNode> Operand using(final Class<N> c, final Tipper<N> ¢) {
      trimmer.fix(c, ¢);
      return this;
    }

    @SafeVarargs public final <N extends ASTNode> Operand using(final Class<N> c, final Tipper<N>... ts) {
      as.list(ts).forEach(λ -> trimmer.addSingleTipper(c, λ));
      return this;
    }
  }

  static class OperandToTipper<N extends ASTNode> extends TrimmerTestsUtils.Operand {
    final Class<N> clazz;

    OperandToTipper(final String from, final Class<N> clazz) {
      super(from);
      this.clazz = clazz;
    }

    private N findNode(final Tipper<N> t) {
      assert t != null;
      final Wrap wrap = Wrap.find(get());
      assert wrap != null;
      final CompilationUnit u = wrap.intoCompilationUnit(get());
      assert u != null;
      final N $ = firstInstance(u);
      assert $ != null;
      return $;
    }

    private N firstInstance(final CompilationUnit u) {
      final Wrapper<N> $ = new Wrapper<>();
      u.accept(new ASTVisitor() {
        /** The implementation of the visitation procedure in the JDT seems to
         * be buggy. Each time we find a node which is an instance of the sought
         * class, we return false. Hence, we do not anticipate any further calls
         * to this function after the first such node is found. However, this
         * does not seem to be the case. So, in the case our wrapper is not
         * null, we do not carry out any further tests.
         * @param pattern the node currently being visited.
         * @return <code><b>true</b></code> <i>iff</i> the sought node is
         *         found. */
        @Override @SuppressWarnings("unchecked") public boolean preVisit2(final ASTNode ¢) {
          if ($.get() != null)
            return false;
          if (!clazz.isAssignableFrom(¢.getClass()))
            return true;
          $.set((N) ¢);
          return false;
        }
      });
      return $.get();
    }

    public OperandToTipper<N> in(final Tipper<N> ¢) {
      assert ¢.canTip(findNode(¢));
      return this;
    }

    public OperandToTipper<N> notIn(final Tipper<N> ¢) {
      assert !¢.canTip(findNode(¢));
      return this;
    }
  }
}
