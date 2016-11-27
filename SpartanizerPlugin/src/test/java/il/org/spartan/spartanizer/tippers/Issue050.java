package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.junit.*;
import org.junit.runners.*;

/** @author Yossi Gil
 * @since 2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SuppressWarnings({ "static-method", "javadoc" })
public class Issue050 {
  @Test public void a$50_Constructors1() {
    trimmingOf("public final class ClassTest{public ClassTest(){}}").stays();
  }

  @Test public void a$50_EnumInInterface1() {
    trimmingOf("public interface Int1{static enum Day{SUNDAY,MONDAY}}")//
        .gives("public interface Int1{enum Day{SUNDAY,MONDAY}}")//
        .stays()//
    ;
  }

  @Test public void a$50_enumCtorPrivate() {
    trimmingOf("enum A {a,b; private A(){}")//
        .gives("enum A {a,b;A(){}")//
        .stays()//
    ;
  }

  @Test public void a$50_enumCtorPublic() {
    trimmingOf("enum A {a,b; public A(){}")//
        .gives("enum A {a,b;A(){}")//
        .stays()//
    ;
  }

  @Test public void a$50_enumCtorProtected() {
    trimmingOf("enum A {a,b; protected A(){}")//
        .gives("enum A {a,b;A(){}")//
        .stays()//
    ;
  }

  @Test public void a$50_Enums() {
    trimmingOf("public final class ClassTest{static enum Day{SUNDAY,MONDAY}").gives("public final class ClassTest{enum Day{SUNDAY,MONDAY}")//
        .stays()//
    ;
  }

  @Test public void a$50_EnumsOnlyRightModifierRemoved() {
    trimmingOf("public final class ClassTest{private static enum Day{SUNDAY,MONDAY}")
        .gives("public final class ClassTest{private enum Day{SUNDAY,MONDAY}")//
        .stays()//
    ;
  }

  @Test public void a$50_FinalClassMethods() {
    trimmingOf("final class ClassTest{final void remove();}")//
        .gives("final class ClassTest{void remove();}")//
        .stays()//
    ;
  }

  @Test public void a$50_FinalClassMethodsOnlyRightModifierRemoved() {
    trimmingOf("final class ClassTest{public final void remove();}").gives("final class ClassTest{public void remove();}")//
        .stays()//
    ;
  }

  @Test public void a$50_inEnumMember() {
    trimmingOf("enum A{;final void f(){}public final void g(){}}").stays();
  }

  @Ignore @Test public void a$50_inEnumMemberComplex() {
    trimmingOf("enum A{a1{{f();}protected final void f(){g();}public final void g(){h();}\n"
        + "private final void h(){i();}final void i(){f();}},a2{{f();}final protected void f(){g();}\n"
        + "final void g(){h();}final private void h(){i();}final protected void i(){f();}};\n"
        + "protected abstract void f();protected void ia(){}void i(){}}\n")
            .gives("enum A{a1{{f();}void f(){g();}public void g(){h();}void h(){i();}void i(){f();}\n"
                + "},a2{{f();}void f(){g();}void g(){h();}void h(){i();}void i(){f();}};\n" + "abstract void f();void ia(){}void i(){}}\n");
  }

  @Test public void a$50_InterfaceMethods1() {
    trimmingOf("public interface Int1{public void add();void remove();}")//
        .gives("public interface Int1{void add();void remove();}")//
        .stays()//
    ;
  }

  @Test public void a$50_InterfaceMethods2() {
    trimmingOf("public interface Int1{public abstract void add();abstract void remove()\n;}")
        .gives("public interface Int1{void add();void remove()\n;}")//
        .stays()//
    ;
  }

  @Test public void a$50_InterfaceMethods3() {
    trimmingOf("public interface Int1{abstract void add();void remove()\n;}").gives("public interface Int1{void add();void remove();}")//
        .stays()//
    ;
  }

  @Test public void a$50_SimpleDontWorking() {
    trimmingOf("interface a{}").stays();
  }

  @Test public void a$50_SimpleWorking1() {
    trimmingOf("abstract abstract interface a{}")//
        .gives("abstract interface a{}")//
        .gives("interface a{}")//
        .stays()//
    ;
  }

  @Test public void a$50_SimpleWorking2() {
    trimmingOf("abstract interface a{}")//
        .gives("interface a{}")//
        .stays()//
    ;
  }

  @Test public void a$50a_interface() {
    trimmingOf("abstract interface a{}")//
        .gives("interface a{}")//
        .stays()//
    ;
  }

  @Test public void a$50b_interface() {
    trimmingOf("abstract static interface a{}")//
        .gives("interface a{}")//
        .stays()//
    ;
  }

  @Test public void a$50c_interface__abstract() {
    trimmingOf("abstract interface a{}")//
        .gives("interface a{}")//
        .stays()//
    ;
  }

  @Test public void a$50c_interface_static_abstract() {
    trimmingOf("static abstract interface a{}")//
        .gives("abstract static interface a{}")//
        .gives("interface a{}")//
        .stays();
  }

  @Test public void a$50d_interface() {
    trimmingOf("static interface a{}")//
        .gives("interface a{}")//
        .stays()//
    ;
  }

  @Test public void a$50e_enum() {
    trimmingOf("enum a{a,b}").stays();
  }

  @Test public void a$50e1_enum() {
    trimmingOf("enum a{a}").stays();
  }

  @Test public void a$50e2_enum() {
    trimmingOf("enum a{}").stays();
  }

  @Test public void a$50f_enum() {
    trimmingOf("static enum a{a,b}")//
        .gives("enum a{a,b}")//
        .stays()//
    ;
  }

  @Test public void a$50g_enum() {
    trimmingOf("static abstract enum a{x,y,z;void f(){}}")//
        .gives("abstract static enum a{x,y,z;void f(){}}")//
        .gives("enum a{x,y,z;void f(){}}")//
        .stays() //
    ;
  }

  @Test public void a$50h_enum() {
    trimmingOf("static abstract final enum a{x,y,z;void f(){}}")//
        .gives("abstract static final enum a{x,y,z;void f(){}}") //
        .gives("enum a{x,y,z;void f(){}}") //
        .stays();
  }
}
