package il.org.spartan.spartanizer.java.namespace;

import java.lang.annotation.*;

import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.spartanizer.ast.navigate.*;

/** Unit test of {@link Namespace}
 * @author Yossi Gil
 * @since 2016-12-15 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SuppressWarnings({ "javadoc" })
public class NamespaceTest extends ReflectiveTester {
  private final Namespace fixture = Environment.of(myCompilationUnit());

  @Test public void a01() {
    assert fixture != null;
  }

  @Test public void a02() {
    System.out.println(fixture.description());
  }
}

class NamespaceFixture {
  {
    int $ = 2;
    final int j = 0;
    $ *= j + $ * j + 2 * j;
    final int k = $;
    int l = j;
    $ *= k + $ * l + 2 * j;
    l = $ * k;
    @knows("k") int s = l + k * k * l;
    s *= s + 2;
    f(k + s * hashCode() + l + $ * s);
  }

  int f(final int a) {
    return a >>> hashCode();
  }

  class X {
    int b;
  }

  interface B {
    enum C {
      ;
      class D {
        int a;
      }
    }
  }
}
@Target({ ElementType.FIELD, //
  ElementType.PARAMETER, 
  ElementType.LOCAL_VARIABLE, //
  ElementType.METHOD,
  ElementType.ANNOTATION_TYPE,
  ElementType.CONSTRUCTOR,
  ElementType.TYPE,
  
})
@interface knows {
  String[] value();
}

@Target({ ElementType.FIELD, //
  ElementType.PARAMETER, 
  ElementType.LOCAL_VARIABLE, //
  ElementType.METHOD,
  ElementType.ANNOTATION_TYPE,
  ElementType.CONSTRUCTOR,
  ElementType.TYPE,
  
})
@interface foreign {
  String[] value();
}


interface xyz {
  enum a {
    a
  }
}
