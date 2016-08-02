package il.org.spartan.refactoring.wring;

import static il.org.spartan.azzert.*;
import static il.org.spartan.refactoring.spartanizations.TESTUtils.*;
import static il.org.spartan.refactoring.utils.Funcs.*;
import static il.org.spartan.refactoring.utils.Into.*;
import static il.org.spartan.refactoring.utils.Restructure.*;
import static il.org.spartan.utils.Utils.*;
import il.org.spartan.*;
import il.org.spartan.refactoring.spartanizations.*;
import il.org.spartan.refactoring.utils.*;
import il.org.spartan.refactoring.wring.AbstractWringTest.WringedExpression.Conditional;
import il.org.spartan.refactoring.wring.AbstractWringTest.WringedExpression.Infix;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.jface.text.*;
import org.eclipse.text.edits.*;
import org.junit.*;
import org.junit.runners.Parameterized.Parameter;

/** @author Yossi Gil
 * @since 2015-07-18 */
@SuppressWarnings({ "javadoc", "unchecked" })//
public class AbstractWringTest<N extends ASTNode> extends AbstractTestBase {
  protected final Wring<N> inner;

  public AbstractWringTest() {
    this(null);
  }
  /** Instantiates the enclosing class ({@link AbstractWringTest})
   * @param inner JD */
  AbstractWringTest(final Wring<N> inner) {
    this.inner = inner;
  }
  protected CompilationUnit asCompilationUnit() {
    return Wrap.Expression.intoCompilationUnit(input);
  }
  protected ConditionalExpression asConditionalExpression() {
    final ConditionalExpression $ = c(input);
    azzert.notNull($);
    return $;
  }
  protected Expression asExpression() {
    final Expression $ = e(input);
    azzert.notNull($);
    return $;
  }
  protected InfixExpression asInfixExpression() {
    final InfixExpression $ = i(input);
    azzert.notNull($);
    return $;
  }
  protected N asMe() {
    return null;
  }
  protected PrefixExpression asPrefixExpression() {
    final PrefixExpression $ = p(input);
    azzert.notNull($);
    return $;
  }
  void assertLegible(final String expression) {
    azzert.aye(inner.eligible((N) MakeAST.EXPRESSION.from(expression)));
  }
  void assertNotLegible(final Block b) {
    azzert.that(inner.eligible((N) b), is(false));
  }
  void assertNotLegible(final Expression e) {
    azzert.that(inner.eligible((N) e), is(false));
  }
  void assertNotLegible(final IfStatement b) {
    azzert.that(inner.eligible((N) b), is(false));
  }
  void assertWithinScope(final Expression e) {
    azzert.aye(inner.scopeIncludes((N) e));
  }
  void assertWithinScope(final String expression) {
    assertWithinScope(e(expression));
  }
  void correctScopeExpression(final ASTNode n) {
    assertWithinScope(Funcs.asExpression(n));
  }

  /** @author Yossi Gil
   * @since 2015-07-18 */
  public static class Noneligible<N extends ASTNode> extends InScope<N> {
    /** Description of a test case for {@link Parameter} annotation */
    protected static final String DESCRIPTION = "Test #{index}. ({0}) \"{1}\" ==>|";

    public Noneligible() {
      this(null);
    }
    /** Instantiates the enclosing class ({@link Noneligible})
     * @param inner JD */
    Noneligible(final Wring<N> inner) {
      super(inner);
    }
    @Override @Test public void correctSimplifier() {
      if (inner == null)
        return;
      azzert.that(Toolbox.instance.find(asExpression()), instanceOf(inner.getClass()));
    }
    @Test public void eligible() {
      if (inner == null)
        return;
      azzert.nay(inner.eligible((N) asExpression()));
    }
    @Override @Test public void findsSimplifier() {
      if (inner == null)
        return;
      azzert.notNull(Toolbox.instance.find(asExpression()));
    }
    @Test public void noneligible() {
      if (inner == null)
        return;
      azzert.aye(inner.nonEligible((N) asExpression()));
    }
    @Test public void noOpporunity() {
      final CompilationUnit u = asCompilationUnit();
      azzert.that(u.toString() + wringer.findOpportunities(u), wringer.findOpportunities(u).size(), is(0));
    }
    @Test public void simiplifies() throws MalformedTreeException, IllegalArgumentException, BadLocationException {
      if (input == null)
        return;
      final Document d = asDocument();
      wringer.createRewrite(asCompilationUnit(), null).rewriteAST(d, null).apply(d);
      assertSimilar(gist(Wrap.Expression.off(d.get())), gist(input));
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
        azzert.notNull(asInfixExpression());
      }
      @Override protected InfixExpression asMe() {
        final InfixExpression $ = i(input);
        azzert.notNull($);
        return $;
      }
    }
  }

  /** @author Yossi Gil
   * @since 2015-07-18 */
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
          azzert.notNull(asMe());
      }
      @Test public void scopeDoesNotInclude() {
        if (inner != null)
          azzert.that(inner.scopeIncludes(asMe()), is(false));
      }
      @Override protected VariableDeclarationFragment asMe() {
        return extract.firstVariableDeclarationFragment(MakeAST.STATEMENTS.from(input));
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
        azzert.notNull($);
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
            azzert.notNull(asInfixExpression());
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
      /** Instantiates the enclosing class ({@link Conditional})
       * @param w JD */
      Conditional(final Wring<ConditionalExpression> e) {
        super(e);
      }
      @Test public void inputIsConditionalExpression() {
        if (input != null)
          azzert.notNull(asConditionalExpression());
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
      /** Instantiates the enclosing class ({@link IfStatementAndSurrounding})
       * @param inner */
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
        azzert.that("Nothing done on " + input, peeled, not(input));
        azzert.that("Wringing of " + input + " amounts to mere reformatting", gist(input), is(not(gist(peeled))));
        assertSimilar(expected, peeled);
        assertSimilar(Wrap.Statement.on(expected), excpected);
      }
      /** In case of an IfStatemnet and surrounding, we search and then find the
       * first If statement in the input. */
      @Override protected IfStatement asMe() {
        return extract.firstIfStatement(MakeAST.STATEMENTS.from(input));
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
        azzert.notNull(((Wring.ReplaceCurrentNode<InfixExpression>) inner).replacement(asInfixExpression()));
      }
      @Test public void inputIsInfixExpression() {
        if (inner == null)
          return;
        azzert.notNull(asInfixExpression());
      }
      @Override protected final CompilationUnit asCompilationUnit() {
        final String s = input;
        final ASTNode $ = MakeAST.COMPILATION_UNIT.from(Wrap.Statement.on(s));
        azzert.notNull($);
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
    /** Instantiates the enclosing class ({@link WringedBlock})
     * @param inner */
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
      azzert.notNull(wringer.createRewrite(asCompilationUnit(), null).rewriteAST(d, null).apply(d));
    }
    @Test public void eligible() {
      if (inner == null)
        return;
      azzert.aye(inner.eligible(asMe()));
    }
    @Test public void findsSimplifierAsBlock() {
      if (inner == null)
        return;
      azzert.notNull(Toolbox.instance.find(asMe()));
    }
    @Test public void hasOpportunity() {
      if (inner == null)
        return;
      azzert.aye(inner.scopeIncludes(asMe()));
      final CompilationUnit u = asCompilationUnit();
      azzert.that(u.toString(), wringer.findOpportunities(u).size(), is(greaterThanOrEqualTo(0)));
    }
    @Test public void hasReplacement() {
      if (inner == null)
        return;
      azzert.notNull(((Wring.ReplaceCurrentNode<Block>) inner).replacement(asMe()));
    }
    @Test public void noneligible() {
      if (inner == null)
        return;
      azzert.nay(inner.nonEligible(asMe()));
    }
    @Test public void peelableOutput() {
      if (inner == null)
        return;
      azzert.that(Wrap.Statement.off(Wrap.Statement.on(expected)), is(expected));
    }
    @Test public void scopeIncludes() {
      if (inner == null)
        return;
      azzert.aye(inner.scopeIncludes(asMe()));
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
      azzert.that("Nothing done on " + input, peeled, is(not(input)));
      azzert.that("Wringing of " + input + " amounts to mere reformatting", gist(peeled), is(not(gist(input))));
      assertSimilar(expected, peeled);
      assertSimilar(Wrap.Statement.on(expected), output);
    }
    @Override protected Block asMe() {
      final Statement s = s(input);
      azzert.notNull(s);
      final Block $ = asBlock(s);
      azzert.notNull($);
      return $;
    }
  }

  /** @author Yossi Gil
   * @since 2015-07-15 */
  public static class WringedExpression<E extends Expression> extends InScope<E> {
    /** Description of a test case for {@link Parameter} annotation */
    protected static final String DESCRIPTION = "Test #{index}. ({0}) \"{1}\" ==> \"{2}\"";
    /** What should the output be */
    @Parameter(2) public String expected;

    public WringedExpression() {
      this(null);
    }
    /** Instantiates the enclosing class ( {@link WringedExpression}) */
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
      azzert.notNull(wringer.createRewrite(asCompilationUnit(), null).rewriteAST(d, null).apply(d));
    }
    @Test public void eligible() {
      if (inner == null)
        return;
      azzert.aye(inner.eligible((E) asExpression()));
    }
    @Override @Test public void findsSimplifier() {
      if (inner == null)
        return;
      azzert.notNull(Toolbox.instance.find(asExpression()));
    }
    @Test public void hasOpportunity() {
      if (inner == null)
        return;
      azzert.aye(inner.scopeIncludes((E) asExpression()));
      final CompilationUnit u = asCompilationUnit();
      azzert.that(u.toString(), wringer.findOpportunities(u).size(), is(greaterThanOrEqualTo(1)));
    }
    @Test public void hasReplacement() {
      if (inner == null)
        return;
      azzert.notNull(((Wring.ReplaceCurrentNode<E>) inner).replacement((E) asExpression()));
    }
    @Test public void noneligible() {
      if (inner == null)
        return;
      azzert.nay(inner.nonEligible((E) asExpression()));
    }
    @Test public void peelableOutput() {
      if (input == null)
        return;
      azzert.that(Wrap.Expression.off(Wrap.Expression.on(expected)), is(expected));
    }
    @Test public void scopeIncludes() {
      if (inner == null)
        return;
      azzert.aye(inner.scopeIncludes((E) asExpression()));
    }
    @Test public void simiplifies() throws MalformedTreeException, IllegalArgumentException {
      if (inner == null)
        return;
      final CompilationUnit u = asCompilationUnit();
      final Document actual = TESTUtils.rewrite(wringer, u, asDocument());
      final String peeled = Wrap.Expression.off(actual.get());
      if (expected.equals(peeled))
        return;
      azzert.that("Nothing done on " + input, peeled, not(input));
      azzert.that("Wringing of " + input + " amounts to mere reformatting", gist(peeled), not(gist(input)));
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
        azzert.fail(e.getMessage());
      } catch (final IllegalArgumentException e) {
        e.printStackTrace();
        azzert.fail(e.getMessage());
      } catch (final BadLocationException e) {
        azzert.fail(e.getMessage());
      }
      if (actual == null)
        return;
      final String peeled = Wrap.Expression.off(actual.get());
      if (expected.equals(peeled))
        return;
      azzert.that("Nothing done on " + input, peeled, not(input));
      azzert.that("Wringing of " + input + " amounts to mere reformatting", gist(peeled), not(gist(input)));
      assertSimilar(expected, peeled);
      assertSimilar(Wrap.Expression.on(expected), actual);
    }
    @Override protected CompilationUnit asCompilationUnit() {
      final String s = input;
      final ASTNode $ = MakeAST.COMPILATION_UNIT.from(Wrap.Expression.on(s));
      azzert.notNull($);
      azzert.that($, is(instanceOf(CompilationUnit.class)));
      return (CompilationUnit) $;
    }
    @Override protected Document asDocument() {
      return new Document(Wrap.Expression.on(input));
    }
    @Override protected E asMe() {
      final E $ = (E) e(input);
      azzert.notNull($);
      return $;
    }

    public static class Conditional extends WringedExpression<ConditionalExpression> {
      public Conditional() {
        this(null);
      }
      /** Instantiates the enclosing class ({@link Infix})
       * @param w JD */
      Conditional(final Wring<ConditionalExpression> e) {
        super(e);
      }
      @Test public void inputIsConditionalExpression() {
        if (inner == null)
          return;
        azzert.that(asExpression(), instanceOf(ConditionalExpression.class));
        azzert.notNull(asConditionalExpression());
      }
      @Test public void scopeIncludesAsConditionalExpression() {
        if (inner == null)
          return;
        azzert.aye(inner.scopeIncludes(asConditionalExpression()));
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
        azzert.notNull(((Wring.ReplaceCurrentNode<InfixExpression>) inner).replacement(asInfixExpression()));
      }
      @Test public void inputIsInfixExpression() {
        if (input == null)
          return;
        azzert.notNull(asInfixExpression());
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
    /** Instantiates the enclosing class ({@link WringedInput})
     * @param inner */
    WringedIfStatement(final Wring<IfStatement> inner) {
      super(inner);
    }
    @Override @Test public void correctSimplifier() {
      if (input == null)
        return;
      azzert.that(asMe().toString(), Toolbox.instance.find(asMe()), instanceOf(inner.getClass()));
    }
    @Test public void createRewrite() throws MalformedTreeException, IllegalArgumentException, BadLocationException {
      final String s = input;
      final Document d = new Document(Wrap.Statement.on(s));
      azzert.notNull(wringer.createRewrite(asCompilationUnit(), null).rewriteAST(d, null).apply(d));
    }
    @Test public void eligible() {
      if (input == null)
        return;
      final IfStatement s = asMe();
      azzert.aye(s.toString(), inner.eligible(s));
    }
    @Override @Test public void findsSimplifier() {
      if (input == null)
        return;
      azzert.notNull(Toolbox.instance.find(asMe()));
    }
    @Test public void hasOpportunity() {
      if (inner == null)
        return;
      azzert.aye(inner.scopeIncludes(asMe()));
      final CompilationUnit u = asCompilationUnit();
      azzert.that(u.toString(), wringer.findOpportunities(u).size(), is(greaterThanOrEqualTo(0)));
    }
    @Test public void hasReplacement() {
      if (inner == null)
        return;
      azzert.notNull(((Wring.ReplaceCurrentNode<IfStatement>) inner).replacement(asMe()));
    }
    @Test public void hasSimplifier() {
      if (inner == null)
        return;
      azzert.notNull(asMe().toString(), Toolbox.instance.find(asMe()));
    }
    @Test public void noneligible() {
      if (input == null)
        return;
      azzert.nay(inner.nonEligible(asMe()));
    }
    @Test public void peelableOutput() {
      if (input == null)
        return;
      azzert.that(Wrap.Statement.off(Wrap.Statement.on(expected)), is(expected));
    }
    @Test public void scopeIncludesAsMe() {
      if (input == null)
        return;
      azzert.that(asMe().toString(), inner.scopeIncludes(asMe()), is(true));
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
        azzert.fail("Nothing done on " + input);
      if (gist(peeled).equals(gist(input)))
        assertNotEquals("Wringing of " + input + " amounts to mere reformatting", gist(peeled), gist(input));
      assertSimilar(expected, peeled);
      assertSimilar(Wrap.Statement.on(expected), excpected);
    }
    @Override protected IfStatement asMe() {
      final Statement $ = asSingle(input);
      azzert.notNull($);
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
      azzert.notNull(Toolbox.instance.find(asMe()));
    }
    @Override protected final CompilationUnit asCompilationUnit() {
      final String s = input;
      final ASTNode $ = MakeAST.COMPILATION_UNIT.from(Wrap.Statement.on(s));
      azzert.notNull($);
      azzert.that($, is(instanceOf(CompilationUnit.class)));
      return (CompilationUnit) $;
    }
    @Override protected final Document asDocument() {
      return new Document(Wrap.Statement.on(input));
    }
    @Override protected N asMe() {
      final N $ = (N) s(input);
      azzert.notNull($);
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
        azzert.that(asMe().toString(), Toolbox.instance.find(asMe()), instanceOf(inner.getClass()));
    }
    @Test public void createRewrite() throws MalformedTreeException, IllegalArgumentException, BadLocationException {
      final String s = input;
      final Document d = new Document(Wrap.Statement.on(s));
      final CompilationUnit u = asCompilationUnit();
      final ASTRewrite r = wringer.createRewrite(u, null);
      final TextEdit e = r.rewriteAST(d, null);
      azzert.notNull(e);
      azzert.notNull(e.apply(d));
    }
    @Test public void eligible() {
      if (inner == null)
        return;
      final VariableDeclarationFragment s = asMe();
      azzert.aye(s.toString(), inner.eligible(s));
    }
    @Override @Test public void findsSimplifier() {
      if (inner == null)
        return;
      azzert.notNull(Toolbox.instance.find(asMe()));
    }
    @Test public void hasOpportunity() {
      if (inner == null)
        return;
      azzert.aye(inner.scopeIncludes(asMe()));
      final CompilationUnit u = asCompilationUnit();
      azzert.that(u.toString(), wringer.findOpportunities(u).size(), is(greaterThanOrEqualTo(1)));
    }
    @Test public void hasSimplifier() {
      if (input == null)
        return;
      azzert.notNull(asMe().toString(), Toolbox.instance.find(asMe()));
    }
    @Test public void noneligible() {
      if (inner == null)
        return;
      azzert.nay(inner.nonEligible(asMe()));
    }
    @Test public void peelableOutput() {
      if (expected == null)
        return;
      azzert.that(Wrap.Statement.off(Wrap.Statement.on(expected)), is(expected));
    }
    @Test public void rewriteNotEmpty() throws MalformedTreeException, IllegalArgumentException {
      azzert.notNull(wringer.createRewrite(asCompilationUnit(), null));
    }
    @Test public void scopeIncludesAsMe() {
      if (inner == null)
        return;
      azzert.that(asMe().toString(), inner.scopeIncludes(asMe()), is(true));
    }
    @Test public void simiplifies() throws MalformedTreeException, IllegalArgumentException {
      if (input == null)
        return;
      final Document d = new Document(Wrap.Statement.on(input));
      final CompilationUnit u = (CompilationUnit) MakeAST.COMPILATION_UNIT.from(d);
      final Document actual = TESTUtils.rewrite(wringer, u, d);
      final String peeled = Wrap.Statement.off(actual.get());
      if (expected.equals(peeled))
        return;
      if (input.equals(peeled))
        azzert.fail("Nothing done on " + input);
      if (gist(peeled).equals(gist(input)))
        assertNotEquals("Wringing of " + input + " amounts to mere reformatting", gist(peeled), gist(input));
      assertSimilar(expected, peeled);
      assertSimilar(Wrap.Statement.on(expected), actual);
    }
    @Override protected CompilationUnit asCompilationUnit() {
      final CompilationUnit $ = (CompilationUnit) MakeAST.COMPILATION_UNIT.from(Wrap.Statement.on(input));
      azzert.notNull($);
      return $;
    }
    /** Instantiates the enclosing class ({@link WringedInput})
     * @param inner */
    @Override protected final Document asDocument() {
      return new Document(Wrap.Statement.on(input));
    }
    @Override protected VariableDeclarationFragment asMe() {
      return extract.firstVariableDeclarationFragment(MakeAST.STATEMENTS.from(input));
    }
  }

  /** @author Yossi Gil
   * @since 2015-07-18 */
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
        azzert.that(Toolbox.instance.find(asExpression()), is(inner));
    }
    @Test public void findsSimplifier() {
      if (inner != null)
        azzert.notNull(Toolbox.instance.find(asExpression()));
    }
    @SuppressWarnings("static-method") protected Document asDocument() {
      return null;
    }

    /** @author Yossi Gil
     * @since 2015-07-15 */
    static abstract class WringedInput<N extends ASTNode> extends AbstractWringTest<N> {
      /** How should a test case like this be described? */
      protected static final String DESCRIPTION = "{index}: \"{1}\" => \"{2}\" ({0})";

      static Document rewrite(final Spartanization s, final CompilationUnit u, final Document d) {
        try {
          s.createRewrite(u, null).rewriteAST(d, null).apply(d);
          return d;
        } catch (final MalformedTreeException e) {
          azzert.fail(e.getMessage());
        } catch (final IllegalArgumentException e) {
          azzert.fail(e.getMessage());
        } catch (final BadLocationException e) {
          azzert.fail(e.getMessage());
        }
        return null;
      }

      /** Where the expected output can be found? */
      @Parameter(2) public String output;
      protected final Trimmer trimmer = new Trimmer();

      /** Instantiates the enclosing class ({@link WringedExpression})
       * @param w JD */
      WringedInput(final Wring<N> w) {
        super(w);
      }
      @Test public void correctSimplifier() {
        azzert.that(Toolbox.instance.find(asExpression()), is(inner));
      }
      @Test public void createRewrite() throws MalformedTreeException, IllegalArgumentException, BadLocationException {
        final String s = input;
        final Document d = new Document(Wrap.Expression.on(s));
        azzert.notNull(trimmer.createRewrite(asCompilationUnit(), null).rewriteAST(d, null).apply(d));
      }
      @Test public void eligible() {
        azzert.aye(inner.eligible((N) asExpression()));
      }
      @Test public void findsSimplifier() {
        azzert.notNull(Toolbox.instance.find(asExpression()));
      }
      @Test public void hasReplacement() {
        azzert.notNull(((Wring.ReplaceCurrentNode<N>) inner).replacement((N) asExpression()));
      }
      @Test public void noneligible() {
        azzert.nay(inner.nonEligible((N) asExpression()));
      }
      @Test public void oneOpporunity() {
        final CompilationUnit u = asCompilationUnit();
        azzert.that(u.toString(), trimmer.findOpportunities(u).size(), is(1));
        azzert.aye(inner.scopeIncludes((N) asExpression()));
      }
      @Test public void peelableOutput() {
        azzert.that(Wrap.Expression.off(Wrap.Expression.on(output)), is(output));
      }
      @Test public void scopeIncludes() {
        azzert.nay(inner.scopeIncludes((N) asExpression()));
      }
      @Test public void simiplifies() throws MalformedTreeException, IllegalArgumentException {
        final CompilationUnit u = asCompilationUnit();
        final String s = input;
        final Document excpected = TESTUtils.rewrite(trimmer, u, new Document(Wrap.Expression.on(s)));
        final String peeled = Wrap.Expression.off(excpected.get());
        if (output.equals(peeled))
          return;
        azzert.that("Nothing done on " + input, peeled, not(input));
        if (gist(peeled).equals(gist(input)))
          azzert.that("Wringing of " + input + " amounts to mere reformatting", gist(peeled), not(gist(input)));
        assertSimilar(output, peeled);
        assertSimilar(Wrap.Expression.on(output), excpected);
      }
      protected abstract Document asDocument();
    }
  }
}