package il.org.spartan.spartanizer.wrings;

import static il.org.spartan.azzert.*;
import static il.org.spartan.spartanizer.wrings.TrimmerTestsUtils.*;

import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.*;

/** @author // TODO ALEX
 * @since 2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "static-method", "javadoc" }) //
public class Issue177 {
  @SuppressWarnings("unused") @Test public void BitWiseAnd_withSideEffectsEXT() {
    class Class {
      Inner in = new Inner(0);

      Class() {
        final int x = in.f(1) & 1;
        azzert.that(x, is(0));
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
    trimmingOf("a=a & b").gives("a&=b");
  }

  @Test public void bitWiseOr_noSideEffects() {
    int a = 1;
    final int b = 2;
    a |= b;
    azzert.that(a, is(3));
    trimmingOf("a=a|b").gives("a|=b");
  }

  @SuppressWarnings("unused") @Test public void bitWiseOr_withSideEffects() {
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
    trimmingOf("a=a|b").gives("a|=b");
  }

  @SuppressWarnings("unused") @Test public void BitWiseOr_withSideEffectsEXT() {
    class Class {
      Inner in = new Inner(0);

      Class() {
        final int x = in.f(1) | 1;
        azzert.that(x, is(3));
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
    trimmingOf("a=a | b").gives("a|=b");
  }

  @SuppressWarnings("unused") @Test public void BitWiseXor_withSideEffectsEXT() {
    class Class {
      Inner in = new Inner(0);

      Class() {
        final int x = in.f(1) ^ 1;
        azzert.that(x, is(3));
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
    trimmingOf("a = a ^ b ").gives("a ^= b");
  }

  @Test public void logicalAnd_noSideEffects() {
    boolean a = true;
    final boolean b = false;
    a &= b;
    azzert.nay(a);
    trimmingOf("a=a && b").gives("a&=b");
  }

  @SuppressWarnings("unused") @Test public void logicalAnd_withSideEffects() {
    class Class {
      int a;

      Class() {
        a = 0;
        final boolean x = f(true) & true;
        azzert.nay(x);
        azzert.that(a, is(1));
      }

      boolean f(final boolean $) {
        azzert.aye($);
        ++a;
        return false;
      }
    }
    new Class();
    trimmingOf("a=a && b").gives("a&=b");
  }

  @SuppressWarnings("unused") @Test public void logicalAnd_withSideEffectsEX() {
    class Class {
      Inner in = new Inner(0);

      Class() {
        final boolean x = in.f(true) & true;
        azzert.nay(x);
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
    trimmingOf("a=a && b").gives("a&=b");
  }

  @SuppressWarnings("unused") @Test public void logicalAnd_withSideEffectsEXT() {
    class Class {
      Inner in = new Inner(0);

      Class() {
        final boolean x = in.f(true) & true;
        azzert.nay(x);
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
    trimmingOf("a=a && b").gives("a&=b");
  }

  @Test public void logicalOr_noSideEffects() {
    boolean a = false;
    final boolean b = true;
    a |= b;
    azzert.aye(a);
    trimmingOf("a=a||b").gives("a|=b");
  }

  @SuppressWarnings("unused") @Test public void logicalOr_withSideEffects() {
    class Class {
      int a;

      Class() {
        a = 0;
        final boolean x = f(false) | false;
        azzert.aye(x);
        azzert.that(a, is(1));
      }

      boolean f(final boolean $) {
        azzert.nay($);
        ++a;
        return true;
      }
    }
    new Class();
    trimmingOf("a=a||b").gives("a|=b");
  }

  @SuppressWarnings("unused") @Test public void logicalOr_withSideEffectsEX() {
    class Class {
      Inner in = new Inner(0);

      Class() {
        final boolean x = in.f(false) | false;
        azzert.aye(x);
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
    trimmingOf("a=a||b").gives("a|=b");
  }

  @SuppressWarnings("unused") @Test public void LogicalOr_withSideEffectsEXT() {
    class Class {
      Inner in = new Inner(0);

      Class() {
        final int x = in.f(1) | 1;
        azzert.that(x, is(3));
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
    trimmingOf("a=a|(b=b&a)").gives("a|=b=b&a").gives("a|=b&=a");
  }
}
