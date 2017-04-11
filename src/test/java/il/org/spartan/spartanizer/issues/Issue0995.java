package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsTrimmer.*;

import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.spartanizer.tippers.*;

/** Unit tests for {@link BodyDeclarationModifiersSort.ofEnumConstant}
 * @author Yossi Gil // put your name here
 * @since 2016 // put the year/date here */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SuppressWarnings({ "static-method", "javadoc" })
public class Issue0995 {
  @Test public void a$01() {
    trimminKof("enum A{@c @b a}")//
        .gives("enum A{@b @c a}")//
        .stays();
  }
}
