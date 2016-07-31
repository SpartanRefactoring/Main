package il.org.spartan.refactoring.wring;

import static il.org.spartan.azzert.*;
import static il.org.spartan.azzert.is;
import static il.org.spartan.refactoring.spartanizations.TESTUtils.*;
import static il.org.spartan.refactoring.utils.Funcs.*;
import static il.org.spartan.utils.Utils.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.jface.text.*;
import org.eclipse.text.edits.*;
import org.junit.*;
import org.junit.runner.*;
import org.junit.runners.*;
import org.junit.runners.Parameterized.*;

import il.org.spartan.*;
import il.org.spartan.refactoring.spartanizations.*;
import il.org.spartan.refactoring.utils.*;
import il.org.spartan.utils.Utils;

@SuppressWarnings({ "javadoc" }) //
@RunWith(Parameterized.class) //
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
public class DeclarationIfAssignmentWringedTest extends AbstractWringTest<VariableDeclarationFragment> {
  final static DeclarationInitializerIfAssignment WRING = new DeclarationInitializerIfAssignment();
  /** Description of a test case for {@link Parameter} annotation */
  protected static final String DESCRIPTION = "Test #{index}. ({0}) \"{1}\" ==> \"{2}\"";
  private static String[][] cases = Utils.asArray(//
      new String[] { "Vanilla with newline", "int a = 2; \n if (b) a =3;", "int a= b?3:2;" }, //
      new String[] { "Empty else", "int a=2; if (x) a = 3; else ;", " int a = x ? 3 : 2;" }, //
      new String[] { "Vanilla", "int a = 2; if (b) a =3;", "int a= b?3:2;" }, //
      new String[] { "Empty nested else", "int a=2; if (x) a = 3; else {{{}}}", " int a = x ? 3 : 2;" }, //
      new String[] { "Two fragments", //
          "int n2 = 0, n3;" + //
              "  if (d)\n" + //
              "    n2 = 2;", //
          "int n2 = d ? 2 : 0, n3;" },
      null);

  /**
   * Generate test cases for this parameterized class.
   *
   * @return a collection of cases, where each case is an array of three
   *         objects, the test case name, the input, and the file.
   */
  // TODO: JUnit bug: gets confused when value contains new line characters:
  // @Parameters(name = "Test #{index}. ({0}) \"{1}\" ==> \"{2}\"") //
  @Parameters(name = "Test #{index}. ({0}) ") //
  public static Collection<Object[]> cases() {
    return collect(cases);
  }

  /** What should the output be */
  @Parameter(2) public String expected;

  /** Instantiates the enclosing class ({@link Wringed}) */
  public DeclarationIfAssignmentWringedTest() {
    super(WRING);
  }
  DeclarationIfAssignmentWringedTest(final Wring<VariableDeclarationFragment> inner) {
    super(inner);
  }
  @Test public void checkIf() {
    final IfStatement s = findIf();
    azzert.notNull(s);
    azzert.that(Is.vacuousElse(s), is(true));
  }
  @Test public void correctSimplifier() {
    azzert.that(asMe().toString(), Toolbox.instance.find(asMe()), instanceOf(inner.getClass()));
  }
  @Test public void createRewrite() throws MalformedTreeException, IllegalArgumentException, BadLocationException {
    final String s = input;
    final Document d = new Document(Wrap.Statement.on(s));
    final CompilationUnit u = asCompilationUnit();
    final ASTRewrite r = new Trimmer().createRewrite(u, null);
    final TextEdit e = r.rewriteAST(d, null);
    azzert.notNull(e);
    azzert.notNull(e.apply(d));
  }
  @Test public void eligible() {
    final VariableDeclarationFragment s = asMe();
    azzert.aye(s.toString(), inner.eligible(s));
  }
  @Test public void findsSimplifier() {
    azzert.notNull(Toolbox.instance.find(asMe()));
  }
  @Test public void hasOpportunity() {
    azzert.aye(inner.scopeIncludes(asMe()));
    final CompilationUnit u = asCompilationUnit();
    azzert.that(u.toString(), new Trimmer().findOpportunities(u).size(), is(greaterThanOrEqualTo(1)));
  }
  @Test public void hasSimplifier() {
    azzert.notNull(asMe().toString(), Toolbox.instance.find(asMe()));
  }
  @Test public void noneligible() {
    azzert.nay(inner.nonEligible(asMe()));
  }
  @Test public void peelableOutput() {
    azzert.that(Wrap.Statement.off(Wrap.Statement.on(expected)), is(expected));
  }
  @Test public void rewriteNotEmpty() throws MalformedTreeException, IllegalArgumentException {
    azzert.notNull(new Trimmer().createRewrite(asCompilationUnit(), null));
  }
  @Test public void scopeIncludesAsMe() {
    azzert.that(asMe().toString(), inner.scopeIncludes(asMe()), is(true));
  }
  @Test public void simiplifies() throws MalformedTreeException, IllegalArgumentException {
    if (inner == null)
      return;
    final Document d = new Document(Wrap.Statement.on(input));
    final CompilationUnit u = (CompilationUnit) MakeAST.COMPILATION_UNIT.from(d);
    final Document actual = TESTUtils.rewrite(new Trimmer(), u, d);
    final String peeled = Wrap.Statement.off(actual.get());
    if (expected.equals(peeled))
      return;
    if (input.equals(peeled))
      azzert.fail("Nothing done on " + input);
    if (gist(peeled).equals(gist(input)))
      azzert.that("Wringing of " + input + " amounts to mere reformatting", gist(input), is(not(gist(peeled))));
    assertSimilar(expected, peeled);
    assertSimilar(Wrap.Statement.on(expected), actual);
  }
  @Test public void traceLegiblity() {
    final VariableDeclarationFragment f = asMe();
    final ASTRewrite r = ASTRewrite.create(f.getAST());
    final Expression initializer = f.getInitializer();
    azzert.notNull(f.toString(), initializer);
    final IfStatement s = extract.nextIfStatement(f);
    azzert.notNull(s);
    azzert.zero(extract.statements(elze(s)).size());
    final Assignment a = extract.assignment(then(s));
    azzert.notNull(a);
    azzert.aye(same(left(a), f.getName()));
    r.replace(initializer, Subject.pair(right(a), initializer).toCondition(s.getExpression()), null);
    r.remove(s, null);
  }
  @Override protected CompilationUnit asCompilationUnit() {
    final CompilationUnit $ = (CompilationUnit) MakeAST.COMPILATION_UNIT.from(Wrap.Statement.on(input));
    azzert.notNull($);
    return $;
  }
  @Override protected VariableDeclarationFragment asMe() {
    return extract.firstVariableDeclarationFragment(MakeAST.STATEMENTS.from(input));
  }
  private IfStatement findIf() {
    return extract.firstIfStatement(MakeAST.STATEMENTS.from(input));
  }
}