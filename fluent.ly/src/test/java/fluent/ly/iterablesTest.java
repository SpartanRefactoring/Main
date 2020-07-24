package fluent.ly;

import static il.org.spartan.Utils.contains;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import an.iterable;
import fluent.ly.azzert;
import fluent.ly.count;
import fluent.ly.the;

//
/** A static nested class hosting unit tests for the nesting class Unit test
 * for the containing class. Note the naming convention: a) names of test
 * methods do not use are not prefixed by "test". This prefix is redundant. b)
 * test methods begin with the name of the method they check.
 * @author Yossi Gil
 * @since 2014-05-31 */
@SuppressWarnings("static-method")
public class iterablesTest {
  @Test public void containsDegenerate() {
    azzert.nay(contains("Hello"));
  }
  @Test public void containseturnsFalseTypical() {
    azzert.nay(contains("Hello", null, "x", "y", null, "z", "w", "u", "v"));
  }
  @Test public void containsSimple() {
    azzert.aye("", contains("Hello", "e"));
  }
  @Test public void containsTypical() {
    azzert.aye("", contains("Hello", "a", "b", "c", "d", "e", "f"));
  }
  @Test public void containsWithNulls() {
    azzert.aye("", contains("Hello", null, "a", "b", null, "c", "d", "e", "f", null));
  }
  @Test public void countDoesNotIncludeNull() {
    assertEquals(3, count.notNull(iterable.over(null, "One", null, "Two", null, "Three")));
  }
  @Test public void countEmpty() {
    assertEquals(0, count.notNull(the.<String> empty()));
  }
  @Test public void countSingleton() {
    assertEquals(1, count.notNull(iterable.singleton(new Object())));
  }
  @Test public void countThree() {
    assertEquals(3, count.notNull(iterable.over("One", "Two", "Three")));
  }
}