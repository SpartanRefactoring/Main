package il.org.spartan.refactoring.engine;

import static il.org.spartan.azzert.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.*;
import il.org.spartan.refactoring.ast.*;
import il.org.spartan.refactoring.spartanizations.*;

/** @author Yossi Gil
 * @since 2014-08-25 */
@SuppressWarnings({ "javadoc" }) //
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
public class OccurrencesTest {
  private final String from = "int a = 2,b; if (a+b) a =3;";
  private final String wrap = Wrap.Statement.on(from);
  private final CompilationUnit u = (CompilationUnit) makeAST.COMPILATION_UNIT.from(wrap);
  private final SimpleName a = extract.firstVariableDeclarationFragment(u).getName();
  private final VariableDeclarationStatement ab = (VariableDeclarationStatement) a.getParent().getParent();
  private final SimpleName b = ((VariableDeclarationFragment) ab.fragments().get(1)).getName();
  private final IfStatement s = extract.nextIfStatement(a);
  private final InfixExpression e = (InfixExpression) s.getExpression();

  @Test public void correctSettings() {
    azzert.that(ab, iz("int a=2,b;"));
    azzert.that("" + b, is("b"));
    azzert.that(s, is(extract.firstIfStatement(u)));
    azzert.that(s, iz("if (a + b) a=3;"));
    azzert.that(e, iz("a + b"));
  }

  @Test public void exploreLeftOfE() {
    azzert.that(step.left(e), iz("a"));
  }

  @Test public void lexicalUsesCollector() {
    final List<SimpleName> into = new ArrayList<>();
    final ASTVisitor collector = Collect.lexicalUsesCollector(into, a);
    a.accept(collector);
    azzert.that(into.size(), is(1));
  }

  @Test public void occurencesAinAL() {
    azzert.that(Collect.BOTH_SEMANTIC.of(a).in(a).size(), is(1));
  }

  @Test public void occurencesAinAsame() {
    azzert.that(wizard.same(a, a), is(true));
  }

  @Test public void occurencesAinE() {
    azzert.that(Collect.BOTH_SEMANTIC.of(a).in(e).size(), is(1));
  }

  @Test public void occurencesAinLeftOfE() {
    azzert.that(Collect.BOTH_SEMANTIC.of(a).in(step.left(e)).size(), is(1));
  }

  @Test public void occurencesAinLeftOfEsame() {
    azzert.that(wizard.same(step.left(e), a), is(true));
  }

  @Test public void occurencesAinRightOfE() {
    azzert.that(Collect.BOTH_SEMANTIC.of(a).in(step.right(e)).size(), is(0));
  }

  @Test public void occurencesBinE() {
    azzert.that(Collect.BOTH_SEMANTIC.of(b).in(e).size(), is(1));
  }

  @Test public void occurencesBinRightOfE() {
    azzert.that(Collect.BOTH_SEMANTIC.of(b).in(step.right(e)).size(), is(1));
  }

  @Test public void sameAandLeftOfE() {
    azzert.that(wizard.same(a, step.left(e)), is(true));
  }

  @Test public void sameTypeAandLeftOfE() {
    azzert.that(a, instanceOf(step.left(e).getClass()));
  }
}
