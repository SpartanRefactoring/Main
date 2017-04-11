package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsTrimmer.*;

import org.junit.*;
import org.junit.runners.*;

/** Unit tests for the GitHub issue thus numbered
 * @author Yossi Gil
 * @since 2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SuppressWarnings({ "static-method", "javadoc" })
public class Issue0050 {
  @Test public void a$50_Constructors1() {
    topDownTrimming("public final class ClassTest{public ClassTest(){}}")//
        .stays();
  }

  @Test public void a$50_enumCtorPrivate() {
    topDownTrimming("enum A {a,b; private A(){}")//
        .gives("enum A {a,b;A(){}")//
        .stays()//
    ;
  }

  @Test public void a$50_enumCtorProtected() {
    topDownTrimming("enum A {a,b; protected A(){}")//
        .gives("enum A {a,b;A(){}")//
        .stays()//
    ;
  }

  @Test public void a$50_enumCtorPublic() {
    topDownTrimming("enum A {a,b; public A(){}")//
        .gives("enum A {a,b;A(){}")//
        .stays()//
    ;
  }

  @Test public void a$50_EnumInInterface1() {
    topDownTrimming("public interface Int1{static enum Day{SUNDAY,MONDAY}}")//
        .gives("public interface Int1{enum Day{SUNDAY,MONDAY}}")//
        .stays()//
    ;
  }

  @Test public void a$50_Enums() {
    topDownTrimming("public final class ClassTest{static enum Day{SUNDAY,MONDAY}")//
        .gives("public final class ClassTest{enum Day{SUNDAY,MONDAY}")//
        .stays()//
    ;
  }

  @Test public void a$50_EnumsOnlyRightModifierRemoved() {
    topDownTrimming("public final class ClassTest{private static enum Day{SUNDAY,MONDAY}")
        .gives("public final class ClassTest{private enum Day{SUNDAY,MONDAY}")//
        .stays()//
    ;
  }

  @Test public void a$50_FinalClassMethods() {
    topDownTrimming("final class ClassTest{final void remove();}")//
        .gives("final class ClassTest{void remove();}")//
        .stays()//
    ;
  }

  @Test public void a$50_FinalClassMethodsOnlyRightModifierRemoved() {
    topDownTrimming("final class ClassTest{public final void remove();}")//
        .gives("final class ClassTest{public void remove();}")//
        .stays()//
    ;
  }

  @Test public void a$50_inEnumMember() {
    topDownTrimming("enum A{;final void f(){}public final void g(){}}")//
        .gives("enum A{;void f(){}public void g(){}}")//
        .stays();
  }

  @Test public void a$50_inEnumMemberComplex() {
    topDownTrimming("enum A{a1{{f();}protected final void f(){g();}public final void g(){h();}\n"
        + "private final void h(){i();}final void i(){f();}},a2{{f();}final protected void f(){g();}\n"
        + "final void g(){h();}final private void h(){i();}final protected void i(){f();}};\n"
        + "protected abstract void f();protected void ia(){}void i(){}}\n")//
            .gives(
                "enum A{a1{{f();}void f(){g();}public void g(){h();}void h(){i();}void i(){f();}},a2{{f();}protected final void f(){g();}void g(){h();}private final void h(){i();}protected final void i(){f();}};abstract void f();void ia(){}void i(){}}");
  }

  @Test public void a$50_InterfaceMethods1() {
    topDownTrimming("public interface Int1{public void add();void remove();}")//
        .gives("public interface Int1{void add();void remove();}")//
        .stays()//
    ;
  }

  @Test public void a$50_InterfaceMethods2() {
    topDownTrimming("public interface Int1{public abstract void add();abstract void remove()\n;}")
        .gives("public interface Int1{void add();void remove()\n;}")//
        .stays()//
    ;
  }

  @Test public void a$50_InterfaceMethods3() {
    topDownTrimming("public interface Int1{abstract void add();void remove()\n;}")//
        .gives("public interface Int1{void add();void remove();}")//
        .stays()//
    ;
  }

  @Test public void a$50_SimpleDontWorking() {
    topDownTrimming("interface a{}")//
        .stays();
  }

  @Test public void a$50_SimpleWorking1() {
    topDownTrimming("abstract abstract interface a{}")//
        .gives("abstract interface a{}")//
        .gives("interface a{}")//
        .stays()//
    ;
  }

  @Test public void a$50_SimpleWorking2() {
    topDownTrimming("abstract interface a{}")//
        .gives("interface a{}")//
        .stays()//
    ;
  }

  @Test public void a$50a_interface() {
    topDownTrimming("abstract interface a{}")//
        .gives("interface a{}")//
        .stays()//
    ;
  }

  @Test public void a$50b_interface() {
    topDownTrimming("abstract static interface a{}")//
        .gives("interface a{}")//
        .stays()//
    ;
  }

  @Test public void a$50c_interface__abstract() {
    topDownTrimming("abstract interface a{}")//
        .gives("interface a{}")//
        .stays()//
    ;
  }

  @Test public void a$50c_interface_static_abstract() {
    topDownTrimming("static abstract interface a{}")//
        .gives("abstract static interface a{}")//
        .gives("interface a{}")//
        .stays();
  }

  @Test public void a$50d_interface() {
    topDownTrimming("static interface a{}")//
        .gives("interface a{}")//
        .stays()//
    ;
  }

  @Test public void a$50e_enum() {
    topDownTrimming("enum a{a,b}")//
        .stays();
  }

  @Test public void a$50e1_enum() {
    topDownTrimming("enum a{a}")//
        .stays();
  }

  @Test public void a$50e2_enum() {
    topDownTrimming("enum a{}")//
        .stays();
  }

  @Test public void a$50f_enum() {
    topDownTrimming("static enum a{a,b}")//
        .gives("enum a{a,b}")//
        .stays()//
    ;
  }

  @Test public void a$50g_enum() {
    topDownTrimming("static enum a{x,y,z;void f(){}}")//
        .gives("enum a{x,y,z;void f(){}}")//
        .stays() //
    ;
  }

  @Test public void a$50h_enum() {
    topDownTrimming("static enum a{x,y,z;void f(){}}")//
        .gives("enum a{x,y,z;void f(){}}") //
        .stays();
  }

  @Test public void enumConstants() {
    topDownTrimming("enum A{a{protected int a; }}")//
        .gives("enum A{a{int a; }}").stays()//
    ;
  }

  @Test public void enumConstants1() {
    topDownTrimming("enum A{a{private int a; }}")//
        .gives("enum A{a{int a; }}").stays()//
    ;
  }

  @Test public void enumConstants2() {
    topDownTrimming("enum A{a{public int a; }}")//
        .gives("enum A{a{int a; }}").stays()//
    ;
  }

  @Test public void enumConstantsA() {
    topDownTrimming("enum A{a{protected void f(){}},b{final void g(){}}}") //
        .gives("enum A{a{void f(){}},b{void g(){}}}") //
        .stays();
  }

  @Test public void enumConstantsB() {
    topDownTrimming("enum A{a{protected void f(){}}}") //
        .gives("enum A{a{void f(){}}}") //
        .stays();
  }

  @Test public void finalMethodInEnum() {
    topDownTrimming("enum A{a1; final void f(){}}")//
        .gives("enum A{a1; void f(){}}") //
        .stays();
  }

  @Test public void protectedFinalMethodInEnum() {
    topDownTrimming("enum A{a1; protected final void f(){}}")//
        .gives("enum A{a1; void f(){}}") //
        .stays();
  }

  @Test public void protectedMethodInEnumMember() {
    topDownTrimming("enum A{a1 { protected void f(){}}; void f() {}}")//
        .gives("enum A{a1 { void f(){}}; void f() {}}")//
        .stays();
  }
}
