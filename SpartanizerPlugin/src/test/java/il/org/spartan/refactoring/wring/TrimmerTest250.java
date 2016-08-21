package il.org.spartan.refactoring.wring;

import static il.org.spartan.refactoring.wring.TrimmerTestsUtils.*;

import org.junit.*;
import org.junit.runners.*;

/** * Unit tests for the nesting class Unit test for the containing class. Note
 * our naming convention: a) test methods do not use the redundant "test"
 * prefix. b) test methods begin with the name of the method they check.
 * @author Yossi Gil
 * @since 2014-07-10 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "static-method", "javadoc" }) //
public class TrimmerTest250 {
  @Test public void issue70_01() {
    trimming("(double)5").to("1.*5");
  }

  @Test public void issue70_02() {
    trimming("(double)4").to("1.*4");
  }

  @Test public void issue70_03() {
    trimming("(double)1.2").to("1.*1.2");
  }

  @Test public void issue70_04() {
    trimming("(double)'a'").to("1.*'a'");
  }

  @Test public void issue70_05() {
    trimming("(double)A").to("1.*A");
  }

  @Test public void issue70_06() {
    trimming("(double)a.b").to("1.*a.b");
  }

  @Test public void issue70_07() {
    trimming("(double)5").to("1.*5");
  }

  @Test public void issue70_08() {
    trimming("(double)5").to("1.*5");
  }

  @Test public void issue70_09() {
    trimming("(double) 2. * (double)5")//
        .to("(double)5 * (double)2.") //
        .to("1. * 5  * 1. * 2.")//
        .to("");
  }

  @Test public void issue70_10() {
    trimming("(double)5").to("1.*5");
  }

  @Test public void issue70_11() {
    trimming("(double)5").to("1.*5");
  }

  @Test public void issue70_12() {
    trimming("(double)5").to("1.*5");
  }

  @Test public void issue70_13() {
    trimming("(double)5").to("1.*5");
  }

  @Test public void issue70_14() {
    trimming("(double)5").to("1.*5");
  }

  @Test public void issue70_15() {
    trimming("(double)5").to("1.*5");
    trimming("(double)5").to("1.*5");
  }

  @Test public void issue70_16() {
    trimming("(double)5").to("1.*5");
  }

  @Test public void issue70_17() {
    trimming("(double)5").to("1.*5");
  }

  @Test public void issue70_18() {
    trimming("(double)5").to("1.*5");
  }

  @Test public void issue70_19() {
    trimming("(double)5").to("1.*5");
  }

  @Test public void issue71a() {
    trimming("1*a").to("a");
  }

  @Test public void issue71b() {
    trimming("a*1").to("a");
  }

  @Test public void issue71c() {
    trimming("1*a*b").to("a*b");
  }

  @Test public void issue71d() {
    trimming("1*a*1*b").to("a*b");
  }

  @Test public void issue71e() {
    trimming("a*1*b*1").to("a*b");
  }

  @Test public void issue71f() {
    trimming("1.0*a").to(null);
  }

  @Test public void issue71g() {
    trimming("a*2").to("2*a");
  }

  @Test public void issue71h() {
    trimming("1*1").to("1");
  }

  @Test public void issue71i() {
    trimming("1*1*1").to("1");
  }

  @Test public void issue71j() {
    trimming("1*1*1*1*1.0").to("1.0");
  }

  @Test public void issue71k() {
    trimming("-1*1*1").to("-1");
  }

  @Test public void issue71l() {
    trimming("1*1*-1*-1").to("1*1*1*1").to("1");
  }

  @Test public void issue71m() {
    trimming("1*1*-1*-1*-1*1*-1").to("1*1*1*1*1*1*1").to("1");
  }

  @Test public void issue71n() {
    trimming("1*1").to("1");
  }

  @Test public void issue71o() {
    trimming("(1)*((a))").to("a");
  }

  @Test public void issue71p() {
    trimming("((1)*((a)))").to("(a)");
  }

  @Test public void issue71q() {
    trimming("1L*1").to("1L");
  }

  @Test public void issue71r() {
    trimming("1L*a").to("");
  }

  @Test public void issue72a() {
    trimming("x+0").to("x");
  }

  @Test public void issue72b() {
    trimming("0+x").to("x");
  }
  
  @Test public void issue72c() {
    trimming("0-x").to("-x");
  }

  @Test public void issue72d() {
    trimming("x-0").to("x");
  }

  @Test public void issue82a() {
    trimming("(long)5").to("1L*5");
  }

  @Test public void issue82b() {
    trimming("(long)a").to("1L*a");
  }

  @Test public void issue82c() {
    trimming("(long)(long)a").to("1L*(long)a").to("1L*1L*a");
  }

  @Test public void issue82d() {
    trimming("(long)a*(long)b").to("1L*a*1L*b");
  }

  @Test public void issue82e() {
    trimming("(double)(long)a").to("1.*(long)a").to("1.*1L*a");
  }

  // @formatter:off
        enum A { a1() {{ f(); }
            public final void f() {g();}
             protected final void g() {h();}
             private final void h() {i();}
             final void i() {f();}
          }, a2() {{ f(); }
            final protected void f() {g();}
            final void g() {h();}
            final private void h() {i();}
            final public void i() {f();}
          }
        }
  // @formatter:on

  @Test public void issue50_inEnumMemberComplex() {
    trimming(//
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

  @Test public void issue50_inEnumMember() {
    trimming(//
        "enum A {; final void f() {} public final void g() {} }"//
    ).to(null);
  }

  @Test public void issue50_Constructors1() {
    trimming("public class ClassTest {\n"//
        + "public  ClassTest(){}\n"//
        + "}").to("");
  }

  @Test public void issue50_EnumInInterface1() {
    trimming("public interface Int1 {\n"//
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
    trimming("public class ClassTest {\n"//
        + "static enum Day {\n"//
        + "SUNDAY, MONDAY\n"//
        + "}")
            .to("public class ClassTest {\n"//
                + "enum Day {\n"//
                + "SUNDAY, MONDAY\n"//
                + "}");
  }

  @Test public void issue50_EnumsOnlyRightModifierRemoved() {
    trimming("public class ClassTest {\n"//
        + "private static enum Day {\n"//
        + "SUNDAY, MONDAY\n"//
        + "}")
            .to("public class ClassTest {\n"//
                + "private enum Day {\n"//
                + "SUNDAY, MONDAY\n"//
                + "}");
  }

  @Test public void issue50_FinalClassMethods() {
    trimming("final class ClassTest {\n"//
        + "final void remove();\n"//
        + "}")
            .to("final class ClassTest {\n"//
                + "void remove();\n "//
                + "}");
  }

  @Test public void issue50_FinalClassMethodsOnlyRightModifierRemoved() {
    trimming("final class ClassTest {\n"//
        + "public final void remove();\n"//
        + "}")
            .to("final class ClassTest {\n"//
                + "public void remove();\n "//
                + "}");
  }

  @Test public void issue50_InterfaceMethods1() {
    trimming("public interface Int1 {\n"//
        + "public void add();\n"//
        + "void remove()\n; "//
        + "}")
            .to("public interface Int1 {\n"//
                + "void add();\n"//
                + "void remove()\n; "//
                + "}");
  }

  @Test public void issue50_InterfaceMethods2() {
    trimming("public interface Int1 {\n"//
        + "public abstract void add();\n"//
        + "abstract void remove()\n; "//
        + "}")
            .to("public interface Int1 {\n"//
                + "void add();\n"//
                + "void remove()\n; "//
                + "}");
  }

  @Test public void issue50_InterfaceMethods3() {
    trimming("public interface Int1 {\n"//
        + "abstract void add();\n"//
        + "void remove()\n; "//
        + "}")
            .to("public interface Int1 {\n"//
                + "void add();\n"//
                + "void remove()\n; "//
                + "}");
  }

  @Test public void issue50_SimpleDontWorking() {
    trimming("interface a"//
        + "{}").to("");
  }

  @Test public void issue50_SimpleWorking1() {
    trimming("abstract abstract interface a"//
        + "{}").to("interface a {}");
  }

  @Test public void issue50_SimpleWorking2() {
    trimming("abstract interface a"//
        + "{}").to("interface a {}");
  }

  @Test public void issue50a() {
    trimming("abstract interface a {}")//
        .to("interface a {}");//
  }

  @Test public void issue50b() {
    trimming("abstract static interface a {}")//
        .to("interface a {}");//
  }

  @Test public void issue50c() {
    trimming("static abstract interface a {}")//
        .to("interface a {}");//
  }

  @Test public void issue50d() {
    trimming("static interface a {}")//
        .to("interface a {}");//
  }

  @Test public void issue50e() {
    trimming("enum a {a,b}")//
        .to(null);//
  }

  @Test public void issue50e1() {
    trimming("enum a {a}")//
        .to(null);//
  }

  @Test public void issue50e2() {
    trimming("enum a {}")//
        .to(null);//
  }

  @Test public void issue50f() {
    trimming("static enum a {a, b}")//
        .to("enum a {a, b}");//
  }

  @Test public void issue50g() {
    trimming("static abstract enum a {x,y,z; void f() {}}")//
        .to("enum a {x,y,z; void f() {}}");//
  }

  @Test public void issue50h() {
    trimming("static abstract final enum a {x,y,z; void f() {}}")//
        .to("enum a {x,y,z; void f() {}}");//
  }
}
