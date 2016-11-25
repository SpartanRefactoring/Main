package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.azzert.*;
import static il.org.spartan.spartanizer.tippers.TESTUtils.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jface.text.*;

import il.org.spartan.*;
import il.org.spartan.plugin.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.spartanizer.utils.*;

public final class TrimmerTestsUtils {
  public static int countOpportunities(final AbstractGUIApplicator a, final CompilationUnit u) {
    return a.collectSuggestions(u).size();
  }

  static String apply(final Tipper<? extends ASTNode> n, final String from) {
    final CompilationUnit u = (CompilationUnit) makeAST.COMPILATION_UNIT.from(from);
    assert u != null;
    return TESTUtils.rewrite(new TipperApplicator(n), u, new Document(from)).get();
  }

  static String applyTrimmer(final Trimmer t, final String from) {
    final CompilationUnit u = (CompilationUnit) makeAST.COMPILATION_UNIT.from(from);
    assert u != null;
    final Document d = new Document(from);
    final Document $ = TESTUtils.rewrite(t, u, d);
    assert $ != null;
    return $.get();
  }

  static void assertSimplifiesTo(final String from, final String expected, final Tipper<? extends ASTNode> n, final Wrap w) {
    final String wrap = w.on(from);
    final String unpeeled = apply(n, wrap);
    if (wrap.equals(unpeeled))
      azzert.fail("Nothing done on " + from);
    final String peeled = w.off(unpeeled);
    if (peeled.equals(from))
      azzert.that("No similification of " + from, peeled, is(not(from)));
    if (tide.clean(peeled).equals(tide.clean(from)))
      azzert.that("Simpification of " + from + " is just reformatting", tide.clean(from), is(not(tide.clean(peeled))));
    assertSimilar(expected, peeled);
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

    public Operand gives(final String expected) {
      assert expected != null;
      final Wrap w = Wrap.find(get());
      final String wrap = w.on(get());
      final String unpeeled = TrimmerTestsUtils.applyTrimmer(trimmer, wrap);
      if (wrap.equals(unpeeled))
        azzert.fail("Nothing done on " + get());
      final String peeled = w.off(unpeeled);
      if (peeled.equals(get()))
        azzert.that("No trimming of " + get(), peeled, is(not(get())));
      if (tide.clean(peeled).equals(tide.clean(get())))
        azzert.that("Trimming of " + get() + "is just reformatting", tide.clean(get()), is(not(tide.clean(peeled))));
      assertSimilar(expected, peeled);
      return new Operand(expected);
    }

    public <N extends ASTNode> Operand withTipper(final Class<N> n, final Tipper<N> t) {
      trimmer.add(n, t);
      return this;
    }

    public void stays() {
      checkSame();
    }

    void checkExpected(final String expected) {
      final Wrap w = Wrap.find(get());
      final String wrap = w.on(get());
      final String unpeeled = TrimmerTestsUtils.applyTrimmer(new Trimmer(), wrap);
      if (wrap.equals(unpeeled))
        azzert.fail("Nothing done on " + get());
      final String peeled = w.off(unpeeled);
      if (peeled.equals(get()))
        azzert.that("No trimming of " + get(), peeled, is(not(get())));
      if (tide.clean(peeled).equals(tide.clean(get())))
        azzert.that("Trimming of " + get() + "is just reformatting", tide.clean(get()), is(not(tide.clean(peeled))));
      assertSimilar(expected, peeled);
    }

    private void checkSame() {
      final Wrap w = Wrap.find(get());
      final String wrap = w.on(get());
      final String unpeeled = TrimmerTestsUtils.applyTrimmer(new Trimmer(), wrap);
      if (wrap.equals(unpeeled))
        return;
      final String peeled = w.off(unpeeled);
      if (!peeled.equals(get()) && !tide.clean(peeled).equals(tide.clean(get())))
        assertSimilar(get(), peeled);
    }
  }

  static class OperandToTipper<N extends ASTNode> extends TrimmerTestsUtils.Operand {
    final Class<N> clazz;

    public OperandToTipper(final String from, final Class<N> clazz) {
      super(from);
      this.clazz = clazz;
    }

    public OperandToTipper<N> in(final Tipper<N> n) {
      final N findNode = findNode(n);
      azzert.that(n.canTip(findNode), is(true));
      return this;
    }

    public OperandToTipper<N> notIn(final Tipper<N> ¢) {
      azzert.that(¢.canTip(findNode(¢)), is(false));
      return this;
    }

    private N findNode(final Tipper<N> n) {
      assert n != null;
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
  }
}