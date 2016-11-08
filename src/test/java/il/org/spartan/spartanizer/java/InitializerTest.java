package il.org.spartan.spartanizer.java;

import static il.org.spartan.azzert.*;

import org.junit.*;

import il.org.spartan.*;

@SuppressWarnings("static-method") //
public final class InitializerTest {
  static int a;
  static InitializerTest i;
  static {
    InitializerTest.a = 100;
    InitializerTest.b = 200;
    InitializerTest.c = 300;
    // i.e = 500;
    System.out.println("Static initializer");
  }
  static int b;
  static int c;
  int e;
  {
    // Instance Initializer can union some operations which are required for
    // each constructor of the class.
    // for example: a is updated here instead of being updated by each
    // constructor.
    a = 0;
    e = 5000;
    System.out.println("Instance initializer");
  }

  @Test public void T_00() {
    azzert.that(InitializerTest.a, is(0));
    azzert.that(InitializerTest.b, is(200));
    azzert.that(InitializerTest.c, is(300));
  }
  @Test public void T_01() {
    i = new InitializerTest();
    azzert.that(InitializerTest.a, is(0));
  }
}