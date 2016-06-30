package il.org.spartan.refactoring.utils;

import static il.org.spartan.hamcrest.SpartanAssert.*;
import static il.org.spartan.refactoring.utils.Funcs.*;
import static org.junit.Assert.*;
import il.org.spartan.refactoring.spartanizations.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;
import org.junit.runners.*;

/**
 * @author Yossi Gil
 * @since 2014-08-25
 */
@SuppressWarnings({ "javadoc" })//
@FixMethodOrder(MethodSorters.NAME_ASCENDING)//
public class OccurrencesTest {
  private final String from = "int a = 2,b; if (a+b) a =3;";
  private final String wrap = Wrap.Statement.on(from);
  private final CompilationUnit u = (CompilationUnit) As.COMPILIATION_UNIT.ast(wrap);
  private final SimpleName a = Extract.firstVariableDeclarationFragment(u).getName();
  private final VariableDeclarationStatement ab = (VariableDeclarationStatement) a.getParent().getParent();
  private final SimpleName b = ((VariableDeclarationFragment) ab.fragments().get(1)).getName();
  private final IfStatement s = Extract.nextIfStatement(a);
  private final InfixExpression e = (InfixExpression) s.getExpression();

  @Test public void correctSettings() {
    assertThat(ab, iz("int a=2,b;"));
    assertThat(b.toString(), is("b"));
    assertThat(s, is(Extract.firstIfStatement(u)));
    assertThat(s, iz("if (a + b) a=3;"));
    assertThat(e, iz("a + b"));
  }
  @Test public void exploreLeftOfE() {
    assertThat(left(e), iz("a"));
  }
  @Test public void lexicalUsesCollector() {
    final List<SimpleName> into = new ArrayList<>();
    final ASTVisitor collector = Collect.lexicalUsesCollector(into, a);
    a.accept(collector);
    assertThat(into.size(), is(1));
  }
  @Test public void occurencesAinAL() {
    assertThat(Collect.BOTH_SEMANTIC.of(a).in(a).size(), is(1));
  }
  @Test public void occurencesAinAsame() {
    assertThat(same(a, a), is(true));
  }
  @Test public void occurencesAinE() {
    assertThat(Collect.BOTH_SEMANTIC.of(a).in(e).size(), is(1));
  }
  @Test public void occurencesAinLeftOfE() {
    assertThat(Collect.BOTH_SEMANTIC.of(a).in(left(e)).size(), is(1));
  }
  @Test public void occurencesAinLeftOfEsame() {
    assertThat(same(left(e), a), is(true));
  }
  @Test public void occurencesAinRightOfE() {
    assertThat(Collect.BOTH_SEMANTIC.of(a).in(right(e)).size(), is(0));
  }
  @Test public void occurencesBinE() {
    assertThat(Collect.BOTH_SEMANTIC.of(b).in(e).size(), is(1));
  }
  @Test public void occurencesBinRightOfE() {
    assertThat(Collect.BOTH_SEMANTIC.of(b).in(right(e)).size(), is(1));
  }
  @Test public void sameAandLeftOfE() {
    assertThat(same(a, left(e)), is(true));
  }
  @Test public void sameTypeAandLeftOfE() {
    assertThat(a, instanceOf(left(e).getClass()));
  }
}
