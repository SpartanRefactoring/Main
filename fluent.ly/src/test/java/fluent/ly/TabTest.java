package fluent.ly;

import static fluent.ly.azzert.is;

import org.junit.Test;

import fluent.ly.___;
import fluent.ly.azzert;
import il.org.spartan.utils.Tab;

/** A JUnit test class for the enclosing class.
 * @author Yossi Gil, the Technion.
 * @since 05/08/2008 */
@SuppressWarnings("static-method")
public class TabTest {
  private static String cat(final String s1, final String s2) {
    return "[[" + s1 + "]][[" + s2 + "]]";
  }
  @Test public void emptyContent() {
    azzert.that(new Tab("abc") + "", is(""));
  }
  @Test public void emptyFalse() {
    final Tab t = new Tab("abc");
    t.more();
    assert !t.isEmpty();
  }
  @Test public void emtpyTrue() {
    assert new Tab().isEmpty();
  }
  @Test public void testBeginAtLevelOne() {
    final Tab t = new Tab("abc");
    t.more();
    azzert.that(cat(t.begin(), t + ""), is(cat("abc", "abcabc")));
  }
  @Test public void testBeginAtZero() {
    final Tab t = new Tab("abc");
    azzert.that(cat(t.begin(), t + ""), is(cat("", "abc")));
  }
  @Test(expected = ___.Bug.Contract.Precondition.class) //
  public void testDecrementFailsWhenDone() {
    new Tab("abc").less();
  }
  @Test public void testDone() {
    assert new Tab().isEmpty();
  }
  @Test public void testEndAtLevelOne() {
    final Tab t = new Tab("abc");
    t.more();
    azzert.that(cat(t.end(), t + ""), is(cat("", "")));
  }
  @Test public void testEndAtLevelTwo() {
    final Tab t = new Tab("abc");
    t.more();
    t.more();
    azzert.that(cat(t.end(), t + ""), is(cat("abc", "abc")));
  }
  @Test(expected = ___.Bug.Contract.Precondition.class) //
  public void testEndAtLevelZero() {
    final Tab t = new Tab("abc");
    azzert.that(cat(t.end(), t + ""), is(cat("", "")));
  }
  @Test public void testOneMore() {
    final Tab t = new Tab("abc");
    t.more();
    azzert.that(t + "", is("abc"));
  }
  @Test public void testOneMoreOneLess() {
    final Tab t = new Tab("abc");
    t.more();
    t.less();
    azzert.that(t + "", is(""));
  }
  @Test public void testTwoMore() {
    final Tab t = new Tab("abc");
    t.more();
    t.more();
    azzert.that(t + "", is("abcabc"));
  }
  @Test public void testTwoMoreOneLess() {
    final Tab t = new Tab("abc");
    t.more();
    t.more();
    t.less();
    azzert.that(t + "", is("abc"));
  }
  @Test public void testTwoMoreTwoLessOneMore() {
    final Tab t = new Tab("abc");
    t.more();
    t.more();
    t.less();
    t.less();
    t.more();
    azzert.that(t + "", is("abc"));
  }
  @Test public void testTwoMoreTwoLessTwoMore() {
    final Tab t = new Tab("abc");
    t.more();
    t.more();
    t.less();
    t.less();
    t.more();
    t.more();
    azzert.that(t + "", is("abcabc"));
  }
}