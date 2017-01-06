package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.junit.*;

/** This is a unit test for {@link ReturnToBreakFiniteFor} of previously failed
 * tests. Related to {@link Issue0131}.
 * @author Yuval Simon
 * @since 2016-12-08 */
@Ignore
@SuppressWarnings("static-method")
public class Issue903 {
  @Test public void A$020() {
    trimmingOf("while(i>9)if(i==5)return x;return x;").gives("while(i>9){if(i==5)return x;if(i<=9)return x;}");
  }

  @Test public void A$070() {
    trimmingOf("while(i>5)return x;return x;").gives("while(i>5){return x;if(i<=5)return x;}").stays();
  }

  @Test public void A$080() {
    trimmingOf("while(i>5)if(tipper=4)return x;return x;").gives("while(i>5){if(tipper=4)return x;if(i<=5)return x;}");
  }
}
