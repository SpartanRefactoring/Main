package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import org.junit.*;

/** Test cases for validating that centification works properly with try/catch
 * statements
 * @author Dor Ma'ayan
 * @since 22-11-2016 */
@SuppressWarnings("static-method")
public class Issue0308 {
  @Test public void test0() {
    trimmingOf("try{int a;int b; return a+b;}catch(Exception e){return e;}")//
        .gives("try{int a, b; return a+b;}catch(Exception ¢){return ¢;}");
  }
  @Test public void test1() {
    trimmingOf("try{int a;int b; return a+b;}catch(Exception e){e.toString();return e;}")//
        .gives("try{int a, b; return a+b;}catch(Exception ¢){¢.toString();return ¢;}");
  }
  @Test public void test2() {
    trimmingOf("try{int a;int b; return a+b;}catch(Exception e){return e;}finally{int c=0; f(++c);}")//
        .gives("try{int a, b; return a+b;}catch(Exception ¢){return ¢;}finally{int c=0; f(++c);}");
  }
  @Test public void test3() {
    trimmingOf("try{int a;int b; return a+b;}catch(Exception e){t.find();return e;}finally{int c=0; f(++c);}")//
        .gives("try{int a, b; return a+b;}catch(Exception ¢){t.find();return ¢;}finally{int c=0; f(++c);}");
  }
  @Test public void test4() {
    trimmingOf("try{int a;int b; return a+b;}catch(Exception e){t.find();return e;}catch(Exceptional e){return e;}finally{int c=0; f(++c);}")//
        .gives("try{int a, b; return a+b;}catch(Exception ¢){t.find();return ¢;}catch(Exceptional ¢){return ¢;}finally{int c=0; f(++c);}");
  }
}
