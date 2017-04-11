package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsTrimmer.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.spartanizer.tippers.*;

/** Unit test for the GitHub issue thus numbered.
 * @author Yossi Gil
 * @since 2017-04-03 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SuppressWarnings("static-method")
public class Issue1218 {
  @Test public void a0() {
    trimmingOf("int a(){int $=13;return $;}") //
        .using(VariableDeclarationFragment.class, new LocalInitializedReturnExpression()) //
        .gives("int a(){return 13;}") //
    ;
  }

  @Test public void a1() {
    trimmingOf("int a(){int $=13;return $+$;}") //
        .using(VariableDeclarationFragment.class, new LocalInitializedReturnExpression()) //
        .gives("int a(){return 13+13;}") //
    ;
  }

  @Test public void a2() {
    trimmingOf("int k=1;return 0<k;")//
        .gives("return 0<1;");
  }

  /** Introduced by Yogi on Tue-Apr-11-12:14:38-IDT-2017 
  (code automatically in class 'JUnitTestMethodFacotry')*/
    @Test public void inta5b2c4Return3bac() {
       trimmingOf("int a = 5, b = 2, c = 4; return 3 * b * a * c;") //
           .using(VariableDeclarationFragment.class, new LocalInitializedReturnExpression()) //
           .gives("int b=2,c=4;return 3*b*5*c;") //
           .using(VariableDeclarationFragment.class, new LocalInitializedReturnExpression()) //
           .gives("int c=4;return 3*2*5*c;") //
           .using(VariableDeclarationFragment.class, new LocalInitializedReturnExpression()) //
           .gives("return 3*2*5*4;") //
           .using(InfixExpression.class, new InfixMultiplicationEvaluate()) //
           .gives("return 120;") //
           .stays() //
    ;
  }
  @Test public void a4() {
    trimmingOf("int a=2;return 3*a*4;")//
        .gives("return 3 * 2 * 4;");
  }

  /** Introduced by Yogi on Mon-Apr-03-21:41:40-IDT-2017 (code automatically in
   * class 'JUnitTestMethodFacotry') */
  @Test public void test_inta5b2c4Return3bac() {
    trimmingOf("int a = 5, b = 2, c = 4; return 3 * b * a * c;") //
        .gives("int b=2,c=4;return 3*b*5*c;") //
        .gives("int c=4;return 3*2*5*c;") //
        .gives("return 3*2*5*4;") //
        .gives("return 120;") //
        .stays() //
    ;
  }
}
