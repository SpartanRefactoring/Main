package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

/** Tests {@link VanillaCollection}
 * @author orimarco {@code marcovitch.ori@gmail.com}
 * @since 2017-03-07 */
@SuppressWarnings("static-method")
public class VanillaCollectionTest {
  @Test public void a() {
    trimminKof("class C{public static final List<Int> li = an.empty.list();}")//
        .using(new VanillaCollection(), FieldDeclaration.class)//
        .gives("class C{}")//
        .stays();
  }

  @Test public void b() {
    trimminKof("class C{public static final List<Int> li = new ArrayList<Int>();}")//
        .using(new VanillaCollection(), FieldDeclaration.class)//
        .gives("class C{}")//
        .stays();
  }

  @Test public void c() {
    trimminKof("class C{public static final Set<Int> li = new HashSet<>();}")//
        .using(new VanillaCollection(), FieldDeclaration.class)//
        .gives("class C{}")//
        .stays();
  }

  @Test public void d() {
    trimminKof("class C{public static final Set<Int> li = new TreeSet<>();}")//
        .using(new VanillaCollection(), FieldDeclaration.class)//
        .gives("class C{}")//
        .stays();
  }

  @Test public void e() {
    trimminKof("class C{public static final Map<Int, String> li = new HashMap<>();}")//
        .using(new VanillaCollection(), FieldDeclaration.class)//
        .gives("class C{}")//
        .stays();
  }
}
