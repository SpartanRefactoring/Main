package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;
import org.junit.*;

/** @author Dor Ma'ayan
 * @since 22-11-2016 */
@SuppressWarnings("static-method")
@Ignore
public class Issue308 {
  @Test public void test0() {
    trimmingOf("try{int a;int b; return a+b;}catch(Exception e){return e;}")//
        .gives("try{int a;int b; return a+b;}catch(Exception ¢){return ¢;}");
  }

  @Test public void test1() {
    trimmingOf("try{int a;int b; return a+b;}catch(Exception e){e.toString();return e;}")//
        .gives("try{int a;int b; return a+b;}catch(Exception ¢){¢.toString();return ¢;}");
  }

  @Test public void test2() {
    trimmingOf("try{int a;int b; return a+b;}catch(Exception e){return e;}finally{int c=0;}")//
        .gives("try{int a;int b; return a+b;}catch(Exception ¢){return ¢;}finally{int c=0;}");
  }
}
