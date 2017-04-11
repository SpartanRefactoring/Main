package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsTrimmer.*;

import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.spartanizer.tippers.*;

/** Tests for {@link AssignmentToFromInfixIncludingTo}
 * @author Yossi Gil
 * @since 2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SuppressWarnings({ "static-method", "javadoc" })
public class Issue0103 {
  @Test public void _AND1() {
    topDownTrimming("a=a&5;")//
        .gives("a&=5;");
  }

  @Test public void _AND2() {
    topDownTrimming("a=5&a;")//
        .gives("a&=5;");
  }

  @Test public void _div1() {
    topDownTrimming("a=a/5;")//
        .gives("a/=5;");
  }

  @Test public void _div2() {
    topDownTrimming("a=5/a;")//
        .stays();
  }

  @Test public void _leftShift1() {
    topDownTrimming("a=a<<5;")//
        .gives("a<<=5;");
  }

  @Test public void _leftShift2() {
    topDownTrimming("a=5<<a;")//
        .stays();
  }

  @Test public void _modulo1() {
    topDownTrimming("a=a%5;")//
        .gives("a%=5;");
  }

  @Test public void _modulo2() {
    topDownTrimming("a=5%a;")//
        .stays();
  }

  @Test public void _OR1() {
    topDownTrimming("a=a|5;")//
        .gives("a|=5;");
  }

  @Test public void _OR2() {
    topDownTrimming("a=5|a;")//
        .gives("a|=5;");
  }

  @Test public void _rightShift1() {
    topDownTrimming("a=a>>5;")//
        .gives("a>>=5;");
  }

  @Test public void _rightShift2() {
    topDownTrimming("a=5>>a;")//
        .stays();
  }

  @Test public void _XOR1() {
    topDownTrimming("x = x ^ a.getNum()")//
        .gives("x ^= a.getNum()");
  }

  @Test public void _XOR2() {
    topDownTrimming("j = j ^ k")//
        .gives("j ^= k");
  }

  @Test public void b() {
    topDownTrimming("x=y+x")//
        .stays();
  }

  @Test public void c() {
    topDownTrimming("x=y+z")//
        .stays();
  }

  public void d() {
    topDownTrimming("x = x + x")//
        .gives("x+=x");
  }

  public void e() {
    topDownTrimming("x = y + x + z + x + k + 9")//
        .gives("x += y + z + x + k + 9");
  }

  @Test public void mma() {
    topDownTrimming("x=x*y")//
        .gives("x*=y");
  }

  static class NotWorking {
    @Test public void a() {
      topDownTrimming("x=x+y")//
          .gives("x+=y");
    }

    @Test public void f() {
      topDownTrimming("a=a+5")//
          .gives("a+=5");
    }

    @Test public void g() {
      topDownTrimming("a=a+(alex)")//
          .gives("a+=alex");
    }

    @Test public void h() {
      topDownTrimming("a = a + (c = c + kif)")//
          .gives("a += c = c + kif")//
          .gives("a += c += kif")//
          .stays();
    }

    @Test public void i_mixed_associative() {
      topDownTrimming("a = x = x + (y = y*(z=z+3))")//
          .gives("a = x += y=y*(z=z+3)")//
          .gives("a = x += y *= z=z+3")//
          .gives("a = x += y *= z+=3");
    }

    @Test public void j() {
      topDownTrimming("x=x+foo(x,y)")//
          .gives("x+=foo(x,y)");
    }

    @Test public void k() {
      topDownTrimming("z=foo(x=(y=y+u),17)")//
          .gives("z=foo(x=(y+=u),17)");
    }

    @Test public void l_mixed_associative() {
      topDownTrimming("a = a - (x = x + (y = y*(z=z+3)))")//
          .gives("a-=x=x+(y=y*(z=z+3))")//
          .gives("a-=x+=y=y*(z=z+3)");
    }
  }
}
