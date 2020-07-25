package il.org.spartan.spartanizer.engine;

import static fluent.ly.azzert.instanceOf;
import static fluent.ly.azzert.is;
import static fluent.ly.azzert.iz;
import static il.org.spartan.spartanizer.ast.navigate.step.left;
import static il.org.spartan.spartanizer.ast.navigate.step.right;

import java.util.Collection;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.VariableDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.junit.Test;

import fluent.ly.azzert;
import il.org.spartan.spartanizer.ast.factory.makeAST;
import il.org.spartan.spartanizer.ast.navigate.extract;
import il.org.spartan.spartanizer.ast.navigate.findFirst;
import il.org.spartan.spartanizer.ast.navigate.wizard;
import il.org.spartan.spartanizer.utils.WrapIntoComilationUnit;

/** TODO Yossi Gil please add a description
 * @author Yossi Gil
 * @since 2014-08-25 */
@SuppressWarnings("javadoc")
public final class OccurrencesTest {
  private final String from = "int a = 2,b; if (a+b) a =3;";
  private final String wrap = WrapIntoComilationUnit.Statement.on(from);
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
    final Collection<SimpleName> into = an.empty.list();
    a.accept(collect.lexicalUsesCollector(into, a));
    azzert.that(into.size(), is(1));
  }
  @Test public void occurencesAinAL() {
    azzert.that(collect.BOTH_SEMANTIC.of(a).in(a).size(), is(1));
  }
  @Test public void occurencesAinAsame() {
    assert wizard.eq(a, a);
  }
  @Test public void occurencesAinE() {
    azzert.that(collect.BOTH_SEMANTIC.of(a).in(e).size(), is(1));
  }
  @Test public void occurencesAinLeftOfE() {
    azzert.that(collect.BOTH_SEMANTIC.of(a).in(left(e)).size(), is(1));
  }
  @Test public void occurencesAinLeftOfEsame() {
    assert wizard.eq(left(e), a);
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
    assert wizard.eq(a, left(e));
  }
  @Test public void sameTypeAandLeftOfE() {
    azzert.that(a, instanceOf(left(e).getClass()));
  }
}
