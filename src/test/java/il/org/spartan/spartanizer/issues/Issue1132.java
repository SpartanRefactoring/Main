package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsTrimmer.*;
import static java.lang.Math.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.tippers.*;

/** Test of {@link AssignmentUpdateAndSameUpdate}
 * @author Yossi Gil
 * @since 2017-03-04 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "static-method", "javadoc" })
public class Issue1132 {
  public static void main(final String[] args) {
    ((Triples) (a, b, c) -> {
      if (b * c != 0)
        azzert.that(String.format("%d %% %d %% %d", box.it(a), box.it(b), box.it(c)), a % b % c, azzert.is(a % (b * c)));
    }).go();
  }

  @Test public void and() {
    topDownTrimming("a &=2; a &= 3;")//
        .using(Assignment.class, new AssignmentUpdateAndSameUpdate()) //
        .gives("a &= 2 & 3;")//
    ;
  }

  @Test public void divides() {
    topDownTrimming("a /=2; a /= 3;")//
        .using(Assignment.class, new AssignmentUpdateAndSameUpdate()) //
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
    topDownTrimming("a -=2; a -= 3;")//
        .using(Assignment.class, new AssignmentUpdateAndSameUpdate()) //
        .gives("a -= 2 + 3;")//
    ;
  }

  @Test public void or() {
    topDownTrimming("a |=2; a |= 3;")//
        .using(Assignment.class, new AssignmentUpdateAndSameUpdate()) //
        .gives("a |= 2 | 3;")//
    ;
  }

  @Test public void plus() {
    topDownTrimming("a +=2; a += 3;")//
        .using(Assignment.class, new AssignmentUpdateAndSameUpdate()) //
        .gives("a += 2 + 3;")//
    ;
  }

  @Test public void times() {
    topDownTrimming("a *=2; a *= 3;")//
        .using(Assignment.class, new AssignmentUpdateAndSameUpdate()) //
        .gives("a *= 2 * 3;")//
    ;
  }

  @Test public void xor() {
    topDownTrimming("a ^= 2; a ^= 3;")//
        .using(Assignment.class, new AssignmentUpdateAndSameUpdate()) //
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
