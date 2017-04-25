package il.org.spartan.spartanizer.issues;

import static il.org.spartan.azzert.*;
import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.tippers.*;

/** TODO Yossi Gil Document Class
 * @author Yossi Gil
 * @since Jan 6, 2017 */
@SuppressWarnings("static-method")
public class Issue0283 {
  @Test public void a() {
    trimmingOf("@forget class Test123 { @Test @WebFault @WebEndpoint @SuppressWarnings( 3 ) @Inherited  @Deprecated public void test0() { }}")
        .gives("@forget class Test123{@Deprecated @Inherited @Test @WebEndpoint @WebFault @SuppressWarnings(3)  public void test0(){}}") //
        .stays();
  }

  /** Automatically generated on Sun-Mar-12-18:46:48-IST-2017, copied by
   * Yossi */
  @Test public void err1() {
    trimmingOf("@A @B class C{@A @D1(3)@E @D(3)@E @F public void a(){}}") //
        .using(new AnnotationSort<MethodDeclaration>(), MethodDeclaration.class) //
        .gives("@A @B class C{@A @D(3) @D1(3) @E @F public void a(){}}") //
        .stays() //
    ;
  }

  @Test public void duplication() {
    trimmingOf("@A @D1(3)@E @D(3)@E @F public void a(){}") //
        .using(new AnnotationSort<MethodDeclaration>(), MethodDeclaration.class) //
        .gives("@A @D(3) @D1(3) @E @F public void a(){}") //
        .stays();
  }

  /** Introduced by Yogi on Thu-Mar-23-19:55:44-IST-2017 (code automatically
   * generated in 'il.org.spartan.spartanizer.cmdline.anonymize.java') */
  @Test public void test_aBClassCAD3EF3GPublicVoida() {
    trimmingOf("@A @B class C{@A @D(3)@E @F({3})@G public void a(){}}") //
        .using(new AnnotationRemoveSingletonArrray(), SingleMemberAnnotation.class) //
        .gives("@A @B class C{@A @D(3)@E @F(3)@G public void a(){}}") //
        .stays() //
    ;
  }

  /** Automatically generated */
  @Test public void err4() {
    trimmingOf("@A @B class C{@A @D1(3)@E @D(3) @F public void a(){}}") //
        .gives("@A @B class C{@A @D(3)@D1(3) @E @F public void a(){}}") //
        .stays();
  }

  /** Introduced by Yogi on Thu-Mar-23-20:04:01-IST-2017 (code automatically
   * generated in 'il.org.spartan.spartanizer.cmdline.anonymize.java') */
  @Test public void test_BClassCAD3EF3GPublicVoida() {
    trimmingOf("@A @B class C{@A @D(3)@E @F({3})@G public void a(){}}") //
        .using(new AnnotationRemoveSingletonArrray(), SingleMemberAnnotation.class) //
        .gives("@A @B class C{@A @D(3)@E @F(3)@G public void a(){}}") //
        .stays() //
    ;
  }

  @Test public void test0() {
    trimmingOf("@SuppressWarnings(\"unused\") " //
        + "@Deprecated " //
        + "@Override " //
        + " void myMethod() { }") //
            .gives("@Deprecated @Override @SuppressWarnings(\"unused\") " //
                + " void myMethod() { }")//
            .stays();
  }

  @Test public void test1() {
    azzert.that(AnnotationSort.compare("Override", "Override"), is(0));
    azzert.that(AnnotationSort.compare("Deprecated", "Deprecated"), is(0));
    azzert.that(AnnotationSort.compare("SupportedSourceVersion", "SupportedSourceVersion"), is(0));
    azzert.that(AnnotationSort.compare("Deprecated", "Override"), lessThan(0));
    azzert.that(AnnotationSort.compare("Override", "Inherited"), lessThan(0));
    azzert.that(AnnotationSort.compare("Inherited", "SomeUserDefined"), lessThan(0));
    azzert.that(AnnotationSort.compare("SomeUserDefined", "Transient"), lessThan(0));
    azzert.that(AnnotationSort.compare("Transient", "SuppressWarnings"), lessThan(0));
    azzert.that(AnnotationSort.compare("NonNull", "Nullable"), lessThan(0));
    azzert.that(AnnotationSort.compare("SomeUserDefinedA", "SomeUserDefinedB"), lessThan(0));
    azzert.that(AnnotationSort.compare("SupportedSourceVersion", "WebServiceProvider"), lessThan(0));
  }

  @Test public void test2() {
    trimmingOf("public class Point {" //
        + "@ConstructorProperties({\"x\", \"y\"}) @Deprecated " //
        + "public Point(int x, int y) {" //
        + "this.x = x;" //
        + "this.y = y;" //
        + "}}") //
            .gives("public class Point {" //
                + "@Deprecated @ConstructorProperties({\"x\", \"y\"}) " //
                + "public Point(int x, int y) {" //
                + "this.x = x;" //
                + "this.y = y;" //
                + "}}")//
            .stays();
  }

  @Test public void test31() {
    trimmingOf("class Test123 {@Test  @Deprecated public void test0() { }}").gives("class Test123 {@Deprecated @Test  public void test0() { }}");
  }

  @Test public void test32() {
    trimmingOf("@forget class Test123 {@Test @SuppressWarnings({ 3 })  @Deprecated public void test0() { }}")
        .gives("@forget class Test123 {@Deprecated @Test @SuppressWarnings({ 3 })  public void test0() { }}");
  }

  @Test public void test3() {
    trimmingOf("@forget class Test123 {@Test @SuppressWarnings({ 3, 4 }) @Inherited  @Deprecated public void test0() { }}")
        .gives("@forget class Test123{@Deprecated @Inherited @Test @SuppressWarnings({ 3, 4 })  public void test0(){}}") //
        .stays();
  }

  /** Introduced by Yogi on Thu-Mar-23-20:01:54-IST-2017 (code automatically
   * generated in 'il.org.spartan.spartanizer.cmdline.anonymize.java') */
  @Test public void test_aClassBCDE3E3FPublicVoida() {
    trimmingOf("@A class B{@C @D @E(3)@F public void a(){}}") //
        .stays() //
    ;
  }

  @Test public void test5() {
    trimmingOf("@forget @Deprecated class Test123 {@Test @SuppressWarnings(3) @Inherited  @Deprecated public void test0(){}}")
        .gives("@Deprecated @forget class Test123{@Test @SuppressWarnings(3) @Inherited  @Deprecated public void test0(){}}") //
        .gives("@Deprecated @forget class Test123{@Deprecated @Inherited @Test @SuppressWarnings(3)  public void test0(){}}") //
        .stays();
  }
}
