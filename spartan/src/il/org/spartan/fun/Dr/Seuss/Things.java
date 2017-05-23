package il.org.spartan.fun.Dr.Seuss;

import fluent.ly.*;
import il.org.spartan.fun.Dr.Seuss.Cat.in.the.Hat.*;

/** Dr. Seuss Nonsense
 * @author Yossi Gil
 * @since 2016 */
public class Things {
  private static final Thing[] things = il.org.spartan.fun.Dr.Seuss.Cat.in.the.Hat.things;

  public static void main(final String[] args) {
    nPattern1();
    nPattern2();
    nPattern3();
    nPattern4();
  }
  private static void doSomethingWith(final Object... ¢) {
    nothing(¢);
  }
  private static void doSomethingWithPair(final Object o1, final Object o2) {
    nothing(o1, o2);
  }
  private static void nothing(final Object... os) {
    if (os.length >= 2)
      for (final Object ¢ : os)
        nothing(¢);
  }
  private static void nPattern1() {
    for (final Thing ¢ : things)
      doSomethingWith(¢);
  }
  private static void nPattern2() {
    int i = 0;
    for (final Thing ¢ : things)
      doSomethingWithPair(Integer.valueOf(i++), ¢);
  }
  private static void nPattern3() {
    for (int i = 0; i < things.length - 1; ++i) {
      final Thing first = things[i];
      assert first != null;
      final Thing second = things[i + 1];
      assert second != null;
      doSomethingWithPair(first, second);
    }
  }
  private static void nPattern4() {
    for (int i = 0; i <= things.length; ++i) {
      final int f = i - 1, t = i;
      final Thing first = idiomatic.eval(() -> things[f]).unless(i == 0), second = idiomatic.eval(() -> things[t]).unless(i == things.length);
      assert things.length == 0 == (first == null && second == null);
      doSomethingWithPair(first, second);
    }
  }
}

interface Cat {
  interface in {
    interface the {
      interface Hat {
        Thing thing1 = new Thing() {
          //
        };
        Thing thing2 = new Thing() {
          //
        };
        Thing[] things = { thing1, thing2 };

        interface Thing {
          /* Intentionally empty */
        }
      }
    }
  }
}
