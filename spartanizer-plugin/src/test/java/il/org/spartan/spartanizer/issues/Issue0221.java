package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.trimmingOf;

import org.junit.Test;

import il.org.spartan.spartanizer.tippers.FieldInitializedDefaultValue;

/** Unit tests for {@link FieldInitializedDefaultValue}
 * @author Yossi Gil
 * @since 2016 */
@SuppressWarnings({ "static-method", "javadoc" })
public final class Issue0221 {
  @Test public void A$01() {
    trimmingOf("class D { int a; }")//
        .stays();
  }
  @Test public void A$02() {
    trimmingOf("class D { int a = 3; }")//
        .stays();
  }
  @Test public void A$03() {
    trimmingOf("class D{int a=0;}")//
        .gives("class D{int a;}");
  }
  @Test public void A$04() {
    trimmingOf("class D{Integer a=0;}")//
        .stays();
  }

  static class D {
    int i1, i2, i3 = 2, i4, i5 = 3;
    boolean b1, b2, b3 = true, b4;
    boolean b5;
    boolean b6 = true;
    short s1, s2, s3 = 4, s4;
    char c1, c2 = '\0', c3, c4 = 'a', c5 = 12;
    long l1, l2, l3 = '\0', l4, l5 = (long) 0.0, l6 = (int) 0.0F;
    float f1 = (float) 0.0, f2, f3 = 3, f4;
  }
}
