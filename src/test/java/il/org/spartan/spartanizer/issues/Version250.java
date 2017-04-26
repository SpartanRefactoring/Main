package il.org.spartan.spartanizer.issues;

import static il.org.spartan.azzert.*;
import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;
import il.org.spartan.*;
import il.org.spartan.spartanizer.tippers.*;

/** * Unit tests for the nesting class Unit test for the containing class. Note
 * our naming convention: a) test methods do not use the redundant "test"
 * prefix. b) test methods begin with the name of the method they check.
 * @author Yossi Gil
 * @since 2014-07-10 */
@SuppressWarnings({ "static-method", "javadoc" })
public final class Version250 {
  @Test public void annotationVanilla() {
    trimmingOf("void f(){}").stays();
    trimmingOf("@a void f(){}").stays();
    trimmingOf("@a({\"a\"})void f(){}")//
        .gives("@a(\"a\")void f(){}");
  }

  @Test public void additionZeroTest_a() {
    trimmingOf("b = a + 0;")//
        .stays();
  }

  @Test public void additionZeroTest_b() {
    trimmingOf("b=0+a;")//
        .stays();
  }

  @Test public void issue070_01() {
    trimmingOf("(double)5")//
        .gives("1.*5");
  }

  @Test public void issue070_02() {
    trimmingOf("(double)4")//
        .gives("1.*4");
  }

  @Test public void issue070_03() {
    trimmingOf("(double)1.2")//
        .gives("1.*1.2");
  }

  @Test public void issue070_04() {
    trimmingOf("(double)'a'")//
        .gives("1.*'a'");
  }

  @Test public void issue070_05() {
    trimmingOf("(double)A")//
        .gives("1.*A");
  }

  @Test public void issue070_06() {
    trimmingOf("(double)a.b")//
        .gives("1.*a.b");
  }

  @Test public void issue070_07() {
    trimmingOf("(double)(double)5")//
        .gives("1.*(double)5")//
        .gives("1.*1.*5");
  }

  @Test public void issue070_08() {
    trimmingOf("(double)((double)5)")//
        .gives("1.*(double)5")//
        .gives("1.*1.*5");
  }

  @Test public void issue070_09() {
    trimmingOf("(double) 2. * (double)5")//
        .gives("(double)5 * (double)2.")//
        .gives("1. * 5 * 1. * 2.")//
        .gives("10.0");
  }

  @Test public void issue070_10() {
    trimmingOf("(double)5 - (double)3")//
        .gives("1.*5-1.*3");
  }

  @Test public void issue070_11() {
    trimmingOf("(double)f + (int)g")//
        .gives("(int)g+(double)f")//
        .gives("(int)g + 1.*f")//
        .gives("1.*f + (int)g")//
        .stays();
  }

  @Test public void issue070_12() {
    trimmingOf("foo((double)18)")//
        .gives("foo(1.*18)");
  }

  @Test public void issue076d() {
    trimmingOf("a * (b + c)")//
        .stays();
  }

  @Test public void issue083a() {
    trimmingOf("if(x.size()>=0) return a;")//
        .gives("if(true) return a;");
  }

  @Test public void issue083b() {
    trimmingOf("if(x.size()<0) return a;")//
        .gives("if(false) return a;");
  }

  @Test public void issue083c() {
    trimmingOf("if(x.size()>0)return a;")//
        .gives("if(!x.isEmpty())return a;");
  }

  @Test public void issue083d() {
    trimmingOf("if(x.size()==1) return a;")//
        .stays();
  }

  @Test public void issue083e() {
    trimmingOf("if(x.size()==2) return a;")//
        .stays();
  }

  @Test public void issue083f() {
    trimmingOf("if(2==x.size()) return a;")//
        .gives("if(x.size()==2) return a;");
  }

  @Test public void issue083g() {
    trimmingOf("if(x.size()==4) return a;")//
        .stays();
  }

  @Test public void issue083h() {
    trimmingOf("if(x.size()==0) return a;")//
        .gives("if(x.isEmpty()) return a;");
  }

  @Test public void issue083i() {
    trimmingOf("if(es.size()>= 2) return a;")//
        .stays();
  }

  @Test public void issue083j() {
    trimmingOf("if(es.size()> 2) return a;")//
        .stays();
  }

  @Test public void issue083k() {
    trimmingOf("if(es.size() <2) return a;")//
        .stays();
  }

  @Test public void issue083l() {
    trimmingOf("uses(ns).size() <= 1")//
        .stays();
  }

  @Test public void issue083m() {
    trimmingOf("if(a.size()>= -3) ++a;")//
        .gives("if(true) ++a;")//
        .gives("++a;");
  }

  @Test public void issue083n() {
    trimmingOf("if(a.size() <= -9) ++a;a+=1;")//
        .gives("if(false) ++a;a+=1;")//
        .gives("{}a+=1;")//
        .gives("a+=1;")//
        .stays();
  }

  @Test public void issue085_86a() {
    trimmingOf("if(true){ x(); } else{ y(); }")//
        .gives("{x();}")//
        .gives("x();");
  }

  @Test public void issue085_86b() {
    trimmingOf("if(false){ x(); } else{ y(); }")//
        .gives("{y();}")//
        .gives("y();");
  }

  @Test public void issue085_86c() {
    trimmingOf("if(false) x(); else y(); ")//
        .gives("y();");
  }

  /** Introduced by Yogi on Tue-Apr-11-12:56:11-IDT-2017 (code automatically in
   * class 'JUnitTestMethodFacotry') */
  @Test public void ifFalseaElseIfFalsebElsec() {
    trimmingOf("if (false) { a(); } else { if (false) b(); else c(); }") //
        .using(new IfTrueOrFalse(), IfStatement.class) //
        .gives("{if(false)b();else c();}") //
        .using(new BlockSingletonEliminate(), Block.class) //
        .gives("if(false)b();else c();") //
        .using(new IfTrueOrFalse(), IfStatement.class) //
        .gives("c();") //
        .stays() //
    ;
  }

  /** Introduced by Yogi on Tue-Apr-11-12:57:36-IDT-2017 (code automatically in
   * class 'JUnitTestMethodFacotry') */
  @Test public void ifFalseaElseIfTruebElsec() {
    trimmingOf("if (false) { a(); } else { if (true) b(); else c(); }") //
        .using(new IfTrueOrFalse(), IfStatement.class) //
        .gives("{if(true)b();else c();}") //
        .using(new BlockSingletonEliminate(), Block.class) //
        .gives("if(true)b();else c();") //
        .using(new IfTrueOrFalse(), IfStatement.class) //
        .gives("b();") //
        .stays() //
    ;
  }

  /** Introduced by Yogi on Tue-Apr-11-12:58:48-IDT-2017 (code automatically in
   * class 'JUnitTestMethodFacotry') */
  @Test public void ifTrueIfTrueaElsebElseIfFalseaElseb() {
    trimmingOf("if (true) { if (true) a(); else b(); } else { if (false) a(); else b(); }") //
        .using(new IfTrueOrFalse(), IfStatement.class) //
        .gives("{if(true)a();else b();}") //
        .using(new BlockSingletonEliminate(), Block.class) //
        .gives("if(true)a();else b();") //
        .using(new IfTrueOrFalse(), IfStatement.class) //
        .gives("a();") //
        .stays() //
    ;
  }

  @Test public void issue085_86g() {
    trimmingOf("if(z==k) x(); else y(); ")//
        .stays();
  }

  @Test public void issue085_86h() {
    trimmingOf("if(5==5) x(); else y(); ")//
        .stays();
  }

  @Test public void issue085_86i() {
    trimmingOf("if(z){ if(true) a(); else b(); } else{ if(false) a(); else b();}")//
        .gives("if(z)if(true) a(); else b();else if(false) a(); else b();").gives("if(z)a(); else b(); ");
  }

  /** Introduced by Yogi on Tue-Apr-11-13:00:24-IDT-2017 (code automatically in
   * class 'JUnitTestMethodFacotry') */
  @Test public void ifFaeaElseIfTruebElsec() {
    trimmingOf("if (false) a(); else { if (true) b(); else c(); }") //
        .using(new IfTrueOrFalse(), IfStatement.class) //
        .gives("{if(true)b();else c();}") //
        .using(new BlockSingletonEliminate(), Block.class) //
        .gives("if(true)b();else c();") //
        .using(new IfTrueOrFalse(), IfStatement.class) //
        .gives("b();") //
        .stays() //
    ;
  }

  @Test public void issue085_86k() {
    trimmingOf("if(false){ if(true) a(); else b(); } else c();")//
        .gives("c();");
  }

  @Test public void issue085_86l() {
    trimmingOf("if(false)c();else {if(true) a(); else b(); } ")//
        .gives("{if(true)a();else b();}")//
        .gives("if(true)a();else b();")//
        .gives("a();") //
        .stays();
  }

  @Test public void issue086_1() {
    trimmingOf("if(false)c();int a;")//
        .gives("{}")//
        .gives("")//
        .stays();
  }

  @Test public void issue086_2() {
    trimmingOf("if(false) {c();b();a();}")//
        .gives("{}")//
        .gives("")//
        .stays();
  }

  @Test public void issue086_3() {
    trimmingOf("if(false) {c();b();a();}")//
        .gives("{}")//
        .gives("")//
        .stays();
  }

  @Test public void issue086_4() {
    trimmingOf("if(false) {c();b();a();}")//
        .gives("{}")//
        .gives("")//
        .stays();
  }

  @Test public void issue086_5() {
    trimmingOf("if(false) {c();b();a();}")//
        .gives("{}")//
        .gives("")//
        .stays();
  }

  @Test public void issue199() {
    trimmingOf("void f() { if (a == b) { f(); return; } g();} ").gives("void f(){if(a==b){f();}else g();}")//
        .gives("void f(){if(a==b)f();else g();}")//
        .stays();
  }

  @Test public void issue199a() {
    trimmingOf("void f() { if (a == b) return; g();} ")//
        .gives("void f(){if(a==b);else g();}").gives("void f(){if(a!=b) g();}")//
        .stays();
  }

  @Test public void issue207() {
    trimmingOf("size() == 0")//
        .stays();
  }

  @Test public void issue218() {
    trimmingOf("(long)(long)2")//
        .gives("1L*(long)2")//
        .gives("1L*1L*2")//
        .gives("2L")//
        .stays();
  }

  @Test public void issue218a() {
    trimmingOf("(long)(long)2")//
        .gives("1L*(long)2")//
        .gives("1L*1L*2")//
        .gives("2L")//
        .stays();
  }

  @Test public void issue218x() {
    trimmingOf("(long)1L*2")//
        .gives("2*(long)1L")//
        .gives("2*1L*1L")//
        .gives("2L")//
        .stays();
  }

  @Test public void issue237() {
    trimmingOf("class X {final int __ = 0;}")//
        .stays();
    trimmingOf("class X {final boolean __ = false;}")//
        .stays();
    trimmingOf("class X {final double __ = 0.0;}")//
        .stays();
    trimmingOf("class X {final Object __ = null;}")//
        .stays();
  }

  @Test public void issue241a() {
    trimmingOf("interface x { int a; }")//
        .stays();
  }

  @Test public void issue241b() {
    trimmingOf("interface x { static int a; }")//
        .gives("interface x { int a; }")//
        .stays();
  }

  @Test public void issue243() {
    trimmingOf("interface x { int a = 0; boolean b = 0; byte ba = 0; short s = 0; long s = 0; long s1 = 2; "
        + "double d = 0.0; float f = 0.0; float f1 = 1;}")//
            .stays();
  }

  @Test public void simpleForLoop() {
    trimmingOf("for (final Integer i: range.to(100)) sum+=i;")//
        .gives("for (final Integer ¢: range.to(100)) sum+=¢;")//
        .stays();
  }

  @Test public void test_b() {
    azzert.that("studies".replaceAll("ies$", "y").replaceAll("es$", "").replaceAll("s$", ""), is("study"));
  }

  @Test public void test_c() {
    azzert.that("studes".replaceAll("ies$", "y").replaceAll("es$", "").replaceAll("s$", ""), is("stud"));
  }

  @Test public void test_d() {
    azzert.that("studs".replaceAll("ies$", "y").replaceAll("es$", "").replaceAll("s$", ""), is("stud"));
  }
}
