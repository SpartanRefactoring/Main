package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.trimmingOf;

import org.junit.Test;

import il.org.spartan.spartanizer.tippers.MethodInvocationToStringToEmptyStringAddition;

/** Unit tests for {@link MethodInvocationToStringToEmptyStringAddition}
 * @author Niv Shalmon
 * @since 2016 */
@SuppressWarnings({ "static-method", "javadoc" })
public final class Issue0209 {
  @Test public void issue116_05() {
    trimmingOf("\"\" + foo(x.toString())")//
        .gives("foo(x.toString()) + \"\"")//
        .gives("foo((\"\"+x)) + \"\"")//
        .gives("foo(\"\"+x) + \"\"")//
        .gives("foo(x + \"\") + \"\"")//
        .stays();
  }
  @Test public void issue116_06() {
    trimmingOf("\"\" + ((Integer)5).toString().indexOf(\"5\").toString().length()")
        .gives("((Integer)5).toString().indexOf(\"5\").toString().length() + \"\"")
        .gives("( \"\" + ((Integer)5).toString().indexOf(\"5\")).length() + \"\"")
        .gives("(((Integer)5).toString().indexOf(\"5\")+\"\").length() + \"\"")//
        .gives("((\"\"+(Integer)5).indexOf(\"5\") + \"\").length() + \"\"").gives("(((Integer)5+ \"\").indexOf(\"5\") + \"\").length() + \"\"")
        .stays();
  }
  @Test public void issue209_01() {
    trimmingOf("new Integer(3).toString()")//
        .gives("\"\"+new Integer(3)")//
        .gives("new Integer(3)+\"\"");
  }
  @Test public void issue209_02() {
    Integer.valueOf(3).toString();
    trimmingOf("new Integer(3).toString();")//
        .gives("Integer.valueOf(3).toString();")//
        .stays();
  }
  @Test public void issue54_01() {
    trimmingOf("(x.toString())")//
        .gives("(\"\"+x)")//
        .gives("(x + \"\")");
  }
  @Test public void issue54_02() {
    trimmingOf("if(x.toString() == \"abc\") return a;")//
        .gives("if( \"\" + x == \"abc\") return a;");
  }
  @Test public void issue54_03() {
    trimmingOf("((Integer)6).toString()")//
        .gives("\"\"+(Integer)6");
  }
  @Test public void issue54_04() {
    trimmingOf("switch(x.toString()){ case \"1\":y=2; return; case \"2\":y=2; return; default:  y=2;return; }")
        .gives("switch(x.toString()){ case \"1\": case \"2\":y=2; return; default: y=2; return; }")
        .gives("switch(x.toString()){ case \"1\": case \"2\": default: y=2; return; }")//
        .gives("switch(\"\" + x){ case \"1\": default:y=2; return; }");
  }
  @Test public void issue54_05() {
    trimmingOf("x.toString(5)")//
        .stays();
  }
  @Test public void issue54_06() {
    trimmingOf("a.toString().length()")//
        .gives("(\"\"+a).length()");
  }
  @Test public void issue54_1() {
    trimmingOf("(x.toString())")//
        .gives("(\"\"+x)");
  }
  @Test public void issue54_2() {
    trimmingOf("String s = f() + o.toString();")//
        .gives("f();o.toString();")//
        .stays();
  }
  @Test public void issue54_3() {
    trimmingOf("o.toString();")//
        .stays();
  }
  @Test public void reorderTest() {
    trimmingOf("\"\" + foo(x.toString())")//
        .gives("foo(x.toString()) + \"\"")//
        .gives("foo((\"\"+x)) + \"\"")//
        .gives("foo(\"\"+x) + \"\"")//
        .gives("foo(x + \"\") + \"\"")//
        .stays();
  }
}
