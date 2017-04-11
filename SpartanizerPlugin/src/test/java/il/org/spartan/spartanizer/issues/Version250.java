package il.org.spartan.spartanizer.issues;

import static il.org.spartan.azzert.*;
import static il.org.spartan.spartanizer.testing.TestsUtilsTrimmer.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.tippers.*;

/** * Unit tests for the nesting class Unit test for the containing class. Note
 * our naming convention: a) test methods do not use the redundant "test"
 * prefix. b) test methods begin with the name of the method they check.
 * @author Yossi Gil
 * @since 2014-07-10 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SuppressWarnings({ "static-method", "javadoc" })
public final class Version250 {
  @Test public void annotationVanilla() {
    topDownTrimming("void f(){}").stays();
    topDownTrimming("@a void f(){}").stays();
    topDownTrimming("@a({\"a\"})void f(){}")//
        .gives("@a(\"a\")void f(){}");
  }

  @Test public void additionZeroTest_a() {
    topDownTrimming("b = a + 0;")//
        .stays();
  }

  @Test public void additionZeroTest_b() {
    topDownTrimming("b=0+a;")//
        .stays();
  }

  @Test public void issue070_01() {
    topDownTrimming("(double)5")//
        .gives("1.*5");
  }

  @Test public void issue070_02() {
    topDownTrimming("(double)4")//
        .gives("1.*4");
  }

  @Test public void issue070_03() {
    topDownTrimming("(double)1.2")//
        .gives("1.*1.2");
  }

  @Test public void issue070_04() {
    topDownTrimming("(double)'a'")//
        .gives("1.*'a'");
  }

  @Test public void issue070_05() {
    topDownTrimming("(double)A")//
        .gives("1.*A");
  }

  @Test public void issue070_06() {
    topDownTrimming("(double)a.b")//
        .gives("1.*a.b");
  }

  @Test public void issue070_07() {
    topDownTrimming("(double)(double)5")//
        .gives("1.*(double)5")//
        .gives("1.*1.*5");
  }

  @Test public void issue070_08() {
    topDownTrimming("(double)((double)5)")//
        .gives("1.*(double)5")//
        .gives("1.*1.*5");
  }

  @Test public void issue070_09() {
    topDownTrimming("(double) 2. * (double)5")//
        .gives("(double)5 * (double)2.")//
        .gives("1. * 5 * 1. * 2.")//
        .gives("10.0");
  }

  @Test public void issue070_10() {
    topDownTrimming("(double)5 - (double)3")//
        .gives("1.*5-1.*3");
  }

  @Test public void issue070_11() {
    topDownTrimming("(double)f + (int)g")//
        .gives("(int)g+(double)f")//
        .gives("(int)g + 1.*f")//
        .gives("1.*f + (int)g")//
        .stays();
  }

  @Test public void issue070_12() {
    topDownTrimming("foo((double)18)")//
        .gives("foo(1.*18)");
  }

  @Test public void issue076d() {
    topDownTrimming("a * (b + c)")//
        .stays();
  }

  @Test public void issue083a() {
    topDownTrimming("if(x.size()>=0) return a;")//
        .gives("if(true) return a;");
  }

  @Test public void issue083b() {
    topDownTrimming("if(x.size()<0) return a;")//
        .gives("if(false) return a;");
  }

  @Test public void issue083c() {
    topDownTrimming("if(x.size()>0)return a;")//
        .gives("if(!x.isEmpty())return a;");
  }

  @Test public void issue083d() {
    topDownTrimming("if(x.size()==1) return a;")//
        .stays();
  }

  @Test public void issue083e() {
    topDownTrimming("if(x.size()==2) return a;")//
        .stays();
  }

  @Test public void issue083f() {
    topDownTrimming("if(2==x.size()) return a;")//
        .gives("if(x.size()==2) return a;");
  }

  @Test public void issue083g() {
    topDownTrimming("if(x.size()==4) return a;")//
        .stays();
  }

  @Test public void issue083h() {
    topDownTrimming("if(x.size()==0) return a;")//
        .gives("if(x.isEmpty()) return a;");
  }

  @Test public void issue083i() {
    topDownTrimming("if(es.size()>= 2) return a;")//
        .stays();
  }

  @Test public void issue083j() {
    topDownTrimming("if(es.size()> 2) return a;")//
        .stays();
  }

  @Test public void issue083k() {
    topDownTrimming("if(es.size() <2) return a;")//
        .stays();
  }

  @Test public void issue083l() {
    topDownTrimming("uses(ns).size() <= 1")//
        .stays();
  }

  @Test public void issue083m() {
    topDownTrimming("if(a.size()>= -3) ++a;")//
        .gives("if(true) ++a;")//
        .gives("++a;");
  }

  @Test public void issue083n() {
    topDownTrimming("if(a.size() <= -9) ++a;a+=1;")//
        .gives("if(false) ++a;a+=1;")//
        .gives("{}a+=1;")//
        .gives("a+=1;")//
        .stays();
  }

  @Test public void issue085_86a() {
    topDownTrimming("if(true){ x(); } else{ y(); }")//
        .gives("{x();}")//
        .gives("x();");
  }

  @Test public void issue085_86b() {
    topDownTrimming("if(false){ x(); } else{ y(); }")//
        .gives("{y();}")//
        .gives("y();");
  }

  @Test public void issue085_86c() {
    topDownTrimming("if(false) x(); else y(); ")//
        .gives("y();");
  }

  /** Introduced by Yogi on Tue-Apr-11-12:56:11-IDT-2017 (code automatically in
   * class 'JUnitTestMethodFacotry') */
  @Test public void ifFalseaElseIfFalsebElsec() {
    topDownTrimming("if (false) { a(); } else { if (false) b(); else c(); }") //
        .using(IfStatement.class, new IfTrueOrFalse()) //
        .gives("{if(false)b();else c();}") //
        .using(Block.class, new BlockSingleton()) //
        .gives("if(false)b();else c();") //
        .using(IfStatement.class, new IfTrueOrFalse()) //
        .gives("c();") //
        .stays() //
    ;
  }

  /** Introduced by Yogi on Tue-Apr-11-12:57:36-IDT-2017 (code automatically in
   * class 'JUnitTestMethodFacotry') */
  @Test public void ifFalseaElseIfTruebElsec() {
    topDownTrimming("if (false) { a(); } else { if (true) b(); else c(); }") //
        .using(IfStatement.class, new IfTrueOrFalse()) //
        .gives("{if(true)b();else c();}") //
        .using(Block.class, new BlockSingleton()) //
        .gives("if(true)b();else c();") //
        .using(IfStatement.class, new IfTrueOrFalse()) //
        .gives("b();") //
        .stays() //
    ;
  }

  /** Introduced by Yogi on Tue-Apr-11-12:58:48-IDT-2017 (code automatically in
   * class 'JUnitTestMethodFacotry') */
  @Test public void ifTrueIfTrueaElsebElseIfFalseaElseb() {
    topDownTrimming("if (true) { if (true) a(); else b(); } else { if (false) a(); else b(); }") //
        .using(IfStatement.class, new IfTrueOrFalse()) //
        .gives("{if(true)a();else b();}") //
        .using(Block.class, new BlockSingleton()) //
        .gives("if(true)a();else b();") //
        .using(IfStatement.class, new IfTrueOrFalse()) //
        .gives("a();") //
        .stays() //
    ;
  }

  @Test public void issue085_86g() {
    topDownTrimming("if(z==k) x(); else y(); ")//
        .stays();
  }

  @Test public void issue085_86h() {
    topDownTrimming("if(5==5) x(); else y(); ")//
        .stays();
  }

  @Test public void issue085_86i() {
    topDownTrimming("if(z){ if(true) a(); else b(); } else{ if(false) a(); else b();}")//
        .gives("if(z)if(true) a(); else b();else if(false) a(); else b();").gives("if(z)a(); else b(); ");
  }

  /** Introduced by Yogi on Tue-Apr-11-13:00:24-IDT-2017 (code automatically in
   * class 'JUnitTestMethodFacotry') */
  @Test public void ifFaeaElseIfTruebElsec() {
    topDownTrimming("if (false) a(); else { if (true) b(); else c(); }") //
        .using(IfStatement.class, new IfTrueOrFalse()) //
        .gives("{if(true)b();else c();}") //
        .using(Block.class, new BlockSingleton()) //
        .gives("if(true)b();else c();") //
        .using(IfStatement.class, new IfTrueOrFalse()) //
        .gives("b();") //
        .stays() //
    ;
  }

  @Test public void issue085_86k() {
    topDownTrimming("if(false){ if(true) a(); else b(); } else c();")//
        .gives("c();");
  }

  @Test public void issue085_86l() {
    topDownTrimming("if(false)c();else {if(true) a(); else b(); } ")//
        .gives("{if(true)a();else b();}")//
        .gives("if(true)a();else b();")//
        .gives("a();") //
        .stays();
  }

  @Test public void issue086_1() {
    topDownTrimming("if(false)c();int a;")//
        .gives("{}")//
        .gives("")//
        .stays();
  }

  @Test public void issue086_2() {
    topDownTrimming("if(false) {c();b();a();}")//
        .gives("{}")//
        .gives("")//
        .stays();
  }

  @Test public void issue086_3() {
    topDownTrimming("if(false) {c();b();a();}")//
        .gives("{}")//
        .gives("")//
        .stays();
  }

  @Test public void issue086_4() {
    topDownTrimming("if(false) {c();b();a();}")//
        .gives("{}")//
        .gives("")//
        .stays();
  }

  @Test public void issue086_5() {
    topDownTrimming("if(false) {c();b();a();}")//
        .gives("{}")//
        .gives("")//
        .stays();
  }

  @Test public void issue199() {
    topDownTrimming("void f() { if (a == b) { f(); return; } g();} ").gives("void f(){if(a==b){f();}else g();}")//
        .gives("void f(){if(a==b)f();else g();}")//
        .stays();
  }

  @Test public void issue199a() {
    topDownTrimming("void f() { if (a == b) return; g();} ")//
        .gives("void f(){if(a==b);else g();}").gives("void f(){if(a!=b) g();}")//
        .stays();
  }

  @Test public void issue207() {
    topDownTrimming("size() == 0")//
        .stays();
  }

  @Test public void issue218() {
    topDownTrimming("(long)(long)2")//
        .gives("1L*(long)2")//
        .gives("1L*1L*2")//
        .gives("2L")//
        .stays();
  }

  @Test public void issue218a() {
    topDownTrimming("(long)(long)2")//
        .gives("1L*(long)2")//
        .gives("1L*1L*2")//
        .gives("2L")//
        .stays();
  }

  @Test public void issue218x() {
    topDownTrimming("(long)1L*2")//
        .gives("2*(long)1L")//
        .gives("2*1L*1L")//
        .gives("2L")//
        .stays();
  }

  @Test public void issue237() {
    topDownTrimming("class X {final int __ = 0;}")//
        .stays();
    topDownTrimming("class X {final boolean __ = false;}")//
        .stays();
    topDownTrimming("class X {final double __ = 0.0;}")//
        .stays();
    topDownTrimming("class X {final Object __ = null;}")//
        .stays();
  }

  @Test public void issue241a() {
    topDownTrimming("interface x { int a; }")//
        .stays();
  }

  @Test public void issue241b() {
    topDownTrimming("interface x { static int a; }")//
        .gives("interface x { int a; }")//
        .stays();
  }

  @Test public void issue243() {
    topDownTrimming("interface x { int a = 0; boolean b = 0; byte ba = 0; short s = 0; long s = 0; long s1 = 2; "
        + "double d = 0.0; float f = 0.0; float f1 = 1;}")//
            .stays();
  }

  @Test public void simpleForLoop() {
    topDownTrimming("for (final Integer i: range.to(100)) sum+=i;")//
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
