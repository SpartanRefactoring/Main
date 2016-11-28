package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;
import org.junit.*;

/**
 * @author  Dor Ma'ayan
 * @since  20-11-2016 
 */
@Ignore
@SuppressWarnings("static-method")
public class Issue188 {
  @Test public void test0() {
    trimmingOf("try{f();}catch(Exception e){return a;}catch(ExceptionNull e){return a;}finally{return b;}")
        .gives("try{f();}catch(ExceptionNull|Exception e){return a;}finally{return b;}").stays();
  }

  @Test public void test1() {
    trimmingOf("try{f();}catch(Exception e){int a;return a;}catch(ExceptionNull e){int a;return a;}finally{return b;}")
        .gives("try{f();}catch(ExceptionNull|Exception e){int a;return a;}finally{return b;}").stays();
  }

  @Test public void test2() {
    trimmingOf("try{f();}catch(Exception e){int a;return a;}catch(Exceptiono e){int y;}catch(ExceptionNull e){int a;return a;}finally{return b;}")
        .gives("try{f();}catch(ExceptionNull|Exception e){int a;return a;}catch(Exceptiono e){int y;}finally{return b;}").stays();
  }

  @Test public void test3() {
    trimmingOf(
        "try{f();}catch(Exception e){int a;return a;}catch(Exceptiono e){int a;return a;}catch(ExceptionNull e){int a;return a;}finally{return b;}")
            .gives("try{f();}catch(Exceptiono|Exception e){int a;return a;}catch(ExceptionNull e){int a;return a;}finally{return b;}")
            .gives("try{f();}catch(ExceptionNull|Exceptiono|Exception e){int a;return a;}finally{return b;}").stays();
  }

  @Test public void test4() {
    trimmingOf(
        "try{int y;}catch(Exception e){int a;return a;}catch(Exceptiono e){int a;return a;}catch(ExceptionNull e){int a;return a;}finally{return b;}")
            .gives("try{int y;}catch(Exceptiono|Exception e){int a;return a;}catch(ExceptionNull e){int a;return a;}finally{return b;}")
            .gives("try{int y;}catch(ExceptionNull|Exceptiono|Exception e){int a;return a;}finally{return b;}").stays();
  }

  @Test public void test5() {
    trimmingOf("try{int y;}catch(Exception e){int a;return a;}catch(Exceptiono e){int a;}catch(ExceptionNull e){int a;return a;}finally{return b;}")
        .gives("try{int y;}catch(ExceptionNull|Exception e){int a;return a;}catch(Exceptiono e){int a;}finally{return b;}").stays();
  }
}
