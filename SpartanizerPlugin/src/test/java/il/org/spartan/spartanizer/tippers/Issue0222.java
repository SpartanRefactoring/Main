package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.junit.*;
import org.junit.runners.*;

/** Unit tests for centification of a single parameter to a function even if it
 * defines a "$"variable
 * @author Yossi Gil {@code yossi dot (optional) gil at gmail dot (required) com}
 * @since 2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SuppressWarnings({ "static-method", "javadoc" })
public class Issue0222 {
  @Test public void chocolate1() {
    trimmingOf("L<E> os(I x){if(x==null)return null;int y=x;++y;" + " L<E> $=new A<>();$.a(l(x));$.a(r(x));if(x.h())" + " $.d(s.es(x));return $;}")//
        .stays();
  }

  @Test public void chocolate2() {
    trimmingOf("tc¢(L<F> ifs){for(F $ : fs){int b=f($);f($,b);return g($,b,f());}return true;}")//
        .stays();
  }

  @Test public void chocolate3() {
    trimmingOf("int f(int i){for(int b: fs)return 0;return 1;}").gives("int f(int __){for(int b: fs)return 0;return 1;}")//
        .stays();
  }

  @Ignore // TODO: Yossi Gil, I guess #1115 breaks this some how
  @Test public void vanilla() {
    trimmingOf("L<E> os(I x){if(x==null)return null;L<E> $=new A<>();$.a(l(x));$.a(r(x));if(x.h())$.d(s.es(x));return $;}")
        .gives("L<E> os(I ¢){if(¢==null)return null;L<E> $=new A<>();$.a(l(¢));$.a(r(¢));if(¢.h())$.d(s.es(¢));return $;}").stays();
  }
}
