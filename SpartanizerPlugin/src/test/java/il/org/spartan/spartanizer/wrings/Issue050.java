package il.org.spartan.spartanizer.wrings;

import static il.org.spartan.spartanizer.wrings.TrimmerTestsUtils.*;

import org.junit.*;
import org.junit.runners.*;

/** @author Yossi Gil
 * @since 2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "static-method", "javadoc" }) //
public class Issue050 {
  @Test public void A$50_Constructors1() {
    trimmingOf("public final class ClassTest {\n"//
        + "public  ClassTest(){}\n"//
        + "}").stays();
  }

  @Test public void A$50_EnumInInterface1() {
    trimmingOf("public interface Int1 {\n"//
        + "static enum Day {\n"//
        + "SUNDAY, MONDAY\n"//
        + "}"//
        + "}")
            .gives("public interface Int1 {\n"//
                + "enum Day {\n"//
                + "SUNDAY, MONDAY\n"//
                + "}" + "}");
  }

  @Test public void A$50_Enums() {
    trimmingOf("public final class ClassTest {\n"//
        + "static enum Day {\n"//
        + "SUNDAY, MONDAY\n"//
        + "}")
            .gives("public final class ClassTest {\n"//
                + "enum Day {\n"//
                + "SUNDAY, MONDAY\n"//
                + "}");
  }

  @Test public void A$50_EnumsOnlyRightModifierRemoved() {
    trimmingOf("public final class ClassTest {\n"//
        + "private static enum Day {\n"//
        + "SUNDAY, MONDAY\n"//
        + "}")
            .gives("public final class ClassTest {\n"//
                + "private enum Day {\n"//
                + "SUNDAY, MONDAY\n"//
                + "}");
  }

  @Test public void A$50_FinalClassMethods() {
    trimmingOf("final class ClassTest {\n"//
        + "final void remove();\n"//
        + "}")
            .gives("final class ClassTest {\n"//
                + "void remove();\n "//
                + "}");
  }

  @Test public void A$50_FinalClassMethodsOnlyRightModifierRemoved() {
    trimmingOf("final class ClassTest {\n"//
        + "public final void remove();\n"//
        + "}")
            .gives("final class ClassTest {\n"//
                + "public void remove();\n "//
                + "}");
  }

  @Test public void A$50_inEnumMember() {
    trimmingOf(//
        "enum A {; final void f() {} public final void g() {} }"//
    ).stays();
  }

  @Test public void A$50_inEnumMemberComplex() {
    trimmingOf(//
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
    ).gives("enum A { a1 {{ f(); } \n" + //
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

  @Test public void A$50_InterfaceMethods1() {
    trimmingOf("public interface Int1 {\n"//
        + "public void add();\n"//
        + "void remove()\n; "//
        + "}")
            .gives("public interface Int1 {\n"//
                + "void add();\n"//
                + "void remove()\n; "//
                + "}");
  }

  @Test public void A$50_InterfaceMethods2() {
    trimmingOf("public interface Int1 {\n"//
        + "public abstract void add();\n"//
        + "abstract void remove()\n; "//
        + "}")
            .gives("public interface Int1 {\n"//
                + "void add();\n"//
                + "void remove()\n; "//
                + "}");
  }

  @Test public void A$50_InterfaceMethods3() {
    trimmingOf("public interface Int1 {\n"//
        + "abstract void add();\n"//
        + "void remove()\n; "//
        + "}")
            .gives("public interface Int1 {\n"//
                + "void add();\n"//
                + "void remove()\n; "//
                + "}");
  }

  @Test public void A$50_SimpleDontWorking() {
    trimmingOf("interface a"//
        + "{}").stays();
  }

  @Test public void A$50_SimpleWorking1() {
    trimmingOf("abstract abstract interface a"//
        + "{}").gives("interface a {}");
  }

  @Test public void A$50_SimpleWorking2() {
    trimmingOf("abstract interface a"//
        + "{}").gives("interface a {}");
  }

  @Test public void A$50a_interface() {
    trimmingOf("abstract interface a {}")//
        .gives("interface a {}");//
  }

  @Test public void A$50b_interface() {
    trimmingOf("abstract static interface a {}")//
        .gives("interface a {}");//
  }

  @Test public void A$50c_interface__abstract() {
    trimmingOf("abstract interface a {}")//
        .gives("interface a {}");//
  }

  @Test public void A$50c_interface_static_abstract() {
    trimmingOf("static abstract interface a {}")//
        .gives("interface a {}");//
  }

  @Test public void A$50d_interface() {
    trimmingOf("static interface a {}")//
        .gives("interface a {}");//
  }

  @Test public void A$50e_enum() {
    trimmingOf("enum a {a,b}")//
        .stays();//
  }

  @Test public void A$50e1_enum() {
    trimmingOf("enum a {a}")//
        .stays();//
  }

  @Test public void A$50e2_enum() {
    trimmingOf("enum a {}")//
        .stays();//
  }

  @Test public void A$50f_enum() {
    trimmingOf("static enum a {a, b}")//
        .gives("enum a {a, b}");//
  }

  @Test public void A$50g_enum() {
    trimmingOf("static abstract enum a {x,y,z; void f() {}}")//
        .gives("enum a {x,y,z; void f() {}}");//
  }

  @Test public void A$50h_enum() {
    trimmingOf("static abstract final enum a {x,y,z; void f() {}}")//
        .gives("enum a {x,y,z; void f() {}}");//
  }
}
