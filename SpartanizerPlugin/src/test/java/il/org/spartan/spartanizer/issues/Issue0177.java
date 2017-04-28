package il.org.spartan.spartanizer.issues;

import static il.org.spartan.azzert.*;
import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import org.junit.*;

import il.org.spartan.*;

/** Tests for the GitHub issue thus numbered
 * @author Alex Kopzon
 * @author Dan Greenstein
 * @since 2016 */
@SuppressWarnings({ "static-method", "javadoc", "PointlessBooleanExpression" })
public class Issue0177 {
  @Test @SuppressWarnings("unused") public void BitWiseAnd_withSideEffectsEXT() {
    class Class {
      final Inner in = new Inner(0);

      Class() {
        azzert.that(in.f(1) & 1, is(0));
        azzert.that(in.a, is(1));
      }

      class Inner {
        int a;

        Inner(final int i) {
          a = i;
        }

        int f(final int $) {
          azzert.that($, is(1));
          return g();
        }

        int g() {
          // noinspection SameReturnValue
          class C {
            C() {
              h();
              ++a;
            }

            int h() {
              return 2;
            }
          }
          return new C().h();
        }
      }
    }
    new Class();
    trimmingOf("a=a & b")//
        .gives("a&=b");
  }

  @Test public void bitWiseOr_noSideEffects() {
    azzert.that(1 | 2, is(3));
    trimmingOf("a=a|b")//
        .gives("a|=b");
  }

  @Test @SuppressWarnings("unused") public void bitWiseOr_withSideEffects() {
    class Class {
      Class() {
        azzert.that(f(1) | 1, is(3));
      }

      int f(final int $) {
        azzert.that($, is(1));
        return $ + 1;
      }
    }
    new Class();
    trimmingOf("a=a|b")//
        .gives("a|=b");
  }

  @Test @SuppressWarnings("unused") public void BitWiseOr_withSideEffectsEXT() {
    class Class {
      final Inner in = new Inner(0);

      Class() {
        azzert.that(in.f(1) | 1, is(3));
        azzert.that(in.a, is(1));
      }

      class Inner {
        int a;

        Inner(final int i) {
          a = i;
        }

        int f(final int $) {
          azzert.that($, is(1));
          return g();
        }

        int g() {
          // noinspection SameReturnValue
          class C {
            C() {
              h();
              ++a;
            }

            int h() {
              return 2;
            }
          }
          return new C().h();
        }
      }
    }
    new Class();
    trimmingOf("a=a | b")//
        .gives("a|=b");
  }

  @Test @SuppressWarnings("unused") public void BitWiseXor_withSideEffectsEXT() {
    class Class {
      final Inner in = new Inner(0);

      Class() {
        azzert.that(in.f(1) ^ 1, is(3));
        azzert.that(in.a, is(1));
      }

      class Inner {
        int a;

        Inner(final int i) {
          a = i;
        }

        int f(final int $) {
          azzert.that($, is(1));
          return g();
        }

        int g() {
          // noinspection SameReturnValue
          class C {
            C() {
              h();
              ++a;
            }

            int h() {
              return 2;
            }
          }
          return new C().h();
        }
      }
    }
    new Class();
    trimmingOf("a = a ^ b ")//
        .gives("a ^= b");
  }

  @Test public void logicalAnd_noSideEffects() {
    azzert.nay(true & false);
    trimmingOf("a=a && b")//
        .gives("a&=b");
  }

  @Test @SuppressWarnings("unused") public void logicalAnd_withSideEffects() {
    // noinspection SameReturnValue
    @SuppressWarnings("PointlessBooleanExpression")
    class Class {
      int a;

      Class() {
        a = 0;
        azzert.nay(f(true) & true);
        azzert.that(a, is(1));
      }

      boolean f(final boolean $) {
        azzert.aye($);
        ++a;
        return false;
      }
    }
    new Class();
    trimmingOf("a=a && b")//
        .gives("a&=b");
  }

  @Test @SuppressWarnings("unused") public void logicalAnd_withSideEffectsEX() {
    // noinspection SameReturnValue
    @SuppressWarnings("PointlessBooleanExpression")
    class Class {
      final Inner in = new Inner(0);

      Class() {
        azzert.nay(in.f(true) & true);
        azzert.aye(in.a == 1);
      }

      class Inner {
        int a;

        Inner(final int i) {
          a = i;
        }

        boolean f(final boolean $) {
          azzert.aye($);
          ++a;
          return false;
        }
      }
    }
    new Class();
    trimmingOf("a=a && b")//
        .gives("a&=b");
  }

  @Test @SuppressWarnings("unused") public void logicalAnd_withSideEffectsEXT() {
    @SuppressWarnings("PointlessBooleanExpression")
    class Class {
      final Inner in = new Inner(0);

      Class() {
        azzert.nay(in.f(true) & true);
        azzert.that(in.a, is(1));
      }

      class Inner {
        int a;

        Inner(final int i) {
          a = i;
        }

        boolean f(final boolean $) {
          azzert.aye($);
          return g();
        }

        boolean g() {
          // noinspection SameReturnValue
          class C {
            C() {
              h();
              ++a;
            }

            boolean h() {
              return false;
            }
          }
          return new C().h();
        }
      }
    }
    new Class();
    trimmingOf("a=a && b")//
        .gives("a&=b");
  }

  @Test public void logicalOr_noSideEffects() {
    azzert.aye(true | false);
    trimmingOf("a=a||b")//
        .gives("a|=b");
  }

  @Test @SuppressWarnings("unused") public void logicalOr_withSideEffects() {
    // noinspection SameReturnValue
    @SuppressWarnings("PointlessBooleanExpression")
    class Class {
      int a;

      Class() {
        a = 0;
        azzert.aye(f(false) | false);
        azzert.that(a, is(1));
      }

      boolean f(final boolean $) {
        azzert.nay($);
        ++a;
        return true;
      }
    }
    new Class();
    trimmingOf("a=a||b")//
        .gives("a|=b");
  }

  @Test @SuppressWarnings("unused") public void logicalOr_withSideEffectsEX() {
    // noinspection SameReturnValue
    @SuppressWarnings("PointlessBooleanExpression")
    class Class {
      final Inner in = new Inner(0);

      Class() {
        azzert.aye(in.f(false) | false);
        azzert.that(in.a, is(1));
      }

      class Inner {
        int a;

        Inner(final int i) {
          a = i;
        }

        boolean f(final boolean $) {
          azzert.nay($);
          ++a;
          return true;
        }
      }
    }
    new Class();
    trimmingOf("a=a||b")//
        .gives("a|=b");
  }

  @Test @SuppressWarnings("unused") public void LogicalOr_withSideEffectsEXT() {
    class Class {
      final Inner in = new Inner(0);

      Class() {
        azzert.that(in.f(1) | 1, is(3));
        azzert.that(in.a, is(1));
      }

      class Inner {
        int a;

        Inner(final int i) {
          a = i;
        }

        int f(final int $) {
          azzert.that($, is(1));
          return g();
        }

        int g() {
          // noinspection SameReturnValue
          class C {
            C() {
              h();
              ++a;
            }

            int h() {
              return 2;
            }
          }
          return new C().h();
        }
      }
    }
    new Class();
    trimmingOf("a=a|(b=b&a)")//
        .gives("a|=b=b&a")//
        .gives("a|=b&=a");
  }
}
