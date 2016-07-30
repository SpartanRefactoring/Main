package il.org.spartan.refactoring.wring;

import static il.org.spartan.azzert.*;
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
import il.org.spartan.refactoring.utils.Collect.*;
import il.org.spartan.refactoring.wring.AbstractWringTest.*;
import il.org.spartan.refactoring.wring.Wring.*;
import il.org.spartan.utils.Utils;

/**
 * @author Yossi Gil
 * @since 2014-07-13
 */
@SuppressWarnings({ "javadoc", "static-method" }) //
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
public class DeclarationIfAssginmentTest {
  static final DeclarationInitializerIfAssignment WRING = new DeclarationInitializerIfAssignment();
  static <T> void assertNotEquals(String s, T t1, T t2) {
    azzert.that(s, t2, is(not(t1))); 
   }
  @Test public void traceForbiddenSiblings() {
     azzert.notNull(WRING);
    final String from = "int a = 2,b; if (b) a =3;";
    final String wrap = Wrap.Statement.on(from);
    final CompilationUnit u = (CompilationUnit) MakeAST.COMPILATION_UNIT.from(wrap);
    final VariableDeclarationFragment f = extract.firstVariableDeclarationFragment(u);
    azzert.notNull(f);
    azzert.that(WRING.scopeIncludes(f), is(false));
  }
  @Test public void traceForbiddenSiblingsExpanded() {
    final String from = "int a = 2,b; if (a+b) a =3;";
    final String wrap = Wrap.Statement.on(from);
    final CompilationUnit u = (CompilationUnit) MakeAST.COMPILATION_UNIT.from(wrap);
    final VariableDeclarationFragment f = extract.firstVariableDeclarationFragment(u);
    azzert.notNull(f);
    final Expression initializer = f.getInitializer();
     azzert.notNull(initializer);
    final IfStatement s = extract.nextIfStatement(f);
    azzert.that(s, is(extract.firstIfStatement(u)));
     azzert.notNull(s);
    azzert.that(s, iz("if (a + b) a=3;"));
     azzert.aye(Is.vacuousElse(s));
    final Assignment a = extract.assignment(then(s));
     azzert.notNull(a);
     azzert.aye(same(left(a), f.getName()));
    azzert.that(a.getOperator(), is(Assignment.Operator.ASSIGN));
    final List<VariableDeclarationFragment> x = VariableDeclarationFragementAndStatement.forbiddenSiblings(f);
    azzert.that(x.size(), greaterThan(0));
    azzert.that(x.size(), is(1));
    final VariableDeclarationFragment b = x.get(0);
    azzert.that(b.toString(), is("b"));
    final Of of = Collect.BOTH_SEMANTIC.of(b);
     azzert.notNull(of);
    final Expression e = s.getExpression();
     azzert.notNull(e);
    azzert.that(e, iz("a + b"));
    final List<SimpleName> in = of.in(e);
    azzert.that(in.size(), is(1));
    azzert.that(!in.isEmpty(), is(true));
    azzert.that(Collect.BOTH_SEMANTIC.of(f).existIn(s.getExpression(), right(a)), is(true));
    azzert.that(of.existIn(s.getExpression(), right(a)), is(true));
  }

  @RunWith(Parameterized.class) //
  public static class OutOfScope extends AbstractWringTest.OutOfScope.Declaration {
    static String[][] cases = Utils.asArray(//
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
    @Parameters(name = DESCRIPTION) //
    public static Collection<Object[]> cases() {
      return collect(cases);
    }
    /** Instantiates the enclosing class ({@link OutOfScope}) */
    public OutOfScope() {
      super(WRING);
    }
  }

  @SuppressWarnings({ "javadoc" }) //
  @FixMethodOrder(MethodSorters.NAME_ASCENDING) //
  public class Wringed extends AbstractWringTest<VariableDeclarationFragment> {
    public Wringed() {
      super(WRING);
    }
    @Test public void newlineBug() throws MalformedTreeException, BadLocationException {
      final String from = "int a = 2;\n if (b) a =3;";
      final String expected = "int a = b ? 3 : 2;";
      final Document d = new Document(Wrap.Statement.on(from));
      final CompilationUnit u = (CompilationUnit) MakeAST.COMPILATION_UNIT.from(d);
      final VariableDeclarationFragment f = extract.firstVariableDeclarationFragment(u);
      azzert.notNull(f);
      final ASTRewrite r = new Trimmer().createRewrite(u, null);
      final TextEdit e = r.rewriteAST(d, null);
      azzert.that(e.getChildrenSize(), greaterThan(0));
      final UndoEdit b = e.apply(d);
      azzert.notNull(b);
      final String peeled = Wrap.Statement.off(d.get());
      if (expected.equals(peeled))
        return;
      if (from.equals(peeled))
        fail("Nothing done on " + from);
      if (compressSpaces(peeled).equals(compressSpaces(from)))
        azzert.that("Wringing of " + from + " amounts to mere reformatting", compressSpaces(from), is(not(compressSpaces(peeled))));
      assertSimilar(expected, peeled);
      assertSimilar(Wrap.Statement.on(expected), d);
    }

    @Test public void nonNullWring() {
       azzert.notNull(WRING);
    }
    @Test public void vanilla() throws MalformedTreeException, IllegalArgumentException {
      final String from = "int a = 2; if (b) a =3;";
      final String expected = "int a = b ? 3 : 2;";
      final Document d = new Document(Wrap.Statement.on(from));
      final CompilationUnit u = (CompilationUnit) MakeAST.COMPILATION_UNIT.from(d);
      final Document actual = TESTUtils.rewrite(new Trimmer(), u, d);
      final String peeled = Wrap.Statement.off(actual.get());
      if (expected.equals(peeled))
        return;
      if (from.equals(peeled))
        fail("Nothing done on " + from);
      if (compressSpaces(peeled).equals(compressSpaces(from)))
        azzert.that("Wringing of " + from + " amounts to mere reformatting", compressSpaces(from), is(not(compressSpaces(peeled))));
      assertSimilar(expected, peeled);
      assertSimilar(Wrap.Statement.on(expected), actual);
    }
  }
}
