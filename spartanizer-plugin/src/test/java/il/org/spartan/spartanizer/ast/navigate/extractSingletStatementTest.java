/* TODO Yossi Gil Document Class
 *
 * @author Yossi Gil
 *
 * @since Nov 26, 2016 */
package il.org.spartan.spartanizer.ast.navigate;

import static fluent.ly.azzert.is;
import static il.org.spartan.spartanizer.engine.parse.s;

import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Statement;
import org.junit.Test;

import fluent.ly.azzert;
import il.org.spartan.spartanizer.ast.factory.makeAST;

@SuppressWarnings({ "javadoc", "static-method" })
public final class extractSingletStatementTest {
  @Test public void declarationAndStatementIsNull() {
    azzert.isNull(extract.singleStatement(s("{int a; a();}")));
  }
  @Test public void deeplyNestedOneInCurlyIsNull() {
    assert extract.singleStatement(s("{{{{a();}}}}")) != null;
  }
  @Test public void emptyBlockIsNull() {
    azzert.isNull(extract.singleStatement(s("{}")));
  }
  @Test public void emptyStatementInBlockIsNull() {
    azzert.isNull(extract.singleStatement(s("{;}")));
  }
  @Test public void emptyStatementIsNull() {
    azzert.isNull(extract.singleStatement(s(";")));
  }
  @Test public void fiveIsCorrectSize() {
    azzert.isNull(extract.singleStatement(s("{{a();b();}{a(); b(); {}{}{{}} c();}}")));
  }
  @Test public void manyEmptyStatementInBlockIsNull() {
    azzert.isNull(extract.singleStatement(s("{;};{;;{;;}};")));
  }
  @Test public void manyIsNull() {
    azzert.isNull(extract.singleStatement(s("a(); b(); c();")));
  }
  @Test public void nestedTwoIsCorrectSize() {
    azzert.isNull(extract.singleStatement(s("{a();b();}")));
  }
  @Test public void nullGivesNull() {
    azzert.isNull(extract.singleStatement(null));
  }
  @Test public void oneInCurlyIsNonNull() {
    assert extract.singleStatement(s("{a();}")) != null;
  }
  @Test public void oneIsNonNull() {
    assert extract.singleStatement(s("{a();}")) != null;
  }
  @Test public void peelIf() {
    final ASTNode n = makeAST.STATEMENTS.from("{if (a) return b; else return c;}");
    assert n != null;
    final List<Statement> ss = extract.statements(n);
    assert ss != null;
    azzert.that(ss.size(), is(1));
    assert extract.singleStatement(n) != null;
  }
  @Test public void peelIPlusPlus() {
    final ASTNode n = makeAST.STATEMENTS.from("{i++;}");
    assert n != null;
    final List<Statement> ss = extract.statements(n);
    assert ss != null;
    azzert.that(ss.size(), is(1));
    assert extract.singleStatement(n) != null;
  }
  @Test public void twoFunctionCallsNullValue() {
    azzert.isNull(extract.singleStatement(s("{b(); a();}")));
  }
  @Test public void twoInCurlyIsNull() {
    azzert.isNull(extract.singleStatement(s("{a();b();}")));
  }
  @Test public void twoNullValue() {
    azzert.isNull(extract.singleStatement(s("a();b();")));
  }
}
