package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.testing.TestsUtilsTrimmer.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

/** Tests {@link VanillaCollection}
 * @author orimarco <tt>marcovitch.ori@gmail.com</tt>
 * @since 2017-03-07 */
@SuppressWarnings("static-method")
public class VanillaCollectionTest {
  @Test public void a() {
    trimmingOf("class C{public static final List<Int> li = new ArrayList<>();}")//
        .using(FieldDeclaration.class, new VanillaCollection())//
        .gives("class C{}")//
        .stays();
  }

  @Test public void b() {
    trimmingOf("class C{public static final List<Int> li = new ArrayList<Int>();}")//
        .using(FieldDeclaration.class, new VanillaCollection())//
        .gives("class C{}")//
        .stays();
  }

  @Test public void c() {
    trimmingOf("class C{public static final Set<Int> li = new HashSet<>();}")//
        .using(FieldDeclaration.class, new VanillaCollection())//
        .gives("class C{}")//
        .stays();
  }

  @Test public void d() {
    trimmingOf("class C{public static final Set<Int> li = new TreeSet<>();}")//
        .using(FieldDeclaration.class, new VanillaCollection())//
        .gives("class C{}")//
        .stays();
  }

  @Test public void e() {
    trimmingOf("class C{public static final Map<Int, String> li = new HashMap<>();}")//
        .using(FieldDeclaration.class, new VanillaCollection())//
        .gives("class C{}")//
        .stays();
  }
}
