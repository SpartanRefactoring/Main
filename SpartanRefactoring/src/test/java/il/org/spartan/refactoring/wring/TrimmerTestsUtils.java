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

import java.util.Map;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jface.text.Document;

import il.org.spartan.misc.Wrapper;
import il.org.spartan.refactoring.spartanizations.Spartanization;
import il.org.spartan.refactoring.spartanizations.TESTUtils;
import il.org.spartan.refactoring.spartanizations.Wrap;
import il.org.spartan.refactoring.utils.As;

public class TrimmerTestsUtils {

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
		public static enum OperandType {
			STATEMENT, EXPRESSION, METHOD, COMPILATION_UNIT
		};
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
	      Wrap w = Wrap.ComplilationUnit;

	    	wrap = w.on(get());

	      final String unpeeled = TrimmerTestsUtils.applyCompilationUnit(new Trimmer(), wrap);
	      if (wrap.equals(unpeeled))
	        fail("Nothing done on " + get());
	      final String peeled = w.off(unpeeled);
	      if (peeled.equals(get()))
	        assertNotEquals("No trimming of " + get(), get(), peeled);
	      if (compressSpaces(peeled).equals(compressSpaces(get())))
	        assertNotEquals("Trimming of " + get() + "is just reformatting", compressSpaces(peeled), compressSpaces(get()));
	      assertSimilar(expected, peeled);
	    }
	    private void checkExpected(final String expected) {
	      final Wrap w = findWrap();
	      final String wrap = w.on(get());
	      final String unpeeled = TrimmerTestsUtils.apply(new Trimmer(), wrap);
	      if (wrap.equals(unpeeled))
	        fail("Nothing done on " + get());
	      final String peeled = w.off(unpeeled);
	      if (peeled.equals(get()))
	        assertNotEquals("No trimming of " + get(), get(), peeled);
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

	static String applyCompilationUnit(final Trimmer t, final String from) {
		ASTParser p = ASTParser.newParser(AST.JLS8);
		Map options = JavaCore.getOptions();
		JavaCore.setComplianceOptions(JavaCore.VERSION_1_8, options);
		p.setCompilerOptions(options);
		p.setSource(from.toCharArray());
		CompilationUnit u = (CompilationUnit)p.createAST(null);
		assertNotNull(u);
		final Document d = new Document(from);
		assertNotNull(d);
	    final Document $ = TESTUtils.rewrite(t, u, d);
	    assertNotNull($);
	    return $.get();
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
