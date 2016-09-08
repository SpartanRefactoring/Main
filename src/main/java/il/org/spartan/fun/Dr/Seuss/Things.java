package il.org.spartan.fun.Dr.Seuss;

import il.org.spartan.*;
import il.org.spartan.fun.Dr.Seuss.Cat.in.the.Hat.*;

/**
 * @author yogi
 *
 */
/**
 * @author yogi
 *
 */
public class Things {
  private static final Thing[] things = il.org.spartan.fun.Dr.Seuss.Cat.in.the.Hat.things;

  public static void main(final String[] args) {
    nPattern1();
    nPattern2();
    nPattern3();
    nPattern4();
  }

  private static void doSomethingWith(final Object... os) {
    nothing(os);
  }

  private static void doSomethingWithPair(final Object o1, final Object o2) {
    nothing(o1, o2);
  }

  private static void nothing(final Object... os) {
    if (os.length >= 2)
      for (Object o : os)
        nothing(o);
  }

  private static void nPattern1() {
    for (final Thing t : things)
      doSomethingWith(t);
  }

  private static void nPattern2() {
    int i = 0;
    for (final Thing t : things)
      doSomethingWithPair(Integer.valueOf(i++), t);
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
      final int f = i - 1;
      final int t = i;
      final Thing first = idiomatic.eval(() -> things[f]).unless(i == 0);
      final Thing second = idiomatic.eval(() -> things[t]).unless(i == things.length);
      assert things.length == 0 == (first == null && second == null);
      doSomethingWithPair(first, second);
    }
  }
}

interface Cat {
  interface in {
    interface the {
      interface Hat {
        static final Thing thing1 = new Thing() {
          /* Intentionally empty */
        };
        static final Thing thing2 = new Thing() {
          /* Intentionally empty */
        };
        static final Thing[] things = { thing1, thing2 };

        interface Thing {
          /* Intentionally empty */
        }
      }
    }
  }
}
