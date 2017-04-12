package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsTrimmer.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.spartanizer.tippers.*;

/** Unit test for {@link ForNoUpdatersNoInitializerToWhile}
 * @author Yossi Gil
 * @since 2017-03-24 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "static-method", "javadoc" }) //
public class Issue1123 {
  @Test public void a() {
    trimminKof("for (; i.length() <s.length();)i = \" \" + i;")//
        .gives("while ( i.length() <s.length())i = \" \" + i;")//
        .stays();
  }

  @Test public void b() {
    trimminKof("c(N i){N p=i;int a=5;f(++a);for(;p<10;)p=p.e();return false;}")//
        .gives("c(N i){N p=i;int a=5;f(++a);while(p<10)p=p.e();return false;}")//
        .stays();
  }

  /** Introduced by Yossi on Sat-Mar-25-00:03:25-IDT-2017 (code automatically in
   * class 'TestCaseFacotry') */
  @Test public void test_aaAbIntc0d1WhilecdIfc0c7cReturnc() {
    trimminKof("A a(A b) { int c = 0, d = 1; while (c < d) { if (c == 0) c = 7; ++c; } return c; }") //
        .using(new MethodDeclarationRenameReturnToDollar(), MethodDeclaration.class) //
        .gives("A a(A b){int $=0,d=1;while($<d){if($==0)$=7;++$;}return $;}") //
        .using(new WhileToForUpdaters(), WhileStatement.class) //
        .gives("A a(A b){int $=0,d=1;for(;$<d;++$){if($==0)$=7;}return $;}") //
        .using(new BlockSingleton(), Block.class) //
        .gives("A a(A b){int $=0,d=1;for(;$<d;++$)if($==0)$=7;return $;}") //
        .stays() //
    ;
  }
}
