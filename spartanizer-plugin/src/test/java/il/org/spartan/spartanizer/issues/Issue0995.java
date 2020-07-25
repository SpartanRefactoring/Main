package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.trimmingOf;

import org.junit.Test;

import il.org.spartan.spartanizer.tippers.BodyDeclarationModifiersSort;

/** Unit tests for {@link BodyDeclarationModifiersSort.ofEnumConstant}
 * @author Yossi Gil // put your name here
 * @since 2016 // put the year/date here */
@SuppressWarnings({ "static-method", "javadoc" })
public class Issue0995 {
  @Test public void a$01() {
    trimmingOf("enum A{@c @b a}")//
        .gives("enum A{@b @c a}")//
        .stays();
  }
}
