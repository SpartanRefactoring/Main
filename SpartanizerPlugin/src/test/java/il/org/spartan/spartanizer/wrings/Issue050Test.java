package il.org.spartan.spartanizer.wrings;

import static il.org.spartan.spartanizer.wrings.TrimmerTestsUtils.*;

import org.junit.*;
import org.junit.runners.*;

/** @author Yossi Gil
 * @since 2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "static-method", "javadoc" }) //
public class Issue050Test {
  @Test public void issue50_Constructors1() {
    trimmingOf("public final class ClassTest {\n"//
        + "public  ClassTest(){}\n"//
        + "}").stays();
  }

  @Test public void issue50_EnumInInterface1() {
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

  @Test public void issue50_Enums() {
    trimmingOf("public final class ClassTest {\n"//
        + "static enum Day {\n"//
        + "SUNDAY, MONDAY\n"//
        + "}")
            .gives("public final class ClassTest {\n"//
                + "enum Day {\n"//
                + "SUNDAY, MONDAY\n"//
                + "}");
  }

  @Test public void issue50_EnumsOnlyRightModifierRemoved() {
    trimmingOf("public final class ClassTest {\n"//
        + "private static enum Day {\n"//
        + "SUNDAY, MONDAY\n"//
        + "}")
            .gives("public final class ClassTest {\n"//
                + "private enum Day {\n"//
                + "SUNDAY, MONDAY\n"//
                + "}");
  }

  @Test public void issue50_FinalClassMethods() {
    trimmingOf("final class ClassTest {\n"//
        + "final void remove();\n"//
        + "}")
            .gives("final class ClassTest {\n"//
                + "void remove();\n "//
                + "}");
  }

  @Test public void issue50_FinalClassMethodsOnlyRightModifierRemoved() {
    trimmingOf("final class ClassTest {\n"//
        + "public final void remove();\n"//
        + "}")
            .gives("final class ClassTest {\n"//
                + "public void remove();\n "//
                + "}");
  }

  @Test public void issue50_inEnumMember() {
    trimmingOf(//
        "enum A {; final void f() {} public final void g() {} }"//
    ).stays();
  }

  @Test public void issue50_inEnumMemberComplex() {
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

  @Test public void issue50_InterfaceMethods1() {
    trimmingOf("public interface Int1 {\n"//
        + "public void add();\n"//
        + "void remove()\n; "//
        + "}")
            .gives("public interface Int1 {\n"//
                + "void add();\n"//
                + "void remove()\n; "//
                + "}");
  }

  @Test public void issue50_InterfaceMethods2() {
    trimmingOf("public interface Int1 {\n"//
        + "public abstract void add();\n"//
        + "abstract void remove()\n; "//
        + "}")
            .gives("public interface Int1 {\n"//
                + "void add();\n"//
                + "void remove()\n; "//
                + "}");
  }

  @Test public void issue50_InterfaceMethods3() {
    trimmingOf("public interface Int1 {\n"//
        + "abstract void add();\n"//
        + "void remove()\n; "//
        + "}")
            .gives("public interface Int1 {\n"//
                + "void add();\n"//
                + "void remove()\n; "//
                + "}");
  }

  @Test public void issue50_SimpleDontWorking() {
    trimmingOf("interface a"//
        + "{}").stays();
  }

  @Test public void issue50_SimpleWorking1() {
    trimmingOf("abstract abstract interface a"//
        + "{}").gives("interface a {}");
  }

  @Test public void issue50_SimpleWorking2() {
    trimmingOf("abstract interface a"//
        + "{}").gives("interface a {}");
  }

  @Test public void issue50a() {
    trimmingOf("abstract interface a {}")//
        .gives("interface a {}");//
  }

  @Test public void issue50b() {
    trimmingOf("abstract static interface a {}")//
        .gives("interface a {}");//
  }

  @Test public void issue50c() {
    trimmingOf("static abstract interface a {}")//
        .gives("interface a {}");//
  }

  @Test public void issue50d() {
    trimmingOf("static interface a {}")//
        .gives("interface a {}");//
  }

  @Test public void issue50e() {
    trimmingOf("enum a {a,b}")//
        .stays();//
  }

  @Test public void issue50e1() {
    trimmingOf("enum a {a}")//
        .stays();//
  }

  @Test public void issue50e2() {
    trimmingOf("enum a {}")//
        .stays();//
  }

  @Test public void issue50f() {
    trimmingOf("static enum a {a, b}")//
        .gives("enum a {a, b}");//
  }

  @Test public void issue50g() {
    trimmingOf("static abstract enum a {x,y,z; void f() {}}")//
        .gives("enum a {x,y,z; void f() {}}");//
  }

  @Test public void issue50h() {
    trimmingOf("static abstract final enum a {x,y,z; void f() {}}")//
        .gives("enum a {x,y,z; void f() {}}");//
  }
}
