package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;
import static java.lang.Math.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.tippers.*;
import nano.ly.*;

/** Test of {@link AssignmentUpdateAndSameUpdate}
 * @author Yossi Gil
 * @since 2017-03-04 */
//
@SuppressWarnings({ "static-method", "javadoc" })
public class Issue1132 {
  public static void main(final String[] args) {
    ((Triples) (a, b, c) -> {
      if (b * c != 0)
        azzert.that(String.format("%d %% %d %% %d", box.it(a), box.it(b), box.it(c)), a % b % c, azzert.is(a % (b * c)));
    }).go();
  }

  @Test public void and() {
    trimmingOf("a &=2; a &= 3;")//
        .using(new AssignmentUpdateAndSameUpdate(), Assignment.class) //
        .gives("a &= 2 & 3;")//
    ;
  }

  @Test public void divides() {
    trimmingOf("a /=2; a /= 3;")//
        .using(new AssignmentUpdateAndSameUpdate(), Assignment.class) //
        .gives("a /= 2 * 3;")//
    ;
  }

  @Test public void dividesAssociativity() {
    ((Triples) (a, b, c) -> {
      if (b * c != 0)
        azzert.that(a / b / c, azzert.is(a / (b * c)));
    }).go();
  }

  @Test public void minus() {
    trimmingOf("a -=2; a -= 3;")//
        .using(new AssignmentUpdateAndSameUpdate(), Assignment.class) //
        .gives("a -= 2 + 3;")//
    ;
  }

  @Test public void or() {
    trimmingOf("a |=2; a |= 3;")//
        .using(new AssignmentUpdateAndSameUpdate(), Assignment.class) //
        .gives("a |= 2 | 3;")//
    ;
  }

  @Test public void plus() {
    trimmingOf("a +=2; a += 3;")//
        .using(new AssignmentUpdateAndSameUpdate(), Assignment.class) //
        .gives("a += 2 + 3;")//
    ;
  }

  @Test public void times() {
    trimmingOf("a *=2; a *= 3;")//
        .using(new AssignmentUpdateAndSameUpdate(), Assignment.class) //
        .gives("a *= 2 * 3;")//
    ;
  }

  @Test public void xor() {
    trimmingOf("a ^= 2; a ^= 3;")//
        .using(new AssignmentUpdateAndSameUpdate(), Assignment.class) //
        .gives("a ^= 2 ^ 3;")//
    ;
  }

  @FunctionalInterface
  interface Triples {
    static long sign() {
      return RANDOM.nextLong() > 0 ? 1 : -1;
    }

    static long smaller(final long a) {
      return RANDOM.nextInt(abs((int) a));
    }

    Random RANDOM = new Random();

    default void go() {
      for (long ¢ = 0; ¢ < 7000; ++¢) {
        final long a = RANDOM.nextInt();
        if (a != 0)
          go(a, smaller(a), smaller(a));
      }
    }

    void go(long a, long b, long c);
  }
}
