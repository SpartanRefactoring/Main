package il.org.spartan.spartanizer.engine;

import static il.org.spartan.azzert.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.utils.*;

/** TODO: Yossi Gil please add a description
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @since 2014-08-25 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SuppressWarnings("javadoc")
public final class OccurrencesTest {
  private final String from = "int a = 2,b; if (a+b) a =3;";
  private final String wrap = Wrap.Statement.on(from);
  private final CompilationUnit u = (CompilationUnit) makeAST.COMPILATION_UNIT.from(wrap);
  private final SimpleName a = findFirst.variableDeclarationFragment(u).getName();
  private final VariableDeclarationStatement ab = (VariableDeclarationStatement) a.getParent().getParent();
  private final SimpleName b = ((VariableDeclaration) ab.fragments().get(1)).getName();
  private final IfStatement s = extract.nextIfStatement(a);
  private final InfixExpression e = (InfixExpression) s.getExpression();

  @Test public void correctSettings() {
    azzert.that(ab, iz("int a=2,b;"));
    azzert.that(b + "", is("b"));
    azzert.that(s, is(findFirst.ifStatement(u)));
    azzert.that(s, iz("if (a + b) a=3;"));
    azzert.that(e, iz("a + b"));
  }

  @Test public void exploreLeftOfE() {
    azzert.that(left(e), iz("a"));
  }

  @Test public void lexicalUsesCollector() {
    final Collection<SimpleName> into = new ArrayList<>();
    a.accept(collect.lexicalUsesCollector(into, a));
    azzert.that(into.size(), is(1));
  }

  @Test public void occurencesAinAL() {
    azzert.that(collect.BOTH_SEMANTIC.of(a).in(a).size(), is(1));
  }

  @Test public void occurencesAinAsame() {
    assert wizard.same(a, a);
  }

  @Test public void occurencesAinE() {
    azzert.that(collect.BOTH_SEMANTIC.of(a).in(e).size(), is(1));
  }

  @Test public void occurencesAinLeftOfE() {
    azzert.that(collect.BOTH_SEMANTIC.of(a).in(left(e)).size(), is(1));
  }

  @Test public void occurencesAinLeftOfEsame() {
    assert wizard.same(left(e), a);
  }

  @Test public void occurencesAinRightOfE() {
    azzert.that(collect.BOTH_SEMANTIC.of(a).in(right(e)).size(), is(0));
  }

  @Test public void occurencesBinE() {
    azzert.that(collect.BOTH_SEMANTIC.of(b).in(e).size(), is(1));
  }

  @Test public void occurencesBinRightOfE() {
    azzert.that(collect.BOTH_SEMANTIC.of(b).in(right(e)).size(), is(1));
  }

  @Test public void sameAandLeftOfE() {
    assert wizard.same(a, left(e));
  }

  @Test public void sameTypeAandLeftOfE() {
    azzert.that(a, instanceOf(left(e).getClass()));
  }
}
