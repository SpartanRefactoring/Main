package il.org.spartan.refactoring.wring;

import static org.junit.Assert.*;

import org.junit.*;
import org.junit.runners.*;

/** * Unit tests for {@link NameYourClassHere}
 * @author TODO // Write your name here
 * @since 2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "static-method", "javadoc" }) //
public class AAA_TemplateTestTemplate {
  /** if fails, suite did not compile... */
  @Test public void aaaaa0() {
    new Object().toString();
  }

  /** if fails, assertions do not work */
  @Test public void aaaaa1() {
    assert new Object() != null;
  }
  /** if fails, enable assertions (flag '-va') to the JVM */
  @Test(expected = AssertionError.class) //
  public void aaaaa2() {
    assert new Object() == null;
  }
}
