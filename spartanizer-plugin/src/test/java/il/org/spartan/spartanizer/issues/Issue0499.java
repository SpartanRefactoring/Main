package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.trimmingOf;

import org.eclipse.jdt.core.dom.LambdaExpression;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.junit.Test;

import il.org.spartan.spartanizer.tippers.LambdaRenameSingleParameterToLambda;
import il.org.spartan.spartanizer.tippers.ParameterAnonymize;

/** Checking that a bug with centification in Lambda Expression was fixed
 * @author Dor Ma'ayan
 * @since 17-11-2016 */
@SuppressWarnings("static-method")
public class Issue0499 {
  @Test public void test0() {
    trimmingOf("public S d(final G a) {assert a != null;r(¢ -> Integer.valueOf(a.apply(¢, selection())));return this;}")
        .gives("public S d(final G a) {assert a != null;r(λ -> Integer.valueOf(a.apply(λ, selection())));return this;}")//
        .stays();
  }
  /** Introduced by Yossi on Thu-Mar-23-00:43:14-IST-2017 (code automatically
   * generated in 'il.org.spartan.spartanizer.cmdline.anonymize.java') */
  @Test public void test_publicAaFinalBbAssertaNullrxIntegervalueOfaapplyxselectionReturnThis() {
    trimmingOf("public A a(final B b){assert a!=null;r(x->Integer.valueOf(a.apply(x,selection())));return this;}") //
        .using(new ParameterAnonymize(), SingleVariableDeclaration.class) //
        .gives("public A a(final B __){assert a!=null;r(x->Integer.valueOf(a.apply(x,selection())));return this;}") //
        .using(new LambdaRenameSingleParameterToLambda(), LambdaExpression.class) //
        .gives("public A a(final B __){assert a!=null;r(λ->Integer.valueOf(a.apply(λ,selection())));return this;}") //
        .stays() //
    ;
  }
  @Test public void test1() {
    trimmingOf("public S d(final G a) {assert a != null;r(x -> Integer.valueOf(a.apply(x, selection())));return this;}")//
        .gives("public S d(final G a){assert a!=null;r(λ->Integer.valueOf(a.apply(λ,selection())));return this;}") //
        .stays();
  }
  /** Introduced by Yossi on Thu-Mar-23-00:44:12-IST-2017 (code automatically
   * generated in 'il.org.spartan.spartanizer.cmdline.anonymize.java') */
  @Test public void publicAaFinalBbAssertaNullrxIntegervalueOfaapplyxselectionReturnThis() {
    trimmingOf("public A a(final B b){assert a!=null;r(x->Integer.valueOf(a.apply(x,selection())));return this;}") //
        .using(new ParameterAnonymize(), SingleVariableDeclaration.class) //
        .gives("public A a(final B __){assert a!=null;r(x->Integer.valueOf(a.apply(x,selection())));return this;}") //
        .using(new LambdaRenameSingleParameterToLambda(), LambdaExpression.class) //
        .gives("public A a(final B __){assert a!=null;r(λ->Integer.valueOf(a.apply(λ,selection())));return this;}") //
        .stays() //
    ;
  }
  /** Introduced by Yossi on Thu-Mar-23-00:44:54-IST-2017 (code automatically
   * generated in 'il.org.spartan.spartanizer.cmdline.anonymize.java') */
  @Test public void ublicAaFinalBbAssertaNullrxIntegervalueOfaapplyxselectionReturnThis() {
    trimmingOf("public A a(final B b){assert a!=null;r(x->Integer.valueOf(a.apply(x,selection())));return this;}") //
        .using(new ParameterAnonymize(), SingleVariableDeclaration.class) //
        .gives("public A a(final B __){assert a!=null;r(x->Integer.valueOf(a.apply(x,selection())));return this;}") //
        .using(new LambdaRenameSingleParameterToLambda(), LambdaExpression.class) //
        .gives("public A a(final B __){assert a!=null;r(λ->Integer.valueOf(a.apply(λ,selection())));return this;}") //
        .stays() //
    ;
  }
  @Test public void test2() {
    trimmingOf("public S d(final G a) {assert a != null;r(x -> Integer.valueOf(a.apply(x, selection())));return this;}")
        .gives("public S d(final G a) {assert a != null;r(λ -> Integer.valueOf(a.apply(λ, selection())));return this;}")//
        .stays();
  }
}
