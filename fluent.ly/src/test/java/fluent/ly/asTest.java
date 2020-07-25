package fluent.ly;

import static fluent.ly.azzert.is;
import static org.junit.Assert.assertArrayEquals;

import java.util.List;

import org.junit.Test;

// No values in an 'enum' which serves as a name space for a collection of
// 'static' functions.
/**
 * A static nested class hosting unit tests for the nesting class Unit test for
 * the containing class. Note the naming convention: a) names of test methods do
 * not use are not prefixed by "test". This prefix is redundant. b) test methods
 * begin with the name of the method they check.
 *
 * @author Yossi Gil
 * @since 2014-05-31
 */
@SuppressWarnings("static-method") public class asTest {
  @Test public void asBitOfFalse() {
    azzert.that(as.bit(false), is(0));
  }

  @Test public void asBitOfTrue() {
    azzert.that(as.bit(true), is(1));
  }

  @Test public void asIntArraySimple() {
    final var is = as.intArray(100, 200, 200, 12, 13, 0);
    assertArrayEquals(is, as.ints(as.ilist(is)));
  }

  @Test public void asListSimple() {
    final var is = as.ilist(12, 13, 14);
    azzert.that(is.get(0), is(fluent.ly.box.it(12)));
    azzert.that(is.get(1), is(fluent.ly.box.it(13)));
    azzert.that(is.get(2), is(fluent.ly.box.it(14)));
    azzert.that(is.size(), is(3));
  }

  @Test public void stringOfNull() {
    azzert.that(as.string(null), is("null"));
  }

  @Test public void stringWhenToStringReturnsNull() {
    azzert.that(as.string(new Object() {
      @Override public String toString() {
        return null;
      }
    }), is("null"));
  }
}