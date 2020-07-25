package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.trimmingOf;

import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.junit.Test;

/** Tests {@link VanillaCollection}
 * @author orimarco {@code marcovitch.ori@gmail.com}
 * @since 2017-03-07 */
@SuppressWarnings("static-method")
public class VanillaCollectionTest {
  @Test public void a() {
    trimmingOf("class C{public static final List<Int> li = new ArrayList<>();}")//
        .using(new VanillaCollection(), FieldDeclaration.class)//
        .gives("class C{}")//
        .stays();
  }
  @Test public void b() {
    trimmingOf("class C{public static final List<Int> li = new ArrayList<Int>();}")//
        .using(new VanillaCollection(), FieldDeclaration.class)//
        .gives("class C{}")//
        .stays();
  }
  @Test public void c() {
    trimmingOf("class C{public static final Set<Int> li = new HashSet<>();}")//
        .using(new VanillaCollection(), FieldDeclaration.class)//
        .gives("class C{}")//
        .stays();
  }
  @Test public void d() {
    trimmingOf("class C{public static final Set<Int> li = new TreeSet<>();}")//
        .using(new VanillaCollection(), FieldDeclaration.class)//
        .gives("class C{}")//
        .stays();
  }
  @Test public void e() {
    trimmingOf("class C{public static final Map<Int, String> li = new HashMap<>();}")//
        .using(new VanillaCollection(), FieldDeclaration.class)//
        .gives("class C{}")//
        .stays();
  }
}
