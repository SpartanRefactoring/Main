package il.org.spartan.spartanizer.wrings;

import static il.org.spartan.spartanizer.wrings.TrimmerTestsUtils.*;

import org.junit.*;

@SuppressWarnings("static-method") public final class Issue121Test {
  @Test public void test01() {
    trimming("class A{ \n" //
        + "static public final class EX13{ \n" //
        + "class Onoes{ \n" //
        + "int x;   \n" + "int giveMeANumber() {return 0;}   \n" //
        + "}   \n" //
        + "Onoes foo(int n, int y){   \n" //
        + "return new Onoes(y){    \n" //
        + "@Override int  giveMeANumber(){    \n" //
        + "return n*x;    \n" //
        + "}   \n" //
        + "};   \n" //
        + "}   \n" //
        + "}   \n" //
        + "}  \n" //
    ).to("class A{ \n" //
        + "static public final class EX13{ \n" //
        + "class Onoes{ \n" //
        + "int x;   \n" + "int giveMeANumber() {return 0;}   \n" //
        + "}   \n" //
        + "Onoes foo(int i, int y){   \n" //
        + "return new Onoes(y){    \n" //
        + "@Override int  giveMeANumber(){    \n" //
        + "return i*x;    \n" //
        + "}   \n" //
        + "};   \n" //
        + "}   \n" //
        + "}   \n" //
        + "}  \n" //
    );
  }

  @Test public void test02() {
    trimming("class A{ \n" //
        + "class B{} \n" //
        + "B f(int t){ \n" //
        + "return new B(){ \n" //
        + "public int g(){ \n" //
        + "return t; \n" //
        + "} \n" //
        + "}; \n" //
        + "} \n" //
        + "}")
            .to("class A{ \n" //
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
    trimming("class A{ \n" //
        + "class B{} \n" //
        + "B f(int t){ \n" //
        + "return new B(){ \n" //
        + "public int g(){ \n" //
        + "return t; \n" //
        + "} \n" //
        + "}; \n" //
        + "} \n" //
        + "}")
            .to("class A{ \n" //
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
    trimming("class A{"//
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
            .to("class A{"//
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
    trimming("class A{"//
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
            .to("class A{"//
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
