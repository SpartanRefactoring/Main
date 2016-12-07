package il.org.spartan.spartanizer.research;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.spartanizer.research.patterns.*;

/** @author Ori Marcovitch
 * @since 2016 */
@SuppressWarnings("static-method")
public class TernaryBitOperationTest {
  @Test public void a() {
    trimmingOf("(k == 0) ? 1 : 0").withTipper(ConditionalExpression.class, new TernaryBitOperation()).gives("Bit.not(k)");
  }

  @Test public void b() {
    trimmingOf("k == 0 ? 1 : 0").withTipper(ConditionalExpression.class, new TernaryBitOperation()).gives("Bit.not(k)");
  }

  @Test public void c() {
    trimmingOf("(0 == x(f,g,h.h(a,b,moo))) ? 1 : 0").withTipper(ConditionalExpression.class, new TernaryBitOperation())
        .gives("Bit.not(x(f,g,h.h(a,b,moo)))");
  }

  @Test public void d() {
    trimmingOf("0 == x(f,g,h.h(a,b,moo)) ? 1 : 0").withTipper(ConditionalExpression.class, new TernaryBitOperation())
        .gives("Bit.not(x(f,g,h.h(a,b,moo)))");
  }

  @Test public void e() {
    trimmingOf("k == 0 ? 0 : 1").withTipper(ConditionalExpression.class, new TernaryBitOperation()).gives("Bit.of(k)");
  }

  @Test public void f() {
    trimmingOf("(k == 0) ? 0 : 1").withTipper(ConditionalExpression.class, new TernaryBitOperation()).gives("Bit.of(k)");
  }

  @Test public void g() {
    trimmingOf("(0 == x(f,g,h.h(a,b,moo))) ? 0 : 1").withTipper(ConditionalExpression.class, new TernaryBitOperation())
        .gives("Bit.of(x(f,g,h.h(a,b,moo)))");
  }

  @Test public void h() {
    trimmingOf("0 == x(f,g,h.h(a,b,moo)) ? 0 : 1").withTipper(ConditionalExpression.class, new TernaryBitOperation())
        .gives("Bit.of(x(f,g,h.h(a,b,moo)))");
  }
}
