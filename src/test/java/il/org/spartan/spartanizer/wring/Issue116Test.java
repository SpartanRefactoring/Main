package il.org.spartan.spartanizer.wring;

import static il.org.spartan.spartanizer.wring.TrimmerTestsUtils.*;

import org.junit.*;
import org.junit.runners.*;

/** Unit tests for {@link NameYourClassHere}
 * @author TODO // Write your name here
 * @since 2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "static-method", "javadoc" }) //
public class Issue116Test {
  @Test public void issue116_01() {
    trimming("\"\" + x").to("x + \"\"").stays();
  }

  @Test public void issue116_02() {
    trimming("\"\" + x.foo()").to("x.foo() + \"\"").stays();
  }

  @Test public void issue116_03() {
    trimming("\"\" + (Integer)(\"\" + x).length()").to("(Integer)(\"\" + x).length() + \"\"").to("(Integer)(x +\"\").length() + \"\"").stays();
  }

  @Test public void issue116_04() {
    trimming("String s = \"\" + x.foo();").to("String s = x.foo() + \"\";").stays();
  }

  @Test public void issue116_05() {
    trimming("\"\" + foo(x.toString())").to("foo(x.toString()) + \"\"").to("foo((x + \"\")) + \"\"").stays();
  }

  @Test public void issue116_06() {
    trimming("\"\" + ((Integer)5).toString().indexOf(\"5\").toString().length()")
        .to("((Integer)5).toString().indexOf(\"5\").toString().length() + \"\"").to("(((Integer)5).toString().indexOf(\"5\") + \"\").length() + \"\"")
        .to("(((Integer)5+ \"\").indexOf(\"5\") + \"\").length() + \"\"").stays();
  }

  @Test public void issue116_07() {
    trimming("\"\" + 0 + (x - 7)").to("0 + \"\" + (x - 7)").stays();
  }

  @Test public void issue116_08() {
    trimming("return x == null ? \"Use isEmpty()\" : \"Use \" + x + \".isEmpty()\";")
        .to("return \"Use \" + (x == null ? \"isEmpty()\" : \"\" + x + \".isEmpty()\");")
        .to("return \"Use \" + ((x == null ? \"\" : \"\" + x + \".\")+\"isEmpty()\");")
        .to("return \"Use \" + (x == null ? \"\" : \"\" + x  + \".\")+\"isEmpty()\";")
        .to("return \"Use \" + (x == null ? \"\" : x +\"\" + \".\")+\"isEmpty()\";")
        .to("return \"Use \" + (x == null ? \"\" : x + \".\")+\"isEmpty()\";").stays();
  }
}
