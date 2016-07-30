package il.org.spartan.refactoring.wring;

import static il.org.spartan.hamcrest.CoreMatchers.is;
import static il.org.spartan.hamcrest.MatcherAssert.assertThat;
import static il.org.spartan.refactoring.spartanizations.TESTUtils.assertSimilar;
import static il.org.spartan.utils.Utils.compressSpaces;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.*;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jface.text.Document;
import org.junit.*;

import il.org.spartan.misc.Wrapper;
import il.org.spartan.refactoring.spartanizations.Spartanization;
import il.org.spartan.refactoring.spartanizations.TESTUtils;
import il.org.spartan.refactoring.spartanizations.Wrap;
import il.org.spartan.refactoring.utils.As;
import il.org.spartan.refactoring.wring.TrimmerTestsUtils.Operand;

public class TrimmerTestsUtils {
  @Test public void canParseEnum1() {
    new Operand("enum a{}");
  }
  private static void assertBalanced(final String message, final String s) {
    if (!isBalanced(s))
      fail( //
          new StringBuilder(message)//
              .append(" Unbalanced\n* Essence='") //
              .append(Wrap.essence(s))//
              .append("' len=").append(Wrap.essence(s).length())//
              .append("\n is not {[()]} balanced;") //
              .append("\n FULL=").append(s) //
              .append("   len=").append(s.length()) //
              .append("\n ERROR=").append(unblancingError(s)) //
              .toString() //
      ); //
  }
  @Test public void balanced() {
    assertBalanced("SIMPLE", "class T { T() { super(a); a();}}");
  }
  private static String unblancingError(String s) {
    final Stack<Character> $ = new Stack<Character>();
    int i = 0;
    char t;
    for (char c : s.toCharArray()) {
      switch (c) {
        case '{':
        case '(':
        case '[':
          $.push(c);
          break;
        case ']':
          if ($.isEmpty())
            return "No closers expected at " + i + ", but found '" + c;
          if ((t = $.pop()) != '[')
            return "Found ] at " + i + ", but expecting close of '" + t + "' remaning is ; " + $ + "" + s.substring(0, i);
          break;
        case ')':
          if ($.isEmpty())
            return "No closers expected at " + i + ", but found '" + c;
          if ((t = $.pop()) != '(')
            return "Found } at " + i + ", but expecting close of '" + t + "' remaning is ; " + $+ "" + s.substring(0, i);
          break;
        case '}':
          if ($.isEmpty())
            return "No closers expected at " + i + ", but found '" + c;
          if ((t = $.pop()) != '{')
            return "Found } at " + i + ", but expecting close of '" + t + "' remaning is ; " + $+ "" + s.substring(0, i);
          break;
      }
      i++;
    }
    if (!$.isEmpty())
      return "Expecting closers at end: " + $;
    return null;
  }
  public static boolean isBalanced(String in) {
    final Stack<Character> $ = new Stack<Character>();
    for (char c : in.toCharArray()) {
      switch (c) {
        default:
          continue;
        case '{':
        case '(':
        case '[':
          $.push(c);
          break;
        case ']':
          if ($.isEmpty() || $.pop() != '[')
            return false;
          break;
        case ')':
          if ($.isEmpty() || $.pop() != '(')
            return false;
          break;
        case '}':
          if ($.isEmpty() || $.pop() != '{')
            return false;
          break;
      }
    }
    return $.isEmpty();
  }
  @Test public void canParseEnum2() {
    Operand a = new Operand("enum a{}");
    assertNotNull(a.findWrap());
  }
  @Test public void canParseEnum3() {
    Operand a = new Operand("enum a{}");
    final Wrap $ = Wrap.find(a.get());
    if ($ == null)
      fail("Cannot parse '\n" + a.get() + "\n'; did you forget a semicolon?");
    Wrap x = $;
    assertNotNull(x);
  }
  @Test public void canParseEnum4() {
    String inner = "enum A{}";
    final Wrap $ = Wrap.find(inner);
    assertNotNull($);
  }
  @Test public void canParseEnum5() {
    new Operand("enum a{}");
  }

  static class OperandToWring<N extends ASTNode> extends TrimmerTestsUtils.Operand {
    final Class<N> clazz;

    public OperandToWring(final String from, final Class<N> clazz) {
      super(from);
      this.clazz = clazz;
    }
    public OperandToWring<N> in(final Wring<N> w) {
      final N findNode = findNode(w);
      assertThat(w.scopeIncludes(findNode), is(true));
      return this;
    }
    public OperandToWring<N> notIn(final Wring<N> w) {
      assertThat(w.scopeIncludes(findNode(w)), is(false));
      return this;
    }
    private N findNode(final Wring<N> w) {
      assertThat(w, notNullValue());
      final Wrap wrap = findWrap();
      assertThat(wrap, notNullValue());
      final CompilationUnit u = wrap.intoCompilationUnit(get());
      assertThat(u, notNullValue());
      final N $ = firstInstance(u);
      assertThat($, notNullValue());
      return $;
    }
    private N firstInstance(final CompilationUnit u) {
      final Wrapper<N> $ = new Wrapper<>();
      u.accept(new ASTVisitor() {
        /**
         * The implementation of the visitation procedure in the JDT seems to be
         * buggy. Each time we find a node which is an instance of the sought
         * class, we return false. Hence, we do not anticipate any further calls
         * to this function after the first such node is found. However, this
         * does not seem to be the case. So, in the case our wrapper is not
         * null, we do not carry out any further tests.
         *
         * @param n
         *          the node currently being visited.
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
    Wrap findWrap() {
      final Wrap $ = Wrap.find(get());
      if ($ == null)
        fail("Cannot parse \n        " + get() + "\n did you forget a semicolon?");
      return $;
    }
    private void checkExpected(final String expected) {
      assertBalanced("INPUT", get());
      assertBalanced("EXPECTED", expected);
      final Wrap w = findWrap();
      final String wrap = w.on(get());
      final String unpeeled = apply(new Trimmer(), wrap);
      if (wrap.equals(unpeeled))
        fail("Nothing done on " + get());
      assertBalanced("UNPEELED", unpeeled);
      final String peeled = w.off(unpeeled);
      if (peeled.equals(get()))
        assertNotEquals("No trimming of " + get(), get(), peeled);
      assertBalanced("PEELED", peeled);
      if (compressSpaces(peeled).equals(compressSpaces(get())))
        assertNotEquals("Trimming of " + get() + "is just reformatting", compressSpaces(peeled), compressSpaces(get()));
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
  }

  public static int countOpportunities(final Spartanization s, final CompilationUnit u) {
    return s.findOpportunities(u).size();
  }
  static String apply(final Trimmer t, final String from) {
    final CompilationUnit u = (CompilationUnit) As.COMPILIATION_UNIT.ast(from);
    assertNotNull(u);
    final Document d = new Document(from);
    assertNotNull(d);
    final Document $ = TESTUtils.rewrite(t, u, d);
    assertNotNull($);
    return $.get();
  }
  static String apply(final Wring<? extends ASTNode> ns, final String from) {
    final CompilationUnit u = (CompilationUnit) As.COMPILIATION_UNIT.ast(from);
    assertNotNull(u);
    final Document d = new Document(from);
    assertNotNull(d);
    return TESTUtils.rewrite(new AsSpartanization(ns, "Tested Refactoring"), u, d).get();
  }
  static void assertSimplifiesTo(final String from, final String expected, final Wring<? extends ASTNode> ns, final Wrap wrapper) {
    final String wrap = wrapper.on(from);
    final String unpeeled = apply(ns, wrap);
    if (wrap.equals(unpeeled))
      fail("Nothing done on " + from);
    final String peeled = wrapper.off(unpeeled);
    if (peeled.equals(from))
      assertNotEquals("No similification of " + from, from, peeled);
    if (compressSpaces(peeled).equals(compressSpaces(from)))
      assertNotEquals("Simpification of " + from + " is just reformatting", compressSpaces(peeled), compressSpaces(from));
    assertSimilar(expected, peeled);
  }
  static <N extends ASTNode> OperandToWring<N> included(final String from, final Class<N> clazz) {
    return new OperandToWring<>(from, clazz);
  }
  static Operand trimming(final String from) {
    return new Operand(from);
  }
}
