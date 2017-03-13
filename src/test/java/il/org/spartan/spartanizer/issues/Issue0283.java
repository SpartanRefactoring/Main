/** TODO: Yossi Gil {@code Yossi.Gil@GMail.COM} please add a description
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @since Jan 6, 2017 */
package il.org.spartan.spartanizer.issues;

import static il.org.spartan.azzert.*;
import static il.org.spartan.spartanizer.testing.TestsUtilsTrimmer.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.tippers.*;

@SuppressWarnings("static-method")
public class Issue0283 {
  /** Automatically generated on Sun-Mar-12-17:16:16-IST-2017, copied by
   * Iossi */
  @Test public void aBclassCAD3ED3Fpublicvoida() {
    trimmingOf("@A @B class C{@A @D(3)@E @D({3})@F public void a(){}}") //
        .using(MethodDeclaration.class, new AnnotationSort<MethodDeclaration>()) //
        .gives("@A @B class C{@A @D(3)@D({3})@E@F public void a(){}}") //
        .using(SingleMemberAnnotation.class, new AnnotationRemoveSingletonArrray()) //
        .gives("@A @B class C{@A @D(3)@D(3)@E@F public void a(){}}") //
        .stays() //
    ;
  }

  /** Automatically generated */
  @Test public void abclasscad3ed3fpublicvoida() {
    trimmingOf("@A @B class C{@A @D(3)@E @D({3})@F public void a(){}}") //
        .gives("@A @B class C{@A @D(3)@D({3})@E@F public void a(){}}") //
        .gives("@A @B class C{@A @D(3)@D(3)@E@F public void a(){}}") //
        .stays() //
    ;
  }

  /** Automatically generated on Sun-Mar-12-18:46:48-IST-2017, copied by
   * Yossi */
  @Test public void aBClassCAD3ED3FPublicVoida() {
    trimmingOf("@A @B class C{@A @D(3)@E @D({3})@F public void a(){}}") //
        .using(MethodDeclaration.class, new AnnotationSort<MethodDeclaration>()) //
        .gives("@A @B class C{@A @D(3)@D({3})@E@F public void a(){}}") //
        .using(SingleMemberAnnotation.class, new AnnotationRemoveSingletonArrray()) //
        .gives("@A @B class C{@A @D(3)@D(3)@E@F public void a(){}}") //
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

  @Test public void test3() {
    trimmingOf("@Ignore class Test123 {@Test @SuppressWarnings({ 3 }) @Inherited @NonNull @Deprecated public void test0() { }}")
        .gives("@Ignore class Test123{@Deprecated @SuppressWarnings(3)@Test @SuppressWarnings({3})@NonNull public void test0(){}}") //
        .gives("@Ignore class Test123{@Deprecated @Test@SuppressWarnings(3) @SuppressWarnings(3) @NonNull public void test0(){}}") //
        .stays();
  }

  @Test public void test4() {
    trimmingOf(
        "@Ignore class Test123 { @Test @WebFault @WebEndpoint @SuppressWarnings({ 3 }) @Inherited @NonNull @Deprecated public void test0() { }}")
            .gives(
                "@Ignore class Test123{@Deprecated @Inherited @Test @SuppressWarnings(3) @WebFault @SuppressWarnings({3}) @NonNull public void test0(){}}") //
            .gives(
                "@Ignore class Test123{@Deprecated @Inherited @Test @WebFault @SuppressWarnings(3) @SuppressWarnings(3) @NonNull public void test0(){}}") //
            .stays();
  }

  @Test public void test5() {
    trimmingOf("@Ignore @Deprecated class Test123 {@Test @SuppressWarnings({3}) @Inherited @NonNull @Deprecated public void test0(){}}")
        .gives("@Deprecated @Ignore class Test123{@Deprecated @SuppressWarnings(3) @Test @SuppressWarnings({3}) @NonNull public void test0(){}}") //
        .gives("@Deprecated @Ignore class Test123{@Deprecated @Test@SuppressWarnings(3) @SuppressWarnings(3) @NonNull public void test0(){}}") //
        .stays();
  }
}
