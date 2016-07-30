package il.org.spartan.refactoring.spartanizations;

import static il.org.spartan.azzert.*;
import static il.org.spartan.refactoring.utils.Into.*;
import static il.org.spartan.refactoring.utils.Restructure.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.*;
import il.org.spartan.refactoring.utils.*;

@SuppressWarnings({ "javadoc", "static-method" }) //
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
public class SingletStatementTest {
  @Test public void declarationAndStatementIsNull() {
    azzert.isNull(singleStatement(s("{int a; a();}")));
  }
  @Test public void deeplyNestedOneInCurlyIsNull() {
    azzert.notNull(singleStatement(s("{{{{a();}}}}")));
  }
  @Test public void emptyBlockIsNull() {
    azzert.isNull(singleStatement(s("{}")));
  }
  @Test public void emptyStatementInBlockIsNull() {
    azzert.isNull(singleStatement(s("{;}")));
  }
  @Test public void emptyStatementIsNull() {
    azzert.isNull(singleStatement(s(";")));
  }
  @Test public void fiveIsCorrectSize() {
    azzert.isNull(singleStatement(s("{{a();b();}{a(); b(); {}{}{{}} c();}}")));
  }
  @Test public void manyEmptyStatementInBlockIsNull() {
    azzert.isNull(singleStatement(s("{;};{;;{;;}};")));
  }
  @Test public void manyIsNull() {
    azzert.isNull(singleStatement(s("a(); b(); c();")));
  }
  @Test public void nestedTwoIsCorrectSize() {
    azzert.isNull(singleStatement(s("{a();b();}")));
  }
  @Test public void nullGivesNull() {
    azzert.isNull(singleStatement(null));
  }
  @Test public void oneInCurlyIsNotNull() {
    azzert.notNull(singleStatement(s("{a();}")));
  }
  @Test public void oneIsNotNull() {
    azzert.notNull(singleStatement(s("{a();}")));
  }
  @Test public void peelIf() {
    final ASTNode n = MakeAST.STATEMENTS.from("{if (a) return b; else return c;}");
    azzert.notNull(n);
    final List<Statement> ss = extract.statements(n);
    azzert.notNull(ss);
    azzert.that(ss.size(), is(1));
     azzert.notNull(extract.singleStatement(n));
  }
  @Test public void peelIPlusPlus() {
    final ASTNode n = MakeAST.STATEMENTS.from("{i++;}");
    azzert.notNull(n);
    final List<Statement> ss = extract.statements(n);
    azzert.notNull(ss);
    azzert.that(ss.size(), is(1));
     azzert.notNull(extract.singleStatement(n));
  }
  @Test public void twoFunctionCallsNullValue() {
    azzert.isNull(singleStatement(s("{b(); a();}")));
  }
  @Test public void twoInCurlyIsNull() {
    azzert.isNull(singleStatement(s("{a();b();}")));
  }
  @Test public void twoNullValue() {
    azzert.isNull(singleStatement(s("a();b();")));
  }
}
