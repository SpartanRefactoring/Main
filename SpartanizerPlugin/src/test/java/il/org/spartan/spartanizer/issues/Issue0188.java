package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import org.junit.*;

/** Check that the tipper of unifying identical catch blocks is working properly
 * @author Dor Ma'ayan
 * @since 20-11-2016 */
@SuppressWarnings("static-method")
public class Issue0188 {
  @Test public void test0() {
    trimmingOf("try{f();}catch(E e){return a;}catch(ExceptionNull e){return a;}finally{return b;}")
        .gives("try{f();}catch(ExceptionNull|E e){return a;}finally{return b;}")//
        .stays();
  }
  @Test public void test1() {
    trimmingOf("try{f();}catch(E e){int a;return a;}catch(ExceptionNull e){int a;return a;}finally{return b;}")
        .gives("try{f();}catch(ExceptionNull|E e){int a;return a;}finally{return b;}")//
        .stays();
  }
  @Test public void test2() {
    trimmingOf("try{f();}catch(E e){int a;return a;}catch(O e){int y;}catch(ExceptionNull e){int a=3;return a;}finally{return b;}")
        .gives("try{f();}catch(E e){int a;return a;}catch(O e){}catch(ExceptionNull e){return 3;}finally{return b;}")//
        .stays();
  }
  @Test public void test3() {
    trimmingOf("try{f();}catch(E e){int a;return a;}catch(O e){int a;return a;}catch(ExceptionNull e){int a;return a;}finally{return b;}")
        .gives("try{f();}catch(O|E e){int a;return a;}catch(ExceptionNull e){int a;return a;}finally{return b;}")
        .gives("try{f();}catch(ExceptionNull|O|E e){int a;return a;}finally{return b;}")//
        .stays();
  }
  @Test public void test4() {
    trimmingOf("try{int y;}catch(E e){int a;return a;}catch(O e){int a;return a;}catch(ExceptionNull e){int a;return a;}finally{return b;}")
        .gives("try{int y;}catch(O|E e){int a;return a;}catch(ExceptionNull e){int a;return a;}finally{return b;}")
        .gives("try{int y;}catch(ExceptionNull|O|E e){int a;return a;}finally{return b;}")//
        .gives("try{}catch(ExceptionNull|O|E e){int a;return a;}finally{return b;}")//
        .gives("{return b;}")//
        .gives("return b;")//
        .stays();
  }
  @Test public void test5() {
    trimmingOf("try{int y;}catch(E e){int a = 3;return a;}catch(O e){int a;}catch(N e){int a;return a;}finally{return b;}")
        .gives("try{}catch(E e){return 3;}catch(O e){}catch(N e){int a;return a;}finally{return b;}")//
        .gives("{return b;}")//
        .gives("return b;")//
        .stays();
  }
}
