package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import org.junit.*;

/** Unit test for the GitHub issue thus numbered. case of inlining into the
 * expression of an enhanced for
 * @author Yossi Gil
 * @since 2017-03-16 */
@SuppressWarnings({ "static-method", "javadoc" })
public class Issue0054 {
  @Test public void forPlainUseInUpdaters() {
    trimmingOf("int a = f(); for (int i = 0; i <100; i *= a) b[i] = 3;")//
        .gives("int a=f();for(int ¢=0;¢<100;¢*=a)b[¢]=3;") //
        .gives("for(int a=f(),¢=0;¢<100;¢*=a)b[¢]=3;") //
        .stays();
  }

  @Test public void forPlainUseInCondition() {
    trimmingOf("int a = f(); for (int i = 0; a <100; ++i) b[i] = 3;")//
        .gives("int a=f();for(int ¢=0;a<100;++¢)b[¢]=3;") //
        .gives("for(int a=f(),¢=0;a<100;++¢)b[¢]=3;") //
        .stays();
  }
}
