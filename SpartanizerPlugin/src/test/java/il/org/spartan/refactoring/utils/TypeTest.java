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
      azzert.that(Kind.kind(Into.e("2 + (2.0)*1L")),is(Kind.DOUBLE));
    }
  }

  public static class Working {
    @Test public void test01() {
      for (Kind k : Kind.values())
        System.err.println("Erase me after you figured this out\n\t" + k.fullName());
      azzert.that(Kind.BOOLEAN, is(Kind.BOOLEAN));
    }
  }
}
