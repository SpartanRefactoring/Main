package il.org.spartan.refactoring.wring;

import static il.org.spartan.azzert.*;
import static il.org.spartan.refactoring.utils.Funcs.*;
import static il.org.spartan.refactoring.utils.Is.*;
import static il.org.spartan.refactoring.utils.Restructure.*;
import static il.org.spartan.refactoring.wring.trimming.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.*;
import il.org.spartan.refactoring.utils.*;

/** * Unit tests for the nesting class Unit test for the containing class. Note
 * our naming convention: a) test methods do not use the redundant "test"
 * prefix. b) test methods begin with the name of the method they check.
 * @author Yossi Gil
 * @since 2014-07-10 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "static-method", "javadoc" }) //
public class TrimmerTest250 {
  @Test public void issue50_Constructors1() {
    trimming.of("public class ClassTest {\n"//
        + "public  ClassTest(){}\n"//
        + "}").stays();
  }

  @Test public void issue50_EnumInInterface1() {
    trimming
        .of("public interface Int1 {\n"//
            + "static enum Day {\n"//
            + "SUNDAY, MONDAY\n"//
            + "}"//
            + "}")
        .to("public interface Int1 {\n"//
            + "enum Day {\n"//
            + "SUNDAY, MONDAY\n"//
            + "}" + "}");
  }

  @Test public void issue50_Enums() {
    trimming
        .of("public class ClassTest {\n"//
            + "static enum Day {\n"//
            + "SUNDAY, MONDAY\n"//
            + "}")
        .to("public class ClassTest {\n"//
            + "enum Day {\n"//
            + "SUNDAY, MONDAY\n"//
            + "}");
  }

  @Test public void issue50_EnumsOnlyRightModifierRemoved() {
    trimming
        .of("public class ClassTest {\n"//
            + "private static enum Day {\n"//
            + "SUNDAY, MONDAY\n"//
            + "}")
        .to("public class ClassTest {\n"//
            + "private enum Day {\n"//
            + "SUNDAY, MONDAY\n"//
            + "}");
  }

  @Test public void issue50_FinalClassMethods() {
    trimming
        .of("final class ClassTest {\n"//
            + "final void remove();\n"//
            + "}")
        .to("final class ClassTest {\n"//
            + "void remove();\n "//
            + "}");
  }

  @Test public void issue50_FinalClassMethodsOnlyRightModifierRemoved() {
    trimming
        .of("final class ClassTest {\n"//
            + "public final void remove();\n"//
            + "}")
        .to("final class ClassTest {\n"//
            + "public void remove();\n "//
            + "}");
  }

  @Test public void issue50_inEnumMember() {
    trimming.of(//
        "enum A {; final void f() {} public final void g() {} }"//
    ).stays();
  }

  @Test public void issue50_inEnumMemberComplex() {
    trimming.of(//
        "enum A { a1 {{ f(); } \n" + //
            "protected final void f() {g();}  \n" + //
            "public final void g() {h();}  \n" + //
            "private final void h() {i();}   \n" + //
            "final void i() {f();}  \n" + //
            "}, a2 {{ f(); } \n" + //
            "final protected void f() {g();}  \n" + //
            "final void g() {h();}  \n" + //
            "final private void h() {i();}  \n" + //
            "final protected void i() {f();}  \n" + //
            "};\n" + //
            "protected abstract void f();\n" + //
            "protected void ia() {}\n" + //
            "void i() {}\n" + //
            "} \n"//
    ).to("enum A { a1 {{ f(); } \n" + //
        "void f() {g();}  \n" + //
        "public void g() {h();}  \n" + //
        "void h() {i();}   \n" + //
        "void i() {f();}  \n" + //
        "}, a2 {{ f(); } \n" + //
        "void f() {g();}  \n" + //
        "void g() {h();}  \n" + //
        "void h() {i();}  \n" + //
        "void i() {f();}  \n" + //
        "};\n" + //
        "abstract void f();\n" + //
        "void ia() {}\n" + //
        "void i() {}\n" + //
        "} \n"//
    );
  }

  @Test public void issue50_InterfaceMethods1() {
    trimming
        .of("public interface Int1 {\n"//
            + "public void add();\n"//
            + "void remove()\n; "//
            + "}")
        .to("public interface Int1 {\n"//
            + "void add();\n"//
            + "void remove()\n; "//
            + "}");
  }

  @Test public void issue50_InterfaceMethods2() {
    trimming
        .of("public interface Int1 {\n"//
            + "public abstract void add();\n"//
            + "abstract void remove()\n; "//
            + "}")
        .to("public interface Int1 {\n"//
            + "void add();\n"//
            + "void remove()\n; "//
            + "}");
  }

  @Test public void issue50_InterfaceMethods3() {
    trimming
        .of("public interface Int1 {\n"//
            + "abstract void add();\n"//
            + "void remove()\n; "//
            + "}")
        .to("public interface Int1 {\n"//
            + "void add();\n"//
            + "void remove()\n; "//
            + "}");
  }

  @Test public void issue50_SimpleDontWorking() {
    trimming.of("interface a"//
        + "{}").stays();
  }

  @Test public void issue50_SimpleWorking1() {
    trimming.of("abstract abstract interface a"//
        + "{}").to("interface a {}");
  }

  @Test public void issue50_SimpleWorking2() {
    trimming.of("abstract interface a"//
        + "{}").to("interface a {}");
  }

  @Test public void issue50a() {
    trimming.of("abstract interface a {}")//
        .to("interface a {}");//
  }

  @Test public void issue50b() {
    trimming.of("abstract static interface a {}")//
        .to("interface a {}");//
  }

  @Test public void issue50c() {
    trimming.of("static abstract interface a {}")//
        .to("interface a {}");//
  }

  @Test public void issue50d() {
    trimming.of("static interface a {}")//
        .to("interface a {}");//
  }

  @Test public void issue50e() {
    trimming.of("enum a {a,b}")//
        .stays();//
  }

  @Test public void issue50e1() {
    trimming.of("enum a {a}")//
        .stays();//
  }

  @Test public void issue50e2() {
    trimming.of("enum a {}")//
        .stays();//
  }

  @Test public void issue50f() {
    trimming.of("static enum a {a, b}")//
        .to("enum a {a, b}");//
  }

  @Test public void issue50g() {
    trimming.of("static abstract enum a {x,y,z; void f() {}}")//
        .to("enum a {x,y,z; void f() {}}");//
  }

  @Test public void issue50h() {
    trimming.of("static abstract final enum a {x,y,z; void f() {}}")//
        .to("enum a {x,y,z; void f() {}}");//
  }

  @Test public void issue70_01() {
    trimming.of("(double)5").to("1.*5");
  }

  @Test public void issue70_02() {
    trimming.of("(double)4").to("1.*4");
  }

  @Test public void issue70_03() {
    trimming.of("(double)1.2").to("1.*1.2");
  }

  @Test public void issue70_04() {
    trimming.of("(double)'a'").to("1.*'a'");
  }

  @Test public void issue70_05() {
    trimming.of("(double)A").to("1.*A");
  }

  @Test public void issue70_06() {
    trimming.of("(double)a.b").to("1.*a.b");
  }

  @Test public void issue70_07() {
    trimming.of("(double)(double)5").to("1.*(double)5").to("1.*1.*5");
  }

  @Test public void issue70_08() {
    trimming.of("(double)((double)5)").to("1.*(double)5").to("1.*1.*5");
  }

  @Test public void issue70_09() {
    trimming.of("(double) 2. * (double)5")//
        .to("(double)5 * (double)2.") //
        .to("1. * 5  * 1. * 2.")//
        .stays();
  }

  @Test public void issue70_10() {
    trimming.of("(double)5 - (double)3").to("1.*5-1.*3");
  }

  @Test public void issue70_11() {
    trimming.of("(double)f + (int)g").to("1.*f + (int)g");
  }

  @Test public void issue70_12() {
    trimming.of("foo((double)18)").to("foo(1.*18)");
  }

  @Test public void issue71a() {
    trimming.of("1*a").to("a");
  }

  @Test public void issue71b() {
    trimming.of("a*1").to("a");
  }

  @Test public void issue71c() {
    trimming.of("1*a*b").to("a*b");
  }

  @Test public void issue71d() {
    trimming.of("1*a*1*b").to("a*b");
  }

  @Test public void issue71e() {
    trimming.of("a*1*b*1").to("a*b");
  }

  @Test public void issue71f() {
    trimming.of("1.0*a").stays();
  }

  @Test public void issue71g() {
    trimming.of("a*2").to("2*a");
  }

  @Test public void issue71h() {
    trimming.of("1*1").to("1");
  }

  @Test public void issue71i() {
    trimming.of("1*1*1").to("1");
  }

  @Test public void issue71j() {
    trimming.of("1*1*1*1*1.0").to("1.0");
  }

  @Test public void issue71k() {
    trimming.of("-1*1*1").to("-1");
  }

  @Test public void issue71l() {
    trimming.of("1*1*-1*-1").to("1*1*1*1").to("1");
  }

  @Test public void issue71m() {
    trimming.of("1*1*-1*-1*-1*1*-1").to("1*1*1*1*1*1*1").to("1");
  }

  @Test public void issue71n() {
    trimming.of("1*1").to("1");
  }

  @Test public void issue71o() {
    trimming.of("(1)*((a))").to("a");
  }

  @Test public void issue71p() {
    trimming.of("((1)*((a)))").to("(a)");
  }

  @Test public void issue71q() {
    trimming.of("1L*1").to("1L");
  }

  @Test public void issue71r() {
    trimming.of("1L*a").stays();
  }

  @Test public void issue72ma() {
    final String s = "0-x";
    final InfixExpression i = Into.i(s);
    azzert.that(i, iz(s));
    azzert.that(left(i), iz("0"));
    azzert.that(right(i), iz("x"));
    azzert.nay(i.hasExtendedOperands());
    azzert.aye(isLiteralZero(left(i)));
    azzert.nay(isLiteralZero(right(i)));
    azzert.that(minus(left(i)), iz("0"));
    azzert.that(minus(right(i)), iz("-x"));
    trimming.of(s).to("-x");
  }

  @Test public void issue72mb() {
    trimming.of("x-0").to("x");
  }

  @Test public void issue72mc() {
    trimming.of("x-0-y").to("x-y").stays();
  }

  @Test public void issue72md1() {
    trimming.of("0-x-0").to("-x-0").to("-x").stays();
  }

  @Test public void issue72md2() {
    trimming.of("0-x-0-y").to("-x-0-y").to("-x-y").stays();
  }

  @Test public void issue72md3() {
    trimming.of("0-x-0-y-0-z-0-0")//
        .to("-x-0-y-0-z-0-0")//
        .to("-x-y-0-z-0-0")//
        .to("-x-y-z-0-0")//
        .to("-x-y-z-0")//
        .to("-x-y-z")//
        .stays();
  }

  @Test public void issue72me() {
    trimming.of("0-(x-0)").to("-(x-0)").to("-(x)").stays();
  }

  @Test public void issue72me1() {
    azzert.nay(Is.negative(Into.e("0")));
  }

  @Test public void issue72me2() {
    azzert.aye(Is.negative(Into.e("-1")));
    azzert.nay(Is.negative(Into.e("+1")));
    azzert.nay(Is.negative(Into.e("1")));
  }

  @Test public void issue72me3() {
    azzert.aye(Is.negative(Into.e("-x")));
    azzert.nay(Is.negative(Into.e("+x")));
    azzert.nay(Is.negative(Into.e("x")));
  }

  @Test public void issue72meA() {
    trimming.of("(x-0)").to("(x)").stays();
  }

  @Test public void issue72mf1() {
    trimming.of("0-(x-y)").to("-(x-y)").stays();
  }

  @Test public void issue72mf1A() {
    trimming.of("0-(x-0)")//
        .to("-(x-0)")//
        .to("-(x)") //
        .stays();
  }

  @Test public void issue72mf1B() {
    azzert.aye(Is.isSimple(Into.e("x")));
    trimming.of("-(x-0)")//
        .to("-(x)")//
        .stays();
  }

  @Test public void issue72mg() {
    trimming.of("(x-0)-0").to("(x)").stays();
  }

  @Test public void issue72mg1() {
    trimming.of("-(x-0)-0").to("-(x)").stays();
  }

  @Test public void issue72mh() {
    trimming.of("x-0-y").to("x-y").stays();
  }

  @Test public void issue72mi() {
    trimming.of("0-x-0-y-0-z-0")//
        .to("-x-0-y-0-z-0")//
        .to("-x-y-0-z-0")//
        .to("-x-y-z-0")//
        .to("-x-y-z")//
        .stays();
  }

  @Test public void issue72mj() {
    trimming.of("0-0").to("0");
  }

  @Test public void issue72pa() {
    trimming.of("x+0").to("x");
  }

  @Test public void issue72pb() {
    trimming.of("0+x").to("x");
  }

  @Test public void issue72pc() {
    trimming.of("0+x").to("x");
  }

  @Test public void issue72pd() {
    trimming.of("0+x+0").to("x").stays();
  }

  @Test public void issue72pe() {
    trimming.of("x+0+x").to("x+x").stays();
  }

  @Test public void issue72pf() {
    trimming.of("x+0+x+0+0+y+0+0+0+0+z+0+h+0").to("x+x+y+z+h").stays();
  }

  @Test public void issue72pg() {
    trimming.of("0+(x+y)").to("0+x+y").to("x+y").stays();
  }

  @Test public void issue72ph() {
    trimming.of("0+((x+y)+0+(z+h))+0")//
        .to("0+x+y+0+z+h+0")//
        .to("x+y+z+h")//
        .stays();
  }

  @Test public void issue72pi() {
    trimming.of("0+(0+x+y+(4+0))").to("0+0+x+y+4+0").to("x+y+4").stays();
  }

  @Test public void issue75a() {
    trimming.of("int i = 0").stays();
  }

  @Test public void issue75b() {
    trimming.of("int i = +1;").to("int i = 1;");
  }

  @Test public void issue75c() {
    trimming.of("int i = +a;").to("int i = a;");
  }

  @Test public void issue75d() {
    trimming.of("+ 0").to("0");
  }

  @Test public void issue75e() {
    trimming.of("a = +0").to("a = 0");
  }

  @Test public void issue75f() {
    trimming.of("a = 1+0").to("a = 1");
  }

  @Test public void issue75g() {
    trimming.of("i=0").stays();
  }

  @Test public void issue75h() {
    trimming.of("int i; i = +0;").to("int i = +0;").to("int i=0;");
  }

  @Test public void issue75i() {
    trimming.of("+0").to("0");
  }

  @Test public void issue75i0() {
    trimming.of("-+-+2").to("--+2");
  }

  @Test public void issue75i1() {
    trimming.of("+0").to("0");
  }

  @Test public void issue75i2() {
    trimming.of("+1").to("1");
  }

  @Test public void issue75i3() {
    trimming.of("+-1").to("-1");
  }

  @Test public void issue75i4() {
    trimming.of("+1.0").to("1.0");
  }

  @Test public void issue75i5() {
    trimming.of("+'0'").to("'0'");
  }

  @Test public void issue75i6() {
    trimming.of("+1L").to("1L");
  }

  @Test public void issue75i7() {
    trimming.of("+0F").to("0F");
  }

  @Test public void issue75i8() {
    trimming.of("+0L").to("0L");
  }

  @Test public void issue75il() {
    trimming.of("+(a+b)").to("a+b");
  }

  @Test public void issue75j() {
    trimming.of("+1E3").to("1E3");
  }

  @Test public void issue75k() {
    trimming.of("(+(+(+x)))").to("(x)");
  }

  @Test public void issue75m() {
    trimming.of("+ + + i").to("i");
  }

  @Test public void issue75n() {
    trimming.of("(2*+(a+b))").to("(2*(a+b))");
  }

  @Test public void issue76a() {
    trimming.of("a*b + a*c").to("a*(b+c)");
  }

  @Test public void issue76b() {
    trimming.of("b*a + c*a").to("a*(b+c)");
  }

  @Test public void issue76c() {
    trimming.of("b*a + c*a + d*a").to("a*(b+c+d)");
  }

  @Test public void issue82a() {
    trimming.of("(long)5").to("1L*5");
  }

  @Test public void issue82b() {
    trimming.of("(long)a").to("1L*a");
  }

  @Test public void issue82c() {
    trimming.of("(long)(long)a").to("1L*(long)a").to("1L*1L*a");
  }

  @Test public void issue82d() {
    trimming.of("(long)a*(long)b").to("1L*a*1L*b");
  }

  @Test public void issue82e() {
    trimming.of("(double)(long)a").to("1.*(long)a").to("1.*1L*a");
  }

  @Test public void issue85_86a() {
    trimming.of("if(true){   \n" + "x(); }   \n" + "else{   \n" + "y();   \n" + "}").to("{x();}").to("x();");
  }

  @Test public void issue85_86b() {
    trimming.of("if(false){   \n" + "x(); }   \n" + "else{   \n" + "y();   \n" + "}").to("{y();}").to("y();");
  }

  @Test public void issue85_86c() {
    trimming.of("if(false)   \n" + "x();    \n" + "else   \n" + "y();   \n").to("y();");
  }

  @Test public void issue85_86d() {
    trimming.of("if(false){   \n" + "x(); }   \n" + "else{   \n" + "if(false) a();   \n" + "else b();" + "}").to("{b();}").to("b();");
  }

  @Test public void issue85_86e() {
    trimming.of("if(false){   \n" + "x(); }   \n" + "else{   \n" + "if(true) a();   \n" + "else b();" + "}").to("{a();}").to("a();");
  }

  @Test public void issue85_86f() {
    trimming.of("if(true){   \n" + "if(true) a();   \n" + "else b(); }   \n" + "else{   \n" + "if(false) a();   \n" + "else b();" + "}")
        .to("{a();}").to("a();");
  }

  @Test public void issue85_86g() {
    trimming.of("if(z==k)   \n" + "x();    \n" + "else   \n" + "y();   \n").stays();
  }

  @Test public void issue85_86h() {
    trimming.of("if(5==5)   \n" + "x();    \n" + "else   \n" + "y();   \n").stays();
  }

  @Test public void issue85_86i() {
    trimming.of("if(z){   \n" + "if(true) a();   \n" + "else b(); }   \n" + "else{   \n" + "if(false) a();   \n" + "else b();" + "}")
        .to("if(z)\n" + "if(true) a();   \n" + "else b();\n" + "else\n" + "if(false) a();   \n" + "else b();")
        .to("if(z)\n" + "a(); \n" + "else \n" + "b();   \n");
  }

  @Test public void issue85_86j() {
    trimming.of("if(true){ \n" + "if(true) \n" + "a(); \n" + "else \n" + "b(); \n" + "} \n" + "else c();").to("{a();}").to("a();");
  }

  @Test public void issue85_86k() {
    trimming.of("if(false){ \n" + "if(true) \n" + "a(); \n" + "else \n" + "b(); \n" + "} \n" + "else c();").to("c();");
  }

  @Test public void issue85_86l() {
    trimming.of("if(false)" + "c();" + "else {\n" + "if(true) \n" + "a(); \n" + "else \n" + "b(); \n" + "} \n").to("{a();}").to("a();");
  }

  @Test public void issue87a() {
    trimming.of("a-b*c - (x - - - (d*e))").to("a  - b*c -x + d*e");
  }

  @Test public void issue87c() {
    trimming.of("a + (b-c)").to("a + b -c");
  }

  @Test public void issue87d() {
    trimming.of("a - (b-c)").to("a - b + c");
  }

  @Test public void issue87b() {
    trimming.of("a-b*c").stays();
  }

  // @formatter:off
  enum A { a1() {{ f(); }
      public final void f() {g();}
       protected final void g() {h();}
       final void i() {f();}
       private final void h() {i();}
    }, a2() {{ f(); }
      final public void i() {f();}
      final protected void f() {g();}
      final void g() {h();}
      final private void h() {i();}
    }
  }
 // @formatter:on
}
