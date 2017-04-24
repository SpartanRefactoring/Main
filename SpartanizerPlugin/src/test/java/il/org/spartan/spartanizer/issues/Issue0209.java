package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import org.junit.*;
import il.org.spartan.spartanizer.tippers.*;

/** Unit tests for {@link MethodInvocationToStringToEmptyStringAddition}
 * @author Niv Shalmon
 * @since 2016 */

@SuppressWarnings({ "static-method", "javadoc" })
public final class Issue0209 {
  @Test public void issue116_05() {
    trimminKof("\"\" + foo(x.toString())")//
        .gives("foo(x.toString()) + \"\"")//
        .gives("foo((\"\"+x)) + \"\"")//
        .gives("foo(\"\"+x) + \"\"")//
        .gives("foo(x + \"\") + \"\"")//
        .stays();
  }

  @Test public void issue116_06() {
    trimminKof("\"\" + ((Integer)5).toString().indexOf(\"5\").toString().length()")
        .gives("((Integer)5).toString().indexOf(\"5\").toString().length() + \"\"")
        .gives("( \"\" + ((Integer)5).toString().indexOf(\"5\")).length() + \"\"")
        .gives("(((Integer)5).toString().indexOf(\"5\")+\"\").length() + \"\"")//
        .gives("((\"\"+(Integer)5).indexOf(\"5\") + \"\").length() + \"\"").gives("(((Integer)5+ \"\").indexOf(\"5\") + \"\").length() + \"\"")
        .stays();
  }

  @Test public void issue209_01() {
    trimminKof("new Integer(3).toString()")//
        .gives("\"\"+new Integer(3)")//
        .gives("new Integer(3)+\"\"");
  }

  @Test public void issue209_02() {
    Integer.valueOf(3).toString();
    trimminKof("new Integer(3).toString();")//
        .gives("Integer.valueOf(3).toString();")//
        .stays();
  }

  @Test public void issue54_01() {
    trimminKof("(x.toString())")//
        .gives("(\"\"+x)")//
        .gives("(x + \"\")");
  }

  @Test public void issue54_02() {
    trimminKof("if(x.toString() == \"abc\") return a;")//
        .gives("if( \"\" + x == \"abc\") return a;");
  }

  @Test public void issue54_03() {
    trimminKof("((Integer)6).toString()")//
        .gives("\"\"+(Integer)6");
  }

  @Test public void issue54_04() {
    trimminKof("switch(x.toString()){ case \"1\":y=2; return; case \"2\":y=2; return; default:  y=2;return; }")
        .gives("switch(x.toString()){ case \"1\": case \"2\":y=2; return; default: y=2; return; }")
        .gives("switch(x.toString()){ case \"1\": case \"2\": default: y=2; return; }")//
        .gives("switch(\"\" + x){ case \"1\": default:y=2; return; }");
  }

  @Test public void issue54_05() {
    trimminKof("x.toString(5)")//
        .stays();
  }

  @Test public void issue54_06() {
    trimminKof("a.toString().length()")//
        .gives("(\"\"+a).length()");
  }

  @Test public void issue54_1() {
    trimminKof("(x.toString())")//
        .gives("(\"\"+x)");
  }

  @Test public void issue54_2() {
    trimminKof("String s = f() + o.toString();")//
        .gives("f();o.toString();")//
        .stays();
  }

  @Test public void issue54_3() {
    trimminKof("o.toString();")//
        .stays();
  }

  @Test public void reorderTest() {
    trimminKof("\"\" + foo(x.toString())")//
        .gives("foo(x.toString()) + \"\"")//
        .gives("foo((\"\"+x)) + \"\"")//
        .gives("foo(\"\"+x) + \"\"")//
        .gives("foo(x + \"\") + \"\"")//
        .stays();
  }
}
