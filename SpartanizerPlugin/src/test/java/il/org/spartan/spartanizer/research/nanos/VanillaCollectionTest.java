package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.testing.TestsUtilsTrimmer.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

/** Tests {@link VanillaCollection}
 * @author orimarco {@code marcovitch.ori@gmail.com}
 * @since 2017-03-07 */
@SuppressWarnings("static-method")
public class VanillaCollectionTest {
  @Test public void a() {
    topDownTrimming("class C{public static final List<Int> li = new ArrayList<>();}")//
        .using(FieldDeclaration.class, new VanillaCollection())//
        .gives("class C{}")//
        .stays();
  }

  @Test public void b() {
    topDownTrimming("class C{public static final List<Int> li = new ArrayList<Int>();}")//
        .using(FieldDeclaration.class, new VanillaCollection())//
        .gives("class C{}")//
        .stays();
  }

  @Test public void c() {
    topDownTrimming("class C{public static final Set<Int> li = new HashSet<>();}")//
        .using(FieldDeclaration.class, new VanillaCollection())//
        .gives("class C{}")//
        .stays();
  }

  @Test public void d() {
    topDownTrimming("class C{public static final Set<Int> li = new TreeSet<>();}")//
        .using(FieldDeclaration.class, new VanillaCollection())//
        .gives("class C{}")//
        .stays();
  }

  @Test public void e() {
    topDownTrimming("class C{public static final Map<Int, String> li = new HashMap<>();}")//
        .using(FieldDeclaration.class, new VanillaCollection())//
        .gives("class C{}")//
        .stays();
  }
}
