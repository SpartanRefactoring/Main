package il.org.spartan.reflection;

import fluent.ly.dump;

public enum ReflectionAnalyzer {
  ;
  /** @param args command line arguments */
  public static void main(final String[] args) {
    class LocalClass {
      // Nothing here.
    }
    dump.of(int[].class);
    dump.of(void.class);
    dump.of(java.lang.Object[].class);
    dump.of(ReflectionAnalyzer.class);
    dump.of(InnerClass.class);
    dump.of(StaticInnerClass.class);
    dump.of(LocalClass.class);
    dump.of(new Object() {
      @Override public boolean equals(final Object other) {
        return super.equals(other);
      }
      @Override public int hashCode() {
        return super.hashCode();
      }
    }.getClass());
  }
  public static String toBinary(final int value) {
    String $ = "";
    for (int mask = 1; mask != 0; mask <<= 1)
      $ += (mask & value) == 0 ? "" : "+" + mask;
    return $;
  }

  class InnerClass {
    // Nothing here.
  }

  class StaticInnerClass {
    // Nothing here.
  }
}

class A {
  class B {
    A f() {
      return A.this;
    }
  }
}
