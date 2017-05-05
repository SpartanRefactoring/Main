package il.org.spartan.spartanizer.issues;

import static fluent.ly.azzert.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.utils.tdd.*;

/** Test class for tdd.enumerate.methods (see issue #679)
 * @author Sharon Kuninin
 * @author Yarden Lev
 * @since Nov 2, 2016 */
@SuppressWarnings("static-method")
public class Issue0679 {
  public static CompilationUnit cu(final String program) {
    final ASTParser $ = wizard.parser(ASTParser.K_COMPILATION_UNIT);
    $.setSource(program.toCharArray());
    return (CompilationUnit) $.createAST(null);
  }
  @Test public void checkExistence() {
    enumerate.methods(null);
  }
  @Test public void checkParameterType() {
    enumerate.methods(null);
  }
  @Test public void checkReturnType() {
    enumerate.methods(null);
  }
  @Test public void methodInOutterClassAndMethodInInnerClass() {
    azzert.that(enumerate.methods(cu("class A { void a() {} class B { void b() {} } }")), is(2));
  }
  @Test public void methodsInsideInnerClass() {
    azzert.that(enumerate.methods(cu("class A { class B { void a() {} } }")), is(1));
  }
  @Test public void methodsWithBody() {
    azzert.that(enumerate.methods(cu("class A { int a1() {return 1;} int a2() {return 2;} int a3() {return 3;} }")), is(3));
  }
  @Test public void methodsWithParameters() {
    azzert.that(enumerate.methods(cu("class A { int a1(int n) {return n;} int a2(int m) {return (m+1);} }")), is(2));
  }
  @Test public void noMethodsInCompilationUnit() {
    azzert.that(enumerate.methods(cu("1111")), is(0));
  }
  @Test public void oneMethodInCompilationUnit() {
    azzert.that(enumerate.methods(cu("class A { void a() {} }")), is(1));
  }
  @Test public void twoMethodsInCompilationUnit() {
    azzert.that(enumerate.methods(cu("class A { void a1() {} void a2() {} }")), is(2));
  }
  @Test public void zeroReturnedWhenNullIsSent() {
    azzert.that(enumerate.methods(null), is(0));
  }
}
