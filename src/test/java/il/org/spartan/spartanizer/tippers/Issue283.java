package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.azzert.*;
import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import java.beans.*;

import org.junit.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.java.*;
import il.org.spartan.spartanizer.utils.tdd.*;

@SuppressWarnings("static-method")
public class Issue283 {
  @Test public void test0() {
    trimmingOf("@SuppressWarnings(\"unused\") " //
        + "@Deprecated " //
        + "@Override" //
        + " void myMethod() { }") //
            .gives("@Deprecated " + "@Override " + "@SuppressWarnings(\"unused\") " //
                + " void myMethod() { }")//
            .stays();
  }
  // @Deprecated @ int i = 5;

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
    trimmingOf("public class Point {"  //
       + "@ConstructorProperties({\"x\", \"y\"}) @Deprecated " //
       +  "public Point(int x, int y) {" //
       +   "this.x = x;" //
       +   "this.y = y;" //
       + "}}") //
    .gives("public class Point {"  //
        + "@Deprecated @ConstructorProperties({\"x\", \"y\"}) " //
        +  "public Point(int x, int y) {" //
        +   "this.x = x;" //
        +   "this.y = y;" //
        + "}}")//
    .stays();
   }
  
  @Test public void test3() {
    trimmingOf("@Ignore class Test123 {"
        + " @Test @SuppressWarnings({ \"EnumBody\" }) @Inherited @NonNull @Deprecated public void test0() { }}")
    .gives("@Ignore class Test123 {"
        + " @Deprecated @Inherited @Test @SuppressWarnings({ \"EnumBody\" }) @NonNull public void test0() { }}")
    .stays();
   }
  
  @Test public void test4() {
    trimmingOf("@Ignore class Test123 {"
        + " @Test @WebFault @WebEndpoint @SuppressWarnings({ \"EnumBody\" }) @Inherited @NonNull @Deprecated public void test0() { }}")
    .gives("@Ignore class Test123 {"
        + " @Deprecated @Inherited @Test @WebEndpoint @WebFault @SuppressWarnings({ \"EnumBody\" }) @NonNull public void test0() { }}")
    .stays();
   }
  
  @Test public void test5() {
    trimmingOf("@Ignore @Deprecated class Test123 {"
        + " @Test @SuppressWarnings({ \"EnumBody\" }) @Inherited @NonNull @Deprecated public void test0() { }}")
    .gives("@Deprecated @Ignore class Test123 {"
        + " @Test @SuppressWarnings({ \"EnumBody\" }) @Inherited @NonNull @Deprecated public void test0() { }}")
    .gives("@Deprecated @Ignore class Test123 {"
        + " @Deprecated @Inherited @Test @SuppressWarnings({ \"EnumBody\" }) @NonNull public void test0() { }}")
    .stays();
   }
  
  
}

