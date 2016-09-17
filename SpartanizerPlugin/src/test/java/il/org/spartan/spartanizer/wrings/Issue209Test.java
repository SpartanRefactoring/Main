package il.org.spartan.spartanizer.wrings;

import static il.org.spartan.spartanizer.wrings.TrimmerTestsUtils.*;

import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.spartanizer.wrings.*;

/** Unit tests for {@link MethodInvocationToStringToEmptyStringAddition}
 * @author Niv Shalmon
 * @since 2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "static-method", "javadoc" }) //
public class Issue209Test {
  @Test public void issue116_05() {
    trimming("\"\" + foo(x.toString())").to("foo(x.toString()) + \"\"").to("foo((x + \"\")) + \"\"").stays();
  }

  @Test public void issue116_06() {
    trimming("\"\" + ((Integer)5).toString().indexOf(\"5\").toString().length()")
        .to("((Integer)5).toString().indexOf(\"5\").toString().length() + \"\"").to("(((Integer)5).toString().indexOf(\"5\") + \"\").length() + \"\"")
        .to("(((Integer)5+ \"\").indexOf(\"5\") + \"\").length() + \"\"").stays();
  }

  // TODO: Yossi, you changed this to stay() for some reason. Notice that in
  // this case there is no ";" at the end, so the context is not an expression
  // statement but rather a simple statement and it should be changed.
  // issue209_02 is the same case as an expression statement, and it really
  // doesn't change there. I think this test should look like this
  @Test public void issue209_01() {
    trimming("new Integer(3).toString()").to("new Integer(3)+\"\"");
  }

  @Test public void issue209_02() {
    trimming("new Integer(3).toString();").stays();
  }

  @Test public void issue54_01() {
    trimming("(x.toString())").to("(x + \"\")");
  }

  @Test public void issue54_02() {
    trimming("if(x.toString() == \"abc\") return a;").to("if(x + \"\" == \"abc\") return a;");
  }

  @Test public void issue54_03() {
    trimming("((Integer)6).toString()").to("(Integer)6 + \"\"");
  }

  @Test public void issue54_04() {
    trimming("switch(x.toString()){ case \"1\": return; case \"2\": return; default: return; }")
        .to("switch(x + \"\"){ case \"1\": return; case \"2\": return; default: return; }");
  }

  @Test public void issue54_05() {
    trimming("x.toString(5)").stays();
  }

  @Test public void issue54_06() {
    trimming("a.toString().length()").to("(a + \"\").length()");
  }

  @Test public void issue54_1() {
    trimming("(x.toString())").to("(x+\"\")");
  }

  @Test public void issue54_2() {
    trimming("String s = f() + o.toString();").to("String s = f() + o + \"\";").stays();
  }

  @Test public void reorderTest() {
    trimming("\"\" + foo(x.toString())").to("foo(x.toString()) + \"\"").to("foo((x + \"\")) + \"\"").stays();
  }
}
