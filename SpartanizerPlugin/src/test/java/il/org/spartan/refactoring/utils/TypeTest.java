package il.org.spartan.refactoring.utils;

import static il.org.spartan.azzert.*;
import org.eclipse.jdt.core.dom.*;
import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "javadoc", "static-method" }) //
public class TypeTest {
  @Ignore public static class Pending {
    @Test public void test00() {
      fail("Not yet implemented");
    }

    @Test public void test02() {
      azzert.that(Type.kind(Into.e("2 + (2.0)*1L")),is(Type.DOUBLE));
    }
  }

  public static class Working {
    @Test public void test01() {
      for (Type t : Type.values())
        System.err.println("Erase me after you figured this out\n\t" + t.fullName());
      azzert.that(Type.BOOLEAN, is(Type.BOOLEAN));
    }
  }
}
