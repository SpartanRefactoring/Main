/** TODO: Yossi Gil <yossi.gil@gmail.com> please add a description
 * @author Yossi Gil <yossi.gil@gmail.com>
 * @since Jan 6, 2017 */
package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.junit.*;

@SuppressWarnings("static-method")
public final class Issue0121 {
  @Test public void test01() {
    trimmingOf("class A{ \n" //
        + "class B{} \n" //
        + "B f(int t){ \n" //
        + "return new B(){ \n" //
        + "public int g(){ \n" //
        + "return t; \n" //
        + "} \n" //
        + "}; \n" //
        + "} \n" //
        + "}")
            .gives("class A{ \n" //
                + "class B{} \n" //
                + "B f(int ¢){ \n" //
                + "return new B(){ \n" //
                + "public int g(){ \n" //
                + "return ¢; \n" //
                + "} \n" //
                + "}; \n" //
                + "} \n" //
                + "}");
  }

  // That is a true renaming bug, and a true Environment issue.
  @Ignore public void test02() {
    trimmingOf("class A{"//
        + "class B{"//
        + "int t;"//
        + "B(int t){"//
        + "this.t = t;"//
        + "}"//
        + "}"//
        + "B f(int t){"//
        + "return new B(t){"//
        + "int omigod(){"//
        + "return t*t;"//
        + "}"//
        + "};"//
        + "}"//
        + "}")
            .gives("class A{"//
                + "class B{"//
                + "int t;"//
                + "B(int t){"//
                + "this.t = t;"//
                + "}"//
                + "}"//
                + "B f(int ¢){"//
                + "return new B(¢){"//
                + "int omigod(){"//
                + "return t*t;"//
                + "}"//
                + "};"//
                + "}"//
                + "}");
  }

  @Test public void test03() {
    trimmingOf("class A{"//
        + "class B{"//
        + "int x;"//
        + "B(int x){"//
        + "this.x = x;"//
        + "}"//
        + "}"//
        + "B f(int in){"//
        + "return new B(in){"//
        + "int omigod(int y){"//
        + "return y*y;"//
        + "}"//
        + "};"//
        + "}"//
        + "}")
            .gives("class A{"//
                + "class B{"//
                + "int x;"//
                + "B(int x){"//
                + "this.x = x;"//
                + "}"//
                + "}"//
                + "B f(int i){"//
                + "return new B(i){"//
                + "int omigod(int y){"//
                + "return y*y;"//
                + "}"//
                + "};"//
                + "}"//
                + "}");
  }
}
