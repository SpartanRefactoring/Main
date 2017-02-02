/** TODO: Yossi Gil <yossi.gil@gmail.com> please add a description
 * @author Yossi Gil <yossi.gil@gmail.com>
 * @since Sep 25, 2016 */
package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.azzert.*;
import static il.org.spartan.spartanizer.tippers.TESTUtils.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jface.text.*;

import il.org.spartan.*;
import il.org.spartan.plugin.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.spartanizer.utils.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public enum TrimmerTestsUtils {
  ;
  public static class Operand extends Wrapper<String> {
    @NotNull private final Trimmer trimmer;

    public Operand(final String inner) {
      super(inner);
      trimmer = new Trimmer();
    }

    void checkExpected(@NotNull final String expected) {
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

    public void stays() {
      final Wrap w = Wrap.find(get());
      final String wrap = w.on(get()), unpeeled = TrimmerTestsUtils.applyTrimmer(trimmer, wrap);
      if (wrap.equals(unpeeled))
        return;
      final String peeled = w.off(unpeeled);
      if (!peeled.equals(get()) && !tide.clean(peeled).equals(tide.clean(get())))
        assertSimilar(get(), peeled);
    }

    @NotNull public Operand gives(@NotNull final String $) {
      assert $ != null;
      final Wrap w = Wrap.find(get());
      final String wrap = w.on(get()), unpeeled = TrimmerTestsUtils.applyTrimmer(trimmer, wrap);
      if (wrap.equals(unpeeled))
        azzert.fail("Nothing done on " + get());
      final String peeled = w.off(unpeeled);
      if (peeled.equals(get()))
        azzert.that("No trimming of " + get(), peeled, is(not(get())));
      if (tide.clean(peeled).equals(tide.clean(get())))
        azzert.that("Trimming of " + get() + "is just reformatting", tide.clean(get()), is(not(tide.clean(peeled))));
      assertSimilar($, peeled);
      return new Operand($);
    }

    /** Check whether one of the code options is correct
     * @param options
     * @return Operand
     * @author Dor Ma'ayan
     * @since 09-12-2016 */
    @Nullable public Operand givesEither(@NotNull final String... options) {
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
        if (Wrap.essence($).equals(Wrap.essence(peeled)))
          return new Operand($);
      azzert.fail("Expects: " + peeled + " But None of the given options match");
      return null;
    }

    public void doesNotCrash() {
      final Wrap w = Wrap.find(get());
      assertNotEquals("Trimming of " + get() + " crashed", Wrap.essence(get()),
          Wrap.essence(w.off(TrimmerTestsUtils.applyTrimmer(trimmer, w.on(get())))));
    }

    @NotNull public <N extends ASTNode> Operand using(@NotNull final Class<N> c, final Tipper<N> ¢) {
      trimmer.fix(c, ¢);
      return this;
    }

    @SafeVarargs @NotNull public final <N extends ASTNode> Operand using(@NotNull final Class<N> c, final Tipper<N>... ts) {
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

    private N findNode(@NotNull final Tipper<N> t) {
      assert t != null;
      final Wrap wrap = Wrap.find(get());
      assert wrap != null;
      final CompilationUnit u = wrap.intoCompilationUnit(get());
      assert u != null;
      final N $ = firstInstance(u);
      assert $ != null;
      return $;
    }

    private N firstInstance(@NotNull final CompilationUnit u) {
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
        @Override @SuppressWarnings("unchecked") public boolean preVisit2(@NotNull final ASTNode ¢) {
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

    @NotNull public OperandToTipper<N> in(@NotNull final Tipper<N> ¢) {
      azzert.that(¢.canTip(findNode(¢)), is(true));
      return this;
    }

    @NotNull public OperandToTipper<N> notIn(@NotNull final Tipper<N> ¢) {
      azzert.that(¢.canTip(findNode(¢)), is(false));
      return this;
    }
  }

  static String apply(@NotNull final Tipper<? extends ASTNode> t, @NotNull final String from) {
    final CompilationUnit $ = (CompilationUnit) makeAST1.COMPILATION_UNIT.from(from);
    assert $ != null;
    return TESTUtils.rewrite(new TipperApplicator(t), $, new Document(from)).get();
  }

  public static String applyTrimmer(@NotNull final Trimmer t, @NotNull final String from) {
    final CompilationUnit u = (CompilationUnit) makeAST1.COMPILATION_UNIT.from(from);
    assert u != null;
    final Document $ = TESTUtils.rewrite(t, u, new Document(from));
    assert $ != null;
    return $.get();
  }

  /** [[SuppressWarningsSpartan]] */
  static void assertSimplifiesTo(final String from, @NotNull final String expected, @NotNull final Tipper<? extends ASTNode> n,
      @NotNull final Wrap w) {
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

  public static int countOpportunities(@NotNull final AbstractGUIApplicator a, @NotNull final CompilationUnit u) {
    return a.collectSuggestions(u).size();
  }

  @NotNull static <N extends ASTNode> OperandToTipper<N> included(final String from, final Class<N> clazz) {
    return new OperandToTipper<>(from, clazz);
  }

  @NotNull public static Operand trimmingOf(final String from) {
    return new Operand(from);
  }
}
