package il.org.spartan.spartanizer.java;

import static fluent.ly.azzert.is;

import org.junit.Test;

import fluent.ly.azzert;
import fluent.ly.forget;

@SuppressWarnings("static-method") //
public final class InitializerTest {
  static int a;
  static InitializerTest i;
  static {
    InitializerTest.a = 100;
    InitializerTest.b = 200;
    InitializerTest.c = 300;
    // i.e = 500;
    print("Static initializer");
  }
  static int b;
  static int c;

  static void print(final Object ¢) {
    forget.em(new Object[] { ¢ });
  }

  final int e;
  {
    // Instance Initializer can union some operations which are required for
    // each constructor of the class.
    // for example: a is updated here instead of being updated by each
    // constructor.
    a = 0;
    e = 5000;
    print("Instance initializer");
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
