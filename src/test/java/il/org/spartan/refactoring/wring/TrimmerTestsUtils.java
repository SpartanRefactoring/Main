package il.org.spartan.refactoring.wring;

import static il.org.spartan.azzert.*;
import static il.org.spartan.refactoring.spartanizations.TESTUtils.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jface.text.*;

import il.org.spartan.*;
import il.org.spartan.refactoring.engine.*;
import il.org.spartan.refactoring.spartanizations.*;
import il.org.spartan.refactoring.utils.*;

public class TrimmerTestsUtils {
  public static int countOpportunities(final Spartanization s, final CompilationUnit u) {
    return s.findOpportunities(u).size();
  }

  static String apply(final Trimmer t, final String from) {
    final CompilationUnit u = (CompilationUnit) makeAST.COMPILATION_UNIT.from(from);
    assert u != null;
    final Document d = new Document(from);
    assert d != null;
    final Document $ = TESTUtils.rewrite(t, u, d);
    assert $ != null;
    return $.get();
  }

  static String apply(final Wring<? extends ASTNode> ns, final String from) {
    final CompilationUnit u = (CompilationUnit) makeAST.COMPILATION_UNIT.from(from);
    assert u != null;
    final Document d = new Document(from);
    assert d != null;
    return TESTUtils.rewrite(new AsSpartanization(ns, "Tested Refactoring"), u, d).get();
  }

  static void assertSimplifiesTo(final String from, final String expected, final Wring<? extends ASTNode> ns, final Wrap wrapper) {
    final String wrap = wrapper.on(from);
    final String unpeeled = apply(ns, wrap);
    if (wrap.equals(unpeeled))
      azzert.fail("Nothing done on " + from);
    final String peeled = wrapper.off(unpeeled);
    if (peeled.equals(from))
      azzert.that("No similification of " + from, peeled, is(not(from)));
    if (tide.clean(peeled).equals(tide.clean(from)))
      azzert.that("Simpification of " + from + " is just reformatting", tide.clean(from), is(not(tide.clean(peeled))));
    assertSimilar(expected, peeled);
  }

  static <N extends ASTNode> OperandToWring<N> included(final String from, final Class<N> clazz) {
    return new OperandToWring<>(from, clazz);
  }

  static Operand trimming(final String from) {
    return new Operand(from);
  }

  static class Operand extends Wrapper<String> {
    public Operand(final String inner) {
      super(inner);
    }

    public Operand to(final String expected) {
      if (expected == null || expected.isEmpty())
        checkSame();
      else {
        final Wrap w = Wrap.find(get());
        final String wrap = w.on(get());
        final String unpeeled = TrimmerTestsUtils.apply(new Trimmer(), wrap);
        // System.out.println(unpeeled);
        if (wrap.equals(unpeeled))
          azzert.fail("Nothing done on " + get());
        final String peeled = w.off(unpeeled);
        if (peeled.equals(get()))
          azzert.that("No trimming of " + get(), peeled, is(not(get())));
        if (tide.clean(peeled).equals(tide.clean(get())))
          azzert.that("Trimming of " + get() + "is just reformatting", tide.clean(get()), is(not(tide.clean(peeled))));
        assertSimilar(expected, peeled);
      }
      return new Operand(expected);
    }

    void checkExpected(final String expected) {
      final Wrap w = Wrap.find(get());
      final String wrap = w.on(get());
      final String unpeeled = TrimmerTestsUtils.apply(new Trimmer(), wrap);
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
      final String unpeeled = TrimmerTestsUtils.apply(new Trimmer(), wrap);
      if (wrap.equals(unpeeled))
        return;
      final String peeled = w.off(unpeeled);
      if (peeled.equals(get()) || tide.clean(peeled).equals(tide.clean(get())))
        return;
      assertSimilar(get(), peeled);
    }

    public Operand to(final String expected) {
      if (expected == null || expected.isEmpty())
        checkSame();
      else {
        final Wrap w = Wrap.find(get());
        final String wrap = w.on(get());
        final String unpeeled = TrimmerTestsUtils.apply(new Trimmer(), wrap);
        //System.out.println(unpeeled);
        if (wrap.equals(unpeeled))
          azzert.fail("Nothing done on " + get());
        final String peeled = w.off(unpeeled);
        if (peeled.equals(get()))
          azzert.that("No trimming of " + get(), peeled, is(not(get())));
        if (tide.clean (peeled).equals(tide.clean (get())))
          azzert.that("Trimming of " + get() + "is just reformatting", tide.clean (get()), is(not(tide.clean (peeled))));
        assertSimilar("Attempting " ,expected, peeled);
      }
      return new Operand(expected);
    }
  }

  static class OperandToWring<N extends ASTNode> extends TrimmerTestsUtils.Operand {
    final Class<N> clazz;

    public OperandToWring(final String from, final Class<N> clazz) {
      super(from);
      this.clazz = clazz;
    }

    public OperandToWring<N> in(final Wring<N> w) {
      final N findNode = findNode(w);
      azzert.that(w.scopeIncludes(findNode), is(true));
      return this;
    }

    public OperandToWring<N> notIn(final Wring<N> w) {
      azzert.that(w.scopeIncludes(findNode(w)), is(false));
      return this;
    }

    private N findNode(final Wring<N> w) {
      assert w != null;
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
         * @param n the node currently being visited.
         * @return <code><b>true</b></code> <i>iff</i> the sought node is
         *         found. */
        @SuppressWarnings("unchecked") @Override public boolean preVisit2(final ASTNode n) {
          if ($.get() != null)
            return false;
          if (!clazz.isAssignableFrom(n.getClass()))
            return true;
          $.set((N) n);
          return false;
        }
      });
      return $.get();
    }

    public OperandToWring<N> in(final Wring<N> w) {
      final N findNode = findNode(w);
      azzert.that(w.claims(findNode), is(true));
      return this;
    }

    public OperandToWring<N> notIn(final Wring<N> w) {
      azzert.that(w.claims(findNode(w)), is(false));
      return this;
    }
  }

  static String apply(final Trimmer t, final String from) {
    final CompilationUnit u = (CompilationUnit) makeAST.COMPILATION_UNIT.from(from);
    azzert.notNull(u);
    final Document d = new Document(from);
    azzert.notNull(d);
    final Document $ = TESTUtils.rewrite(t, u, d);
    azzert.notNull($);
    return $.get();
  }

  static String apply(final Wring<? extends ASTNode> ns, final String from) {
    final CompilationUnit u = (CompilationUnit) makeAST.COMPILATION_UNIT.from(from);
    azzert.notNull(u);
    final Document d = new Document(from);
    azzert.notNull(d);
    return TESTUtils.rewrite(new AsSpartanization(ns, "Tested Refactoring"), u, d).get();
  }

  static void assertSimplifiesTo(final String from, final String expected, final Wring<? extends ASTNode> ns, final Wrap wrapper) {
    final String wrap = wrapper.on(from);
    final String unpeeled = apply(ns, wrap);
    if (wrap.equals(unpeeled))
      azzert.fail("Nothing done on " + from);
    final String peeled = wrapper.off(unpeeled);
    if (peeled.equals(from))
      azzert.that("No similification of " + from, peeled, is(not(from)));
    if (tide.clean (peeled).equals(tide.clean (from)))
      azzert.that("Simpification of " + from + " is just reformatting", tide.clean (from), is(not(tide.clean (peeled))));
    assertSimilar(expected, peeled);
  }

  public static int countOpportunities(final Spartanization s, final CompilationUnit u) {
    return s.findOpportunities(u).size();
  }

  static <N extends ASTNode> OperandToWring<N> included(final String from, final Class<N> clazz) {
    return new OperandToWring<>(from, clazz);
  }

  static Operand trimming(final String from) {
    return new Operand(from);
  }
}