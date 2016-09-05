package il.org.spartan.fun.Dr.Seuss;

import il.org.spartan.fun.Dr.Seuss.Cat.in.the.Hat.*;

interface Cat {
  interface in {
    interface the {
      interface Hat {
        static final Thing thing1 = new Thing() {
        };
        static final Thing thing2 = new Thing() {
        };
        static final Thing[] things = { thing1, thing2 };

        interface Thing {
          /* Empty on purpose */
        }
      }
    }
  }
}

public class Things {
  public static void main(final String[] args) {
    nPattern1();
    nPattern2();
  }

  private static void nPattern1() {
    int i = 0;
    for (final Thing t : il.org.spartan.fun.Dr.Seuss.Cat.in.the.Hat.things)
      doSomethingWith(i++, t);
  }

  private static void nPattern2() {
    int i = 0;
    for (final Thing t : il.org.spartan.fun.Dr.Seuss.Cat.in.the.Hat.things)
      doSomethingWith(i++, t);
  }

  private static void doSomethingWith(final Object... os) {
    // Here is where we do the main job
  }
}
