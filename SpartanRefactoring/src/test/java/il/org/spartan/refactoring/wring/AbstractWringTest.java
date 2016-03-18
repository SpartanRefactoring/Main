package il.org.spartan.refactoring.wring;

import static il.org.spartan.hamcrest.CoreMatchers.is;
import static il.org.spartan.hamcrest.MatcherAssert.assertThat;
import static il.org.spartan.hamcrest.OrderingComparison.greaterThanOrEqualTo;
import static il.org.spartan.refactoring.spartanizations.TESTUtils.asSingle;
import static il.org.spartan.refactoring.spartanizations.TESTUtils.assertSimilar;
import static il.org.spartan.refactoring.utils.Funcs.asBlock;
import static il.org.spartan.refactoring.utils.Funcs.asIfStatement;
import static il.org.spartan.refactoring.utils.Into.*;
import static il.org.spartan.refactoring.utils.Restructure.flatten;
import static il.org.spartan.utils.Utils.compressSpaces;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.text.edits.TextEdit;
import org.junit.Test;
import org.junit.runners.Parameterized.Parameter;

import il.org.spartan.refactoring.spartanizations.Spartanization;
import il.org.spartan.refactoring.spartanizations.TESTUtils;
import il.org.spartan.refactoring.spartanizations.Wrap;
import il.org.spartan.refactoring.utils.As;
import il.org.spartan.refactoring.utils.Extract;
import il.org.spartan.refactoring.utils.Funcs;
import il.org.spartan.refactoring.wring.Toolbox;
import il.org.spartan.refactoring.wring.Trimmer;
import il.org.spartan.refactoring.wring.Wring;
import il.org.spartan.refactoring.wring.AbstractWringTest.WringedExpression.Conditional;
import il.org.spartan.refactoring.wring.AbstractWringTest.WringedExpression.Infix;

/**
 * @author Yossi Gil
 * @since 2015-07-18
 */
@SuppressWarnings({ "javadoc", "unchecked" }) //
public class AbstractWringTest<N extends ASTNode> extends AbstractTestBase {
  protected final Wring<N> inner;
  public AbstractWringTest() {
    this(null);
  }
  /**
   * Instantiates the enclosing class ({@link AbstractWringTest})
   *
   * @param inner JD
   */
  AbstractWringTest(final Wring<N> inner) {
    this.inner = inner;
  }
  protected CompilationUnit asCompilationUnit() {
    return Wrap.Expression.intoCompilationUnit(input);
  }
  protected ConditionalExpression asConditionalExpression() {
    final ConditionalExpression $ = c(input);
    assertNotNull($);
    return $;
  }
  protected Expression asExpression() {
    final Expression $ = e(input);
    assertNotNull($);
    return $;
  }
  protected InfixExpression asInfixExpression() {
    final InfixExpression $ = i(input);
    assertNotNull($);
    return $;
  }
  protected N asMe() {
    return null;
  }
  protected PrefixExpression asPrefixExpression() {
    final PrefixExpression $ = p(input);
    assertNotNull($);
    return $;
  }
  void assertLegible(final String expression) {
    assertTrue(inner.eligible((N) As.EXPRESSION.ast(expression)));
  }
  void assertNotLegible(final Block b) {
    assertThat(inner.eligible((N) b), is(false));
  }
  void assertNotLegible(final Expression e) {
    assertThat(inner.eligible((N) e), is(false));
  }
  void assertNotLegible(final IfStatement b) {
    assertThat(inner.eligible((N) b), is(false));
  }
  void assertWithinScope(final Expression e) {
    assertTrue(inner.scopeIncludes((N) e));
  }
  void assertWithinScope(final String expression) {
    assertWithinScope(e(expression));
  }
  void correctScopeExpression(final ASTNode n) {
    assertWithinScope(Funcs.asExpression(n));
  }

  /**
   * @author Yossi Gil
   * @since 2015-07-18
   */
  public static class Noneligible<N extends ASTNode> extends InScope<N> {
    /** Description of a test case for {@link Parameter} annotation */
    protected static final String DESCRIPTION = "Test #{index}. ({0}) \"{1}\" ==>|";
    public Noneligible() {
      this(null);
    }
    /**
     * Instantiates the enclosing class ({@link Noneligible})
     *
     * @param inner JD
     */
    Noneligible(final Wring<N> inner) {
      super(inner);
    }
    @Override @Test public void correctSimplifier() {
      if (inner == null)
        return;
      assertThat(Toolbox.instance.find(asExpression()), instanceOf(inner.getClass()));
    }
    @Test public void eligible() {
      if (inner == null)
        return;
      assertFalse(inner.eligible((N) asExpression()));
    }
    @Override @Test public void findsSimplifier() {
      if (inner == null)
        return;
      assertNotNull(Toolbox.instance.find(asExpression()));
    }
    @Test public void noneligible() {
      if (inner == null)
        return;
      assertTrue(inner.nonEligible((N) asExpression()));
    }
    @Test public void noOpporunity() {
      final CompilationUnit u = asCompilationUnit();
      assertEquals(u.toString() + wringer.findOpportunities(u), 0, wringer.findOpportunities(u).size());
    }
    @Test public void simiplifies() throws MalformedTreeException, IllegalArgumentException, BadLocationException {
      if (input == null)
        return;
      final Document d = asDocument();
      wringer.createRewrite(asCompilationUnit(), null).rewriteAST(d, null).apply(d);
      assertSimilar(compressSpaces(Wrap.Expression.off(d.get())), compressSpaces(input));
      assertSimilar(Wrap.Expression.on(input), d.get());
    }
    @Override protected final Document asDocument() {
      return new Document(Wrap.Expression.on(input));
    }

    public static class Infix extends Noneligible<InfixExpression> {
      public Infix() {
        this(null);
      }
      /** Instantiates the enclosing class ({@link Infix})@param simplifier */
      Infix(final Wring<InfixExpression> e) {
        super(e);
      }
      @Test public void correctSimplifieInfix() {
        if (inner == null)
          return;
        assertThat(Toolbox.instance.find(asInfixExpression()), instanceOf(inner.getClass()));
      }
      @Test public void flattenIsIdempotentt() {
        if (input == null)
          return;
        final InfixExpression flatten = flatten(asInfixExpression());
        assertThat(flatten(flatten).toString(), is(flatten.toString()));
      }
      @Test public void inputIsInfixExpression() {
        if (inner == null)
          return;
        assertNotNull(asInfixExpression());
      }
      @Override protected InfixExpression asMe() {
        final InfixExpression $ = i(input);
        assertNotNull($);
        return $;
      }
    }
  }

  /**
   * @author Yossi Gil
   * @since 2015-07-18
   */
  public static class OutOfScope<N extends ASTNode> extends AbstractWringTest<N> {
    /** Description of a test case for {@link Parameter} annotation */
    protected static final String DESCRIPTION = "Test #{index}. ({0}) \"{1}\" N/A";
    public OutOfScope() {
      super(null);
    }
    /** Instantiates the enclosing class ({@link OutOfScope})@param inner */
    OutOfScope(final Wring<N> inner) {
      super(inner);
    }

    public static class Declaration extends OutOfScope<VariableDeclarationFragment> {
      public Declaration() {
        this(null);
      }
      Declaration(final Wring<VariableDeclarationFragment> inner) {
        super(inner);
      }
      @Test public void asMeNotNull() {
        if (inner != null)
          assertThat(asMe(), notNullValue());
      }
      @Test public void scopeDoesNotInclude() {
        if (inner != null)
          assertThat(inner.scopeIncludes(asMe()), is(false));
      }
      @Override protected VariableDeclarationFragment asMe() {
        return Extract.firstVariableDeclarationFragment(As.STATEMENTS.ast(input));
      }
    }

    public static class Exprezzion<E extends Expression> extends OutOfScope<E> {
      public Exprezzion() {
        this(null);
      }
      Exprezzion(final Wring<E> inner) {
        super(inner);
      }
      @Override protected E asMe() {
        final E $ = (E) e(input);
        assertNotNull($);
        return $;
      }

      public static class Infix extends OutOfScope.Exprezzion<InfixExpression> {
        public Infix() {
          this(null);
        }
        /** Instantiates the enclosing class ({@link Infix})@param simplifier */
        Infix(final Wring<InfixExpression> e) {
          super(e);
        }
        @Test public void inputIsInfixExpression() {
          if (input != null)
            assertNotNull(asInfixExpression());
        }
      }
    }
  }

  public static class Wringed<N extends ASTNode> extends InScope<N> {
    /** Description of a test case for {@link Parameter} annotation */
    protected static final String DESCRIPTION = "Test #{index}. ({0}) \"{1}\" ==> \"{2}\"";
    /** What should the output be */
    @Parameter(2) public String expected;
    /** Default constructor to make Junit happy */
    public Wringed() {
      this(null);
    }
    Wringed(final Wring<N> inner) {
      super(inner);
    }
    @Override protected Document asDocument() {
      return new Document(Wrap.Expression.on(input));
    }

    public static class Conditional extends WringedExpression<ConditionalExpression> {
      /** Default constructor to make Junit happy */
      public Conditional() {
        this(null);
      }
      /**
       * Instantiates the enclosing class ({@link Conditional})
       *
       * @param w JD
       */
      Conditional(final Wring<ConditionalExpression> e) {
        super(e);
      }
      @Test public void inputIsConditionalExpression() {
        if (input != null)
          assertNotNull(asConditionalExpression());
      }
      @Test public void isConditionalExpression() {
        if (input != null)
          assertThat(asMe(), instanceOf(ConditionalExpression.class));
      }
    }

    public static class IfStatementAndSurrounding extends WringedIfStatement {
      public IfStatementAndSurrounding() {
        this(null);
      }
      /**
       * Instantiates the enclosing class ({@link IfStatementAndSurrounding})
       *
       * @param inner
       */
      IfStatementAndSurrounding(final Wring<IfStatement> inner) {
        super(inner);
      }
      @Override @Test public void hasReplacement() {
        // Eliminate test; in surrounding, you do not replace the text.
      }
      @Override @Test public void simiplifies() throws MalformedTreeException, IllegalArgumentException {
        if (input == null)
          return;
        final CompilationUnit u = asCompilationUnit();
        final Document excpected = TESTUtils.rewrite(wringer, u, asDocument());
        final String peeled = Wrap.Statement.off(excpected.get());
        if (expected.equals(peeled))
          return;
        if (input.equals(peeled))
          fail("Nothing done on " + input);
        if (compressSpaces(peeled).equals(compressSpaces(input)))
          assertNotEquals("Wringing of " + input + " amounts to mere reformatting", compressSpaces(peeled), compressSpaces(input));
        assertSimilar(expected, peeled);
        assertSimilar(Wrap.Statement.on(expected), excpected);
      }
      /**
       * In case of an IfStatemnet and surrounding, we search and then find the
       * first If statement in the input.
       */
      @Override protected IfStatement asMe() {
        return Extract.firstIfStatement(As.STATEMENTS.ast(input));
      }
    }

    public static class Infix extends WringedExpression<InfixExpression> {
      public Infix() {
        this(null);
      }
      /** Instantiates the enclosing class ({@link Infix})@param simplifier */
      Infix(final Wring<InfixExpression> e) {
        super(e);
      }
      @Test public void flattenIsIdempotentt() {
        if (inner == null)
          return;
        final InfixExpression flatten = flatten(asInfixExpression());
        assertThat(flatten(flatten).toString(), is(flatten.toString()));
      }
      @Test public void hasReplacementAsInfix() {
        if (inner == null)
          return;
        assertNotNull(((Wring.ReplaceCurrentNode<InfixExpression>) inner).replacement(asInfixExpression()));
      }
      @Test public void inputIsInfixExpression() {
        if (inner == null)
          return;
        assertNotNull(asInfixExpression());
      }
      @Override protected final CompilationUnit asCompilationUnit() {
        final String s = input;
        final ASTNode $ = As.COMPILIATION_UNIT.ast(Wrap.Statement.on(s));
        assertThat($, is(notNullValue()));
        assertThat($, is(instanceOf(CompilationUnit.class)));
        return (CompilationUnit) $;
      }
    }
  }

  public static class WringedBlock extends WringedStatement<Block> {
    /** Description of a test case for {@link Parameter} annotation */
    protected static final String DESCRIPTION = "Test #{index}. ({0}) \"{1}\" ==> \"{2}\"";
    /** What should the output be */
    @Parameter(2) public String expected;
    public WringedBlock() {
      super(null);
    }
    /**
     * Instantiates the enclosing class ({@link WringedBlock})
     *
     * @param inner
     */
    WringedBlock(final Wring<Block> inner) {
      super(inner);
    }
    @Test public void correctSimplifierAsBlock() {
      if (inner == null)
        return;
      assertThat(Toolbox.instance.find(asBlock(asMe())), instanceOf(inner.getClass()));
    }
    @Test public void createRewrite() throws MalformedTreeException, IllegalArgumentException, BadLocationException {
      final String s = input;
      final Document d = new Document(Wrap.Statement.on(s));
      assertThat(wringer.createRewrite(asCompilationUnit(), null).rewriteAST(d, null).apply(d), is(notNullValue()));
    }
    @Test public void eligible() {
      if (inner == null)
        return;
      assertTrue(inner.eligible(asMe()));
    }
    @Test public void findsSimplifierAsBlock() {
      if (inner == null)
        return;
      assertNotNull(Toolbox.instance.find(asMe()));
    }
    @Test public void hasOpportunity() {
      if (inner == null)
        return;
      assertTrue(inner.scopeIncludes(asMe()));
      final CompilationUnit u = asCompilationUnit();
      assertThat(u.toString(), wringer.findOpportunities(u).size(), is(greaterThanOrEqualTo(0)));
    }
    @Test public void hasReplacement() {
      if (inner == null)
        return;
      assertNotNull(((Wring.ReplaceCurrentNode<Block>) inner).replacement(asMe()));
    }
    @Test public void noneligible() {
      if (inner == null)
        return;
      assertFalse(inner.nonEligible(asMe()));
    }
    @Test public void peelableOutput() {
      if (inner == null)
        return;
      assertEquals(expected, Wrap.Statement.off(Wrap.Statement.on(expected)));
    }
    @Test public void scopeIncludes() {
      if (inner == null)
        return;
      assertTrue(inner.scopeIncludes(asMe()));
    }
    @Test public void simiplifies() throws MalformedTreeException, IllegalArgumentException {
      if (inner == null)
        return;
      final CompilationUnit u = asCompilationUnit();
      final String s = input;
      final Document output = TESTUtils.rewrite(wringer, u, new Document(Wrap.Statement.on(s)));
      final String peeled = Wrap.Statement.off(output.get());
      if (expected.equals(peeled))
        return;
      if (input.equals(peeled))
        fail("Nothing done on " + input);
      if (compressSpaces(peeled).equals(compressSpaces(input)))
        assertNotEquals("Wringing of " + input + " amounts to mere reformatting", compressSpaces(peeled), compressSpaces(input));
      assertSimilar(expected, peeled);
      assertSimilar(Wrap.Statement.on(expected), output);
    }
    @Override protected Block asMe() {
      final Statement s = s(input);
      assertNotNull(s);
      final Block $ = asBlock(s);
      assertNotNull($);
      return $;
    }
  }

  /**
   * @author Yossi Gil
   * @since 2015-07-15
   */
  public static class WringedExpression<E extends Expression> extends InScope<E> {
    /** Description of a test case for {@link Parameter} annotation */
    protected static final String DESCRIPTION = "Test #{index}. ({0}) \"{1}\" ==> \"{2}\"";
    /** What should the output be */
    @Parameter(2) public String expected;
    public WringedExpression() {
      this(null);
    }
    /**
     * Instantiates the enclosing class ( {@link WringedExpression})
     */
    WringedExpression(final Wring<E> inner) {
      super(inner);
    }
    @Override @Test public void correctSimplifier() {
      if (inner == null)
        return;
      assertThat(Toolbox.instance.find(asExpression()), instanceOf(inner.getClass()));
    }
    @Test public void createRewrite() throws MalformedTreeException, IllegalArgumentException, BadLocationException {
      final String s = input;
      final Document d = new Document(Wrap.Expression.on(s));
      assertThat(wringer.createRewrite(asCompilationUnit(), null).rewriteAST(d, null).apply(d), is(notNullValue()));
    }
    @Test public void eligible() {
      if (inner == null)
        return;
      assertTrue(inner.eligible((E) asExpression()));
    }
    @Override @Test public void findsSimplifier() {
      if (inner == null)
        return;
      assertNotNull(Toolbox.instance.find(asExpression()));
    }
    @Test public void hasOpportunity() {
      if (inner == null)
        return;
      assertTrue(inner.scopeIncludes((E) asExpression()));
      final CompilationUnit u = asCompilationUnit();
      assertThat(u.toString(), wringer.findOpportunities(u).size(), is(greaterThanOrEqualTo(1)));
    }
    @Test public void hasReplacement() {
      if (inner == null)
        return;
      assertNotNull(((Wring.ReplaceCurrentNode<E>) inner).replacement((E) asExpression()));
    }
    @Test public void noneligible() {
      if (inner == null)
        return;
      assertFalse(inner.nonEligible((E) asExpression()));
    }
    @Test public void peelableOutput() {
      if (input == null)
        return;
      assertEquals(expected, Wrap.Expression.off(Wrap.Expression.on(expected)));
    }
    @Test public void scopeIncludes() {
      if (inner == null)
        return;
      assertTrue(inner.scopeIncludes((E) asExpression()));
    }
    @Test public void simiplifies() throws MalformedTreeException, IllegalArgumentException {
      if (inner == null)
        return;
      final CompilationUnit u = asCompilationUnit();
      final Document actual = TESTUtils.rewrite(wringer, u, asDocument());
      final String peeled = Wrap.Expression.off(actual.get());
      if (expected.equals(peeled))
        return;
      if (input.equals(peeled))
        fail("Nothing done on " + input);
      if (compressSpaces(peeled).equals(compressSpaces(input)))
        assertNotEquals("Wringing of " + input + " amounts to mere reformatting", compressSpaces(peeled), compressSpaces(input));
      assertSimilar(expected, peeled);
      assertSimilar(Wrap.Expression.on(expected), actual);
    }
    @Test public void simiplifiesExpanded() throws MalformedTreeException, IllegalArgumentException {
      if (inner == null)
        return;
      final CompilationUnit u = asCompilationUnit();
      final Document d = asDocument();
      Document actual = null;
      // TESTUtils.rewrite(wringer, u, d);
      try {
        wringer.createRewrite(u, null).rewriteAST(d, null).apply(d);
        actual = d;
      } catch (final MalformedTreeException e) {
        fail(e.getMessage());
      } catch (final IllegalArgumentException e) {
        e.printStackTrace();
        fail(e.getMessage());
      } catch (final BadLocationException e) {
        fail(e.getMessage());
      }
      if (actual == null)
        return;
      final String peeled = Wrap.Expression.off(actual.get());
      if (expected.equals(peeled))
        return;
      if (input.equals(peeled))
        fail("Nothing done on " + input);
      if (compressSpaces(peeled).equals(compressSpaces(input)))
        assertNotEquals("Wringing of " + input + " amounts to mere reformatting", compressSpaces(peeled), compressSpaces(input));
      assertSimilar(expected, peeled);
      assertSimilar(Wrap.Expression.on(expected), actual);
    }
    @Override protected CompilationUnit asCompilationUnit() {
      final String s = input;
      final ASTNode $ = As.COMPILIATION_UNIT.ast(Wrap.Expression.on(s));
      assertThat($, is(notNullValue()));
      assertThat($, is(instanceOf(CompilationUnit.class)));
      return (CompilationUnit) $;
    }
    @Override protected Document asDocument() {
      return new Document(Wrap.Expression.on(input));
    }
    @Override protected E asMe() {
      final E $ = (E) e(input);
      assertNotNull($);
      return $;
    }

    public static class Conditional extends WringedExpression<ConditionalExpression> {
      public Conditional() {
        this(null);
      }
      /**
       * Instantiates the enclosing class ({@link Infix})
       *
       * @param w JD
       */
      Conditional(final Wring<ConditionalExpression> e) {
        super(e);
      }
      @Test public void inputIsConditionalExpression() {
        if (inner == null)
          return;
        assertThat(asExpression(), instanceOf(ConditionalExpression.class));
        assertNotNull(asConditionalExpression());
      }
      @Test public void scopeIncludesAsConditionalExpression() {
        if (inner == null)
          return;
        assertTrue(inner.scopeIncludes(asConditionalExpression()));
      }
    }

    public static class Infix extends WringedExpression<InfixExpression> {
      public Infix() {
        this(null);
      }
      /** Instantiates the enclosing class ({@link Infix})@param simplifier */
      Infix(final Wring<InfixExpression> e) {
        super(e);
      }
      @Test public void flattenIsIdempotentt() {
        if (input == null)
          return;
        final InfixExpression flatten = flatten(asInfixExpression());
        assertThat(flatten(flatten).toString(), is(flatten.toString()));
      }
      @Test public void hasReplacementAsInfix() {
        if (input == null)
          return;
        assertNotNull(((Wring.ReplaceCurrentNode<InfixExpression>) inner).replacement(asInfixExpression()));
      }
      @Test public void inputIsInfixExpression() {
        if (input == null)
          return;
        assertNotNull(asInfixExpression());
      }
    }
  }

  public static class WringedIfStatement extends WringedStatement<IfStatement> {
    /** Description of a test case for {@link Parameter} annotation */
    protected static final String DESCRIPTION = "Test #{index}. ({0}) \"{1}\" ==> \"{2}\"";
    /** What should the output be */
    @Parameter(2) public String expected;
    public WringedIfStatement() {
      this(null);
    }
    /**
     * Instantiates the enclosing class ({@link WringedInput})
     *
     * @param inner
     */
    WringedIfStatement(final Wring<IfStatement> inner) {
      super(inner);
    }
    @Override @Test public void correctSimplifier() {
      if (input == null)
        return;
      assertThat(asMe().toString(), Toolbox.instance.find(asMe()), instanceOf(inner.getClass()));
    }
    @Test public void createRewrite() throws MalformedTreeException, IllegalArgumentException, BadLocationException {
      final String s = input;
      final Document d = new Document(Wrap.Statement.on(s));
      assertThat(wringer.createRewrite(asCompilationUnit(), null).rewriteAST(d, null).apply(d), is(notNullValue()));
    }
    @Test public void eligible() {
      if (input == null)
        return;
      final IfStatement s = asMe();
      assertTrue(s.toString(), inner.eligible(s));
    }
    @Override @Test public void findsSimplifier() {
      if (input == null)
        return;
      assertNotNull(Toolbox.instance.find(asMe()));
    }
    @Test public void hasOpportunity() {
      if (inner == null)
        return;
      assertTrue(inner.scopeIncludes(asMe()));
      final CompilationUnit u = asCompilationUnit();
      assertThat(u.toString(), wringer.findOpportunities(u).size(), is(greaterThanOrEqualTo(0)));
    }
    @Test public void hasReplacement() {
      if (inner == null)
        return;
      assertNotNull(((Wring.ReplaceCurrentNode<IfStatement>) inner).replacement(asMe()));
    }
    @Test public void hasSimplifier() {
      if (inner == null)
        return;
      assertThat(asMe().toString(), Toolbox.instance.find(asMe()), is(notNullValue()));
    }
    @Test public void noneligible() {
      if (input == null)
        return;
      assertFalse(inner.nonEligible(asMe()));
    }
    @Test public void peelableOutput() {
      if (input == null)
        return;
      assertEquals(expected, Wrap.Statement.off(Wrap.Statement.on(expected)));
    }
    @Test public void scopeIncludesAsMe() {
      if (input == null)
        return;
      assertThat(asMe().toString(), inner.scopeIncludes(asMe()), is(true));
    }
    @Test public void simiplifies() throws MalformedTreeException, IllegalArgumentException {
      if (inner == null)
        return;
      final CompilationUnit u = asCompilationUnit();
      final String s = input;
      final Document excpected = TESTUtils.rewrite(wringer, u, new Document(Wrap.Statement.on(s)));
      final String peeled = Wrap.Statement.off(excpected.get());
      if (expected.equals(peeled))
        return;
      if (input.equals(peeled))
        fail("Nothing done on " + input);
      if (compressSpaces(peeled).equals(compressSpaces(input)))
        assertNotEquals("Wringing of " + input + " amounts to mere reformatting", compressSpaces(peeled), compressSpaces(input));
      assertSimilar(expected, peeled);
      assertSimilar(Wrap.Statement.on(expected), excpected);
    }
    @Override protected IfStatement asMe() {
      final Statement $ = asSingle(input);
      assertNotNull($);
      return asIfStatement($);
    }
  }

  public static class WringedStatement<N extends Statement> extends InScope<N> {
    public WringedStatement() {
      super(null);
    }
    WringedStatement(final Wring<N> inner) {
      super(inner);
    }
    @Override @Test public void correctSimplifier() {
      if (inner == null)
        return;
      assertThat(Toolbox.instance.find(asMe()), instanceOf(inner.getClass()));
    }
    @Override @Test public void findsSimplifier() {
      if (inner == null)
        return;
      assertNotNull(Toolbox.instance.find(asMe()));
    }
    @Override protected final CompilationUnit asCompilationUnit() {
      final String s = input;
      final ASTNode $ = As.COMPILIATION_UNIT.ast(Wrap.Statement.on(s));
      assertThat($, is(notNullValue()));
      assertThat($, is(instanceOf(CompilationUnit.class)));
      return (CompilationUnit) $;
    }
    @Override protected final Document asDocument() {
      return new Document(Wrap.Statement.on(input));
    }
    @Override protected N asMe() {
      final N $ = (N) s(input);
      assertNotNull($);
      return $;
    }
  }

  public static class WringedVariableDeclarationFragmentAndSurrounding extends Wringed<VariableDeclarationFragment> {
    public WringedVariableDeclarationFragmentAndSurrounding() {
      this(null);
    }
    WringedVariableDeclarationFragmentAndSurrounding(final Wring<VariableDeclarationFragment> inner) {
      super(inner);
    }
    @Override @Test public void correctSimplifier() {
      if (input != null)
        assertThat(asMe().toString(), Toolbox.instance.find(asMe()), instanceOf(inner.getClass()));
    }
    @Test public void createRewrite() throws MalformedTreeException, IllegalArgumentException, BadLocationException {
      final String s = input;
      final Document d = new Document(Wrap.Statement.on(s));
      final CompilationUnit u = asCompilationUnit();
      final ASTRewrite r = wringer.createRewrite(u, null);
      final TextEdit e = r.rewriteAST(d, null);
      assertThat(e, notNullValue());
      assertThat(e.apply(d), is(notNullValue()));
    }
    @Test public void eligible() {
      if (inner == null)
        return;
      final VariableDeclarationFragment s = asMe();
      assertTrue(s.toString(), inner.eligible(s));
    }
    @Override @Test public void findsSimplifier() {
      if (inner == null)
        return;
      assertNotNull(Toolbox.instance.find(asMe()));
    }
    @Test public void hasOpportunity() {
      if (inner == null)
        return;
      assertTrue(inner.scopeIncludes(asMe()));
      final CompilationUnit u = asCompilationUnit();
      assertThat(u.toString(), wringer.findOpportunities(u).size(), is(greaterThanOrEqualTo(1)));
    }
    @Test public void hasSimplifier() {
      if (input == null)
        return;
      assertThat(asMe().toString(), Toolbox.instance.find(asMe()), is(notNullValue()));
    }
    @Test public void noneligible() {
      if (inner == null)
        return;
      assertFalse(inner.nonEligible(asMe()));
    }
    @Test public void peelableOutput() {
      if (expected == null)
        return;
      assertEquals(expected, Wrap.Statement.off(Wrap.Statement.on(expected)));
    }
    @Test public void rewriteNotEmpty() throws MalformedTreeException, IllegalArgumentException {
      assertNotNull(wringer.createRewrite(asCompilationUnit(), null));
    }
    @Test public void scopeIncludesAsMe() {
      if (inner == null)
        return;
      assertThat(asMe().toString(), inner.scopeIncludes(asMe()), is(true));
    }
    @Test public void simiplifies() throws MalformedTreeException, IllegalArgumentException {
      if (input == null)
        return;
      final Document d = new Document(Wrap.Statement.on(input));
      final CompilationUnit u = (CompilationUnit) As.COMPILIATION_UNIT.ast(d);
      final Document actual = TESTUtils.rewrite(wringer, u, d);
      final String peeled = Wrap.Statement.off(actual.get());
      if (expected.equals(peeled))
        return;
      if (input.equals(peeled))
        fail("Nothing done on " + input);
      if (compressSpaces(peeled).equals(compressSpaces(input)))
        assertNotEquals("Wringing of " + input + " amounts to mere reformatting", compressSpaces(peeled), compressSpaces(input));
      assertSimilar(expected, peeled);
      assertSimilar(Wrap.Statement.on(expected), actual);
    }
    @Override protected CompilationUnit asCompilationUnit() {
      final CompilationUnit $ = (CompilationUnit) As.COMPILIATION_UNIT.ast(Wrap.Statement.on(input));
      assertNotNull($);
      return $;
    }
    /**
     * Instantiates the enclosing class ({@link WringedInput})
     *
     * @param inner
     */
    @Override protected final Document asDocument() {
      return new Document(Wrap.Statement.on(input));
    }
    @Override protected VariableDeclarationFragment asMe() {
      return Extract.firstVariableDeclarationFragment(As.STATEMENTS.ast(input));
    }
  }

  /**
   * @author Yossi Gil
   * @since 2015-07-18
   */
  static class InScope<N extends ASTNode> extends AbstractWringTest<N> {
    protected final Trimmer wringer = new Trimmer();
    public InScope() {
      this(null);
    }
    InScope(final Wring<N> inner) {
      super(inner);
    }
    @Test public void correctSimplifier() {
      if (inner != null)
        assertEquals(inner, Toolbox.instance.find(asExpression()));
    }
    @Test public void findsSimplifier() {
      if (inner != null)
        assertNotNull(Toolbox.instance.find(asExpression()));
    }
    @SuppressWarnings("static-method") protected Document asDocument() {
      return null;
    }

    /**
     * @author Yossi Gil
     * @since 2015-07-15
     */
    static abstract class WringedInput<N extends ASTNode> extends AbstractWringTest<N> {
      /** How should a test case like this be described? */
      protected static final String DESCRIPTION = "{index}: \"{1}\" => \"{2}\" ({0})";
      static Document rewrite(final Spartanization s, final CompilationUnit u, final Document d) {
        try {
          s.createRewrite(u, null).rewriteAST(d, null).apply(d);
          return d;
        } catch (final MalformedTreeException e) {
          fail(e.getMessage());
        } catch (final IllegalArgumentException e) {
          fail(e.getMessage());
        } catch (final BadLocationException e) {
          fail(e.getMessage());
        }
        return null;
      }
      /** Where the expected output can be found? */
      @Parameter(2) public String output;
      protected final Trimmer trimmer = new Trimmer();
      /**
       * Instantiates the enclosing class ({@link WringedExpression})
       *
       * @param w JD
       */
      WringedInput(final Wring<N> w) {
        super(w);
      }
      @Test public void correctSimplifier() {
        assertEquals(inner, Toolbox.instance.find(asExpression()));
      }
      @Test public void createRewrite() throws MalformedTreeException, IllegalArgumentException, BadLocationException {
        final String s = input;
        final Document d = new Document(Wrap.Expression.on(s));
        assertThat(trimmer.createRewrite(asCompilationUnit(), null).rewriteAST(d, null).apply(d), is(notNullValue()));
      }
      @Test public void eligible() {
        assertTrue(inner.eligible((N) asExpression()));
      }
      @Test public void findsSimplifier() {
        assertNotNull(Toolbox.instance.find(asExpression()));
      }
      @Test public void hasReplacement() {
        assertNotNull(((Wring.ReplaceCurrentNode<N>) inner).replacement((N) asExpression()));
      }
      @Test public void noneligible() {
        assertFalse(inner.nonEligible((N) asExpression()));
      }
      @Test public void oneOpporunity() {
        final CompilationUnit u = asCompilationUnit();
        assertEquals(u.toString(), 1, trimmer.findOpportunities(u).size());
        assertTrue(inner.scopeIncludes((N) asExpression()));
      }
      @Test public void peelableOutput() {
        assertEquals(output, Wrap.Expression.off(Wrap.Expression.on(output)));
      }
      @Test public void scopeIncludes() {
        assertFalse(inner.scopeIncludes((N) asExpression()));
      }
      @Test public void simiplifies() throws MalformedTreeException, IllegalArgumentException {
        final CompilationUnit u = asCompilationUnit();
        final String s = input;
        final Document excpected = TESTUtils.rewrite(trimmer, u, new Document(Wrap.Expression.on(s)));
        final String peeled = Wrap.Expression.off(excpected.get());
        if (output.equals(peeled))
          return;
        if (input.equals(peeled))
          fail("Nothing done on " + input);
        if (compressSpaces(peeled).equals(compressSpaces(input)))
          assertNotEquals("Wringing of " + input + " amounts to mere reformatting", compressSpaces(peeled), compressSpaces(input));
        assertSimilar(output, peeled);
        assertSimilar(Wrap.Expression.on(output), excpected);
      }
      protected abstract Document asDocument();
    }
  }
}