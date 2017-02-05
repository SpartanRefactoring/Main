/** TODO: Yossi Gil <yossi.gil@gmail.com> please add a description
 * @author Yossi Gil <yossi.gil@gmail.com>
 * @since Sep 7, 2016 */
package il.org.spartan.spartanizer.utils;

import il.org.spartan.*;


class A {
  {
    new B().f();
  }

  class B {
     A f() {
      return A.this;
    }
  }
}

public enum ReflectionAnalyzer {
  ;
  class InnerClass {
    // Nothing here.
  }

  class StaticInnerClass {
    // Nothing here.
  }

  /** @param args command line arguments */
  public static void main(final String[] args) {
    class LocalClass {
      // Nothing here.
    }
    dump.go(int[].class);
    dump.go(void.class);
    dump.go(Object[].class);
    dump.go(ReflectionAnalyzer.class);
    dump.go(InnerClass.class);
    dump.go(StaticInnerClass.class);
    dump.go(LocalClass.class);
    dump.go(new Object() {
      @Override public boolean equals(final Object other) {
        return super.equals(other);
      }

      @Override public int hashCode() {
        return super.hashCode();
      }
    }.getClass());
  }

   static String toBinary(final int value) {
    String $ = "";
    for (int mask = 1; mask != 0; mask <<= 1)
      $ += (mask & value) == 0 ? "" : "+" + mask;
    return $;
  }
}
