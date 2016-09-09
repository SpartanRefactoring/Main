package il.org.spartan.spartanizer.wring;

import static il.org.spartan.spartanizer.wring.TrimmerTestsUtils.*;

import org.junit.*;

/** @author Yossi Gil
 * @since 2016 */
@SuppressWarnings("static-method") //
public class BlockSimplifyTest {
  @Test public void seriesA00() {
    trimming("public void testParseInteger() {\n" + "").stays();
  }

  @Test public void seriesA01() {
    trimming("public void f() {\n" + "").stays();
  }

  @Test public void seriesA02() {
    trimming("public void f() {\n" + "").stays();
  }

  @Test public void seriesA03() {
    trimming("public void f() {\n" + "").stays();
  }

  @Test public void seriesA04() {
    trimming("public void f() {\n" + "").stays();
  }

  @Test public void seriesA05() {
    trimming("public void f() {\n" + "").stays();
  }

  @Test public void seriesA06() {
    trimming("public void f() {\n" + "").stays();
  }

  @Test public void seriesA07() {
    trimming("public void f() {\n" + "").stays();
  }

  @Test public void seriesA08() {
    trimming("public void f() {\n" + "").stays();
  }

  @Test public void seriesA09() {
    trimming("public void f() {\n" + "")
            .to("public void f() {\n" + ""
            ).stays();
  }

  @Test public void seriesA10() {
    trimming("public void f() {\n" + "")
            .to("public void f() {\n" + ""
            ).stays();
  }
}
