package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.utils.tdd.*;

/** Test class for tdd.enumerate.methods (see issue #679)
 * @author Sharon Kuninin
 * @author Yarden Lev
 * @since Nov 2, 2016 */
@SuppressWarnings("static-method")
public class Issue679 {
  public static CompilationUnit cu(final String program) {
    final ASTParser parser = wizard.parser(ASTParser.K_COMPILATION_UNIT);
    parser.setSource(program.toCharArray());
    return (CompilationUnit) parser.createAST(null);
  }

  @Test public void checkExistence() {
    enumerate.methods((CompilationUnit) null);
  }

  @Test public void checkReturnType() {
    enumerate.methods((CompilationUnit) null);
  }

  @Test public void checkParameterType() {
    enumerate.methods((CompilationUnit) null);
  }

  @Test public void noMethodsInCompilationUnit() {
    Assert.assertEquals(0, enumerate.methods(cu("1111")));
  }

  @Test public void oneMethodInCompilationUnit() {
    Assert.assertEquals(1, enumerate.methods(cu("class A { void a() {} }")));
  }

  @Test public void zeroReturnedWhenNullIsSent() {
    Assert.assertEquals(0, enumerate.methods(null));
  }

  @Test public void twoMethodsInCompilationUnit() {
    Assert.assertEquals(2, enumerate.methods(cu("class A { void a1() {} void a2() {} }")));
  }

  @Test public void methodsInsideInnerClass() {
    Assert.assertEquals(1, enumerate.methods(cu("class A { class B { void a() {} } }")));
  }

  @Test public void methodInOutterClassAndMethodInInnerClass() {
    Assert.assertEquals(2, enumerate.methods(cu("class A { void a() {} class B { void b() {} } }")));
  }

  @Test public void methodsWithBody() {
    Assert.assertEquals(3, enumerate.methods(cu("class A { int a1() {return 1;} int a2() {return 2;} int a3() {return 3;} }")));
  }

  @Test public void methodsWithParameters() {
    Assert.assertEquals(2, enumerate.methods(cu("class A { int a1(int n) {return n;} int a2(int m) {return (m+1);} }")));
  }
}
