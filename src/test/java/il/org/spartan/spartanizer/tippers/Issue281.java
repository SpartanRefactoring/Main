package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.junit.*;

/** @author Dor Ma'ayan
 * @since 30-11-2016 */
@SuppressWarnings("static-method")
@Ignore
public class Issue281 {
  @Test public void test0() {
    trimmingOf("static Statement recursiveElze(final IfStatement ¢) {" + "Statement $ = ¢.getElseStatement();" + "while ($ instanceof IfStatement)"
        + "$ = ((IfStatement) $).getElseStatement();" + "return $;" + "}")
            .gives("  static Statement recursiveElze(final IfStatement ¢) {"
                + "for (Statement $ = ¢.getElseStatement();; $ = ((IfStatement) $).getElseStatement())" + "if (!($ instanceof IfStatement))"
                + "return $;}");
  }

  @Test public void test1() {
    trimmingOf("int a=0;while(a!=5){q=6+9;q--;a+=8;}").gives("for(int a=0;a!=5;a+=8){q=6+9;q--;}");
  }

  @Test public void test2() {
    trimmingOf("int a=0;while(a!=5){q=6+9;q--;a+=8;}a=3;").gives("int a=0;for(;a!=5;a+=8){q=6+9;q--;}a=3;");
  }

  @Test public void test3() {
    trimmingOf("int a=0;while(a!=5){q=6+9;q--;a+=8;}z+=8;a=3;").gives("int a=0;for(;a!=5;a+=8){q=6+9;q--;}z+=8;a=3;");
  }
}
