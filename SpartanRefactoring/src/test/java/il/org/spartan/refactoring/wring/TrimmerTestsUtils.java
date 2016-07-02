package il.org.spartan.refactoring.wring;

import static il.org.spartan.Utils.*;
import static il.org.spartan.azzert.*;
import static il.org.spartan.refactoring.spartanizations.TESTUtils.*;
import static org.junit.Assert.*;
import il.org.spartan.*;
import il.org.spartan.misc.*;
import il.org.spartan.refactoring.preferences.*;
import il.org.spartan.refactoring.spartanizations.*;
import il.org.spartan.refactoring.utils.*;

import java.util.*;

import org.eclipse.jdt.annotation.*;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jface.text.*;

public class TrimmerTestsUtils {
  static class OperandToWring<N extends ASTNode> extends TrimmerTestsUtils.Operand {
    final Class<N> clazz;

    public OperandToWring(final String from, final Class<N> clazz) {
      super(from);
      this.clazz = clazz;
    }
    public OperandToWring<N> in(final Wring<N> w) {
      final N findNode = findNode(w);
      that(w.createScalpel(null, null).scopeIncludes(findNode), is(true));
      return this;
    }
    public OperandToWring<N> notIn(final Wring<N> w) {
      that(w.createScalpel(null, null).scopeIncludes(findNode(w)), is(false));
      return this;
    }
    private N findNode(final Wring<N> w) {
      that(w, notNullValue());
      final Wrap wrap = findWrap();
      that(wrap, notNullValue());
      final CompilationUnit u = wrap.intoCompilationUnit(get());
      that(u, notNullValue());
      final @Nullable N $ = firstInstance(u);
      assert $ != null;
      that($, notNullValue());
      return $;
    }
    private @Nullable N firstInstance(final CompilationUnit u) {
      final maybe<@Nullable N> $ = maybe.no();
      u.accept(new ASTVisitor() {
        /**
         * The implementation of the visitation procedure in the JDT seems to be
         * buggy. Each time we find a node which is an instance of the sought
         * class, we return false. Hence, we do not anticipate any further calls
         * to this function after the first such node is found. However, this
         * does not seem to be the case. So, in the case our wrapper is not
         * null, we do not carry out any further tests.
         *
         * @param n the node currently being visited.
         * @return <code><b>true</b></code> <i>iff</i> the sought node is found.
         */
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
  }

  static class Operand extends Wrapper<String> {
    public enum OperandType {
      STATEMENT, EXPRESSION, METHOD, COMPILATION_UNIT
    }

    public Operand(final String inner) {
      super(inner);
    }
    public Operand to(final String expected) {
      if (expected == null || expected.isEmpty())
        checkSame();
      else
        checkExpected(expected);
      return new Operand(expected);
    }
    public Operand preservesComment() {
      final Wrap w = findWrap();
      final String wrap = w.on(get());
      final Set<String> csb = getComments(wrap);
      Source.set(Source.NONE_PATH, wrap);
      final String unpeeled = TrimmerTestsUtils.apply(new Trimmer(), wrap);
      assertThat("Nothing done on " + get(), wrap, not(unpeeled));
      final String peeled = w.off(unpeeled);
      assertThat("No trimming of " + get(), get(), not(peeled));
      assertThat("Trimming of " + get() + "is just reformatting", compressSpaces(peeled), not(compressSpaces(get())));
      final Set<String> csa = getComments(unpeeled);
      for (final String c : csb)
        assertTrue("Comment " + c + " not preserved", csa.contains(c));
      return new Operand(peeled);
    }
    public Operand toCompilationUnit(final String expected) {
      if (expected == null || expected.isEmpty())
        checkSame();
      else
        checkExpectedCompilationUnit(expected);
      return new Operand(expected);
    }
    Wrap findWrap() {
      final Wrap $ = Wrap.find(get());
      assertThat("Cannot parse '" + get() + "'; did you forget a semicolon?", $, notNullValue());
      return $;
    }
    private void checkExpectedCompilationUnit(final String expected) {
      String wrap;
      final Wrap w = Wrap.ComplilationUnit;
      wrap = w.on(get());
      final String unpeeled = TrimmerTestsUtils.applyCompilationUnit(new Trimmer(), wrap);
      assertThat("Nothing done on " + get(), wrap, not(unpeeled));
      final String peeled = w.off(unpeeled);
      assertThat("No trimming of " + get(), get(), not(peeled));
      assertThat("Trimming of " + get() + "is just reformatting", compressSpaces(peeled), not(compressSpaces(get())));
      assertSimilar(expected, peeled);
    }
    private void checkExpected(final String expected) {
      final Wrap w = findWrap();
      final String wrap = w.on(get());
      final String unpeeled = TrimmerTestsUtils.apply(new Trimmer(), wrap);
      assertThat("Nothing done on " + get(), wrap, not(unpeeled));
      final String peeled = w.off(unpeeled);
      assertThat("No trimming of " + get(), get(), not(peeled));
      assertThat("Trimming of " + get() + "is just reformatting", compressSpaces(peeled), not(compressSpaces(get())));
      assertSimilar(expected, peeled);
    }
    private void checkSame() {
      final Wrap w = findWrap();
      final String wrap = w.on(get());
      final String unpeeled = TrimmerTestsUtils.apply(new Trimmer(), wrap);
      if (wrap.equals(unpeeled))
        return;
      final String peeled = w.off(unpeeled);
      if (peeled.equals(get()) || compressSpaces(peeled).equals(compressSpaces(get())))
        return;
      assertSimilar(get(), peeled);
    }
    @SuppressWarnings("unchecked") private static Set<String> getComments(final String unpeeled) {
      final List<Comment> cs = ((CompilationUnit) ast.COMPILIATION_UNIT.from(unpeeled)).getCommentList();
      final Set<String> $ = new HashSet<>();
      for (final Comment c : cs)
        $.add(unpeeled.substring(c.getStartPosition(), c.getStartPosition() + c.getLength()));
      return $;
    }
  }

  public static int countOpportunities(final Spartanization s, final CompilationUnit u) {
    return s.findOpportunities(u).size();
  }
  static String applyCompilationUnit(final Trimmer t, final String from) {
    final ASTParser p = ASTParser.newParser(AST.JLS8);
    @SuppressWarnings("unchecked") final Map<String, String> options = JavaCore.getOptions();
    JavaCore.setComplianceOptions(JavaCore.VERSION_1_8, options);
    p.setCompilerOptions(options);
    p.setSource(from.toCharArray());
    p.setResolveBindings(PluginPreferencesResources.getResolveBindingEnabled());
    final CompilationUnit u = (CompilationUnit) p.createAST(null);
    that(u, notNullValue());
    final Document d = new Document(from);
    that(d, notNullValue());
    final Document $ = TESTUtils.rewrite(t, u, d);
    that($, notNullValue());
    return $.get();
  }
  static String apply(final Trimmer t, final String from) {
    final CompilationUnit u = (CompilationUnit) ast.COMPILIATION_UNIT.from(from);
    that(u, notNullValue());
    final Document d = new Document(from);
    that(d, notNullValue());
    final Document $ = TESTUtils.rewrite(t, u, d);
    that($, notNullValue());
    return $.get();
  }
  static String apply(final Wring<? extends ASTNode> ns, final String from) {
    final CompilationUnit u = (CompilationUnit) ast.COMPILIATION_UNIT.from(from);
    that(u, notNullValue());
    final Document d = new Document(from);
    that(d, notNullValue());
    return TESTUtils.rewrite(new AsSpartanization(ns, "Tested Refactoring"), u, d).get();
  }
  static void assertSimplifiesTo(final String from, final String expected, final Wring<? extends ASTNode> ns, final Wrap wrapper) {
    final String wrap = wrapper.on(from);
    final String unpeeled = apply(ns, wrap);
    assertThat("Nothing done on " + from, from, not(unpeeled));
    final String peeled = wrapper.off(unpeeled);
    assertThat("No similification of " + from, from, not(peeled));
    assertThat("Simpification of " + from + " is just reformatting", compressSpaces(peeled), not(compressSpaces(from)));
    assertSimilar(expected, peeled);
  }
  static <N extends ASTNode> OperandToWring<N> included(final String from, final Class<N> clazz) {
    return new OperandToWring<>(from, clazz);
  }
  static Operand trimming(final String from) {
    return new Operand(from + "\n");
  }
}
