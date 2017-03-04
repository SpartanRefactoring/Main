package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.azzert.*;
import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;
import static java.lang.Math.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.*;

/** Test of {@link AssignmentUpdateAndSameUpdate}
 * @author Yossi Gil <tt>yogi@cs.technion.ac.il</tt>
 * @since 2017-03-04 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "static-method", "javadoc" })
public class Issue1132 {
  public static void main(String[] args) {
    
      new Triples() {
        @Override public void go(final long a, final long b, final long c) {
          if (b * c != 0)
            azzert.that(String.format("%d %% %d %% %d", box.it(a), box.it(b), box.it(c)), a % b % c, is(a % (b * c)));
        }
      }.go();
    }

  @Test public void and() {
    trimmingOf("a &=2; a &= 3;")//
        .using(Assignment.class, new AssignmentUpdateAndSameUpdate()) //
        .gives("a &= 2 & 3;")//
    ;
  }

  @Test public void divides() {
    trimmingOf("a /=2; a /= 3;")//
        .using(Assignment.class, new AssignmentUpdateAndSameUpdate()) //
        .gives("a /= 2 * 3;")//
    ;
  }

  @Test public void dividesAssociativity() {
    new Triples() {
      @Override public void go(final long a, final long b, final long c) {
        if (b * c != 0)
          azzert.that(a / b / c, is(a / (b * c)));
      }
    }.go();
  }

@Test public void minus() {
  trimmingOf("a -=2; a -= 3;")//
      .using(Assignment.class, new AssignmentUpdateAndSameUpdate()) //
      .gives("a -= 2 + 3;")//
  ;
}

  @Test public void or() {
    trimmingOf("a |=2; a |= 3;")//
        .using(Assignment.class, new AssignmentUpdateAndSameUpdate()) //
        .gives("a |= 2 | 3;")//
    ;
  }

  @Test public void plus() {
    trimmingOf("a +=2; a += 3;")//
        .using(Assignment.class, new AssignmentUpdateAndSameUpdate()) //
        .gives("a += 2 + 3;")//
    ;
  }

  @Test public void times() {
    trimmingOf("a *=2; a *= 3;")//
        .using(Assignment.class, new AssignmentUpdateAndSameUpdate()) //
        .gives("a *= 2 * 3;")//
    ;
  }

  @Ignore @Test public void xor() {
    trimmingOf("a ^= 2; a ^= 3;")//
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
