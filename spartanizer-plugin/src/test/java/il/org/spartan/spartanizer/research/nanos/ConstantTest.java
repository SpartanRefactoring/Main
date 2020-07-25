package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.trimmingOf;

import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.junit.Test;

/** Tests {@link Constant}
 * @author orimarco {@code marcovitch.ori@gmail.com}
 * @since 2017-02-09 */
@SuppressWarnings("static-method")
public class ConstantTest {
  @Test public void a() {
    trimmingOf("class C{public static final int a = 7;}")//
        .using(new Constant(), FieldDeclaration.class)//
        .gives("class C{}")//
        .stays();
  }
  @Test public void b() {
    trimmingOf("class C{public static final Boolean a = 7;}")//
        .using(new Constant(), FieldDeclaration.class)//
        .gives("class C{}")//
        .stays();
  }
  @Test public void c() {
    trimmingOf("class C{public static final Boolean a = true;"//
        + "public static final Integer b = 7;"//
        + "public static final char b = 7;"//
        + "public static final String b = \"fff\";"//
        + "}")//
            .using(new Constant(), FieldDeclaration.class)//
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
            .using(new Constant(), FieldDeclaration.class)//
            .gives("class C{public static final Int b = 7;}")//
            .stays();
  }
}
