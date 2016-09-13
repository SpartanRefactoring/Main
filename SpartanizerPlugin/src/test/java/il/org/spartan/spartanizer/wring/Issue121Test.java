package il.org.spartan.spartanizer.wring;

import static il.org.spartan.spartanizer.wring.TrimmerTestsUtils.*;

import org.junit.*;

@SuppressWarnings("static-method") public class Issue121Test {
  @Test public void test01() {
    trimming("class A{ \n" //
        + "static public class EX13{ \n" //
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
        + "static public class EX13{ \n" //
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
}
