package il.org.spartan.refactoring.wring;

import static il.org.spartan.Utils.*;
import static il.org.spartan.azzert.*;
import static il.org.spartan.refactoring.spartanizations.TESTUtils.*;

import static il.org.spartan.refactoring.utils.Funcs.*;

import static il.org.spartan.refactoring.utils.Into.*;
import static il.org.spartan.refactoring.utils.Restructure.*;
import static org.junit.Assert.*;
import il.org.spartan.*;
import il.org.spartan.refactoring.contexts.*;
import il.org.spartan.refactoring.spartanizations.*;
import il.org.spartan.refactoring.suggestions.*;
import il.org.spartan.refactoring.utils.*;
import il.org.spartan.refactoring.wring.AbstractWringTest.WringedExpression.Conditional;
import il.org.spartan.refactoring.wring.AbstractWringTest.WringedExpression.Infix;

import org.eclipse.jdt.annotation.*;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.jface.text.*;
import org.eclipse.text.edits.*;
import org.hamcrest.*;
import org.junit.*;
import org.junit.runners.Parameterized.Parameter;

/**
 * @author Yossi Gil
 * @since 2015-07-18
 */
@SuppressWarnings({ "javadoc", "unchecked" })//
public class AbstractWringTest<@Nullable N extends ASTNode> extends AbstractTestBase {
  protected final Wring<N> inner;

  public AbstractWringTest() {
    this(null);
  }
  /**
   * Instantiates the enclosing class ({@link AbstractWringTest})
   *
   * @param inner JD
   */
  AbstractWringTest(final @Nullable Wring<N> inner) {
    this.inner = inner;
  }
  protected CompilationUnit asCompilationUnit() {
    return Wrap.Expression.intoCompilationUnit(input);
  }
  protected ConditionalExpression asConditionalExpression() {
    final ConditionalExpression $ = c(input);
    azzert.that($, notNullValue());
    return $;
  }
  protected Expression asExpression() {
    final Expression $ = e(input);
    azzert.that($, notNullValue());
    return $;
  }
  protected InfixExpression asInfixExpression() {
    final InfixExpression $ = i(input);
    azzert.that($, notNullValue());
    return $;
  }
  protected N asMe() {
    return null;
  }
  protected PrefixExpression asPrefixExpression() {
    final PrefixExpression $ = p(input);
    azzert.that($, notNullValue());
    return $;
  }
  void assertLegible(final String expression) {
    if (inner != null)
      azzert.that(inner.eligible((N) ast.EXPRESSION.from(expression)), is(true));
  }
  void assertNotLegible(final Block b) {
    if (inner != null)
      azzert.that(inner.eligible((N) b), is(false));
  }
  void assertNotLegible(final Expression e) {
    if (inner != null)
      azzert.that(inner.eligible((N) e), is(false));
  }
  void assertNotLegible(final IfStatement b) {
    if (inner != null)
      azzert.that(inner.eligible((N) b), is(false));
  }
  void assertWithinScope(final Expression e) {
    if (inner != null)
      azzert.that(inner.scopeIncludes((N) e), is(true));
  }
  void assertWithinScope(final String expression) {
    if (inner != null)
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
      if (inner != null)
        azzert.that(Toolbox.instance.find(asExpression()), instanceOf(inner.getClass()));
    }
    @Test public void eligible() {
      if (inner == null)
        return;
      if (inner != null)
        azzert.that(inner.eligible((N) asExpression()), is(false));
    }
    @Override @Test public void findsSimplifier() {
      if (inner == null)
        return;
      azzert.that(Toolbox.instance.find(asExpression()), notNullValue());
    }
    @Test public void noneligible() {
      if (inner == null)
        return;
      if (inner != null)
        azzert.that(inner.nonEligible((N) asExpression()), is(true));
    }
    @Test public void noOpporunity() {
      final CompilationUnit u = asCompilationUnit();
      azzert.that(u.toString() + wringer.collect(u), wringer.collect(u).size(), is(0));
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
        azzert.that(Toolbox.instance.find(asInfixExpression()), instanceOf(inner.getClass()));
      }
      @Test public void flattenIsIdempotentt() {
        if (input == null)
          return;
        final InfixExpression flatten = flatten(asInfixExpression());
        azzert.that(flatten(flatten).toString(), is(flatten.toString()));
      }
      @Test public void inputIsInfixExpression() {
        if (inner == null)
          return;
        azzert.that(asInfixExpression(), notNullValue());
      }
      @Override protected InfixExpression asMe() {
        final InfixExpression $ = i(input);
        azzert.that($, notNullValue());
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
          assertNotNull(asMe());
      }
      @Test public void scopeDoesNotInclude() {
        if (inner != null)
          azzert.that(inner.scopeIncludes(asMe()), is(false));
      }
      @Override protected VariableDeclarationFragment asMe() {
        return extract.firstVariableDeclarationFragment(ast.STATEMENTS.from(input));
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
        azzert.that($, notNullValue());
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
            azzert.that(asInfixExpression(), notNullValue());
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
          azzert.that(asConditionalExpression(), notNullValue());
      }
      @Test public void isConditionalExpression() {
        if (input != null)
          azzert.that(asMe(), instanceOf(ConditionalExpression.class));
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
        azzert.that("Nothing done on " + input, input, not(peeled));
        azzert.that("Wringing of " + input + " amounts to mere reformatting", compressSpaces(peeled), not(compressSpaces(input)));
        assertSimilar(expected, peeled);
        assertSimilar(Wrap.Statement.on(expected), excpected);
      }
      /**
       * In case of an IfStatemnet and surrounding, we search and then find the
       * first If statement in the input.
       */
      @Override protected IfStatement asMe() {
        return extract.firstIfStatement(ast.STATEMENTS.from(input));
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
        azzert.that(flatten(flatten).toString(), is(flatten.toString()));
      }
      @Test public void hasReplacementAsInfix() {
        if (inner == null)
          return;
        azzert.that(((Wring.ReplaceCurrentNode<InfixExpression>) inner).replacement(asInfixExpression()), notNullValue());
      }
      @Test public void inputIsInfixExpression() {
        if (inner == null)
          return;
        azzert.that(asInfixExpression(), notNullValue());
      }
      @Override protected final CompilationUnit asCompilationUnit() {
        final String s = input;
        final ASTNode $ = ast.COMPILIATION_UNIT.from(Wrap.Statement.on(s));
        azzert.that($, is(notNullValue()));
        azzert.that($, is(instanceOf(CompilationUnit.class)));
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
      azzert.that(Toolbox.instance.find(asBlock(asMe())), instanceOf(inner.getClass()));
    }
    @Test public void createRewrite() throws MalformedTreeException, IllegalArgumentException, BadLocationException {
      final String s = input;
      final Document d = new Document(Wrap.Statement.on(s));
      azzert.that(wringer.createRewrite(asCompilationUnit(), null).rewriteAST(d, null).apply(d), is(notNullValue()));
    }
    @Test public void eligible() {
      if (inner == null)
        return;
      azzert.that(inner.eligible(asMe()), is(true));
    }
    @Test public void findsSimplifierAsBlock() {
      if (inner == null)
        return;
      azzert.that(Toolbox.instance.find(asMe()), notNullValue());
    }
    @Test public void hasOpportunity() {
      if (inner == null)
        return;
      assertTrue(inner.scopeIncludes(asMe()));
      final CompilationUnit u = asCompilationUnit();
      azzert.that(u.toString(), wringer.collect(u).size(), is(greaterThanOrEqualTo(0)));
    }
    @Test public void hasReplacement() {
      if (inner == null)
        return;
      assertNotNull(((Wring.ReplaceCurrentNode<Block>) inner).replacement(asMe()));
    }
    @Test public void noneligible() {
      if (inner == null)
        return;
      azzert.that(inner.nonEligible(asMe()), is(false));
    }
    @Test public void peelableOutput() {
      if (inner == null)
        return;
      azzert.that(Wrap.Statement.off(Wrap.Statement.on(expected)), is(expected));
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
      azzert.that("Nothing done on " + input, input, not(peeled));
      azzert.that("Wringing of " + input + " amounts to mere reformatting", compressSpaces(peeled), not(compressSpaces(input)));
      assertSimilar(expected, peeled);
      assertSimilar(Wrap.Statement.on(expected), output);
    }
    @Override protected Block asMe() {
      final Statement s = s(input);
      azzert.that(s, notNullValue());
      final Block $ = asBlock(s);
      azzert.that($, notNullValue());
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
      azzert.that(Toolbox.instance.find(asExpression()), instanceOf(inner.getClass()));
    }
    @Test public void createRewrite() throws MalformedTreeException, IllegalArgumentException, BadLocationException {
      final String s = input;
      final Document d = new Document(Wrap.Expression.on(s));
      azzert.that(wringer.createRewrite(asCompilationUnit(), null).rewriteAST(d, null).apply(d), is(notNullValue()));
    }
    @Test public void eligible() {
      if (inner == null)
        return;
      assertTrue(inner.eligible((E) asExpression()));
    }
    @Override @Test public void findsSimplifier() {
      if (inner == null)
        return;
      azzert.that(Toolbox.instance.find(asExpression()), notNullValue());
    }
    @Test public void hasOpportunity() {
      if (inner == null)
        return;
      azzert.that(inner.scopeIncludes((E) asExpression()), is(true));
      final CompilationUnit u = asCompilationUnit();
      azzert.that(u.toString(), wringer.collect(u).size(), is(greaterThanOrEqualTo(1)));
    }
    @Test public void hasReplacement() {
      if (inner == null)
        return;
      azzert.that(((Wring.ReplaceCurrentNode<E>) inner).replacement((E) asExpression()), notNullValue());
    }
    @Test public void noneligible() {
      if (inner == null)
        return;
      azzert.that(inner.nonEligible((E) asExpression()), is(false));
    }
    @Test public void peelableOutput() {
      if (input == null)
        return;
      azzert.that(Wrap.Expression.off(Wrap.Expression.on(expected)), is(expected));
    }
    @Test public void scopeIncludes() {
      if (inner == null)
        return;
      azzert.that(inner.scopeIncludes((E) asExpression()), is(true));
    }
    @Test public void simiplifies() throws MalformedTreeException, IllegalArgumentException {
      if (inner == null)
        return;
      final CompilationUnit u = asCompilationUnit();
      final Document actual = TESTUtils.rewrite(wringer, u, asDocument());
      final String peeled = Wrap.Expression.off(actual.get());
      if (expected.equals(peeled))
        return;
      azzert.that("Nothing done on " + input, input, not(peeled));
      azzert.that("Wringing of " + input + " amounts to mere reformatting", compressSpaces(peeled), not(compressSpaces(input)));
      assertSimilar(expected, peeled);
      assertSimilar(Wrap.Expression.on(expected), actual);
    }
    @Test public void simiplifiesExpanded() throws MalformedTreeException, IllegalArgumentException, BadLocationException {
      if (inner == null)
        return;
      final CompilationUnit u = asCompilationUnit();
      final Document d = asDocument();
      Document actual = null;
      wringer.createRewrite(u, null).rewriteAST(d, null).apply(d);
      actual = d;
      if (actual == null)
        return;
      final String peeled = Wrap.Expression.off(actual.get());
      if (expected.equals(peeled))
        return;
      azzert.that("Nothing done on " + input, input, not(peeled));
      azzert.that("Wringing of " + input + " amounts to mere reformatting", compressSpaces(peeled), not(compressSpaces(input)));
      assertSimilar(expected, peeled);
      assertSimilar(Wrap.Expression.on(expected), actual);
    }
    @Override protected CompilationUnit asCompilationUnit() {
      final String s = input;
      final ASTNode $ = ast.COMPILIATION_UNIT.from(Wrap.Expression.on(s));
      azzert.that($, is(notNullValue()));
      azzert.that($, is(instanceOf(CompilationUnit.class)));
      return (CompilationUnit) $;
    }
    @Override protected Document asDocument() {
      return new Document(Wrap.Expression.on(input));
    }
    @Override protected E asMe() {
      final E $ = (E) e(input);
      azzert.that($, notNullValue());
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
        azzert.that(asExpression(), instanceOf(ConditionalExpression.class));
        azzert.that(asConditionalExpression(), notNullValue());
      }
      @Test public void scopeIncludesAsConditionalExpression() {
        if (inner == null)
          return;
        azzert.that(inner.scopeIncludes(asConditionalExpression()), is(true));
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
        azzert.that(flatten(flatten).toString(), is(flatten.toString()));
      }
      @Test public void hasReplacementAsInfix() {
        if (input == null)
          return;
        azzert.that(((Wring.ReplaceCurrentNode<InfixExpression>) inner).replacement(asInfixExpression()), notNullValue());
      }
      @Test public void inputIsInfixExpression() {
        if (input == null)
          return;
        azzert.that(asInfixExpression(), notNullValue());
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
      @Nullable final IfStatement me = asMe();
      assert me != null;
      azzert.that(me.toString(), Toolbox.instance.find(me), instanceOf(inner.getClass()));
    }
    @Test public void createRewrite() throws MalformedTreeException, IllegalArgumentException, BadLocationException {
      final String s = input;
      final Document d = new Document(Wrap.Statement.on(s));
      azzert.that(wringer.createRewrite(asCompilationUnit(), null).rewriteAST(d, null).apply(d), is(notNullValue()));
    }
    @Test public void eligible() {
      if (input == null)
        return;
      final IfStatement s = asMe();
      assert s != null;
      assertTrue(s.toString(), inner.eligible(s));
    }
    @Override @Test public void findsSimplifier() {
      if (input == null)
        return;
      azzert.that(Toolbox.instance.find(asMe()), notNullValue());
    }
    @Test public void hasOpportunity() {
      if (inner == null)
        return;
      azzert.that(inner.scopeIncludes(asMe()), is(true));
      final CompilationUnit u = asCompilationUnit();
      azzert.that(u.toString(), wringer.collect(u).size(), is(greaterThanOrEqualTo(0)));
    }
    @Test public void hasReplacement() {
      if (inner == null)
        return;
      azzert.that(((Wring.ReplaceCurrentNode<IfStatement>) inner).replacement(asMe()), notNullValue());
    }
    @Test public void hasSimplifier() {
      if (inner == null)
        return;
      @Nullable final IfStatement me = asMe();
      if (me != null)
        azzert.that(me.toString(), Toolbox.instance.find(asMe()), is(notNullValue()));
    }
    @Test public void noneligible() {
      if (input == null)
        return;
      azzert.that(inner.nonEligible(asMe()), is(false));
    }
    @Test public void peelableOutput() {
      if (input == null)
        return;
      azzert.that(Wrap.Statement.off(Wrap.Statement.on(expected)), CoreMatchers.is(expected));
    }
    @Test public void scopeIncludesAsMe() {
      if (input == null)
        return;
      @Nullable final IfStatement me = asMe();
      if (me != null)
        azzert.that(me.toString(), inner.scopeIncludes(me), is(true));
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
      azzert.that(input, not(peeled));
      azzert.that("Wringing of " + input + " amounts to mere reformatting", compressSpaces(peeled), not(compressSpaces(input)));
      assertSimilar(expected, peeled);
      assertSimilar(Wrap.Statement.on(expected), excpected);
    }
    @Override protected IfStatement asMe() {
      final Statement $ = asSingle(input);
      azzert.that($, notNullValue());
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
      azzert.that(Toolbox.instance.find(asMe()), instanceOf(inner.getClass()));
    }
    @Override @Test public void findsSimplifier() {
      if (inner == null)
        return;
      azzert.that(Toolbox.instance.find(asMe()), notNullValue());
    }
    @Override protected final CompilationUnit asCompilationUnit() {
      final String s = input;
      final ASTNode $ = ast.COMPILIATION_UNIT.from(Wrap.Statement.on(s));
      azzert.that($, is(notNullValue()));
      azzert.that($, is(instanceOf(CompilationUnit.class)));
      return (CompilationUnit) $;
    }
    @Override protected final Document asDocument() {
      return new Document(Wrap.Statement.on(input));
    }
    @Override protected N asMe() {
      final N $ = (N) s(input);
      azzert.that($, notNullValue());
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
      assert input != null;
      @Nullable final VariableDeclarationFragment me = asMe();
      assert me != null;
      assertNotNull(me);
      azzert.that(me.toString(), Toolbox.instance.find(me), instanceOf(inner.getClass()));
    }
    @Test public void createRewrite() throws MalformedTreeException, IllegalArgumentException, BadLocationException {
      final String s = input;
      final Document d = new Document(Wrap.Statement.on(s));
      final CompilationUnit u = asCompilationUnit();
      final ASTRewrite r = wringer.createRewrite(u, null);
      final TextEdit e = r.rewriteAST(d, null);
      azzert.that(e, notNullValue());
      azzert.that(e.apply(d), is(notNullValue()));
    }
    @Test public void eligible() {
      if (inner == null)
        return;
      final VariableDeclarationFragment s = asMe();
      assert s != null;
      azzert.that(s.toString(), inner.eligible(s), is(true));
    }
    @Override @Test public void findsSimplifier() {
      if (inner == null)
        return;
      azzert.that(Toolbox.instance.find(asMe()), notNullValue());
    }
    @Test public void hasOpportunity() {
      if (inner == null)
        return;
      azzert.that(inner.scopeIncludes(asMe()), is(true));
      final CompilationUnit u = asCompilationUnit();
      azzert.that(u.toString(), wringer.collect(u).size(), is(greaterThanOrEqualTo(1)));
    }
    @Test public void hasSimplifier() {
      if (input == null)
        return;
      @Nullable final VariableDeclarationFragment me = asMe();
      assert me != null;
      azzert.that(me.toString(), Toolbox.instance.find(asMe()), is(notNullValue()));
    }
    @Test public void noneligible() {
      if (inner == null)
        return;
      azzert.that(inner.nonEligible(asMe()), is(false));
    }
    @Test public void peelableOutput() {
      if (expected == null)
        return;
      azzert.that(Wrap.Statement.off(Wrap.Statement.on(expected)), is(expected));
    }
    @Test public void rewriteNotEmpty() throws MalformedTreeException, IllegalArgumentException {
      azzert.that(wringer.createRewrite(asCompilationUnit(), null), notNullValue());
    }
    @Test public void scopeIncludesAsMe() {
      if (inner == null)
        return;
      @Nullable final VariableDeclarationFragment me = asMe();
      assert me != null;
      azzert.that(me.toString(), inner.scopeIncludes(asMe()), is(true));
    }
    @Test public void simiplifies() throws MalformedTreeException, IllegalArgumentException {
      if (input == null)
        return;
      final Document d = new Document(Wrap.Statement.on(input));
      final CompilationUnit u = (CompilationUnit) ast.COMPILIATION_UNIT.from(d);
      final Document actual = TESTUtils.rewrite(wringer, u, d);
      final String peeled = Wrap.Statement.off(actual.get());
      if (expected.equals(peeled))
        return;
      azzert.that("Nothing done on " + input, input, not(peeled));
      azzert.that("Wringing of " + input + " amounts to mere reformatting", compressSpaces(peeled), not(compressSpaces(input)));
      assertSimilar(expected, peeled);
      assertSimilar(Wrap.Statement.on(expected), actual);
    }
    @Override protected CompilationUnit asCompilationUnit() {
      final CompilationUnit $ = (CompilationUnit) ast.COMPILIATION_UNIT.from(Wrap.Statement.on(input));
      azzert.that($, notNullValue());
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
      return extract.firstVariableDeclarationFragment(ast.STATEMENTS.from(input));
    }
  }

  /**
   * @author Yossi Gil
   * @since 2015-07-18
   */
  static class InScope<N extends ASTNode> extends AbstractWringTest<N> {
    protected final CurrentAST wringer = new CurrentAST();

    public InScope() {
      this(null);
    }
    InScope(final Wring<N> inner) {
      super(inner);
    }
    @Test public void correctSimplifier() {
      if (inner != null)
        azzert.that(Toolbox.instance.find(asExpression()), is(inner));
    }
    @Test public void findsSimplifier() {
      if (inner != null)
        azzert.that(Toolbox.instance.find(asExpression()), notNullValue());
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
        } catch (MalformedTreeException | IllegalArgumentException | BadLocationException e) {
          e.printStackTrace();
          return null;
        }
      }

      /** Where the expected output can be found? */
      @Parameter(2) public String output;
      protected final CurrentAST trimmer = new CurrentAST();

      /**
       * Instantiates the enclosing class ({@link WringedExpression})
       *
       * @param w JD
       */
      WringedInput(final Wring<N> w) {
        super(w);
      }
      @Test public void correctSimplifier() {
        azzert.that(Toolbox.instance.find(asExpression()), is(inner));
      }
      @Test public void createRewrite() throws MalformedTreeException, IllegalArgumentException, BadLocationException {
        final String s = input;
        final Document d = new Document(Wrap.Expression.on(s));
        azzert.that(trimmer.createRewrite(asCompilationUnit(), null).rewriteAST(d, null).apply(d), is(notNullValue()));
      }
      @Test public void eligible() {
        azzert.that(inner.eligible((N) asExpression()), is(true));
      }
      @Test public void findsSimplifier() {
        azzert.that(Toolbox.instance.find(asExpression()), notNullValue());
      }
      @Test public void hasReplacement() {
        azzert.that(((Wring.ReplaceCurrentNode<N>) inner).replacement((N) asExpression()), notNullValue());
      }
      @Test public void noneligible() {
        azzert.that(inner.nonEligible((N) asExpression()), is(false));
      }
      @Test public void oneOpporunity() {
        final CompilationUnit u = asCompilationUnit();
        azzert.that("" + u, trimmer.collect(u).size(), is(1));
        azzert.that(inner.scopeIncludes((N) asExpression()), is(true));
      }
      @Test public void peelableOutput() {
        azzert.that(Wrap.Expression.off(Wrap.Expression.on(output)), is(output));
      }
      @Test public void scopeIncludes() {
        if (inner != null)
          azzert.that(inner.scopeIncludes((N) asExpression()), is(false));
      }
      @Test public void simiplifies() throws MalformedTreeException, IllegalArgumentException {
        final CompilationUnit u = asCompilationUnit();
        final String s = input;
        final Document excpected = TESTUtils.rewrite(trimmer, u, new Document(Wrap.Expression.on(s)));
        final String peeled = Wrap.Expression.off(excpected.get());
        if (output.equals(peeled))
          return;
        azzert.that("Nothing done on " + input, input, is(not(peeled)));
        azzert.that("Wringing of " + input + " amounts to mere reformatting", compressSpaces(peeled), not(compressSpaces(input)));
        assertSimilar(output, peeled);
        assertSimilar(Wrap.Expression.on(output), excpected);
      }
      protected abstract Document asDocument();
    }
  }
}