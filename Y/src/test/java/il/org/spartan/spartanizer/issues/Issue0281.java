package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import org.junit.*;

/** Checking that a bug when converting while to for was fixed
 * @author Dor Ma'ayan
 * @since 30-11-2016 */
@SuppressWarnings("static-method")
public class Issue0281 {
  @Test public void test0() {
    trimmingOf("static Statement recursiveElze(final IfStatement ¢) {Statement $ = ¢.getElseStatement();while ($ instanceof IfStatement)"
        + "$ = ((IfStatement) $).getElseStatement();return $;}")//
            .stays();
  }

  @Test public void test2() {
    trimmingOf("int a=0;while(a!=5){q=6+9;q--;a+=8;}a=3;")//
        .gives("int a=0;for(;a!=5;a+=8){q=6+9;q--;}a=3;");
  }

  @Test public void test3() {
    trimmingOf("int a=0;while(a!=5){q=6+9;q--;a+=8;}z+=8;a=3;")//
        .gives("int a=0;for(;a!=5;a+=8){q=6+9;q--;}z+=8;a=3;");
  }
}
