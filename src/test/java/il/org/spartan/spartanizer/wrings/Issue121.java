package il.org.spartan.spartanizer.wrings;

import static il.org.spartan.spartanizer.wrings.TrimmerTestsUtils.*;

import org.junit.*;

@SuppressWarnings("static-method") public final class Issue121 {
  @Test public void test01() {
    trimmingOf("class A{ \n" //
        + "static public final class EX13{ \n" //
        + "class F{ \n" //
        + "int x;   \n" + "int giveMeANumber() {return 0;}   \n" //
        + "}   \n" //
        + "F foo(int ai, int y){   \n" //
        + "return new F(y){    \n" //
        + "@Override int  giveMeANumber(){    \n" //
        + "return ai*x;    \n" //
        + "}   \n" //
        + "};   \n" //
        + "}   \n" //
        + "}   \n" //
        + "}  \n" //
    ).gives("class A{ \n" //
        + "public static final class EX13{ \n" //
        + "class F{ \n" //
        + "int x;   \n" + "int giveMeANumber() {return 0;}   \n" //
        + "}   \n" //
        + "F foo(int ai, int y){   \n" //
        + "return new F(y){    \n" //
        + "@Override int  giveMeANumber(){    \n" //
        + "return ai*x;    \n" //
        + "}   \n" //
        + "};   \n" //
        + "}   \n" //
        + "}   \n" //
        + "}  \n" //
    );
  }

  @Test public void test02() {
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

  @Test public void test03() {
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
  @Ignore public void test04() {
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

  @Test public void test05() {
    trimmingOf("class A{"//
        + "class B{"//
        + "int t;"//
        + "B(int t){"//
        + "this.t = t;"//
        + "}"//
        + "}"//
        + "B f(int t){"//
        + "return new B(t){"//
        + "int omigod(int t){"//
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
                + "B f(int i){"//
                + "return new B(i){"//
                + "int omigod(int t){"//
                + "return t*t;"//
                + "}"//
                + "};"//
                + "}"//
                + "}");
  }
}
