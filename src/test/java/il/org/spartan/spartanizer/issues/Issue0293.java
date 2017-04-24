package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;
import il.org.spartan.spartanizer.tippers.*;

/** Unit test for the GitHub issue thus numbered. case of inlining into the
 * expression of an enhanced for
 * @author Yossi Gil
 * @since 2017-03-16 */

@SuppressWarnings({ "static-method", "javadoc" })
public class Issue0293 {
  /** Introduced by Yogi on Thu-Apr-13-00:39:17-IDT-2017 (code automatically in
   * class 'JUnitTestMethodFacotry') */
  @Test public void inta6Intb2Intca2Aeabc() {
    trimminKof("int a = 6; int b = 2; int c = a + 2; A.e(a - b + c);") //
        .using(new TwoDeclarationsIntoOne(), VariableDeclarationStatement.class) //
        .gives("int a=6,b=2;int c=a+2;A.e(a-b+c);") //
        .using(new TwoDeclarationsIntoOne(), VariableDeclarationStatement.class) //
        .gives("int a=6,b=2,c=a+2;A.e(a-b+c);") //
        .using(new LocalInitializedStatementTerminatingScope(), VariableDeclarationFragment.class) //
        .gives("int a=6,c=a+2;A.e(a-2+c);") //
        .using(new LocalInitializedStatementTerminatingScope(), VariableDeclarationFragment.class) //
        .gives("int a=6;A.e(a-2+(a+2));") //
        .using(new LocalInitializedStatementTerminatingScope(), VariableDeclarationFragment.class) //
        .gives("A.e(6-2+(6+2));") //
        .using(new InfixAdditionSubtractionExpand(), InfixExpression.class) //
        .gives("A.e(6-2+6+2);") //
        .using(new InfixAdditionEvaluate(), InfixExpression.class) //
        .gives("A.e(6-2+8);") //
        .using(new InfixSubtractionEvaluate(), InfixExpression.class) //
        .gives("A.e(4+8);") //
        .using(new InfixAdditionEvaluate(), InfixExpression.class) //
        .gives("A.e(12);") //
        .stays() //
    ;
  }
}
