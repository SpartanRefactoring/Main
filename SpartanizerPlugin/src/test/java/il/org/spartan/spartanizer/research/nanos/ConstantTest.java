package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.testing.TestsUtilsTrimmer.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

/** Tests {@link Constant}
 * @author orimarco {@code marcovitch.ori@gmail.com}
 * @since 2017-02-09 */
@SuppressWarnings("static-method")
public class ConstantTest {
  @Test public void a() {
    topDownTrimming("class C{public static final int a = 7;}")//
        .using(FieldDeclaration.class, new Constant())//
        .gives("class C{}")//
        .stays();
  }

  @Test public void b() {
    topDownTrimming("class C{public static final Boolean a = 7;}")//
        .using(FieldDeclaration.class, new Constant())//
        .gives("class C{}")//
        .stays();
  }

  @Test public void c() {
    topDownTrimming("class C{public static final Boolean a = true;"//
        + "public static final Integer b = 7;"//
        + "public static final char b = 7;"//
        + "public static final String b = \"fff\";"//
        + "}")//
            .using(FieldDeclaration.class, new Constant())//
            .gives("class C{}")//
            .stays();
  }

  @Test public void d() {
    topDownTrimming("class C{public static final Boolean a = true;"//
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
