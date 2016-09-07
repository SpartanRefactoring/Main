package il.org.spartan.refactoring.spartanizations;

import static il.org.spartan.azzert.*;
import static il.org.spartan.refactoring.engine.into.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.*;
import il.org.spartan.refactoring.ast.*;
import il.org.spartan.refactoring.engine.*;

@SuppressWarnings({ "javadoc", "static-method" }) //
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
public class SingletStatementTest {
  @Test public void declarationAndStatementIsNull() {
    azzert.isNull(extract.singleStatement(s("{int a; a();}")));
  }

  @Test public void deeplyNestedOneInCurlyIsNull() {
    assert null != extract.singleStatement(s("{{{{a();}}}}"));
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

  @Test public void oneInCurlyIsNotNull() {
    assert null != extract.singleStatement(s("{a();}"));
  }

  @Test public void oneIsNotNull() {
    assert null != extract.singleStatement(s("{a();}"));
  }

  @Test public void peelIf() {
    final ASTNode n = makeAST.STATEMENTS.from("{if (a) return b; else return c;}");
    assert null != n;
    final List<Statement> ss = extract.statements(n);
    assert null != ss;
    azzert.that(ss.size(), is(1));
    assert null != extract.singleStatement(n);
  }

  @Test public void peelIPlusPlus() {
    final ASTNode n = makeAST.STATEMENTS.from("{i++;}");
    assert null != n;
    final List<Statement> ss = extract.statements(n);
    assert null != ss;
    azzert.that(ss.size(), is(1));
    assert null != extract.singleStatement(n);
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
