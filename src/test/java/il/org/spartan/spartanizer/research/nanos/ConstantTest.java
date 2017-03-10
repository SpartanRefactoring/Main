package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.testing.TrimmerTestsUtils.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

/** Tests {@link Constant}
 * @author orimarco <tt>marcovitch.ori@gmail.com</tt>
 * @since 2017-02-09 */
@SuppressWarnings("static-method")
public class ConstantTest {
  @Test public void a() {
    trimmingOf("class C{public static final int a = 7;}")//
        .using(FieldDeclaration.class, new Constant())//
        .gives("class C{}")//
        .stays();
  }

  @Test public void b() {
    trimmingOf("class C{public static final Boolean a = 7;}")//
        .using(FieldDeclaration.class, new Constant())//
        .gives("class C{}")//
        .stays();
  }

  @Test public void c() {
    trimmingOf("class C{public static final Boolean a = true;"//
        + "public static final Integer b = 7;"//
        + "public static final char b = 7;"//
        + "public static final String b = \"fff\";"//
        + "}")//
            .using(FieldDeclaration.class, new Constant())//
            .gives("class C{}")//
            .stays();
  }

  @Test public void d() {
    trimmingOf("class C{public static final Boolean a = true;"//
        + "public static final Character b = 7;"//
        + "public static final Int b = 7;"//
        + "public static final char b = 7;"//
        + "public static final String b = \"fff\";"//
        + "}")//
            .using(FieldDeclaration.class, new Constant())//
            .gives("class C{public static final Int b = 7;}")//
            .stays();
  }
}
