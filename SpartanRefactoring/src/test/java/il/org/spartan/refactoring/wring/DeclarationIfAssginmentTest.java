package il.org.spartan.refactoring.wring;

import static il.org.spartan.Utils.*;
import static il.org.spartan.azzert.*;
import static il.org.spartan.refactoring.spartanizations.TESTUtils.*;
import static il.org.spartan.refactoring.utils.Funcs.*;
import il.org.spartan.*;
import il.org.spartan.refactoring.spartanizations.*;
import il.org.spartan.refactoring.utils.*;
import il.org.spartan.refactoring.utils.Collect.Of;
import il.org.spartan.refactoring.wring.Wring.VariableDeclarationFragementAndStatement;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.jface.text.*;
import org.eclipse.text.edits.*;
import org.junit.*;
import org.junit.runner.*;
import org.junit.runners.*;
import org.junit.runners.Parameterized.Parameters;

/**
 * @author Yossi Gil
 * @since 2014-07-13
 */
@SuppressWarnings({ "javadoc", "static-method" })//
@FixMethodOrder(MethodSorters.NAME_ASCENDING)//
public class DeclarationIfAssginmentTest {
  static final DeclarationInitializerIfAssignment WRING = new DeclarationInitializerIfAssignment();

  @Test public void traceForbiddenSiblings() {
    that(WRING, notNullValue());
    final String from = "int a = 2,b; if (b) a =3;";
    final String wrap = Wrap.Statement.on(from);
    final CompilationUnit u = (CompilationUnit) ast.COMPILIATION_UNIT.ast(wrap);
    final VariableDeclarationFragment f = extract.firstVariableDeclarationFragment(u);
    that(f, notNullValue());
    that(WRING.scopeIncludes(f), is(false));
  }
  @Test public void traceForbiddenSiblingsExpanded() {
    final String from = "int a = 2,b; if (a+b) a =3;";
    final String wrap = Wrap.Statement.on(from);
    final CompilationUnit u = (CompilationUnit) ast.COMPILIATION_UNIT.ast(wrap);
    final VariableDeclarationFragment f = extract.firstVariableDeclarationFragment(u);
    that(f, notNullValue());
    final Expression initializer = f.getInitializer();
    that(initializer, notNullValue());
    final IfStatement s = extract.nextIfStatement(f);
    that(s, is(extract.firstIfStatement(u)));
    that(s, notNullValue());
    that(s, iz("if (a + b) a=3;"));
    azzert.that(Is.vacuousElse(s), is(true));
    final Assignment a = extract.assignment(then(s));
    that(a, notNullValue());
    that(same(left(a), f.getName()), is(true));
    that(a.getOperator(), is(Assignment.Operator.ASSIGN));
    final List<VariableDeclarationFragment> x = VariableDeclarationFragementAndStatement.forbiddenSiblings(f);
    that(x.size(), greaterThan(0));
    that(x.size(), is(1));
    final VariableDeclarationFragment b = x.get(0);
    that(b.toString(), is("b"));
    final Of of = Collect.BOTH_SEMANTIC.of(b);
    that(of, notNullValue());
    final Expression e = s.getExpression();
    that(e, notNullValue());
    that(e, is("a + b"));
    final List<SimpleName> in = of.in(e);
    that(in.size(), is(1));
    that(!in.isEmpty(), is(true));
    that(Collect.BOTH_SEMANTIC.of(f).existIn(s.getExpression(), right(a)), is(true));
    that(of.existIn(s.getExpression(), right(a)), is(true));
  }

  @RunWith(Parameterized.class)//
  public static class OutOfScope extends AbstractWringTest.OutOfScope.Declaration {
    // Use as.array for more compact initialization of 2D array.
    static String[][] cases = as.array(//
        new String[] { "Vanilla", "int a; a =3;", }, //
        new String[] { "Not empty else", "int a; if (x) a = 3; else a++;", }, //
        new String[] { "Not plain assignment", "int a = 2; if (b) a +=a+2;", }, //
        new String[] { "Uses later variable", "int a = 2,b = true; if (b) a =3;", }, //
        null);

    /**
     * Generate test cases for this parameterized class.
     *
     * @return a collection of cases, where each case is an array of three
     *         objects, the test case name, the input, and the file.
     */
    @Parameters(name = DESCRIPTION)//
    public static Collection<Object[]> cases() {
      return collect(cases);
    }
    /** Instantiates the enclosing class ({@link OutOfScope}) */
    public OutOfScope() {
      super(WRING);
    }
  }

  @SuppressWarnings({ "javadoc" })//
  @FixMethodOrder(MethodSorters.NAME_ASCENDING)//
  public class Wringed extends AbstractWringTest<VariableDeclarationFragment> {
    @SuppressWarnings("null") public Wringed() {
      super(WRING);
    }
    @Test public void newlineBug() throws MalformedTreeException, BadLocationException {
      final String from = "int a = 2;\n if (b) a =3;";
      final String expected = "int a = b ? 3 : 2;";
      final Document d = new Document(Wrap.Statement.on(from));
      final CompilationUnit u = (CompilationUnit) ast.COMPILIATION_UNIT.from(d);
      final VariableDeclarationFragment f = extract.firstVariableDeclarationFragment(u);
      that(f, notNullValue());
      final ASTRewrite r = new Trimmer().createRewrite(u, null);
      final TextEdit e = r.rewriteAST(d, null);
      that(e.getChildrenSize(), greaterThan(0));
      final UndoEdit b = e.apply(d);
      that(b, notNullValue());
      final String peeled = Wrap.Statement.off(d.get());
      if (expected.equals(peeled))
        return;
      assertThat("Nothing done on " + from, from, not(peeled));
      if (compressSpaces(peeled).equals(compressSpaces(from)))
        assertThat("Wringing of " + from + " amounts to mere reformatting", compressSpaces(peeled), not(compressSpaces(from)));
      assertSimilar(expected, peeled);
      assertSimilar(Wrap.Statement.on(expected), d);
    }
    @Test public void nonNullWring() {
      that(WRING, notNullValue());
    }
    @Test public void vanilla() throws MalformedTreeException, IllegalArgumentException {
      final String from = "int a = 2; if (b) a =3;";
      final String expected = "int a = b ? 3 : 2;";
      final Document d = new Document(Wrap.Statement.on(from));
      final CompilationUnit u = (CompilationUnit) ast.COMPILIATION_UNIT.from(d);
      final Document actual = TESTUtils.rewrite(new Trimmer(), u, d);
      final String peeled = Wrap.Statement.off(actual.get());
      if (expected.equals(peeled))
        return;
      assertThat("Nothing done on " + from, from, not(peeled));
      assertThat("Wringing of " + from + " amounts to mere reformatting", compressSpaces(peeled), not(compressSpaces(from)));
      assertSimilar(expected, peeled);
      assertSimilar(Wrap.Statement.on(expected), actual);
    }
  }
}
