package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsTrimmer.*;

import org.junit.*;

/** Test Class for PlusAsignToPostfix
 * @author dormaayn <tt>dor.d.ma@gmail.com</tt>
 * @since 2017-03-25 */
@SuppressWarnings("static-method")
public class Issue1138 {
  @Test public void test0() {
    trimmingOf("int counter=0;while(j<9)counter+=1;printf(counter);")//
        .gives("int counter=0;while(j<9)counter++;printf(counter);");
  }

  @Test public void test1() {
    trimmingOf("String counter=0;while(j<9)counter+=1;printf(counter);")//
        .stays();
  }

  @Test public void test2() {
    trimmingOf("long counter=0;while(j<9)counter+=1;printf(counter);")//
        .gives("long counter=0;while(j<9)counter++;printf(counter);");
  }

  @Test public void test3() {
    trimmingOf("String d=\"sdf\";long counter=0;while(j<9)counter+=1;printf(counter+d);")//
        .gives("String d=\"sdf\";long counter=0;while(j<9)counter++;printf(counter+d);");
  }
}
