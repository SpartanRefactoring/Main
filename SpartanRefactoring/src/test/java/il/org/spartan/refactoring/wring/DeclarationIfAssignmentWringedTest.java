package il.org.spartan.refactoring.wring;

import static il.org.spartan.Utils.*;
import static il.org.spartan.azzert.*;
import static il.org.spartan.refactoring.spartanizations.TESTUtils.*;
import static il.org.spartan.refactoring.utils.Funcs.*;
import static org.junit.Assert.*;
import il.org.spartan.*;
import il.org.spartan.refactoring.spartanizations.*;
import il.org.spartan.refactoring.utils.*;

import java.util.*;

import org.eclipse.jdt.annotation.*;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.jface.text.*;
import org.eclipse.text.edits.*;
import org.junit.*;
import org.junit.runner.*;
import org.junit.runners.*;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

@SuppressWarnings({ "javadoc" })//
@RunWith(Parameterized.class)//
@FixMethodOrder(MethodSorters.NAME_ASCENDING)//
public class DeclarationIfAssignmentWringedTest extends AbstractWringTest<VariableDeclarationFragment> {
  final static DeclarationInitializerIfAssignment WRING = new DeclarationInitializerIfAssignment();
  /** Description of a test case for {@link Parameter} annotation */
  protected static final String DESCRIPTION = "Test #{index}. ({0}) \"{1}\" ==> \"{2}\"";
  private static String[][] cases = as.array(//
      new String[] { "Vanilla with newline", "int a = 2; \n if (b) a =3;", "int a= b?3:2;" }, //
      new String[] { "Empty else", "int a=2; if (x) a = 3; else ;", " int a = x ? 3 : 2;" }, //
      new String[] { "Vanilla", "int a = 2; if (b) a =3;", "int a= b?3:2;" }, //
      new String[] { "Empty nested else", "int a=2; if (x) a = 3; else {{{}}}", " int a = x ? 3 : 2;" }, //
      new String[] { "Two fragments", //
          "int n2 = 0, n3;" + //
              "  if (d)\n" + //
              "    n2 = 2;", //
          "int n2 = d ? 2 : 0, n3;" }, null);

  /**
   * Generate test cases for this parameterized class.
   *
   * @return a collection of cases, where each case is an array of three
   *         objects, the test case name, the input, and the file.
   */
  // TODO: JUnit bug: gets confused when value contains new line characters:
  // @Parameters(name = "Test #{index}. ({0}) \"{1}\" ==> \"{2}\"") //
  @Parameters(name = "Test #{index}. ({0}) ")//
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
    that(s, notNullValue());
    that(Is.vacuousElse(s), is(true));
  }
  @Test public void correctSimplifier() {
    @Nullable final VariableDeclarationFragment asMe = asMe();
    assert asMe != null;
    assertThat(asMe.toString(), Toolbox.instance.find(asMe()), instanceOf(inner.getClass()));
  }
  @Test public void createRewrite() throws MalformedTreeException, IllegalArgumentException, BadLocationException {
    final String s = input;
    final Document d = new Document(Wrap.Statement.on(s));
    final CompilationUnit u = asCompilationUnit();
    final ASTRewrite r = new Trimmer().createRewrite(u, null);
    final TextEdit e = r.rewriteAST(d, null);
    that(e, notNullValue());
    that(e.apply(d), is(notNullValue()));
  }
  @Test public void eligible() {
    final VariableDeclarationFragment s = asMe();
    assert inner != null;
    assert s != null;
    assertTrue(s.toString(), inner.eligible(s));
  }
  @Test public void findsSimplifier() {
    that(Toolbox.instance.find(asMe()), notNullValue());
  }
  @Test public void hasOpportunity() {
    that(inner.scopeIncludes(asMe()), is(true));
    final CompilationUnit u = asCompilationUnit();
    that(u.toString(), new Trimmer().findOpportunities(u).size(), is(greaterThanOrEqualTo(1)));
  }
  @Test public void hasSimplifier() {
    @Nullable final VariableDeclarationFragment asMe = asMe();
    assert asMe != null;
    assertThat(asMe.toString(), Toolbox.instance.find(asMe), is(notNullValue()));
  }
  @Test public void noneligible() {
    that(inner.nonEligible(asMe()), is(false));
  }
  @Test public void peelableOutput() {
    that(Wrap.Statement.off(Wrap.Statement.on(expected)), is(expected));
  }
  @Test public void rewriteNotEmpty() throws MalformedTreeException, IllegalArgumentException {
    that(new Trimmer().createRewrite(asCompilationUnit(), null), notNullValue());
  }
  @Test public void scopeIncludesAsMe() {
    @Nullable final VariableDeclarationFragment asMe = asMe();
    assert asMe != null;
    that(asMe.toString(), inner.scopeIncludes(asMe()), is(true));
  }
  @Test public void simiplifies() throws MalformedTreeException, IllegalArgumentException {
    if (inner == null)
      return;
    final Document d = new Document(Wrap.Statement.on(input));
    final CompilationUnit u = (CompilationUnit) ast.COMPILIATION_UNIT.from(d);
    final Document actual = TESTUtils.rewrite(new Trimmer(), u, d);
    final String peeled = Wrap.Statement.off(actual.get());
    if (expected.equals(peeled))
      return;
    assertThat("Nothing done on " + input, input, not(peeled));
    assertThat("Wringing of " + input + " amounts to mere reformatting", compressSpaces(peeled), not(compressSpaces(input)));
    assertSimilar(expected, peeled);
    assertSimilar(Wrap.Statement.on(expected), actual);
  }
  @Test public void traceLegiblity() {
    final VariableDeclarationFragment f = asMe();
    assert f != null;
    final ASTRewrite r = ASTRewrite.create(f.getAST());
    final Expression initializer = f.getInitializer();
    assertThat(f.toString(), initializer, notNullValue());
    final IfStatement s = extract.nextIfStatement(f);
    that(s, notNullValue());
    that(extract.statements(elze(s)).size(), is(0));
    final Assignment a = extract.assignment(then(s));
    that(a, notNullValue());
    that(same(left(a), f.getName()), is(true));
    r.replace(initializer, Subject.pair(right(a), initializer).toCondition(s.getExpression()), null);
    r.remove(s, null);
  }
  @Override protected CompilationUnit asCompilationUnit() {
    final CompilationUnit $ = (CompilationUnit) ast.COMPILIATION_UNIT.ast(Wrap.Statement.on(input));
    assertNotNull($);
    return $;
  }
  @Override protected VariableDeclarationFragment asMe() {
    return extract.firstVariableDeclarationFragment(ast.STATEMENTS.ast(input));
  }
  private IfStatement findIf() {
    return extract.firstIfStatement(ast.STATEMENTS.ast(input));
  }
}